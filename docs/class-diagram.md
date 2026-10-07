# Class Diagram

```mermaid
classDiagram
    class Movie {
        -String movieId
        -String title
        -int durationMinutes
    }

    class Screen {
        -String screenId
        -String name
        -List~Seat~ seats
        +addSeat(Seat seat)
    }

    class Seat {
        -String seatId
        -PricingTier pricingTier
        +price(double basePrice) double
    }

    class PricingTier {
        <<interface>>
        +calculatePrice(double basePrice) double
        +name() String
    }

    class StandardTier
    class PremiumTier
    class ReclinerTier

    class Showtime {
        -Movie movie
        -Screen screen
        -LocalDateTime startTime
        -Map~String, SeatState~ seatStates
        +tryLockSeats(Set~String~ seatIds, String bookingId)
        +confirmBooking(String bookingId)
        +releaseSeats(String bookingId)
        +isSeatAvailable(String seatId) boolean
    }

    class Booking {
        -String bookingId
        -Showtime showtime
        -List~Seat~ selectedSeats
        -DiscountPolicy discountPolicy
        +calculateTotal() double
        +generateConfirmation() String
    }

    class DiscountPolicy {
        <<interface>>
        +calculateDiscount(double subtotal, int seatCount) double
        +name() String
    }

    class StudentDiscountPolicy
    class GroupDiscountPolicy

    Movie "1" --> "*" Showtime
    Screen "1" --> "*" Seat
    Screen "1" --> "*" Showtime
    Showtime "1" --> "*" Seat
    Seat "*" --> "1" PricingTier
    Booking "*" --> "1" Showtime
    Booking "1" --> "*" Seat
    Booking "1" --> "1" DiscountPolicy

    PricingTier <|.. StandardTier
    PricingTier <|.. PremiumTier
    PricingTier <|.. ReclinerTier
    DiscountPolicy <|.. StudentDiscountPolicy
    DiscountPolicy <|.. GroupDiscountPolicy
```

## Main responsibilities

- `Movie`: lưu thông tin phim.
- `Screen`: sở hữu danh sách ghế vật lý của một phòng chiếu.
- `Seat`: đại diện cho một ghế và loại giá của ghế đó.
- `Showtime`: gắn phim với phòng chiếu, đồng thời quản lý trạng thái ghế của riêng suất chiếu.
- `Booking`: chứa các ghế đã chọn, tính tổng tiền và tạo confirmation summary.
- `PricingTier`: cung cấp hành vi tính giá theo loại ghế.
- `DiscountPolicy`: cung cấp hành vi tính giảm giá.
