import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EventGroup extends GridPane{
    final int width = 640;
    private Stage stageBefore;
    private boolean thisEventFinished;
    private int previousPopularity,previousGoodness;
    private MediaPlayer player;
    private WavingText newWavingText;
    Button exit;
    EventGroup() {
        this.setMinSize(width, -1);
    }

    EventGroup(Stage stage,WavingText wavingText,List<String> buttonText,int eventnumber, GameLogic logic) {

        this.newWavingText=wavingText;
        this.setAlignment(Pos.TOP_LEFT);
        thisEventFinished=false;
        stageBefore=stage;
        this.setMinSize(width, -1);
        if(eventnumber<5)
        {
            player= new MediaPlayer(new Media(getClass().
                    getResource("css/event/event1.mp4").toExternalForm()));
        }
        else{
            player = new MediaPlayer(new Media(getClass().
                    getResource("css/event/event2.mp4").toExternalForm()));
        }

        MediaView mediaView = new MediaView(player);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setFitHeight(480);
        mediaView.setFitWidth(640);
        mediaView.setOpacity(30);
        mediaView.setTranslateX(0);
        mediaView.setTranslateY(0);
        this.getChildren().add(mediaView);
        player.play();


        this.getChildren().add(newWavingText.get_text());
        previousGoodness=Integer.parseInt(logic.get_goodness());
        previousPopularity=Integer.parseInt(logic.get_popularity());
        /*modified by ASLAN 18/05/12 22:18*/
        for(int i=0;i<buttonText.size();i++)
        {
            makeNewButton(i,logic,eventnumber,buttonText.get(i));
        }



    }
    /*modified by ASLAN 18/05/12 22:18*/
    void makeNewButton(int buttonNumber, GameLogic logic, int eventnumber, String text)
    {
        Button button=new Button();
        Image next = new Image("css/graph/next.png");
        ImageView nextPage = new ImageView(next);
        nextPage.setFitHeight(50);
        nextPage.setFitWidth(50);

        button.setGraphic(nextPage);
        button.setMaxSize(40, 40);
        GridPane.setValignment(button, VPos.TOP);

        button.setTranslateX(40);
        button.setTranslateY(220+60*buttonNumber);
        FlashText flashText=new FlashText(text);

        flashText.setTranslateX(110);
        flashText.setTranslateY(5+60*buttonNumber);

        this.getChildren().add(button);
        this.getChildren().add(flashText);
        button.getStyleClass().add("sale");

        button.setOnAction(e -> {
            if(thisEventFinished==false)
            {
                final Stage educationwin = new Stage();
                educationwin.initModality(Modality.WINDOW_MODAL);
                EventContent eventContent=new EventContent(eventnumber);
                StackPane eduwindow =new StackPane();
                eduwindow.setMinSize(320, 190);

                MediaPlayer eduPlayer = new MediaPlayer(new Media(getClass().
                        getResource("css/event/edu.m4v").toExternalForm()));
                MediaView eduMedia = new MediaView(eduPlayer);
                eduPlayer.setAutoPlay(true);
                eduPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                eduMedia.setFitHeight(480);
                eduMedia.setFitWidth(640);
                eduMedia.setTranslateX(0);
                eduMedia.setTranslateY(0);
                eduwindow.setAlignment(Pos.TOP_LEFT);
                eduwindow.getChildren().add(eduMedia);
                player.play();

                Text temp=new Text(eventContent.educationalText());
                temp.setWrappingWidth(480);
                temp.setTranslateX(115);
                temp.setTranslateY(70);
                temp.setFont(Font.font("", FontWeight.BOLD, 18));
                temp.setFill(Color.WHITE);
                eduwindow.getChildren().add(temp);
                //temp.getStyleClass().add("eduText");






                exit=new Button("Exit");
                exit.setPrefSize(200, 40);
                exit.setTranslateX(400);
                exit.setTranslateY(400);
                eduwindow.getChildren().add(exit);
                exit.getStyleClass().add("sale_orange");
                exit.setOnAction( e1->{
                    educationwin.close();
                    stageBefore.close();
                    SceneController.back.fire();
                    //
 //                   System.out.println(eventnumber);
                    if(eventnumber==4 || eventnumber==9)
                    {
                        final Stage finish=new Stage();
                        Scene finalScene=new Scene(new Ending(logic.state(), eventnumber, finish));
                        finish.initModality(Modality.WINDOW_MODAL);
                        finish.setScene(finalScene);
                        finish.show();
       //                 SceneController.eventButton.setDisable(true);
                    }
                });


                logic.calculate_numbers(eventnumber,buttonNumber);
    //            logic.calculate_goodness(eventnumber,buttonNumber);

                //Chenxi 14/5
                int wellness = Integer.parseInt(logic.get_goodness());
                //System.out.println(wellness);
                if (wellness >= 20) {
                    Text tips = new Text("You were careful online! Now your\n "+
                            "high wellness will make it easier \n " +
                            "for you to be popular and happy!");
                    tips.getStyleClass().add("tips");
                    tips.setTranslateX(250);
                    tips.setTranslateY(300);
                    imageBlink(tips, 1000);
                    eduwindow.getChildren().add(tips);
                }


                HBox reBox=reminder(Integer.parseInt(logic.get_popularity()),Integer.parseInt(logic.get_goodness()));


                this.getChildren().add(reBox);

                Scene eventScene = new Scene(eduwindow, 640, 480);
                eventScene.getStylesheets().add("css/layoutstyle.css");
                educationwin.setScene(eventScene);

                Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            educationwin.show();
                        });
                    }
                }, 2*1000);
            }
            thisEventFinished=true;
        });
    }

    Button returnExit() {
        return exit;
    }

    boolean checkEventStatus()
    {
        return thisEventFinished;
    }

    HBox reminder(int popularity, int goodness)
    {
        final Text text;
        ScheduledExecutorService bgThread = Executors.newSingleThreadScheduledExecutor();
        int pop=Math.abs(popularity-previousPopularity);
        int good=Math.abs(goodness-previousGoodness);
        if(popularity>=previousPopularity)
        {
            if(goodness>=previousGoodness)
            {
                text= new Text("Popularity + "+ pop+", Wellness + "+good);
            }
            else
            {
                text= new Text("Popularity + "+ pop+", Wellness - "+good);
            }
        }
        else
        {
            if(goodness>=previousGoodness)
            {
                text= new Text("Popularity - "+ pop+", Wellness + "+good);
            }
            else
            {
                text= new Text("Popularity - "+ pop+", Wellness - "+good);
            }
        }

        text.getStyleClass().add("reminder");
        StackPane group = new StackPane();
        Rectangle frame = new Rectangle(500, 50);
        frame.setFill(Color.color((double)136/255, (double)144/255, (double)167/255, 1));
        frame.setArcWidth(25);
        frame.setArcHeight(25);

        group.getChildren().add(frame);
        group.getChildren().add(text);
        group.setVisible(false);

        bgThread.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {

                group.setVisible(true);
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), group);
                tt.setToY(0);

                ParallelTransition pt = new ParallelTransition(tt);
                pt.setOnFinished(e -> {
                    group.setTranslateY(0);

                    TranslateTransition tt2 = new TranslateTransition(Duration.seconds(0.5), group);
                    tt2.setToY(180);

                    ParallelTransition pt2 = new ParallelTransition(tt2);
                    pt2.play();
                });
                pt.play();
            });
        }, 0, 4, TimeUnit.SECONDS);

        HBox hBox=new HBox();
        hBox.getChildren().add(group);
        hBox.setTranslateX(100);
        hBox.setTranslateY(0);
        return hBox ;
    }
    private void blink(Text text, int time) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(text.opacityProperty(), 0.0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }


    private void imageBlink(Node node, int time) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(node.opacityProperty(), 0.0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }
}
