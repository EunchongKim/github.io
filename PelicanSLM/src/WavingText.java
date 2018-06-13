import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class WavingText extends StackPane {
    private Rectangle frame;
    private int line=0;
    private boolean sayFinished = false;
    private Animation typing;
    private Text peliSay;

    WavingText(String content){
        this.getStylesheets().add("css/layoutstyle.css");
        line=(int)(content.length()/58)+1;

        frame= new Rectangle(630, line*40+20);
        frame.setFill(Color.color((double)242/255, (double)241/255, (double)243/255, 0.8));
        frame.setArcWidth(25);
        frame.setArcHeight(25);
        frame.setTranslateX(5);
        frame.setTranslateY(15);
        this.getChildren().add(frame);
        
        this.setAlignment(Pos.TOP_LEFT);
        peliSay = new Text(630, line*40+20, "");
        peliSay.setWrappingWidth(600);
        peliSay.getStyleClass().add("eventcontent");
        peliSay.setTranslateX(20);
        peliSay.setTranslateY(40);
        this.getChildren().add(peliSay);

        typing = new Transition() {
            { setCycleDuration(Duration.millis(5000)); }
            protected void interpolate(double frac) {
                int length = content.length();
                int n = Math.round(length * (float) frac);
                peliSay.setText(content.substring(0, n));
            }
        };
        if (! sayFinished) {
            clickFast();
        }
        typing.play();
    }

    private void initFullGrid(GridPane grid, int i, double width, double height) {
        for (int j=0; j<i; j++) {
            ColumnConstraints column = new ColumnConstraints(width);
            RowConstraints row = new RowConstraints(height);
            grid.getColumnConstraints().add(column);
            grid.getRowConstraints().add(row);
        }
    }

    private void clickFast() {
        frame.setOnMousePressed(e -> {
            typing.setRate(typing.getCycleDuration().toMillis() - 1000.0);
        });
    }

    /*Last update by ASLAN 0:58 5/13*/
    int getHeightofFrame()
    {
        return (line*50);
    }
    
    StackPane get_text()
    { return this; }
    void setWrapped(int width)
    {
        this.peliSay.setWrappingWidth(width);
    }
}
