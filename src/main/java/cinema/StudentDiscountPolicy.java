package cinema;

public final class StudentDiscountPolicy implements DiscountPolicy {
    private static final double DISCOUNT_RATE = 0.10;

    @Override
    public double calculateDiscount(double subtotal, int seatCount) {
        return subtotal * DISCOUNT_RATE;
    }

    @Override
    public String name() {
        return "Student discount 10%";
    }
}
