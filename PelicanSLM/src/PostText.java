import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostText {

    String postTexts(int i) {
        String newPost = "";
        switch (i) {
            case 0: newPost = "Does anybody want to go to the gym with me?\n"+
                    "8pm at Union Gate!!!";
            break;
            case 1: newPost = "Oh god! I have good news and bad news to tell you all :d\n"+
                    "Bad is my laptop doesn’t work now :(\n"+
                    "And the Good news is I submitted my assessment just now :)";
            break;
            case 2: newPost = "Today was a big snowfall! Happy to fly!\n"+
                    "Does anyone want to have a snowball fight?! ";
            break;
            case 3: newPost = "Today is World Sleep Day. I wish everyone good dreams!";
            break;
            case 4: newPost = "Happy birthday to you!";
            break;
            case 5: newPost = "OMG! Dd you guys see the news about fakebook?\n"+
                    "I hope that our Pelibook user information is safe!";
            break;
            case 6: newPost = "Who is in Pelican Valley this weekend?";
            break;
            case 7: newPost = "My daddy bought me a new video game\n" +
                    "“Angry Human”!!!!!! Anyone want to play with me!?" +
                    "I am ready to be challenged!";
            break;
            case 8: newPost = "Can anyone lend me a picnic basket for the weekend?\n"+
                    "Or you’re welcome to join with your basket as well !!! xx";
            break;
        }
        return newPost;
    }

    List<String> comments(int i) {
        switch (i) {
            case 0: {
                return new ArrayList<>(Arrays.asList("No time :)",
                        "Don’t you know you have an exam on Friday?! 100%, bro!!",
                        "Cool!!! Please join our gym!! If you need some help, contact me: 12345677",
                        "Register your supercool " +
                                "membership card now for just $9.99/year!!! Only TODAY"));
            }
            case 1: {
                return new ArrayList<>(Arrays.asList(
                        "Mum: Is that ok? Do you need to buy a new one?",
                        "Friend: hahhhhh so interesting!!"+
                                "You are a computer science student!!! lol",
                        "Classmate: please google it! If you are lazy,"+
                                "please give me 10 pounds, I'll fix it for you."));
            }
            case 2: {
                return new ArrayList<>(Arrays.asList(
                        "YEEEEP!!! SUCH HEAVY SNOW!!!! I love it!!!!",
                        "I’ll come! When? Where? With whom?? Please reply!!"+
                                "I really want to come!!!",
                        "Please, you are no longer a child"));
            }
            case 3: {
                return new ArrayList<>(Arrays.asList("Good night!",
                        "Really? Ok I‘ll go to bed!",
                        "But I still have a lot of working Um……"));
            }
            case 4: {
                return new ArrayList<>(Arrays.asList("happy birthday!!!",
                        "Thank you very much!!!",
                        "Happy birthday!!! Don’t forget to have a cake!!"));
            }
            case 5: {
                return new ArrayList<>(Arrays.asList("Yes!!so terrible!!",
                        "Don't worry! I hope we’re safe! Our organisers are brilliant :P",
                        "I’m lucky that I choose Pelibook!!!!"));
            }
            case 6: {
                return new ArrayList<>(Arrays.asList("Me! What’s up.",
                        "I am :p",
                        "Me too. Let’s ride bikes. What are you up to Saturday."));
            }
            case 7: {
                return new ArrayList<>(Arrays.asList(
                        "What? Is that the game which you throw people to the sky and"+
                                "destroy their buildings? That’s so amazing!!",
                        "I’d love to! Can I go on Friday after school?",
                        "I am ready to beat U."));
            }
            case 8: {
                return new ArrayList<>(Arrays.asList(
                        "Ah really!!!! But I don’t have a basket. T-T",
                        "I just got a new one from Pelikea."+
                                "Do you wanna come tonight, we can have dinner tgt.",
                        "OH. U R not going to gym then."));
            }
        }
        return null;

    }

    String postNames(int i) {
        String name = "";
        switch (i) {
            case 0: name = "Bear";
            break;
            case 1: name = "Buffalo";
            break;
            case 2: name = "Chick";
            break;
            case 3: name = "Chicken";
            break;
            case 4: name = "Cow";
            break;
            case 5: name = "Crocodile";
            break;
            case 6: name = "Dog";
            break;
            case 7: name = "Duck";
            break;
            case 8: name = "Elephant";
            break;
            case 9: name = "Frog";
            break;
            case 10: name = "Giraffe";
            break;
            case 11: name = "Goat";
            break;
            case 12: name = "Gorilla";
            break;
            case 13: name = "Hippo";
            break;
            case 14: name = "Horse";
            break;
            case 15: name = "Narwhal";
            break;
            case 16: name = "Owl";
            break;
            case 17: name = "Parrot";
            break;
            case 18: name = "Penguin";
            break;
            case 19: name = "Pig";
            break;
            case 20: name = "Rhino";
            break;
            case 21: name = "Sloth";
            break;
            case 22: name = "Snake";
            break;
            case 23: name = "Walrus";
            break;
            case 24: name = "Whale";
            break;
            case 25: name = "Zebra";
            break;
        }
        return name;
    }
/*
    String commTexts(int i) {
    } */
}
