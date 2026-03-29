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