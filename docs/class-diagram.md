# Class Diagram - Baseline tuần 1

Sơ đồ dưới đây mô tả source code hiện tại, không đưa các class dự kiến của tuần sau vào thiết kế đã hoàn thành.

```mermaid
classDiagram
    class Movie {
        -String movieId
        -String title
        -int durationMinutes
        +getMovieId() String
        +getTitle() String
        +getDurationMinutes() int
    }

    class Screen {
        -String screenId
        -String name
        -Map~String, Seat~ seats
        +addSeat(Seat seat)
        +getSeat(String seatId) Seat
        +getSeats() Collection~Seat~
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

    class SeatState {
        <<enumeration>>
        AVAILABLE
        LOCKED
        BOOKED
    }

    class Showtime {
        -Movie movie
        -Screen screen
        -LocalDateTime startTime
        -Map~String, SeatState~ seatStates
        -Map~String, String~ seatLockOwners
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
        +calculateSubtotal() double
        +calculateDiscount() double
        +calculateTotal() double
        +generateConfirmation() String
    }

    class DiscountPolicy {
        <<interface>>
        +calculateDiscount(double subtotal, int seatCount) double
        +name() String
    }

    class StudentDiscountPolicy
    class NoDiscountPolicy

    Screen "1" o-- "*" Seat : contains
    Showtime "*" --> "1" Movie : shows
    Showtime "*" --> "1" Screen : uses
    Showtime "1" *-- "*" SeatState : owns per-seat state
    Booking "*" --> "1" Showtime : belongs to
    Booking "1" o-- "1..*" Seat : selected seats
    Seat "*" --> "1" PricingTier : delegates pricing
    Booking "*" --> "1" DiscountPolicy : applies

    PricingTier <|.. StandardTier
    PricingTier <|.. PremiumTier
    PricingTier <|.. ReclinerTier
    DiscountPolicy <|.. StudentDiscountPolicy
    DiscountPolicy <|.. NoDiscountPolicy
```

## Trách nhiệm chính

- `Movie`: lưu thông tin nhận diện, tiêu đề và thời lượng phim.
- `Screen`: quản lý layout ghế vật lý và ngăn mã ghế bị trùng.
- `Seat`: đại diện một ghế và ủy quyền việc tính giá cho `PricingTier`.
- `PricingTier`: abstraction cho thuật toán tính giá; ba implementation tạo ba mức giá.
- `Showtime`: gắn phim, phòng và thời gian; sở hữu trạng thái ghế theo riêng suất chiếu và quản lý lock owner.
- `Booking`: giữ bản sao danh sách ghế đã chọn, tính subtotal/discount/total và tạo confirmation.
- `DiscountPolicy`: abstraction cho quy tắc giảm giá; baseline có student discount và lựa chọn không giảm giá.

## Lưu ý về composition

Trong source tuần 1, `Showtime` không trực tiếp chứa collection `Seat`; nó tham chiếu `Screen`, đọc các seat của screen khi khởi tạo và sở hữu `seatStates`/`seatLockOwners` theo từng mã ghế. Vì vậy sơ đồ chỉ đánh dấu composition đối với state theo suất chiếu, không tuyên bố `Showtime` sở hữu vòng đời của các `Seat` vật lý.

`Booking` tạo một `ArrayList` riêng cho `selectedSeats`, nhưng các phần tử vẫn là reference đến `Seat` từ `Screen`. Đây là object composition theo nghĩa booking được cấu thành từ các ghế đã chọn, không phải ownership tuyệt đối về vòng đời. Điểm này cần được trình bày đúng khi bảo vệ.
