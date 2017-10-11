/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.samplers;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.hashers.IHasher;
import telefunken.utils.Assertion;

/**
 *
 * @author Bilal Khan
 */

public abstract class AbstractSample {

    private final DirectedSparseGraph<Vertex,Edge> _graph;
    private final DirectedSparseGraph<Vertex,Edge> _sample;
    
    protected final String _type;
    protected AbstractSample(DirectedSparseGraph<Vertex,Edge> graph, String type) {
        _type = type;
        _sample = new DirectedSparseGraph(); // initially an empty graph
        _graph = graph;
    }

    public void addVertex(Vertex v) {
        _sample.addVertex(v);
    }

    public void addEdge(Vertex u, Vertex v) {
        _sample.addEdge(new Edge(), u,v);
        _sample.addEdge(new Edge(), v,u);
    }
    
    public Collection<Vertex> getVertices() {
       return _sample.getVertices();
    }
    
    public double numComponents() {
	WeakComponentClusterer wccg = new WeakComponentClusterer();
	Set<Set<Vertex>> csg = wccg.transform(_sample);
        return csg.size();
    }
    
    public double dGraph(Vertex v) {
        return _graph.outDegree(v);
    }
    
    public double dTree(Vertex v) {
        return _sample.outDegree(v);
    }
    
    public double dFree(Vertex v) {
        return dGraph(v)-dTree(v);
    }
    
    public double get_TrueL() {
        return get_GraphEdgeCount() - get_SampleEndsCount();
    }
    
    public double get_GraphVertexCount() {
        return (double)_graph.getVertexCount();
    }
    
    
    public double get_GraphEdgeCount() {
        return (double)_graph.getEdgeCount();
    }
    
    public double get_SampleEndsCount() {
        // 2F
        return (double)_sample.getEdgeCount();
    }
    
    public static final int MOMENTS_COUNT=5;
    
    public double dSample_Harmonic(int power) {
        double sum=0.0;
        double count=0.0;
        for (Vertex v : _sample.getVertices()) 
        {
            if (_graph.outDegree(v) > 0) { // change-9-5-17
                sum += 1.0 / (Math.pow((double)_graph.outDegree(v), (double)power));
                count += 1.0;
            }
        }
        double val = (count > 0) ? count/sum : 0.0; // change-9-5-17
        return val;
    }
    
    public double[] dSample_All_Harmonic_Moments() 
    {
        Assertion.test(MOMENTS_COUNT>=0, "maxpower >= 0");
        double[] moments = new double[MOMENTS_COUNT];
        for (int power=0; power<MOMENTS_COUNT; power++) 
        {
            moments[power] = dSample_Harmonic(power);
        }
        return moments;
    }
    
    public double[] dSample_All_Moments() 
    {
        Assertion.test(MOMENTS_COUNT>=0, "maxpower >= 0");
        double[] moments = new double[MOMENTS_COUNT];
        for (int power=0; power<MOMENTS_COUNT; power++) 
        {
            moments[power] = dG_Sample_Calculate_Moment(power);
        }
        return moments;
    }
    
    public double dG_Sample_Calculate_Moment(int power) {
        double sum=0.0;
        double count=0.0;
        for (Vertex v : _sample.getVertices()) 
        {
            sum += Math.pow((double)_graph.outDegree(v), (double)power);
            count += 1.0;
        }
        double val = (count > 0) ? sum/count : 0.0;
        Assertion.test(power%2==1 || val>=0, "power is even and dG_Sample_Calculate_Moment >= 0");
        return val;
    }
    
    public double[] dG_All_Moments() 
    {
        Assertion.test(MOMENTS_COUNT>=0, "maxpower >= 0");
        double[] moments = new double[MOMENTS_COUNT];
        for (int power=0; power<MOMENTS_COUNT; power++) 
        {
            moments[power] = dG_Pop_True_Calculate_Moment(power);
        }
        return moments;
    }
    
    public double dG_Pop_True_Calculate_Moment(int power) {
        double sum=0.0;
        double count=0.0;
        for (Vertex v : _graph.getVertices()) 
        {
            sum += Math.pow((double)(double)_graph.outDegree(v), (double)power);
            count += 1.0;
        }
        double val = (count > 0) ? sum/count : 0.0;
        Assertion.test(power%2==1 || val>=0, "dG_Pop_Ave_True >= 0");
        return val;
    }
    
    public int size() {
        return _sample.getVertices().size();
    }

