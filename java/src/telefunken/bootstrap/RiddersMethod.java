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
public class RiddersMethod implements RootFinder {

    public static double root(double a, double b, AbstractMatchingEstimator f) {
        return root(1e-15, 1000, a, b, f);
    }

    public static double root(double eps, double a, double b, AbstractMatchingEstimator f) {
        return root(eps, 1000, a, b, f);
    }

    public static double root(double eps, int maxIterations, double x1, double x2, AbstractMatchingEstimator f) {
        double fx1 = f.f(x1);
        double fx2 = f.f(x2);
        double halfEps = eps * 0.5;

        if (fx1 * fx2 >= 0) {
            throw new ArithmeticException("The given interval does not appear to bracket the root");
        }

        double dif = 1;//Measure the change interface values
        while (Math.abs(x1 - x2) > eps && maxIterations-- > 0) {
            double x3 = (x1 + x2) * 0.5;
            double fx3 = f.f(x3);

            double x4 = x3 + (x3 - x1) * Math.signum(fx1 - fx2) * fx3 / Math.sqrt(fx3 * fx3 - fx1 * fx2);
            double fx4 = f.f(x4);
            
            if (fx3 * fx4 < 0) {
                x1 = x3;
                fx1 = fx3;
                x2 = x4;
                fx2 = fx4;
            } else if (fx1 * fx4 < 0) {
                dif = Math.abs(x4 - x2);
                if (dif <= halfEps)//WE are no longer updating, return the value
                {
                    return x4;
                }
                x2 = x4;
                fx2 = fx4;
            } else {
                dif = Math.abs(x4 - x1);
                if (dif <= halfEps)//WE are no longer updating, return the value
                {
                    return x4;
                }
                x1 = x4;
                fx1 = fx4;
            }
//            double err=Math.abs(x1 - x2);
//            System.out.println("Err="+err);
        }
        return x2;
    }

    public double root(double eps, int maxIterations, double[] initialGuesses, AbstractMatchingEstimator f) {
        return root(eps, maxIterations, initialGuesses[0], initialGuesses[1], f);
    }

    public double root(double eps, int maxIterations, double[] initialGuesses, AbstractMatchingEstimator f, int pos) {
        return root(eps, maxIterations, initialGuesses[0], initialGuesses[1], f);
    }
}
