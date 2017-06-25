/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.simlite.javafxgraph;

/**
 *
 * @author jfulem
 */
import javafx.scene.input.MouseEvent;

public abstract class FXTool {

    public abstract void mousePressedOnNode(MouseEvent aEvent, FXNode aNode);

    public abstract void mousePressedOnEdge(MouseEvent aEvent, FXEdge aEdge);

    public abstract void mousePressedOnEdgeWayPoint(MouseEvent aEvent, FXEdgeWayPoint aWayPoint);

    public abstract void mousePressed(MouseEvent aEvent);

    public abstract void mouseDragged(MouseEvent aEvent);

    public abstract void mouseReleased(MouseEvent aEvent);
}
