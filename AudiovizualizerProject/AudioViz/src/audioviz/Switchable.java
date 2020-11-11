/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Professor Wergeles
 */
// this class was made by Nickolas Wergeles
public abstract class Switchable {
    public static Scene scene;
    public static final HashMap<String, Switchable> controllers = new HashMap<>();
    
    private Parent root;  
    
    // this function was made by Nickolas Wergeles
    public static Switchable add(String name) {
        Switchable controller; 
        
        controller = controllers.get(name); 
        
        if(controller == null){
            try {
                
                FXMLLoader loader = new FXMLLoader(Switchable.class.getResource(name + ".fxml"));
                 
                Parent root = loader.load();
                
                controller = loader.getController(); 

                controller.setRoot(root); 
                
                controllers.put(name, controller); 
                
            } catch (IOException ex) {
                Logger.getLogger(Switchable.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error loading " + name + ".fxml \n\n " + ex); 
                controller = null; 
            } catch (Exception ex){
                System.out.println("Error loading " + name + ".fxml \n\n " + ex); 
                controller = null; 
            }
        }
        return controller; 
    }
    
    // this function was made by Nickolas Wergeles
    public static void switchTo(String name) {
        Switchable controller = controllers.get(name); 
        
        if(controller == null){
            controller = add(name); 
        }
        
        if(controller != null){
            if(scene != null){
                scene.setRoot(controller.getRoot());
            }
        }
    }
    
    // this function was made by Nickolas Wergeles
    public void setRoot(Parent root) {
        this.root = root;
    }
    
    // this function was made by Nickolas Wergeles
    public Parent getRoot() {
        return root;
    }
    
    // this function was made by Nickolas Wergeles
    public static Switchable getControllerByName(String name) {
        return controllers.get(name);
    }
}