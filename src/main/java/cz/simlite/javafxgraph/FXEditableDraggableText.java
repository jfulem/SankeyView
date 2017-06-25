/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.simlite.javafxgraph;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author jfulem
 */

/**
 * An EditableText (a text field which looks like a label), which can be dragged
 * around the screen to reposition it.
 */
public class FXEditableDraggableText extends StackPane {

    private final double PADDING = 5;
    private FXEditableText text = new FXEditableText(PADDING, PADDING);

    public FXEditableDraggableText(double x, double y) {
        relocate(x - PADDING, y - PADDING);
        getChildren().add(text);
        getStyleClass().add("editable-draggable-text");

        // if the text is empty when we lose focus,
        // the node has no purpose anymore
        // just remove it from the scene.
        text.focusedProperty().addListener((observable, hadFocus, hasFocus) -> {
            if (!hasFocus && getParent() != null && getParent() instanceof Pane
                    && (text.getText() == null || text.getText().trim().isEmpty())) {
                ((Pane) getParent()).getChildren().remove(this);
            }
        });

        enableDrag();
    }

    public FXEditableDraggableText(int x, int y, String text) {
        this(x, y);
        this.text.setText(text);
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed(mouseEvent -> {
            this.toFront();
            // record a delta distance for the drag and drop operation.
            dragDelta.x = mouseEvent.getX();
            dragDelta.y = mouseEvent.getY();
            getScene().setCursor(Cursor.MOVE);
        });
        setOnMouseReleased(mouseEvent -> getScene().setCursor(Cursor.HAND));
        setOnMouseDragged(mouseEvent -> {
            double newX = getLayoutX() + mouseEvent.getX() - dragDelta.x;
            if (newX > 0 && newX < getScene().getWidth()) {
                setLayoutX(newX);
            }
            double newY = getLayoutY() + mouseEvent.getY() - dragDelta.y;
            if (newY > 0 && newY < getScene().getHeight()) {
                setLayoutY(newY);
            }
        });
        setOnMouseEntered(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });
        setOnMouseExited(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    // records relative x and y co-ordinates.
    private class Delta {
        double x, y;
    }
    
    public void setText(String text){
        this.text.setText(text);
    }
}
