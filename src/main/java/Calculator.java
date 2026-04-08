/**
 * A simple Calculator class that performs basic arithmetic operations.
 * This calculator supports addition, subtraction, multiplication, division,
 * modulo, power, and square root operations.
 */
public class Calculator {
    
    /**
     * Adds two numbers.
     * 
     * @param a the first number
     * @param b the second number
     * @return the sum of a and b
     */
    public double add(double a, double b) {
        return a + b;
    }
    
    /**
     * Subtracts the second number from the first number.
     * 
     * @param a the first number
     * @param b the second number
     * @return the difference of a and b
     */
    public double subtract(double a, double b) {
        return a - b;
    }
    
    /**
     * Multiplies two numbers.
     * 
     * @param a the first number
     * @param b the second number
     * @return the product of a and b
     */
    public double multiply(double a, double b) {
        return a * b;
    }
    
    /**
     * Divides the first number by the second number.
     * 
     * @param a the numerator
     * @param b the denominator
     * @return the quotient of a divided by b
     * @throws ArithmeticException if b is zero
     */
    public double divide(double a, double b) {
        if (Math.abs(b) < 1e-10) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }
    
    /**
     * Calculates the modulo (remainder) of two numbers.
     * 
     * @param a the dividend
     * @param b the divisor
     * @return the remainder of a divided by b
     * @throws ArithmeticException if b is zero
     */
    public double modulo(double a, double b) {
        if (Math.abs(b) < 1e-10) {
            throw new ArithmeticException("Cannot calculate modulo with zero");
        }
        return a % b;
    }
    
    /**
     * Calculates the power of a number.
     * 
     * @param base the base number
     * @param exponent the exponent
     * @return base raised to the power of exponent
     */
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }
    
    /**
     * Calculates the square root of a number.
     * 
     * @param a the number
     * @return the square root of a
     * @throws IllegalArgumentException if a is negative
     */
    public double squareRoot(double a) {
        if (a < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of negative number");
        }
        return Math.sqrt(a);
    }
}
