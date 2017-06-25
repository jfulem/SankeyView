/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.simlite.javafxgraph;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.scene.control.TextField;



/**
 *
 * @author jfulem
 *
 * A text field which has no special decorations like background, border or
 * focus ring. i.e. the EditableText just looks like a vanilla Text node or a
 * Label node.
 */
public class FXEditableText extends TextField {
    // The right margin allows a little bit of space
    // to the right of the text for the editor caret.

    private final double RIGHT_MARGIN = 5;

    public FXEditableText(double x, double y) {
        relocate(x, y);
        getStyleClass().add("editable-text");
        FontMetrics metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(getFont());
        setPrefWidth(RIGHT_MARGIN);
        textProperty().addListener((observable, oldTextString, newTextString)
                -> setPrefWidth(metrics.computeStringWidth(newTextString) + RIGHT_MARGIN)
        );
        Platform.runLater(this::requestFocus);
    }
}
