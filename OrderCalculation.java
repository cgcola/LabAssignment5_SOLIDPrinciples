public class OrderCalculation implements PriceCalculator {
    @Override
    public double calculateTotal(double price, int quantity) {
        double total = price * quantity;
        System.out.println("Order Total: $" + total);
        return total;
    }
    
}
