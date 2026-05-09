import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    static String newDir(String[] dirs, File file) {
        StringBuilder result = new StringBuilder();
        for (String dir : dirs) {
            File newDir = new File(file, dir);
            if (newDir.mkdir()) {
                result.append("Создана директория " + newDir.getAbsolutePath() + "\n");
            } else {
                result.append("Не удалось создать директорию " + newDir.getAbsolutePath() + "\n");
            }
        }
        return result.toString();
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(gameProgress);
            System.out.println("Сохранение создано: " + path);

        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла " + path + ": " + e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> files) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : files) {
                File file = new File(filePath);

                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
                System.out.println("Добавлен в архив: " + file.getName());
            }

            System.out.println("Архив создан: " + zipPath);

        } catch (IOException e) {
            System.out.println("Ошибка при создании архива: " + e.getMessage());
        }
    }

    public static void deleteFiles(List<String> files) {
        for (String filePath : files) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Удален файл: " + filePath);
            } else {
                System.out.println("Не удалось удалить файл: " + filePath);
            }
        }
    }

    public static void main(String[] args) {
        String path = "/Users/admin/Games";
        File gamesDir = new File(path);
        List<String> log = new ArrayList<>();
        String savegamesPath = "/Users/admin/Games/savegames";

        GameProgress progress1 = new GameProgress(100, 5, 3, 150.5);
        GameProgress progress2 = new GameProgress(85, 7, 4, 230.2);
        GameProgress progress3 = new GameProgress(60, 3, 2, 89.7);

        String saveFile1 = savegamesPath + "/save1.dat";
        String saveFile2 = savegamesPath + "/save2.dat";
        String saveFile3 = savegamesPath + "/save3.dat";


        String[] dirs = {"src", "res", "savegames", "temp"};
        log.add(newDir(dirs, gamesDir));

        saveGame(saveFile1, progress1);
        saveGame(saveFile2, progress2);
        saveGame(saveFile3, progress3);

        String zipPath = savegamesPath + "/saves.zip";
        List<String> filesToZip = new ArrayList<>();
        filesToZip.add(saveFile1);
        filesToZip.add(saveFile2);
        filesToZip.add(saveFile3);
        zipFiles(zipPath, filesToZip);

        // 4. Удаляем файлы сохранений, лежащие вне архива
        deleteFiles(filesToZip);

        System.out.println("Готово! Файлы сохранений упакованы в архив и удалены.");

        File srcDir = new File(gamesDir, "src");
        String[] srcDirs = {"main", "test"};
        log.add(newDir(srcDirs, srcDir));

        File mainDir = new File(srcDir, "main");
        String[] mainFiles = {"Main.java", "Utils.java"};
        for (String files : mainFiles) {
            File newFile = new File(mainDir, files);
            try {
                if (newFile.createNewFile()) {
                    log.add("Создан файл " + newFile.getAbsolutePath());
                }
            } catch (IOException e) {
                log.add("Не удалось создать файл " + newFile.getAbsolutePath() + " - " + e.getMessage());
            }
        }

        File resDir = new File(gamesDir, "res");
        String[] resDirs = {"drawables", "vectors", "icons"};
        log.add(newDir(resDirs, resDir));

        File tempDir = new File(gamesDir, "temp");
        File temp = new File(tempDir, "temp.txt");
        try (FileWriter writer = new FileWriter(temp)) {
            for (String entry : log) {
                writer.write(entry + System.lineSeparator());
            }
            log.add("Успешно записан лог в: " + temp.getAbsolutePath());
        } catch (IOException e) {
            log.add("Не удалось записать лог в temp.txt - " + e.getMessage());
        }
    }
}
