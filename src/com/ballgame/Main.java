package com.ballgame;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.ballgame.Difficulty.*;
import static com.ballgame.Direction.*;

/* (c) 2021 Vasily Inkovskiy */
public class Main extends Application {

    //private instance variableso % echo $PATH_TO_FX
      private Ball ball1 = new Ball();
      private Paddle paddle = new Paddle();
      private ScoreKeeper score = new ScoreKeeper();

      //Second ball is introduced to the game
      //if the user selects hard difficulty
      private Ball ball2 = new Ball();

      //difficulty level
      Difficulty level = EASY;

      //default background color
      private Color backgroundColour = Color.WHITE;

      private AnimationTimer timer; //for animating frame based motion
      private Timeline timeline; //for updating the score

        //GUI elements
        private Canvas canvas; //drawing canvas

        //GUI menus
        private MenuBar menubar = new MenuBar();
        private Menu fileMenu = new Menu("Ball Game");
        private Menu optionMenu = new Menu("Options");
        private ContextMenu contextMenu = new ContextMenu();
        private Menu difficultyLevel = new Menu("Difficulty Level");
        private ColorPicker colorPicker = new ColorPicker();
        private ToolBar toolBar = new ToolBar();
        private Label colourSelection = new Label("    " +
            "Background Colour" + "   ");


