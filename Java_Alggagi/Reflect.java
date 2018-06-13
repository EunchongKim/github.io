import java.util.ArrayList;
import java.util.List;

/* Calculate a reflection of a shooting stone.
 * The fixPos is a bumped stone position,
 * the startPos is a shooting stone position,
 * and the bumpPos is a collision point.
 * Firstly, the angle between two vectors, shooting and bumped.
 * Secondly, check if the direction is clockwise or not.
 * Finally, find a right angle a shooting stone should move,
 * and return a position value.
 */
public class Reflect {
    final double pi = 3.1415926;
    List reflect(List<Double> fixPos, List<Double> startPos, List<Double> bumpPos, double time) {
        List<Double> newFixPos = new ArrayList<>(fixPos);
        List<Double> newStartPos = new ArrayList<>(startPos);
        List<Double> newBumpPos = new ArrayList<>(bumpPos);
        List<Double> vecShot = new ArrayList<>();
        List<Double> vecCon = new ArrayList<>();
        List<Double> vecRes = new ArrayList<>();
        vecShot.add(newBumpPos.get(0) - newStartPos.get(0));
        vecShot.add(newBumpPos.get(1) - newStartPos.get(1));
        vecCon.add(newFixPos.get(0) - newBumpPos.get(0));
        vecCon.add(newFixPos.get(1) - newBumpPos.get(1));

        boolean isClockwise = vecShot.get(0) * vecCon.get(1) - vecShot.get(1) * vecCon.get(0) > 0;

        double angleA = (vecShot.get(0) * vecCon.get(0) + vecShot.get(1) * vecCon.get(1)) /
                (Math.sqrt(vecShot.get(0) * vecShot.get(0) + vecShot.get(1) * vecShot.get(1))
                        *Math.sqrt(vecCon.get(0)* vecCon.get(0) + vecCon.get(1)* vecCon.get(1)));
        double angleB = pi  - 2 * Math.acos(angleA);
        angleB = isClockwise == false ? pi * 2 - angleB : angleB;
        vecRes.add(vecShot.get(0) * Math.cos(angleB) + vecShot.get(1) * Math.sin(angleB));
        vecRes.add(vecShot.get(1) * Math.cos(angleB) - vecShot.get(0) * Math.sin(angleB));
        double length = Math.sqrt(vecRes.get(0) * vecRes.get(0) + vecRes.get(1) * vecRes.get(1));
        vecRes.set(0, newBumpPos.get(0) + time * vecRes.get(0) / length);
        vecRes.set(1, newBumpPos.get(1) + time * vecRes.get(1) / length);
        return vecRes;
    }
}
