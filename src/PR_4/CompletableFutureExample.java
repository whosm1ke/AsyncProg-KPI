package PR_4;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Генерація масиву асинхронно
        CompletableFuture<int[]> initialArrayFuture = CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            int[] array = new Random().ints(10, 1, 100).toArray();
            System.out.println("Initial array: " + Arrays.toString(array));
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken for initial array generation: " + (endTime - startTime) + " ms");
            return array;
        });

        // Збільшення кожного елементу масиву на 5 асинхронно
        CompletableFuture<int[]> incrementedArrayFuture = initialArrayFuture.thenApplyAsync(array -> {
            long startTime = System.currentTimeMillis();
            int[] incrementedArray = Arrays.stream(array).map(i -> i + 5).toArray();
            System.out.println("Incremented array: " + Arrays.toString(incrementedArray));
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken for incrementing array: " + (endTime - startTime) + " ms");
            return incrementedArray;
        });

        // Обчислення факторіалу асинхронно
        CompletableFuture<BigInteger> factorialFuture = incrementedArrayFuture.thenCombineAsync(initialArrayFuture, (incArray, initArray) -> {
            long startTime = System.currentTimeMillis();
            int sumIncArray = Arrays.stream(incArray).sum();
            int sumInitArray = Arrays.stream(initArray).sum();
            int totalSum = sumIncArray + sumInitArray;
            BigInteger factorialResult = factorial(totalSum);
            long endTime = System.currentTimeMillis();
            System.out.println("Factorial result: " + factorialResult);
            System.out.println("Time taken for factorial calculation: " + (endTime - startTime) + " ms");
            return factorialResult;
        });

        // Очікування завершення всіх асинхронних задач
        factorialFuture.get();
    }

    private static BigInteger factorial(int n) {
        if (n == 0) return BigInteger.ONE;
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}