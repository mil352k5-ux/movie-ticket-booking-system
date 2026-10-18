# Bộ câu hỏi chuẩn bị bảo vệ - Nguyễn Thành Triệu

Tài liệu này là phần chuẩn bị dựa trên source hiện tại, không phải bằng
chứng rằng đã có buổi vấn đáp độc lập.

## 1. Vì sao thêm `LuxuryTier` mà không sửa `Seat`?

`PricingTier` là interface; `Seat.price(basePrice)` chỉ gọi
`pricingTier.calculatePrice(basePrice)`. `LuxuryTier` thay implementation
và trả 175%, nên cùng một API có thể dùng tier mới mà không cần sửa `Seat`
hay `Booking`.

## 2. Vì sao phép tính Luxury không viết thẳng `basePrice * 175 / 100`?

Phép nhân có thể overflow trước khi chia. `LuxuryTier.calculatePrice` tách
phần `basePrice / 100` và phần dư, kiểm tra giới hạn trước phép nhân, rồi
dùng `Math.addExact`. Nếu kết quả không biểu diễn được bằng `long`, method
ném `ArithmeticException`, không trả giá âm.

## 3. `Seat.price()` chứng minh polymorphism thế nào?

Test giữ một danh sách `PricingTier` gồm Standard, Premium, Recliner và
Luxury; với cùng base price 100.000, kết quả lần lượt là 100.000,
125.000, 150.000 và 175.000 VND. Không có `instanceof` hay if/else theo
loại ghế.

## 4. `BookingService` chịu trách nhiệm gì?

Service điều phối workflow: tạo `Booking`, gọi `Showtime.tryLockSeats`,
confirm/cancel và chuyển trạng thái. `Booking` vẫn tính subtotal/discount/
total, giữ selected seats và tạo confirmation; service không lặp lại logic
tính tiền.

## 5. Vòng đời booking hiện tại ra sao?

Booking bắt đầu ở `PENDING`. Các chuyển hợp lệ là `PENDING -> CONFIRMED`,
`PENDING -> CANCELLED` và `PENDING -> EXPIRED`. Booking đã Confirmed không
được cancel hoặc confirm lại; các method `mark...` có phạm vi package và
kiểm tra trạng thái.

## 6. Invariant ghế được bảo vệ ở đâu?

`Showtime` giữ `seatStates` và `seatLocks` theo từng suất chiếu. Trong
`tryLockSeats`, method kiểm tra toàn bộ seat ID tồn tại và đều AVAILABLE
trước khi cập nhật bất kỳ ghế nào; vì vậy yêu cầu có ghế lỗi không để lại
partial lock. Một ghế BOOKED/LOCKED bị booking khác yêu cầu sẽ bị từ chối.

## 7. Synchronized giải quyết được gì và còn giới hạn gì?

Các method đồng bộ của `Showtime` đặt check và update trong cùng critical
section của một object, nên hai task trong cùng JVM không cùng lock một ghế.
`synchronized` không thay thế transaction hoặc distributed lock giữa nhiều
application server/process; production có thể cần database transaction,
row-level lock, unique constraint `(showtime_id, seat_id)` hoặc optimistic
locking với version.

## 8. Exception nào dùng cho lỗi input và lỗi trạng thái?

Input null/rỗng/không tồn tại như seat ID hoặc booking ID dùng
`IllegalArgumentException`. Thao tác xung đột với state như lock ghế đã
được dùng, confirm lại hoặc cancel booking đã confirm dùng
`IllegalStateException`. Message ngắn để caller hiểu nguyên nhân.

## 9. `BookingHistory` có phải database recovery không?

Không. `BookingHistory` lưu confirmation UTF-8 vào file và có thể đọc lại
nội dung đã lưu. Nó không khôi phục `seatStates`/`seatLocks` sau restart và
không cung cấp database transaction hay distributed locking.

## 10. Làm thế nào chứng minh flow tuần 4 đầy đủ sau khi phần Thắng merge?

Giữ A1 Standard và A6 Luxury: subtotal là 275.000 VND; Member 15% giảm
41.250 VND; total là 233.750 VND. Sau khi `MemberDiscountPolicy` và lựa
chọn Member có trên `main`, cần bổ sung/chạy test qua `DiscountPolicy`, chạy
console chọn A1,A6 và Member, rồi kiểm tra confirmation, trạng thái BOOKED,
history và booking thứ hai bị từ chối. Ở trạng thái hiện tại, phần này chưa
được tuyên bố hoàn thành.
