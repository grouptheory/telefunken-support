/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.samplers;

import com.google.common.collect.Sets;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import telefunken.core.Edge;
import telefunken.core.Vertex;
import telefunken.utils.GraphUtils;
import telefunken.math.Randomness;

/**
 *
 * @author Bilal Khan
 */
public class RDSSampler implements ISampler {
    
    private int _numseeds, _samplesize;
    private double _referralsDist[];
    
    public RDSSampler(int numseeds, int samplesize, double[] referrals) {
        _numseeds=numseeds; 
        _samplesize=samplesize; 
        _referralsDist = new double[referrals.length];
        for (int i=0; i<referrals.length; i++) _referralsDist[i] = referrals[i];
    }

    public AbstractSample generateSample(edu.uci.ics.jung.graph.DirectedSparseGraph<Vertex,Edge> g) 
    {
        RDSSample S=enqueueInitialSeeds(g, _numseeds);
        double timeNow=S.timeNow();
        
        while (S.size() < _samplesize) 
        {
            // there are no more vertices in our sample that can be expanded
            if (S.emptyQueue()) {
                // go and get another seed
                HashSet<Vertex> seeds=getSeeds(g, S, 1);
                // add it to our sample
                addSeedsToSample(S, seeds, timeNow);
            }
            // the time is the time that the next person in our sample 
            // is scheduled to come in and give us their referrals
            timeNow=S.timeNow();
            
            // get the next person from our sample -
            // they are ready to turn in their recruits
            //System.out.println("Subject comes in to give referalls");
            Vertex subject=S.dequeue();
            //System.out.println("----It is "+subject.getUserDatum(Vertex.LABEL) + "");
        
            // get all their neighbors in G
            Collection<Vertex> neighbors=g.getSuccessors(subject); 
            
            // remove the neighbors who are already in S 
            // because they were already recruited
            Set<Vertex> legalNeighbors=Sets.difference(new HashSet(neighbors), new HashSet(S.getVertices())); 
            Set<Vertex> legalNeighbors_mutable = new HashSet();
            legalNeighbors_mutable.addAll(legalNeighbors);
            
            // compute target number of recruits
            int maxPossibleRecruits=Math.min(legalNeighbors_mutable.size(), _referralsDist.length - 1);
            
            // we have a shot at recruiting
            if (maxPossibleRecruits > 0) {
                // how many people will they ideally recruit
                double adjustedReferralDist[] = new double[maxPossibleRecruits+1]; // +1 for the 0 recruit case
                for (int i=0; i<maxPossibleRecruits+1; i++) {
                    adjustedReferralDist[i] = _referralsDist[i];
                }
                double referralsCumulativeDist [] = Randomness.convertToCumulativeDistribution(adjustedReferralDist);
                int targetRecruitNumber=Randomness.getSample(referralsCumulativeDist);

                int actualRecruits = 0;
                while (actualRecruits < targetRecruitNumber && legalNeighbors_mutable.size() > 0 && S.size()<_samplesize) {
                    // pick the neighbor you give the coupon to
                    Vertex oneRecruit=getRandomVertex(legalNeighbors_mutable);
                    // attach new referral to sample
                    S.addVertex(oneRecruit);
                    
                    S.addEdge(subject, oneRecruit);
                    
                    S.addRecruited(subject,oneRecruit);
                    
                    // schedule when this recruit will come in with their recruits
                    double offsetTime = Randomness.getExponential(1.0);
                    S.enqueue(timeNow+offsetTime, oneRecruit);
                    actualRecruits++;
                    
                    // the recruit is no longer legal
                    legalNeighbors_mutable.remove(oneRecruit);
                }
            }
        }
        return S;    
    }
    
    private Vertex getRandomVertex(Set<Vertex> set) {
        int options = set.size();
        Vertex[] legalNeighborsArray = new Vertex[options];
        set.toArray(legalNeighborsArray);
        int x = Randomness.getInteger();
        int index = x % options;
        Vertex oneRecruit = legalNeighborsArray[index];
        return oneRecruit;
    }
    
    private RDSSample enqueueInitialSeeds(DirectedSparseGraph<Vertex,Edge> g, int requiredSeeds) {
        RDSSample EmptySample=new RDSSample(g);
        HashSet<Vertex> seeds=getSeeds(g, EmptySample, requiredSeeds);
        
        addSeedsToSample(EmptySample, seeds, 0.0);
        
        return EmptySample; // not empty any more
    }
    
    private void addSeedsToSample(RDSSample S, HashSet<Vertex> seeds, double time) {
        double EPSI=1.0/(double)seeds.size();
        int i=1;
        for (Vertex v : seeds) {
            S.addVertex(v);
            S.addRecruited(v,v);
        
            S.enqueue(time + i * EPSI, v);
            i++;
        }
    }
    
    private HashSet<Vertex> getSeeds(DirectedSparseGraph<Vertex,Edge> g, RDSSample exclude, int requiredSeeds) {
        HashSet<Vertex> seeds=new HashSet<Vertex>();
        while (seeds.size()<requiredSeeds) 
        {
            Vertex seed=GraphUtils.getRandomGraphVertex(g);
            if ( ! seeds.contains(seed) && !exclude.getVertices().contains(seed)) {
                seeds.add(seed);
            }
        }
        return seeds;
    }

    @Override
    public String getHumanReadableName() {
        return "RDS";
    }
}
