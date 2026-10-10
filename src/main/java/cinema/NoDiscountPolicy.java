package cinema;

public final class NoDiscountPolicy implements DiscountPolicy {
    @Override
    public long calculateDiscount(long subtotal, int seatCount) {
        return 0;
    }

    @Override
    public String name() {
        return "No discount";
    }
}
