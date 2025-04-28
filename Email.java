package TextProcessingProject;

public class Email {
    String text;
    int label; // 1 = spam, 0 = not spam
    
    public Email(String text, int label) {
        this.text = text;
        this.label = label;
    }
       
}
