package ee.aieesti.testassignment.file;

import ee.aieesti.testassignment.exception.FileReaderException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class DocxFileReader {

    public static List<String> read(String filename) {
        List<String> textParts = new ArrayList<>();
        try (InputStream inputStream = DocxFileReader.class.getClassLoader().getResourceAsStream(filename);
             XWPFDocument document = new XWPFDocument(inputStream);
        ) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                textParts.add(paragraph.getText());
            }
        } catch (IOException e) {
            throw new FileReaderException("File with given name is not present: ", e);
        }

        return textParts;

    }


}
