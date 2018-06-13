import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;

public class Ending extends StackPane{

    private Image finish;
    private String string;
    private int eventnumber;
    private MediaPlayer goodSound, badSound;
    Ending(boolean state, int eventnumber, Stage finish0) {
        this.eventnumber = eventnumber;
        SceneController.mainSound.pause();
       if(state)
       {
           URL pathToMusic = getClass().getResource("sound/GoodEnding.mp3");
           Media goodEndingMedia = new Media(pathToMusic.toString());
           goodSound = new MediaPlayer(goodEndingMedia);
           goodSound.play();
           goodSound.setCycleCount(MediaPlayer.INDEFINITE);
           string =new String("Well done! You've helped make Peli popular and happy! " +
                         "Remember, don't trust strangers online, and always be brave " +
                         "enough to say no!");
           finish = new Image("css/graph/endingPage/happy.jpg");
       }
       else
       {
           URL pathToMusic = getClass().getResource("sound/BadEnding.mp3");
           Media badEndingMedia = new Media(pathToMusic.toString());
           badSound = new MediaPlayer(badEndingMedia);
           badSound.play();
           badSound.setCycleCount(MediaPlayer.INDEFINITE);

           string =new String("Now you could end up sad like Peli. "+
                   "But don't worry, with the knowledge you've " +
                         "learned, you understand more about being safe online. "+
           "Please keep going! You could make Peli popular and happy! ");
           finish = new Image("css/graph/endingPage/sad.jpg");
       }
        this.getStylesheets().add("css/layoutstyle.css");
        ImageView page = new ImageView(finish);
        page.setTranslateX(0);
        page.setTranslateY(0);
        WavingText wavingText=new WavingText(string);
        wavingText.setTranslateX(0);
        wavingText.setTranslateY(250);

        Button quit=new Button("EXIT");
        quit.setPrefSize(200, 40);
        quit.getStyleClass().add("sale");
        quit.setTranslateX(180);
        quit.setTranslateY(210);
        quit.setOnAction(event -> {
            if (eventnumber == 4) {
                finish0.close();
                if (state) {
                    goodSound.pause();
                }
                else {
                    badSound.pause();
                }
                SceneController.mainSound.play();
            }
            if (eventnumber == 9) {
                System.exit(0);
            }
        });

        this.getChildren().add(page);
        this.getChildren().add(wavingText);
        this.getChildren().add(quit);
        this.setMinSize(640, 480);
        this.setTranslateX(0);
        this.setTranslateY(0);
        
    }
}
