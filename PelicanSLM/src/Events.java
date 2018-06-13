
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Events extends GridPane{
 
    private GameLogic logic = new GameLogic();
    private int CurrentEventNo=-1;
    final int WIDTH = 25;
    final int HEIGHT = 15;
    final private Stage newEvent = new Stage();
    
    private EventGroup newEventGroup;
    private GridPane background;
    private boolean thisClose=false;
      Events(GameLogic logic)
      {
          this.setPrefSize(WIDTH, HEIGHT);
          this.logic=logic;
          newEvent.initModality(Modality.WINDOW_MODAL);
          newEvent.setOnCloseRequest(new EventHandler<WindowEvent>() {
              @Override
              public void handle(WindowEvent event) {
                  thisClose=true;
              }
          });
      }
      
      void start()
      {
          background =new GridPane();
          background.setMinSize(640, 480);
          if(CurrentEventNo==-1||(newEventGroup.checkEventStatus()&&CurrentEventNo<10))
          {
              CurrentEventNo++;
              if (CurrentEventNo == 1 || CurrentEventNo == 3 || CurrentEventNo == 7)
                  CurrentEventNo++;
              newEventGroup=buildEvent(CurrentEventNo);
          }
          newEventGroup.setTranslateX(0);
          newEventGroup.setTranslateY(0);
          background.getChildren().add(newEventGroup);
          background.setAlignment(Pos.TOP_LEFT);
          Scene eventScene = new Scene(background, 640, 480);
          eventScene.getStylesheets().add("css/layoutstyle.css");
          newEvent.setScene(eventScene);
          newEvent.show();
      }
      
      boolean checkClose()
      {
          return thisClose;
      }
      boolean checkEventStatus()
      {
          return newEventGroup.checkEventStatus();
      }
      
      private EventGroup buildEvent(int eventNumber)
      {
          EventContent eventContent=new EventContent(eventNumber);
          WavingText wavingText=new WavingText(eventContent.eventText());
          EventGroup eventGroup=new EventGroup(newEvent,wavingText,eventContent.buttonText(),eventNumber,logic);
          return eventGroup;
      }
}