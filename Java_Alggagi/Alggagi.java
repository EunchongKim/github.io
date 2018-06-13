import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
/* Generate the start page. If the start button is clicked,
 * the option box to select play modes between AI and self comes up.
 */
public class Alggagi extends Application {
    public static boolean aiPlayMode;

    public void start(Stage stage) {
        GridPane startGrid = new GridPane();
        new MakeBoard().initFullGrid(startGrid, 600, 1, 1);
        startGrid.getStyleClass().add("startColor");

        Image startPage0 = new Image("css/startPage.png");
        ImageView startPage = new ImageView(startPage0);
        startPage.setFitWidth(600);
        startPage.setFitHeight(600);
        startGrid.add(startPage, 1, 300);

        Button startButton = new Button("START");
        startButton.setPrefSize(90, 20);
        startGrid.add(startButton, 450, 530, 100, 4);
        startButton.getStyleClass().add("startButton");
        blink(startButton, 800);

        startButton.setOnAction(e -> {
            final Stage select = new Stage();
            select.initModality(Modality.WINDOW_MODAL);
            Text newSelect = new Text("Select a play mode");
            Button aiMode = new Button("AI Play");
            Button selfMode = new Button("Self Play");
            aiMode.setMinWidth(110);
            selfMode.setMinWidth(110);
            aiMode.getStyleClass().add("selectButton");
            selfMode.getStyleClass().add("selectButton");

            GridPane dialogPane = new GridPane();
            dialogPane.setMinSize(250, 200);
            new MakeBoard().initFullGrid(dialogPane, 100, 3, 2);
            dialogPane.add(newSelect, 20, 20);
            dialogPane.add(aiMode, 10, 50);
            dialogPane.add(selfMode, 50, 50);
            newSelect.getStyleClass().add("questions");
            dialogPane.getStyleClass().add("questionBox");
            Scene dialogScene = new Scene(dialogPane, 300, 170);
            dialogScene.getStylesheets().add("css/Style.css");

            aiMode.setOnAction(e1 -> {
                select.close();
                stage.close();
                aiPlayMode = true;
                MakeBoard newStart = new MakeBoard();
                newStart.start(new Stage());
            });

            selfMode.setOnAction(e1 -> {
                select.close();
                stage.close();
                aiPlayMode = false;
                MakeBoard newStart = new MakeBoard();
                newStart.start(new Stage());
            });

            select.setScene(dialogScene);
            select.show();
        });

        Scene startScene = new Scene(startGrid);
        startScene.getStylesheets().add("css/Style.css");

        stage.setScene(startScene);
        stage.setTitle("Romanesco Alggagi");
        stage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}
