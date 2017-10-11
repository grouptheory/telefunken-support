/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.utils;

import telefunken.core.Edge;


/**
 *
 * @author Bilal Khan
 */
public interface IEdgeWeightAssigner {

    public double getWeight(Edge e);
}