        private void buildMenus(Stage theStage){
        //build the menus for the menu bar

            //Build Run menu items
            MenuItem startMenuItem = new MenuItem("Start Game");
            optionMenu.getItems().addAll(startMenuItem);
            startMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    startAnimation();
                    repaintCanvas(canvas);
                }
            });
            MenuItem stopMenuItem = new MenuItem("Stop Game");
            optionMenu.getItems().addAll(stopMenuItem);
            stopMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    stopAnimation();
                    repaintCanvas(canvas);
                }
            });

            //Build Options menu items
            //Action menu button to reset score
            MenuItem resetScoreItem = new MenuItem("Reset");
            optionMenu.getItems().addAll(resetScoreItem);
            resetScoreItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    //reset the score
                    score.resetScore();
                    //return ball to its original position
                    //and reset its speed to the original value
                    ball1.returnToInitialPos();
                    ball1.changeBallColor();
                    ball1.setBallSpeed(score.getTotalScore());
                    //repeat the same for the second ball (if present)
                    if (level == HARD) {
                        ball2.returnToInitialPos();
                        ball2.changeBallColor();
                        ball2.setBallSpeed(score.getTotalScore());
                    }
                    repaintCanvas(canvas);
                }
            });

            //Build Difficulty Level menu items
            //Create mutually exclusive radio menu items
            //The instruction and code on how to create
            //radio menu items were taken from: http://tutorials.jenkov.com/javafx/menubar.html.
            RadioMenuItem easy = new RadioMenuItem("Easy");
            easy.setSelected(true); //select easy difficulty as the default value
            RadioMenuItem medium = new RadioMenuItem("Medium");
            RadioMenuItem hard = new RadioMenuItem("Hard");

            ToggleGroup difficultyLevels = new ToggleGroup();
            difficultyLevels.getToggles().addAll(easy, medium, hard);
            difficultyLevel.getItems().addAll(easy, medium, hard);
            //Modify difficulty level according to the user selection
            easy.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    level = EASY;
                    //increase the radius of the first ball
                    ball1.setBallRadius(level);
                    repaintCanvas(canvas);
                }
            });
            medium.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    level = MEDIUM;
                    //decrease the radius of the first ball
                    ball1.setBallRadius(level);
                    repaintCanvas(canvas);
                }
            });
            hard.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    level = HARD;
                    //decrease the radius of each ball
                    ball1.setBallRadius(level);
                    ball2.setBallRadius(level);
                    repaintCanvas(canvas);
                }
            });


            //Build File menu items
            MenuItem aboutMenuItem = new MenuItem("About This App");
            fileMenu.getItems().addAll(aboutMenuItem);
            aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Ver 1.0 \u00A9 V. Inkovskiy 2021");
                    alert.showAndWait();
                }
            });

            //Build Color Piker in the toolbar
            //to allow user to change background color
            colorPicker.getStyleClass().add("button");
            colorPicker.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    //Modify background colour depending on the user selection
                    backgroundColour = colorPicker.getValue();
                    repaintCanvas(canvas);
                }
            });

            //Build Popup context menu items
            MenuItem pauseContextMenuItem = new MenuItem("Pause Game");
            contextMenu.getItems().addAll(pauseContextMenuItem);
            pauseContextMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    stopAnimation();
                    repaintCanvas(canvas);
                }
            });

            MenuItem resumeContextMenuItem = new MenuItem("Resume Game");
            contextMenu.getItems().addAll(resumeContextMenuItem);
            resumeContextMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    startAnimation();
                    repaintCanvas(canvas);
                }
            });
        }

    //required by any Application subclass
        @Override
        public void start(Stage mainStage){

            //Here we do most of the initialization for the application
            //This method is called automatically as a result of
            //launching the application

            mainStage.setTitle("Ball Game"); //window title
            //set width and height of the stage,
            //so it can fit canvas and toolbar
            mainStage.setWidth(500);
            mainStage.setHeight(635);

            VBox root = new VBox(); //root node of scene graph
            Scene theScene = new Scene(root); //our GUI scene
            mainStage.setScene(theScene); //add scene to our app's stage

            //build application menus
            //add menus to menu bar object
            menubar.getMenus().addAll(fileMenu, optionMenu, difficultyLevel);
            //add Color Picker to the toolbar
            toolBar.getItems().addAll(colourSelection, colorPicker);

            //add menu bar object to application scene root
            canvas = new Canvas(500,600); //GUI element we will draw on
            root.getChildren().addAll(menubar, toolBar, canvas); //add menubar to GUI
            buildMenus(mainStage); //add menu items to menus

            //add mouse event handler (for popup menu)
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
                        handleMousePressedEvent(e);
                    }
            );
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
                        handleMouseReleasedEvent(e);
                    }
            );
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
                        handleMouseDraggedEvent(e);
                    }
            );

            canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {
                    //listen for arrow keys using key press event
                    //arrow keys don't show up in KeyTyped events
                    String text = "";
                    double moveIncrement = 45;
                    // move paddle with the arrow keys
                    // check whether the paddle moves out of the canvas bounds
                    if (ke.getCode() == KeyCode.RIGHT) {
                        paddle.rightMoveALongX(moveIncrement);
                        text += "RIGHT";
                    }
                    else if (ke.getCode() == KeyCode.LEFT) {
                        paddle.leftMoveAlongX(moveIncrement);
                        text += "LEFT";
                    }
                    else if (ke.getCode() == KeyCode.UP) {
                        paddle.upMoveALongY(moveIncrement);
                        text += "UP";
                    }
                    else if (ke.getCode() == KeyCode.DOWN) {
                        paddle.downMoveAlongY(moveIncrement);
                        text += "DOWN";
                     }
                    System.out.println("key press: " + text);
                    ke.consume(); //don't let keyboard event propagate
                }
            });


            timer = new AnimationTimer() { //e.g. of anonymous inner subclass
                @Override
                public void handle(long nowInNanoSeconds) {
                    //this method will be called about 60 times per second
                    //which is default behaviour of the AnimationTimer class

                    //advance the ball
                    ball1.advance();

                    // check if the main ball should bounce off canvas slides
                    if (ball1.getCenterX() + ball1.getRadius() > canvas.getWidth()) ball1.setDirectionX(LEFT);
                    if (ball1.getCenterX() - ball1.getRadius() < 0) ball1.setDirectionX(RIGHT);
                    if (ball1.getCenterY() - ball1.getRadius() < 0) ball1.setDirectionY(DOWN);

                    if (ball1.getCenterY() + ball1.getRadius() > canvas.getHeight()){
                        //change ball colour and return it to the original position
                        ball1.returnToInitialPos();
                        ball1.changeBallColor();
                        //decrease the score if the falls off the bottom of the canvas
                        score.updateScore(-10);
                    }

                    repaintCanvas(canvas); //refresh our canvas rendering

                    //Build a circle object to represent the ball
                    Circle theBall = ball1.ballRepresentation();

                    //Bounce off the ball from the paddle
                    if (paddle.checkTopCollision(theBall)) ball1.setDirectionY(UP);
                    if (paddle.checkBottomCollision(theBall)) ball1.setDirectionY(DOWN);
                    if (paddle.checkLeftCollision(theBall)) ball1.setDirectionX(LEFT);
                    if (paddle.checkRightCollision(theBall)) ball1.setDirectionX(RIGHT);

                    repaintCanvas(canvas); //refresh our canvas rendering

                    //repeat the same procedures for the second ball
                    //if it is present in the game
                    if (level == HARD){
                        //advance the second ball
                        ball2.advance();

                        // check if the second ball should bounce off canvas slides
                        if (ball2.getCenterX() + ball2.getRadius() > canvas.getWidth()) ball2.setDirectionX(LEFT);
                        if (ball2.getCenterX() - ball2.getRadius() < 0) ball2.setDirectionX(RIGHT);
                        if (ball2.getCenterY() - ball2.getRadius() < 0) ball2.setDirectionY(DOWN);

                        if (ball2.getCenterY() + ball2.getRadius() > canvas.getHeight()) {
                            //change ball colour and return it to the original position
                            ball2.returnToInitialPos();
                            ball2.changeBallColor();
                            //decrease the score if the falls off the bottom of the canvas
                            score.updateScore(-10);
                        }

                        repaintCanvas(canvas); //refresh our canvas rendering

                        //Build a circle object to represent the ball
                        Circle theBall2 = ball2.ballRepresentation();

                        //Bounce off the ball from the paddle
                        if (paddle.checkTopCollision(theBall2)) ball2.setDirectionY(UP);
                        if (paddle.checkBottomCollision(theBall2)) ball2.setDirectionY(DOWN);
                        if (paddle.checkLeftCollision(theBall2)) ball2.setDirectionX(LEFT);
                        if (paddle.checkRightCollision(theBall2)) ball2.setDirectionX(RIGHT);

                        repaintCanvas(canvas); //refresh our canvas rendering
                    }
                }
            };

            //create timeline to adjust the score
            //every half-a-second the score increases by one
            //also increase the ball speed if the score increases
            //code for the timeline was taken from: https://bit.ly/3zIjIWf
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                if (level == HARD) {
                    //increase score by 2 if there are two balls in the game
                    score.updateScore(2);
                    ball2.setBallSpeed(score.getTotalScore());
                }
                else score.updateScore(1); //increase score by 1
                ball1.setBallSpeed(score.getTotalScore());
            }));
            timeline.setCycleCount(Animation.INDEFINITE);

            startAnimation(); //start the animation timer

            mainStage.show(); //show the application window
            repaintCanvas(canvas); //do initial repaint
        }

    private void startAnimation(){
        timer.start();
        timeline.play(); //start timeline animation for score change
    }
    private void stopAnimation(){
        timer.stop();
        timeline.pause(); //stop timeline animation for score change
    }

    private void handleMousePressedEvent(MouseEvent e){
        //mouse handler for canvas
        canvas.requestFocus(); //set canvas to receive keyboard events

        //Windows uses mouse release popup trigger
        //Mac uses mouse press popup trigger
        if(e.isPopupTrigger()) {
            contextMenu.show(canvas, e.getScreenX(), e.getScreenY());
        } else {
            contextMenu.hide(); //in case it was left open

            //print out mouse locations for inspection and debugging
            System.out.println("mouse scene: " +
                            e.getSceneX() +
                            "," +
                            e.getSceneY()
            );
            System.out.println("mouse screen: " +
                            e.getScreenX() +
                            "," +
                            e.getScreenY()
            );
            System.out.println("mouse get: " +
                            e.getX() +
                            "," +
                            e.getY()
            );
        }
        repaintCanvas(canvas); //update the GUI canvas
    }

    private void handleMouseReleasedEvent(MouseEvent e){
        //Windows uses mouse release popup trigger
        //Mac uses mouse press popup trigger
        if(e.isPopupTrigger())
            contextMenu.show(canvas, e.getScreenX(), e.getScreenY());

        repaintCanvas(canvas);
    }
    private void handleMouseDraggedEvent(MouseEvent e){
            //nothing to do here
        repaintCanvas(canvas);
    }

    private void repaintCanvas(Canvas aCanvas){
            //repaint the contents of our GUI canvas

            //obtain the graphics context for drawing on the canvas
            GraphicsContext thePen = aCanvas.getGraphicsContext2D();

            //clear the canvas
            double canvasWidth = aCanvas.getWidth();
            double canvasHeight = aCanvas.getHeight();
            thePen.setFill(backgroundColour); //change background colour of the canvas
            thePen.fillRect(0, 0, canvasWidth, canvasHeight);

            //set graphics context for drawing on canvas
            thePen.setFill(Color.RED);
            thePen.setStroke(Color.BLACK);
            thePen.setLineWidth(1);
            thePen.setFont(score.getFont());

            //draw the score
            score.drawWith(thePen, level);

            //draw the first ball
            ball1.drawWith(thePen);

            //draw the second ball if the user has selected hard difficulty
            if (level == HARD) ball2.drawWith(thePen);

            //draw the paddle
            paddle.drawWith(thePen);

            canvas.requestFocus(); //request keyboard focus
        }

    public static void main(String[] args) {
            //entry point for javaFX application
            System.out.println("starting main application");
            launch(args); //will cause application to start and
                          // run it's own start() method
            System.out.println("main application is finished");
        }


}
