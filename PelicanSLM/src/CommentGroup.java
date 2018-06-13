import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.util.List;

public class CommentGroup extends GridPane {
    final int width = 600;
    private int index = 0;
    CommentGroup() {
        //    this.setGridLinesVisible(true);
        this.setVgap(10);
        this.setMinSize(width, -1);
        buildConstraint(this);
    }

    CommentGroup(PostGrid post, List<CommentGrid> commentGrids ) {
        //    this.setGridLinesVisible(true);
        this.setVgap(10);
        this.setMinSize(width, -1);
        buildConstraint(this);
        this.add(post,0,0);

        if (commentGrids != null) {
            Image commentImage = new Image("/css/graph/commentLogo.png");
            ImageView commentLogo = new ImageView();

            commentLogo.setImage(commentImage);
            commentLogo.setFitWidth(30);
            commentLogo.setFitHeight(30);
            this.add(commentLogo,2,1);


            Text comment = new Text("Comments");
            comment.getStyleClass().add("commentlabel");
            this.add(comment, 6, 1);
        }
        //commentGrids.add(peliComment());
        for(; commentGrids != null && index < commentGrids.size();index++) {
            this.add(commentGrids.get(index),2, index+ 2);
        }
        peliComment();
    }

    void peliComment() {
        final int LIMIT = 55;
        CommentGrid peliComent = new CommentGrid("peliface", "");
        TextArea createInput = new TextArea();
        createInput.setMaxWidth(500);
        createInput.setMaxHeight(20);
        createInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (createInput.getText().length() > LIMIT) {
                    String s = createInput.getText().substring(0, LIMIT);
                    createInput.setText(s);
                }
            }
        });
        createInput.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String txt = createInput.getText();
                txt = txt.substring(0, txt.length() - 1);
                CommentGrid newComment = new CommentGrid("peliface", txt);
                createInput.setText("");
                this.getChildren().remove(peliComent);
                this.add(newComment, 2, index + 2);
                index++;
                this.add(peliComent, 2, index + 2);
            }
        });

        peliComent.add(createInput, 5, 0,50, 1);
        this.add(peliComent, 2, index + 2);
        //return peliComent;
    }

    private void buildConstraint(GridPane grid) {
        int widthNum = 60;
        for (int i = 0; i < widthNum; i++) {
            ColumnConstraints column = new ColumnConstraints(10);
            grid.getColumnConstraints().add(column);
        }
    }
}
