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

## 10. Flow kết hợp Luxury và Member được kiểm chứng thế nào?

Giữ A1 Standard và A6 Luxury: subtotal là 275.000 VND; Member 15% giảm
41.250 VND; total là 233.750 VND. `LuxuryMemberIntegrationTest` dùng các
implementation thật để kiểm tra đổi policy, xác nhận/lưu history, từ chối
đặt trùng, cancel, expiration và ba luồng console. Ví dụ 107 VND của ảnh
viết tay cho giá Luxury 187 VND cũng được thêm vào `LuxuryTierTest`.

Phần Member được tích hợp từ nhánh Thắng vào nhánh delivery; cả hai phần
đã merge vào `main` tại [e828683](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/e828683).
Bộ test có 95 test pass ở cấu hình mặc định và locale Việt Nam.
Lệnh và output nằm tại [minh chứng tích hợp](../evidence/week-4-integration-test-output.txt).

## Reflection — bản nháp kỹ thuật

Phần dễ nhầm của Luxury là giới hạn số nguyên. Giá bằng 175% base price nên
có base price hợp lệ với kiểu `long` nhưng giá sau nhân không còn vừa `long`.
Tách thương/dư tránh tràn ở tích trung gian; kiểm tra giới hạn và
`Math.addExact` vẫn cần để từ chối kết quả thực sự vượt miền biểu diễn.

AI hỗ trợ triển khai tier, test, wiring và tài liệu. Một điểm yếu đã kiểm
chứng là test confirmation so sánh cứng chuỗi `275,000`. Test pass ở môi
trường mặc định nhưng fail với locale Việt Nam. Cách sửa là tạo chuỗi kỳ
vọng theo cùng locale với confirmation, không đổi số tiền hoặc công thức
nghiệp vụ. [Log trước/sau sửa](../evidence/week-4-locale-regression.txt)
giữ lại trường hợp thất bại này.

Qua phần tích hợp có thể phân biệt hai việc: thêm implementation qua
interface và nối dữ liệu demo vào Main. `Seat` và `Booking` không cần biết
Luxury là loại ghế gì; Main chỉ chọn implementation cho A6. Kiểm thử kết
hợp còn cần xác nhận trạng thái ghế và file history, không chỉ kiểm tra
một kết quả 175.000 VND.

Hướng cải tiến là đưa các cấu hình vùng vào bước kiểm thử thường xuyên,
giữ log lịch sử riêng với kết quả tích hợp mới và bổ sung kiểm thử khi có
thay đổi yêu cầu. Đây là bản nhận xét kỹ thuật để chuẩn bị bảo vệ; trải
nghiệm học tập cá nhân và khả năng trả lời không có hỗ trợ chưa được xác
nhận bằng tài liệu này.
