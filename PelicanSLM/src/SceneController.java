import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SceneController extends Application {
    private String username, password, birth;
    private Scene startScene, getInfoScene, mainScene, bookScene;
    private GridPane startGrid, getInfoGrid, mainGrid, bookGrid, socialBox, statBox,
            dayBox, nameBox, words, peliFace;
    private boolean peliPost, sayFinished = false, newGame=true,firstEvent=true;
    private String peliPostText = "", peliContent = "";
    private Text peliSay;
    private List<PostGrid> peliPostGroup = new ArrayList<>();

    //Chenxi 14/5
    private GameLogic logic = new GameLogic();
    final int wellLimit = 20, popLimit = 0;



    private int postSize, day = 1, peliAge = 13;
    private PasswordAndUser namePassCheck = new PasswordAndUser();
    private Animation typing;
    public static MediaPlayer startSound, mainSound, bookSound;
    public static Button back,eventButton;

    private Stage select = new Stage();
    private Events event;

    //chenxi 12-5
    private boolean startword = false;
    Animation typeAni;
    private ImageView peliInRoom;
    private boolean isFirstPeliSay = true;


    //--end

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        event = new Events(logic);
        Font.loadFont(SceneController.class.getResource("css/LUNCH.ttf").toExternalForm(), 10);
        Font.loadFont(SceneController.class.getResource("css/SSANGMUN.ttf").toExternalForm(), 10);
        Font.loadFont(SceneController.class.getResource("css/AmericanTypewriter.ttc").toExternalForm(), 10);

            startGrid = new GridPane();
            mainGrid = new GridPane();

        generateStart();
        generateGetInfo(stage);
 //       generateMain(stage);

        Button startButton = new Button("START");
        startButton.setPrefSize(90, 20);
        startGrid.add(startButton, 65, 65,10,4);
        startButton.getStyleClass().add("startButton");
        blink(startButton, 700);
        startButton.setCursor(Cursor.HAND);

        startSound.play();
        startSound.setCycleCount(MediaPlayer.INDEFINITE);

        //Chenxi 12-5
        startButton.setOnAction(e -> {
            typeAni.play();
            stage.setScene(getInfoScene);
        });

        startScene = new Scene(startGrid);
        getInfoScene = new Scene(getInfoGrid);
        mainScene = new Scene(mainGrid);
        getInfoScene.getStylesheets().add("css/layoutstyle.css");
        startScene.getStylesheets().add("css/layoutstyle.css");
        mainScene.getStylesheets().add("css/layoutstyle.css");

        setCursor(startScene);
     // setCursor(mainScene);
        stage.setScene(startScene);
        stage.setTitle("Pelican Social Life Maker");

        stage.show();
    }

    private void getInfoImage() {
        Image background = new Image("css/graph/getinfo.png");
        ImageView view = new ImageView(background);
        view.setFitHeight(508);
        view.setFitWidth(903);
        getInfoGrid.add(view, 0, 50);

        Image animalworld = new Image("css/graph/animalmap.png");
        ImageView animalWorld = new ImageView(animalworld);
        animalWorld.setFitWidth(450);
        animalWorld.setFitHeight(230);
        getInfoGrid.add(animalWorld, 4, 55);
    }

    private void generateGetInfo(Stage stage) {
        getInfoGrid = new GridPane();
        getInfoGrid.setMinSize(900, 500);
        initFullGrid(getInfoGrid,100, 9, 5);
        getInfoImage();

        Button createButton = new Button("CREATE");
        createButton.setPrefSize(90, 20);
        getInfoGrid.add(createButton, 55, 80,10,4);
        createButton.setCursor(Cursor.HAND);

        //Chenxi
        Button cancelButton0 = new Button("CANCEL");
        cancelButton0.setPrefSize(90, 20);
        getInfoGrid.add(cancelButton0, 85, 80,10,4);
        cancelButton0.setCursor(Cursor.HAND);

        //Chenxi
        Button loginButton = new Button("LOGIN");
        loginButton.setPrefSize(90, 20);
        getInfoGrid.add(loginButton, 70, 80, 10, 4);
        loginButton.setCursor(Cursor.HAND);

        Text word0 = new Text("Pelibook helps you connect and share with the\n"+
        "animals in your life.");
        word0.getStyleClass().add("word0");
        getInfoGrid.add(word0, 8, 25);

        Text createPeli = new Text("Create your pelibook");
        TextField createName = new TextField();
        createName.setText("Name");
        PasswordField passWord = new PasswordField();
        passWord.setText("Password");
        Text nameLimit = new Text("* User name cannot include special characters\n"+
                "like '!@#$'");
        Text passLimit = new Text("* At least 8 characters, must include\n upper and lower-case letters, and special characters!\nIt should have no relation with your birthday! ");


        createName.setMinWidth(150);
        createName.setMinHeight(30);
        passWord.setMinWidth(150);
        passWord.setMinHeight(30);
        createPeli.getStyleClass().add("word1");
        createName.getStyleClass().add("createBox");
        passWord.getStyleClass().add("createBox");
        nameLimit.getStyleClass().add("limitWord");
        passLimit.getStyleClass().add("limitWord");

        VBox vbox = new VBox(50);
        Scene scene = new Scene(vbox, 300, 300);
        stage.setScene(scene);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DatePicker birthday = new DatePicker();
        birthday.setValue(LocalDate.parse("01-01-2008", formatter));


        GridPane gridPane = new GridPane();
        gridPane.setVgap(3);

        Text dateText = new Text("Birthday");
        dateText.getStyleClass().add("dateText");
        gridPane.add(dateText, 0, 0);

        GridPane.setHalignment(dateText, HPos.LEFT);
        gridPane.add(birthday, 0, 2);
        vbox.getChildren().add(gridPane);
        getInfoGrid.add(gridPane, 60, 65,20,1);
        stage.show();


        getInfoGrid.add(createPeli, 60, 13);
        getInfoGrid.add(createName, 60, 23);
        getInfoGrid.add(passWord, 60, 43);
        getInfoGrid.add(nameLimit, 60, 30);
        getInfoGrid.add(passLimit, 60, 54);

        Text warningForUser = new Text(), warningForPass = new Text();
        imageBlink(warningForPass, 2000);
        imageBlink(warningForUser, 2000);
        warningForPass.getStyleClass().add("warning");
        warningForUser.getStyleClass().add("warning");

        getInfoGrid.add(warningForUser, 60, 18, 20, 1);
        getInfoGrid.add(warningForPass, 60, 38, 20, 1);


        //Chenxi 12/5
        //Chenxi 12/5 --start
        Image techPeli = new Image("css/graph/left_PeliTeach.png");
        ImageView techPeliView = new ImageView(techPeli);
        techPeliView.setFitHeight(600);
        techPeliView.setFitWidth(900);
        techPeliView.setOpacity(1);
        getInfoGrid.add(techPeliView, 0, 45);

        String peliIntro = "Hi, I'm Peli. You can " +
                "create an account on the Pelibook social network! But please make sure you know how " +
                "to protect yourself! Now, start to learn how to create an account.";
        Text peliIntroText = new Text(500, 150, "");
        peliIntroText.getStyleClass().add("intro");
        peliIntroText.setWrappingWidth(500);
        typingEffect(peliIntro, peliIntroText);
        getInfoGrid.add(peliIntroText, 36, 70);


        ImageView arrow = new ImageView();
        arrow.setImage(new Image("css/graph/arrow.png"));
        arrow.setFitWidth(25);
        arrow.setFitHeight(15);

        arrow.setOnMouseClicked(e->{
            slowDisappear(techPeliView, 1000);
            slowDisappear(peliIntroText, 1000);
            slowDisappear(arrow, 1000);
            /*
            getInfoGrid.getChildren().remove(techPeliView);
            getInfoGrid.getChildren().remove(peliIntroText);
            getInfoGrid.getChildren().remove(arrow);*/
        });

        imageBlink(arrow, 200);
        getInfoGrid.add(arrow, 90, 81);

        //Chenxi 12/5 --end

        createButton.setOnAction(e -> {
            username = String.valueOf(createName.getText());
            password = String.valueOf(passWord.getText());
            birth = birthday.getConverter().toString(birthday.getValue()).
                    replace("/","");
            if (!checkUserPass(username, password, birth, warningForUser, warningForPass)) {
                return;
            }
            startSound.pause();
            generateMain(stage);
            mainSound.play();
            mainSound.setCycleCount(MediaPlayer.INDEFINITE);
       //     mainGrid.getChildren().remove(words);
        //  change
            typingEffect();
    //        nextSaying();
            stage.setScene(mainScene);
        });
        //Chenxi
        loginButton.setOnAction(e -> {
            username = String.valueOf(createName.getText());
            password = String.valueOf(passWord.getText());
            birth = birthday.getConverter().toString(birthday.getValue()).
                    replace("/","");
            if(!checkLogin(username, password, warningForUser, warningForPass)) {
                return;
            }
            startSound.pause();
            generateMain(stage);
            mainSound.play();
            mainSound.setCycleCount(MediaPlayer.INDEFINITE);
            //change
            typingEffect();
            //      nextSaying();
            stage.setScene(mainScene);

        });
        cancelButton0.setOnAction(e -> {
            stage.setScene(startScene);
        });
    }
    //chenxi
    boolean checkLogin(String username, String password, Text warningForUser, Text warningForPass) {
        if (!namePassCheck.checkSameName(username)) {
            warningForUser.setText("No such a user!");
            return false;
        }
        if (!namePassCheck.checkUserToKey(username, password)) {
            warningForPass.setText("Wrong password!");
            return false;
        }
        return true;
    }

    //chenxi
    boolean checkUserPass(String username, String password, String birth,
                          Text warningForUser, Text warningForPass) {

        int nameCode = namePassCheck.isValidUserName(username);
        int passCode = namePassCheck.isValidPassword(password, birth);
        if (nameCode == 0 && passCode == 0){
            namePassCheck.storeUserAndPass(username, password);
            return true;
        }

        if (nameCode == 0) {
            warningForUser.setText("");
        }
        if (nameCode == 1) {
            warningForUser.setText("Invalid username!");
            return false;
            //getInfoGrid.add(warningForUser, 60, 20, 20, 1);
        }
        if (nameCode == 2) {
            warningForUser.setText("Already exits!");
            return false;
        }
        if (passCode == 1) {
            warningForPass.setText("Invalid! You choose a notorious password!");
        }
        else if (passCode == 2) {
            warningForPass.setText("Invalid! Please read the requirement of password!");
        }
        else {
            warningForPass.setText("Invalid! " +
                    "Don't use your birthday!");
        }
        //getInfoGrid.add(warningForPass, 60, 42, 20, 1);
        return false;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getBirth() {
        return birth;
    }

    private void setCursor(Scene scene) {
        Image cursor = new Image("css/graph/cursor.png", 50, 50,
                true, true);
        scene.setCursor(new ImageCursor(cursor, cursor.getWidth()/2,
                cursor.getHeight()/2));
    }

    // Select post or not
    // If user select to post, move on to textfield,
    // if not, move on to PeliBook page
    private void selectBox(Stage stage) {
        //select.initModality(Modality.WINDOW_MODAL);
        //select.initOwner(stage);
        Text newSelect = new Text("Do you want to post?");
        Button post = new Button("Post");
        Button skip = new Button("Skip");
        post.setMinWidth(100);
        skip.setMinWidth(100);
        post.getStyleClass().add("selectButton");
        skip.getStyleClass().add("selectButton");
        post.setCursor(Cursor.HAND);
        skip.setCursor(Cursor.HAND);

        GridPane dialogPane = new GridPane();
        dialogPane.setMinSize(250, 200);
        initFullGrid(dialogPane, 100, 3, 2);
        dialogPane.add(newSelect, 10, 20);
        dialogPane.add(post, 10, 50);
        dialogPane.add(skip, 50, 50);
        newSelect.getStyleClass().add("questions");
        dialogPane.getStyleClass().add("questionBox");
        Scene dialogScene = new Scene(dialogPane, 300, 170);
        dialogScene.getStylesheets().add("css/layoutstyle.css");

        post.setOnAction(e -> {
            select.close();
            createPost(stage);
            newGame=false;
        });

        skip.setOnAction(e -> {
            peliPost = false;

            select.close();
            mainSound.pause();
 //           if(newGame)
 //           {
                generateBook(stage);
                bookScene = new Scene(bookGrid);
                bookScene.getStylesheets().add("css/layoutstyle.css");
  //          }
            stage.setScene(bookScene);
            bookSound.play();
            bookSound.setCycleCount(MediaPlayer.INDEFINITE);
            newGame=false;
        });

  //      setCursor(dialogScene);
        select.setScene(dialogScene);
        select.show();
    }

    // Receive post content
    // If user select 'post' button, get input as Pelican's post,
    // it not, move on to PeliBook page
    private void createPost(Stage stage) {
        final Stage inputPost = new Stage();
        inputPost.initModality(Modality.WINDOW_MODAL);
        inputPost.initOwner(stage);
        Text newSelect = new Text("What's on your mind?");
        TextArea createInput = new TextArea();
        Button post = new Button("Post");
        Button cancel = new Button("Cancel");
        post.setCursor(Cursor.HAND);
        cancel.setCursor(Cursor.HAND);

        post.setMinWidth(100);
        cancel.setMinWidth(100);
        post.getStyleClass().add("selectButton");
        cancel.getStyleClass().add("selectButton");
        createInput.setMinWidth(250);
        createInput.setMinHeight(100);

        GridPane questionBox = new GridPane();
        questionBox.setMinSize(350, 300);
        initFullGrid(questionBox, 100, 3.5, 3);
        questionBox.add(newSelect, 15, 10);
        questionBox.add(createInput, 12, 40);
        questionBox.add(post, 12, 75);
        questionBox.add(cancel, 55, 75);
        newSelect.getStyleClass().add("questions");
        questionBox.getStyleClass().add("questionBox");
        Scene inputScene = new Scene(questionBox, 350, 300);
        inputScene.getStylesheets().add("css/layoutstyle.css");

        post.setOnAction(e -> {
            peliPostText = String.valueOf(createInput.getText());
            peliPost = true;
            inputPost.close();
            moveToBook(stage);
        });

        cancel.setOnAction(e -> {
            peliPost = false;
            inputPost.close();
            moveToBook(stage);
        });

        inputPost.setScene(inputScene);
        inputPost.show();
    }

    private void moveToBook(Stage stage) {
        mainSound.pause();
        generateBook(stage);
        bookScene = new Scene(bookGrid);
        bookScene.getStylesheets().add("css/layoutstyle.css");
  //      setCursor(bookScene);
        stage.setScene(bookScene);
        bookSound.play();
        bookSound.setCycleCount(MediaPlayer.INDEFINITE);
    }

    // Create PeliBook page
    // If user write input, show Pelican's post first
    // Randomly generate animal friends' posts and comments among 25 kinds of animals
    private void generateBook(Stage stage) {
        bookGrid = new GridPane();
        URL pathToMusic = getClass().getResource("sound/Pelibook.mp3");
        Media bookMedia = new Media(pathToMusic.toString());
        bookSound = new MediaPlayer(bookMedia);

        bookGrid.setMinSize(900, 500);
        initFullGrid(bookGrid,100, 9, 5);

        Image background = new Image("css/graph/bookback.png");
        ImageView view = new ImageView(background);
        view.setFitHeight(508);
        view.setFitWidth(903);
        bookGrid.add(view, 0, 50);

        PostGrid newPost0;

        //Chenxi........
        ScrollPane scGrid = new ScrollPane();
        scGrid.setPrefSize(600, 380);

        List<CommentGroup> commentGroups = new ArrayList<>();

        if(peliPost) {
            newPost0 = new PostGrid(username, peliPostText,
                    true, logic.get_likes());
            CommentGroup peli = new CommentGroup(newPost0, null);
            commentGroups.add(peli);
            peliPostGroup.add(new PostGrid(username, peliPostText,
                    true, logic.get_likes()));
        }
        generateMorePostCom(commentGroups);

        UnitGroup allPostCom = new UnitGroup(commentGroups);
        scGrid.setContent(allPostCom);
        scGrid.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scGrid.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scGrid.getStyleClass().add("scroll");

        bookGrid.add(scGrid, 1, 13, 69, 85);

        Image newFriend = friendRecommend(new Random().nextInt(3));
        ImageView friendList = new ImageView(newFriend);
        friendList.setFitHeight(163);
        friendList.setFitWidth(245);
        bookGrid.add(friendList, 71, 80);

        Button peliProfile = new Button();
        Image peliface0 = new Image("css/graph/peliface.png");
        ImageView peliFace = new ImageView(peliface0);
        peliFace.setFitHeight(50);
        peliFace.setFitWidth(50);
        peliProfile.setGraphic(peliFace);
        peliProfile.getStyleClass().add("backbutton");
        blink(peliProfile, 3000);
        bookGrid.add(peliProfile, 53, 5);


        peliProfile.setOnAction (e -> {
            final Stage profile = new Stage();
            profile.initModality(Modality.WINDOW_MODAL);
            profile.initOwner(stage);
            GridPane backGround = new GridPane();
            backGround.setMinSize(640, 380);
            initFullGrid(backGround, 10, 62, 38);
            ScrollPane peliHistory = new ScrollPane();
            peliHistory.setPrefSize(610, 380);

            PeliPostGroup allPostHistory = new PeliPostGroup(peliPostGroup);
            peliHistory.setContent(allPostHistory);
            peliHistory.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            peliHistory.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            peliHistory.getStyleClass().add("scroll");

            backGround.add(peliHistory, 0, 0, 20, 20);
            backGround.getStyleClass().add("scroll");
            Scene profileScene = new Scene(backGround, 640, 380);
            profileScene.getStylesheets().add("css/layoutstyle.css");
            profile.setScene(profileScene);
            profile.show();
        });

        peliProfile.setCursor(Cursor.HAND);

        back = new Button();
        Image backbutton0 = new Image("css/graph/backbutton.png");
        ImageView backbutton = new ImageView(backbutton0);
        backbutton.setFitHeight(35);
        backbutton.setFitWidth(35);
        back.setGraphic(backbutton);
        back.getStyleClass().add("backbutton");
        blink(back, 3000);
        bookGrid.add(back, 1, 5);

        //chenxi 12/5
        back.setOnAction (e -> {
            bookSound.pause();
            mainGrid.getChildren().remove(dayBox);
            changeNum();

            typingEffect();
            stage.setScene(mainScene);
            mainSound.play();
            mainSound.setCycleCount(MediaPlayer.INDEFINITE);
            changePeliFace();
            makePeliSay();
        });
        back.setCursor(Cursor.HAND);
/*
        EventGroup forExit = new EventGroup();
        Button exit = forExit.returnExit();
        exit.addEventHandler(MouseEvent.MOUSE_RELEASED, e-> {
            bookSound.pause();
            mainGrid.getChildren().remove(dayBox);
            changeNum();

            typingEffect();
            stage.setScene(mainScene);
            mainSound.play();
            mainSound.setCycleCount(MediaPlayer.INDEFINITE);
        });
        exit.setCursor(Cursor.HAND);
*/
        GridPane trendBox = new TrendGrid();
        bookGrid.add(trendBox, 71, 38);


        /*Last update by ASLAN 0:45 5/13*/
        ImageView newMessage=new ImageView(new Image("css/graph/message.png"));
        newMessage.setFitWidth(60);
        newMessage.setFitHeight(60);
        eventButton =new Button("New Message!",newMessage);
        eventButton.setContentDisplay(ContentDisplay.LEFT);
        eventButton.setTranslateX(630);
        eventButton.setTranslateY(350);
        eventButton.setPrefSize(210, 80);
        eventButton.setEffect(new DropShadow());
        eventButton.getStyleClass().add("sale");

        blink(eventButton, 1000);
        eventButton.setOnAction(e -> {
            event.start();

        });
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if(firstEvent)
                    {
                        event.start();
                        firstEvent=false;
                    }
                    bookGrid.add(eventButton,3,5,30,30);
                });
            }
        }, 2*1000);

    }

    Button returnBack() {
        return back;
    }

    private void changeNum() {
        day = day + 1;
        postSize = peliPostGroup.size();
        //    logic.increment_posts();
        logic.calculate_likes();
        logic.calculate_comments();

        mainGrid.getChildren().remove(socialBox);
        mainGrid.getChildren().remove(statBox);
        makeDay();
        makeName();
        makeSocialBox();
        makeStatBox();
    }

    int generateRandom(int end, int []mark) {
        int res = new Random().nextInt(end);
        while (mark[res] == 1) {
            res = new Random().nextInt(end);
        }
        mark[res] = 1;
        return res;
    }

    //chenxi new
    void generateMorePostCom(List<CommentGroup> commentGroups ) {
        int num = new Random().nextInt(5) + 2;
    //    System.out.println(num);
        ArrayList<Integer> norepeat = new ArrayList<Integer>();
        while(num-- > 0) {
            PostText callPost = new PostText();
            int postNum = new Random().nextInt(8);
            if (!norepeat.contains(postNum)) {
                norepeat.add(postNum);
                String postText = callPost.postTexts(postNum);
                String name = callPost.postNames(new Random().nextInt(25));
                //System.out.println(name);
                PostGrid newPost = new PostGrid(name, postText, true, logic.get_likes());
                List<String> comments = callPost.comments(postNum);
                List<CommentGrid> commentGrids = new ArrayList<>();
                int []mark = new int[25];
                mark[postNum] = 1;
                for (String item : comments) {
                    commentGrids.add(new CommentGrid(callPost.postNames
                            (generateRandom(25, mark)),item));
                }
                CommentGroup commentGroup = new CommentGroup(newPost, commentGrids);
                commentGroups.add(commentGroup);
            }
        }
    }

    private Image friendRecommend(int i) {
        Image newFriend = new Image("css/graph/friendrecom0.png");
        switch(i) {
            case 0: newFriend = new Image("css/graph/friendrecom0.png");
            break;
            case 1: newFriend = new Image("css/graph/friendrecom1.png");
            break;
            case 2: newFriend = new Image("css/graph/friendrecom2.png");
            break;
            case 3: newFriend = new Image("css/graph/friendrecom3.png");
            break;
        }
        return newFriend;
    }

    private void initFullGrid(GridPane grid, int i, double width, double height) {
        for (int j=0; j<i; j++) {
            ColumnConstraints column = new ColumnConstraints(width);
            RowConstraints row = new RowConstraints(height);
            grid.getColumnConstraints().add(column);
            grid.getRowConstraints().add(row);
        }
    }

    private void makeDay() {
        dayBox = new GridPane();
        dayBox.setMinSize(220, 40);
        ImageView dayBox0 = new ImageView(new Image("css/graph/dayBox.png"));
        dayBox0.setFitWidth(220);
        dayBox0.setFitHeight(40);
        ImageView sandWatch = new ImageView(new Image("css/graph/sandWatch.png"));
        sandWatch.setFitWidth(30);
        sandWatch.setFitHeight(40);
        Text dayNum = new Text("Stage "+Integer.toString(day));
        rotate(sandWatch, 500);

        initFullGrid(dayBox,40, 5, 1);
        dayBox.add(sandWatch, 2, 13);
        dayBox.add(dayNum, 12, 15);

        dayNum.getStyleClass().add("dayNum");
        mainGrid.add(dayBox0, 10, 11);
        mainGrid.add(dayBox, 13, 12);
    }

    private void rotate(ImageView watch, double time) {
        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(time),
                        new KeyValue(watch.rotateProperty(), 0)),
                new KeyFrame(Duration.millis(time*2),
                        new KeyValue(watch.rotateProperty(), 90)),
                new KeyFrame(Duration.millis(time*3),
                        new KeyValue(watch.rotateProperty(), 180)),
                new KeyFrame(Duration.millis(time*4),
                        new KeyValue(watch.rotateProperty(), 270)),
                new KeyFrame(Duration.millis(time*5),
                        new KeyValue(watch.rotateProperty(), 360)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    private void makeName() {
        nameBox = new GridPane();
        nameBox.setMinSize(240, 40);
        ImageView nameBox0 = new ImageView(new Image("css/graph/dayBox.png"));
        nameBox0.setFitWidth(240);
        nameBox0.setFitHeight(40);

        peliAge = 2018 - (Integer.valueOf(birth.substring(4, 8)));
        Text nameAge = new Text(username+", Age "+Integer.toString(peliAge));
        initFullGrid(nameBox,40, 6, 1);
        nameBox.add(nameAge, 1, 15);

        nameAge.getStyleClass().add("dayNum");
        mainGrid.add(nameBox0, 65, 11);
        mainGrid.add(nameBox, 69, 12);
    }

    private void makeSocialBox() {
        socialBox = new GridPane();
        socialBox.setMinSize(220, 200);
        ImageView infoBox = new ImageView(new Image("css/graph/infoBox.png"));
        infoBox.setFitWidth(220);
        infoBox.setFitHeight(200);
        Text socialtitle = new Text("Social Network");
        Text posts = new Text("Posts");
        Text likes = new Text("Likes");
        Text comments = new Text("Comments");
        Text postNum = new Text(Integer.toString(postSize));
        Text likeNum = new Text(logic.get_likes());
        Text commentNum = new Text(logic.get_comments());
        initFullGrid(socialBox,100, 2, 2.5);
        socialBox.add(socialtitle, 0, 7);
        socialBox.add(posts, 3, 23);
        socialBox.add(likes, 3, 37);
        socialBox.add(comments, 3, 50);
        socialBox.add(postNum, 72, 23);
        socialBox.add(likeNum, 72, 37);
        socialBox.add(commentNum, 72, 50);
        posts.getStyleClass().add("contents");
        likes.getStyleClass().add("contents");
        comments.getStyleClass().add("contents");
        postNum.getStyleClass().add("numbers");
        likeNum.getStyleClass().add("numbers");
        commentNum.getStyleClass().add("numbers");
        socialtitle.getStyleClass().add("title");
        mainGrid.add(infoBox, 10, 35);
        mainGrid.add(socialBox, 13, 38);
    }

    private void makeStatBox() {
        statBox = new GridPane();
        statBox.setMinSize(240, 160);
        ImageView infoBox1 = new ImageView(new Image("css/graph/infoBox.png"));
        infoBox1.setFitWidth(240);
        infoBox1.setFitHeight(160);
        Text stattitle = new Text("Pelican Stats");
        Text popularity = new Text("Popularity");
        Text wellness = new Text("Wellness");
        Text popularityNum = new Text(logic.get_popularity());
        Text wellnessNum = new Text(logic.get_goodness());
        initFullGrid(statBox, 100, 2.4, 1.6);
        statBox.add(stattitle, 12, 7);
        statBox.add(popularity, 3, 30);
        statBox.add(wellness, 3, 50);
        statBox.add(popularityNum, 60, 30);
        statBox.add(wellnessNum, 60, 50);
        wellness.getStyleClass().add("contents");
        popularity.getStyleClass().add("contents");
        popularityNum.getStyleClass().add("numbers");
        wellnessNum.getStyleClass().add("numbers");
        stattitle.getStyleClass().add("title");
        mainGrid.add(infoBox1, 65, 31);
        mainGrid.add(statBox, 67, 35);
    }



    //Chenxi 12/5
    private void makePeliSay() {

        int popular = Integer.parseInt(logic.get_popularity());
        int goodness = Integer.parseInt(logic.get_goodness());

        sayFinished = false;
        mainGrid.getChildren().remove(words);

        words = new GridPane();
        words.setMinSize(500, 100);
        initFullGrid(words, 100, 5, 1);
        ImageView sayBox = new ImageView();
        sayBox.setImage(new Image("css/graph/saybox.png"));
        sayBox.setFitWidth(400);
        sayBox.setFitHeight(110);
        ImageView arrow = new ImageView();
        arrow.setImage(new Image("css/graph/arrow.png"));
        arrow.setFitWidth(25);
        arrow.setFitHeight(15);
        imageBlink(arrow, 200);

        if (isFirstPeliSay) {
            peliContent = "I am Pelican and I like Pelibook. \n"
                    + username + ", will you help me make my Pelibook popular and good?";
            isFirstPeliSay = false;
        }
        else if (popular < 0 && goodness >= 20) {
            peliContent = "Don't be sad! My wellness will bring\n" +
                    "a good result. Keep going!";
        }
        else if (popular < 0 || goodness < 0) {
            peliContent = "I'm wasn't careful online! Sad... I will learn more and play the game again!";
        }
        //wait to change
        else if (goodness >= wellLimit && popular >= 10) {
            peliContent = "I'm popular! Happy! You could see a happy ending if you keep mindful online";
        }
        else {
            peliContent = "I will make my Pelibook popular and good!";
        }

        peliSay = new Text(200, 100, "");
        peliSay.setWrappingWidth(300);

        words.add(sayBox, 7, 45);
        words.add(peliSay, 11, 37);
        words.add(arrow, 73, 75);
        peliSay.getStyleClass().add("pelisay");



        mainGrid.add(words, 17, 85);
    }


    private void typingEffect() {
        typing = new Transition() {
            { setCycleDuration(Duration.millis(2500)); }
            protected void interpolate(double frac) {
                int length = peliContent.length();
                int n = Math.round(length * (float) frac);
                peliSay.setText(peliContent.substring(0, n));
            }
        };
        if (! sayFinished) {
            clickFast();
        }
        typing.play();
    }

    // chenxi 12-5
    private void typingEffect(String text, Text textbox) {
        typeAni = new Transition() {
            { setCycleDuration(Duration.millis(8000)); }
            protected void interpolate(double frac) {
                int length = text.length();
                int n = Math.round(length * (float) frac);
                textbox.setText(text.substring(0, n));
            }
        };
        textbox.setOnMousePressed(e -> {
            typeAni.setRate(typeAni.getCycleDuration().toMillis() - 1000.0);
        });

    }

    private void clickFast() {
        words.setOnMousePressed(e -> {
            typing.setRate(typing.getCycleDuration().toMillis() - 1000.0);
            Task<Void> sleeper = new Task() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep failed");
                    }
                    return null;
                }
            };
            if (! sayFinished) {
                sleeper.setOnSucceeded(e1 -> nextSaying());
                new Thread(sleeper).start();
            }
        });
    }

    private void nextSaying() {
        peliContent = "If you click 'START POSTING',\n" +
                "you can see Pelibook!";
        typingEffect();
        sayFinished = true;
    }

    private void makePeliFace() {
        peliFace = new GridPane();
        peliFace.setMinSize(130, 130);
        initFullGrid(peliFace, 130, 1, 1);
        ImageView peliFaceBox = new ImageView();
        peliFaceBox.setImage(new Image("css/graph/peliFaceBox.png"));
        peliFaceBox.setFitWidth(125);
        peliFaceBox.setFitHeight(125);
        peliFace.add(peliFaceBox, 0, 2);
        mainGrid.add(peliFace, 65, 98);
    }

    // Main page containing Pelican character, social and stat box
    // ** Numbers and logic are needed
    private void generateMain(Stage stage) {
        URL pathToMusic = getClass().getResource("sound/Main.mp3");
        Media mainMedia = new Media(pathToMusic.toString());
        mainSound = new MediaPlayer(mainMedia);

        mainGrid.setMinSize(900, 500);
        initFullGrid(mainGrid,100,9, 5);

        //Chenxi 12/5
        Image backimage = new Image("/css/graph/background.png");
        //Image pelican0 = new Image("/css/graph/Pelican0.png");

        ImageView BackGround = new ImageView();
        BackGround.setImage(backimage);
        BackGround.setFitWidth(905);
        BackGround.setFitHeight(505);
        mainGrid.add(BackGround, 0, 50);

        /*
        ImageView Pelican0 = new ImageView();
        Pelican0.setImage(pelican0);
        Pelican0.setFitWidth(270);
        Pelican0.setFitHeight(300);
        mainGrid.add(Pelican0, 33, 67);*/
        changePeliFace();
        //mainGrid.add(peliInRoom, 33, 67);


        //--end

        Button postButton = new Button("START POSTING");
        postButton.setPrefSize(170, 40);
        postButton.getStyleClass().add("postButton");
        mainGrid.add(postButton, 69, 48,30,10);
        postButton.setCursor(Cursor.HAND);

        makeSocialBox();
        makeStatBox();
        // change
        makeDay();
        makeName();
        makePeliSay();
        makePeliFace();

        postButton.setOnAction(e -> {
            selectBox(stage);
        });
    }

    private void generateStart() {
        URL pathToMusic = getClass().getResource("sound/Start.mp3");
        Media startMedia = new Media(pathToMusic.toString());
        startSound = new MediaPlayer(startMedia);
        startGrid.setMinSize(900, 500);
        initFullGrid(startGrid,100, 9, 5);
        Image startpage = new Image("/css/graph/Startpage0.png");
        ImageView view = new ImageView(startpage);
        view.setFitHeight(508);
        view.setFitWidth(903);
        startGrid.add(view,0,50);
    }

    // Make the start button blink
    private void blink(Button button, int time) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(button.opacityProperty(), 0.0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }
