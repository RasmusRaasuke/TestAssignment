package ee.aieesti.testassignment;

import ee.aieesti.testassignment.file.FileUpdater;

public class Main {

    public static void main(String[] args) {
        FileUpdater fileUpdater = new FileUpdater();

        //String inputFilename = "TN Järveotsa tee 25-15 Õismäe.docx";
        String inputFilename = "TN Tulika tn 20-5 Kristiine.docx";

        fileUpdater.updateFileContents(inputFilename);
    }
}
