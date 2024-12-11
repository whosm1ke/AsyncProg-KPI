package PR_4;

public class DataWrapper {
    private final int[] sequence;
    private final int sum;

    public DataWrapper(int[] sequence, int sum) {
        this.sequence = sequence;
        this.sum = sum;
    }

    public int[] getSequence() {
        return sequence;
    }

    public int getSum() {
        return sum;
    }
}