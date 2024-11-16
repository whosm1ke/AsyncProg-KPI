package PR_3;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;


/*
Executor Service: Це інтерфейс, який надає методи для керування асинхронними завданнями.
 Він дозволяє запускати, зупиняти та керувати пулом потоків.

Thread Pool: Це набір потоків, які використовуються для виконання завдань.
 Пул потоків дозволяє ефективно керувати ресурсами, зменшуючи накладні
 витрати на створення нових потоків.

Fork/Join Framework: Це фреймворк для рекурсивного поділу завдань на підзадачі, які можуть
 виконуватись паралельно. Він використовує алгоритм work-stealing для
 балансування навантаження між потоками.

Work Stealing: Це підхід, при якому потоки, що завершили свої завдання,
 можуть "вкрасти" завдання у інших потоків, які ще зайняті.
  Це допомагає ефективно використовувати ресурси.

Work Dealing: Це підхід, при якому завдання розподіляються між потоками заздалегідь.
 Кожен потік отримує свою частину роботи і виконує її незалежно від інших потоків.
 */


public class ParallelSum {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);

        // Введення користувачем параметрів масиву
        System.out.print("Введіть кількість елементів масиву: ");
        int size = scanner.nextInt();
        System.out.print("Введіть початкове значення для генерації випадкових чисел: ");
        int startValue = scanner.nextInt();
        System.out.print("Введіть кінцеве значення для генерації випадкових чисел: ");
        int endValue = scanner.nextInt();

        int[] array = new int[size];
        Random random = new Random();

        // Генерація випадкових значень для масиву
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(endValue - startValue + 1) + startValue;
        }

        // Виведення згенерованого масиву
        System.out.println("Generated array:");
        for (int i = 0; i < size; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();

        // Виконання через Work Stealing
        long startTime = System.currentTimeMillis();
        int workStealingResult;
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            workStealingResult = forkJoinPool.invoke(new WorkStealingTask(array, 0, size));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Work Stealing Result: " + workStealingResult);
        System.out.println("Work Stealing Time: " + (endTime - startTime) + " ms");

        // Виконання через Work Dealing
        startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<Integer> workDealingResult = executorService.submit(new WorkDealingTask(array, 0, size));
        int result = workDealingResult.get();
        endTime = System.currentTimeMillis();
        System.out.println("Work Dealing Result: " + result);
        System.out.println("Work Dealing Time: " + (endTime - startTime) + " ms");

        executorService.shutdown();
    }
}

// Завдання для Work Stealing
class WorkStealingTask extends RecursiveTask<Integer> {
    private final int[] array;
    private final int start;
    private final int end;

    public WorkStealingTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    /*
    Базовий випадок: Якщо підмасив має 1000 або менше елементів,
    обчислюється попарна сума елементів у цьому підмасиві.

    Рекурсивний випадок: Якщо підмасив більший за 1000 елементів, він ділиться на дві частини:
    Створюються два нових завдання WorkStealingTask для лівої та правої частин підмасиву.
    Завдання для лівої частини (leftTask) виконується асинхронно за допомогою методу fork().
    Завдання для правої частини (rightTask) виконується в поточному потоці за допомогою методу compute().
    Результати обох завдань об'єднуються за допомогою методу join().
    */
    @Override
    protected Integer compute() {
        if (end - start <= 1000) {
            int sum = 0;
            for (int i = start; i < end - 1; i++) {
                sum += array[i] + array[i + 1];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            WorkStealingTask leftTask = new WorkStealingTask(array, start, mid);
            WorkStealingTask rightTask = new WorkStealingTask(array, mid, end);
            leftTask.fork();
            return rightTask.compute() + leftTask.join();
        }
    }
}

// Завдання для Work Dealing
class WorkDealingTask implements Callable<Integer> {
    private final int[] array;
    private final int start;
    private final int end;

    public WorkDealingTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() {
        int sum = 0;
        for (int i = start; i < end - 1; i++) {
            sum += array[i] + array[i + 1];
        }
        return sum;
    }
}