import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/* Check each move of stones and a game result.
 * If a stone is out of board,
 * remove it from the board and stone lists.
 * After checking a result, show a winner,
 * and ask to go back to the start page or exit the game.
 */
public class CheckMove {

    private List<Shape> whiteStones;
    private List<Shape> blackStones;
    private List<Shape> allStones;
    private Pane stones;
    private Stage stage;

    CheckMove(List<Shape> whiteStones, List<Shape> blackStones, Pane stones, Stage stage) {
        this.whiteStones = new ArrayList<>(whiteStones);
        this.blackStones = new ArrayList<>(blackStones);
        this.stones = stones;
        this.stage = stage;
    }

    void checkOut () {
        allStones = new ArrayList<>();
        allStones.addAll(whiteStones);
        allStones.addAll(blackStones);

        for (Shape shape : allStones) {
            if (shape.getLayoutX() < 0 || shape.getLayoutX() > 600 ||
                    shape.getLayoutY() < 0 || shape.getLayoutY() > 600) {
                allStones.remove(shape);
                stones.getChildren().remove(shape);
                if (blackStones.contains(shape)) {
                    blackStones.remove(shape);
                }
                else if (whiteStones.contains(shape)) {
                    whiteStones.remove(shape);
                }
                break;
            }
        }
        checkWin(whiteStones, blackStones);
    }

    void checkWin (List<Shape> whiteStones, List<Shape> blackStones) {
        if (allStones.size() == 0) {
            String draw = "Draw!";
            endGame(draw);
        } else if (blackStones.size() == 0) {
            String whiteWin = "White Win!";
            endGame(whiteWin);
        } else if (whiteStones.size() == 0) {
            String blackWin = "Black Win!";
            endGame(blackWin);
        }
    }

    void endGame (String results) {
        final Stage select = new Stage();
        select.initModality(Modality.WINDOW_MODAL);
        Text newSelect = new Text(results);
        Button start = new Button("Start Again");
        Button end = new Button("Exit");
        start.setMinWidth(110);
        end.setMinWidth(110);
        start.getStyleClass().add("selectButton");
        end.getStyleClass().add("selectButton");

        GridPane dialogPane = new GridPane();
        dialogPane.setMinSize(250, 200);
        new MakeBoard().initFullGrid(dialogPane, 100, 3, 2);
        dialogPane.add(newSelect, 30, 20);
        dialogPane.add(start, 10, 50);
        dialogPane.add(end, 50, 50);
        newSelect.getStyleClass().add("questions");
        dialogPane.getStyleClass().add("questionBox");
        Scene dialogScene = new Scene(dialogPane, 300, 170);
        dialogScene.getStylesheets().add("css/Style.css");

        start.setOnAction(e -> {
            select.close();
            stage.close();
            Alggagi newStart = new Alggagi();
            newStart.start(new Stage());
        });

        end.setOnAction(e -> {
            Platform.exit();
        });

        select.setScene(dialogScene);
        select.show();
    }

    List returnWhite() {
        return whiteStones;
    }

    List returnBlack() {
        return blackStones;
    }
}
