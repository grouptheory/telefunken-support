/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import telefunken.math.bigdecimalutils.BigDecimalMath;

/**
 *
 * @author Bilal Khan
 */
class BoundedComputationV2 {
    
    private static final int DIGITS=10;
    
    public static BigDecimal calculateProb(BigDecimal R, BigDecimal K, BigDecimal L) {
        BigDecimal accum = BigDecimal.ZERO;
        
        int r_int = R.intValue();
        int count=0;
        for (BigDecimal H = BigDecimal.ZERO; ; H=H.add(BigDecimal.ONE)) {
            BigDecimal increment = calcForOneH(R, K, L, H);
            accum = accum.add( increment );
            
            //print("H-loop prob = ", R, K, L, H, increment);
            
            count++;
            if (count == r_int+1) break;
        }
        
        return accum;
    }

    public static void print(String msg, BigDecimal R, BigDecimal K, BigDecimal L, BigDecimal H, BigDecimal val) {
        if (L!=null) System.out.println("L="+L.toPlainString());
        if (K!=null) System.out.println("K="+K.toPlainString());
        if (R!=null) System.out.println("R="+R.toPlainString());
        if (H!=null) System.out.println("H="+H.toPlainString());
        System.out.println(""+msg+" : "+val.toPlainString());
    }
    
    private static BigDecimal calcForOneH(BigDecimal R, BigDecimal K, BigDecimal L, BigDecimal H) {
        
        LinkedList<BigDecimal> numerator = build_numerator(R,K,L,H);
        LinkedList<BigDecimal> denominator = build_denominator(R,K,L,H);
        
        BigDecimal answer = BigDecimal.ONE;
        while (numerator.size() > 0 || denominator.size() > 0) 
        {
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
    
    private static LinkedList<BigDecimal> build_numerator(BigDecimal R, BigDecimal K, BigDecimal L, BigDecimal H) 
    {
        LinkedList<BigDecimal> numerator = new LinkedList<BigDecimal>();
        
        // K!
        addFactToList(K, numerator);
        
        // L lower factorial
        addLowerFactToList(L, K.subtract(R), numerator);
        
        // R!
        addFactToList(R, numerator);
        
        // Gamma(H, R-H, R-H)
        addGammaToList(H, R.subtract(H), R.subtract(H), numerator);
        
        return numerator;
    }
    
    
    private static LinkedList<BigDecimal> build_denominator(BigDecimal R, BigDecimal K, BigDecimal L, BigDecimal H) 
    {
        LinkedList<BigDecimal> denominator = new LinkedList<BigDecimal>();

        //  h! * (h+k-2*r)! * ((r-h)!)^2)
        addFactToList(H.add(K).subtract(TWO.multiply(R)), denominator);
        addFactToList(H, denominator);
        addFactToList(R.subtract(H), denominator);
        addFactToList(R.subtract(H), denominator);
        
        // K+L lower factorial K
        addLowerFactToList(K.add(L), K, denominator);
        
        // Gamma(K, 0, L)
        addGammaToList(K, BigDecimal.ZERO, L, denominator);
        
        return denominator;
    }
    
    
    private static void addFactToList(BigDecimal X, LinkedList<BigDecimal> list) {
        // X!
        if (X.compareTo(BigDecimal.ONE) <= 0) return;
        
        int x_int = X.intValue();
        int count=0;
        for (BigDecimal x = BigDecimal.ONE; ; x=x.add(BigDecimal.ONE)) {
            list.add(x);
            count++;
            if (count == x_int) break;
        }
    }
    
    private static void addLowerFactToList(BigDecimal base, BigDecimal exponent, LinkedList<BigDecimal> list) {
        // X!
        if (exponent.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (base.compareTo(BigDecimal.ONE) <= 0) {
            int test = 1;
            return;
        }
        
        int exp_int = exponent.intValue();
        int count=0;
        for (BigDecimal x = base; ; x=x.subtract(BigDecimal.ONE)) {
            list.add(x);
            count++;
            if (count == exp_int) break;
        }
    }
    
    private static void addGammaToList(BigDecimal C, BigDecimal A, BigDecimal B, LinkedList<BigDecimal> list) {
        BigDecimal FRAC = C.divide(C.add(B),DIGITS, RoundingMode.HALF_UP);
        BigDecimal answer = 
                BigDecimal.ONE
                          .subtract( LINEAR_COEFF.multiply(FRAC) )
                          .add( QUAD_COEFF.multiply(FRAC).multiply(FRAC) );
        list.add(answer);
    }
    
    private static final BigDecimal TWO = new BigDecimal("2.0");
    private static final BigDecimal TEN = new BigDecimal("10.0");
    private static final BigDecimal ONE_MINUS_1_OVER_E = BigDecimal.ONE.subtract
                (BigDecimal.ONE.divide( BigDecimalMath.E, DIGITS, RoundingMode.HALF_UP));
    private static final BigDecimal PI_OVER_10 = BigDecimalMath.pi(MathContext.UNLIMITED).divide
                (TEN, DIGITS, RoundingMode.HALF_UP);
    
    private static final BigDecimal LINEAR_COEFF = ONE_MINUS_1_OVER_E.add(PI_OVER_10);
    private static final BigDecimal QUAD_COEFF = PI_OVER_10;
}
