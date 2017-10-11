/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.hashers;

import com.google.common.collect.Multiset;
import java.util.Collection;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public interface IHasher {
 
    boolean isLossy();
    int getHashspaceSize();
    
    void initialize(Collection<Vertex> vset);
    
    Integer getCode(Vertex v);  
    Multiset<Integer> getCodes(Multiset<Vertex> vset);  
    
    Collection<Vertex> getVerticesWithCode(int code);
}
