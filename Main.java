import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
        String path = "/Users/admin/Games";
        File gamesDir = new File(path);
        List<String> log = new ArrayList<>();

        String[] dirs = {"src", "res", "savegames", "temp"};
        log.add(newDir(dirs, gamesDir));

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
