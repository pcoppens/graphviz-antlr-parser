package be.pcoppens.graphviz.dot.parser;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @Overview main entry to verify a dot file.
 * @specfield inputFile. the file to verify.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String NAME = "graphviz-parser";
    private static final String HELP = "h";
    private static final String INPUT = "i";

    /**
     * The command line options.
     */
    private final Options options;

    private static File inputFile;

    /**
     * @modifies: the options
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
     * @effects: Prints help message with this options.
     */
    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | %s",
                NAME, HELP, INPUT), options);
    }

    /**
     * @requires a not null Line.
     * @modifies: set the inputFile with the line input argument.
     * @effects: parse the given input line.
     * @throws Exception If one of the required arguments is not provided.
     */
    private void initialise(CommandLine line) throws Exception {
        LOG.debug("Initialisation");
        // Check that the arguments
        if (!line.hasOption(INPUT)) {
            throw new ParseException(String.format("Option %s is mandatory!", INPUT));
        }
        // Get given files and check they exist
        inputFile = new File(line.getOptionValue(INPUT));
        checkArgument(inputFile.exists() && inputFile.isFile(), "File %s not found!", inputFile.getName());
        LOG.debug("Input file set to {}", inputFile.getPath());
    }

    /**
     * @effects Verify is the input file is a valid dot file according graphviz grammar.
     *
     */
    public static void main( String[] args )
    {
        Main main = new Main();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine line = null;
        try {
            line = commandLineParser.parse(main.options, args);
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
                    new Parser(inputFile).parse();
                } catch (IOException e) {
                    LOG.error("Cannot read the file: ", e);
                } catch (ParsingException e) {
                    LOG.error(e.getLocalizedMessage());
                }
            }
        }
    }
}
