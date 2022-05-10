package com.vinkovskiy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.vinkovskiy.Difficulty.*;
import static com.vinkovskiy.Direction.*;

public class Ball {
    private double radius = 50; //original radius of ball
    private double centerX = 100; //center X
    private double centerY = 100; //center Y
    private double speed = 3; //pixels/timer event
    private double directionX = 1; //1=>right, -1=>left
    private double directionY = 1; //1=>downward, -1=>upward,
    private Color ballColor = Color.RED;

    //get methods for the ball
    public double getCenterX(){return centerX;}
    public double getCenterY(){return centerY;}
    public double getRadius(){return radius;}

    public void returnToInitialPos(){
        centerX = 100;
        centerY = 100;
    }

    public void advance() {
        centerX += speed*directionX;
        centerY += speed*directionY;
    }

    //set methods for the ball
    public void setDirectionX(Direction aDirection) {
        if (aDirection == RIGHT) directionX = 1;
        if (aDirection == LEFT) directionX = -1;
    }
    public void setDirectionY(Direction aDirection) {
        if (aDirection == DOWN) directionY = 1;
        if (aDirection == UP) directionY = -1;
    }

    //adjust the ball speed depending on the score
    public void setBallSpeed(int currentScore) {
        if (currentScore <= 30) speed = 3;
        else if (currentScore <= 60) speed = 3.5;
        else if (currentScore <= 90) speed = 4;
        else if (currentScore <= 120) speed = 4.5;
        else if (currentScore <= 150) speed = 5;
        else if (currentScore <= 180) speed = 5.5;
        else if (currentScore <= 210) speed = 6;
        else if (currentScore <= 240) speed = 6.5;
        else speed = 7;
    }
    //set random ball color
    //original code was taken from: https://bit.ly/3yfxS0Q
    public void changeBallColor(){
        ballColor = Color.color(Math.random(),Math.random(),Math.random(),1.0);
    }
    //decrease ball radius depending on the difficulty level
    public void setBallRadius(Difficulty level){
        if (level == EASY) radius = 50;
        else radius = 25;
    }

    public void drawWith(GraphicsContext thePen){
        thePen.setFill(ballColor);
        thePen.fillOval(centerX-radius, //upper left X
                        centerY-radius, //upper left Y
                        2*radius, //width
                        2*radius); //height
    }

    //build a circle representing the ball dimensions
    public Circle ballRepresentation(){
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setFill(ballColor);
        return circle;
    }
}
