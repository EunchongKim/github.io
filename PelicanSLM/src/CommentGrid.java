
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.geometry.*;


// inheritance PostGrid is possible
public class CommentGrid extends GridPane {
    final int width = 600;

    final int textWidth = 500;
    CommentGrid(String animal, String news) {
     //   this.setGridLinesVisible(true);


        this.setMinSize(width, -1);
        buildConstraint(this);
        GridPane textGrid = new GridPane();
        generateText(textGrid, news);
        this.add(textGrid, 5,0);

        Image profileImage = new Image("/css/graph/squareProfile/" + animal + ".png");
        ImageView profile = new ImageView();
        profile.setImage(profileImage);
        profile.setFitWidth(35);
        profile.setPreserveRatio(true);
    /*    profile.setFitWidth(35);
        profile.setFitHeight(32); */
        GridPane.setValignment(profile, VPos.TOP);
        this.add(profile, 0,0);

        Image likeImage = new Image("/css/graph/like.png");
        ImageView like = new ImageView();
        like.setImage(likeImage);
        like.setFitHeight(20);
        like.setFitWidth(20);
        GridPane.setValignment(like, VPos.BOTTOM);
        this.add(like, 51,0);

    }

    void generateText(GridPane textGrid, String news) {
        textGrid.getStyleClass().add("postBox");
        textGrid.setMinSize(500, - 1);
        textGrid.getStyleClass().add("postBox");
        buildConstraint(textGrid);
        Text text = new Text(news);
        text.getStyleClass().add("comment");
        text.setWrappingWidth(450);
        textGrid.add(text, 0, 0);

    }
    private void buildConstraint(GridPane grid) {
        int widthNum = 60;
        for (int i = 0; i < widthNum; i++) {
            ColumnConstraints column = new ColumnConstraints(10);
            grid.getColumnConstraints().add(column);
        }
    }


}
