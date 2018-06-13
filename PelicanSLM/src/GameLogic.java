/*
This class implements the logic of the game. This includes managing the
important statsistics and numbers such as popularity, goodness, posts, likes etc.
*/

/*
To do:
implement negative popularity for goodness below 5
*/

import java.util.Random;

class GameLogic {

    private int popularity = 0;
    private int goodness = 10; //need to do defensive programming to make sure stays between 0 and 10
    private int posts = 0;
    private int likes = 0;
    private int prev_likes = 0;
    private int total_likes = 0;
    private int comments = 0;
    private int prev_comments = 0;
    private int total_comments = 0;
    private int eventNumber=0;
    private int buttonNumber = 0;
    //  private PeliPostGroup getPostNum;

    //Chenxi 13/05

    private int times() {
        if (goodness >= 20) return 3;
        return 1;
    }

    void calculate_numbers(int eventNumber, int buttonNumber){

        if(eventNumber == 0 && buttonNumber == 0){
            this.goodness +=10; this.popularity -=5;
        }
        if(eventNumber == 0 && buttonNumber == 1){
            this.goodness +=5;
        }
        if(eventNumber == 0 && buttonNumber == 2){
            this.goodness -=5; this.popularity +=5 * times();
        }
        if(eventNumber == 1 && buttonNumber == 0){
            this.goodness +=5; this.popularity +=3 * times();
        }
        if(eventNumber == 1 && buttonNumber == 1){
            this.goodness -=5; this.popularity -=3;
        }
        if(eventNumber == 1 && buttonNumber == 2){
            this.goodness -=3; this.popularity -=1;
        }
        if(eventNumber == 2 && buttonNumber == 0){
            this.goodness +=5; this.popularity -=5;
        }
        if(eventNumber == 2 && buttonNumber == 1){
            this.goodness -=10; this.popularity +=5 * times();
        }
        if(eventNumber == 3 && buttonNumber == 0){
            this.goodness +=5; this.popularity +=3 * times();
        }
        if(eventNumber == 3 && buttonNumber == 1){
            this.popularity -=3;
        }
        if(eventNumber == 4 && buttonNumber == 0){
            this.goodness +=10;
        }
        if(eventNumber == 4 && buttonNumber == 1){
            this.goodness +=3; this.popularity +=3 * times();
        }

        if(eventNumber == 5 && buttonNumber == 0){
            this.goodness -=10; this.popularity +=10 * times();
        }
        if(eventNumber == 5 && buttonNumber == 1){
            this.popularity +=5;
        }
        if(eventNumber == 5 && buttonNumber == 2){
            this.goodness +=10; this.popularity +=5 * times();
        }
        if(eventNumber == 6 && buttonNumber == 0){
            this.goodness -=10; this.popularity +=5 * times();
        }
        if(eventNumber == 6 && buttonNumber == 1){
            this.goodness +=10;
        }
        if(eventNumber == 7 && buttonNumber == 0){
            this.goodness -=5; this.popularity -=3;
        }
        if(eventNumber == 7 && buttonNumber == 1){
            this.goodness +=5; this.popularity +=3 * times();
        }
        if(eventNumber == 8 && buttonNumber == 0){
            this.goodness -=10; this.popularity +=5 * times();
        }
        if(eventNumber == 8 && buttonNumber == 1){
            this.goodness +=10;
        }
        if(eventNumber == 9 && buttonNumber == 0){
            this.goodness +=20; this.popularity +=10;
        }
        if(eventNumber == 9 && buttonNumber == 1){
            this.goodness -=20; this.popularity -=20;
        }
    }

    //Return number of posts.
    String get_posts() {
        return Integer.toString(posts);
    }

    //Return the likes value.
    String get_likes() {
        return Integer.toString(total_likes);
    }

    //Return the total number of likes.
    String get_total_likes() {
        return Integer.toString(total_likes);
    }

    //Return number of comments.
    String get_comments() {
        return Integer.toString(total_comments);
    }

    //Return the total number of comments.
    String get_total_comments() {
        return Integer.toString(total_comments);
    }

    //Return the goodness value.
    String get_goodness() {
        return Integer.toString(goodness);
    }

    //Return popularity value.
    String get_popularity() {
        return Integer.toString(popularity);
    }

    //Increment total number of posts by one
    void increment_posts() {
        //getPostNum = new PeliPostGroup();
        //posts = getPostNum.getSize();
        //posts = posts + 1;
    }

    /*
    Calculate number of likes for new post.
    For now this will be random.
    In future, will be decided by tycoon 'recipe'
    */
    void calculate_likes() {
        Random rand = new Random();
        prev_likes = likes;

        likes = rand.nextInt(15);

        total_likes = total_likes + likes;
    }

    /*
    Calculate number of comments for new post.
    For now this will be random.
    In future, will be decided by tycoon 'recipe'
    */
    void calculate_comments() {
        Random rand = new Random();
        prev_comments = comments;

        comments = rand.nextInt(7);

        total_comments = total_comments + comments;
    }
    boolean state()
    {
        boolean face=false;
        if(goodness>=20 &&popularity>=10)
            face = true;
        return face;
    }

    /*
    Calulate the new popularity level based in the number of likes, comments,
    and on the goodness value. There is a small random element for variety.
    */
    /*void calculate_popularity() {
        Random rand = new Random();
        int rn = rand.nextInt(10);

        float gr;
        float tp=0;

        if(goodness >= 5) {
            gr = 1 + (float)goodness/200;
        }
        else{
            gr = -1 + ((float)goodness/200);
        }
        //System.out.println(gr);
       // tp = popularity + (rn + ((likes - prev_likes)/10)
        //        + ((comments - prev_comments)/10)) * (gr);

        popularity = (int) tp;
    }*/


    /*void claim(boolean b) {
        if (!b) throw new Error("Test " + testNumber + " fails");
        testNumber++;
    }
    private int testNumber = 1;

    void run(String[] args) {

    }

    public static void main(String[] args) {
        GameLogic program = new GameLogic();
        program.run(args);
    }*/

}