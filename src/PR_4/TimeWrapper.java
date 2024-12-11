package PR_4;

public class TimeWrapper {
    private final long startTime;
    private final long endTime;

    public TimeWrapper(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getDuration() {
        return endTime - startTime;
    }
}
