
import java.util.List;

public class UnitGroup extends CommentGroup {
    final int width = 600;
    UnitGroup(List<CommentGroup> commentGroups) {
        for(int i = 0; i < commentGroups.size(); i++) {
            this.add(commentGroups.get(i), 0,i);
        }
        this.setPrefWidth(610);
        this.getStyleClass().add("scroll");
    }
}
