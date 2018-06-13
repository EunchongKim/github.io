import java.io.*;
import java.util.*;

/* Hold each record and read it out.
 * Testing is possible in two ways: using command line
 * and an automated unit testing. */
class Record {

    private List<String> data = new ArrayList<String>();
    private String belong;

    Record(String belong) {
        this.belong = belong;
    }

    void add(String datain) {
        try {
            data = Arrays.asList(datain.split(", "));
        } catch (Exception e) { throw new Error(e); }  
    }

    String gets(int i) {
        return data.get(i);
    }

    private void read() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("HomeDB > ");
        String line = scanner.nextLine();
        add(line);
    }

    private void run() {
        while (true) {
            read();
            for(int i=0;i<data.size();i++) {
                System.out.print(gets(i));
                if(i!=data.size()-1) System.out.print(", ");
            }
            System.out.println();
        }
    }

    void claim(boolean b) {
        if (!b) throw new Error("Test " + testNumber + " fails");
        testNumber++;
    }
    private int testNumber = 1;

    private void test() {
        System.out.println(">>Test strat<<");
        String test0 = "1, Fido, dog, ab123";
        String test1 = "2, Wanda, fish, ef789";
        String test2 = "3, Garfield, cat, ab123";
        fortest(test0);
        fortest(test1);
        fortest(test2);
        System.out.println(">>Test end<<");
    }

    private void fortest(String test0) {
        String[] test1 = test0.split(", ");
        Record test2 = new Record("test0");
        test2.add(test0);
        for(int i=0;i<test1.length;i++) {
            claim(test2.gets(i).equals(test1[i]));
        }   
    }

    public static void main(String[] args) {
        Record record = new Record("test");
        record.test();
        record.run();
    }
}