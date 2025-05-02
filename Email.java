package TextProcessingProject;

import java.util.*;

public class Email {
    int id; // Email Identifier
    int label; // 1 = spam, 0 = not spam
    String text; // Raw email text content
    List<String> words; // List of words in the email
    int[] features; // Count of distinguishing words

    public Email(int id, int label, String text) {
        this.id = id;
        this.label = label;
        this.text = text.toLowerCase();
        this.words = new ArrayList<>();
        processText();
    }

    /**
     * Processes the email text into a list of words, cleaning non-alphabetic characters.
     */
    private void processText() {
        // Remove non-alphabetical characters and split the text into words
        String[] splitWords = text.replaceAll("[^a-z ]", "").split("\\s+");

        for (String word : splitWords) {
            if (!word.isEmpty()) {
                words.add(word); // Add each word to the list
            }
        }
    }

    /**
     * Computes the feature array based on the selected distinguishing words.
     * Features are stored as the count of occurrences of each distinguishing word.
     *
     * @param selectedWords List of words that are considered distinguishing
     */
    public void computeFeatures(List<String> selectedWords) {
        features = new int[selectedWords.size()]; // Initialize feature array

        // For each distinguishing word, count its occurrences in the email's words list
        for (int i = 0; i < selectedWords.size(); i++) {
            String targetWord = selectedWords.get(i);
            int count = 0;

            for (String word : words) {
                if (word.equals(targetWord)) {
                    count++; // Increment count if the word matches
                }
            }

            features[i] = count; // Store the count in the feature array
        }
    }

    /**
     * Returns the features of the email as a double array.
     *
     * @return Array of features as double
     */
    public double[] getFeaturesAsDouble() {
        double[] featuresAsDouble = new double[features.length];
        for (int i = 0; i < features.length; i++) {
            featuresAsDouble[i] = (double) features[i]; // Convert int to double
        }
        return featuresAsDouble;
    }

    /**
     * Returns the top words in the email based on their frequency.
     * Useful for debugging or inspecting the most frequent words.
     *
     * @param count Number of top words to retrieve
     * @return A string containing the top words and their frequencies
     */
    public String getTopWords(int count) {
        Map<String, Integer> wordCount = new HashMap<>();

        // Count occurrences of each word
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        // Sort words by frequency in descending order
        List<Map.Entry<String, Integer>> wordList = new ArrayList<>(wordCount.entrySet());
        wordList.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        StringBuilder topWords = new StringBuilder();

        // Append the top words to the result string
        for (int i = 0; i < Math.min(count, wordList.size()); i++) {
            topWords.append(wordList.get(i).getKey()).append("(").append(wordList.get(i).getValue()).append(") ");
        }

        return topWords.toString().trim(); // Return the top words as a formatted string
    }
}
