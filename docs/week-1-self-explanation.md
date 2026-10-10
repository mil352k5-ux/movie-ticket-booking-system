# Self-Explanation - Tuần 1

## 1. Polymorphism trong seat-tier pricing

Các class/method liên quan:

- `PricingTier.calculatePrice(double basePrice)`
- `StandardTier.calculatePrice()`
- `PremiumTier.calculatePrice()`
- `ReclinerTier.calculatePrice()`
- `Seat.price(double basePrice)`
- `Booking.calculateSubtotal()`

`Seat` chỉ giữ một reference có kiểu `PricingTier`. Khi `Seat.price()` gọi `pricingTier.calculatePrice(basePrice)`, Java dispatch lời gọi đến implementation thực tế: Standard giữ nguyên giá, Premium nhân 1,25 và Recliner nhân 1,50. `Booking.calculateSubtotal()` chỉ gọi `Seat.price()` cho mọi ghế, không chứa `if/else` hoặc `switch` để nhận biết từng loại ghế.

Đây là công cụ phù hợp vì loại ghế là phần thay đổi độc lập với quy trình booking. Nếu bổ sung một tier mới, nhóm có thể thêm một class implements `PricingTier` mà không viết lại thuật toán cộng tiền của `Booking`.

## 2. Encapsulation trong seat locking

Các field/method liên quan:

- `Showtime.seatStates`
- `Showtime.seatLockOwners`
- `Showtime.tryLockSeats()`
- `Showtime.confirmBooking()`
- `Showtime.releaseSeats()`
- `Showtime.isSeatAvailable()`
- `Showtime.getSeatStates()`

Hai map chứa trạng thái và lock owner là `private`. Code bên ngoài không thể tự chuyển một ghế từ `BOOKED` về `AVAILABLE`. Mọi thay đổi phải đi qua method của `Showtime`. `getSeatStates()` trả về unmodifiable view để người gọi chỉ đọc.

`tryLockSeats()` kiểm tra toàn bộ seat ID trước, sau đó mới đổi trạng thái của cả nhóm ghế. Method được đánh dấu `synchronized`, vì vậy bước kiểm tra và cập nhật không bị hai thread trong cùng JVM xen giữa. `confirmBooking()` chỉ chuyển những ghế có owner trùng booking ID, còn `releaseSeats()` chỉ giải phóng ghế vẫn đang `LOCKED`.

Đây là công cụ phù hợp vì tính hợp lệ của trạng thái ghế phụ thuộc vào suất chiếu. Nếu nhiều class được sửa map trực tiếp, hệ thống có thể bỏ qua validation và tạo double-booking.

## 3. Composition giữa các domain object

Các class/field liên quan:

- `Screen.seats`
- `Showtime.screen`
- `Showtime.seatStates`
- `Booking.selectedSeats`
- Constructor của `Showtime`
- Constructor của `Booking`

`Screen` được cấu thành từ các `Seat` trong một map. Khi tạo `Showtime`, constructor đọc các seat của `Screen` và tạo trạng thái `AVAILABLE` tương ứng cho riêng suất chiếu đó. Điều này giúp cùng một ghế vật lý có trạng thái khác nhau ở các suất chiếu khác nhau.

`Booking` tạo một `ArrayList` riêng chứa các `Seat` đã chọn. Nhờ đó booking có thể tính giá và sinh confirmation từ các object ghế thay vì chỉ giữ một chuỗi mô tả không có hành vi.

Tuy nhiên, baseline tuần 1 chưa thể hiện strict composition `Showtime`–`Seat`: `Showtime` tham chiếu `Screen` và không sở hữu trực tiếp collection `Seat`. Các `Seat` trong `Booking` cũng là reference đến object của `Screen`. Nhóm cần mô tả đây là object composition/has-a ở mức thiết kế hiện tại và có thể cải tiến bằng collection hoặc value object theo suất chiếu nếu rubric yêu cầu ownership rõ hơn.

## 4. Strategy cho discount

Các class/method liên quan:

- `DiscountPolicy.calculateDiscount(double subtotal, int seatCount)`
- `StudentDiscountPolicy.calculateDiscount()`
- `NoDiscountPolicy.calculateDiscount()`
- `Booking.calculateDiscount()`

`Booking` nhận một `DiscountPolicy` qua constructor và gọi cùng một method bất kể policy cụ thể. Student policy giảm 10%, còn no-discount trả về 0. Quy tắc giảm giá được tách khỏi trách nhiệm quản lý ghế và tạo confirmation của booking.

Thiết kế này phù hợp vì discount thường thay đổi theo chương trình khuyến mãi. Một group discount có thể được thêm bằng implementation mới mà không thêm điều kiện trực tiếp vào `Booking`.

## 5. Double-booking và giới hạn đồng thời

Trong demo tuần 1, B001 khóa A1 trước. Khi B002 gọi `tryLockSeats(Set.of("A1"), "B002")`, `isSeatAvailable()` trả về false và `Showtime` ném `IllegalStateException`. Đây là validation scenario tối thiểu.

Nếu hai user gọi đồng thời trong cùng JVM, `synchronized tryLockSeats()` đảm bảo chỉ một lời gọi kiểm tra/cập nhật tại một thời điểm. Nếu hệ thống chạy trên nhiều JVM/server, khóa Java không đủ; phiên bản production cần database transaction, row-level lock hoặc unique constraint trên cặp `(showtime_id, seat_id)`.
