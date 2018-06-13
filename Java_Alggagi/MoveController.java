import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/* Player stone controller.
 * When a stone is clicked, check the time.
 * When it is dragged, draw a line.
 * When it is released, make a stone move
 * according to the calculated trajectory
 */
public class MoveController {
    private List<Shape> whiteStones;
    private List<Shape> blackStones;
    private Pane root;
    private Pane stones;
    private Stage stage;
    private List<Shape> allStones = new ArrayList<>();
    private List<Shape> thisOneCheck = new ArrayList<>();
    final double maxSpeed = 2500.0;

    MoveController(List<Shape> whiteStones, List<Shape> blackStones,
                   Pane root, Pane stones, Stage stage) {
        this.whiteStones = new ArrayList<>(whiteStones);
        this.blackStones = new ArrayList<>(blackStones);
        this.root = root;
        this.stones = stones;
        this.stage = stage;
    }

    public void moveControl() {
        List<Long> timeList = new ArrayList<>();
        List<Double> timeDiff = new ArrayList<>();
        // If it is not set before, segmentation fault
        timeList.add(System.currentTimeMillis());
        timeList.add(System.currentTimeMillis());
        timeDiff.add(0.0);

        if (Alggagi.aiPlayMode) {
            allStones.addAll(blackStones);
        }
        else {
            allStones.addAll(whiteStones);
            allStones.addAll(blackStones);
        }

        thisOneCheck.add(new Circle());
        thisOneCheck.add(new Circle());

        for (Shape thisOne : allStones) {
            thisOne.setOnMousePressed(e -> {
                timeDiff.set(0, 0.0);
                timeList.set(0, System.currentTimeMillis());
                thisOneCheck.set(0, thisOne);
                CheckMove checkMove = new CheckMove(whiteStones, blackStones, stones, stage);
                checkMove.checkOut();
                whiteStones = new ArrayList<>(checkMove.returnWhite());
                blackStones = new ArrayList<>(checkMove.returnBlack());
            });

            thisOne.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
                double X = e.getSceneX(), Y = e.getSceneY();
                drawLine(X, Y);
            });

            thisOne.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                root.getChildren().clear();
                List<Shape> otherStones = new ArrayList<>();
                List<Shape> allMakeLists = new ArrayList<>();
                allMakeLists.addAll(whiteStones);
                allMakeLists.addAll(blackStones);
                for (Shape makeList : allMakeLists) {
                    if (!makeList.equals(thisOne)) {
                        otherStones.add(makeList);
                    }
                }
                timeList.set(1, System.currentTimeMillis());
                timeDiff.set(0, (double) (timeList.get(1) - timeList.get(0)));
                if (timeDiff.get(0) > maxSpeed) {
                    timeDiff.set(0, maxSpeed);
                }

                List<Double> endPoint = new ArrayList<>();
                endPoint.add(e.getSceneX());
                endPoint.add(e.getSceneY());

                List<Double> thisOnePos = new ArrayList<>();
                thisOnePos.add(thisOne.getLayoutX());
                thisOnePos.add(thisOne.getLayoutY());

                List<Double> newThisOnePos = new Eject().eject(endPoint, thisOnePos, timeDiff.get(0));

                final Timeline timeline = new Timeline();
                timeline.setCycleCount(1);
                timeline.setAutoReverse(true);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                        new KeyValue(thisOne.layoutXProperty(), newThisOnePos.get(0)),
                        new KeyValue(thisOne.layoutYProperty(), newThisOnePos.get(1))));
                timeline.play();
                timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO));

                thisOne.boundsInParentProperty().addListener((observableValue, oldValue, newValue) -> {
                    CollisionCheck newCollision = new CollisionCheck();
                    newCollision.isBound(thisOne, otherStones, thisOnePos,
                            thisOneCheck.get(0), timeDiff.get(0), timeline);
                });
            });
        }
    }

    private void drawLine ( double X, double Y) {
        Line line = new Line();
        line.setStrokeWidth(3.0f);
        line.setStartX(X);
        line.setStartY(Y);
        line.setEndX(X);
        line.setEndY(Y);
        line.getStyleClass().add("Line");
        root.getChildren().add(line);
    }
}
