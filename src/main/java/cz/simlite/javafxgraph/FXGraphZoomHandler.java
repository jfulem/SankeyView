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
import javafx.scene.Node;
import javafx.util.Duration;

public class FXGraphZoomHandler {

    private final FXGraph graph;

    private double currentZoomLevel;
    private double targetZoomLevel;

    private final Timeline zoomTimeLine;

    public FXGraphZoomHandler(FXGraph aGraph) {
        graph = aGraph;
        currentZoomLevel = 1;
        targetZoomLevel = 1;

        Duration theDuration = Duration.millis(1000 / 25);
        KeyFrame theOneFrame = new KeyFrame(theDuration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateNodePositionsAndScale();
            }
        });

        zoomTimeLine = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(theOneFrame).build();
        zoomTimeLine.play();

    }

    private void updateNodePositionsAndScale() {

        boolean evolvePosition = false;

        if (getCurrentZoomLevel() > targetZoomLevel) {
            setCurrentZoomLevel(getCurrentZoomLevel() - (getCurrentZoomLevel() - targetZoomLevel) * 0.06);
            evolvePosition = true;
        }

        if (getCurrentZoomLevel() < targetZoomLevel) {
            setCurrentZoomLevel(getCurrentZoomLevel() + (targetZoomLevel - getCurrentZoomLevel()) * 0.06);
            evolvePosition = true;
        }

        if (evolvePosition) {

            for (FXNode theNode : graph.getModel().getNodes()) {
                theNode.setZoomLevel(getCurrentZoomLevel());
            }
            
            for(Node node: graph.getContentPane().getChildren()){
                if(node instanceof FXEditableDraggableText){
                    //node.setLayoutX(node.getLayoutX() * getCurrentZoomLevel());
                    //node.setLayoutY(node.getLayoutY() * getCurrentZoomLevel());
                    node.setScaleX(getCurrentZoomLevel());
                    node.setScaleY(getCurrentZoomLevel());
                }
            }

            graph.updateSelectionInScene();
        }

        if (Math.abs(getCurrentZoomLevel() - targetZoomLevel) < 0.01) {
            setCurrentZoomLevel(targetZoomLevel);
        }
    }

    public void zoomOneStepIn() {
        targetZoomLevel -= 0.1;
        if (targetZoomLevel < 0.1) {
            targetZoomLevel = 0.1;
        }
        updateNodePositionsAndScale();
    }

    public void zoomOneStepOut() {
        targetZoomLevel += 0.1;

        updateNodePositionsAndScale();

    }
    
    public void zoomByValue(double value){
        targetZoomLevel += value;

        updateNodePositionsAndScale();
    }

    /**
     * @return the currentZoomLevel
     */
    public double getCurrentZoomLevel() {
        return currentZoomLevel;
    }

    /**
     * @param currentZoomLevel the currentZoomLevel to set
     */
    public void setCurrentZoomLevel(double currentZoomLevel) {
        this.currentZoomLevel = currentZoomLevel;
    }
}
