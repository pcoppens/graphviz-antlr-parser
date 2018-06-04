package be.pcoppens.graphviz.dot.parser;

import be.pcoppens.graphviz.dot.GraphvizDotParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @overview: Merge dot files into a big one. Merge do not duplicate edge.
 * immutable class.
 * @specfield ContextList. A list of dot context.
 * @Invariant: ContextList.size() >0 .
 */
public class DotFileMergeTools {
    private static final Logger LOG = LoggerFactory.getLogger(DotFileMergeTools.class);

    private final List<GraphvizDotParser.GraphContext> contextList= new ArrayList<>();

    /**
     * @requires: fileList is not null and contains dot files
     * @effects: parse all files
     * @modifies: fill the contextList
     */
    public DotFileMergeTools(List<File> fileList){
        fileList.forEach(file-> {
            try {
                contextList.add(new Parser(file).parse());
            } catch (IOException | ParsingException e) {
                LOG.error("DotFileTools.parse("+file.getName()+"):  "+e.getMessage()); // do nothing by spec
            }
        });

        if(! repOk()){
            throw new FailureException();
        }
    }


    /**
     * @effects: merge all dot files in a large dot file named: mergeAll_timestamp.dot
     * @throws IOException: cannot write the file
     */
    public void mergeAll() {
        // add all from the first
        GraphvizDot graphvizDot= new GraphvizDot(contextList.get(0));
        for(int i=1;i<contextList.size();i++){
            graphvizDot.addContext(contextList.get(i));
        }

    }

    /**
     * @effects: merge all dot files in a large dot file named: mergeAllWithPrefix_timestamp.dot.
     * Only nodes with nodePrefix will be included.
     * @throws IOException: cannot write the file
     */
    public void mergeAllWithPrefix(String nodePrefix) {

    }

    /**
     * @effects: merge all dot files in a large dot file named: mergeAllWithTarget_timestamp.dot
     * Only nodes with nodeTarget as target are included and the nodeTarget.
     * @throws IOException: cannot write the file
     */
    public void mergeAllWithTarget(String nodeTarget) {

    }

    /**
     * @effects: merge all dot files in a large dot file named: mergeAllWithSource_timestamp.dot
     * only nodeSource and his targets are included.
     * @throws IOException: cannot write the file
     */
    public void mergeAllWithSource(String nodeSource) {

    }

    //IR:
    // contextList !=null && contextList.foreach(o-> o is a valid GraphContext)

    public boolean repOk() {
        return contextList!=null && contextList.size()>0;
    }

    private void write(String fileName, String data) throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(data);
        }
    }
}
