# Báo cáo tiến độ tuần 4 — Movie Ticket Booking System

Repository GitHub public: [movie-ticket-booking-system](https://github.com/mil352k5-ux/movie-ticket-booking-system).

Thành viên: Nguyễn Đức Thắng — 23110062; Nguyễn Thành Triệu — 23110066.

Phiên bản mã nguồn đối chiếu: [e828683](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/e828683), merge tuần 4 vào `main` ngày 18/10/2026. Đây là báo cáo tiến độ tuần 4, chưa phải báo cáo tổng kết cuối kỳ.

## 1. Mục tiêu tuần 4

- Thêm một discount và một pricing tier mới để kiểm tra khả năng mở rộng thiết kế.
- Dùng lại luồng tạo, xác nhận, hủy booking và lưu history từ tuần 3.
- Kiểm thử kết hợp hai phần, dữ liệu biên và lỗi định dạng số theo vùng.
- Cập nhật UML, minh chứng viết tay, AI Usage Log và tài liệu chuẩn bị bảo vệ.

## 2. Công việc đã hoàn thành

### Phần của Nguyễn Đức Thắng

Branch: `week4-thang-member-discount`.

Các commit chính:

- [4d1dc10](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/4d1dc10): thêm `MemberDiscountPolicy`, unit tests và booking tests.
- [087ce92](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/087ce92): thêm lựa chọn Member ở console và bốn test console.
- [79f9bfe](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/79f9bfe): tài liệu giải thích thiết kế và minh chứng kiểm thử.
- [7975bcf](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/7975bcf): bổ sung hai ảnh viết tay tuần 4.

Nội dung thực hiện:

- Thêm Member discount 15% qua interface `DiscountPolicy`; mỗi booking dùng một policy, không cộng dồn giảm giá.
- Bổ sung lựa chọn `3 = member` trong `CinemaConsole`.
- Tách thương/dư khi tính phần trăm để tránh overflow ở phép nhân trung gian và không bỏ mất phần dư.
- Kiểm tra subtotal âm, giá lẻ 107 VND, biên `Long.MAX_VALUE` và đối chiếu kết quả bằng `BigInteger`.
- Kiểm tra Member qua booking, console xác nhận/hủy, trạng thái ghế và history.
- Giữ nguyên `Booking`, `BookingService`, `Seat`, `Showtime` và các policy cũ.

### Phần của Nguyễn Thành Triệu

Branch: `week4-trieu-final-delivery`.

Các commit chính:

- [a00434d](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a00434d): thêm `LuxuryTier`, ghế A6 và unit/booking flow tests.
- [469f605](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/469f605): tài liệu thiết kế, output kiểm thử và câu hỏi bảo vệ.
- [c61bce8](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c61bce8): bổ sung hai ảnh viết tay tuần 4.

Nội dung thực hiện:

- Thêm `LuxuryTier implements PricingTier`, giá bằng 175% giá cơ bản.
- Thêm ghế A6 dùng Luxury vào dữ liệu interactive của `Main` trước khi tạo `Showtime`.
- Tính tiền bằng `long`, làm tròn xuống đến 1 VND; từ chối giá âm và báo `ArithmeticException` nếu giá Luxury vượt miền biểu diễn.
- Kiểm thử qua `Seat.price()` để chứng minh Luxury thay thế được các tier cũ qua cùng interface.
- Kiểm tra luồng A1 Standard + A6 Luxury, confirmation và từ chối đặt trùng.
- Giữ nguyên logic tính subtotal, giữ ghế và lifecycle của booking.

### Phần rà soát và tích hợp — Thắng phụ trách

- [9e76e9a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9e76e9a): sửa assertion định dạng số trong `LuxuryBookingFlowTest`.
- [26479fb](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/26479fb): tích hợp Member từ nhánh Thắng vào nhánh delivery; giữ nguyên commit và tác giả của hai phần.
- [962cf35](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/962cf35): thêm bảy test `LuxuryMemberIntegrationTest` và dữ liệu làm tròn Luxury 107 → 187 VND.
- [c1e2a4f](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c1e2a4f): cập nhật UML, báo cáo, minh chứng tích hợp và hồ sơ cá nhân.
- [2ef6c42](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/2ef6c42): thống nhất cách trình bày AI Usage Log tuần 4.
- [e828683](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/e828683): merge toàn bộ tuần 4 vào `main`.

## 3. Self-Explanation

### Thắng — Member discount và polymorphism

`Booking` giữ một `DiscountPolicy`. Trong `Booking.calculateDiscount()`, lời gọi `discountPolicy.calculateDiscount(subtotal, seatCount)` được chuyển đến implementation đang dùng. Khi chọn Member, công thức giảm 15% nằm trong `MemberDiscountPolicy`, không cần thêm nhánh kiểm tra loại discount vào `Booking` hoặc `BookingService`.

Ví dụ A1 Standard và A2 Premium có subtotal 225.000 VND. Member giảm 33.750 VND, total 191.250 VND. Nếu đổi sang Student thì vẫn dùng cùng luồng booking nhưng giảm 10%, total 202.500 VND.

Phép tính `subtotal * 15 / 100` có thể tràn ở bước nhân dù tiền giảm cuối cùng vẫn vừa `long`. Ngược lại, chỉ tính `subtotal / 100 * 15` sẽ mất phần dư. Vì vậy policy dùng:

```text
q = subtotal / 100
r = subtotal % 100
discount = q * 15 + (r * 15) / 100
```

Với subtotal 107 VND, discount là 16 VND, không phải 15 VND. Bộ test dùng `BigInteger` làm cách tính đối chiếu độc lập.

### Triệu — Luxury tier và khả năng mở rộng

`Seat.price(long)` gọi `PricingTier.calculatePrice(long)` thay vì tự kiểm tra ghế thuộc Standard, Premium hay Luxury. `LuxuryTier` cung cấp công thức 175%; `Booking.calculateSubtotal()` vẫn chỉ cộng giá từng ghế như trước.

Với giá cơ bản 100.000 VND, Luxury có giá 175.000 VND. Với giá cơ bản 107 VND, kết quả 187,25 được làm tròn xuống thành 187 VND.

Luxury cũng tách thương/dư để tránh overflow trung gian. Tuy nhiên, vì giá Luxury lớn hơn giá cơ bản, kết quả cuối vẫn có thể vượt `long`; kiểm tra giới hạn và `Math.addExact` dùng để từ chối trường hợp đó.

### Encapsulation, composition và phân chia trách nhiệm

`Showtime` tiếp tục giữ các map trạng thái/lock là private và kiểm tra ghế trong vùng đồng bộ. Thêm Luxury hoặc Member không cho phép bỏ qua kiểm tra owner, thời hạn giữ ghế hoặc trạng thái BOOKED.

`Screen` giữ layout ghế; `Booking` giữ danh sách ghế được chọn và policy. Booking tham chiếu các ghế có sẵn, không sở hữu vòng đời ghế vật lý. Trạng thái đặt ghế vẫn thuộc từng `Showtime`, không thuộc loại giá ghế.

`CinemaConsole` lo nhập/xuất, `BookingService` điều phối, `Booking` tính tiền và giữ trạng thái booking, `Showtime` bảo vệ ghế, `BookingHistory` lưu/đọc confirmation. Policy và tier chỉ tính giá hoặc giảm giá.

Hai class mới mở rộng qua interface mà không viết lại workflow cũ. `Main` có thêm dữ liệu A6 và `CinemaConsole` có thêm lựa chọn Member; đây là phần nối dữ liệu/giao diện, không phải thay logic nghiệp vụ. [Diff tích hợp](../evidence/week-4-integration-extensibility-diff.txt) phân biệt rõ các thay đổi này.

Nội dung tuần này gắn với interfaces, polymorphism, composition/has-a, encapsulation, SRP, Open/Closed, exceptions và kiểm thử. [UML tuần 4](final-class-diagram.md) có đủ Luxury, Member, lifecycle, service, history và console.

## 4. Minh chứng kiểm thử

Lệnh chạy:

```powershell
mvn clean test
mvn "-DargLine=-Duser.language=vi -Duser.country=VN" test
```

Kết quả phiên bản tích hợp:

| Nhóm kiểm thử | Số test |
| --- | ---: |
| Các nhóm từ tuần 3, không gồm CinemaConsoleTest | 63 |
| CinemaConsoleTest | 7 |
| MemberDiscountPolicyTest | 7 |
| MemberDiscountBookingTest | 4 |
| LuxuryTierTest | 5 |
| LuxuryBookingFlowTest | 2 |
| LuxuryMemberIntegrationTest | 7 |
| **Tổng** | **95** |

Cả hai cấu hình đều có `Tests run: 95; Failures: 0; Errors: 0; Skipped: 0; BUILD SUCCESS`. Ba scenario tuần 1 vẫn pass: happy path, double-booking và pricing polymorphism.

Luồng kết hợp đã chạy qua service và console:

```text
A1 Standard       = 100.000 VND
A6 Luxury         = 175.000 VND
Subtotal          = 275.000 VND
Member discount   =  41.250 VND
Total             = 233.750 VND
```

Sau xác nhận, booking là CONFIRMED, A1/A6 là BOOKED; confirmation được lưu/đọc lại đúng nội dung. Booking khác thử dùng A6 bị từ chối. Các test kết hợp còn kiểm tra cancel trả ghế về AVAILABLE, confirm sau expiration bị từ chối và không lưu confirmation cho booking chưa xác nhận.

Với cùng giá A1/A6, NoDiscount, Student và Member cho total lần lượt 275.000, 247.500 và 233.750 VND. Các fixture độc lập giúp kiểm tra đổi policy mà không dùng lại ghế đã BOOKED.

Khi rà soát, `LuxuryBookingFlowTest` có assertion so sánh cứng `275,000`. Chạy với locale Việt Nam làm 1 trong 2 test thất bại vì confirmation dùng dấu chấm. Expected được đổi sang `String.format` theo cùng locale với confirmation; không đổi công thức tính tiền. Output trước/sau được giữ lại.

Minh chứng trên repo:

- [Kết quả test và demo tích hợp](../evidence/week-4-integration-test-output.txt).
- [Regression trước và sau sửa](../evidence/week-4-locale-regression.txt).
- [Diff chứng minh khả năng mở rộng](../evidence/week-4-integration-extensibility-diff.txt).
- Ảnh Thắng: [tính tiền/trace](../evidence/week-4-handwritten/thang-member-discount-calculation-and-trace.jpg), [mở rộng/làm tròn](../evidence/week-4-handwritten/thang-discount-extensibility-and-rounding.jpg).
- Ảnh Triệu: [tính giá/booking](../evidence/week-4-handwritten/trieu-luxury-tier-pricing-trace.png), [trace/mở rộng](../evidence/week-4-handwritten/trieu-luxury-booking-polymorphism-trace.png).

Các ảnh giữ nguyên bản. Nhãn Student trước phép tính 15% ở trang 1 của Thắng có chú thích phân biệt trong hồ sơ cá nhân; code Student vẫn giảm 10%, Member giảm 15%.

Log độc lập trước tích hợp được giữ riêng: [81 test của Thắng](../evidence/week-4-thang-test-output.txt), [73 test của Triệu](../evidence/week-4-final-test-output.txt).

## 5. Kết quả và hạn chế

Kết quả tuần 4:

- Member và Luxury dùng chung các interface hiện có, không lặp lại logic booking.
- Luồng Standard + Luxury + Member chạy được qua service, console và file history.
- 95 test pass ở hai cấu hình locale; các test và scenario cũ vẫn hoạt động.
- Hai phần đã merge vào `main`, giữ nguyên lịch sử đóng góp.
- UML hiện tại, ảnh viết tay, AI Usage Log và bộ 10 câu hỏi/trả lời cho mỗi thành viên đã có trên repo.

Giới hạn còn lại:

- Member là lựa chọn mô phỏng, chưa kiểm tra tài khoản hoặc thẻ thành viên; không cộng dồn discount.
- Đồng bộ chỉ có hiệu lực với các object dùng chung trong một JVM; chưa có database/distributed locking.
- History lưu confirmation, không khôi phục inventory/lock khi khởi động lại; xác nhận booking và lưu file chưa nằm trong một transaction chung.
- Lock được dọn khi truy cập, chưa có background scheduler.
- Kiểm thử biên lớn ở hai biến thể mới không có nghĩa mọi phép tính trong các tier/policy cũ đã có cùng bảo đảm chống overflow.
- Reflection hiện là bản nháp nhận xét kỹ thuật; phần trải nghiệm học tập riêng của từng thành viên còn cần bổ sung. Báo cáo tổng kết cuối kỳ chưa tạo.

## 6. Công việc còn lại trước khi nộp cuối kỳ và bảo vệ

- Hoàn thiện báo cáo tổng kết toàn bộ hệ thống, tập hợp UML, Self-Explanation, test evidence và AI Usage Log.
- Mỗi thành viên bổ sung reflection bằng trải nghiệm thực tế và rà lại phần mình phụ trách.
- Dùng bộ 10 câu hỏi/trả lời để ôn lời gọi qua interface, cách tính tiền, trace trạng thái, expiration và concurrency.
- Luyện chạy demo, giải thích source và xử lý một thay đổi yêu cầu nhỏ khi bảo vệ.
- Kiểm tra lại hướng dẫn build/run và các liên kết minh chứng trước khi nộp bản cuối.

Hồ sơ cá nhân: [Thắng](thang-defense.md), [Triệu](trieu-defense.md).

AI Usage Log tuần 4: [Thắng](week-4-thang-ai-usage-log.md), [Triệu](week-4-trieu-ai-usage-log.md).
