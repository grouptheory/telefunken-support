/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.utils;

/**
 *
 * @author Bilal Khan
 */
public class Assertion {
    
    public static void test(boolean cond, String message) {
        if (!cond) throw new RuntimeException(message);
    }
}
