package cinema;

public final class GroupDiscountPolicy implements DiscountPolicy {
    private static final int MINIMUM_SEATS = 5;
    private static final double DISCOUNT_RATE = 0.05;

    @Override
    public double calculateDiscount(double subtotal, int seatCount) {
        return seatCount >= MINIMUM_SEATS ? subtotal * DISCOUNT_RATE : 0;
    }

    @Override
    public String name() {
        return "Group discount 5% for 5+ seats";
    }
}
