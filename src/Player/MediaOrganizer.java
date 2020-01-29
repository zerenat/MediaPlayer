package Player;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaOrganizer {
    private static FileChooser fileChooser;
    private static List<File> mInfoList;
    private static ObservableList<Media> mList;
    private static Media mFile;

    //private constructor for Static instance
    public MediaOrganizer() {
        this.fileChooser = new FileChooser();
        this.mInfoList = new ArrayList<>();
        this.mList = FXCollections.observableArrayList();
    }

    //Open Media files and place them in a observable list
    public ObservableList<Media> openMedia() {
        try {
            mInfoList = fileChooser.showOpenMultipleDialog(Main.mainStage);
//            FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                    "JPG & GIF Images", "jpg", "gif");

            if (mInfoList != null) {
                mList.removeAll();
                for (int i = 0; i < mInfoList.size(); i++) {
                    mFile = new Media(new File(mInfoList.get(i).getAbsolutePath()).toURI().toString());
                    mList.add(mFile);
                }
                return mList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }
}


//    final URL url = getClass().getResource("resources/defaultAlbum.png");
        //   final Image image = new Image(url.toString());

//    albumCover = new ImageView(image);
        //   albumCover.setFitWidth(240);
        //  albumCover.setPreserveRatio(true);
        // albumCover.setSmooth(true);
        // albumCover.setEffect(reflection);

    //1 Item filechooser
    //FileChooser fileChooser = new FileChooser();
    //mediaFile = new File(fileChooser.showOpenDialog(Main.mainStage).getAbsolutePath());
    //media = new Media(mediaFile.toURI().toString());

