/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.samplers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import telefunken.core.Edge;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */

public class RDSSample extends AbstractSample 
{
    static class Pair {
        Vertex subject;
        Vertex recruited;
    }
    
    private HashMap<Vertex, Vertex> vertexToSeedMap = new HashMap<Vertex,Vertex>();
    private HashSet<Vertex> seeds = new HashSet<Vertex>();
    private HashMap<Vertex, HashSet<Vertex>> seedToComponentMap = new HashMap<Vertex,HashSet<Vertex>>();
    
    private final TreeMap<Double,Vertex> _queue;
    private final ArrayList<Pair> _recruitmentSequence;

    RDSSample(DirectedSparseGraph<Vertex,Edge> graph) {
        super(graph, "RDS");
        _queue = new TreeMap<Double,Vertex>();
        _recruitmentSequence = new ArrayList<Pair>();
    }

    public void enqueue(Double t, Vertex v) {
        if (_queue.containsKey(t)) {
            throw new RuntimeException("Subject attempts to book a time that is already used");
        }
        _queue.put(t, v);
        //System.out.println("Enqueue "+v.getUserDatum(Vertex.LABEL) + " size="+_queue.size());
    }

    public double timeNow() {
        double timeNow=_queue.firstKey();
        return timeNow;
    }

    public Vertex dequeue() {
        double timeNow=_queue.firstKey();
        Vertex subject=_queue.remove(timeNow);
        //System.out.println("Deque "+subject.getUserDatum(Vertex.LABEL) + " size="+_queue.size());
        return subject;
    }

    public boolean emptyQueue() {
        return _queue.isEmpty();
    }
    
    public String toString() {
        String s = "";
        s = super.toString();
        
        for (Pair p : _recruitmentSequence) {
            String line;
            if (p.subject == p.recruited) {
                line = "** seed: "+p.subject.getUserDatum(Vertex.LABEL) + "\n";
            }
            else {
                line = p.subject.getUserDatum(Vertex.LABEL) + 
                    " recruited " + 
                    p.recruited.getUserDatum(Vertex.LABEL)+
                    "\n";
            }
            s += line;
        }
        return s;
    }
    
    void addRecruited(Vertex subject, Vertex oneRecruit) {
        Pair p = new Pair();
        p.subject=subject;
        p.recruited=oneRecruit;
        _recruitmentSequence.add(p);
        
        if (subject==oneRecruit) {
            // the subject is a seed!
            seeds.add(subject);
            HashSet newSeedsComponent = new HashSet<Vertex>();
            newSeedsComponent.add(subject); // change-9-5-17
            seedToComponentMap.put(subject, newSeedsComponent);
            vertexToSeedMap.put(subject, subject);
        }
        else {
            // the subject is not a seed
            Vertex subjectsSeed = vertexToSeedMap.get(subject);
            HashSet<Vertex> seedsComponent = seedToComponentMap.get(subjectsSeed);
            vertexToSeedMap.put(oneRecruit, subjectsSeed);
            seedsComponent.add(oneRecruit);
        }
    }
    
    public HashSet<Vertex> getSeeds() {
        return seeds;
    }
    
    public Vertex getSeedOf(Vertex v) {
        return vertexToSeedMap.get(v);
    }
    
    public HashSet<Vertex> getComponent(Vertex v) {
        return seedToComponentMap.get(getSeedOf(v));
    }
}
