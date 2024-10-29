package PR_2;

import java.util.concurrent.Callable;

public class ArrayMultiplier implements Callable<int[]> {
    private final int[] array;
    private final int multiplier;

    public ArrayMultiplier(int[] array, int multiplier) {
        this.array = array;
        this.multiplier = multiplier;
    }

    @Override
    public int[] call() {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] * multiplier;
        }
        return result;
    }
}
