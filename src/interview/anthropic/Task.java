package interview.anthropic;

public class Task {
    private final String taskId;
    private String assignedUserId;
    private String title;
    private String description;
    private int priority;
    private int timestamp;
    private int scheduledDeletionTimestamp;

    public Task(String taskId, String title, String description, int timestamp){
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.priority = 0;
    }

    public String getTaskId(){
        return this.taskId;
    }

    public String getAssignedUserId(){
        return this.assignedUserId;
    }

    public void setAssignedUserId(String userId){
        this.assignedUserId = userId;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getTimestamp(){
        return this.timestamp;
    }

    public void setTimestamp(int timestamp){
        this.timestamp = timestamp;
    }

    public int getScheduledDeletionTimestamp(){
        return this.scheduledDeletionTimestamp;
    }

    public void setScheduledDeletionTimestamp(int timestamp){
        this.scheduledDeletionTimestamp = timestamp;
    }
    @Override
    public String toString(){
        return String.format("\nTask{taskId='%s', title='%s', description='%s', timestamp=%s, priority=%s}", taskId, title, description, timestamp, priority);
    }
}
