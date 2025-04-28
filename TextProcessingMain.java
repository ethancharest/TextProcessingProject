package TextProcessingProject;

import java.nio.file.*;
import java.util.*;
import java.io.*;

public class TextProcessingMain {
    public static void main(String[] args) {
        try {
            EmailFeatureExtractor extractor = new EmailFeatureExtractor();
            PrintWriter printer = new PrintWriter("email_features.csv");
            

            List<String> emailTexts = new ArrayList<>();
            List<Integer> labels = new ArrayList<>();

            // Read CSV file
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\ethan\\OneDrive\\Computer Science\\TextProcessingProject\\spam_or_not_spam.csv"));
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }

                String text = line.trim();
                if (!text.isEmpty()) {
                    emailTexts.add(text);
                }
            }
            reader.close();

            // Extract features
            extractor.extractFeatures(emailTexts);

            // Save features
            extractor.saveEmailFeatures("email_featuresRAW.csv");


            for (int i = 1; i < Math.min(10, emailTexts.size()); i++) {
                System.out.println("Features for Email " + i + ":");
                extractor.printEmailFeatures(i);
                System.out.println();
            }
            
            printer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
