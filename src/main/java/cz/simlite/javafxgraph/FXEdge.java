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
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.RectangleBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.CubicCurve;

public class FXEdge {

    FXGraph graph;
    FXNode source;
    FXNode destination;
    Node displayShape;
    private FXArrow arrow;
    private double value;
    private Paint paint;
    String id;
    List<FXEdgeWayPoint> wayPoints = new ArrayList<>();
    Map<FXEdgeWayPoint, Node> wayPointHandles = new HashMap<>();

    public FXEdge(FXGraph graph, FXNode source, FXNode destination) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.value = 1.0;
        this.paint = Color.web("#008744");
    }

    public FXEdge(FXGraph graph, FXNode source, FXNode destination, double value) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.value = value;
        this.paint = Color.web("#008744");
    }

    public FXEdge(FXGraph graph, FXNode source, FXNode destination, double value, Paint paint) {
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.value = value;
        this.paint = paint;
    }

    public void addWayPoint(FXEdgeWayPoint wayPoint) {
        wayPoints.add(wayPoint);
        graph.updateEdge(this, graph.getZoomHandler().getCurrentZoomLevel());
    }

    public void removeWayPoint(FXEdgeWayPoint wayPoint) {
        wayPoints.remove(wayPoint);
        graph.updateEdge(this, graph.getZoomHandler().getCurrentZoomLevel());
    }

    public Node compileDisplayShapeFor(FXEdgeWayPoint aWayPoint, double aZoomLevel) {
        RectangleBuilder theBuilder = RectangleBuilder.create();
        theBuilder.width(4).height(4).fill(Color.RED).stroke(Color.RED);
        Node theNode = theBuilder.build();
        theNode.setScaleX(aZoomLevel);
        theNode.setScaleY(aZoomLevel);
        theNode.setLayoutX((aWayPoint.positionX - 2) * aZoomLevel);
        theNode.setLayoutY((aWayPoint.positionY - 2) * aZoomLevel);
        theNode.setUserData(aWayPoint);
        return theNode;
    }

    public void computeDisplayShape(double currentZoomLevel) {
        Path thePath = new Path();
        thePath.setUserData(this);
        //Bounds theSourceBounds = source.node.getBoundsInParent();
        //Bounds theDestinationBounds = destination.node.getBoundsInParent();
        // From the middle of the source
        //MoveTo theMoveTo = new MoveTo(theSourceBounds.getMinX() + theSourceBounds.getWidth(), theSourceBounds.getMinY() + theSourceBounds.getHeight() / 2);
        //thePath.getElements().add(theMoveTo);
        wayPointHandles.clear();
        // Thru the waypoints
        for (FXEdgeWayPoint theWayPoint : wayPoints) {
            wayPointHandles.put(theWayPoint, compileDisplayShapeFor(theWayPoint, currentZoomLevel));

            //LineTo theLineTo = new LineTo(theWayPoint.positionX * aCurrentZoomLevel, theWayPoint.positionY * aCurrentZoomLevel);
            //thePath.getElements().add(svgPath);
            //thePath.getElements().add(theLineTo);
        }
        // To the middle of the destination
        CubicCurve curve = createCubicCurve(source, destination, currentZoomLevel);
        displayShape = curve;
        // prepare the arrow shape with scaling factor
        double[] arrowShape = new double[]{0, 0, 10, 20, -10, 20};
        double[] scaledArrowShape = new double[arrowShape.length];
        for (int i = 0; i < arrowShape.length; i++) {
            scaledArrowShape[i] = arrowShape[i] * currentZoomLevel;
        }
        arrow = new FXArrow((CubicCurve) displayShape, 0.7f, this.paint, scaledArrowShape);

    }

    public void removeAllNodes(Pane pane) {
        pane.getChildren().remove(displayShape);
        pane.getChildren().removeAll(wayPointHandles.values());
        pane.getChildren().remove(getArrow());
    }

    public void addAllNodes(Pane pane, double aZIndex) {
        // add curve
        pane.getChildren().add(displayShape);
        displayShape.setTranslateZ(aZIndex);
        displayShape.toBack();
        // add arrow
        pane.getChildren().add(getArrow());
        getArrow().setTranslateZ(aZIndex);
        getArrow().toFront();
        // add the waypoints
        for (Node theNode : wayPointHandles.values()) {
            theNode.setTranslateZ(aZIndex);
            pane.getChildren().add(theNode);
            theNode.toBack();
        }
    }

    private CubicCurve createCubicCurve(FXNode source, FXNode destination, double zoomLevel) {
        Bounds sourceBounds = source.getNode().getConnectorDim();//getBoundsInParent();
        Bounds destinationBounds = destination.getNode().getConnectorDim();//.getBoundsInParent();
        double x0 = sourceBounds.getMaxX() + this.getValue() * zoomLevel / 2;
        double x1 = destinationBounds.getMinX() - this.getValue() * zoomLevel / 2;
        double x2 = (x0 + x1) / 2.0;
        double x3 = x2;
        double y0 = sourceBounds.getMinY() + sourceBounds.getHeight() / 2;
        double y1 = destinationBounds.getMinY() + destinationBounds.getHeight() / 2;
        CubicCurve curve = new CubicCurve();
        curve.setStartX(x0);
        curve.setStartY(y0);
        curve.setControlX1(x2);
        curve.setControlY1(y0);
        curve.setControlX2(x3);
        curve.setControlY2(y1);
        curve.setEndX(x1);
        curve.setEndY(y1);
        //curve.setStroke(Color.FORESTGREEN.deriveColor(0, 1.2, 1, 0.6));
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.REFLECT,
                new Stop(0.0, ((Color)paint).deriveColor(0, 1, 1, 0.5)),
                new Stop(1.0, ((Color)paint).darker().deriveColor(0, 1, 1, 0.5)));
        curve.setStroke(lg);
        //curve.setStyle("-fx-stroke: " + this.paint + ";");
        curve.setStrokeWidth(this.getValue() * zoomLevel);
        curve.setFill(null);
        Tooltip t = new Tooltip(source.getName() + " --> " + destination.getName());
        Tooltip.install(curve, t);
        return curve;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the paint
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @param paint the paint to set
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /**
     * @return the arrow
     */
    public FXArrow getArrow() {
        return arrow;
    }
}
