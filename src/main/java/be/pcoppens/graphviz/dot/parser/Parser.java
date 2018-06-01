package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotLexer;
import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.antlr.v4.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * @Overview: Parser for a dot file.
 * Immutable classe.
 * @specfield inputFile; a dot file to parse.
 *
 * IR: inputFile is a readable file.
 */
public class Parser {
    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    private final File inputFile;
    private final ErrorListener errorListener= new ErrorListener();


    /**
     * Create a Parser for the inputFile parameter
     * @requires file is a readable file.
     * @modifies set the inputFile with file parameter.
     */
    public Parser(File file) {
        this.inputFile= file;
        if(!repOk())
            throw new FailureException();
    }

    public boolean repOk(){
        if(inputFile==null)
            return false;
        return inputFile.isFile();
    }



    /**
     * @requires inputFile is not null.
     * @effects try to parse the inputFile. Use de GraphvizDot.g4 grammar definition.
     * @return the dot context from the inputFile
     * @throws IOException if the inputFile cannot be read.
     * @throws ParsingException if the file is not a dot file.
     */
    public GraphvizDotParser.GraphContext parse() throws IOException, ParsingException {
        final CharStream charStreams= CharStreams.fromFileName(inputFile.getAbsolutePath());
        // Create the token stream
        CommonTokenStream tokens = new CommonTokenStream(new GraphvizDotLexer(charStreams));
        // Intialise parser
        GraphvizDotParser parser = new GraphvizDotParser(tokens);
        // Set error listener to adoc implementation
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        // Launch parsing
        GraphvizDotParser.GraphContext tree = parser.graph();
        if(errorListener.isError())
            throw new ParsingException("Error in parsing "+ inputFile.getName());
        return tree;
    }
}
