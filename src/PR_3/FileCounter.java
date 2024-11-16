package PR_3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class FileCounter {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);

        // Введення користувачем параметрів
        System.out.print("Введіть відносний шлях до директорії: ");
        String directoryPath = scanner.nextLine();
        System.out.print("Введіть формат файлів (наприклад, .pdf): ");
        String fileFormat = scanner.nextLine();

        // Виконання через Work Stealing
        long startTime = System.currentTimeMillis();
        int fileCount;
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            fileCount = forkJoinPool.invoke(new FileSearchTask(new File(directoryPath), fileFormat));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Кількість файлів формату " + fileFormat + ": " + fileCount);
        System.out.println("Час виконання: " + (endTime - startTime) + " ms");
    }
}

class FileSearchTask extends RecursiveTask<Integer> {
    private final File directory;
    private final String fileFormat;

    public FileSearchTask(File directory, String fileFormat) {
        this.directory = directory;
        this.fileFormat = fileFormat;
    }

    @Override
    protected Integer compute() {
        int count = 0;
        File[] files = directory.listFiles();

        if (files != null) {
            List<FileSearchTask> subTasks = new ArrayList<>();

            for (File file : files) {
                if (file.isDirectory()) {
                    FileSearchTask subTask = new FileSearchTask(file, fileFormat);
                    subTask.fork();
                    subTasks.add(subTask);
                } else if (file.getName().endsWith(fileFormat)) {
                    count++;
                }
            }

            for (FileSearchTask subTask : subTasks) {
                count += subTask.join();
            }
        }

        return count;
    }
}