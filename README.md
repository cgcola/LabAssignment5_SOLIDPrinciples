# SOLID Principles Refactoring: Order Management System

## Problem Description:
This repository demonstrates the refactoring of a Order Management System to adhere to **SOLID Object-Oriented Design Principles**. 

In the initial implementation, the system relied on a single, monolithic interface (`Order`) and a single concrete class (`OrderAction`) to handle every step of processing an order. This included calculating totals, placing the order, generating invoice PDFs, and sending email notifications. Furthermore, the test class explicitly noted that certain steps (like generating an invoice or sending an email) were optional and might not be needed for all orders.

## SOLID Principles Violated

The original code violated several key SOLID principles:

1. **Single Responsibility Principle (SRP):** The `OrderAction` class had too many reasons to change. It was responsible for mathematical calculations, database/system placements, file generation, and notification (emails).
2. **Interface Segregation Principle (ISP):** The `Order` interface was a "fat" interface. It forced clients to implement methods (`generateInvoice`, `sendEmailNotification`) even if the specific type of order they were processing did not require an invoice or an email.
3. **Dependency Inversion Principle (DIP):** The client (`OrderTest`) directly instantiated the concrete `OrderAction` class rather than relying on abstractions to coordinate the workflow.

## How We Refactored the Code

To resolve these violations and create a highly cohesive, loosely coupled system, we applied the following structural changes:

1. **Applied ISP (Interface Splitting):** The monolithic `Order` interface was split into four distinct, highly-focused interfaces: `PriceCalculator`, `OrderPlacer`, `InvoiceGenerator`, and `EmailNotifier`.
2. **Applied SRP (Class Splitting):** The `OrderAction` class was broken down into four separate concrete classes (`OrderCalculation`, `OrderPlacement`, `InvoiceAction`, `EmailNotification`). Each class now has only one responsibility.
3. **Applied DIP & Facade Pattern (`OrderManager`):** We introduced an `OrderManager` class to orchestrate the workflow. It uses **Constructor Injection** to accept the interfaces as dependencies rather than tightly coupling to the concrete classes. It also provides overloaded constructors so basic orders can be processed without requiring the optional invoice or email.
4. ** The main class now acts as the Composition Root, instantiating the specific concrete tools and injecting them into the `OrderManager` to execute the system.

---

## Code Comparison

### Original Code (Violates SOLID):

```java
public interface Order {
  void calculateTotal(double price, int quantity);
  void placeOrder(String customerName, String address);
  void generateInvoice(String fileName);
  void sendEmailNotification(String email);
}

public class OrderAction implements Order {
  @Override
  public void calculateTotal(double price, int quantity) {
    double total = price * quantity;
    System.out.println("Order total: $" + total);
  }

  @Override
  public void placeOrder(String customerName, String address) {
    System.out.println("Order placed for " + customerName + " at " + address);
  }

  @Override
  public void generateInvoice(String fileName) {
    System.out.println("Invoice generated: " + fileName);
  }

  @Override
  public void sendEmailNotification(String email) {
    System.out.println("Email notification sent to: " + email);
  }
}

public class OrderTest {
  public static void main(String[] args) {
    Order order = new OrderAction();
    order.calculateTotal(10.0, 2);
    order.placeOrder("John Doe", "123 Main St");
    
    // These methods might not be needed for all orders
    order.generateInvoice("order_123.pdf");
    order.sendEmailNotification("johndoe@example.com");
  }
}
```

### Refactored Code (Adheres to SOLID):

```java
// Interfaces (ISP)
public interface PriceCalculator {
    double calculateTotal(double price, int quantity);
}
public interface OrderPlacer {
    void placeOrder(String customerName, String address);
}
public interface InvoiceGenerator {
    void generateInvoice(String fileName);
}
public interface EmailNotifier {
    void sendEmailNotification(String email);
}

// Concrete Implementations (SRP)
public class OrderCalculation implements PriceCalculator {
    @Override
    public double calculateTotal(double price, int quantity) {
        double total = price * quantity;
        System.out.println("Order Total: $" + total);
        return total;
    }
}

public class OrderPlacement implements OrderPlacer {
    @Override
    public void placeOrder(String customerName, String address) {
        System.out.println("Order placed for " + customerName + " at " + address);
    }
}

public class InvoiceAction implements InvoiceGenerator {
    @Override
    public void generateInvoice(String fileName) {
        System.out.println("Invoice generated: " + fileName);
    }
}

public class EmailNotification implements EmailNotifier {
    @Override
    public void sendEmailNotification(String email) {
        System.out.println("Email notification sent to: " + email);
    }
}

// Orchestrator (DIP)
public class OrderManager {
    private PriceCalculator priceCalculator;
    private OrderPlacer orderPlacer;
    private InvoiceGenerator invoiceGenerator;
    private EmailNotifier emailNotifier;

    // The Full Constructor
    public OrderManager(PriceCalculator priceCalculator, OrderPlacer orderPlacer, 
                        InvoiceGenerator invoiceGenerator, EmailNotifier emailNotifier) {
        this.priceCalculator = priceCalculator;
        this.orderPlacer = orderPlacer;
        this.invoiceGenerator = invoiceGenerator;
        this.emailNotifier = emailNotifier;
    }

    // The Basic Constructor
    public OrderManager(PriceCalculator priceCalculator, OrderPlacer orderPlacer) {
        this.priceCalculator = priceCalculator;
        this.orderPlacer = orderPlacer;
    }

    // Process Full Order
    public void processStandardOrder(double price, int quantity, String customerName,
            String address, String fileName, String email) {
        
        // Mandatory steps
        priceCalculator.calculateTotal(price, quantity);
        orderPlacer.placeOrder(customerName, address);

        // Optional steps
        if (invoiceGenerator != null) {
            invoiceGenerator.generateInvoice(fileName);
        }
        if (emailNotifier != null) {
            emailNotifier.sendEmailNotification(email);
        }
    }

    // Process Basic Order
    public void processStandardOrder(double price, int quantity, String customerName, String address) {
        priceCalculator.calculateTotal(price, quantity);
        orderPlacer.placeOrder(customerName, address);
    }
}

public class OrderTest {
    public static void main(String[] args) {
        PriceCalculator priceCalculator = new OrderCalculation();
        OrderPlacer orderPlacer = new OrderPlacement();
        InvoiceGenerator invoiceGenerator = new InvoiceAction();
        EmailNotifier emailNotifier = new EmailNotification();

        OrderManager fullManager = new OrderManager(priceCalculator, orderPlacer, invoiceGenerator, emailNotifier);
        fullManager.processStandardOrder(10.0, 2, "John Doe", "123 Main St", "order_123.pdf", "johndoe@example.com");
    }
}
```

---

## UML Diagram:
<img width="5492" height="2466" alt="image" src="https://github.com/user-attachments/assets/c97d61e9-7f6c-4bb2-aa71-b3a61020eb0f" />
