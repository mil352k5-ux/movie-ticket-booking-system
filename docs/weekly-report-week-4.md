# Báo cáo tuần 4 - phần Nguyễn Thành Triệu

- Sinh viên: Nguyễn Thành Triệu - MSSV 23110066
- Nhánh làm việc: `week4-trieu-final-delivery`
- Ngày kiểm chứng trong phiên này: 15/10/2026

## Phạm vi đã hoàn thiện

Phần độc lập của Triệu bổ sung `LuxuryTier` và các kiểm thử mở rộng. Phần
`MemberDiscountPolicy` giảm 15% và lựa chọn Member trong `CinemaConsole` là
phần của Nguyễn Đức Thắng; hai nội dung này chưa có trên `origin/main` tại
thời điểm bắt đầu nhánh. Vì vậy báo cáo này không nhận thay phần của Thắng
và không kết luận rằng flow Member đã hoàn thành.

`LuxuryTier implements PricingTier` và trả 175% base price. Phép tính được
tách thành phần hàng trăm và phần dư để nhân bằng `long` mà không tạo phép
nhân trung gian vượt giới hạn sớm; `Math.addExact` và kiểm tra giới hạn báo
`ArithmeticException` thay vì trả số âm do overflow. Giá lẻ được làm tròn
xuống đến 1 VND, còn base price âm bị từ chối bằng `IllegalArgumentException`.

Trong `Main.runInteractive`, ghế A6 dùng `new LuxuryTier()` được thêm vào
catalog trước khi tạo `Showtime`. `Main` chỉ wiring dữ liệu demo; việc tính
giá, khóa ghế, discount và lifecycle vẫn do domain/service hiện có xử lý.

## Đối chiếu thiết kế OOP

- `PricingTier` là abstraction chung; `StandardTier`, `PremiumTier`,
  `ReclinerTier` và `LuxuryTier` cung cấp các implementation khác nhau.
- `Seat.price(long)` gọi `pricingTier.calculatePrice(basePrice)`. Test dùng
  cùng lời gọi này cho bốn implementation và không kiểm tra subtype bằng
  `instanceof` hoặc nhánh theo tên loại ghế.
- `BookingService` điều phối `createBooking`/`confirmBooking`, còn `Booking`
  giữ selected seats, subtotal, discount, total, confirmation và status.
  `Showtime` sở hữu state/lock theo suất chiếu; `BookingHistory` chỉ lưu
  confirmation UTF-8.
- Việc thêm `LuxuryTier` không sửa `Seat`, `Booking`, `BookingService`,
  `Showtime`, interface hoặc các tier/policy cũ. Đây là minh chứng mở rộng
  theo hướng Open/Closed ở phần pricing.

## Test và flow đã chạy

`LuxuryTierTest` kiểm tra ví dụ 100.000 -> 175.000 VND, các số nhỏ/lẻ,
giá lớn sát giới hạn `long`, overflow, base âm và polymorphism qua
`Seat.price()`.

`LuxuryBookingFlowTest` tạo A1 Standard và A6 Luxury, sau đó dùng
`BookingService` với `NoDiscountPolicy` để kiểm tra:

- subtotal = 100.000 + 175.000 = 275.000 VND;
- booking chuyển sang `CONFIRMED`, A1/A6 chuyển sang `BOOKED`;
- confirmation chứa đúng ghế, subtotal, total và được `BookingHistory`
  lưu/đọc lại;
- booking thứ hai dùng lại A6 bị từ chối.

Kết quả kiểm chứng thực tế trong môi trường hiện tại:

- JUnit Platform: 73 test tìm thấy, 73 thành công, 0 thất bại;
- `cinema.Main`: exit code 0;
- `cinema.Week1Scenarios`: 3/3 scenario passed;
- interactive console: catalog hiển thị A6 `Luxury`; flow A1,A6 với
  `NoDiscountPolicy` hiển thị total 275.000 VND.

Maven CLI không có trong PATH của phiên kiểm chứng nên `mvn clean test`
không khởi chạy được; kết quả 73/73 ở trên được chạy bằng JDK 21 với
`--release 17` và JUnit Platform 5.11.4 đã có trong máy. Khi Maven được
cấu hình, lệnh chuẩn của repository vẫn là `mvn clean test`.

## Những phần chưa kết luận

- Chưa kiểm thử subtotal 275.000, Member discount 41.250 và total 233.750
  bằng `MemberDiscountPolicy`, vì policy và lựa chọn Member chưa có trong
  `origin/main` của nhánh gốc này.
- Chưa ghi nhận console chọn Member; không sửa file `CinemaConsole.java`
  thuộc phần của Thắng.
- Final report, reflection cá nhân và minh chứng vấn đáp cuối kỳ chưa được
  tạo theo yêu cầu của người thực hiện.
- Không commit, không push và không tạo ảnh viết tay thay người thực hiện.

Minh chứng lệnh/output: [week-4-final-test-output.txt](../evidence/week-4-final-test-output.txt).
Minh chứng extensibility: [week-4-final-extensibility-diff.txt](../evidence/week-4-final-extensibility-diff.txt).
