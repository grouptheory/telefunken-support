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
public class CacheByTwoFreeDegrees {
    private HashMap<Integer, HashMap<Integer, Double>> 
        _cache2 = new HashMap<Integer, HashMap<Integer, Double>>();

    public void clear() {
        _cache2.clear();
    }
    
    public Double get(int fdu, int fdv) {
        HashMap<Integer, Double> submap  = null;
        if (_cache2.containsKey(fdu)) {
            submap = _cache2.get(fdu);
        }
        else {
            submap = new HashMap<Integer, Double>();
            _cache2.put(fdu, submap);
        }
        if (submap.containsKey(fdv)) {
            return submap.get(fdv);
        }
        else {
            return null;
        }
    }

    public void save(int fdu, int fdv, Double val) {
        HashMap<Integer, Double> submap = null;
        if (_cache2.containsKey(fdu)) {
            submap = _cache2.get(fdu);
        }
        else {
            submap = new HashMap<Integer, Double>();
            _cache2.put(fdu, submap);
        }
        submap.put(fdv, val);
    }
}