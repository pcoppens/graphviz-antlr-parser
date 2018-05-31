package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class ParserTest {
    public static final String FILENAME= "/simpleFile.dot";

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
    public void parse() throws URISyntaxException, IOException {
        //setup
        File inputFile = new File(ParserTest.class.getResource(FILENAME).toURI());
        Parser parser= new Parser(inputFile);
        //exercice
        GraphvizDotParser.GraphContext graphContext= parser.parse();
        //verify
        assertNotNull(graphContext);
        //teardown
    }
}