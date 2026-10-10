package cinema;

public interface DiscountPolicy {
    long calculateDiscount(long subtotal, int seatCount);

    String name();
}
