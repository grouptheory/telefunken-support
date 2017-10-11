/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.experimenters;

/**
 *
 * @author Bilal Khan
 */
    
public class Results {
    private double _mean;
    private double _std;
    private double _fail;
    
    Results(double mean, double std, double fail) {
        _mean = mean;
        _std = std;
        _fail = fail;
    }
    
    public String toString() {
        String s = "";
        s += "mean=" + _mean + "; ";
        s += "std=" + _std + "; ";
        s += "fail=" + _fail + "; ";
        return s;
    }
};
