package Player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class ProgressionBar {
    private String name;
    private boolean isDragged;
    private @FXML MediaPlayer mPlayer;
    private @FXML Slider progressSlider;
    private @FXML Label progressShowLabel;
    private @FXML Duration mFileDuration;

    public ProgressionBar(String name, MediaPlayer mPlayerName, Slider progressSliderName, Label progressLabelName,
                          Duration mediaFileDuration){
        this.name = name;
        this.isDragged = false;
        this.mPlayer = mPlayerName;
        this.progressSlider = progressSliderName;
        this.progressShowLabel = progressLabelName;
        this.mFileDuration = mediaFileDuration;
        progressSliderSetup();
    }
    //Create seek bar and set listeners
    private void progressSliderSetup(){
        try {
            //set up a change listener to be triggered on change in progressed (currentTime) time property
            mPlayer.currentTimeProperty().addListener((observable, oldVal, newVal) -> {
                int oldProgressValue = (int)Math.floor(oldVal.toSeconds()); //FLOOR rounds up double to int
                int newProgressValue = (int)Math.floor(newVal.toSeconds());

                if (oldProgressValue != newProgressValue && !isDragged) { //only run this if SECONDS value changes
                    int progressTimeSeconds = (int)Math.floor(mPlayer.getCurrentTime().toSeconds());
                    int progressTimeHours = progressTimeSeconds/3600%24;
                    int progressTimeMinutes = progressTimeSeconds/60%60;

                    //format progress time information into a string, store it under label (label is display in GUI) object
                    progressShowLabel.setText(String.format("%2d : %2d : %2d", progressTimeHours, progressTimeMinutes, progressTimeSeconds%60));

                    progressSlider.setMax(100.0);
                    double mediaDuration = mPlayer.getMedia().getDuration().toMillis();
                    double currentProgress = newVal.toMillis();
                    progressSlider.setValue(currentProgress / mediaDuration * 100);
                }
            });
            progressSlider.onMousePressedProperty().setValue((mouseEvent) -> isDragged = true);

            progressSlider.onMouseReleasedProperty().setValue((mouseEvent)->{
                int seekBarPercentageValue = (int) progressSlider.getValue();
                mPlayer.seek(mFileDuration.divide(100).multiply(seekBarPercentageValue));
                isDragged = false;
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Create seek bar and set listeners
//    public void createSeekBar(){
//        try {
//            //set up a change listener to be triggered on change in progressed (currentTime) time property
//            mPlayer.currentTimeProperty().addListener((observable, oldVal, newVal) -> {
//                int oldProgressValue = (int)Math.floor(oldVal.toSeconds()); //FLOOR rounds up double to int
//                int newProgressValue = (int)Math.floor(newVal.toSeconds());
//
//                if (oldProgressValue != newProgressValue && !isDragged) { //only run this if SECONDS value changes
//                    int progressTimeSeconds = (int)Math.floor(mPlayer.getCurrentTime().toSeconds());
//                    int progressTimeHours = progressTimeSeconds/3600%24;
//                    int progressTimeMinutes = progressTimeSeconds/60%60;
//
//                    //format progress time information into a string, store it under label (label is display in GUI) object
//                    progressShowLabel.setText(String.format("%2d : %2d : %2d", progressTimeHours, progressTimeMinutes, progressTimeSeconds%60));
//
//                    progressSlider.setMax(100.0);
//                    double mediaDuration = mPlayer.getMedia().getDuration().toMillis();
//                    double currentProgress = newVal.toMillis();
//                    progressSlider.setValue(currentProgress / mediaDuration * 100);
//                }
//            });
//            progressSlider.onMousePressedProperty().setValue((mouseEvent) -> isDragged = true);
//
//            progressSlider.onMouseReleasedProperty().setValue((mouseEvent)->{
//                int seekBarPercentageValue = (int) progressSlider.getValue();
//                mPlayer.seek(mFileDuration.divide(100).multiply(seekBarPercentageValue));
//                isDragged = false;
//            });
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }


}
