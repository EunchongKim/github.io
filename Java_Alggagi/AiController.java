import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Toggle turns of player and AI, and make AI move.
 * Calculate each score of player stones for each AI stone
 * based on gaps between expected player stone position after shooting
 * and the border lines.
 */
public class AiController {

    private List<Shape> whiteStones;
    private List<Shape> blackStones;
    private Pane stones;
    private Stage stage;
    private Pane root;
    final double border = 600.0;
    final double maxSpeed = 2500.0;
    private double AiSpeed = 2000.0;

    AiController(List<Shape> whiteStones, List<Shape> blackStones, Pane root, Pane stones, Stage stage) {
        this.whiteStones = new ArrayList<>(whiteStones);
        this.blackStones = new ArrayList<>(blackStones);
        this.stones = stones;
        this.stage = stage;
        this.root = root;
    }

    public void CallPlayer() {
        MoveController makeMove = new MoveController(whiteStones, blackStones, root, stones, stage);
        List<Shape> allStones = new ArrayList<>();
        allStones.addAll(whiteStones);
        allStones.addAll(blackStones);

        for (Shape stone : allStones) {
            stone.setOnMousePressed(e -> makeMove.moveControl());
            stone.setOnMouseReleased(e -> {
                Task<Void> sleeper = new Task<>() {
                    @Override
                    protected Void call() {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            System.out.println("Sleep failed");
                        }
                        return null;
                    }
                };
                sleeper.setOnSucceeded(e1 -> AiMoveControl());
                new Thread(sleeper).start();
            });
        }
    }

    public void AiMoveControl() {
        CheckMove checkMove = new CheckMove(whiteStones, blackStones, stones, stage);
        checkMove.checkOut();
        whiteStones = new ArrayList<>(checkMove.returnWhite());
        blackStones = new ArrayList<>(checkMove.returnBlack());

        List<Double> expectScore = new ArrayList<>();
        List<List<Shape>> expectStoneLists = new ArrayList<>();
        List<Double> finalPos;
        Eject ejects = new Eject();

        for (Shape AiStone : whiteStones) {
            List<Double> thisAiPos = new ArrayList<>();
            thisAiPos.add(AiStone.getLayoutX());
            thisAiPos.add(AiStone.getLayoutY());
            for (Shape playerStone : blackStones) {
                List<Double> thisPlayerPos = new ArrayList<>();
                thisPlayerPos.add(playerStone.getLayoutX());
                thisPlayerPos.add(playerStone.getLayoutY());
                List<Double> expectVal = ejects.eject(thisAiPos, thisPlayerPos, maxSpeed);
                List<Double> gap = new ArrayList<>();
                gap.add(Math.abs(expectVal.get(0) - 0.0));
                gap.add(Math.abs(expectVal.get(0) - border));
                gap.add(Math.abs(expectVal.get(1) - 0.0));
                gap.add(Math.abs(expectVal.get(1) - border));

                if (expectVal.get(0) < 0 || expectVal.get(0) > border ||
                        expectVal.get(1) < 0 || expectVal.get(1) > border) {
                    expectScore.add(100.0 * Collections.min(gap));
                }
                else {
                    expectScore.add(10000.0 * 1/Collections.min(gap));
                }
                List<Shape> thisList = new ArrayList<>();
                thisList.add(AiStone);
                thisList.add(playerStone);
                expectStoneLists.add(thisList);
            }
        }

        if(expectStoneLists.size() > 0) {
            int maxAt = expectScore.indexOf(Collections.max(expectScore));
            AiSpeed = Collections.max(expectScore) / 5;
            AiSpeed = AiSpeed > 2500.0 ? 2500.0 : AiSpeed;
            List<Double> tempAi = new ArrayList<>();
            tempAi.add(expectStoneLists.get(maxAt).get(0).getLayoutX());
            tempAi.add(expectStoneLists.get(maxAt).get(0).getLayoutY());
            List<Double> tempPlayer = new ArrayList<>();
            tempPlayer.add(expectStoneLists.get(maxAt).get(1).getLayoutX());
            tempPlayer.add(expectStoneLists.get(maxAt).get(1).getLayoutY());

            finalPos = ejects.eject(tempPlayer, tempAi, AiSpeed);
            finalPos = ejects.eject(finalPos, tempAi, AiSpeed);

            Shape thisAbsOne = expectStoneLists.get(maxAt).get(0);
            List<Shape> allStones = new ArrayList<>();
            allStones.addAll(whiteStones);
            allStones.addAll(blackStones);

            List<Shape> otherStones = new ArrayList<>();
            for (Shape shape : allStones) {
                if (!shape.equals(thisAbsOne)) {
                    otherStones.add(shape);
                }
            }

            final Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(true);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                    new KeyValue(thisAbsOne.layoutXProperty(), finalPos.get(0)),
                    new KeyValue(thisAbsOne.layoutYProperty(), finalPos.get(1))));
            timeline.play();
            timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO));

            thisAbsOne.boundsInParentProperty().addListener((observableValue, oldValue, newValue) -> {
                CollisionCheck newCollision = new CollisionCheck();
                newCollision.isBound(thisAbsOne, otherStones, tempAi,
                        thisAbsOne, AiSpeed, timeline);
            });
        }
    }
}

