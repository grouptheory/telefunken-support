/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.Test;
import telefunken.math.Combinatorics;
import telefunken.math.RandomFunctionImageAnalytic;
import telefunken.math.RandomFunctionImageApproximations;
import telefunken.math.RandomFunctionImageExplicit;

/**
 *
 * @author Bilal Khan
 */
public class StirlingTest {
    
    @Test
    public void test1() {
        int n=30;
        int k=5;
        dowork(n,k);
        
        n=60;
        k=10;
        dowork(n,k);
        
        n=120;
        k=20;
        dowork(n,k);
        
        n=1200;
        k=200;
        dowork(n,k);
        
        n=2400;
        k=400;
        dowork(n,k);
    }
    
    private void dowork(int n, int k) {
        
        System.out.println("n="+n);
        System.out.println("k="+k);
        
        BigInteger npowk = Combinatorics.Power(n, k);
        System.out.println("n^k="+npowk);
        
        BigInteger nCk = Combinatorics.Choose(n, k);
        System.out.println("nCk="+nCk);
        
        BigInteger Snk = Combinatorics.Stirling(n, k);
        BigInteger Snk2 = Combinatorics.StirlingApprox(n, k);
        BigDecimal ratio = (new BigDecimal(Snk)).divide(new BigDecimal(Snk2),100,RoundingMode.HALF_UP);
        
        System.out.println("Snk_ACTUAL="+Snk);
        System.out.println("Snk_APPROX="+Snk2);
        System.out.println("Snk_RATIO ="+ratio);
        
        double expIm1 = RandomFunctionImageApproximations.expectedImageSize(n, k);
        System.out.println("ExpIm approx="+expIm1);
        
        double expIm2 = RandomFunctionImageAnalytic.expectedImageSize(n, k);
        System.out.println("ExpIm analytic="+expIm2);
        
        System.out.println("Coverage="+expIm2/(double)k);
        
        double expIm3 = RandomFunctionImageExplicit.expectedImageSize(n, k);
        System.out.println("ExpIm explicit="+expIm3);
    }
}
