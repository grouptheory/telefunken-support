/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.estimators;

import telefunken.bootstrap.BaselineWithRDSCorrection;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public class BaselineWithRDSCorrectionEstimate extends AbstractEstimate implements IPopulationEstimator  {
    
    @Override
    public double computeEstimate(AbstractSample S, IHasher hash) 
    {
        _calc = new BaselineWithRDSCorrection(S, hash);
        _trueM = _calc.actualMatches();
        
        Double answer = _calc.getPopulationEstimate(S);
        if (answer == null) {
            return Double.NaN;
        }
        else {
            return answer;
        }
    }

    @Override
    public double getK() {
        return _calc.trueK();
    }

    @Override
    public double getM() {
        return _trueM;
    }

    @Override
    public double get2F() {
        return _calc.true2F();
    }

    @Override
    public String getHumanReadableName() {
        return "RDS Correction";
    }
}
