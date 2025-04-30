package TextProcessingProject;

import java.util.ArrayList;
import java.util.List;

public class Email {
    int id; //Email Identifier
    int label; // 1 = spam, 0 = not spam
    String text; //Raw email text content
    List<String> words; 
    int[] features; // Count of distinguishing words

    public Email(int id, int label, String text) {
        this.id = id;
        this.label = label;
        this.text = text.toLowerCase();
        this.words = new ArrayList<>();
        processText();
    }

    private void processText() {
        String[] splitWords = text.replaceAll("[^a-z ]", "").split("\\s+");
        for (String word : splitWords) {
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
    }

    public void computeFeatures(List<String> selectedWords) {
        features = new int[selectedWords.size()];
        for (int i = 0; i < selectedWords.size(); i++) {
            String targetWord = selectedWords.get(i);
            int count = 0;
            for (String w : words) {
                if (w.equals(targetWord)) {
                    count++;
                }
            }
            features[i] = count;
        }
    }

    // gets the top distingishing words from each email
    public String getTopWords(int count) {
        List<String> uniqueWords = new ArrayList<>();
        List<Integer> frequencies = new ArrayList<>();

        for (String word : words) {
            int index = uniqueWords.indexOf(word);
            if (index == -1) {
                uniqueWords.add(word);
                frequencies.add(1);
            } else {
                frequencies.set(index, frequencies.get(index) + 1);
            }
        }

        List<String> topWords = new ArrayList<>();
        for (int i = 0; i < count && !uniqueWords.isEmpty(); i++) {
            int maxIndex = 0;
            for (int j = 1; j < frequencies.size(); j++) {
                if (frequencies.get(j) > frequencies.get(maxIndex)) {
                    maxIndex = j;
                }
            }
            topWords.add(uniqueWords.get(maxIndex) + "(" + frequencies.get(maxIndex) + ")");
            uniqueWords.remove(maxIndex);
            frequencies.remove(maxIndex);
        }

        return String.join(" ", topWords);
    }

}