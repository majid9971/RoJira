/**
 * Main class to demonstrate the Calculator functionality.
 */
public class Main {
    
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        
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
        System.out.println("\n=== Additional Examples ===\n");
        System.out.println("20 + 15 = " + calculator.add(20, 15));
        System.out.println("50 - 30 = " + calculator.subtract(50, 30));
        System.out.println("7 * 8 = " + calculator.multiply(7, 8));
        System.out.println("100 / 4 = " + calculator.divide(100, 4));
        System.out.println("17 % 5 = " + calculator.modulo(17, 5));
        System.out.println("2 ^ 10 = " + calculator.power(2, 10));
        System.out.println("√25 = " + calculator.squareRoot(25));
        
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
    }
}
