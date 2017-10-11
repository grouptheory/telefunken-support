/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import telefunken.math.bigdecimalutils.BigDecimalMath;

/**
 *
 * @author Bilal Khan
 */
public class BoundedComputationV1 
{
    private static final int DIGITS=10;
    
    public static BigDecimal calculateProb(BigDecimal R, BigDecimal K, BigDecimal L) {
        LinkedList<BigDecimal> numerator = build_numerator(R,K,L);
        LinkedList<BigDecimal> denominator = build_denominator(R,K,L);
        
        BigDecimal answer = BigDecimal.ONE;
        while (numerator.size() > 0 || denominator.size() > 0) {
            while (answer.compareTo(BigDecimal.ONE) > 0 && denominator.size() > 0) {
                BigDecimal y = denominator.removeFirst();
                answer = answer.divide(y, DIGITS, RoundingMode.HALF_UP);
            }
            if (numerator.size() > 0) {
                BigDecimal x = numerator.removeFirst();
                answer = answer.multiply(x);
            }
            else {
                while (denominator.size() > 0) {
                    BigDecimal y = denominator.removeFirst();
                    answer = answer.divide(y, DIGITS, RoundingMode.HALF_UP);
                }
            }
        }
        return answer;
    }
    
    private static LinkedList<BigDecimal> build_numerator(BigDecimal R, BigDecimal K, BigDecimal L) 
    {
        LinkedList<BigDecimal> numerator = new LinkedList<BigDecimal>();
        
        int r_int = R.intValue();
        int kminusr_int = K.intValue() - r_int;
        
        int count=0;
        for (BigDecimal x = K; ; x=x.subtract(BigDecimal.ONE)) {
            numerator.add(x);
            count++;
            if (count == r_int) break;
        }
        
        count=0;
        for (BigDecimal x = K; ; x=x.subtract(BigDecimal.ONE)) {
            numerator.add(x);
            count++;
            if (count == r_int) break;
        }
        
        count=0;
        for (BigDecimal x = L; ; x=x.subtract(BigDecimal.ONE)) {
            numerator.add(x);
            count++;
            if (count == kminusr_int) break;
        }
        
        return numerator;
    }
    
    private static LinkedList<BigDecimal> build_denominator(BigDecimal R, BigDecimal K, BigDecimal L) 
    {
        LinkedList<BigDecimal> denominator = new LinkedList<BigDecimal>();

        int r_int = R.intValue();
        int k_int = K.intValue();
        BigDecimal L_plus_K = L.add(K);
                
        int count=0;
        for (BigDecimal y = R; ; y=y.subtract(BigDecimal.ONE)) {
            count++;
            denominator.add(y);
            if (count == r_int) break;
        }
        count=0;
        for (BigDecimal y = L_plus_K; ; y=y.subtract(BigDecimal.ONE)) {
            denominator.add(y);
            count++;
            if (count == k_int) break;
        }
        
        denominator.add(BigDecimalMath.E);
        
        return denominator;
    }
}