/*
    private void imageBlink(ImageView imageview, int time) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(imageview.opacityProperty(), 0.0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }*/

    private void imageBlink(Node node, int time) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue keyvalue = new KeyValue(node.opacityProperty(), 0.0);
        final KeyFrame keyframe = new KeyFrame(Duration.millis(time), keyvalue);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }

    //Chenxi 12/5
    private void slowDisappear(Node node, int time) {
        FadeTransition ft = new FadeTransition(Duration.millis(time), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        //ft.setCycleCount(Timeline.);
        //ft.setAutoReverse(true);
        ft.play();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event1 -> {
            getInfoGrid.getChildren().remove(node);
        });
        new Thread(sleeper).start();
    }

    //Chenxi 12/5
    private void changePeliFace() {
        int popular = Integer.parseInt(logic.get_popularity());
        int goodness = Integer.parseInt(logic.get_goodness());
        Image happyPeli = new Image("/css/graph/happypeli.png");
        ImageView happyPeliView = new ImageView(happyPeli);
        Image sadPeli = new Image("/css/graph/badpeli.png");
        ImageView sadPeliView = new ImageView(sadPeli);
        ImageView comPeliView = new ImageView(new Image("/css/graph/pelican0.png"));
        happyPeliView.setFitWidth(270);
        happyPeliView.setFitHeight(300);
        sadPeliView.setFitWidth(270);
        sadPeliView.setFitHeight(300);
        comPeliView.setFitWidth(270);
        comPeliView.setFitHeight(300);
        mainGrid.getChildren().remove(peliInRoom);


        if(popular < 0 || goodness < wellLimit/4) {
            peliInRoom = sadPeliView;
        }
        //tempchange
        else if (goodness >= 20 && popular >= 10) {
            System.out.println("change");
            peliInRoom = happyPeliView;
        }
        else {
            peliInRoom = comPeliView;
        }
        mainGrid.add(peliInRoom, 33, 67);

    }
}
