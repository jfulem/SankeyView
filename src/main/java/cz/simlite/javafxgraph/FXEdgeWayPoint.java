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
public class FXEdgeWayPoint {

    double positionX;
    double positionY;
    FXEdge edge;

    public FXEdgeWayPoint(FXEdge aEdge, double positionX, double positionY) {
        edge = aEdge;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void translatePosition(double movementX, double movementY, double zoomLevel) {
        this.positionX += movementX;
        this.positionY += movementY;

        this.edge.graph.updateEdge(edge, zoomLevel);
    }
}
