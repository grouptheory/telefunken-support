/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.generators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public class BarabasiAlbertGenerator implements IGraphGenerator {

    private int _n;
    double _m;
    private double _a;
    
    public BarabasiAlbertGenerator(int n, double m, double a) {
        _n=n; _m=m; _a=a;
    }
    
    @Override
    public DirectedSparseGraph<Vertex,Edge> generateGraph() {
        
	DirectedSparseGraph<Vertex,Edge> g = new DirectedSparseGraph<Vertex,Edge>();
        
        HashMap<Integer, Vertex> id2vertex = new HashMap<Integer,Vertex>();
        
        for (int i=0; i<_n; i++) {
            Vertex v = new Vertex();
            v.setUserDatum(Vertex.LABEL, ""+i);
            g.addVertex(v);
            id2vertex.put(i,v);
        }
        
        for (Integer x=0;x<_n;x++) {
            if (x<_m) continue;
            else {
                Vertex v1=id2vertex.get(x);
                Set<Vertex> vset = new HashSet<Vertex>();
                
                int rounded_m = (Randomness.getDouble() <= _m-Math.floor(_m)) ?
                        (int)Math.floor(_m) : (int)Math.ceil(_m);
                
                while (vset.size() < rounded_m) {
                    int index=Randomness.getInteger()%x;
                    Vertex v2=id2vertex.get(index);
                    if ( ! vset.contains(v2)) {
                        vset.add(v2);
                        g.addEdge(new Edge(), v1, v2);
                        g.addEdge(new Edge(), v2, v1);
                    }
                }
            }
        }
        return g;  
    } 

    @Override
    public String getHumanReadableName() {
        return "BarabasiAlbert";
    }
}