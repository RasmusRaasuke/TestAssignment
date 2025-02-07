package ee.aieesti.testassignment.database;

import ee.aieesti.testassignment.file.DocxFileReader;
import ee.aieesti.testassignment.exception.DatabaseInitializationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Database {
    private final Map<String, DatabaseFile> data;

    public Database() {
        data = new HashMap<>();
        readDataFromFiles();
    }

    public Map<String, DatabaseFile> getData() {
        return data;
    }

    private void readDataFromFiles() {
        URL databaseUrl = Database.class.getClassLoader().getResource("database");

        try {
            Path databasePath = Paths.get(databaseUrl.toURI());

            try (Stream<Path> paths = Files.list(databasePath)) {
                paths.filter(Files::isRegularFile).forEach(file -> {
                    String filename = file.getFileName().toString();
                    List<String> text = DocxFileReader.read("database/" + filename);
                    data.put(text.getFirst(), new DatabaseFile(filename, text.getFirst(), text.getLast()));
                });
            }

        } catch (URISyntaxException | IOException e) {
            throw new DatabaseInitializationException("Something went wrong with database initialization: ", e);
        }

    }
}
