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

import java.util.*;

public class FXGraphModel {

    private final Map<FXSankeyNode, FXNode> nodes;
    private final Set<FXEdge> edges;

    public FXGraphModel() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }

    public void registerNewNode(FXNode aNode) {
        nodes.put(aNode.getNode(), aNode);
    }

    public Collection<FXNode> getNodes() {
        return nodes.values();
    }
    
    public FXNode getNodeById(String id){
        for (FXNode node : this.nodes.values()) {
            if(id.equals(node.getId())){
                return node;
            }
        }
        return null;
    }
    
    public void registerNewEdge(FXEdge aEdge) {
        edges.add(aEdge);
    }

    public Set<FXEdge> getEdges() {
        return edges;
    }
    
    public FXEdge getEdgeById(String id){
        for (FXEdge edge : this.edges) {
            if(id.equals(edge.getId())){
                return edge;
            }
        }
        return null;
    }
}
