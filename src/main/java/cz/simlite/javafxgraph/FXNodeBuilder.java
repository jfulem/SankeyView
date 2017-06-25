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
import javafx.scene.Node;

public class FXNodeBuilder {

    private final FXGraph graph;
    private FXSankeyNode node;
    private double positionX;
    private double positionY;
    private String name;
    private String id;

    public FXNodeBuilder(FXGraph aGraph) {
        graph = aGraph;
    }

    public FXNodeBuilder addNode(FXSankeyNode node) {
        this.node = node;
        return this;
    }

    public FXNodeBuilder setX(double x) {
        positionX = x;
        return this;
    }

    public FXNodeBuilder setY(double y) {
        positionY = y;
        return this;
    }
    
    public FXNodeBuilder setName(String name) {
        this.name = name;
        return this;
    }
    
    public FXNodeBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public FXNode build() {
        FXNode theNode = new FXNode(graph, node);
        theNode.setPosition(positionX, positionY);
        theNode.setName(name);
        theNode.setId(id);
        graph.addNode(theNode);
        return theNode;
    }
}
