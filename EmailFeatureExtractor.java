package TextProcessingProject;

import java.util.ArrayList;
import java.util.List;

public class EmailFeatureExtractor {

    List<String> distinguishingWords;

    
    public EmailFeatureExtractor() {
        distinguishingWords = new ArrayList<>();
    }

    /**
     * Selects the top distinguishing words that help separate spam from non-spam emails.
    * This is based on how much more frequently a word appears in spam vs not-spam emails.
    *
    * @param emails List of email objects
    * @param topN   Number of top distinguishing words to select
    */
    public void selectDistinguishingWords(List<Email> emails, int topN) {
        List<String> allWords = new ArrayList<>(); //Stores all unique words found in emails 
        List<Integer> wordSpamCounts = new ArrayList<>(); //Tracks how many times each word appears in spam
        List<Integer> wordNotSpamCounts = new ArrayList<>(); //Tracks how many times each word appears in not spam

        // Count how many times each word appears in spam vs not-spam
        for (Email email : emails) {
            for (String word : email.words) {
                int index = allWords.indexOf(word);
                if (index == -1) {
                    allWords.add(word);
                    if (email.label == 1) { //spam
                        wordSpamCounts.add(1);
                        wordNotSpamCounts.add(0);
                    } else { //not spam
                        wordSpamCounts.add(0);
                        wordNotSpamCounts.add(1);
                    }
                } else {
                    if (email.label == 1) {
                        wordSpamCounts.set(index, wordSpamCounts.get(index) + 1);
                    } else {
                        wordNotSpamCounts.set(index, wordNotSpamCounts.get(index) + 1);
                    }
                }
            }
        }
        //calculate "score" for each word based on the difference in usage between spam/not spam
        List<Double> scores = new ArrayList<>();
        for (int i = 0; i < allWords.size(); i++) {
            scores.add((double) (wordSpamCounts.get(i) - wordNotSpamCounts.get(i)));
        }
        //Pick words with largest score values 
        for (int i = 0; i < topN; i++) {
            double bestScore = 0;
            int bestIndex = -1;
            for (int j = 0; j < scores.size(); j++) {
                if (Math.abs(scores.get(j)) > Math.abs(bestScore)) {
                    bestScore = scores.get(j);
                    bestIndex = j;
                }
            }
            if (bestIndex != -1) {
                distinguishingWords.add(allWords.get(bestIndex)); //Add words to list
                scores.set(bestIndex, 0.0);
            }
        }
    }
}
