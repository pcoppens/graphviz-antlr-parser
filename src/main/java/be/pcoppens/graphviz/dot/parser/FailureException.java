package be.pcoppens.graphviz.dot.parser;

public class FailureException extends RuntimeException {

    public FailureException(){
        super();
    }

    public FailureException(String message){
        super(message);
    }
}
