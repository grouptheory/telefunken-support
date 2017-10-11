/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.utils;

import telefunken.math.Randomness;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import telefunken.core.Edge;

/**
 *
 * @author Bilal Khan
 */
public final class EdgeSelector {

    private final TreeMap<Double,Edge> _accum2edge = new TreeMap();
    private final double _max;

    public EdgeSelector(Collection<Edge> edges, IEdgeWeightAssigner a) {
	double accum=0.0;
	for (Edge e : edges) {
	    double w = a.getWeight(e);
	    accum+=w;
	    _accum2edge.put(accum, e);
	}
	_max = accum;
    }

    public Edge getEdge() {
	SortedMap<Double,Edge> tail = _accum2edge.tailMap( _max*Randomness.getDouble() );
	Edge e = tail.get(tail.firstKey());
	return e;
    }
}

