/*  
    Name: Ritika Lama
    Course: CS 260
    Programming Assignment related to GUI
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GuiQuiz implements ActionListener {
    //Arraylists to hold the questions and radio buttons
    private ArrayList<QuizQuestion> quizGame = new ArrayList<>();
    private ArrayList<JRadioButton> options = new ArrayList<>();
    private JFrame frame;
    private JLabel questionText;
    private ButtonGroup buttonGroup;
    private int currentQuestion;
    private int score;

    //default constructor
    public GuiQuiz(){
    }
    /*
     * Constructor to initialize GUI
     */
    public GuiQuiz(int currentQuestion, int score){
        this.currentQuestion = 0;
        this.score = 0;
        window();
    }

    //function to create the panel and add buttons 
    public void window() {
        //set the frame for the quiz
        frame = new JFrame("GUI Quiz");
        frame.setSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //read from the file given using exception handling
        try {
            File inputFile = new File("mcq.txt");
            Scanner input = new Scanner(inputFile);
            QuizQuestion question = null;
            int answerIndex = 0;
            //loop continues till there are questions and answers
            while (input.hasNextLine()) {
                String line = input.nextLine();
                //mark the line as a question if it starts with #
                if (line.charAt(0) == '#') {
                    if (question != null) {
                        quizGame.add(question);
                    }
                    //add the questio to the arraylist
                    question = new QuizQuestion(line.substring(1));
                    answerIndex = 0;
                }
                //mark the line as correct answer if it begins with > 
                else if (line.charAt(0) == '>') {
                    question.setCorrectAnswer(answerIndex);
                    question.addQuestion(answerIndex, line.substring(1));
                    answerIndex++;
                } 
                //else add everything as an option
                else {
                    question.addQuestion(answerIndex, line);
                    answerIndex++;
                }
            }
            // Add last question to the list
            if (question != null) {
                quizGame.add(question);
            }
            //close the scanner to prevent data leakage
            input.close();
        }
        //print the error message if file is not found
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //give a random question, pass quizGame arraylist
        random(quizGame);

        //make the question panel
        // Retrieve the current question object from the quizGame 
        //ArrayList based on the current question index
        QuizQuestion currentquestionobj = quizGame.get(currentQuestion);
        JPanel questions = new JPanel();
        questions.setLayout(new BorderLayout());
        questionText = new JLabel(currentquestionobj.getQuestionText());
        questions.add(questionText, BorderLayout.NORTH);
        //make option panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2,2));
        
        //get the answers
        String[] answers = currentquestionobj.getAnswers();
        options = new ArrayList<>();
        // Initializing the ArrayList
        buttonGroup = new ButtonGroup(); 
        for (int i = 0; i < answers.length; i++) {
            //Create Radio buttons for each options
            JRadioButton radioButton = new JRadioButton(answers[i]);
            //add the event listener
            radioButton.addActionListener(this);
            buttonGroup.add(radioButton);
            optionsPanel.add(radioButton);
            options.add(radioButton);
            //set radio button to initially unselected pg 686
            radioButton.setSelected(false);
        }
        //add options to the panel
        questions.add(optionsPanel, BorderLayout.CENTER);
        //add panel to frame
        frame.add(questions, BorderLayout.CENTER);

        //add the buttons on the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        //create restart, back,next, and submit buttons
        JButton button1 = new JButton("Restart");
        JButton button2 = new JButton("Back");
        JButton button3 = new JButton("Next");
        JButton button4 = new JButton("Submit");

        //add action listeners to those buttons
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        //add those button to the panel
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        
        //add the button panel to the south
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Get random questions from the text file
     *
     * @param list The ArrayList of QuizQuestion objects to be shuffled.
    */
    private void random(ArrayList<QuizQuestion> list) {
        //new Random object to generate random numbers
        Random randomQuestion = new Random();
        //iterate through the list to shuffle
        for (int i = 0; i < list.size(); i++) {
            int randomNumber = randomQuestion.nextInt(list.size());
            // Swap elements at positions i and j
            QuizQuestion temp = list.get(i);
            list.set(i, list.get(randomNumber));
            list.set(randomNumber, temp);
        }
        //to display only 5 random quiz questions
        while(list.size() > 5){
            list.remove(list.size() - 1);
        }
    }    

    /**
     * Invoked when the button is clicked
     * 
     * @param e the action that occured
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //check if it is an instance of JButton
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            //get the text of the button
            String valueString = button.getText();
            //clicking restart sets everything to zero
            if ("Restart".equals(valueString)) {
                currentQuestion = 0;
                score = 0;
                updateQuestion();
            } 
            //move to previous question if pressed back and if current question >0
            else if ("Back".equals(valueString) && currentQuestion > 0) {
                currentQuestion--;
                updateQuestion();
            } 
            //move forward if pressed next
            else if ("Next".equals(valueString) && currentQuestion < quizGame.size() - 1) {
                int selectedOption = getSelectedOption();
                //if the answer is right update the score
                if (selectedOption == quizGame.get(currentQuestion).getCorrectAnswer()) {
                    score += 10;
                }
                currentQuestion++;
                updateQuestion();
            } 
            else if ("Submit".equals(valueString)) {
                int selectedOption = getSelectedOption();
                if (selectedOption == quizGame.get(currentQuestion).getCorrectAnswer()) {
                    score += 10;
                }
                //display final score
                JOptionPane.showMessageDialog(frame, "Congratulations, Your score is: " + score);
            }
        } 
    }

    /**
     * gets the index of radio option
    */
    private int getSelectedOption() {
        //iterate through options
        for (int i = 0; i < options.size(); i++) {
            //checks if the button is selected (page 686)
            if (options.get(i).isSelected()) {
                //return the index
                return i;
            }
        }
        return -1;
    }

    /*
     * Updates the question based on the click of user
     */
    private void updateQuestion() {
        //get the current question
        String currentText = quizGame.get(currentQuestion).getQuestionText();
        questionText.setText(currentText);

        //get the answers for current question
        String[] answers = quizGame.get(currentQuestion).getAnswers();
        //iterate through the options and set the options
        for (int i = 0; i < options.size(); i++) {
            options.get(i).setText(answers[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        GuiQuiz quiz = new GuiQuiz();
        quiz.window();
    }
}
