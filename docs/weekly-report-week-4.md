# Báo cáo tiến độ tuần 4 — Movie Ticket Booking System

- Thành viên: Nguyễn Đức Thắng — 23110062; Nguyễn Thành Triệu — 23110066.
- Nhánh tích hợp: `week4-trieu-final-delivery`.
- Ngày kiểm chứng tích hợp: 18/10/2026.
- Phạm vi tài liệu: báo cáo tiến độ tuần 4, chưa phải final report.

## Phạm vi đã hoàn thiện

Nhánh delivery hiện có cả `MemberDiscountPolicy` giảm 15% của Thắng và
`LuxuryTier` tính 175% giá cơ bản của Triệu. Phần Member được tích hợp từ
nhánh `week4-thang-member-discount` tại commit `26479fb`; các commit gốc và
tác giả được giữ nguyên. Nhánh này chưa được merge vào main.

`LuxuryTier implements PricingTier` và trả 175% base price. Phép tính được
tách thành phần hàng trăm và phần dư để nhân bằng `long` mà không tạo phép
nhân trung gian vượt giới hạn sớm; `Math.addExact` và kiểm tra giới hạn báo
`ArithmeticException` thay vì trả số âm do overflow. Giá lẻ được làm tròn
xuống đến 1 VND, còn base price âm bị từ chối bằng `IllegalArgumentException`.

Trong `Main.runInteractive`, ghế A6 dùng `new LuxuryTier()` được thêm vào
catalog trước khi tạo `Showtime`. `Main` chỉ wiring dữ liệu demo; việc tính
giá, khóa ghế, discount và lifecycle vẫn do domain/service hiện có xử lý.

## Phân công và lịch sử

| Thành viên | Phần triển khai | Commit |
| --- | --- | --- |
| Nguyễn Đức Thắng | Member discount, unit/booking tests, lựa chọn Member trên console và các test console | [4d1dc10](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/4d1dc10), [087ce92](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/087ce92) |
| Nguyễn Thành Triệu | Luxury tier, wiring ghế A6, unit/booking flow tests, hồ sơ và ảnh viết tay | [a00434d](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a00434d), [469f605](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/469f605), [c61bce8](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c61bce8) |

Phần rà soát/tích hợp do Thắng phụ trách: [9e76e9a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9e76e9a)
sửa test theo locale; [26479fb](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/26479fb)
tích hợp Member; [962cf35](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/962cf35)
bổ sung bảy test kết hợp và ví dụ làm tròn Luxury 107 → 187 VND.

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
- `MemberDiscountPolicy` implements `DiscountPolicy` và được gọi qua
  `Booking.calculateDiscount()`. Với cùng A1 Standard/A6 Luxury, ba policy
  NoDiscount/Student/Member cho total 275.000/247.500/233.750 VND. Mỗi lượt
  kiểm thử dùng fixture riêng, không tái sử dụng ghế đã BOOKED.

UML hiện tại nằm tại [final-class-diagram.md](final-class-diagram.md), có đủ
Luxury, Member, lifecycle, history và console. Diff không khẳng định mọi
class cũ đều không đổi: Main thêm A6 và console thêm lựa chọn Member; các
class nghiệp vụ/interface cũ không thay đổi.

## Test và flow đã chạy

`LuxuryTierTest` kiểm tra ví dụ 100.000 -> 175.000 VND, các số nhỏ/lẻ,
giá lớn sát giới hạn `long`, overflow, base âm và polymorphism qua
`Seat.price()`.

`LuxuryBookingFlowTest` giữ các kiểm tra A1 Standard/A6 Luxury với
NoDiscount; `LuxuryMemberIntegrationTest` kiểm tra cả hai phần qua các
implementation thật:

- subtotal = 100.000 + 175.000 = 275.000 VND;
- Member discount = 41.250 VND, total = 233.750 VND;
- booking chuyển sang `CONFIRMED`, A1/A6 chuyển sang `BOOKED`;
- confirmation chứa đúng ghế, subtotal, total và được `BookingHistory`
  lưu/đọc lại;
- booking thứ hai dùng lại A6 bị từ chối;
- cancel trả cả hai ghế về AVAILABLE, không lưu confirmation;
- confirm sau expiration bị từ chối, booking thành EXPIRED;
- console xác nhận/lưu history, từ chối đặt lại A6 và xử lý cancel.

Kết quả kiểm chứng thực tế trong môi trường hiện tại:

- Maven/JUnit: 95 test, 0 failure/error/skipped ở cấu hình mặc định;
- chạy lại toàn bộ 95 test với `vi_VN`: đều pass;
- `cinema.Main`: exit code 0;
- `cinema.Week1Scenarios`: 3/3 scenario passed;
- interactive console: A1,A6 với lựa chọn `3 = member` hiển thị total
  233.750 VND; file history đọc lại có cùng giá; đặt lại A6 bị từ chối;
