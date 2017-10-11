/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.estimators;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import telefunken.bootstrap.RiddersMethod;
import telefunken.core.Vertex;
import telefunken.hashers.IHasher;
import telefunken.samplers.AbstractSample;
import telefunken.samplers.RDSSample;
import telefunken.utils.Assertion;

/**
 *
 * @author Bilal Khan
 */
public class S_rS_M_Computer 
{
    public final Multiset<Vertex> S;
    public final Multiset<Integer> S_codes;
    public final double S_size;
    
    public final Multiset<Vertex> rS;
    public final Multiset<Integer> rS_codes;
    public final double rS_size;
    
    public final Multiset<Integer> M_codes;
    
    public final double rho;
    public final double dSample;
    public final double dPopulation;
    
    public final HashMap<Vertex, Double> seed_to_non_comp_i_size = new HashMap<Vertex, Double>();
    public final HashMap<Vertex, Double> seed_to_non_comp_i_deg = new HashMap<Vertex, Double>();
    public final HashMap<Vertex, Double> seed_to_outset_i_size = new HashMap<Vertex, Double>();
    public final HashMap<Vertex, Multiset<Vertex>> seed_to_mId_i = new HashMap<Vertex, Multiset<Vertex>>();
    
    public S_rS_M_Computer(AbstractSample sample, IHasher hash) 
    {
        S = HashMultiset.create();
        S.addAll(sample.getVertices());
        S_codes = hash.getCodes(S);
        S_size = S.size();
        
        rS = sample.getOutset(sample.getVertices()) ;
        rS_codes = hash.getCodes(rS);
        rS_size = rS.size();
        
        M_codes = HashMultiset.create();
        for (Integer x : rS_codes) {
            if (S_codes.contains(x)) {
                M_codes.add(x);
            }
        }
        
        rho = hash.isLossy() ? 1.0/hash.getHashspaceSize() : 1.0;
        dSample = sample.dG_Sample_Calculate_Moment(1);
        dPopulation = sample.dSample_Harmonic(1);
        
        if (sample instanceof RDSSample) {
            RDSSample rds = (RDSSample)sample;
            for (Vertex seed_i : rds.getSeeds()) 
            {
                HashSet<Vertex> comp_i = rds.getComponent(seed_i);
                Multiset<Vertex> non_comp_i = all_other_components(rds, seed_i);
                
                Assertion.test(comp_i.size()+non_comp_i.size() == (int)sample.size(), 
                        "not all vertices are accounted for or some are double counted");
                
                seed_to_non_comp_i_size.put(seed_i, (double)non_comp_i.size());
                seed_to_non_comp_i_deg.put(seed_i, meanDegree(non_comp_i, rds));
                
                Multiset<Integer> non_comp_i_codes = hash.getCodes(non_comp_i);
                
                // free neighbors of vertices in comp_i (excluding RDS neighbors)
                Multiset<Vertex> outset_i = sample.getOutset(comp_i);
                
                // codes of free neighbors of vertices in comp_i
                Multiset<Integer> outset_i_codes = hash.getCodes(outset_i);
                
                // size of outset multiset of codes
                seed_to_outset_i_size.put(seed_i, (double)outset_i.size());
                
                Assertion.test(outset_i.size() == outset_i_codes.size(), 
                        "outset size and outset codes size must be the same");
                
                Multiset<Integer> match_i_codes = HashMultiset.create();
                // go through the reports (hash codes)
                for (Integer x : outset_i_codes) {
                    // if we find that hash code in an outside component
                    if (non_comp_i_codes.contains(x)) {
                        // it is called a match code and we add it to M
                        match_i_codes.add(x);
                    }
                }
                
                // for every match code xH
                Multiset<Vertex> mId_i = HashMultiset.create();
                for (Integer xH : match_i_codes) 
                { 
                    // this is actually ALL vertices in the entire graph which have the code xH
                    Collection<Vertex> AllVertices_withCode_xH = hash.getVerticesWithCode(xH);
                    
                    // filter this set down
                    for (Vertex v : AllVertices_withCode_xH) {
                        // to just those vertices which are in the other cross seed components
                        // and have code xH
                        if (non_comp_i.contains(v)) {
                            mId_i.add(v);
                        }
                    }
                }
                seed_to_mId_i.put(seed_i, mId_i);
            }
        }
    }
    
    // average of true graph vertex degrees (not free degree)
    private double meanDegree(Multiset<Vertex> vset, AbstractSample sample) {
        double ave = 0.0;
        if (vset.size() > 0) {
            double accum = 0.0;
            for (Vertex v : vset) {
                accum += sample.dGraph(v);
            }
            ave = accum/(double)vset.size();
        }
        return ave;
    }
    
    private Multiset<Vertex> all_other_components(RDSSample rds, Vertex v) 
    {
        Vertex seed = rds.getSeedOf(v);
        
        Multiset<Vertex> others = HashMultiset.create();
        for (Vertex seed_i : rds.getSeeds()) 
        {
            if (seed_i==seed) continue;
            
            HashSet<Vertex> comp_i = rds.getComponent(seed_i);
            others.addAll(comp_i);
        }
        return others;
    }
    
    public double getWeightedM(double N, AbstractSample sample, IHasher hash) {
        double weightedM = 0.0;
        for (Integer xH : M_codes) {
            Collection<Vertex> AllVertices_withCode_xH = hash.getVerticesWithCode(xH);
            
            for (Vertex v : AllVertices_withCode_xH) {
                if (S.contains(v)) {
                    weightedM += 1.0 / (dPopulation * rho * (N - 1.0) / (sample.dGraph(v) - 1.0) + 1.0);
                }
            }
        }
        return weightedM;
    }
    
    public double getCrossSeedNumerator(double N, RDSSample rds) {
        double num = 0.0;
        for (Vertex seed_i : rds.getSeeds()) 
        {
            num += (seed_to_outset_i_size.get(seed_i) * 
                    seed_to_non_comp_i_size.get(seed_i) * 
                    (seed_to_non_comp_i_deg.get(seed_i) - 1.0) / dPopulation);
        }
        return num;
    }
    
    public double getCrossSeedDenominator(double N, RDSSample rds) {
        double den = 0.0;
        for (Vertex seed_i : rds.getSeeds()) 
        {
            double weightedM = 0.0;
            Multiset<Vertex> mId_i = seed_to_mId_i.get(seed_i);
            for (Vertex v : mId_i) {
                weightedM += 1.0 / (dPopulation * rho * (N - 1.0) / (rds.dGraph(v) - 1.0) + 1.0);
            }
            den += weightedM;
        }
        return den;
    }
}
