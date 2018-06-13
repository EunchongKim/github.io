import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class FlashText extends StackPane{
    private Animation typing;
    private boolean sayFinished = false;
    
    FlashText(String string) {

        this.setAlignment(Pos.CENTER_LEFT);
        Rectangle frame;
        frame= new Rectangle(520, 50);
        frame.setFill(Color.color((double)242/255, (double)241/255, (double)243/255, 0.8));
        frame.setArcWidth(25);
        frame.setArcHeight(25);
        frame.setTranslateX(0);
        frame.setTranslateY(0);
        this.getChildren().add(frame);

            Text text = new Text(string);
            text.getStyleClass().add("eventbutton");


            typing = new Transition() {
                { setCycleDuration(Duration.millis(5000)); }
                protected void interpolate(double frac) {
                    int length = string.length();
                    int n = Math.round(length * (float) frac);
                    text.setText(string.substring(0, n));
                }
            };
            if (! sayFinished) {
                clickFast();
            }
            typing.play();
        this.getChildren().add(text);

    }

    private void clickFast() {
        this.setOnMousePressed(e -> {
            typing.setRate(typing.getCycleDuration().toMillis() - 1000.0);
        });
    }

    StackPane get_text()
    {
        return this;
    }

}
