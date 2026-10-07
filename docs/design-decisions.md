# Design Decisions

## 1. Tách PricingTier khỏi Booking

`PricingTier` là interface được triển khai bởi `StandardTier`, `PremiumTier` và `ReclinerTier`. `Booking` chỉ gọi `Seat.price()` và không cần biết từng loại ghế.

Lựa chọn này thể hiện polymorphism và cho phép thêm loại ghế mới mà không viết lại booking logic.

## 2. Showtime quản lý trạng thái ghế

Trạng thái ghế phụ thuộc vào suất chiếu, không chỉ phụ thuộc vào ghế vật lý. Vì vậy `Showtime` sở hữu map trạng thái `seatStates` và cung cấp các method `tryLockSeats`, `confirmBooking` và `releaseSeats`.

Code bên ngoài không được sửa trực tiếp map này. Đây là điểm thể hiện encapsulation.

## 3. DiscountPolicy là interface

Giảm giá được tách khỏi `Booking`. Có thể thêm `GroupDiscountPolicy` hoặc `HolidayDiscountPolicy` mà không làm thay đổi cách booking tính subtotal.

## 4. Khóa ghế đồng thời

Trong phiên bản console, `tryLockSeats` được đánh dấu `synchronized`, nên việc kiểm tra ghế còn trống và chuyển sang `LOCKED` được thực hiện nguyên tử trong cùng một JVM.

Trong hệ thống thực tế có nhiều server, cần dùng database transaction, row lock hoặc unique constraint trên cặp `(showtime_id, seat_id)`.

## 5. GroupDiscountPolicy

`GroupDiscountPolicy` giảm 5% khi booking có từ 5 ghế trở lên. Quy tắc này được cài trong một implementation riêng của `DiscountPolicy`, nên không cần thêm điều kiện vào `Booking`.

## 6. Validation

Các constructor và method kiểm tra dữ liệu đầu vào như ID rỗng, thời lượng phim không hợp lệ, ghế không tồn tại, booking không có ghế và screen có ghế trùng ID. Validation được đặt gần nơi dữ liệu được tạo hoặc thay đổi để lỗi xuất hiện sớm.
