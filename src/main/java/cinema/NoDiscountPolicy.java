package cinema;

public final class NoDiscountPolicy implements DiscountPolicy {
    @Override
    public double calculateDiscount(double subtotal, int seatCount) {
        return 0;
    }

    @Override
    public String name() {
        return "No discount";
    }
}
