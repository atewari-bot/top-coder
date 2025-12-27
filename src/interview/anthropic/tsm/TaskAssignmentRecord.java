package interview.anthropic.tsm;

public class TaskAssignmentRecord {
    private final String userId;
    private final String taskId;
    private final int timestamp;
    private final boolean isAssgined;

    public TaskAssignmentRecord(String userId, String taskId, int timestamp, boolean isAssgined){
        this.userId = userId;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.isAssgined = isAssgined;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getTaskId(){
        return this.taskId;
    }

    public int getTimestamp(){
        return this.timestamp;
    }

    public boolean isAssgined(){
        return this.isAssgined;
    }
}
