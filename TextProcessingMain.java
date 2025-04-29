package TextProcessingProject;

import java.util.*;
import java.io.*;


public class TextProcessingMain {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Reading dataset...");
        List<Email> emails = readDataset("spam_or_not_spam.csv");

        EmailFeatureExtractor extractor = new EmailFeatureExtractor();
        extractor.selectDistinguishingWords(emails, 50); // Select top 50 words

        for (Email email : emails) {
            email.computeFeatures(extractor.distinguishingWords);
        }

        Classifier classifier = new Classifier();
        classifier.train(emails);

        System.out.println("Classifying Emails:");
        int correct = 0;
        for (Email email : emails) {
            int predicted = classifier.classify(email);
            System.out.println("Email ID " + email.id + ": Actual=" + email.label + ", Predicted=" + predicted);
            if (predicted == email.label) correct++;
        }

        System.out.println("\nAccuracy: " + (100.0 * correct / emails.size()) + "%");

        FeatureWriter.writeEmailFeaturesToCSV(emails, "email_features.csv");
        sc.close();

        
    }
    public static List<Email> readDataset(String filepath) {
        List<Email> emails = new ArrayList<>();
        File f = new File(filepath);
        if (!f.exists()) {
            System.out.println("File does not exist: " + filepath);
            return emails;
        }
    
        System.out.println("Reading from file: " + f.getAbsolutePath());
    
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            StringBuilder emailBuilder = new StringBuilder();
            int id = 0;
            String line;
    
            while ((line = br.readLine()) != null) {
                emailBuilder.append(line).append("\n");
    
                line = line.trim();
                if (line.endsWith(",0") || line.endsWith(",1")) {
                    String fullEmail = emailBuilder.toString().trim();
    
                    // Find last comma to split label
                    int lastComma = fullEmail.lastIndexOf(',');
                    if (lastComma == -1) {
                        System.out.println("Skipping malformed email block: " + fullEmail);
                        emailBuilder.setLength(0); // reset builder
                        continue;
                    }
    
                    String text = fullEmail.substring(0, lastComma).trim();
                    String labelStr = fullEmail.substring(lastComma + 1).trim();
    
                    if (text.startsWith("\"") && text.endsWith("\"")) {
                        text = text.substring(1, text.length() - 1);
                    }
                    text = text.replace("\"\"", "\"");
    
                    try {
                        int label = Integer.parseInt(labelStr);
                        emails.add(new Email(id++, label, text));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping email with invalid label: " + labelStr);
                    }
    
                    emailBuilder.setLength(0); // reset for next email
                }
            }
    
        } catch (Exception e) {
            System.out.println("Error while reading file: " + e.getMessage());
            e.printStackTrace();
        }
    
        System.out.println("Loaded " + emails.size() + " emails.");
        return emails;
    }

}