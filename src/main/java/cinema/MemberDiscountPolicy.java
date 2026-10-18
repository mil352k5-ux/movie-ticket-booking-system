package cinema;

public final class MemberDiscountPolicy implements DiscountPolicy {
    @Override
    public long calculateDiscount(long subtotal, int seatCount) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal must not be negative.");
        }
        // Splitting the amount preserves floor rounding without overflowing subtotal * 15.
        return (subtotal / 100) * 15 + ((subtotal % 100) * 15) / 100;
    }

    @Override
    public String name() {
        return "Member discount 15%";
    }
}
