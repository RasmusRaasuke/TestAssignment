package ee.aieesti.testassignment.file;

import ee.aieesti.testassignment.database.Database;
import ee.aieesti.testassignment.database.DatabaseFile;
import ee.aieesti.testassignment.exception.UpdatingFileException;
import ee.aieesti.testassignment.exception.WritingLogException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileUpdater {
    private static final String OUTPUT_PATH = "src/main/resources/output/";
    private static final String LOG_PATH = "src/main/resources/log/";
    private static final String LOG_FILENAME = "log.txt";

    private final Database database;

    public FileUpdater() {
        database = new Database();
    }

    public void updateFileContents(String filename) {
        Map<String, DatabaseFile> databaseData = database.getData();

        File outputFolder = new File(OUTPUT_PATH);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        try (InputStream inputstream = FileUpdater.class.getClassLoader().getResourceAsStream("input/" + filename)) {
            XWPFDocument document = new XWPFDocument(inputstream);

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (int i = 0; i < paragraphs.size(); i++) {
                String content = paragraphs.get(i).getText();

                if (databaseData.containsKey(content) &&
                        !Objects.equals(databaseData.get(content).text(), paragraphs.get(i + 1).getText())) {

                    DatabaseFile dbFile = databaseData.get(content);
                    editParagraph(paragraphs.get(i + 1), dbFile.text());

                    String logText = String.format("[%s] Failis '%s' muudeti lõiku '%s'. Uus sisu tuli failist: '%s'.",
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            filename,
                            dbFile.header() ,
                            dbFile.filename());
                    writeLog(logText);

                    i++;
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_PATH + filename)) {
                document.write(outputStream);
            }
        } catch (IOException e) {
            throw new UpdatingFileException("Something went wrong during file content updating: ", e);
        }

        System.out.printf("Successfully updated file: %s%n",filename);
    }

    private void editParagraph(XWPFParagraph paragraph, String newText) {
        List<XWPFRun> runs = paragraph.getRuns();

        XWPFRun firstRun = runs.getFirst();
        String fontFamily = firstRun.getFontFamily();
        String color = firstRun.getColor();
        boolean isBold = firstRun.isBold();
        boolean isItalic = firstRun.isItalic();

        for (int i = runs.size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        XWPFRun newRun = paragraph.createRun();
        newRun.setText(newText);
        newRun.setFontFamily(fontFamily);
        newRun.setColor(color);
        newRun.setBold(isBold);
        newRun.setItalic(isItalic);
    }

    private void writeLog(String logText) {
        File logFolder = new File(LOG_PATH);
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH + LOG_FILENAME, true))) {
            writer.write(logText);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new WritingLogException("Could not write the log file: ", e);
        }
    }
}
