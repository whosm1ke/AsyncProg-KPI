package PR_4;

public class DataTimeWrapper {
    private final DataWrapper dataWrapper;
    private final TimeWrapper timeWrapper;

    public DataTimeWrapper(DataWrapper dataWrapper, TimeWrapper timeWrapper) {
        this.dataWrapper = dataWrapper;
        this.timeWrapper = timeWrapper;
    }

    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    public TimeWrapper getTimeWrapper() {
        return timeWrapper;
    }
}