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