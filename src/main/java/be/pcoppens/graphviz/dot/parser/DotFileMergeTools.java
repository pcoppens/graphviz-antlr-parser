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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private  final static String getDateTime()
    {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
        return df.format(new Date());
    }

    /**
     * @effects: merge all dot files in a large dot file named: mergeAll_timestamp.dot
     * @throws IOException: cannot write the file
     */
    public void mergeAll() throws IOException {
        // add all from the first
        GraphvizDot graphvizDot= new GraphvizDot(contextList.get(0));
        for(int i=1;i<contextList.size();i++){
            graphvizDot.addContext(contextList.get(i));
        }
        write("mergeAll_"+ getDateTime()+".dot", graphvizDot.toString());
    }

    /**
     * @effects: merge all dot files in a large dot file named: mergeAllWithPrefix_timestamp.dot.
     *  && Only nodes with start with source will be included
     *  && Only nodes with start with target as target are included
     * @throws IOException: cannot write the file
     */
    public void mergeAllWithPrefix(String source, String target) throws IOException {
        // add all from the first
        GraphvizDot graphvizDot= new GraphvizDot(contextList.get(0), source, target);
        for(int i=1;i<contextList.size();i++){
            graphvizDot.addContext(contextList.get(i), source, target);
        }
        write("mergeAllWithPrefix_"+ getDateTime()+".dot", graphvizDot.toString());
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
