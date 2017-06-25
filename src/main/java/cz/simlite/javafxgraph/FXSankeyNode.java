/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.simlite.javafxgraph;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 *
 * @author jfulem
 */
public class FXSankeyNode extends VBox{
    
    private final Rectangle rectangle;
    private Text textField;
    /**
     * 
     * @param spacing
     * @param w
     * @param h
     * @param color
     * @param text 
     */
    public FXSankeyNode(double spacing, double w, double h, Paint color, String text) {
        setSpacing(spacing);
        setAlignment(Pos.TOP_CENTER);
        this.rectangle = new Rectangle(w, h);
        this.rectangle.setFill(color);
        textField = new Text(text);
        getChildren().addAll(this.rectangle, textField);
    }
    
    /**
     * 
     * @param angle 
     */
    public void rotateNode(double angle){
        getTransforms().add(new Rotate(angle));
    }
    
    /**
     * 
     * @return 
     */
    public Bounds getConnectorDim(){
        return this.localToParent(this.rectangle.getBoundsInParent());//this.localToScene(this.rectangle.getBoundsInParent());
    }

    /**
     * 
     * @param color 
     */
    public void setNodePaint(Paint color) {
        this.rectangle.setFill(color);
    }
    
    /**
     * 
     * @param value 
     */
    public void setCapacity(double value){
        this.rectangle.setHeight(value);
    }

    /**
     * @param text 
     */
    public void setTextField(String text) {
        getChildren().clear();
        textField = new Text(text);
        getChildren().addAll(this.rectangle, textField);
    }
    
    public Text getTextField(){
        return this.textField;
    }

}
