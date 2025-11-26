package Main;

/**
 *
 * @author thiago
 */
import java.io.FileWriter;
import java.io.IOException;

public class CSVExport {

    /**
     * Exports a string to a CSV file
     * @param content the text to export
     * @param filePath full path of the CSV file
     * @throws IOException
     */
    public static void exportToCSV(String content, String filePath) throws IOException {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(content);
        }
    }
}

