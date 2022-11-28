import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
 
public class ClientApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // creates a media player, displays it, and plays the media
        //### put file location that is being sent here
        MediaPlayer mediaPlayer = new MediaPlayer(new Media("file:/C:/Users/Aidan/Downloads/Desktop/ParallelClientVideo/7MbVideo.mp4")); 
        primaryStage.setScene(new Scene(new StackPane(new MediaView(mediaPlayer)), 1280, 720));
        primaryStage.show();
        // closes the media player and file after playing to the end
        mediaPlayer.setOnEndOfMedia(() -> {
            System.exit(0);
        });
        mediaPlayer.play();
    }

    // launches the media player
    public static void run(String[] args) { 
        launch(args); 
    }
}