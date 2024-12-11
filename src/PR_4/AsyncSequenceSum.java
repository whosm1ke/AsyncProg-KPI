package PR_4;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncSequenceSum {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        variant2();
    }

    private static void variant2() throws ExecutionException, InterruptedException {
        // Генерація послідовності асинхронно
        CompletableFuture<DataTimeWrapper> sequenceFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            int[] sequence = new Random().ints(20, 1, 101).toArray();
            long end = System.currentTimeMillis();
            System.out.println("Generated sequence: " + Arrays.toString(sequence));
            System.out.println("Time taken for sequence generation: " + (end - start) + " ms");
            return new DataTimeWrapper(new DataWrapper(sequence, 0), new TimeWrapper(start, end));
        });

        // Обчислення суми асинхронно
        CompletableFuture<DataTimeWrapper> sumFuture = sequenceFuture.thenApplyAsync(dataTimeWrapper -> {
            long start = System.currentTimeMillis();
            int sum = Arrays.stream(dataTimeWrapper.getDataWrapper().getSequence()).sum();
            long end = System.currentTimeMillis();
            System.out.println("Sum of sequence: " + sum);
            System.out.println("Time taken for sum calculation: " + (end - start) + " ms");
            return new DataTimeWrapper(new DataWrapper(dataTimeWrapper.getDataWrapper().getSequence(), sum), new TimeWrapper(start, end));
        });

        // Виведення результатів асинхронно
        sumFuture.thenAcceptAsync(dataTimeWrapper -> {
            long totalTimeTaken = dataTimeWrapper.getTimeWrapper().getDuration() + sequenceFuture.join().getTimeWrapper().getDuration();
            System.out.println("Total time taken for all asynchronous operations: " + totalTimeTaken + " ms");
        }).get(); // Очікування завершення всіх асинхронних задач
    }

    private static void variant1() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        // Генерація послідовності асинхронно
        CompletableFuture<int[]> sequenceFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            int[] sequence = new Random().ints(20, 1, 101).toArray();
            System.out.println("Generated sequence: " + Arrays.toString(sequence));
            long end = System.currentTimeMillis();
            System.out.println("Time taken for sequence generation: " + (end - start) + " ms");
            return sequence;
        });

        // Обчислення суми асинхронно
        CompletableFuture<Integer> sumFuture = sequenceFuture.thenApplyAsync(sequence -> {
            long start = System.currentTimeMillis();
            int sum = Arrays.stream(sequence).sum();
            System.out.println("Sum of sequence: " + sum);
            long end = System.currentTimeMillis();
            System.out.println("Time taken for sum calculation: " + (end - start) + " ms");
            return sum;
        });

        // Виведення результатів асинхронно
        sumFuture.thenAcceptAsync(sum -> {
            long endTime = System.currentTimeMillis();
            long totalTimeTaken = endTime - startTime;
            System.out.println("Total time taken for all asynchronous operations: " + (totalTimeTaken) + " ms");
        }).get(); // Очікування завершення всіх асинхронних задач
    }
}