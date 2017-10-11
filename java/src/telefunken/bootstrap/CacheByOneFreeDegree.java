/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import java.util.HashMap;

/**
 *
 * @author Bilal Khan
 */
public class CacheByOneFreeDegree {
    private HashMap<Integer, Double> 
            _cache1 = new HashMap<Integer, Double>();

    public void clear() {
        _cache1.clear();
    }
    
    public Double get(int fdv) {
        if (_cache1.containsKey(fdv)) {
            return _cache1.get(fdv);
        }
        else {
            return null;
        }
    }

    public void save(int fdv, double val) {
        _cache1.put(fdv, val);
    }
}
