package TextProcessingProject;

import java.io.*;
import java.util.*;

public class FeatureWriter {

    /**
     * Writes email features to a CSV file.
     * @param emails List of emails
     * @param filePath The output file path
     */
    public static void writeEmailFeaturesToCSV(List<Email> emails, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Email ID,Label,Features\n"); // CSV header

            for (Email email : emails) {
                // Convert email features from int[] to double[] before writing to CSV
                double[] featuresAsDouble = email.getFeaturesAsDouble(); // Convert to double[] for compatibility

                // Write email ID and label to CSV
                writer.write(email.id + "," + email.label + ",");

                // Write the feature values as comma-separated strings
                for (int i = 0; i < featuresAsDouble.length; i++) {
                    writer.write(Double.toString(featuresAsDouble[i])); // Convert double to string
                    if (i < featuresAsDouble.length - 1) {
                        writer.write(","); // Add commas between values
                    }
                }
                writer.write("\n"); // New line for the next email
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
