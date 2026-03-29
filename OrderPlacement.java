public class OrderPlacement implements OrderPlacer {

    // Simulate placing order in a system
    @Override
    public void placeOrder(String customerName, String address) {
        System.out.println("Order placed for " + customerName + " at " + address);
    }
}