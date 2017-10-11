/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.generators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.io.PajekNetReader;
import java.util.HashMap;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public class ConfigurationGenerator implements IGraphGenerator {

    private int _n, _d[];
    static final int MAX_FAILURES = 100;
    private String _name;
    
    public ConfigurationGenerator(int[] d, String name) {
        _n = d.length;
        _d = new int [_n];
        for (int i=0; i<_n; i++) _d[i]=d[i];
        
        _name = name;
    }
    
    @Override
    public DirectedSparseGraph<Vertex,Edge> generateGraph() {
        
	DirectedSparseGraph<Vertex,Edge> g = new DirectedSparseGraph<Vertex,Edge>();
        
        int [] residual = new int [_n];
        double [] residualD = new double [_n];
        double [] residualD2 = new double [_n];
        HashMap<Integer, Vertex> id2vertex = new HashMap<Integer,Vertex>();
        
        for (int i=0; i<_n; i++) {
            residual[i]=_d[i];
            residualD[i]=residual[i];
            Vertex v = new Vertex();
            v.setUserDatum(Vertex.LABEL, ""+i);
            g.addVertex(v);
            id2vertex.put(i,v);
        }
        
        boolean done=false;
        int failure_run = 0;
        do {
            int sum1=0;
            for (int i=0; i<_n; i++) {
                residualD[i]=residual[i];
                sum1+=residual[i];
            }
            if (sum1>=2) {
                double [] residualAccum = Randomness.convertToCumulativeDistribution(residualD);
                int index1 = Randomness.getSample(residualAccum);
                residual[index1]-=1;
                
                for (int i=0; i<_n; i++) residualD2[i]=residual[i];
                double [] residualAccum2 = Randomness.convertToCumulativeDistribution(residualD2);
                int index2 = Randomness.getSample(residualAccum2);
                residual[index2]-=1;
                
                // if it is not a loop
                if (index1!=index2) {
                    Vertex v1=id2vertex.get(index1);
                    Vertex v2=id2vertex.get(index2);
                    // if it is not a parallel edge
                    if ( ! g.getSuccessors(v1).contains(v2)) {
                        g.addEdge(new Edge(), v1, v2);
                        g.addEdge(new Edge(), v2, v1);
                        failure_run = 0;
                    }
                    else {
                        // put the ends back in the pool
                        residual[index1]+=1;
                        residual[index2]+=1;
                        failure_run += 1;
                    }
                }
                else {
                    // put the ends back in the pool
                    residual[index1]+=1;
                    residual[index2]+=1;
                    failure_run += 1;
                }
            }
            else {
                done=true;
            }
            
            if (failure_run > MAX_FAILURES) {
                done = true;
            }
        }
        while ( ! done);
        return g;
    }

    @Override
    public String getHumanReadableName() {
        return "Config["+_name+"]";
    }
}

