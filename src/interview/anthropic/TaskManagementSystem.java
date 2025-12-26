package interview.anthropic;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TaskManagementSystem {
    private final Map<String, Task> tasks;
    private final Map<String, User> users;
    private final Comparator<Task> taskComparator;
    private final Map<String, TaskAssignmentRecord> assignmentHistory;
    private final PriorityQueue<ScheduledDeletion> deletionQueue;
    private static final int MAX_TIMESTAMP = 1000000000;
    private int currentTimestamp;

    private static class ScheduledDeletion implements Comparable<ScheduledDeletion>{
        @SuppressWarnings("unused")
        String taskId;
        int deletionTime;

        @SuppressWarnings("unused")
        ScheduledDeletion(String taskId, int deletionTime){
            this.taskId = taskId;
            this.deletionTime = deletionTime;
        }

        @Override
        public int compareTo(ScheduledDeletion other){
            return Integer.compare(this.deletionTime, other.deletionTime);
        }
    }

    public TaskManagementSystem(){
        this(1);
    }

    public TaskManagementSystem(int currentTimestamp){
        this.tasks = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.deletionQueue = new PriorityQueue<>();
        this.assignmentHistory = new HashMap<>();
        this.taskComparator = Comparator.comparingInt(Task::getPriority).reversed().thenComparingInt(Task::getTimestamp);
        this.currentTimestamp = currentTimestamp;
    }

    public int getCurrentTimestamp(){
        return this.currentTimestamp;
    }

    public void advanceTime(int newTime){
        if(newTime < currentTimestamp){
            throw new IllegalArgumentException("Cannot move time backwards");
        }

        if(newTime > MAX_TIMESTAMP){
            throw new IllegalArgumentException("Cannot move time beyond 10^9");
        }

        this.currentTimestamp = newTime;
    }

    public Task createTask(String taskId, String title, String description){
        if(this.tasks.containsKey(taskId)){
            throw new IllegalArgumentException("Task already exist!");
        }
        
        Task task = new Task(taskId, title, description, getCurrentTimestamp());
        tasks.put(taskId, task);
        return task;
    }

    public Task getTask(String taskId){
        Task task = this.tasks.get(taskId);

        if(task == null){
            throw new IllegalArgumentException("Task does not exist");
        }
        return task;
    }

    public List<Task> getTasks(){
        return new ArrayList<>(this.tasks.values());
    }

    public Task updateTask(String taskId, String title, String description){
        Task task = this.tasks.get(taskId);
        if(task == null){
            throw new IllegalArgumentException("Task does not exist");
        }

        task.setTitle(title);
        task.setDescription(description);
        this.tasks.put(taskId, task);

        return task;
    }

    public boolean deleteTask(String taskId){
        Task task = this.tasks.remove(taskId);
        if(task != null && task.getAssignedUserId() != null){
            User user = this.users.get(task.getAssignedUserId());
            if(user != null){
                user.decrementTaskCount();
                assignmentHistory.put(task.getAssignedUserId(), 
                    new TaskAssignmentRecord(task.getAssignedUserId(), taskId, getCurrentTimestamp(), false));
            }
        }
        return task != null;
    }

    public boolean setTaskPriority(int timestamp, String taskId, int priority){
        Task task = this.tasks.get(taskId);
        if(task == null)
            return false;

        task.setTimestamp(timestamp);
        task.setPriority(priority);
        this.tasks.put(taskId, task);

        return true;
    }

    public List<Task> getTasksBeforeTimestamp(int timestamp){
        return tasks.values().stream()
            .filter(t -> t.getTimestamp() <= timestamp)
            .sorted(this.taskComparator)
            .collect(Collectors.toList());
    }

    public User createUser(String userId, String name, int taskQuota){
        if(this.users.containsKey(userId)){
            throw new IllegalArgumentException("User already exist!");
        }

        User user = new User(userId, name, taskQuota);
        this.users.put(userId, user);
        return user;
    }

    public User getUser(String userId){
        return this.users.get(userId);
    }

    public User updateUserQuota(String userId, int newTaskQuota){
        if(!this.users.containsKey(userId)){
            throw new IllegalArgumentException("User does not exist!");
        }
        User user = this.users.get(userId);
        user.setTaskQuota(newTaskQuota);
        return user;
    }

    // ******** Task Assignment **********
    public void assignTask(String taskId, String userId){
        Task task = this.tasks.get(taskId);
        User user = this.users.get(userId);

        if(task == null){
            throw new IllegalArgumentException("Task not found!!");
        }

        if(user == null){
            throw new IllegalArgumentException("User not found!!");
        }

        if(!user.canAcceptsTasks()){
            throw new IllegalArgumentException("User does not have available quota");
        }

        if(task.getAssignedUserId() != null){
            User prevUser = this.users.get(task.getAssignedUserId());
            if(prevUser != null){
                prevUser.decrementTaskCount();
                assignmentHistory.put(prevUser.getUserId(), new TaskAssignmentRecord(prevUser.getUserId(), taskId, getCurrentTimestamp(), false));
            }
        }

        task.setAssignedUserId(userId);
        user.incrementTaskCount();
        assignmentHistory.put(userId, new TaskAssignmentRecord(userId, taskId, getCurrentTimestamp(), true));
    }

    // ******* Schedule Deletion *************
    public void scheduleTaskDeletion(String taskId, int deletionTime){
        Task task = this.tasks.get(taskId);
        if(task == null){
            throw new IllegalArgumentException("Task not found!!");
        }

        if(deletionTime < this.getCurrentTimestamp()){
            throw new IllegalArgumentException("Could not schedule deletion back in time");
        }

        if(deletionTime > MAX_TIMESTAMP){
            throw new IllegalArgumentException("Could not schedule deletion for time > 10^9");
        }

        task.setScheduledDeletionTimestamp(deletionTime);
        this.deletionQueue.offer(new ScheduledDeletion(taskId, deletionTime));
    }
    public static void main(String args[]){
        TaskManagementSystem tms = new TaskManagementSystem(100); // Start at time 100
        
        System.out.println("=== Task Management System Demo ===\n");
        System.out.println("Starting at time: " + tms.getCurrentTimestamp());
        
        // 1. Create tasks
        System.out.println("\n1. Creating tasks at time " + tms.getCurrentTimestamp() + "...");
        tms.createTask("T1", "Fix bug in login", "Critical bug");
        tms.setTaskPriority(tms.getCurrentTimestamp(), "T1", 10);
        
        tms.advanceTime(110);
        tms.createTask("T2", "Update documentation", "User guide");
        tms.setTaskPriority(tms.getCurrentTimestamp(), "T2", 5);
        
        tms.advanceTime(120);
        tms.createTask("T3", "Code review", "Review PR #123");
        tms.setTaskPriority(tms.getCurrentTimestamp(), "T3", 8);
        
        tms.advanceTime(130);
        tms.createTask("T4", "Database optimization", "Index performance");
        tms.setTaskPriority(tms.getCurrentTimestamp(), "T4", 9);

        // 2. Display tasks
        System.out.println(tms.getTasks());

        // 3. Display tasks before timestamp
        System.out.println(tms.getTasksBeforeTimestamp(119));

        // 4. Create users with quotas
        System.out.println("\n3. Creating users...");
        tms.createUser("U1", "Alice", 2);
        tms.createUser("U2", "Bob", 3);
        System.out.println(tms.getUser("U1"));
        System.out.println(tms.getUser("U2"));
        
        // 4. Assign tasks to users
        System.out.println("\n4. Assigning tasks to users...");
        tms.advanceTime(150);
        int time1 = tms.getCurrentTimestamp();
        tms.assignTask("T1", "U1");
        
        tms.advanceTime(160);
        int time2 = tms.getCurrentTimestamp();
        tms.assignTask("T2", "U1");
        
        tms.advanceTime(170);
        int time3 = tms.getCurrentTimestamp();
        tms.assignTask("T3", "U2");
        
        System.out.println("Alice's tasks: " + tms.getUser("U1").getTaskCount());
        System.out.println("Bob's tasks: " + tms.getUser("U2").getTaskCount());

        // 5. Try to exceed quota
        System.out.println("\n5. Testing quota enforcement...");
        try {
            tms.assignTask("T4", "U1"); // Should fail - quota exceeded
        } catch (IllegalStateException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
        
    }
}
