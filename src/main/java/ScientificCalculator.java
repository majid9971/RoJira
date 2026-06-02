/**
 * A Scientific Calculator class that extends basic arithmetic operations
 * with advanced mathematical functions including trigonometric, logarithmic,
 * and other scientific calculations.
 */
public class ScientificCalculator extends Calculator {
    
    /**
     * Calculates the sine of an angle (in radians).
     * 
     * @param angle the angle in radians
     * @return the sine of the angle
     */
    public double sine(double angle) {
        return Math.sin(angle);
    }
    
    /**
     * Calculates the cosine of an angle (in radians).
     * 
     * @param angle the angle in radians
     * @return the cosine of the angle
     */
    public double cosine(double angle) {
        return Math.cos(angle);
    }
    
    /**
     * Calculates the tangent of an angle (in radians).
     * 
     * @param angle the angle in radians
     * @return the tangent of the angle
     */
    public double tangent(double angle) {
        return Math.tan(angle);
    }
    
    /**
     * Calculates the natural logarithm (base e) of a number.
     * 
     * @param x the number
     * @return the natural logarithm of x
     * @throws IllegalArgumentException if x is less than or equal to zero
     */
    public double naturalLogarithm(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Logarithm argument must be positive");
        }
        return Math.log(x);
    }
    
    /**
     * Calculates the logarithm base 10 of a number.
     * 
     * @param x the number
     * @return the logarithm base 10 of x
     * @throws IllegalArgumentException if x is less than or equal to zero
     */
    public double logarithm10(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Logarithm argument must be positive");
        }
        return Math.log10(x);
    }
    
    /**
     * Calculates the exponential function (e^x).
     * 
     * @param x the exponent
     * @return e raised to the power of x
     */
    public double exponential(double x) {
        return Math.exp(x);
    }
    
    /**
     * Calculates the absolute value (magnitude) of a number.
     * 
     * @param x the number
     * @return the absolute value of x
     */
    public double absolute(double x) {
        return Math.abs(x);
    }
    
    /**
     * Calculates the factorial of a number.
     * 
     * @param n the non-negative integer
     * @return the factorial of n
     * @throws IllegalArgumentException if n is negative
     */
    public long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    /**
     * Converts an angle from degrees to radians.
     * 
     * @param degrees the angle in degrees
     * @return the angle in radians
     */
    public double degreesToRadians(double degrees) {
        return Math.toRadians(degrees);
    }
    
    /**
     * Converts an angle from radians to degrees.
     * 
     * @param radians the angle in radians
     * @return the angle in degrees
     */
    public double radiansToDegrees(double radians) {
        return Math.toDegrees(radians);
    }
    
    /**
     * Calculates the inverse sine (arcsine) of a number.
     * 
     * @param x the number (must be between -1 and 1)
     * @return the arcsine in radians
     * @throws IllegalArgumentException if x is not in range [-1, 1]
     */
    public double arcsine(double x) {
        if (x < -1 || x > 1) {
            throw new IllegalArgumentException("Arcsine argument must be between -1 and 1");
        }
        return Math.asin(x);
    }
    
    /**
     * Calculates the inverse cosine (arccosine) of a number.
     * 
     * @param x the number (must be between -1 and 1)
     * @return the arccosine in radians
     * @throws IllegalArgumentException if x is not in range [-1, 1]
     */
    public double arccosine(double x) {
        if (x < -1 || x > 1) {
            throw new IllegalArgumentException("Arccosine argument must be between -1 and 1");
        }
        return Math.acos(x);
    }
    
    /**
     * Calculates the inverse tangent (arctangent) of a number.
     * 
     * @param x the number
     * @return the arctangent in radians
     */
    public double arctangent(double x) {
        return Math.atan(x);
    }
}
