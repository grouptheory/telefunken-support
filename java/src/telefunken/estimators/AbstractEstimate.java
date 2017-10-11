/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.estimators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.bootstrap.Baseline;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public abstract class AbstractEstimate {
    
    protected Baseline _calc;
    protected double _trueM;
    
    protected AbstractEstimate() {
    }
    
    public void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex,Edge> g) {
        Double estPop = _calc.getPopulationEstimate(S);
        
        if (estPop != null) 
        {
            double estPop_asDouble = estPop.doubleValue();

            System.out.println("                             True V="+g.getVertexCount());
            System.out.println("                             True S="+S.getVertices().size());
            double trueK = _calc.trueK();
            System.out.println("                             true K="+trueK);
            System.out.println("                             true M="+_trueM);
            double trueL = S.get_TrueL();
            System.out.println("                             true L="+trueL);
            
            _calc.diagnostics(S, hash, g);
            
            System.out.println("                             est V="+estPop_asDouble);
            if ( ! Double.isNaN(estPop_asDouble)) {
                System.out.println("                             error="+g.getVertexCount() / estPop_asDouble);
            }
            else {
                System.out.println("                             error=NaN");
            }
        }
    }

}
