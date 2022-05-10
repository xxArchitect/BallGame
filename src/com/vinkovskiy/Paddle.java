package com.vinkovskiy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Paddle {
    private double topLeftX = 200; //center X
    private double topLeftY = 450; //center Y
    private double width = 100; // paddle width
    private double height = 25; // paddle height

    //Move the paddle according to the user's arrow key input
    public void leftMoveAlongX(double aDirection) {topLeftX -= aDirection;}
    public void rightMoveALongX(double aDirection) {topLeftX += aDirection;}
    public void downMoveAlongY(double aDirection) {topLeftY += aDirection;}
    public void upMoveALongY(double aDirection) {topLeftY -= aDirection;}

    public void drawWith(GraphicsContext thePen){
        thePen.setFill(Color.GREEN);
        thePen.fillRect(topLeftX, //upper left X
                        topLeftY, //upper left Y
                        width, //width of paddle
                        height); //height of paddle
    }

    //Build the boundaries of the paddle to check
    //if the ball collides with them and return true
    //if there is a collision
    //The code used to check whether the two objects collide
    //was taken from: https://bit.ly/3iMq6Fa
    public boolean checkTopCollision(Circle ball){
        Line top = new Line();
        top.setStartX(topLeftX);
        top.setEndX(topLeftX + width);
        top.setStartY(topLeftY);
        top.setEndY(topLeftY);

        Shape topCollision = Shape.intersect(ball, top);
        if (topCollision.getBoundsInParent().getWidth() > 0){
            return true;
        }
        else return false;
    }

    public boolean checkBottomCollision(Circle ball){
        Line bottom = new Line();
        bottom.setStartX(topLeftX);
        bottom.setEndX(topLeftX + width);
        bottom.setStartY(topLeftY + height);
        bottom.setEndY(topLeftY + height);

        Shape bottomCollision = Shape.intersect(ball, bottom);
        if (bottomCollision.getBoundsInParent().getWidth() > 0){
            return true;
        }
        else return false;
    }

    public boolean checkLeftCollision(Circle ball){
        Line leftSide = new Line();
        leftSide.setStartX(topLeftX);
        leftSide.setEndX(topLeftX);
        leftSide.setStartY(topLeftY);
        leftSide.setEndY(topLeftY + height);

        Shape leftCollision = Shape.intersect(ball, leftSide);
        if (leftCollision.getBoundsInParent().getWidth() > 0){
            return true;
        }
        else return false;
    }

    public boolean checkRightCollision(Circle ball){
        Line rightSide = new Line();
        rightSide.setStartX(topLeftX + width);
        rightSide.setEndX(topLeftX + width);
        rightSide.setStartY(topLeftY);
        rightSide.setEndY(topLeftY + height);

        Shape rightCollision = Shape.intersect(ball, rightSide);
        if (rightCollision.getBoundsInParent().getWidth() > 0) {
            return true;
        }
        else return false;
    }
}
