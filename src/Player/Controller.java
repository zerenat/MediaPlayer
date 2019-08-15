package Player;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


//TODO LIST
//FIX BUG NEXT MEDIA/ PREV MEDIA VIDEO CRASH
//ADD AUTO-ROTATE ON PLAY
//SAVE MEDIA THAT WAS PLAYED, LAST TIME PLAYER WAS USED
//MAKE SURE THE PROGRAM LOADS MEDIA WITHOUT TOO MUCH DELA (TRY LOADING MEDIA IN DIFFERENT THREADS)

//TODO TEST-LIST
//TEST IF APP IS ABLE TO RUN WITHOUT RUNNING STUCK
//TEST ALL THE BUTTONS
//TEST IF ALL TEH FUNCTIONALITY WORKS AS INTENDED

//NOTES
//Synchronize to sync threads for shared data
public class Controller{
    @FXML private GridPane mViewPane;
    @FXML private MediaView mView;
    @FXML private TextField text;
    @FXML private Slider seekSlider;
    @FXML private ListView mInfo;
    private MediaPlayer mPlayer;
    private ObservableList<Media> mList;
    private int bookMark;
    private boolean active;
    private MediaOrganizer mediaOrganizer;

    public void initialize(){
        mediaOrganizer = new MediaOrganizer();
        active = false;
        Stage mainStage = Main.mainStage;

        //Height listener to modify mediaView size according to parent
        mainStage.heightProperty().addListener((observable, oldVal, newVal)->{
            mViewPane.setPrefHeight(Main.mainStage.getHeight()-37);
            mView.setFitHeight(Main.mainStage.getHeight()*90/100);
        });
        //Width listener to modify mediaView size according to parent
        //This is what fixed the overlap issues (Video cut from areas)
        mainStage.widthProperty().addListener((observable, oldVal, newVal)->{
            mViewPane.setPrefWidth(Main.mainStage.getWidth()*75/100);
            mView.setFitWidth(mViewPane.getWidth()*70/100);
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
            //mInfo.getItems().clear();
            mList = mediaOrganizer.openMedia();
            mInfo.getItems().addAll(mList);

            if (mPlayer == null){
                bookMark = 0;
                createMediaPlayer();
            }
        }catch(RuntimeException e){
            e.printStackTrace();
        }
    }
    //Create seek bar
    public void createSeekBar(){
        try{

            mPlayer.currentTimeProperty().addListener((observable, newVal, oldVal)->{
                //System.out.println("current time property: "+((mPlayer.getTotalDuration().toMillis() / 100) * mPlayer.getCurrentTime().toMillis()));
                System.out.println("Seek slider placeholder = "+(mPlayer.getCurrentTime().toMillis()/mPlayer.getTotalDuration().toMillis())*100);
                seekSlider = new Slider();
                double mediaDuration = mPlayer.getCycleDuration().toMillis();
                seekSlider.valueProperty().setValue((mPlayer.getCurrentTime().toMillis()/mediaDuration) * 100);
                seekSlider.setValue(0.6);
                //seekSlider.setValue((mediaDuration / mPlayer.getCurrentTime().toMillis()) * 100);
            });
            System.out.println(mPlayer.getCurrentTime());
        }catch(Exception e){
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
                mPlayer.stop();
                disposeMediaPlayer();
                createMediaPlayer();
            }
            mPlayer = new MediaPlayer(mList.get(bookMark));
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
            mPlayer = new MediaPlayer(mList.get(bookMark));
        }
    }
    //Create media player
    private void createMediaPlayer(){
        active = true;
        mPlayer = new MediaPlayer(mList.get(bookMark));
        mView.setMediaPlayer(mPlayer);
        mPlayer.onReadyProperty().setValue(()-> {
            playMedia();
            System.out.println((String)mList.get(bookMark).getMetadata().get("title"));
            Thread thread = new Thread(new MediaBufferThread());
            createSeekBar();
        });

        //createSeekBar();
        //System.out.println("create");
    }
    //Dispose of media player
    private void disposeMediaPlayer(){
        active = false;
        mPlayer.dispose();
        //System.out.println("dispose");
    }
    //Play media
    //Functionality for "play" button
    public void playMedia(){
        if(active){
            try{

                mPlayer.play();
            }catch(Exception e){
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
