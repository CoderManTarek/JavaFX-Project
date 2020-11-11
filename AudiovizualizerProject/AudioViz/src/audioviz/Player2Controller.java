/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Tarek Elbendary 
 * 
 * Music: http://www.bensound.com/royalty-free-music
 * http://www.audiocheck.net/testtones_sinesweep20-20k.php
 * http://stackoverflow.com/questions/11994366/how-to-reference-primarystage
 */
public class Player2Controller extends Switchable implements Initializable {      
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    @FXML
    private void handleExit(ActionEvent event) {
        Switchable.switchTo("Player");
    }
}
