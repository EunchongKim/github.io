import java.util.ArrayList;
import java.util.List;
public class EventContent{
    //someone post a message
    private int eventNumber;
    EventContent(int eventNumber) {
        this.eventNumber=eventNumber;
    }
    String eventText(){
        String event = "";
        switch (eventNumber){
            // Story 1: Piggy gets bullied.
            case 0: event = "Piggy posted a lovely picture. "
                                      + "Somebody has put a teasing comment "
                                      + "\"Piggy becomes as fat as human.\" "
                                      + "Some friends are starting to make a joke on Piggy. What should you do?";
            break;
            case 1: event = "Look! Piggy has posted a lovely video. "+
                    "But, uh oh, somebody has written a nasty comment again. "
                                     + "What should Piggy do?";
            break;
            case 2: event = "Piggy has another comment. "+
                    "This time they are telling piggy "
                                     + "that she needs to loose weight."
                                     + " What should piggy do?";
            break;
            case 3: event = "Piggy blocked those people, "+
                    "but she can still see nasty comments "
                                     + "about her on other people's Pelibook page."
                                     + " Piggy feels very sad. "
                                     + "What should piggy do?";
            break;
            case 4: event = "Piggy has sent a post. "+
                    "She says she feels very sad, and doesn't care "
                                      + "about anything anymore. "+
                    "You are very worried about poor piggy. "
                                      +"What should you do?";
            break;
            // Story 2: Pelican gets messaged by Tiger.
            case 5: event = "Tiger has added you on Pelibook. "+
                    "He sent you a message: "
                                     + "\" Hello Pelican, you don't know me, "+
                    "but I am friends with one of "
                                     + "your other friends, Elephant.\" "
                                     +"What should you do?";
            break;
            case 6 : event = "You have another message from Tiger: "
                                      + "\" Hi Pelican, I think we have a lot of similar hobbies. "+
                    "I also like football. I go to a different school in your town, "+
                    "I am on their football team.\" "
                                      +"Do you know what school Tiger goes to?";
            break;
            case 7: event = "1 new message from Tiger: "
                                    + "\"Hey P, I also think we are the same age!\" "
                                    +" Do you know what age Tiger is?";
            break;
            case 8: event = "1 new message from Tiger: "
                                    + "\"There aren't many pictures of you on your profile, "
                                    + "can you send me some more pictures of you?\" "
                                    +" What should you do?";
            break;
            case 9: event = "1 new message from Tiger: \"Hello Pelican, we have become "
                                    + "good friends online now, so I really want to meet you. "
                                    + "I think we will be really good friends\" "
                                    +"What should you do?";
            break;
        }
        return event;
    }
    String eventOwner(){
        String owner = "";
        switch(eventNumber){
            case 0: owner = "Pig";
                break;
            case 1: owner = "Pig";
                break;
            case 2: owner = "Pig";
                break;
            case 3: owner = "Pig";
                break;
            case 4: owner = "Pig";
                break;
            case 5: owner = "Pelican";
                break;
            case 6: owner = "Pelican";
            break;
            case 7: owner = "Pelican";
            break;
            case 8: owner = "Pelican";
            break;
            case 9: owner = "Pelican";
            break;
        }
        return owner;
    }
    List<String>buttonText(){
        List<String> text = new ArrayList<>();
        switch(eventNumber){
            case 0:
                // p -5 g +10
                text.add("Tell others 'Stop teasing Piggy!'");
                // p 0 g 5
                text.add("Privately tell Piggy that you love the picture");
                // p 5 g -5
                text.add("Add a similar comment as friends' to the post");
                break;
            case 1:
                text.add("Report the comment and tell her parents");
                text.add("Reply with a nasty comment about them");
                text.add("Take down her lovely video");
                break;
            case 2:
                // p -5 g +5
                text.add("Piggy should report the comment and block those people");
                // p +5 g -10
                text.add("Piggy should tell them that she will loose weight");
                break;
            case 3:
                text.add("Tell her parents to tell Pelibook to remove those people");
                text.add("Keep it a secret, in case someone makes the situation worse");
                break;
            case 4:
                // p 0 g+10
                text.add("Tell your parents, and ask them to contact Piggy's parents");
                // p +3 g +3
                text.add("Give piggy an encouraging comment");
                break;
            case 5:
                // p +10 g -10
                text.add("Add Tiger");
                // p +5 g 0
                text.add("Check with Elephant that they know Tiger");
                // p 0 g +10
                text.add("Check with Elephant's parents that your friend knows Tiger");
                break;
            case 6:
                // p +5 g-10
                text.add("A school in Pelican's hometown");
                // p 0 g+10
                text.add("I don't know");
                break;
            case 7:
                text.add("The same age as Pelican");
                text.add("I Don't know");
                break;
            case 8:
                // p +5 g-10
                text.add("Send Tiger some pictures");
                // p 0 g+10
                text.add("Refuse");
                break;
            case 9:
                // p -10 g+20
                text.add("Refuse");
                // p -10 g-20
                text.add("Agree to meet Tiger");
                break;
    }
    return text;
}

