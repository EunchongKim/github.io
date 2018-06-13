import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/* Check a collision among stones.
 * If a collision happens, stop the previous timeline,
 * and calculate new positions of shooting stone and bumped stone.
 */
public class CollisionCheck {

    public void isBound(Shape thisOne, List<Shape> prevOtherStones, List<Double> prevThisOnePos,
                        Shape thisOneCheck, Double timeDiff, Timeline timeline) {
        List<Shape> otherStones = new ArrayList<>(prevOtherStones);
        List<Double> thisOnePos = new ArrayList<>(prevThisOnePos);
        for (final Shape otherStone : otherStones) {
            if (((Path) Shape.intersect(thisOne, otherStone)).getElements().size() > 0) {
                if (checkConstraint(thisOneCheck, thisOne, otherStone)) {
                    timeline.stop();
                    List<Double> otherPos = new ArrayList<>();
                    otherPos.add(otherStone.getLayoutX());
                    otherPos.add(otherStone.getLayoutY());

                    List<Double> bumpPos = new ArrayList<>();
                    bumpPos.add(thisOne.getLayoutX());
                    bumpPos.add(thisOne.getLayoutY());

                    List<Double> newThisPos = new Reflect().reflect(otherPos, thisOnePos,
                            bumpPos, timeDiff / 150);

                    timeline.getKeyFrames().set(0, new KeyFrame(Duration.millis(100),
                            new KeyValue(thisOne.layoutXProperty(), newThisPos.get(0)),
                            new KeyValue(thisOne.layoutYProperty(), newThisPos.get(1))));

                    List<Double> startPos = new ArrayList<>();
                    startPos.add(Shape.intersect(thisOne, otherStone).boundsInParentProperty().
                            getValue().getMinX());
                    startPos.add(Shape.intersect(thisOne, otherStone).boundsInParentProperty().
                            getValue().getMinY());

                    List<Double> pushStone = new Eject().eject(startPos, otherPos,
                            timeDiff / 2);

                    timeline.getKeyFrames().set(1, new KeyFrame(Duration.millis(100),
                            new KeyValue(otherStone.layoutXProperty(), pushStone.get(0)),
                            new KeyValue(otherStone.layoutYProperty(), pushStone.get(1))));
                    timeline.play();
                }
            }
        }
    }

    Boolean checkConstraint (Shape thisOneCheck, Shape thisOne, Shape otherStone){
        if (! thisOneCheck.equals(otherStone)) {
            return true;
        }
        if (thisOneCheck.equals(thisOne)) {
            return true;
        } else return false;
    }
}
