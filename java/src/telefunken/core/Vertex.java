/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.core;

import edu.uci.ics.jung.io.PajekNetReader;
import java.util.HashMap;

/**
 *
 * @author Bilal Khan
 */
public class Vertex {
    public final static String LABEL = "VertexLabel";
    private final HashMap<String,Object> _map = new HashMap<String,Object>();
    
    public void setUserDatum(String key, Object value) {
        _map.put(key,value);
    }
    public Object getUserDatum(String key) {
        return _map.get(key);
    }
}
