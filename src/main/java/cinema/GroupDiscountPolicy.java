package cinema;

public final class GroupDiscountPolicy implements DiscountPolicy {
    private static final int MINIMUM_GROUP_SIZE = 5;
    private static final long DISCOUNT_PERCENT = 5L;

    @Override
    public long calculateDiscount(long subtotal, int seatCount) {
        if (seatCount < MINIMUM_GROUP_SIZE) {
            return 0L;
        }
        return Math.multiplyExact(subtotal, DISCOUNT_PERCENT) / 100L;
    }

    @Override
    public String name() {
        return "Group discount 5% for 5 or more seats";
    }
}
