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
public class FXGraphBuilder {

    public static FXGraphBuilder create() {
        return new FXGraphBuilder();
    }

    private FXGraphBuilder() {
    }

    public FXGraph build() {
        return new FXGraph();
    }
}
