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
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Node;

public class FXNode {

    private FXSankeyNode node;

    FXGraph owner;

    double positionX;

    double positionY;
    
    private List<FXEdge> incomingEdges;
    
    private List<FXEdge> outcomingEdges;
    
    private String name;
    
    private String id;
    
    @SuppressWarnings("LeakingThisInConstructor")
    FXNode(FXGraph owner, FXSankeyNode node) {
        this.node = node;
        this.owner = owner;
        this.node.setUserData(this);
    }

    public void setPosition(double positionX, double positionY) {
        getNode().relocate(positionX, positionY);
        this.positionX = positionX;
        this.positionY = positionY;
        owner.updateEdgeNodesFor(this);
    }

    public void translatePosition(double movementX, double movementY, double zoomLevel) {
        getNode().setLayoutX(getNode().getLayoutX() + movementX);
        getNode().setLayoutY(getNode().getLayoutY() + movementY);

        positionX += movementX / zoomLevel;
        positionY += movementY / zoomLevel;

        owner.updateEdgeNodesFor(this);
    }

    public void setZoomLevel(double zoomLevel) {
        getNode().setLayoutX(positionX * zoomLevel);
        getNode().setLayoutY(positionY * zoomLevel);
        getNode().setScaleX(zoomLevel);
        getNode().setScaleY(zoomLevel);

        owner.updateEdgeNodesFor(this, zoomLevel);
    }

    /**
     * @return the incomingEdges
     */
    public List<FXEdge> getIncomingEdges() {
        if (incomingEdges == null) {
            incomingEdges = new LinkedList<>();
        }
        return incomingEdges;
    }

    /**
     * @return the outcomingEdges
     */
    public List<FXEdge> getOutcomingEdges() {
        if (outcomingEdges == null) {
            outcomingEdges = new LinkedList<>();
        }
        return outcomingEdges;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public double getTotalIncome(){
        double val = 0;
        if(incomingEdges != null){
            for (FXEdge edge : incomingEdges) {
                val += edge.getValue();
            }
        }
        return val;
    }
    
    public double getTotalOutcome(){
        double val = 0;
        if(outcomingEdges != null){
            for (FXEdge edge : outcomingEdges) {
                val += edge.getValue();
            }
        }
        return val;
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
     * @return the node
     */
    public FXSankeyNode getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(FXSankeyNode node) {
        this.node = node;
    }
    
}
