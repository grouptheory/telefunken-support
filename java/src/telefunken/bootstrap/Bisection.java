/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.bootstrap;

/**
 *
 * @author Bilal Khan
 */
public class Bisection implements RootFinder
{
    public static double root(double a, double b, AbstractMatchingEstimator f)
    {
        return root(1e-2, 1000, a, b, f);
    }
   
    public static double root(double eps, int maxIterations, double a, double b, AbstractMatchingEstimator f)
    {
        if(b <= a)
            throw new ArithmeticException("a must be < b for Bisection to work");
        
        double fb = f.f(b);
        double fa = f.f(a);
        
        if(fa* fb >= 0)
            throw new ArithmeticException("The given interval does not appear to bracket the root");
   
        while(b - a > 2*eps && maxIterations-- > 0)
        {
            double mid = (a+b)*0.5;
            double ftmp = f.f(mid);
            
            if(fa*ftmp < 0)
            {
                b = mid;
                fb = ftmp;
            }
            else if(fb * ftmp < 0)
            {
                a = mid;
                fa = ftmp;
            }
            else
                break;//We converged
        }
        
        return (a+b)*0.5;
    }

    public double root(double eps, int maxIterations, double[] initialGuesses, AbstractMatchingEstimator f)
    {
        return root(eps, maxIterations, initialGuesses[0], initialGuesses[1], f);
    }
}
