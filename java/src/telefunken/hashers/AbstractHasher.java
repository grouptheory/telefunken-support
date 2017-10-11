/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.hashers;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.Collection;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public abstract class AbstractHasher implements IHasher {

    protected AbstractHasher() {
    }
    
    public final Multiset<Integer> getCodes(Multiset<Vertex> vset) 
    {    
        Multiset<Integer> codes = HashMultiset.create();
        for (Vertex v : vset) {
            codes.add(getCode(v));
        }
        return codes;
    }
    
    public abstract void initialize(Collection<Vertex> vset);
    public abstract Integer getCode(Vertex v);
    public abstract Collection<Vertex> getVerticesWithCode(int code);
    
    public abstract boolean isLossy();
}
