/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.hashers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import telefunken.core.Vertex;
import telefunken.utils.Assertion;

/**
 *
 * @author Bilal Khan
 */

public final class LosslessHasher extends AbstractHasher {

    private final HashMap<Vertex, Integer> _vertex2code = new HashMap<Vertex, Integer>();
    private final HashMap<Integer,Vertex> _code2vertex = new HashMap<Integer,Vertex>();
       
    public LosslessHasher() {
    }
    
    public int getHashspaceSize() {
        throw new RuntimeException("Called getHashspaceSize() on LosslessHasher");
    }
    
    private boolean _initialized = false;
    @Override
    public void initialize(Collection<Vertex> vset) {
        _vertex2code.clear();
        _code2vertex.clear();
        int code=1;
        for (Vertex v : vset) {
            _vertex2code.put(v,code);
            _code2vertex.put(code,v);
            code++;
        }
        _initialized = true;
    }
    
    @Override
    public Integer getCode(Vertex v) {
        Assertion.test(_initialized, "Use of uninitialized LosslessHasher");
        return _vertex2code.get(v);
    }
    
    @Override
    public Collection<Vertex> getVerticesWithCode(int code) {
        Assertion.test(_initialized, "Use of uninitialized LosslessHasher");
        Set<Vertex> set = new HashSet<Vertex>();
        set.add(_code2vertex.get(code));
        return set;
    }

    @Override
    public boolean isLossy() {
        return false;
    }
}