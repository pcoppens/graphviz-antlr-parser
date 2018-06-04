package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

public class GraphvizDotTest {
    public static final String FILENAME= "/simpleFile.dot";
    public static final String REAL_FILENAME= "/project-graph.png.dot";
    private static GraphvizDotParser.GraphContext context;

    @BeforeClass
    public static void setup() throws URISyntaxException, IOException, ParsingException {
        File inputFile = new File(ParserTest.class.getResource(FILENAME).toURI());
        context= new Parser(inputFile).parse();
    }

    @Test
    public void getEdgesTest() {
        //setup
        GraphvizDot graphvizDot= new GraphvizDot(context);
        //exercice
        List<GraphvizDotParser.Edge_stmtContext> list=  graphvizDot.getEdges();
        //verify
        assertNotNull(list);
        assertTrue(list.size()>0);
        //teardown
    }

    @Test
    public void addEdgeTest() {
        //setup
        GraphvizDot graphvizDot= new GraphvizDot(context);
        int size= graphvizDot.getEdges().size();
        //exercice
        graphvizDot.addEdge(context.stmt_list().stmt(8).edge_stmt());
        //verify
        assertEquals(size, graphvizDot.getEdges().size());
        //teardown
    }

    @Test
    public void addContextTest() {
        //setup
        GraphvizDot graphvizDot= new GraphvizDot(context);
        int size= graphvizDot.getEdges().size();
        //exercice
        graphvizDot.addContext(context);
        //verify
        assertEquals(size, graphvizDot.getEdges().size());
        //teardown
    }

    @Test
    public void cloneTest() {
        //setup
        GraphvizDot graphvizDot= new GraphvizDot(context);
        //exercice
        GraphvizDot graphvizDotClone=graphvizDot.clone();
        //verify
        assertEquals(graphvizDot.toString(), graphvizDotClone.toString());
        //teardown
    }

    @Test
    public void edgeEqualsTest() {
        assertTrue(GraphvizDot.edgeEquals(context.stmt_list().stmt().get(8).edge_stmt(), context.stmt_list().stmt().get(8).edge_stmt()));
        assertFalse(GraphvizDot.edgeEquals(context.stmt_list().stmt().get(8).edge_stmt(), context.stmt_list().stmt().get(9).edge_stmt()));
    }

    @Test
    public void toStringTest() {
        //setup
        GraphvizDot graphvizDot= new GraphvizDot(context);
        //exercice

        //verify
        assertNotNull(graphvizDot.toString());
        System.out.println(graphvizDot.toString());
        //teardown
    }


    @Test
    public void toString2Test() {
        //setup
        GraphvizDot graphvizDot= new GraphvizDot(context);
        //exercice
        graphvizDot.addContext(context);
        //verify
        assertNotNull(graphvizDot.toString());
        System.out.println(graphvizDot.toString());
        //teardown
    }
}