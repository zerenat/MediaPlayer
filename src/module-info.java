module MediaPlayerV2 {
        requires javafx.fxml;
        requires javafx.controls;
        requires javafx.media;
        requires java.compiler;
        //requires kotlin.stdlib;

        opens Player;
}