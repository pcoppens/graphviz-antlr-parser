package be.pcoppens.graphviz.dot.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DotFileMergeToolsTest {
    public static final String FILENAME= "/simpleFile.dot";
    private List<File> files;

    @Before
    public void setUp() throws URISyntaxException {
        files = new ArrayList<>(2);
        files.add(new File(ParserTest.class.getResource(FILENAME).toURI()));
    }
    @Test
    public void mergeAll() throws IOException {
        //setup
        DotFileMergeTools dotFileMergeTools= new DotFileMergeTools(files);
        //exercice
        dotFileMergeTools.mergeAll();
        //verify
        //teardown
    }

    @Test
    public void mergeAllWithPrefix() {
    }

    @Test
    public void mergeAllWithTarget() {
    }

    @Test
    public void mergeAllWithSource() {
    }

    @Test
    public void repOk() {
    }
}