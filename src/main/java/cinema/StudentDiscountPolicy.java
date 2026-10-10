package cinema;

public final class StudentDiscountPolicy implements DiscountPolicy {
    @Override
    public long calculateDiscount(long subtotal, int seatCount) {
        return subtotal / 10;
    }

    @Override
    public String name() {
        return "Student discount 10%";
    }
}
