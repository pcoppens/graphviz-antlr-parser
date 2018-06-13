package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ParserTest {
    public static final String FILENAME= "/simpleFile.dot";
    public static final String REAL_FILENAME= "/project-graph.png.dot";
    public static final String FAIL= "/failFile.dot";

    @Test
    public void repOk() throws URISyntaxException {
        //setup
        File inputFile = new File(ParserTest.class.getResource(FILENAME).toURI());
        Parser parser= new Parser(inputFile);
        //exercice
        boolean result= parser.repOk();
        //verify
        assertTrue(result);
        //teardown
    }

    @Test(expected = FailureException.class)
    public void repOkFail() throws URISyntaxException {
        //setup
        File inputFile = new File(ParserTest.class.getResource(".").toURI());
        //exercice
        Parser parser= new Parser(inputFile);
        //verify
            // a FailureException must be throwed.
        //teardown
    }

    @Test
    public void parse() throws URISyntaxException, IOException, ParsingException {
        //setup
        File inputFile = new File(ParserTest.class.getResource(FILENAME).toURI());
        Parser parser= new Parser(inputFile);
        //exercice
        GraphvizDotParser.GraphContext graphContext= parser.parse();
        //verify
        assertNotNull(graphContext);
        //teardown
    }

    @Test
    public void parseReal() throws URISyntaxException, IOException, ParsingException {
        //setup
        File inputFile = new File(ParserTest.class.getResource(REAL_FILENAME).toURI());
        Parser parser= new Parser(inputFile);
        //exercice
        GraphvizDotParser.GraphContext graphContext= parser.parse();
        //verify
        assertNotNull(graphContext);
        System.out.println("Digraph: "+graphContext.DIGRAPH());
        System.out.println("Grapth: "+graphContext.GRAPH());
        System.out.println("stmt(0): "+graphContext.stmt_list().stmt(1).getText());
        System.out.println("stmt(0): "+graphContext.stmt_list().stmt(1).attr_stmt().NODE());
        //teardown
    }

    @Test(expected = ParsingException.class)
    public void parseFail() throws URISyntaxException, IOException, ParsingException {
        //setup
        File inputFile = new File(ParserTest.class.getResource(FAIL).toURI());
        Parser parser= new Parser(inputFile);
        //exercice
        GraphvizDotParser.GraphContext graphContext= parser.parse();
        //verify
        assertNotNull(graphContext);
        //teardown
    }

    @Test()
    public void walkTest() throws IOException {
        //setup
        Files.walk(FileSystems.getDefault().getPath("."), 1).filter(path -> Files.isDirectory(path) ).forEach(path -> {
            System.out.println(path.toString());
        });
        //teardown

    }
}