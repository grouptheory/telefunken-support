/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.generators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.HashMap;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public class ErdosRenyiGenerator implements IGraphGenerator {

    private int _n;
    private double _p;
    
    public ErdosRenyiGenerator(int n, double p) {
        _n=n; _p=p;
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
        
        for (int x=0;x<_n;x++) {
            for (int y=0;y<_n;y++) {
                if (x>=y) continue;
                double toss = Randomness.getDouble();
                if (toss <= _p) {
                    Vertex v1=id2vertex.get(x);
                    Vertex v2=id2vertex.get(y);
                    g.addEdge(new Edge(), v1, v2);
                    g.addEdge(new Edge(), v2, v1);
                }
            }
        }   
        return g;    
    } 

    @Override
    public String getHumanReadableName() {
        return "ErdosRenyi";
    }
}

