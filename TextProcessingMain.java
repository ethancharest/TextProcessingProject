package TextProcessingProject;

import java.util.*;
import java.io.*;

public class TextProcessingMain {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Reading dataset...");
        List<Email> emails = readDataset("spam_or_not_spam.csv"); //Read Dataset from CSV and parse

        //Create Feature Extractor and select the 50 most commonly used words in the Dataset
        EmailFeatureExtractor extractor = new EmailFeatureExtractor();
        extractor.selectDistinguishingWords(emails, 50); 

        for (Email email : emails) {
            email.computeFeatures(extractor.distinguishingWords); //Distiguish features for each email using distinguishingWords
        }

        Classifier classifier = new Classifier(); //Train classifier using email features
        classifier.train(emails);

        //Create models for spam and not spam emails
        int[] spamModelInt = classifier.computeModel(emails, 1); //1 = spam
        int[] notSpamModelInt = classifier.computeModel(emails, 0);//0 = not spam

        // Convert to double[] for later calculations
        double[] spamModel = convertToDoubleArray(spamModelInt);
        double[] notSpamModel = convertToDoubleArray(notSpamModelInt);

        //Classify each email and print actual vs predicted 
        System.out.println("Classifying Emails...");
        int correct = 0;
        for (Email email : emails) {
            int predicted = classifier.classify(email);
            System.out.println("Email ID " + email.id + ": Actual=" + email.label + ", Predicted=" + predicted);
            if (predicted == email.label)
                correct++;
        }

        //Print filter Accuracy
        System.out.println("\nAccuracy: " + (100.0 * correct / emails.size()) + "%");

        //Write features to CSV
        FeatureWriter.writeEmailFeaturesToCSV(emails, spamModel, notSpamModel, classifier,
                "email_features.csv");

        sc.close();

    }

    //Converts int array to Double array
    private static double[] convertToDoubleArray(int[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }
    
    /*
     * Reads the dataset file and parses emails from it.
     * Each email ends with either ,0 or ,1 to indicate not spam (0) or spam (1).
     */
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
            
            //Looks for the label at the end of the email block
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

                    //Extract email text/label
                    String text = fullEmail.substring(0, lastComma).trim();
                    String labelStr = fullEmail.substring(lastComma + 1).trim();

                    //remove wrapping quotes/fixes quotes
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