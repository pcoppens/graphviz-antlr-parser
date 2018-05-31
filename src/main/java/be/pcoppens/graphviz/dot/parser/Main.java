package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotLexer;
import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @Overview
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String NAME = "graphviz-parser";
    private static final String HELP = "h";
    private static final String INPUT = "i";

    private File inputFile;

    /**
     * The command line options.
     */
    private Options options;

    /**
     *
     * @param inputFile
     */
    public Main(File inputFile) {
        this.inputFile= inputFile;
    }

    /**
     *
     */
    private Main() {
        // Create command line options
        options = new Options();
        options.addOption(Option.builder(HELP)
                .desc("Prints this help message.")
                .build());

        options.addOption(Option.builder(INPUT)
                .desc("The graphviz dot input file.")
                .hasArg()
                .build());
    }

    /**
     * Prints help message with this options.
     */
    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | %s",
                NAME, HELP, INPUT), options);
    }

    /**
     *
     * @throws IOException
     */
    public void parse() throws IOException {
        LOG.debug("Parsing input ... ");
        GraphvizDotParser.GraphContext tree = parse(CharStreams.fromFileName(inputFile.getAbsolutePath()));
        LOG.debug("Parsing input: done.");
    }

    /**
     *
     * @param charStreams
     * @return
     */
    private GraphvizDotParser.GraphContext parse(CharStream charStreams) {
        // Create the token stream
        CommonTokenStream tokens = new CommonTokenStream(new GraphvizDotLexer(charStreams));
        // Intialise parser
        GraphvizDotParser parser = new GraphvizDotParser(tokens);
        // Set error listener to adoc implementation
        parser.removeErrorListeners();
        parser.addErrorListener(ConsoleErrorListener.INSTANCE);
        // Launch parsing
        GraphvizDotParser.GraphContext tree;
        try {
            tree = parser.graph();
        } catch (RecognitionException e) {
            throw new RuntimeException("Error while retrieving parsing tree!");
        }
        return tree;
    }

    /**
     * Initialise the input compiler using the given input line.
     *
     * @throws Exception If one of the three required arguments is not provided.
     */
    private void initialise(CommandLine line) throws Exception {
        LOG.debug("Initialisation");
        // Check that the arguments are there
        if (!line.hasOption(INPUT)) {
            throw new ParseException(String.format("Option %s is mandatory!", INPUT));
        }
        // Get given files and check they exist
        inputFile = new File(line.getOptionValue(INPUT));
        checkArgument(inputFile.exists() && inputFile.isFile(), "File %s not found!", inputFile.getName());
        LOG.debug("Input file set to {}", inputFile.getPath());
    }

    /**
     *
     * @param args
     */
    public static void main( String[] args )
    {
        Main main = new Main();
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(main.options, args);
        } catch (ParseException ex) {
            LOG.error("Error while parsing command line!", ex);
            main.printHelpMessage();
        }
        // If help is requested, print help message and exit.
        if (line != null) {
            if (line.hasOption(HELP)) {
                main.printHelpMessage();
            } else {
                // Else start parsing
                try {
                    main.initialise(line);
                    main.parse();
                } catch (Exception e) {
                    LOG.error("Exception occured during compilation!", e);
                }
            }
        }
    }
}
