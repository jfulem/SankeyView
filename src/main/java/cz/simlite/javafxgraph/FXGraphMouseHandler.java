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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class FXGraphMouseHandler {

    private FXGraph graph;

    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<ScrollEvent> scrolLEventHandler;
    private double pressedX, pressedY;

    public FXGraphMouseHandler(FXGraph aGraph) {

        graph = aGraph;

        mousePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                pressedX = aEvent.getX();
                pressedY = aEvent.getY();
                Object theSource = aEvent.getSource();
                if (!aEvent.isControlDown()) {
                    if (theSource instanceof FXSankeyNode) {
                        FXSankeyNode theNode = (FXSankeyNode) theSource;
                        Object theUserData = theNode.getUserData();

                        if (theUserData instanceof FXNode) {
                            graph.currentTool.mousePressedOnNode(aEvent, (FXNode) theUserData);
                        } else if (theUserData instanceof FXEdge) {
                            graph.currentTool.mousePressedOnEdge(aEvent, (FXEdge) theUserData);
                        } else if (theUserData instanceof FXEdgeWayPoint) {
                            graph.currentTool.mousePressedOnEdgeWayPoint(aEvent, (FXEdgeWayPoint) theUserData);
                        } else {
                            graph.currentTool.mousePressed(aEvent);
                        }
                    } else {
                        graph.currentTool.mousePressed(aEvent);
                    }
                } else {
                    FXEditableDraggableText text = new FXEditableDraggableText(pressedX, pressedY);
                    if (!(theSource instanceof FXSankeyNode)) {
                        if (theSource instanceof Pane) {
                            ((Pane) theSource).getChildren().add(text);
                        }
                    }
                    graph.currentTool.mousePressed(aEvent);
                }
                aEvent.consume();
            }
        };

        mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                // responsible for scene panning
                if (aEvent.getButton() == MouseButton.SECONDARY) {
                    graph.contentPane.setTranslateX(graph.contentPane.getTranslateX() + aEvent.getX() - pressedX);
                    graph.contentPane.setTranslateY(graph.contentPane.getTranslateY() + aEvent.getY() - pressedY);

                } else {
                    graph.currentTool.mouseDragged(aEvent);
                }
                aEvent.consume();
            }
        };

        mouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {

                graph.currentTool.mouseReleased(aEvent);

                aEvent.consume();
            }
        };

        scrolLEventHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent aEvent) {
                if (aEvent.getDeltaY() > 0) {
                    graph.getZoomHandler().zoomOneStepOut();
                } else {
                    graph.getZoomHandler().zoomOneStepIn();
                }
            }
        };

        aGraph.setOnScroll(scrolLEventHandler);
    }

    public void registerHandlerFor(Node aNode) {
        aNode.setOnMouseDragged(mouseDraggedEventHandler);
        aNode.setOnMousePressed(mousePressedEventHandler);
        aNode.setOnMouseReleased(mouseReleasedEventHandler);
        aNode.setOnScroll(scrolLEventHandler);
    }

    public void registerNewNode(FXNode aNode) {
        registerHandlerFor(aNode.getNode());
    }

    public void registerNewEdge(FXEdge aEdge) {
        registerHandlerFor(aEdge.displayShape);
        for (Node theNode : aEdge.wayPointHandles.values()) {
            registerHandlerFor(theNode);
        }
    }
}
