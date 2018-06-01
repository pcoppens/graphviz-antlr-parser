package be.pcoppens.graphviz.dot.parser;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * @Overview: ConsoleErrorListener with a error remainder.
 * @specfield: error. Boolean
 */
public class ErrorListener extends ConsoleErrorListener {

    private boolean error = false;

    /**
     * @modifies: set error to true.
     * @param recognizer
     * @param offendingSymbol
     * @param line
     * @param charPositionInLine
     * @param msg
     * @param e
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        error =true;
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
    }

    public boolean isError() {
        return error;
    }

}
