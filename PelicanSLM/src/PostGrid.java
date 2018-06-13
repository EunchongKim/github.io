
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PostGrid extends GridPane{
    final int width = 600;
    private boolean isComment;
    private int height;
    private boolean isLike;
    private String animal;
    int widthNum;
    int heightNum;

    PostGrid() {
        buildConstraint();
        this.setVgap(10);
        this.setMinSize(width, -1);
        this.getStylesheets().add("css/layoutstyle.css");
        this.getStyleClass().add("postBox");
    }

    PostGrid(String animal, String news, boolean isComment, String peli_likes) {
 //       this.animal = animal;
        buildConstraint();
        this.getStylesheets().add("css/layoutstyle.css");
        this.setMinSize(width, -1);
        this.getStyleClass().add("postBox");
        String username = null;

        //add profile

        //System.out.println(animal);
        if(!findAnimals(animal)) {
            username = animal;
            animal = "Pelican";
        }
        Image profileImage = new Image("/css/graph/roundProfile/" + animal + ".png");
        ImageView profile = new ImageView();
        profile.setImage(profileImage);
        profile.setFitWidth(45);
        profile.setPreserveRatio(true);
        this.add(profile, 1,2);

        //add content
        this.isComment = isComment;
        Text text = new Text("\n\n" + news);
        text.getStyleClass().add("post");
        text.setWrappingWidth(430);
        this.add(text, 7,2,2,2);

        //add name
        Text name;
        if(animal.equals("Pelican")) {
            name = new Text(username);
        }
        else {
            name = new Text(animal);
        }
        //GridPane.setFillWidth(name, true);
        name.getStyleClass().add("profileName");
        //name.setTextAlignment(TextAlignment.CENTER);
        this.add(name, 7, 2);
     //   this.setGridLinesVisible(true);

        //add like
        Image likeImage = new Image("/css/graph/like.png");
        ImageView like = new ImageView();
        like.setImage(likeImage);
        like.setFitHeight(25);
        like.setFitWidth(25);
        Button likeButton = new Button();
        likeButton.setGraphic(like);
        likeButton.getStyleClass().add("likebutton");
        blink(likeButton, 3000);
        GridPane.setValignment(likeButton, VPos.BOTTOM);
        this.add(likeButton, 52,3);
        likeButton.setCursor(Cursor.HAND);

        //add like number
        Text likeNum;
        if(animal.equals("Pelican")) {
            likeNum = new Text(peli_likes);
        }
        else {
            likeNum = new Text(Integer.toString(
                    new Random().nextInt(100)));
        }
        likeNum.getStyleClass().add("likeNumber");
        GridPane.setValignment(likeNum, VPos.BOTTOM);
        this.add(likeNum, 55,3);

        List<Integer> currentNum = new ArrayList<>();
        List<Node> increased = new ArrayList<>();
        currentNum.add(Integer.parseInt(likeNum.getText()));
        increased.add(likeNum);

        likeButton.setOnAction (e -> {
            if(!isLike) {
                Text newLikeNum = new Text(Integer.toString(
                        Integer.parseInt(likeNum.getText()) + 1));
                newLikeNum.getStyleClass().add("likeNumber");
                GridPane.setValignment(newLikeNum, VPos.BOTTOM);
                this.getChildren().remove(increased.get(0));
                this.add(newLikeNum, 55, 3);
                currentNum.set(0, Integer.parseInt(newLikeNum.getText()));
                isLike = true;
                increased.set(0, newLikeNum);
            }
            else {
                Text cancelLikeNum = new Text(Integer.toString(
                        (currentNum.get(0) - 1)));
                cancelLikeNum.getStyleClass().add("likeNumber");
                GridPane.setValignment(cancelLikeNum, VPos.BOTTOM);
                this.getChildren().remove(increased.get(0));
                this.add(cancelLikeNum, 55, 3);
                currentNum.set(0, Integer.parseInt(cancelLikeNum.getText()));
                isLike = false;
                increased.set(0, cancelLikeNum);
            }
        });

        Text triDot = new Text("...");
        triDot.getStyleClass().add("likeNumber");
        GridPane.setValignment(triDot, VPos.TOP);
        this.add(triDot, 55,2);
    }

    private boolean findAnimals(String username) {
        List<String> animalLists = new ArrayList<>();
        animalLists.add("Bear");
        animalLists.add("Buffalo");
        animalLists.add("Chick");
        animalLists.add("Chicken");
        animalLists.add("Cow");
        animalLists.add("Crocodile");
        animalLists.add("Dog");
        animalLists.add("Duck");
        animalLists.add("Elephant");
        animalLists.add("Frog");
        animalLists.add("Giraffe");
        animalLists.add("Goat");
        animalLists.add("Gorilla");
        animalLists.add("Hippo");
        animalLists.add("Horse");
        animalLists.add("Monkey");
        animalLists.add("Moose");
        animalLists.add("Narwhal");
        animalLists.add("Owl");
        animalLists.add("Panda");
        animalLists.add("Parrot");
        animalLists.add("Pelican");
        animalLists.add("Penguin");
        animalLists.add("Pig");
        animalLists.add("Rabbit");
        animalLists.add("Rhino");
        animalLists.add("Sloth");
        animalLists.add("Snake");
        animalLists.add("Walrus");
        animalLists.add("Whale");
        animalLists.add("Zebra");

        for(String s: animalLists) {
            if (username.equals(s)) return true;
        }
        return false;
    }

    private void blink(Button button, int time) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(button.opacityProperty(), 0.0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }

    private void buildConstraint() {
        widthNum = isComment ? 50 : 60;
        heightNum = height / 10;
        for (int i = 0; i < widthNum; i++) {
            ColumnConstraints column = new ColumnConstraints(10);
            this.getColumnConstraints().add(column);
        }
        for (int i = 0; i < heightNum; i++) {
            RowConstraints row = new RowConstraints(10);
            this.getRowConstraints().add(row);
        }
    }
}
