package RecyclerHelper;

public class QuizItem {
    public QuizItem(String question) {
        Question = question;
    }

    String Question;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }
}
