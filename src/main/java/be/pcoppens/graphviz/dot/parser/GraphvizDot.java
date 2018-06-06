package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Overview: Abstraction of simple semantic of graphviz dot file.
 * mutable class (because it's use a collection).
 * @specfield: edges; set of edge
 * @specfield: data; all elements exept the edges
 * @invariant data is not empty && edge is a set (no duplicate)
 */
public class GraphvizDot {
    private String data;
    //it's a set but Edge_stmtContext dot not implement equals
    private List<GraphvizDotParser.Edge_stmtContext> edges= new ArrayList<>();

    /**
     * internal use only (clone)
     * @requires: s. GraphContext String representation.
     * @modifies: data. Copy s to data
     * @param s
     */
    private GraphvizDot(String s){
        data=s;
    }

    /**
     * @requires: graphContext is not null
     * @modifies: fill data with graphContext without the edges
     * @modifies: add all egdes to edges.
     */
    public GraphvizDot(GraphvizDotParser.GraphContext graphContext){
        this(graphContext, null, null);
    }

    /**
     * @requires: graphContext is not null
     * @modifies: fill data with graphContext without the edges
     * @modifies: add all egdes to edges && If source is not null only edge with matched(regex) with source is added
     *                                  && If target is not null only edge with matched(regex) with target is added
     */
    public GraphvizDot(GraphvizDotParser.GraphContext graphContext, String source, String target){
        StringBuffer sb= new StringBuffer(graphContext.getText());
        int indice= sb.lastIndexOf("}");
        sb.delete(indice, sb.length());
        data= sb.toString();
        addEdges(graphContext.stmt_list().stmt(), source, target);
    }

    /**
     * @requires: stmtContexts is not null
     * @modifies: add all egdes to edges.
     */
    private void addEdges(List<GraphvizDotParser.StmtContext> stmtContexts){
        addEdges(stmtContexts, null, null);
    }
    /**
     * @requires: stmtContexts is not null
     * @modifies: add all egdes to edges && If source is not null only edge with matched(regex) with source is added
     *                                  && If target is not null only edge with matched(regex) with target is added
     */
    private void addEdges(List<GraphvizDotParser.StmtContext> stmtContexts, String source, String target){
        stmtContexts.forEach(stmtContext -> {
            if(stmtContext.edge_stmt()!=null ){
                String tmp= stmtContext.edge_stmt().getText();
                data=data.replaceFirst(tmp+";", "");
                //restrict source ?
                if(source!=null && target==null)
                {
                    if( stmtContext.edge_stmt().node_id().id().getText().matches(source)){
                        addEdge(stmtContext.edge_stmt());
                    }
                }
                else
                if(source==null && target!=null)
                {
                    if( stmtContext.edge_stmt().edgeRHS().node_id().id().getText().matches(target)){
                        addEdge(stmtContext.edge_stmt());
                    }
                }
                else
                if(source!=null && target!=null)
                {
                    if( stmtContext.edge_stmt().node_id().id().getText().matches(source) &&
                            stmtContext.edge_stmt().edgeRHS().node_id().id().getText().matches(target)){
                        addEdge(stmtContext.edge_stmt());
                    }
                }
                else
                    addEdge(stmtContext.edge_stmt());
            }
        });
    }


    public List<GraphvizDotParser.Edge_stmtContext> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    /**
     * @requires: edge is not null && edge is a valid edge sentence
     * @modifies: add edge to edges
     */
    public void addEdge(GraphvizDotParser.Edge_stmtContext edge){
        for(GraphvizDotParser.Edge_stmtContext tmp: edges){
            if(edgeEquals(tmp, edge)){
                return;
            }
        }
        edges.add(edge);
    }

    /**
     *
     * @requires graphContext !=null
     * @modifies: data, add all graphContext content except edges
     * @modifies: edges: all edges from graphContext if not already exist.
     */
    public void addContext(GraphvizDotParser.GraphContext graphContext){
        addContext(graphContext, null, null);
    }

    /**
     *
     * @requires graphContext !=null
     * @modifies: data, add all graphContext content except edges
     * @modifies: edges: all edges from graphContext if not already exist
     *                  && If source is not null only edge with matched(regex) with source is added
     *                  && If target is not null only edge with matched(regex) with target is added
     */
    public void addContext(GraphvizDotParser.GraphContext graphContext, String source, String target){
        StringBuffer sb= new StringBuffer(graphContext.getText());
        //remove graph header
        int indice= sb.indexOf("{");
        sb.delete(0, indice+1);
        sb.insert(0, "\n");
        //remove graph footer
        indice= sb.lastIndexOf("}");
        sb.delete(indice, sb.length());
        //insert existing data at first place
        sb.insert(0, data);
        data= sb.toString();
        //add edges
        addEdges(graphContext.stmt_list().stmt(), source, target);
    }

    public GraphvizDot clone() {
        GraphvizDot result= new GraphvizDot(data);
        result.edges.addAll(edges); //do not use addEdge() to avoid not needed check for duplicate.
        return result;
    }

    /**
     *
     * @requires a !=null
     * @requires b !=null
     * @return true if a.source = b.source && a.destination= b.destination
     */
    public static boolean edgeEquals(GraphvizDotParser.Edge_stmtContext a, GraphvizDotParser.Edge_stmtContext b){
        return a.node_id().id().getText().equals(b.node_id().id().getText()) &&
                a.edgeRHS().node_id().id().getText().equals(b.edgeRHS().node_id().id().getText());
    }

    @Override
    public String toString() {
        StringBuffer sb= new StringBuffer();
        sb.append(data);
        edges.forEach(edge_stmtContext ->{
            sb.append("\n");
            sb.append(edge_stmtContext.getText());
            sb.append(";");
        } );
        sb.append("\n}");

        return sb.toString();
    }
}
