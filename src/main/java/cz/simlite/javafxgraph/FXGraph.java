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
import cz.simlite.sankeyview.SankeyView;
import javafx.geometry.Side;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public final class FXGraph extends ScrollPane {

    static final double NODES_Z_OFFSET = 10;
    static final double EDGES_Z_OFFSET = 10;

    Pane contentPane;

    private final FXGraphModel model;
    FXGraphSelectionTool selectionTool;
    private final FXGraphZoomHandler zoomHandler;

    FXTool currentTool;

    private final FXGraphMouseHandler mouseHandler;

    public FXGraph() {

        model = new FXGraphModel();

        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        //setStyle("-fx-background-color: transparent;");
        
        getStylesheets().add(SankeyView.class.getResource("/css/default.css").toExternalForm());
        Image im = new Image(SankeyView.class.getResourceAsStream("/images/banner.jpg"));
        
        BackgroundImage image = new BackgroundImage(im, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.RIGHT, 20, false, Side.TOP, 20, false), new BackgroundSize(131, 80, false, false, false, false));
        setBackground(new Background(image));
        setPannable(true);
        //FXEditableText title = new FXEditableText(5, 5);
        TextField title = new TextField();
        title.setText("Ricardo Sankey Diagram");
        title.autosize();
        title.getStyleClass().add("title-text");
        contentPane = new Pane();
        VBox pane = new VBox(title, contentPane);
        setContent(pane);
        contentPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
        zoomHandler = new FXGraphZoomHandler(this);
        selectionTool = new FXGraphSelectionTool(contentPane, getModel(), getZoomHandler());
        mouseHandler = new FXGraphMouseHandler(this);

        mouseHandler.registerHandlerFor(contentPane);
        currentTool = selectionTool;
    }

    void updateEdge(FXEdge aEdge, double aZoomLevel) {
        aEdge.removeAllNodes(contentPane);
        aEdge.computeDisplayShape(aZoomLevel);
        aEdge.addAllNodes(contentPane, EDGES_Z_OFFSET);
        mouseHandler.registerNewEdge(aEdge);
    }
    
    public void updateEdge(FXEdge aEdge) {
        aEdge.removeAllNodes(contentPane);
        aEdge.computeDisplayShape(getZoomHandler().getCurrentZoomLevel());
        aEdge.addAllNodes(contentPane, EDGES_Z_OFFSET);
        mouseHandler.registerNewEdge(aEdge);
    }
    
    void updateEdgeNodesFor(FXNode aNode, double aZoomLevel) {
        for (FXEdge theEdge : getModel().getEdges()) {
            if (theEdge.source == aNode || theEdge.destination == aNode) {
                updateEdge(theEdge, aZoomLevel);
            }
        }
    }

    public void updateEdgeNodesFor(FXNode aNode) {
        updateEdgeNodesFor(aNode, getZoomHandler().getCurrentZoomLevel());
    }

    public void updateSelectionInScene() {
        selectionTool.updateSelectionInScene();
    }

    public void addNode(FXNode aNode) {

        aNode.getNode().setTranslateZ(NODES_Z_OFFSET);

        contentPane.getChildren().add(aNode.getNode());

        getModel().registerNewNode(aNode);
        mouseHandler.registerNewNode(aNode);
    }

    public void addEdge(FXEdge edge) {

        edge.computeDisplayShape(getZoomHandler().getCurrentZoomLevel());

        edge.addAllNodes(contentPane, EDGES_Z_OFFSET);
        getModel().registerNewEdge(edge);

        mouseHandler.registerNewEdge(edge);
    }
    
    public void updateNodesWithEdge(FXNode source, FXNode destination, FXEdge edge){
        source.getOutcomingEdges().add(edge);
        destination.getIncomingEdges().add(edge);
    }

    /**
     * @return the model
     */
    public FXGraphModel getModel() {
        return model;
    }
    
    public Pane getContentPane(){
        return this.contentPane;
    }

    /**
     * @return the zoomHandler
     */
    public FXGraphZoomHandler getZoomHandler() {
        return zoomHandler;
    }
    
    
}
