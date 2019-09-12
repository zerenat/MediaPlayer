package Player;

import javafx.animation.RotateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller{
    @FXML private GridPane mViewPane;
    @FXML private MediaView mView;
    @FXML private MediaView mediaView;
    @FXML private TextField text;
    @FXML private Slider progressSlider;
    @FXML private ListView mInfo;
    @FXML private Label progressShowLabel;
    @FXML private Pane mViewContainer;
    private MediaPlayer mPlayer;
    private ObservableList<Media> mList;
    private int bookMark;
    private MediaOrganizer mediaOrganizer;
    private boolean active;
    private boolean isDragged;
    private Duration mFileDuration;
    private ProgressionBar progressionBar;

    public void initialize(){
        mediaOrganizer = new MediaOrganizer();
        active = false;
        isDragged = false;
        Stage mainStage = Main.mainStage;

        //Height listener to modify mediaView size according to parent
        mainStage.heightProperty().addListener((observable, oldVal, newVal)->{
            mViewPane.setMaxHeight(newVal.doubleValue());
            //mViewPane.setPrefHeight(Main.mainStage.getHeight()-37);
            mViewPane.setPrefHeight(newVal.doubleValue());
            //mView.setFitHeight(Main.mainStage.getHeight()*90/100);
            mViewContainer.setPrefHeight(newVal.doubleValue()*90/100);
            mView.setFitHeight(mViewContainer.getHeight());
        });
        //Width listener to modify mediaView size according to parent
        //This is what fixed the overlap issues (Video cut from areas)
        mainStage.widthProperty().addListener((observable, oldVal, newVal)->{
            mViewPane.setMaxWidth(newVal.doubleValue());
            //mViewPane.setPrefWidth(Main.mainStage.getWidth()*75/100);
            mViewPane.setPrefWidth(newVal.doubleValue());
            //mView.setFitWidth(mViewPane.getWidth()*70/100);
            mViewContainer.setPrefWidth(newVal.doubleValue()*75/100);
            mView.setFitWidth(newVal.doubleValue()*75/100);
        });
    }
    //Call file search from MediaOrganizer and setup media
    public void mediaSetup() throws RuntimeException{
        try{
            if(mList!=null) {
                //disposeMediaPlayer();
                mList.clear();
                System.out.println("mList contents cleared.");
            }
            mInfo.getItems().removeAll();
            mList = mediaOrganizer.openMedia();
            mInfo.getItems().addAll(mList);

            if (mPlayer == null){
                bookMark = 0;
                createMediaPlayer();
//                rotate.setAngle(180);
//                rotate.setPivotX(200);
//                rotate.setPivotY(100);
//                System.out.println(mView.getBoundsInParent());
//                System.out.println(mView.getTransforms());
//                mView.getTransforms().add(rotate);
            }
        }catch(RuntimeException e){
            e.printStackTrace();
        }
    }
    //Next media file
    //Functionality for "next" button
    public void nextFile(){
        if(active){
            if(bookMark<mList.size()-1) {
                bookMark++;
                disposeMediaPlayer();
                createMediaPlayer();
            }
            else{
                bookMark = 0;
                disposeMediaPlayer();
                createMediaPlayer();
            }
        }
    }
    //Previous media file
    //Functionality for "previous" button
    public void previousFile(){
        if(active){
            if(bookMark-1 < 0){
                bookMark = 0;
                disposeMediaPlayer();
                createMediaPlayer();
            }
            else{
                bookMark--;
                disposeMediaPlayer();
                createMediaPlayer();
            }
        }
    }
    //Create media player
    private void createMediaPlayer(){
        mPlayer = new MediaPlayer(mList.get(bookMark));
        mView.setMediaPlayer(mPlayer);
        mPlayer.onReadyProperty().setValue(()-> {
            active = true;
            playMedia();
            System.out.println((String)mList.get(bookMark).getMetadata().get("title"));
            //Thread thread = new Thread(new MediaBufferThread());
            mFileDuration = mPlayer.getMedia().getDuration();
//            progressionBar = new ProgressionBar("mediaProgressionBar", mPlayer, progressSlider,
//                    progressShowLabel, mFileDuration);
        });
    }
    //Dispose of media player
    private void disposeMediaPlayer(){
        mPlayer.dispose();
        active = false;
        //System.out.println("dispose");
    }
    //Play media
    //Functionality for "play" button
    public void playMedia(){
        if(active) {
            try {
                mPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //Pause player
    //Functionality for "pause" button
    public void pauseMedia(){
        if(active) {
            try{
                mPlayer.pause();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    //Stop player
    //Functionality for "stop" button
    public void stopMedia(){
        mPlayer.stop();
        if(active) {
            mPlayer.stop();
        }
    }
    public MediaPlayer getmPlayer() {
        return mPlayer;
    }
}



