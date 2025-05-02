package TextProcessingProject;

import java.util.*;

/**
 * This class extracts distinguishing words from a list of emails.
 * It selects the most frequent words that help separate spam from non-spam emails.
 */
public class EmailFeatureExtractor {

    List<String> distinguishingWords; // List to hold the top distinguishing words

    public EmailFeatureExtractor() {
        distinguishingWords = new ArrayList<>();
    }

    /**
     * Selects the top distinguishing words based on how frequently they appear in spam
     * compared to not-spam emails.
     *
     * @param emails List of email objects
     * @param topN   Number of top distinguishing words to select
     */
    public void selectDistinguishingWords(List<Email> emails, int topN) {
        Map<String, int[]> wordCounts = new HashMap<>(); // Map to hold word counts for spam and not-spam

        // Count occurrences of each word in spam vs. not-spam emails
        for (Email email : emails) {
            for (String word : email.words) {
                wordCounts.putIfAbsent(word, new int[2]); // Initialize word count if not present
                if (email.label == 1) { // If the email is spam
                    wordCounts.get(word)[0]++; // Increment spam count
                } else { // If the email is not spam
                    wordCounts.get(word)[1]++; // Increment not-spam count
                }
            }
        }

        // Calculate scores for each word: Difference between spam and not-spam counts
        List<Map.Entry<String, int[]>> wordList = new ArrayList<>(wordCounts.entrySet());
        wordList.sort((entry1, entry2) -> {
            int spamCount1 = entry1.getValue()[0];
            int notSpamCount1 = entry1.getValue()[1];
            int spamCount2 = entry2.getValue()[0];
            int notSpamCount2 = entry2.getValue()[1];
            int score1 = Math.abs(spamCount1 - notSpamCount1);
            int score2 = Math.abs(spamCount2 - notSpamCount2);
            return Integer.compare(score2, score1); // Sort by absolute score (descending)
        });

        // Select the top N distinguishing words based on the scores
        for (int i = 0; i < topN && i < wordList.size(); i++) {
            distinguishingWords.add(wordList.get(i).getKey());
        }
    }

    /**
     * Get the top distinguishing words for debugging or analysis.
     *
     * @return List of top distinguishing words
     */
    public List<String> getDistinguishingWords() {
        return distinguishingWords;
    }
}
