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
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Paint;

public class FXEdgeBuilder {

    private final FXGraph graph;
    private FXNode source;
    private FXNode destination;
    private double value = 1.0;
    private Paint paint;
    private final List<FXEdgeWayPoint> wayPoints = new ArrayList<>();
    private String id;
    
    public FXEdgeBuilder(FXGraph graph) {
        this.graph = graph;
    }

    public FXEdgeBuilder source(FXNode source) {
        this.source = source;
        return this;
    }

    public FXEdgeBuilder destination(FXNode destination) {
        this.destination = destination;
        return this;
    }

    public FXEdgeBuilder wayPoint(FXEdgeWayPoint wayPoint) {
        wayPoints.add(wayPoint);
        return this;
    }
    
    public FXEdgeBuilder value(double value) {
        this.value = value;
        return this;
    }
    
    public FXEdgeBuilder paint(Paint paint) {
        this.paint = paint;
        return this;
    }
    
    public FXEdgeBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public FXEdge build() {
        FXEdge theEdge = new FXEdge(graph, source, destination, value, paint);
        theEdge.wayPoints.addAll(wayPoints);
        theEdge.setId(id);
        graph.updateNodesWithEdge(source, destination, theEdge);
        graph.addEdge(theEdge);
        return theEdge;
    }
}
