public class QuizQuestion {
    private String questionText;
    private String answers[];
    private int correctAnswer;
    private final int MAX_ANS = 4;

    
    /**
     * Constructor to initialize a new QuizQuestion 
     * @param qt the question text
     */
    QuizQuestion(String qt) {
        questionText = qt;
        answers = new String[MAX_ANS];
    }

    /**
     * Adds an answer option to the question
     * 
     * @param index index of the answer.
     * @param answerText the text of the answer.
    */
    void addQuestion(int index, String answerText) {
        answers[index] = answerText;
    }

    /**
     * Sets the correct answer for the question.
     * @param answerIndex The index of the correct answer.
    */
    void setCorrectAnswer(int answerIndex) {
        correctAnswer = answerIndex;
    }
    /**
     * Returns the question.
     * @return text of the question.
    */
    String getQuestionText() {
        return questionText;
    } 
    
    /**
     * Returns an array containing the answers for question.
     * @return An array of answer.
     */
    String[] getAnswers() {
        return answers;
    }
    
    /**
     * Returns the index of the correct answer for the question.
     * @return The index of the correct answer.
    */
    int getCorrectAnswer() {
        return correctAnswer;
    }
}