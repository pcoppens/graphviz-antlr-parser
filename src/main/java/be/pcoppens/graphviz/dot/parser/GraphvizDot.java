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
        StringBuffer sb= new StringBuffer(graphContext.getText());
        int indice= sb.lastIndexOf("}");
        sb.delete(indice, sb.length());
        data= sb.toString();
        addEdges(graphContext.stmt_list().stmt());
    }

    /**
     * @requires: stmtContexts is not null
     * @modifies: add all egdes to edges.
     */
    private void addEdges(List<GraphvizDotParser.StmtContext> stmtContexts){
        stmtContexts.forEach(stmtContext -> {
            if(stmtContext.edge_stmt()!=null ){
                String tmp= stmtContext.edge_stmt().getText();
                data=data.replaceFirst(tmp+";", "");
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
        addEdges(graphContext.stmt_list().stmt());
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
