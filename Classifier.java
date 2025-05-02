package TextProcessingProject;

import java.util.*;

public class Classifier {

    public Classifier() {
        this.spamModel = new double[0];
        this.notSpamModel = new double[0];
    }

    /**
     * Trains the classifier by processing a list of emails.
     * This method builds the spam and not-spam models based on email features.
     *
     * @param emails List of emails to train the classifier
     */
    public void train(List<Email> emails) {
        // Here we compute models (spam and not-spam) based on the email features.
        this.spamModel = computeModel(emails, 1);  // 1 indicates spam
        this.notSpamModel = computeModel(emails, 0);  // 0 indicates not spam
    }

    /**
     * Computes a model (vector) for either spam or not-spam emails.
     * 
     * @param emails List of emails
     * @param label  1 for spam, 0 for not spam
     * @return A double array representing the computed model
     */
    public double[] computeModel(List<Email> emails, int label) {
        double[] model = new double[emails.get(0).features.length];  // Make sure features array is initialized correctly
        // Process emails and accumulate feature counts for the given label (spam or not-spam)
        for (Email email : emails) {
            if (email.label == label) {
                for (int i = 0; i < email.features.length; i++) {
                    model[i] += email.features[i]; // Use accumulated feature counts for the model
                }
            }
        }

        // Normalize the model by dividing by the number of emails
        double divisor = emails.stream().filter(e -> e.label == label).count();
        for (int i = 0; i < model.length; i++) {
            model[i] /= divisor;  // Average each feature value
        }

        return model; // Return the model as a double array
    }

    /**
     * Classifies an email by calculating its distance to both spam and not-spam models.
     * 
     * @param email The email to classify
     * @return The predicted label (1 for spam, 0 for not-spam)
     */
    public int classify(Email email, double[] spamModel, double[] notSpamModel) {
        // Your classification logic based on the spam and not-spam models
        // Compute distances and classify accordingly
        double spamDistance = computeDistance(email.getFeaturesAsDouble(), spamModel);
        double notSpamDistance = computeDistance(email.getFeaturesAsDouble(), notSpamModel);
    
        // Compare distances and classify based on the smaller distance
        return (spamDistance < notSpamDistance) ? 1 : 0;  // 1 = spam, 0 = not spam
    }

    /**
     * Computes the distance between an email's feature vector and a model (spam or not-spam).
     * 
     * @param emailFeatures Feature vector of the email
     * @param model         The model to compute distance against
     * @return The computed distance
     */
    public double computeDistance(double[] emailFeatures, double[] model) {
        double distance = 0;

        for (int i = 0; i < emailFeatures.length; i++) {
            distance += Math.pow(emailFeatures[i] - model[i], 2);
        }
        return Math.sqrt(distance);
    }
}
