# RoJira

## Basic Calculator

This repository contains a simple Java calculator that performs basic arithmetic operations.

### Features

The calculator supports the following operations:
- **Addition**: Add two numbers
- **Subtraction**: Subtract one number from another
- **Multiplication**: Multiply two numbers
- **Division**: Divide one number by another (with divide-by-zero protection)
- **Modulo**: Calculate the remainder of division
- **Power**: Raise a number to a power
- **Square Root**: Calculate the square root of a number

### Project Structure

```
src/
└── main/
    └── java/
        ├── Calculator.java  # Calculator class with arithmetic operations
        └── Main.java        # Demo application showing calculator usage
```

### How to Use

#### Compile the code:
```bash
cd src/main/java
javac Calculator.java Main.java
```

#### Run the demo:
```bash
java Main
```

#### Use in your own code:
```java
Calculator calculator = new Calculator();

// Perform calculations
double sum = calculator.add(10, 5);        // 15.0
double diff = calculator.subtract(10, 5);  // 5.0
double product = calculator.multiply(10, 5); // 50.0
double quotient = calculator.divide(10, 5);  // 2.0
double remainder = calculator.modulo(10, 5); // 0.0
double power = calculator.power(2, 3);       // 8.0
double sqrt = calculator.squareRoot(16);     // 4.0
```

### Error Handling

The calculator includes proper error handling:
- **Division by zero**: Throws `ArithmeticException`
- **Negative square root**: Throws `IllegalArgumentException`

Example:
```java
try {
    calculator.divide(10, 0);
} catch (ArithmeticException e) {
    System.out.println("Error: " + e.getMessage());
}
```