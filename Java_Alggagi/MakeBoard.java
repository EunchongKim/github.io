import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/* Make a board and set initial positions of stones.
 * Control self play mode and AI mode.
 * If self play mode, move on to MoveController class.
 * If AI mode, move on to AiController class.
 */
public class MakeBoard extends Application {

    private Pane root = new Pane();
    private Pane stones = new Pane();
    private List<Shape> whiteStones = new ArrayList<>();
    private List<Shape> blackStones = new ArrayList<>();
    final int border = 600;
    public void start(Stage stage) {
        final int radius = 20;

        GridPane forLine = new GridPane();
        initFullGrid(forLine, 15, 40, 40);
        Image backGround0 = new Image("css/background.png");
        ImageView backGround = new ImageView(backGround0);
        backGround.setFitHeight(border);
        backGround.setFitWidth(border);

        Image whiteImage0 = new Image("/css/romanescowhite.png");
        ImagePattern whitePattern = new ImagePattern(whiteImage0);
        Circle whiteOne = new Circle(radius);
        whiteOne.setFill(whitePattern);
        whiteOne.getStyleClass().add("stone");

        Circle whiteTwo = new Circle(radius);
        whiteTwo.setFill(whitePattern);
        whiteTwo.getStyleClass().add("stone");

        Circle whiteThree = new Circle(radius);
        whiteThree.setFill(whitePattern);
        whiteThree.getStyleClass().add("stone");

        Image blackImage0 = new Image("/css/romanescoblack.png");
        ImagePattern blackPattern = new ImagePattern(blackImage0);
        Circle blackOne = new Circle(radius);
        blackOne.setFill(blackPattern);
        blackOne.getStyleClass().add("stone");

        Circle blackTwo = new Circle(radius);
        blackTwo.setFill(blackPattern);
        blackTwo.getStyleClass().add("stone");

        Circle blackThree = new Circle(radius);
        blackThree.setFill(blackPattern);
        blackThree.getStyleClass().add("stone");

        whiteStones.add(whiteOne);
        whiteStones.add(whiteTwo);
        whiteStones.add(whiteThree);
        blackStones.add(blackOne);
        blackStones.add(blackTwo);
        blackStones.add(blackThree);

        List<Shape> allStones = new ArrayList<>();
        allStones.addAll(whiteStones);
        allStones.addAll(blackStones);

        stones.getChildren().addAll(whiteOne, whiteTwo, whiteThree,
                blackOne, blackTwo, blackThree);
        Group Board = new Group(backGround, stones, root);

        blackOne.setLayoutX(110.0);
        blackOne.setLayoutY(490.0);
        blackTwo.setLayoutX(300.0);
        blackTwo.setLayoutY(490.0);
        blackThree.setLayoutX(490.0);
        blackThree.setLayoutY(490.0);

        whiteOne.setLayoutX(110.0);
        whiteOne.setLayoutY(110.0);
        whiteTwo.setLayoutX(300.0);
        whiteTwo.setLayoutY(110.0);
        whiteThree.setLayoutX(490.0);
        whiteThree.setLayoutY(110.0);

        Scene foreScene = new Scene(Board);
        foreScene.getStylesheets().add("css/Style.css");

        if (! Alggagi.aiPlayMode) {
            MoveController makeMove = new MoveController(whiteStones, blackStones, root, stones, stage);
            makeMove.moveControl();
        }
        else {
            AiController makeAiMove = new AiController(whiteStones, blackStones, root, stones, stage);
            makeAiMove.CallPlayer();
        }
        stage.setScene(foreScene);
        stage.setTitle("Romanesco Alggagi");
        stage.show();
    }

    void initFullGrid(GridPane grid, int i, double width, double height) {
        for (int j=0; j<i; j++) {
            ColumnConstraints column = new ColumnConstraints(width);
            RowConstraints row = new RowConstraints(height);
            grid.getColumnConstraints().add(column);
            grid.getRowConstraints().add(row);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
