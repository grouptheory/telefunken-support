/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.utils;

import telefunken.math.Randomness;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.Collection;
import java.util.Set;
import telefunken.core.Edge;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public final class GraphUtils {

    public static Vertex getRandomBigComponentVertex(DirectedSparseGraph<Vertex,Edge> g) {

	WeakComponentClusterer wccg = new WeakComponentClusterer();
	Set<Set<Vertex>> csg = wccg.transform(g);

	int biggestsize = -1;
        Set biggestcluster = null;
	for (Set<Vertex> clus : csg) {
	    if (clus.size() > biggestsize) {
                biggestsize = clus.size();
                biggestcluster = clus;
            }
	}

	Vertex chosenVertex = null;
        if (biggestsize > 0) 
        {
		Object[] varray = biggestcluster.toArray();
		int r = (int)(Randomness.getDouble() * varray.length);
		chosenVertex = (Vertex)varray[r];
		Log.diag(GraphUtils.class.getName(), Log.DEBUG, "vertex "+chosenVertex+" in biggest cluster of size "+biggestsize);
	}
	return chosenVertex;
    }

    public static Vertex getRandomGraphVertex(DirectedSparseGraph<Vertex,Edge> g) {
	Collection<Vertex> vset = g.getVertices();
	Object[] varray = vset.toArray();
	int r = (int)(Randomness.getDouble() * varray.length);
	Vertex v = (Vertex)varray[r];
	return v;
    }

    public static Vertex getRandomOutNeighborGraphVertex(DirectedSparseGraph<Vertex,Edge> g, Vertex gv, IEdgeWeightAssigner ews) {
	Collection<Edge> e2 = g.getOutEdges(gv);
	EdgeSelector es = new EdgeSelector(e2, ews);
	Edge e = es.getEdge();
	Vertex gu = g.getOpposite(gv,e);
	return gu;
    }

    public static Vertex getRandomInNeighborGraphVertex(DirectedSparseGraph<Vertex,Edge> g, Vertex gv, IEdgeWeightAssigner ews) {
	Collection<Edge> e1 = g.getInEdges(gv);
	EdgeSelector es = new EdgeSelector(e1, ews);
	Edge e = es.getEdge();
	Vertex gu = g.getOpposite(gv,e);
	return gu;
    }

    public static String getVertexLabel(Vertex v) {
	return (String)v.getUserDatum(Vertex.LABEL);
    }
}

