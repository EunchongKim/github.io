import java.util.ArrayList;
import java.util.List;

/* Calculate a bumped stone trajectory.
 * The endPos is a final point of a dragged line,
 * the currentPos is a current position of a stone.
 */
public class Eject {
    List eject (List<Double> endPos, List<Double> currentPos, double time) {
        List<Double> newEndPos = new ArrayList<>(endPos);
        List<Double> newCurrentPos = new ArrayList<>(currentPos);

        List<Double> res = new ArrayList<>();
        double difX = newCurrentPos.get(0) - newEndPos.get(0), difY = newCurrentPos.get(1) - newEndPos.get(1);
        double length = Math.sqrt(difX*difX + difY*difY);

        res.add(newCurrentPos.get(0) + time * difX / length / 10);
        res.add(newCurrentPos.get(1) + time * difY / length / 10);
        return res;
    }
}
