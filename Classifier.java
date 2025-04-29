package TextProcessingProject;

import java.util.List;

public class Classifier {
    double[] spamModel;
    double[] notSpamModel;

    public void train(List<Email> emails) {
        int featureSize = emails.get(0).features.length;
        double[] spamSum = new double[featureSize];
        double[] notSpamSum = new double[featureSize];
        int spamCount = 0, notSpamCount = 0;

        for (Email email : emails) {
            if (email.label == 1) {
                for (int i = 0; i < featureSize; i++) {
                    spamSum[i] += email.features[i];
                }
                spamCount++;
            } else {
                for (int i = 0; i < featureSize; i++) {
                    notSpamSum[i] += email.features[i];
                }
                notSpamCount++;
            }
        }

        spamModel = new double[featureSize];
        notSpamModel = new double[featureSize];
        for (int i = 0; i < featureSize; i++) {
            spamModel[i] = spamSum[i] / spamCount;
            notSpamModel[i] = notSpamSum[i] / notSpamCount;
        }
    }

    public int classify(Email email) {
        double spamDist = computeDistance(email.features, spamModel);
        double notSpamDist = computeDistance(email.features, notSpamModel);
        return spamDist < notSpamDist ? 1 : 0;
    }

    private double computeDistance(int[] features, double[] model) {
        double sum = 0;
        for (int i = 0; i < features.length; i++) {
            sum += Math.pow(features[i] - model[i], 2);
        }
        return Math.sqrt(sum);
    }
}
