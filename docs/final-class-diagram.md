# Class diagram — phiên bản tích hợp tuần 4

Đối chiếu source tại commit `962cf35`, sau khi tích hợp Member discount của Thắng và Luxury tier của Triệu. Sơ đồ liệt kê các field/method chính; constructor, một số getter và tham số kiểu của Map được lược bớt. Đây là tài liệu UML, không thay thế báo cáo tổng kết.

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
        -Map seats
        +addSeat(Seat) void
        +getSeat(String) Seat
        +getSeats() Collection~Seat~
    }
    class Seat {
        -String seatId
        -PricingTier pricingTier
        +price(long) long
        +getPricingTier() PricingTier
    }
    class PricingTier {
        <<interface>>
        +calculatePrice(long) long
        +name() String
    }
    class StandardTier
    class PremiumTier
    class ReclinerTier
    class LuxuryTier
    class DiscountPolicy {
        <<interface>>
        +calculateDiscount(long, int) long
        +name() String
    }
    class NoDiscountPolicy
    class StudentDiscountPolicy
    class GroupDiscountPolicy
    class MemberDiscountPolicy
    class Showtime {
        -Movie movie
        -Screen screen
        -LocalDateTime startTime
        -Clock clock
        -Duration lockDuration
        -Map seatStates
        -Map seatLocks
        -Set~String~ usedBookingIds
        +tryLockSeats(Set~String~, String) void
        +confirmBooking(String) void
        +releaseSeats(String) void
        +isSeatAvailable(String) boolean
        +getSeatStates() Map
        -releaseExpiredLocks(Instant) void
    }
    class SeatLock {
        <<record>>
        +bookingId() String
        +expiresAt() Instant
        +isOwnedBy(String) boolean
        +isExpiredAt(Instant) boolean
    }
    class SeatState {
        <<enumeration>>
        AVAILABLE
        LOCKED
        BOOKED
    }
    class Booking {
        -String bookingId
        -Showtime showtime
        -List~Seat~ selectedSeats
        -DiscountPolicy discountPolicy
        -BookingStatus status
        +calculateSubtotal() long
        +calculateDiscount() long
        +calculateTotal() long
        +generateConfirmation() String
        +getStatus() BookingStatus
        ~markConfirmed() void
        ~markCancelled() void
        ~markExpired() void
    }
    class BookingStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        CANCELLED
        EXPIRED
    }
    class BookingService {
        +createBooking(String, Showtime, Set~String~, DiscountPolicy) Booking
        +confirmBooking(Booking) void
        +cancelBooking(Booking) void
    }
    class BookingHistory {
        -Path directory
        +save(Booking) void
        +find(String) Optional~String~
    }
    class CinemaConsole {
        -List~Showtime~ showtimes
        -BookingService service
        -BookingHistory history
        -InputStream input
        -PrintStream output
        +run() void
    }
    class Main {
        +main(String[]) void
        -runInteractive(String[]) void
    }

    PricingTier <|.. StandardTier
    PricingTier <|.. PremiumTier
    PricingTier <|.. ReclinerTier
    PricingTier <|.. LuxuryTier
    DiscountPolicy <|.. NoDiscountPolicy
    DiscountPolicy <|.. StudentDiscountPolicy
    DiscountPolicy <|.. GroupDiscountPolicy
    DiscountPolicy <|.. MemberDiscountPolicy
    Screen o-- Seat : layout
    Seat --> PricingTier : delegates pricing
    Showtime --> Movie
    Showtime --> Screen
    Showtime *-- SeatLock : owns active locks
    Showtime --> SeatState
    Booking o-- Seat : selected seats
    Booking --> Showtime
    Booking --> DiscountPolicy : delegates discount
    Booking --> BookingStatus
    BookingService ..> Booking : creates and transitions
    BookingService ..> Showtime : locks confirms releases
    BookingHistory ..> Booking : stores confirmation
    CinemaConsole --> Showtime : browses
    CinemaConsole --> BookingService
    CinemaConsole --> BookingHistory
    Main ..> CinemaConsole
    Main ..> BookingService
    Main ..> BookingHistory
    Main ..> LuxuryTier : seeds A6
```

## Quan hệ và trách nhiệm

- **Polymorphism:** `Seat.price()` gọi `PricingTier.calculatePrice()`; `Booking.calculateDiscount()` gọi `DiscountPolicy.calculateDiscount()`. Luxury và Member thêm công thức mới mà không sửa hai call site này.
- **Encapsulation:** `Showtime` giữ các map state/lock và tập ID đã dùng là private. Các method đọc/thay đổi ghế được đồng bộ trên cùng Showtime; snapshot `Map.copyOf()` không cho caller tự sửa ghế. `Booking` giữ status private và chỉ chuyển trạng thái từ PENDING.
- **Object composition/has-a:** Screen giữ layout Seat; Booking giữ collection ghế đã chọn; Showtime sở hữu state/lock theo từng mã ghế. Hình thoi rỗng ở Seat biểu thị reference được chia sẻ: hủy Booking không hủy ghế vật lý. Không diễn giải thành strict lifecycle composition giữa Showtime và các Seat của Screen.
- **SRP:** Console lo nhập/xuất; service điều phối; Booking tính tiền và giữ trạng thái booking; Showtime giữ invariant ghế; BookingHistory lo file. Policy và tier không khóa ghế hoặc lưu file.
- **Phạm vi extension:** Main bổ sung dữ liệu A6/Luxury; CinemaConsole thêm lựa chọn Member. Hai thay đổi này là wiring. Không tuyên bố mọi class cũ đều không đổi; các class nghiệp vụ và interface cũ được giữ nguyên, được kiểm tra trong [diff tích hợp](../evidence/week-4-integration-extensibility-diff.txt).

## Luồng và giới hạn

`createBooking()` tạo Booking rồi giữ ghế; `confirmBooking()` chuyển ghế sang BOOKED và booking sang CONFIRMED; `generateConfirmation()` chỉ hoạt động sau xác nhận. Cancel chỉ áp dụng cho PENDING; confirm sau deadline làm booking EXPIRED. Mốc hết hạn được tính bằng Clock/Duration, không có background timer.

Monitor của Booking bao trọn confirm/cancel; thứ tự khóa nội bộ là Booking rồi Showtime. Bảo vệ này cần các caller dùng chung object trong một JVM. File confirmation không khôi phục inventory/lock khi restart và chưa có transaction chung giữa xác nhận ghế với lưu file.

Sơ đồ [baseline tuần 1](class-diagram.md) và UML trong [báo cáo tuần 3](weekly-report-week-3.md#6-uml-cập-nhật-tuần-3) được giữ làm tài liệu lịch sử.
