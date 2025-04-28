package TextProcessingProject;
import java.io.*;
import java.util.*;

public class EmailFeatureExtractor {
    private List<String> featureList = new ArrayList<>();
    private int[][] emailFeatures;
    private List<String> emails;
    

    // Preprocess text (lowercase and remove non-alphanumerics)
    public String preprocess(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
    }

    // Tokenize text into words
    public List<String> tokenize(String text) {
        return Arrays.asList(text.split("\\s+"));
    }

    // Build feature list from all emails (words + bigrams)
    private void buildFeatureList(List<String> emails) {
    Map<String, Integer> featureCounts = new HashMap<>();
    for (String email : emails) {
        String clean = preprocess(email);
        List<String> tokens = tokenize(clean);

        for (String token : tokens) {
            if (!token.isEmpty()) {
                featureCounts.put(token, featureCounts.getOrDefault(token, 0) + 1);
            }
        }
        for (int i = 0; i < tokens.size() - 1; i++) {
            String bigram = tokens.get(i) + "_" + tokens.get(i + 1);
            featureCounts.put(bigram, featureCounts.getOrDefault(bigram, 0) + 1);
        }
    }

    // Sort and keep top 1000 features
    featureList = featureCounts.entrySet().stream()
        .sorted((e1, e2) -> e2.getValue() - e1.getValue())
        .limit(1000)
        .map(Map.Entry::getKey)
        .toList();
}

    // Extract features
    public void extractFeatures(List<String> emails) {
        this.emails = emails;
        buildFeatureList(emails);
        emailFeatures = new int[emails.size()][featureList.size()];

        for (int i = 0; i < emails.size(); i++) {
            String clean = preprocess(emails.get(i));
            List<String> tokens = tokenize(clean);

            for (String token : tokens) {
                if (!token.isEmpty()) {
                    int index = featureList.indexOf(token);
                    if (index >= 0) {
                        emailFeatures[i][index]++;
                    }
                }
            }
            for (int j = 0; j < tokens.size() - 1; j++) {
                String bigram = tokens.get(j) + "_" + tokens.get(j + 1);
                int index = featureList.indexOf(bigram);
                if (index >= 0) {
                    emailFeatures[i][index]++;
                }
            }
        }
    }

    // Save features to CSV
    public void saveEmailFeatures(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write("EmailID");
        for (String feature : featureList) {
            writer.write("," + feature);
        }
        writer.newLine();

        for (int i = 0; i < emailFeatures.length; i++) {
            writer.write(String.valueOf(i));
            for (int value : emailFeatures[i]) {
                writer.write("," + value);
            }
            writer.newLine();
        }
        writer.close();
    }
    public void printEmailFeatures(int emailIdx) {
        //System.out.println("Email " + emailIdx + " Features:");
        for (int i = 0; i < featureList.size(); i++) {
            if (emailFeatures[emailIdx][i] != 0) {
                System.out.println(featureList.get(i) + ": " + emailFeatures[emailIdx][i]);
            }
        }
    }

    // Create mean summary vector for given indices
    public double[] createSummary(List<Integer> indices) {
        double[] summary = new double[featureList.size()];
        for (int idx : indices) {
            for (int j = 0; j < featureList.size(); j++) {
                summary[j] += emailFeatures[idx][j];
            }
        }
        for (int j = 0; j < featureList.size(); j++) {
            summary[j] /= indices.size();
        }
        return summary;
    }

    // Calculate distance between two emails
    public double distanceBetweenEmails(int idx1, int idx2) {
        double sum = 0.0;
        for (int i = 0; i < featureList.size(); i++) {
            int v1 = emailFeatures[idx1][i];
            int v2 = emailFeatures[idx2][i];
            sum += Math.pow(v1 - v2, 2);
        }
        return Math.sqrt(sum);
    }

    // Distance from email to summary
    public double distanceToSummary(int emailIdx, double[] summary) {
        double sum = 0.0;
        for (int i = 0; i < featureList.size(); i++) {
            double diff = emailFeatures[emailIdx][i] - summary[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    // Classify
    public String classify(int emailIdx, double[] spamModel, double[] notSpamModel) {
        double distSpam = distanceToSummary(emailIdx, spamModel);
        double distNotSpam = distanceToSummary(emailIdx, notSpamModel);
        return distSpam < distNotSpam ? "Spam" : "Not Spam";
    }
}
