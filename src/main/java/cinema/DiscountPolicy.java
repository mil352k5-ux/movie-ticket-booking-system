package cinema;

public interface DiscountPolicy {
    double calculateDiscount(double subtotal, int seatCount);

    String name();
}
