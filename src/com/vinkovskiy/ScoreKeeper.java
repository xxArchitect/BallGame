package com.vinkovskiy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static com.vinkovskiy.Difficulty.HARD;

public class ScoreKeeper {
    private int currentScore = 0;
    private double scoreX = 320, scoreY = 40; //score location
    private Font scoreFont = Font.font("Arial", FontWeight.BOLD, 25);
    //Keep track of whether the score has increased or decreased
    boolean increaseScore = false;
    boolean decreaseScore = false;

    //Update score during the game
    public void updateScore(int scoreChange) {
        currentScore += scoreChange;
        //indicate whether the current score should
        //be printed with either +ve or -ve change
        if (scoreChange > 0) {
            increaseScore = true;
            decreaseScore = false;
        }
        else {
            increaseScore = false;
            decreaseScore = true;
        }
    }

    //Reset the total score
    public void resetScore() {currentScore = 0;}

    //get methods
    public Font getFont(){return scoreFont;}
    public int getTotalScore(){return currentScore;}

    public void drawWith(GraphicsContext thePen, Difficulty level){
        String score = "";
        //indicate the score change along with the current score
        if (increaseScore) {
            if (level == HARD) score =
                    String.format("Score: %d +2", currentScore);
            else  score =
                    String.format("Score: %d +1", currentScore);
        }
        else if (decreaseScore) {score =
                String.format("Score: %d -10", currentScore);}
        else score = "Score: " + currentScore;
        thePen.fillText(score, scoreX, scoreY);
        thePen.strokeText(score, scoreX, scoreY);
    }
}
