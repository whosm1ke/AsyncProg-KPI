package PW_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WordCount {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String text = "Word is Word where word is not Word";
        List<String> parts = Arrays.asList(
                text.substring(0, text.length() / 2),
                text.substring(text.length() / 2)
        );

        ConcurrentHashMap<String, Integer> wordCounts = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<Void>> futures = new ArrayList<>();

        for (String part : parts) {
            Callable<Void> task = () -> {
                String[] words = part.split("\\W+");
                for (String word : words) {
                    wordCounts.merge(word.toLowerCase(), 1, Integer::sum);
                }
                return null;
            };
            futures.add(executor.submit(task));
        }

        for (Future<Void> future : futures) {
            while (!future.isDone()) {
                // You can perform other tasks here if needed
                System.out.println("It is not done yet");
            }
            future.get(); // Ensure the task is completed
        }

        executor.shutdown();

        // Print the word counts
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

//ConcurrentHashMap: Використовується для зберігання частоти слів, забезпечуючи безпечний доступ з кількох потоків.
//Callable: Використовується для підрахунку слів у кожній частині тексту.
//Future: Використовується для збору результатів підрахунку слів.
//isDone(): Метод використовується для перевірки завершення завдання.
//Ця програма розділяє текст на дві частини, створює два завдання для підрахунку слів і об’єднує
// результати в ConcurrentHashMap. Можна змінити кількість частин тексту та кількість потоків
// у пулі відповідно потреб.