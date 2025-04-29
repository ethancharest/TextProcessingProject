package TextProcessingProject;

import java.util.ArrayList;
import java.util.List;

public class EmailFeatureExtractor {
    
    List<String> distinguishingWords;

    public EmailFeatureExtractor() {
        distinguishingWords = new ArrayList<>();
    }

    public void selectDistinguishingWords(List<Email> emails, int topN) {
        List<String> allWords = new ArrayList<>();
        List<Integer> wordSpamCounts = new ArrayList<>();
        List<Integer> wordNotSpamCounts = new ArrayList<>();

        for (Email email : emails) {
            for (String word : email.words) {
                int index = allWords.indexOf(word);
                if (index == -1) {
                    allWords.add(word);
                    if (email.label == 1) {
                        wordSpamCounts.add(1);
                        wordNotSpamCounts.add(0);
                    } else {
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

        List<Double> scores = new ArrayList<>();
        for (int i = 0; i < allWords.size(); i++) {
            scores.add((double)(wordSpamCounts.get(i) - wordNotSpamCounts.get(i)));
        }

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
                distinguishingWords.add(allWords.get(bestIndex));
                scores.set(bestIndex, 0.0);
            }
        }
    }
}
