package TextProcessingProject;

import java.util.List;

public class Classifier {
    
    private int[] spamModel;
    private int[] notSpamModel;

    //stores training data
    public void train(List<Email> emails) {
        spamModel = computeModel(emails, 1);
        notSpamModel = computeModel(emails, 0);
    }

    /**
     * Classifies an email as spam or not spam based on which model it's closer to.
     *
     * @param email The email to classify
     * @return 1 if classified as spam, 0 if not spam
     */
    public int classify(Email email) {
        double[] spamModelD = convertToDoubleArray(spamModel); //Convert models to double arrays for easier calculation
        double[] notSpamModelD = convertToDoubleArray(notSpamModel);
        double distToSpam = computeDistance(email.features, spamModelD); //Compute Eulidian Distance 
        double distToNotSpam = computeDistance(email.features, notSpamModelD);
        return distToSpam < distToNotSpam ? 1 : 0; //Classify by which distance is smaller
    }

    /**
     * Computes the average feature vector (model) for a group of emails with a specific label.
     *
     * @param emails list of emails
     * @param label  label to filter by (0 or 1)
     * @return Average feature model as an int array
     */
    public int[] computeModel(List<Email> emails, int label) {
        if (emails.isEmpty()) return new int[0];

        int featureLength = emails.get(0).features.length;
        int[] model = new int[featureLength];
        int count = 0;

        for (Email email : emails) {
            if (email.label == label) {
                for (int i = 0; i < featureLength; i++) {
                    model[i] += email.features[i];
                }
                count++; //counts how many emails matched
            }
        }

        //Average
        if (count > 0) {
            for (int i = 0; i < featureLength; i++) {
                 model[i] /= count;
            }
        }

        return model;
    }
    /**
     * Computes Euclidean distance 
     * @param a first vector (int[])
     * @param b second vector (double[])
     * @return The Euclidean distance
     */
    public double computeDistance(int[] a, double[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("Feature length mismatch");

        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    private double[] convertToDoubleArray(int[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }

}


