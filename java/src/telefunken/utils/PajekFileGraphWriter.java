/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.utils;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.io.PajekNetWriter;
import java.io.IOException;
import org.apache.commons.collections15.Transformer;
import telefunken.core.Edge;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public class PajekFileGraphWriter {
    
    public static boolean save(DirectedSparseGraph<Vertex,Edge> g, Transformer sl, String fname) 
    {
        boolean result = false;
        if (fname != null) {
            try {
                PajekNetWriter pnw = new PajekNetWriter();

                pnw.save(g,fname,sl,null);
                Log.diag(PajekFileGraphWriter.class.getName(), Log.INFO, "Output: "+fname);
                result = true;
            }
            catch (IOException ex) {
                Log.diag(PajekFileGraphWriter.class.getName(), Log.ERROR, "Exception: "+ex);
                result = true;
            }
       }
       else {
           result = false;
       }
        return result;
    }
}
