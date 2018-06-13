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
    public static final String FILENAME_COMPLEX= "/project-graph.png.dot";
    private List<File> files;

    @Before
    public void setUp() throws URISyntaxException {
        files = new ArrayList<>(2);
        files.add(new File(ParserTest.class.getResource(FILENAME).toURI()));
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
    public void mergeAllComplex() throws IOException, URISyntaxException {
        files = new ArrayList<>(2);
        files.add(new File(ParserTest.class.getResource(FILENAME_COMPLEX).toURI()));
        files.add(new File(ParserTest.class.getResource(FILENAME_COMPLEX).toURI()));
        //setup
        DotFileMergeTools dotFileMergeTools= new DotFileMergeTools(files);
        //exercice
        dotFileMergeTools.mergeAll();
        //verify
        //teardown
    }

    @Test(expected = FailureException.class)
    public void repOk() {
        DotFileMergeTools dotFileMergeTools= new DotFileMergeTools(new ArrayList<>());
    }

    @Test(expected = FailureException.class)
    public void repOk1() throws URISyntaxException {
        List<File> myList= new ArrayList<>();
        myList.add(new File(ParserTest.class.getResource("/log4j2-test.xml").toURI()));
        DotFileMergeTools dotFileMergeTools= new DotFileMergeTools(myList);
    }

    @Test()
    public void repOk2() {
        DotFileMergeTools dotFileMergeTools= new DotFileMergeTools(files);
        assertTrue(dotFileMergeTools.repOk());
    }


    @Test
    public void mergeAllWithPrefix() throws IOException {
        //setup
        DotFileMergeTools dotFileMergeTools= new DotFileMergeTools(files);
        //exercice
        dotFileMergeTools.mergeAllWithPrefix("main(.*)", null);
        //verify
        //teardown
    }

}