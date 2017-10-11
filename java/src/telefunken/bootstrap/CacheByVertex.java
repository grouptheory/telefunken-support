/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import java.util.HashMap;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public class CacheByVertex<T> {
    private HashMap<Vertex, T> 
            _cache1 = new HashMap<Vertex, T>();

    public void clear() {
        _cache1.clear();
    }
    
    public T get(Vertex v) {
        if (_cache1.containsKey(v)) {
            return _cache1.get(v);
        }
        else {
            return null;
        }
    }

    public void save(Vertex v, T val) {
        _cache1.put(v, val);
    }
}