- console chạy với locale Việt Nam hiển thị 275.000 / 41.250 / 233.750 VND,
  thay vì dấu phẩy ở môi trường mặc định.

Lần kiểm chứng ngày 18/10 dùng Maven 3.9.9 sẵn có trong môi trường và JDK 22,
biên dịch target Java 17. Lệnh chạy chuẩn là `mvn clean test`; kiểm tra vùng
dùng thêm `"-DargLine=-Duser.language=vi -Duser.country=VN"`. Kết quả 73 test
bằng JUnit Platform ở lần triển khai độc lập của Triệu được giữ trong log
cũ, không ghi đè thành kết quả tích hợp.

## Lỗi phát hiện và sửa sau rà soát

`LuxuryBookingFlowTest` ban đầu so sánh cứng chuỗi `275,000`. Khi chạy JVM
với locale Việt Nam, confirmation dùng dấu chấm nên một trong hai test
thất bại. Hai assertion được sửa để tạo chuỗi kỳ vọng bằng
`String.format("%,d", 275_000L)`, cùng cách định dạng của confirmation.
Không đổi phép tính giá hoặc code nghiệp vụ. Sau sửa, hai test qua được
locale Việt Nam; bộ tích hợp 95 test cũng pass ở cả hai cấu hình.

Minh chứng: [regression trước/sau](../evidence/week-4-locale-regression.txt),
[test và demo tích hợp](../evidence/week-4-integration-test-output.txt),
[diff tích hợp](../evidence/week-4-integration-extensibility-diff.txt).

## Liên hệ syllabus

Theo Appendix A, Project 12 yêu cầu Polymorphism và Composition, với seat
tiers, locking, discounts; question family POLY.

| Nội dung | Vị trí trong tuần 4 |
| --- | --- |
| Interfaces/polymorphism | LuxuryTier → PricingTier → Seat.price(); MemberDiscountPolicy → DiscountPolicy → Booking.calculateDiscount() |
| Composition/has-a | Screen giữ layout, Booking giữ selected seats/policy, Showtime sở hữu lock/state theo ghế |
| Encapsulation/invariants | Showtime private maps, owner/expiration; Booking chỉ chuyển từ PENDING |
| SRP/Open-Closed | Thêm hai implementation; Main/console chỉ wiring; không viết lại booking workflow |
| Testing/exceptions/file I/O | Unit, lifecycle, hai locale, console, UTF-8 history và từ chối thao tác sai |

## Giới hạn và phần để lại

- Final report chưa tạo theo yêu cầu hiện tại của nhóm.
- Reflection trong hai tài liệu cá nhân là bản nháp kỹ thuật; không xác
  nhận thay trải nghiệm học tập hoặc kết quả vấn đáp của từng người.
- Khóa chỉ bảo vệ caller dùng chung object trong một JVM. History không
  khôi phục inventory/lock sau restart; chưa có transaction chung với file.
- Member là lựa chọn mô phỏng, không xác minh tài khoản thành viên; không
  cộng dồn discount. Các kiểm thử biên mới không bảo đảm chống tràn cho mọi
  phép tính ở các tier/policy cũ.

Log triển khai độc lập của Triệu: [73 test](../evidence/week-4-final-test-output.txt)
và [diff Luxury](../evidence/week-4-final-extensibility-diff.txt).

## Minh chứng viết tay

- [Thắng — tính tiền và trace Member](../evidence/week-4-handwritten/thang-member-discount-calculation-and-trace.jpg)
- [Thắng — mở rộng và làm tròn](../evidence/week-4-handwritten/thang-discount-extensibility-and-rounding.jpg)
- [Tính giá các tier và flow A1 Standard + A6 Luxury](../evidence/week-4-handwritten/trieu-luxury-tier-pricing-trace.png)
- [Trace gọi `Seat.price`, `LuxuryTier` và tính subtotal/discount/total](../evidence/week-4-handwritten/trieu-luxury-booking-polymorphism-trace.png)

Các ảnh được giữ nguyên bản. Hai trang của Triệu trình bày thiết kế/tính tay
dự kiến; sau tích hợp, kết quả 275.000 − 41.250 = 233.750 VND đã được chạy
qua service và console. Nhãn Student trước phép tính 15% ở ảnh của Thắng
được giải thích rõ trong tài liệu cá nhân; code Student vẫn là 10%.

## Hồ sơ cá nhân và AI Usage Log

- Thắng: [10 câu hỏi và reflection](thang-defense.md), [AI log](week-4-thang-ai-usage-log.md).
- Triệu: [10 câu hỏi và reflection](trieu-defense.md), [AI log](week-4-trieu-ai-usage-log.md).
