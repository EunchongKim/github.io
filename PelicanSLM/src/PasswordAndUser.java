import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class PasswordAndUser {
    private List<String> stupidpasswords = Arrays.asList("123456", "password", "12345678", "qwerty", "1234567",
            "123456789", "letmein", "1234567", "football", "IloveYou!", "admin", "welcome", "login",
            "monkey", "abc123", "starwars", "123123", "dragon", "passw0rd", "master", "hello", "freedom",
            "whatever", "qazwsx", "trustno1");

    // only character, number, _ and - allowed
    // if valid return 0; if not return positive number
    // 1 means invalid
    // 2 means already exit
    int isValidUserName(String name) {
        String userPattern = "^[a-zA-Z0-9_-]{2,16}$";
        if (!Pattern.matches(userPattern, name)) {
            return 1;
        }
        else if (checkSameName (name)) {
            return 2;
        }
        else {
            return 0;
        }
    }

    boolean checkSameName (String name) {
        try {
            File file = new File(System.getProperty("user.dir") + "/src/gameData/userInfo.txt");
            Scanner readin = new Scanner(file);
            while(readin.hasNext()) {
                String nameAndKey = readin.nextLine();
                if (name.equals(nameAndKey.split(" ")[0])) {
                    return true;
                }
            }
            return false;

        }
        catch (Exception e) {
            System.out.println("Warning: can't open");
            return false;
        }
    }

    boolean checkUserToKey(String name, String key) {
        try {
            File file = new File(System.getProperty("user.dir") + "/src/gameData/userInfo.txt");
            Scanner readin = new Scanner(file);
            while(readin.hasNext()) {
                String nameAndKey = readin.nextLine();
                if (name.equals(nameAndKey.split(" ")[0])) {
                    return hash(key).equals(nameAndKey.split(" ")[1]);
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Warning: fail to read the file!");
            return false;
        }
    }

    //no four continuous characters from your birthday

    //if return 1, means you choose the most famous stupid password
    //if return 2, means that you choose an invalid password
    //if return 3, means relevent to birthday
    //if return 0, means valid

    int isValidPassword (String passward, String birth) {
        //if(passward.equals("passward"))
        String passPattern = "^.*(?=.{8,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$";
        if (stupidpasswords.contains(passward)) {
            return 1;
        }
        else if(isRelev(passward, birth))
            return 3;

        else if(Pattern.matches(passPattern, passward)) {
            return 0;
        }
        else {
            return 2;
        }
    }
    //"Alice1505!", "15051992"
    boolean isRelev(String pass, String birth) {
        if (pass == null || birth == null) return false;
        String reg = "[^0-9]";
        Matcher m = Pattern.compile(reg).matcher(pass);
        pass = m.replaceAll("").trim();
        if (pass.length() > birth.length()) {
            return false;
        }
        if (pass.contains(birth.substring(0, 4))) return true;
        if (pass.contains(birth.substring(4, 8))) return true;
        if (pass.contains(birth.substring(2,4) + birth.substring(0, 2))) return true;
        if (pass.contains(birth.charAt(3) + birth.substring(0, 2))) return true;
        //System.out.println(birth.charAt(3) + birth.substring(0, 2));
        return false;
    }


    private int testNumber = 0;

    void claim(boolean b) {
        if (!b) throw new Error("Test " + testNumber + " fails");
        testNumber++;
    }

    private void test_username() {
        String user1 = "bristol";
        claim(isValidUserName(user1) == 0);
        user1 = "&fruit";
        claim(isValidUserName(user1) == 1);
        user1 = "fruit banana";
        claim(isValidUserName(user1) == 1);
        user1 = "()";
        claim(isValidUserName(user1) == 1);
    }

    private void test_password() {
        String password = "password";

        claim(isValidPassword(password, null) == 1);
        claim(isValidPassword("1234567", null) == 1);
        claim(isValidPassword("12rt3093", null) != 0);
        claim(isValidPassword("uY71Er!89", "28032018") == 0);
        claim(isValidPassword("1992aliCe#", "15051992") == 3);
        claim(isValidPassword("Alice1505!", "15051992") == 3);
        //System.out.println(isValidPassword("Alice155", "15051992"));
        //claim(isValidPassword("Alice515", "15051992") == 3);

    }

    public void storeUserAndPass(String name, String pass) {
        try {
            /*
            File file = new File(System.getProperty("user.dir") + "/src/gameData/userInfo.txt");
            System.out.println(name + " " + hash(pass));
            FileWriter fw = new FileWriter(file,true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(name + " " + pass);
            pw.close();
            fw.close();*/
            RandomAccessFile rf=new RandomAccessFile(System.getProperty("user.dir") + "/src/gameData/userInfo.txt","rw");
            rf.seek(rf.length());
            //System.out.println(hash(pass));
            rf.writeBytes(name + " " + hash(pass) + "\n");
            rf.close();

        }
        catch (Exception e) {
            System.out.println("Warning: Fail to write!");
        }


    }

    String hash(String pass) {
        int hash = (Math.abs(pass.hashCode() >> 1) + Math.abs(pass.hashCode() >> 3));

        return Integer.toString(hash);
    }


    private void test() {
        test_username();
        test_password();
    }

    public static void main(String[] args){
        PasswordAndUser program = new PasswordAndUser();
        program.test();
    }

}
