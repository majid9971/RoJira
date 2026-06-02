/**
 * Main class to demonstrate the Calculator functionality.
 * Supports both basic and scientific calculator modes.
 */
public class Main {
    
    public static void main(String[] args) {
        // Toggle for scientific calculator mode
        boolean useScientificMode = args.length > 0 && args[0].equalsIgnoreCase("scientific");
        
        Calculator calculator = useScientificMode ? new ScientificCalculator() : new Calculator();
        
        System.out.println("=== Basic Calculator Demo ===\n");
        
        // Addition
        double num1 = 10.0;
        double num2 = 5.0;
        System.out.println("Addition: " + num1 + " + " + num2 + " = " + calculator.add(num1, num2));
        
        // Subtraction
        System.out.println("Subtraction: " + num1 + " - " + num2 + " = " + calculator.subtract(num1, num2));
        
        // Multiplication
        System.out.println("Multiplication: " + num1 + " * " + num2 + " = " + calculator.multiply(num1, num2));
        
        // Division
        System.out.println("Division: " + num1 + " / " + num2 + " = " + calculator.divide(num1, num2));
        
        // Modulo
        System.out.println("Modulo: " + num1 + " % " + num2 + " = " + calculator.modulo(num1, num2));
        
        // Power
        System.out.println("Power: " + num1 + " ^ " + num2 + " = " + calculator.power(num1, num2));
        
        // Square Root
        double num3 = 16.0;
        System.out.println("Square Root: √" + num3 + " = " + calculator.squareRoot(num3));
        
        // More examples
        System.out.println("\n=== Additional Example ===\n");
        System.out.println("20 + 15 = " + calculator.add(20, 15));
        System.out.println("50 - 30 = " + calculator.subtract(50, 30));
        System.out.println("7 * 8 = " + calculator.multiply(7, 8));
        System.out.println("100 / 4 = " + calculator.divide(100, 4));
        
        // Demonstrate error handling
        System.out.println("\n=== Error Handling Examples ===\n");
        try {
            calculator.divide(10, 0);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        try {
            calculator.squareRoot(-4);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n=== Calculator Demo Complete ===");
        
        // Scientific Calculator Demo
        if (useScientificMode) {
            demonstrateScientificCalculator();
        }
    }
    
    /**
     * Demonstrates the scientific calculator functionality.
     */
    public static void demonstrateScientificCalculator() {
        ScientificCalculator sciCalc = new ScientificCalculator();
        
        System.out.println("\n\n=== Scientific Calculator Demo ===\n");
        
        // Trigonometric functions (using radians)
        System.out.println("--- Trigonometric Functions ---");
        double angle = Math.PI / 4; // 45 degrees
        System.out.println("sin(π/4) = " + sciCalc.sine(angle));
        System.out.println("cos(π/4) = " + sciCalc.cosine(angle));
        System.out.println("tan(π/4) = " + sciCalc.tangent(angle));
        
        // Degree to Radian conversion
        System.out.println("\n--- Angle Conversion ---");
        double degrees = 45.0;
        double radians = sciCalc.degreesToRadians(degrees);
        System.out.println(degrees + "° = " + radians + " radians");
        System.out.println("sin(45°) = " + sciCalc.sine(radians));
        
        // Logarithmic functions
        System.out.println("\n--- Logarithmic Functions ---");
        double num = 100.0;
        System.out.println("ln(" + num + ") = " + sciCalc.naturalLogarithm(num));
        System.out.println("log10(" + num + ") = " + sciCalc.logarithm10(num));
        
        // Exponential function
        System.out.println("\n--- Exponential Functions ---");
        System.out.println("e^2 = " + sciCalc.exponential(2.0));
        System.out.println("e^(-1) = " + sciCalc.exponential(-1.0));
        
        // Factorial
        System.out.println("\n--- Factorial ---");
        System.out.println("5! = " + sciCalc.factorial(5));
        System.out.println("10! = " + sciCalc.factorial(10));
        
        // Inverse trigonometric functions
        System.out.println("\n--- Inverse Trigonometric Functions ---");
        double value = 0.5;
        System.out.println("arcsin(0.5) = " + sciCalc.arcsine(value) + " radians");
        System.out.println("arcsin(0.5) = " + sciCalc.radiansToDegrees(sciCalc.arcsine(value)) + "°");
        
        // Absolute value
        System.out.println("\n--- Absolute Value ---");
        System.out.println("|-25.5| = " + sciCalc.absolute(-25.5));
        
        System.out.println("\n=== Scientific Calculator Demo Complete ===");
    }
}
