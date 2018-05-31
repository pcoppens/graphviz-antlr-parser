package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotLexer;
import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.antlr.v4.runtime.*;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

import be.pcoppens.graphviz.dot.parser.Parser;

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
     * @requires inputFile is a valid dot file.
     * @effects try to parse the inputFile. Use de GraphvizDot.g4 grammar definition.
     * @return the dot context from the inputFile
     * @throws IOException if the inputFile cannot be read.
     */
    public GraphvizDotParser.GraphContext parse() throws IOException {
        final CharStream charStreams= CharStreams.fromFileName(inputFile.getAbsolutePath());
        // Create the token stream
        CommonTokenStream tokens = new CommonTokenStream(new GraphvizDotLexer(charStreams));
        // Intialise parser
        GraphvizDotParser parser = new GraphvizDotParser(tokens);
        // Set error listener to adoc implementation
        parser.removeErrorListeners();
        parser.addErrorListener(ConsoleErrorListener.INSTANCE);
        // Launch parsing
        GraphvizDotParser.GraphContext tree;

        tree = parser.graph();
        return tree;
    }
}
