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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.MouseButton;

public class FXGraphSelectionTool extends FXTool {

    static final double SELECTION_Z_OFFSET = 20;

    private final Pane owningControl;
    private final FXGraphModel model;
    private final FXGraphZoomHandler zoomHandler;

    private final Set<FXNode> currentSelection;
    private Rectangle currentSelectionRectangle;
    private Timeline currentSelectionTimeline;
    private Rectangle interactiveSelectionRectangle;
    private Timeline interactiveSelectionTimeline;

    private boolean dragging;
    private double lastDragX;
    private double lastDragY;
    private boolean mousePressedOnNodeOrSelection;
    private FXEdgeWayPoint pressedWaypoint;

    FXGraphSelectionTool(Pane aOwningControl, FXGraphModel aModel, FXGraphZoomHandler aZoomHandler) {
        owningControl = aOwningControl;
        model = aModel;
        zoomHandler = aZoomHandler;

        currentSelection = new HashSet<>();
    }

    public void resetSelection() {
        currentSelection.clear();
    }

    public void add(FXNode aNode) {
        currentSelection.add(aNode);
    }

    public void updateSelectionInScene() {
        if (currentSelectionRectangle != null) {
            owningControl.getChildren().remove(currentSelectionRectangle);
            currentSelectionTimeline.stop();
        }

        if (currentSelection.size() > 0) {
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;

            for (FXNode theNode : currentSelection) {
                Bounds theBounds = theNode.getNode().getBoundsInParent();
                minX = Math.min(minX, theBounds.getMinX());
                minY = Math.min(minY, theBounds.getMinY());
                maxX = Math.max(maxX, theBounds.getMaxX());
                maxY = Math.max(maxY, theBounds.getMaxY());
            }

            double startX = minX - 20;
            double startY = minY - 20;
            double width = maxX - minX + 40;
            double height = maxY - minY + 40;

            RectangleBuilder theBuilder = RectangleBuilder.create();
            theBuilder.x(startX).y(startY).width(width).height(height).strokeWidth(1).stroke(Color.BLACK).strokeDashArray(3.0, 7.0, 3.0, 7.0).fill(Color.TRANSPARENT).mouseTransparent(true);

            currentSelectionRectangle = theBuilder.build();
            currentSelectionRectangle.setTranslateZ(SELECTION_Z_OFFSET);
            owningControl.getChildren().add(currentSelectionRectangle);

            Duration theDuration = Duration.millis(1000 / 25);
            KeyFrame theOneFrame = new KeyFrame(theDuration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    currentSelectionRectangle.setStrokeDashOffset(currentSelectionRectangle.getStrokeDashOffset() + 1);
                }
            });

            currentSelectionTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(theOneFrame).build();
            currentSelectionTimeline.play();
        }

    }

    public Set<FXNode> getCurrentSelection() {
        return currentSelection;
    }

    public Rectangle getCurrentSelectionRectangle() {
        return currentSelectionRectangle;
    }

    public boolean contains(FXNode aNode) {
        return currentSelection.contains(aNode);
    }

    public void remove(FXNode aNode) {
        currentSelection.remove(aNode);
    }

    public boolean isSelectionMode() {
        return interactiveSelectionRectangle != null;
    }

    public void startSelectionAt(double aSceneX, double aSceneY) {
        RectangleBuilder theBuilder = RectangleBuilder.create();
        theBuilder.x(aSceneX).y(aSceneY).width(1).height(1).strokeWidth(1).stroke(Color.BLACK).strokeDashArray(3.0, 7.0, 3.0, 7.0).fill(Color.TRANSPARENT).mouseTransparent(true);

        interactiveSelectionRectangle = theBuilder.build();
        interactiveSelectionRectangle.setTranslateZ(SELECTION_Z_OFFSET);

        owningControl.getChildren().add(interactiveSelectionRectangle);

        Duration theDuration = Duration.millis(1000 / 25);
        KeyFrame theOneFrame = new KeyFrame(theDuration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                interactiveSelectionRectangle.setStrokeDashOffset(interactiveSelectionRectangle.getStrokeDashOffset() + 1);
            }
        });

        interactiveSelectionTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(theOneFrame).build();
        interactiveSelectionTimeline.play();

    }

    public void enhanceSelectionTo(double aSceneX, double aSceneY) {
        double width = aSceneX - interactiveSelectionRectangle.getX();
        double height = aSceneY - interactiveSelectionRectangle.getY();

        interactiveSelectionRectangle.setWidth(width);
        interactiveSelectionRectangle.setHeight(height);
    }

    public void endSelection() {

        for (FXNode theNode : model.getNodes()) {
            if (interactiveSelectionRectangle.intersects(theNode.getNode().getBoundsInParent())) {
                add(theNode);
            }
        }

        interactiveSelectionTimeline.stop();
        interactiveSelectionTimeline = null;

        owningControl.getChildren().remove(interactiveSelectionRectangle);
        interactiveSelectionRectangle = null;

        updateSelectionInScene();
    }

    @Override
    public void mousePressedOnNode(MouseEvent aEvent, FXNode aNode) {

        mousePressedOnNodeOrSelection = true;
        pressedWaypoint = null;

        if (!(aEvent.isControlDown() || aEvent.isShiftDown())) {
            resetSelection();
            add(aNode);
        } else {
            if (contains(aNode)) {
                remove(aNode);
            } else {
                add(aNode);
            }
        }
        updateSelectionInScene();
    }

    @Override
    public void mousePressedOnEdge(MouseEvent aEvent, FXEdge aEdge) {

        mousePressedOnNodeOrSelection = true;
        pressedWaypoint = null;

        if (aEvent.isShiftDown()) {
            aEdge.addWayPoint(new FXEdgeWayPoint(aEdge, aEvent.getSceneX() / zoomHandler.getCurrentZoomLevel(), aEvent.getSceneY() / zoomHandler.getCurrentZoomLevel()));
        }
        resetSelection();
        updateSelectionInScene();
    }

    @Override
    public void mousePressedOnEdgeWayPoint(MouseEvent aEvent, FXEdgeWayPoint aWayPoint) {
        mousePressedOnNodeOrSelection = false;
        pressedWaypoint = aWayPoint;
    }

    @Override
    public void mousePressed(MouseEvent aEvent) {

        mousePressedOnNodeOrSelection = false;
        pressedWaypoint = null;

        Rectangle theSelection = getCurrentSelectionRectangle();
        if (theSelection != null) {
            if (theSelection.contains(aEvent.getX(), aEvent.getY())) {
                mousePressedOnNodeOrSelection = true;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent aEvent) {
        if(aEvent.getButton() == MouseButton.PRIMARY){
            if ((mousePressedOnNodeOrSelection || pressedWaypoint != null) && !aEvent.isControlDown()) {
                if (!dragging) {
                    dragging = true;
                } else {
                    double movementX = aEvent.getSceneX() - lastDragX;
                    double movementY = aEvent.getSceneY() - lastDragY;

                    if (pressedWaypoint == null) {
                        for (FXNode theNode : getCurrentSelection()) {
                            theNode.translatePosition(movementX, movementY, zoomHandler.getCurrentZoomLevel());
                        }
                    } else {
                        pressedWaypoint.translatePosition(movementX / zoomHandler.getCurrentZoomLevel(), movementY / zoomHandler.getCurrentZoomLevel(), zoomHandler.getCurrentZoomLevel());
                    }


                    updateSelectionInScene();
                }
                lastDragX = aEvent.getSceneX();
                lastDragY = aEvent.getSceneY();

            } else {
                if (!isSelectionMode()) {

                    if (!aEvent.isShiftDown()) {
                        resetSelection();
                    }
                    if(aEvent.isShiftDown()){
                        startSelectionAt(aEvent.getX(), aEvent.getY());
                    }
                } else {
                    if(aEvent.isShiftDown()){
                        enhanceSelectionTo(aEvent.getX(), aEvent.getY());
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent aEvent) {
        if (dragging) {
            dragging = false;

            updateSelectionInScene();
        }

        if (isSelectionMode()) {
            endSelection();
        }
    }
}
