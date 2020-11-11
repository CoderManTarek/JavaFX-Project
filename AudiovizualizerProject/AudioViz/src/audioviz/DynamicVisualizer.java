/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Tarek Elbendary
 */
public interface DynamicVisualizer {
    public void start(AnchorPane vizPane2);
    public void end();
    public void draw(double timestamp, double lenght, double factor, float[] magnitudes, float[] phases);
}
