import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class TrendGrid extends GridPane {
    final int WIDTH = 245;
    final int HEIGHT = 235;
    private Image trendImage = new Image("css/graph/trendbutton.png");
    private Image fishImage = new Image("css/graph/fishbutton.png");
    private Image gameImage = new Image("css/graph/gamebutton.png");
    private Image movieImage = new Image("css/graph/moviebutton.png");
    private ImageView trend = new ImageView(trendImage);
    private ImageView fish = new ImageView(fishImage);
    private ImageView game = new ImageView(gameImage);
    private ImageView movie = new ImageView(movieImage);

    private Image trendListImage = new Image("css/graph/trendlist.png");
    private Image fishListImage = new Image("css/graph/fishlist.png");
    private Image gameListImage = new Image("css/graph/gamelist.png");
    private Image movieListImage = new Image("css/graph/movielist.png");
    private ImageView trendList = new ImageView(trendListImage);
    private ImageView fishList = new ImageView(fishListImage);
    private ImageView gameList = new ImageView(gameListImage);
    private ImageView movieList = new ImageView(movieListImage);

    TrendGrid() {
        this.setPrefSize(WIDTH, HEIGHT);
        Image backgroundImage = new Image("css/graph/trendbox.png");
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);
        buildConstraint(this);

        this.add(background, 0, 23);
        //this.setGridLinesVisible(true);

        setButton(trend, fish, game, movie, trendList, fishList, gameList, movieList);
    }

    void setButton(ImageView ...viewArray) {
        for (ImageView item : viewArray) {
            item.setFitWidth(25);
            item.setFitHeight(15);
        }
        List<Button> buttons = new ArrayList<>();
        List<Timeline> sequences = new ArrayList<>();
        for (int i = 0; i < viewArray.length / 2; i++) {
            viewArray[i].setFitWidth(25);
            viewArray[i].setFitHeight(22);
            Button button = new Button();
            button.setGraphic(viewArray[i]);
            button.setPadding(new Insets(0,0,0,0));
            buttons.add(button);
            button.setBackground(Background.EMPTY);
            this.add(button, 22 + i*6, 5);

            sequences.add(blink(button, 2000, i));

            //set trending content
            viewArray[i + 4].setOpacity(0);
            viewArray[4].setOpacity(100);
            viewArray[i + 4].setFitWidth(210);
            viewArray[i + 4].setFitHeight(135);
            this.add(viewArray[i + 4], 3, 24);
            button.setCursor(Cursor.HAND);
        }
        for (int i = 0; i < viewArray.length/2; i++) {
            int finalI = i;
            buttons.get(i).setOnAction(e -> {
                for(int j = 4; j < 8; j++) {
                    viewArray[j].setOpacity(0);
                }
                viewArray[finalI + 4].setOpacity(100);
            });
        }
    }

    private Timeline blink(Button button, int time, int i) {
        final Timeline timeline = new Timeline();
        timeline.setDelay(Duration.millis(500*i));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(button.opacityProperty(), 0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();

        return timeline;
    }

    private void buildConstraint(GridPane grid) {
        for (int i = 0; i < WIDTH/5; i++) {
            ColumnConstraints column = new ColumnConstraints(5);
            grid.getColumnConstraints().add(column);
        }
        for (int i = 0; i < HEIGHT/5; i++) {
            RowConstraints row = new RowConstraints(5);
            grid.getRowConstraints().add(row);
        }
    }
}
