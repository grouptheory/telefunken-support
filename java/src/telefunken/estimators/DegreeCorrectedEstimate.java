/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.estimators;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import telefunken.bootstrap.AbstractMatchingEstimator;
import telefunken.bootstrap.DegreeCorrectedEstimator;
import telefunken.bootstrap.RiddersMethod;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.FixedSampler;

/**
 *
 * @author Bilal Khan
 */
public class DegreeCorrectedEstimate implements IPopulationEstimator  {

    private AbstractMatchingEstimator _calc;
    
    @Override
    public double computeEstimate(AbstractSample S, IHasher hash) 
    {
        if (S instanceof FixedSampler.FixedSample) 
        {
            _calc = null;
            return 0.0;
        }
        else {
            double answer;
            try {
                _calc = new DegreeCorrectedEstimator(S, hash);
                answer = RiddersMethod.root(0.5, 2, 9000000, _calc);
            }
            catch (Exception ex) {
                System.out.print("Exception");
                Thread.dumpStack();
                answer = Double.NaN;
            }
            return answer;
        }
    }

    
    @Override
    public void diagnostics(AbstractSample S, IHasher hash, DirectedSparseGraph<Vertex,Edge> g) 
    {
    }

    @Override
    public double getK() {
        if (_calc == null) 
        {
            return 0.0;
        }
        return _calc.trueK();
    }

    @Override
    public double getM() {
        if (_calc == null) 
        {
            return 0.0;
        }
        return _calc.actualMatches();
    }

    @Override
    public double get2F() {
        if (_calc == null) 
        {
            return 0.0;
        }
        return _calc.true2F();
    }
    
    @Override
    public String getHumanReadableName() {
        return "DegreeCorrectedEstimate";
    }
}
