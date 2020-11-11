/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Math.abs;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Tarek Elbendary
 */

public class DynamicVisualizer1 implements DynamicVisualizer{
    private AnchorPane vizPane2;
    private double height=0.0;
    private double width=0.0;
    private String vizPaneInitialStyle = "";
    ArrayList<Ellipse> ellipses;
    private int i = 0;
    
    @Override
    public void start(AnchorPane vizPane2) {
        end();
        this.vizPane2 = vizPane2;
        ellipses = new ArrayList<Ellipse>();
        height = vizPane2.getHeight();
        width = vizPane2.getWidth();
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane2.setClip(clip);
    }

    @Override
    public void end() {
        if (ellipses != null) {
            for (Ellipse ellipse : ellipses) {
                vizPane2.getChildren().remove(ellipse);
            }
            ellipses = null;
            vizPane2.setClip(null);
            vizPane2.setStyle(vizPaneInitialStyle);
        }
        i=0;
    }

    @Override
    public void draw(double timestamp, double lenght, double factor, float[] magnitudes, float[] phases) {
        Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(i*factor);
        ellipse.setCenterY(height/2);
        ellipse.setRadiusX(1);
        ellipse.setRadiusY(65+magnitudes[10]);
        ellipse.setFill(Color.hsb((magnitudes[10] * -6.0), 1.0, 1.0, 1.0));
        ellipses.add(ellipse);
        vizPane2.getChildren().add(ellipse);
        i++;
    }
    
    
}
