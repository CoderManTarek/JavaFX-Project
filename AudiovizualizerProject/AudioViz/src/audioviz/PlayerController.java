/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Tarek Elbendary && Nickolas Wergeles 
 * 
 * Music: http://www.bensound.com/royalty-free-music
 * http://www.audiocheck.net/testtones_sinesweep20-20k.php
 * http://stackoverflow.com/questions/11994366/how-to-reference-primarystage
 */
public class PlayerController extends Switchable implements Initializable {

//    @FXML
//    private AnchorPane vizPane;
    
    @FXML 
    private AnchorPane vizPane2;

    @FXML
    private MediaView mediaView;

    @FXML
    private Text filePathText;

    @FXML
    private Text lengthText;

    @FXML
    private Text currentText;

    @FXML
    private Text bandsText;

    @FXML
    private Text errorText;

    @FXML
    private Button playPause;

    private Media media;
    private MediaPlayer mediaPlayer;

    private Integer numOfBands = 120;
    private final Double updateInterval = 0.05;
    
    private DynamicVisualizer currentVisualizer2;
    
    private double audioLength;
    private double windowLength;
    private double factor;
    
    private Stage stage; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bandsText.setText(Integer.toString(numOfBands));
        currentVisualizer2 = new DynamicVisualizer1();

    }
    
    @FXML
    private void handleGoToFullscreen(ActionEvent event) {
        Switchable.switchTo("Player2");
    }
    
    // this function was made by Nickolas Wergeles
    private void openMedia(File file) {
        filePathText.setText("");
        errorText.setText("");

        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }

        try {
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnReady(() -> {
                handleReady();
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                handleEndOfMedia();
            });
            mediaPlayer.setAudioSpectrumNumBands(numOfBands);
            mediaPlayer.setAudioSpectrumInterval(updateInterval);
            mediaPlayer.setAudioSpectrumListener((double timestamp, double duration, float[] magnitudes, float[] phases) -> {
                handleVisualize(timestamp, duration, factor, magnitudes, phases);
            });
            mediaPlayer.setAutoPlay(true);
            filePathText.setText(file.getPath());
        } catch (Exception ex) {
            errorText.setText(ex.toString());
        }
    }
    
    // this function was made Nickolas Wergeles, but I slightly changed it to fit my specific program
    private void handleReady() {
        Duration duration = mediaPlayer.getTotalDuration();
        audioLength = duration.toMillis() / 10 / 5;
        windowLength = vizPane2.getWidth();
        factor = windowLength/audioLength;
        lengthText.setText(duration.toString());
        Duration ct = mediaPlayer.getCurrentTime();
        currentText.setText(ct.toString());
//        currentVisualizer.start(numOfBands, vizPane);
        currentVisualizer2.start(vizPane2);
    }

    // this function was made by Nickolas Wergeles
    private void handleEndOfMedia() {
        mediaPlayer.stop();
        mediaPlayer.seek(Duration.ZERO);;
    }

    // this function was made by Nickolas Wergeles, but I slghtly changed it to fit my program
    private void handleVisualize(double timestamp, double duration, double factor, float[] magnitudes, float[] phases) {
        Duration ct = mediaPlayer.getCurrentTime();
        double ms = ct.toMillis();
        currentText.setText(String.format("%.1f ms", ms));       
//        currentVisualizer.draw(timestamp, duration, magnitudes, phases);
        currentVisualizer2.draw(timestamp, duration, factor, magnitudes, phases);
        
    }

    // this function was made by Nickolas Wergeles
    @FXML
    private void handleOpen(Event event) {
        Stage primaryStage = (Stage) vizPane2.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            openMedia(file);
        }
    }

    @FXML
    private void handleSave(Event event) {
        WritableImage image = vizPane2.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if(file==null){
            System.out.println("File Not Selected");
            return;
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (FileNotFoundException ex) {
            String message = "File not found exception occured while saving to " + file.getPath(); 
            displayExceptionAlert(message, ex); 
                
        } catch (IOException ex) {
            String message = "IOException occured while saving to " + file.getPath();
            displayExceptionAlert(message, ex); 
        }
    }
    
    // this function was made by Nickolas Wergeles
    @FXML
    private void handlePlayPause(ActionEvent event) {
        if (mediaPlayer != null) {
            if (!isPlaying()) {
                play(); 
            } else {
                pause(); 
            }
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("There was an error playing the song.");
            alert.setContentText("You must select a song first before I can play it for you.");

            alert.showAndWait();
        }

    }
    
    // this function was made by Nickolas Wergeles
    public void play(){
        if(mediaPlayer != null){
            mediaPlayer.play(); 
            playPause.setText("Pause");
        }   
    }
    
    // this function was made by Nickolas Wergeles
    public void pause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            playPause.setText("Play");
        }
    }
    
    // this function was made by Nickolas Wergeles
    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            playPause.setText("Play"); 
        }
    }

    // this function was made by Nickolas Wergeles
    @FXML
    private void handleStop(ActionEvent event) {
        stop();
        handleReady();
    }

    // this function was made by Nickolas Wergeles
    public boolean isPlaying(){
        if(mediaPlayer != null){
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                return true; 
            }
        }
        return false; 
    }
    
    // this function was made by Nickolas Wergeles
    private void displayExceptionAlert(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }  
}
