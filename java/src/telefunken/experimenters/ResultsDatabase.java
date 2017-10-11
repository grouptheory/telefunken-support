/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.experimenters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import telefunken.math.Stats;
import telefunken.samplers.AbstractSample;

/**
 *
 * @author Bilal Khan
 */
public class ResultsDatabase {
    
    private static String RESULTS = "X-results.txt";
    private static String AGG_FILE = "X-agg.txt";
    private static String VERBOSE_FILE = "X-verbose.txt";
    
    private void initializeNames(String s) {
        RESULTS = s+"-results.txt";
        AGG_FILE = s+"-agg.txt";
        VERBOSE_FILE = s+"-verbose.txt";
        
        AggResults = new ResultsFile(AGG_FILE);
        VerboseResults = new ResultsFile(VERBOSE_FILE);

        try(FileWriter fw = new FileWriter(RESULTS, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
    
    private ResultsFile AggResults = new ResultsFile(AGG_FILE);
    private ResultsFile VerboseResults = new ResultsFile(VERBOSE_FILE);
    
    public static class Cases{
        private HashMap<String, List<Double>> _values = new HashMap<String, List<Double>>();
        
        public void add(String key, double val) {
            List<Double> vallist = null;
            if (_values.containsKey(key)) {
                vallist = _values.get(key);
            }
            else {
                vallist = new ArrayList<Double>();
                _values.put(key, vallist);
            }
            vallist.add(val);
        }
        
        public List<Double> get(String key) {
            return _values.get(key);
        }
        
        public Set<String> keySet() {
            return _values.keySet();
        }
        
        public Stats.Moments agg(String key) {
            List<Double> values = _values.get(key);
            int len_values = values.size();
            Double[] array_values = new Double [len_values];
            values.toArray(array_values);
            Stats.Moments agg_values = Stats.process(array_values);
            return agg_values;
        }
    }
    
    private Cases _values = new Cases();
    private Cases _times = new Cases();
    private Cases[] _dG = new Cases [AbstractSample.MOMENTS_COUNT];
    private Cases _trueL = new Cases();
    private Cases[] _dS = new Cases [AbstractSample.MOMENTS_COUNT];
    private Cases[] _dH = new Cases [AbstractSample.MOMENTS_COUNT];
    private Cases _dpopTrue = new Cases();
    private Cases _success = new Cases();
    private Cases _truek = new Cases();
    private Cases _truem = new Cases();
    private Cases _2f = new Cases();
    
    private DecimalFormat df = new DecimalFormat("0.00"); 
    
    private String INSERTION_HEADER = null;
    private String getInsertionHeader() {
        if (INSERTION_HEADER==null) {
            INSERTION_HEADER = "# N, G, H, SS, Smp, Ig, Alg, g#, t#, estV";
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                INSERTION_HEADER += (", dG"+i);
            }
            INSERTION_HEADER += ", trueL";
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                INSERTION_HEADER += (", dS"+i);
            }
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                INSERTION_HEADER += (", dH"+i);
            }
            INSERTION_HEADER += ", Ig3, tK, tM, t2F";
        }
        return INSERTION_HEADER;
    }
    
    public ResultsDatabase(String s) {
        
        initializeNames(s);
        
        for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
            _dG[i]=new Cases();
            _dS[i]=new Cases();
            _dH[i]=new Cases();
        }
        
        VerboseResults.write(getInsertionHeader());
        System.out.println(getInsertionHeader());
    }
    
    private String vector_AsString(double[] v) {
       String ans = "";
        for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
            ans += (","+df.format(v[i]));
        }
       return ans;
    }
    public void store(int n, int gi, int gnum, int hi, int ss, int si, int trial, int ei, 
            double val, double[] dG, double trueL, double[] dS, double[] dH, double time, 
            double k, double m, double f2) 
    {
        String key = ""+
                n+","+
                gi+","+
                hi+","+
                ss+","+
                si+","+
                Math.round(dG[1])+","+
                ei;
        
        String subkey = ""+
                gnum+","+
                trial;
        
        //if ( ! Double.isNaN(val)) {
             String row=key+","+subkey
                +","+  (Double.isNaN(val) ? "" : df.format(val))
                + vector_AsString(dG)
                +","+df.format(trueL)
                +vector_AsString(dS)
                +vector_AsString(dH)
                +","+df.format(time)
                +","+df.format(k)
                +","+df.format(m)
                +","+df.format(f2);
            VerboseResults.write(row);
            System.out.println(row);

            try(FileWriter fw = new FileWriter(RESULTS, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(row);
                out.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
            
            if ( ! Double.isNaN(val)) {
                _values.add(key, val);
            }
            else {
                _success.add(key, 0.0);
            }
            
            _times.add(key, time);
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                _dG[i].add(key, dG[i]);
            }
            _trueL.add(key, trueL);
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                _dS[i].add(key, dS[i]);
            }
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                _dH[i].add(key, dH[i]);
            }
            _success.add(key, 1.0);
            _truek.add(key, k);
            _truem.add(key, m);
            _2f.add(key, f2);
        //} else
    }
    
    private String DUMP_HEADER = null;
    private String getDumpHeader() {
        if (DUMP_HEADER==null) {
            DUMP_HEADER = "# N, G, H, SS, Smp, Alg, Trials, V, dV, naV";
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                DUMP_HEADER += (", dG"+i);
            }
            DUMP_HEADER += ", trueL";
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                DUMP_HEADER += (", dS"+i);
            }
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                DUMP_HEADER += (", dH"+i);
            }
            DUMP_HEADER += ", %good, tK, tM, t2F, T, dT";
        }
        return DUMP_HEADER;
    }
    
    public void dump() {
        AggResults.write(getDumpHeader());
        System.out.println(getDumpHeader());
        
        for (String key : _values.keySet()) {
            Stats.Moments agg_values = _values.agg(key);
            Stats.Moments agg_times = _times.agg(key);
            Stats.Moments[] dG_values = new Stats.Moments [AbstractSample.MOMENTS_COUNT];
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                dG_values[i] = _dG[i].agg(key);
            }
            Stats.Moments agg_trueL = _trueL.agg(key);
            Stats.Moments[] dS_values = new Stats.Moments [AbstractSample.MOMENTS_COUNT];
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                dS_values[i] = _dS[i].agg(key);
            }
            Stats.Moments agg_success = _success.agg(key);
            Stats.Moments agg_k = _truek.agg(key);
            Stats.Moments agg_m = _truem.agg(key);
            Stats.Moments agg_2f = _2f.agg(key);
            
            Stats.Moments[] dH_values = new Stats.Moments [AbstractSample.MOMENTS_COUNT];
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                dH_values[i] = _dH[i].agg(key);
            }
            
            // build output
            
            String row = key+","+_values.get(key).size()+","+
                    df.format(agg_values.mean)+","+df.format(agg_values.std)+","+df.format(agg_values.na);
            
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                row+=","+df.format(dG_values[i].mean);
            }
            
            row+=","+(df.format(agg_trueL.mean));
            
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                row+=","+df.format(dS_values[i].mean);
            }
            
            for (int i=0; i<AbstractSample.MOMENTS_COUNT; i++) {
                row+=","+df.format(dH_values[i].mean);
            }
            
            row+=","+
                    df.format(agg_success.mean)+","+
                    df.format(agg_k.mean)+","+
                    df.format(agg_m.mean)+","+
                    df.format(agg_2f.mean)+","+
                    df.format(agg_times.mean)+","+df.format(agg_times.std);
            
            AggResults.write(row);
            System.out.println(row);
        }
        
        AggResults.close();
        VerboseResults.close();
    }
}
