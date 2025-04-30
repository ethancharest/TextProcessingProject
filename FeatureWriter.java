package TextProcessingProject;

import java.io.*;
import java.util.*;

public class FeatureWriter {

    //Writes the Data (Features) found by our methods in the emails from the Dataset into a .csv file 
    public static void writeEmailFeaturesToCSV(List<Email> emails, double[] spamModel,
            double[] notSpamModel, Classifier classifier, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("ID,Label,TopWords,DistanceToSpamModel,DistanceToNotSpamModel,EucildeanDistance");

            for (Email e : emails) {
                String labelText = (e.label == 1) ? "spam" : "not spam";
                double distToSpam = classifier.computeDistance(e.features, spamModel);
                double distToNotSpam = classifier.computeDistance(e.features, notSpamModel);
                double euclideanDistance = Math.abs(distToSpam - distToNotSpam);

                writer.printf("%d,%s,\"%s\",%.4f,%.4f,%.4f\n", e.id, labelText, e.getTopWords(10), distToSpam,
                        distToNotSpam,euclideanDistance);
            }

            System.out.println("Email features written to: " + outputPath);

        } catch (IOException ex) {
            System.out.println("Failed to write features to CSV: " + ex.getMessage());
        }   
    }
}
