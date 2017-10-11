/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.generators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.PajekNetReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections15.Factory;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.settings.Params;
import telefunken.utils.Log;

/**
 *
 * @author Bilal Khan
 */
public class PajekFileGraphGenerator implements IGraphGenerator {
    
    private final String _fname;
    private final Factory<Vertex> _fv;
    private final Factory<Edge> _fe;
    
    public PajekFileGraphGenerator(String fname, Factory<Vertex> fv, Factory<Edge> fe) {
        _fname = fname;
        _fv = fv;
        _fe = fe;
    }
    
    public DirectedSparseGraph<Vertex,Edge> generateGraph() 
    {
	Graph<Vertex,Edge> g = new DirectedSparseGraph<Vertex,Edge>();
        
	int v,e;
        v = e = 0;

	try {
	    PajekNetReader pnr = new PajekNetReader(_fv, _fe);

	    Log.diag(PajekFileGraphGenerator.class.getName(), Log.DEBUG, "Begin reading: "+_fname);
	    g = pnr.load(_fname, g);
	    Log.diag(PajekFileGraphGenerator.class.getName(), Log.DEBUG, "Done reading graph: "+_fname);

	    v = g.getVertexCount();
	    e = g.getEdgeCount();

	    if (Params.SYMMETRIC) {
		List<Vertex> src = new ArrayList<Vertex>();
		List<Vertex> dst = new ArrayList<Vertex>();

		for (Edge e2 : g.getEdges()) {
		    Vertex u1 = g.getSource(e2);
		    Vertex v1 = g.getDest(e2);
		    if (! g.isPredecessor(v1,u1)) {
                        src.add(u1);
                        dst.add(v1);
		    }
		}

		for (Iterator<Vertex> it1=src.iterator(), it2=dst.iterator(); 
                        it1.hasNext()&&it2.hasNext();) 
                {
                    Vertex u1=it1.next();
                    Vertex v1=it2.next();
                    g.addEdge(new Edge(),u1,v1);
                    g.addEdge(new Edge(),v1,u1);
		}
	    }
	}
	catch (IOException ex) {
	    Log.diag(PajekFileGraphGenerator.class.getName(), Log.FATAL, "Could not initialize, Exception: "+ex);
	}

        Log.diag(PajekFileGraphGenerator.class.getName(), Log.DEBUG, "GRAPH READ: \n"+g.toString());
        
        Log.diag(PajekFileGraphGenerator.class.getName(), Log.INFO, "converting to directed...");
        
	DirectedSparseGraph<Vertex,Edge> directed_g = new DirectedSparseGraph<Vertex,Edge>();
        
        HashMap<Vertex,Vertex> g2a = new HashMap<Vertex,Vertex>();
        HashMap<Vertex,Vertex> a2g = new HashMap<Vertex,Vertex>();
        
        for (Vertex vg : g.getVertices()) {
            Vertex va = new Vertex();
	    va.setUserDatum(Vertex.LABEL, vg.getUserDatum(Vertex.LABEL));
            directed_g.addVertex(va);
            g2a.put(vg, va);
            a2g.put(va, vg);
        }
        
        for (Vertex vg : g.getVertices()) {
            Vertex ag = g2a.get(vg);
            
            Collection<Vertex> vgN = g.getSuccessors(vg);
            for (Vertex vg_prime : vgN) {
                Vertex ag_prime = g2a.get(vg_prime);
                directed_g.addEdge(new Edge(), ag, ag_prime);
            }
        }
        Log.diag(PajekFileGraphGenerator.class.getName(), Log.DEBUG, "CONVERTED GRAPH: \n"+directed_g.toString());
        
        return directed_g;
    }

    @Override
    public String getHumanReadableName() {
        return "Pajek";
    }
}

