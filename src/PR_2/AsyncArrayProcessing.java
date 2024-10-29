package PR_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class AsyncArrayProcessing {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);

        // Введення множника
        System.out.print("Введіть множник: ");
        int multiplier = scanner.nextInt();

        // Введення діапазону
        System.out.print("Введіть мінімальне значення діапазону: ");
        int minRange = scanner.nextInt();
        System.out.print("Введіть максимальне значення діапазону: ");
        int maxRange = scanner.nextInt();

        int arraySize = new Random().nextInt(21) + 40; // Розмір масиву від 40 до 60
        int[] array = new int[arraySize];

        // Заповнення масиву випадковими числами
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(maxRange - minRange + 1) + minRange;
        }

        // Виведення початкового масиву
        System.out.println("Початковий масив: ");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        // Розбиття масиву на частини
        int chunkSize = 10; // Розмір частини
        List<Future<int[]>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(array.length / chunkSize + 1);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < array.length; i += chunkSize) {
            int[] chunk = new int[Math.min(chunkSize, array.length - i)];
            System.arraycopy(array, i, chunk, 0, chunk.length);
            Callable<int[]> task = new ArrayMultiplier(chunk, multiplier);
            futures.add(executor.submit(task));
        }

        // Збір результатів
        List<Integer> resultList = new CopyOnWriteArrayList<>();
        for (Future<int[]> future : futures) {
            while (!future.isDone()) {
                System.out.println("Я рахую, а ти зроби щось корисне поки!1!");
            }
            if (!future.isCancelled()) {
                int[] resultChunk = future.get();
                for (int num : resultChunk) {
                    resultList.add(num);
                }
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();

        // Виведення результату
        System.out.println("Результат: " + resultList);
        System.out.printf("Результат на рівність довжин %d == %d = %b%n", array.length, resultList.size(), array.length == resultList.size());
        System.out.println("Час виконання програми: " + (endTime - startTime) + " мс");
    }
}

