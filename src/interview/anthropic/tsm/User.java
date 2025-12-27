package interview.anthropic.tsm;

public class User {
    private final String userId;
    private String name;
    private int taskQuota;
    private int taskCount;

    public User(String userId, String name, int taskQuota){
        this.userId = userId;
        this.name = name;
        this.taskQuota = taskQuota;
        this.taskCount = 0;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getTaskQuota(){
        return this.taskQuota;
    }

    public void setTaskQuota(int taskQuota){
        this.taskQuota = taskQuota;
    }

    public int getTaskCount(){
        return this.taskCount;
    }

    public void setTaskCount(int taskCount){
        this.taskCount = taskCount;
    }

    public boolean canAcceptsTasks(){
        return this.taskCount < this.taskQuota;
    }

    public void incrementTaskCount(){
        this.taskCount++;
    }

    public void decrementTaskCount(){
        this.taskCount--;
    }

    @Override
    public String toString(){
        return String.format("\nUser{userId='%s', name='%s', taskQuota=%d, taskCount=%d}", userId, name, taskQuota, taskCount);
    }
}
