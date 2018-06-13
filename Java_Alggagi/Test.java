import java.util.*;

/* Test eject and reflect functions.
 */
public class Test {

    private int testNumber = 1;

    void claim(boolean b) {
        if (!b) throw new Error("Test " + testNumber + " fails");
        testNumber++;
    }

    // Test the Eject class (from 0 to 3)
    void testEject() {
        Eject testEject = new Eject();
        List<Double> endPoint = new ArrayList<>();
        List<Double> currentPoint = new ArrayList<>();
        endPoint.add(0.0);
        endPoint.add(0.0);

        currentPoint.add(0.0);
        currentPoint.add(1.0);
        List<Double> vecRes = testEject.eject(endPoint, currentPoint, 10);
        double x = vecRes.get(0);
        double y = vecRes.get(1);
        claim(sameDouble(x, 0));
        claim(sameDouble(y, 2.0));

        currentPoint.clear();
        currentPoint.add(1.0);
        currentPoint.add(1.0);
        vecRes = testEject.eject(endPoint, currentPoint, 10);
        x = vecRes.get(0);
        y = vecRes.get(1);
        claim(sameDouble(x, 1.707));
        claim(sameDouble(y, 1.707));

        System.out.println("Test for eject finished!");
    }

    // Test the Reflect class (from 4 to 6)
    void testReflect() {
        Reflect testReflect = new Reflect();
        List<Double> fixPos = new ArrayList<>();
        List<Double> startPos = new ArrayList<>();
        List<Double> bumpPos = new ArrayList<>();
        fixPos.add(0.0);
        fixPos.add(0.0);
        startPos.add(-1.0);
        startPos.add(2.732);
        bumpPos.add(0.0);
        bumpPos.add(1.0);
        List<Double> vecRes = testReflect.reflect(fixPos, startPos, bumpPos, 1);
        double x = vecRes.get(0);
        double y = vecRes.get(1);
        double tanA = (y - 1) / x;
        claim(sameDouble(tanA, 1.732));

        startPos.clear();
        startPos.add(-1.0);
        startPos.add(1.0);
        vecRes = testReflect.reflect(fixPos, startPos, bumpPos, 1);
        x = vecRes.get(0);
        y = vecRes.get(1);
        tanA = (y - 1) / x;
        claim(sameDouble(tanA, 0));

        startPos.clear();
        startPos.add(-1.0);
        startPos.add(2.0);
        vecRes = testReflect.reflect(fixPos, startPos, bumpPos, 1);
        x = vecRes.get(0);
        y = vecRes.get(1);
        tanA = (y - 1) / x;
        claim(sameDouble(tanA, 1));

        System.out.println("Test for reflect function finished!");
    }

    // This is for comparing whether two double numbers are same or not
    boolean sameDouble(double num1, double num2) {
        double diff = num1 - num2;
        double limit = 0.001;
        if (diff < limit && diff > limit * -1) {
            return true;
        }
        else {
            return false;
        }
    }

    void test() {
        testEject();
        testReflect();
        System.out.println("Test finished!");
    }

    void run(String[] args){
        if(args.length > 0){
            System.err.println("There should no argument");
            System.exit(1);
        }
        test();
    }

    public static void main(String[] args){
        Test program = new Test();
        program.run(args);
    }
}
