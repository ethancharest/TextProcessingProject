package TextProcessingProject;

import java.io.*;
import java.util.*;

public class FeatureWriter {
    public static void writeEmailFeaturesToCSV(List<Email> emails, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("ID,Label,TopWords");
            for (Email e : emails) {
                String labelText = (e.label == 1) ? "spam" : "not spam";
                writer.printf("%d,%s,\"%s\"\n", e.id, labelText, e.getTopWords(10));
            }
            System.out.println("Email features written to: " + outputPath);
        } catch (IOException ex) {
            System.out.println("Failed to write features to CSV: " + ex.getMessage());
        }
    }
}