    public Multiset<Vertex> getOutset(Collection<Vertex> vertices) 
    {
        Multiset<Vertex> reportsMultiset = HashMultiset.create();
        for (Vertex v : vertices) 
        {
            // For a vertex v
            Collection<Vertex> neighborsInG = _graph.getSuccessors(v); // definitely a set because we no parallel edges
            Collection<Vertex> neighborsInS = _sample.getSuccessors(v);
            
            Multiset<Vertex> freeNeighbors = HashMultiset.create();
            for (Vertex neighbor_v : neighborsInG) {
                if (neighborsInS.contains(neighbor_v)) {
                    continue;
                }
                else {
                    freeNeighbors.add(neighbor_v);
                }
            }
            
            reportsMultiset.addAll(freeNeighbors);
        }
        return reportsMultiset; // this is outset
    }
    
    public Multiset<Integer> getReportsAsCodes_OLD(IHasher hash) 
    {
        Multiset<Vertex> reportsMultiset = HashMultiset.create();
        Multiset<Integer> reportsMultiset_codes = HashMultiset.create();
            
        for (Vertex v : getVertices()) 
        {
            // For a vertex v
            Collection<Vertex> neighborsInG = _graph.getSuccessors(v); // definitely a set because we no parallel edges
            Multiset<Vertex> neighborsInG_multiset = HashMultiset.create();
            neighborsInG_multiset.addAll(neighborsInG); // make it into a multiset of vertices -- this is a formality -- it is just a set
            // get hash codes
            Multiset<Integer> neighborsInG_codes = hash.getCodes(neighborsInG_multiset);
            // in lossy hasher this may be smaller because of code collisions
            Set<Integer> neighborsInG_codes_set = neighborsInG_codes.elementSet();
            
            // for a vertex in v
            Collection<Vertex> neighborsInS = _sample.getSuccessors(v);
            Multiset<Vertex> neighborsInS_multiset = HashMultiset.create();
            neighborsInS_multiset.addAll(neighborsInS);
            // get hash codes of neighbors in S
            Multiset<Integer> neighborsInS_codes = hash.getCodes(neighborsInS_multiset);
            // in lossy hasher this may be smaller because of code collisions
            Set<Integer> neighborsInS_codes_set = neighborsInS_codes.elementSet();
            
            // a code is illegal to be reported 
            // if it matches a code of your neighbor in S
            Set<Integer> legal_codes = Sets.difference(neighborsInG_codes_set, neighborsInS_codes_set);
                    
            for (Vertex neighbor : neighborsInG_multiset) {
                Integer neighborcode=hash.getCode(neighbor);
                if (legal_codes.contains(neighborcode)) {
                    reportsMultiset.add(neighbor);
                    reportsMultiset_codes.add(neighborcode);
                }
            }
        }
        return reportsMultiset_codes;
    }

    public static class Report {
        public Vertex subject;
        public Vertex reported;
    }
    
    public Set<Report> getReportsWithoutHashing() 
    {
        Set<Report> reports = new HashSet<Report>();
            
        // go through vertices in sample
        for (Vertex v : getVertices()) 
        {
            // get neighbors in G
            Collection<Vertex> neighborsInG = _graph.getSuccessors(v);
            Multiset<Vertex> neighborsInG_multiset = HashMultiset.create();
            neighborsInG_multiset.addAll(neighborsInG);
            
            // get the neighbors in S
            Collection<Vertex> neighborsInS = _sample.getSuccessors(v);
            Multiset<Vertex> neighborsInS_multiset = HashMultiset.create();
            neighborsInS_multiset.addAll(neighborsInS);
            
            for (Vertex neighbor : neighborsInG_multiset) 
            {
                if ( ! neighborsInS.contains(neighbor)) {
                    Report one_report = new Report();
                    one_report.subject = v;
                    one_report.reported = neighbor;
                    reports.add(one_report);
                }
            }
        }
        return reports;
    }

    public HashMap<Vertex, Integer> getSampleDegreeinGMap() {
        HashMap<Vertex, Integer> v2deg = new HashMap<Vertex,Integer>();
        for (Vertex v : _sample.getVertices()) {
            v2deg.put(v, _graph.outDegree(v));
        }
        return v2deg;
    }
    
    public String toString() {
        String s = "";
        
        for (Vertex v : _sample.getVertices()) {
            String line = v.getUserDatum(Vertex.LABEL) + ": " + 
                    dTree(v) + ", " + 
                    dGraph(v) + ", " + 
                    dFree(v) +"\n";
            s += line;
        }
        
        return s;
    }
}