    String educationalText(){
        String text = "";
        switch(eventNumber){
            case 0: text = "If somebody has said something nasty to Piggy, "
                                + "you should let her know that her friends are there for her. "
                    + "If you publicly tell others to stop, your wellness highly increases "
                    + "although your popularity decreases "
                +"since it could cause any arguments with the other animals.";
                break;
            case 1: text = "Most social media platforms, including Pelibook, "
                                + "allow you to tell them if a post or comment is bad. "
                                + "Usually, just under the post or comment, there will "
                                + "be a button called \"Report\". If you friend is "
                                +"receiving bad comments then she can report them.";
                break;
            case 2: text = "Did you know that on most social media platforms, you "
                                + "can choose to block people that you don't want to "
                                + "communicate with? Usually, in the settings section, you "
                                + "can choose to block people from having any direct "
                                + "communication with you. This is the best choice for Piggy. "
                                + "Those people were telling Piggy to loose weight. "
                                + "This is not a good thing to say. A healthy body and a healthy "
                                + "diet is different for different people. Piggy is a beautiful "
                                + "animal, and she should be proud.";
                break;
            case 3: text = "This is bullying. When somebody is nasty several times, "
                                + "over a period of time, then they are a bully. Being bullied "
                                + "will make any animal feel very sad. We should not encourage "
                                + "bullying. Every animal is beautiful, and every animal is special "
                                + " If we are being bullied then we should tell our Parents or "
                                + "our teacher. They can help us to stop the bullying. "
                                + "On some social media platforms, it is possible "
                                + "to have an account removed if the owner is nasty. Your parents "
                                + "or teacher can help you do this.";
                break;
            case 4: text = "If you feel worried about your friend, or they post something "
                                + "that worries you, don't be afraid to speak out. It is very hard "
                                + "to see how someone is really feeling from just an online post. "
                                + "They might be feeling very, very sad, and they might need help. "
                                + "So if your friend posts something like this,you should tell your "
                                + "parents or teacher.";
                break;
            case 5: text = "Online, it is very difficult to tell if someone is telling the truth. "
                                + "Pelican should definitely check with Elephant's parents that "
                                + "Tiger is a friend. That way Pelican can be sure whether it "
                                + "is safe to add Tiger or not.";
                break;
            case 6: text = "It seems like Tiger and Pelican do have a lot in common. Tiger said "
                                + "they go to school in the same town, and they both like football. "
                                + "But how can Pelican know that Tiger is telling the truth? It is very "
                                + "easy to lie online, so we have to be very careful about believing "
                                + "what strangers say, even if they seem nice.";
                break;
            case 7: text = "Although Tiger says that he is the same age as Pelican, "
                                + "there's is no way for Pelican to know for sure just by looking "
                                + "profile picture, There is know way of knowing if that picture "
                                +"really is of her.";
                break;
            case 8: text = "You never have to do anything you don't want to do online. You should "
                                + "always be brave enough to say no. If you do not know someone and they "
                                + "ask you for pictures you should refuse and tell your parents or teacher. "
                                + "Even if your friend asks you for pictures should say no. There is no way "
                                + "of knowing for sure if it really is your friend using your friend's account. "
                                + "Only post pictures that you are comfortable sharing, and make sure you "
                                + "ask your parents first.";
                break;
            case 9: text = "Even if someone seems very friendly online, there's no way to know who "
                                + "they really are. Never agree to meet a stranger. "
                                + "Remember: even if they have pictures and videos on their account, "
                                + "you can't really be sure who the person using that account is. "
                                + "If a stranger online is asking Pelican to meet, then he should "
                                + "tell his parents and his teacher, that way he can help keep "
                                + "himself and all the other animals safe and happy.";
                break;
        }
        return text;
    }
}
