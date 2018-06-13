import java.util.List;

public class PeliPostGroup extends PostGrid  {
    //final int width = 600;
    PeliPostGroup(List<PostGrid> postGrids) {
        for(int i = 0; i < postGrids.size(); i++) {
            this.add(postGrids.get(i), 0,i);
        }
        this.setPrefWidth(620);
        this.setMinHeight(600);
        this.getStyleClass().add("scroll");
    }
/*
    PeliPostGroup() {
        this.setPrefWidth(620);
        this.setMinHeight(600);
    }

    int getSize() {
        int size = 1;
        for (Node n: this.getChildren()) {
            size++;
        }
        return size;
    }*/
}
