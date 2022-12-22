package data;

public class Syntax {
    String[] word;
    
// GETTERS
    public String[] getWord() {
        return word;
    }

// SETTERS
    public void setWord(String[] word) {
        this.word = word;
    }

// CONSTRUCTOR
    public Syntax(){
        String[] word = new String[5];
        word[0] = "anaty";
        word[1] = "ny";
        word[2] = "aiza";
        word[3] = "sy";
        word[4] = "eo";
        this.setWord(word);
    } 
}
