# Movie Ticket Booking System

Đề tài OOP - HK1 2026-2027 Đợt 1.

## Phạm vi hệ thống

Hệ thống mô phỏng quy trình đặt vé xem phim tại rạp. Người dùng có thể xem phim, suất chiếu, chọn ghế, khóa ghế tạm thời, áp dụng giảm giá và xác nhận booking.

Phiên bản hiện tại chưa bao gồm cơ sở dữ liệu, đăng nhập, thanh toán thật hoặc giao diện đồ họa.

## Cấu trúc thư mục

```text
movie-ticket-booking-system/
├── pom.xml
├── README.md
└── src/
    ├── main/java/cinema/
    └── test/java/cinema/
```

## Yêu cầu chạy

- Java 17 trở lên.
- Maven 3.9 trở lên.

## Build và automated tests với Maven

Từ thư mục project:

```powershell
mvn clean test
mvn package
java -cp target/classes cinema.Main
```

JUnit tests kiểm tra giá Standard/Premium/Recliner, khả năng thay thế
`PricingTier`, subtotal, student discount, group discount từ 5 ghế và
trường hợp không giảm giá. Phần tuần 4 bổ sung `LuxuryTier` (175% giá cơ
bản), kiểm tra làm tròn xuống/overflow và flow kết hợp Standard + Luxury
với `MemberDiscountPolicy` giảm 15% qua `BookingService` và console.
Giá tiền được biểu diễn bằng `long` vì hệ thống sử dụng đơn vị VND nguyên;
phép chia tỷ lệ làm tròn xuống đến 1 VND.

Output chạy thật của bộ test tuần 2 được lưu tại
`evidence/week-2-test-output.txt`.

Domain model hiện có validation cho dữ liệu rỗng/null, duration không hợp lệ,
seat trùng hoặc không tồn tại, booking thiếu ghế và thao tác khóa ghế không
hợp lệ; thao tác khóa nhiều ghế vẫn all-or-nothing.

Seat lock mặc định có hiệu lực 5 phút. `Showtime` sử dụng `Clock` và
`Duration` để xác định expiration; test dùng clock điều khiển được thay vì
chờ thời gian thật. Thiết kế chi tiết nằm tại
[`docs/week-3-seat-lock-lifecycle.md`](docs/week-3-seat-lock-lifecycle.md).

`BookingService` điều phối việc tạo, xác nhận và hủy booking; trạng thái
booking gồm `PENDING`, `CONFIRMED`, `CANCELLED` và `EXPIRED`. Bộ test tuần 3
bao gồm lifecycle và hai user đồng thời tranh ghế cuối bằng
`ExecutorService`/`CountDownLatch`. Ghi chú thiết kế nằm tại
[`docs/week-3-concurrency-notes.md`](docs/week-3-concurrency-notes.md), còn
output chạy thật nằm tại
[`evidence/week-3-concurrency-test-output.txt`](evidence/week-3-concurrency-test-output.txt).
AI usage log của phần này nằm tại
[`docs/week-3-ai-usage-log.md`](docs/week-3-ai-usage-log.md).

## Phiên bản tích hợp tuần 4

`LuxuryTier` triển khai cùng interface `PricingTier`, tính 175% base price
bằng `long`, làm tròn xuống đến 1 VND và báo lỗi rõ ràng khi kết quả overflow.
Catalog interactive trong `Main` có thêm ghế A6 dùng `LuxuryTier`; ghế được
thêm trước khi tạo các `Showtime` và không đưa business logic vào `Main`.

Các test mới nằm tại
[`LuxuryTierTest`](src/test/java/cinema/LuxuryTierTest.java) và
[`LuxuryBookingFlowTest`](src/test/java/cinema/LuxuryBookingFlowTest.java).
Minh chứng lần triển khai độc lập của Triệu nằm tại
[`evidence/week-4-final-test-output.txt`](evidence/week-4-final-test-output.txt),
còn phân biệt class mới với wiring nằm tại
[`evidence/week-4-final-extensibility-diff.txt`](evidence/week-4-final-extensibility-diff.txt).

`MemberDiscountPolicy` và lựa chọn Member trong `CinemaConsole` thuộc phần
của Nguyễn Đức Thắng, đã được tích hợp cùng Luxury và merge vào `main`
tại [e828683](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/e828683).
Chọn A1 Standard + A6 Luxury, subtotal 275.000 VND; chọn Member giảm
41.250 VND, total 233.750 VND. Member là lựa chọn mô phỏng, chưa xác minh
tài khoản/thẻ thành viên; mỗi booking dùng một policy, không cộng dồn.

Bộ kiểm thử hiện có 95 test, đã chạy ở cấu hình mặc định và locale Việt Nam.
Test kết hợp nằm tại [LuxuryMemberIntegrationTest](src/test/java/cinema/LuxuryMemberIntegrationTest.java).
Minh chứng mới nằm tại [test/demo tích hợp](evidence/week-4-integration-test-output.txt),
[diff tích hợp](evidence/week-4-integration-extensibility-diff.txt) và
[regression định dạng vùng](evidence/week-4-locale-regression.txt).
Các log 73 test của Triệu và 81 test của Thắng vẫn được giữ riêng theo lần chạy.

UML hiện tại: [Class diagram tuần 4](docs/final-class-diagram.md).
Ghi chú tuần, câu hỏi bảo vệ và reflection kỹ thuật nằm tại
[`docs/weekly-report-week-4.md`](docs/weekly-report-week-4.md),
[hồ sơ Thắng](docs/thang-defense.md) và [hồ sơ Triệu](docs/trieu-defense.md).
AI log: [Thắng](docs/week-4-thang-ai-usage-log.md), [Triệu](docs/week-4-trieu-ai-usage-log.md).
Final report chưa tạo; reflection hiện là bản nháp kỹ thuật, chưa xác nhận
thay trải nghiệm hoặc kết quả vấn đáp cá nhân.

## Console và lưu lịch sử booking

```powershell
mvn clean test
java -cp target/classes cinema.Main --interactive
```

Chọn số suất chiếu, nhập mã ghế cách nhau bằng dấu phẩy, chọn discount rồi
xác nhận hoặc hủy. Booking ID trong console dùng UUID. `BookingHistory` lưu
confirmation UTF-8 vào `data/booking-history/`; có thể truyền thư mục khác:

```powershell
java -cp target/classes cinema.Main --interactive data/booking-history
```

Chạy `cinema.Main` không có `--interactive` để xem demo cố định cùng bước
lưu/đọc lại confirmation. File history chỉ lưu lịch sử, không khôi phục state
ghế hoặc lock sau khi khởi động lại.

Để kiểm tra vùng Việt Nam bằng Maven:

```powershell
mvn "-DargLine=-Duser.language=vi -Duser.country=VN" test
```

Báo cáo tiến độ hiện tại nằm tại [tuần 4](docs/weekly-report-week-4.md).
Refactoring trước/sau của tuần 3 và output lịch sử vẫn nằm tại
[báo cáo tuần 3](docs/weekly-report-week-3.md),
[week-3-review-test-output.txt](evidence/week-3-review-test-output.txt).

## Compile và chạy trực tiếp

Từ thư mục gốc của repository:

PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out src/main/java/cinema/*.java

javac -cp out -d out src/test/java/cinema/Week1Scenarios.java

java -cp out cinema.Main
java -cp out cinema.Week1Scenarios
```

Các lệnh trên lần lượt compile source chính, compile scenario tuần 1,
chạy demo `cinema.Main` và chạy toàn bộ scenario `cinema.Week1Scenarios`.

Để xem catalog interactive có A6 Luxury, chạy:

```powershell
java -cp target/classes cinema.Main --interactive
```

Sau khi build bằng Maven, chọn suất chiếu, nhập `A1,A6`, chọn discount
`3 = member`, rồi nhập `y` để xác nhận. Các lựa chọn cũ `0 = none`,
`1 = student`, `2 = group` vẫn giữ nguyên.

Để lưu output chạy thật vào evidence:

```powershell
New-Item -ItemType Directory -Force evidence | Out-Null
& {
    Write-Output '=== java -cp out cinema.Main ==='
    java -cp out cinema.Main
    Write-Output '=== java -cp out cinema.Week1Scenarios ==='
    java -cp out cinema.Week1Scenarios
} | Tee-Object -FilePath evidence/week-1-console-output.txt
```

## Kết quả mẫu

Chương trình minh họa:

1. Tạo movie, screen, seat và showtime.
2. Tính giá Standard và Premium thông qua cùng interface `PricingTier`.
3. Khóa ghế, tạo booking và áp dụng student discount.
4. Từ chối booking thứ hai cố gắng sử dụng ghế đã được đặt.

## Trạng thái phát triển

- [x] Phân tích phạm vi và use case.
- [x] Thiết kế class diagram.
- [x] Tạo domain skeleton có thể chạy.
- [x] Minh họa polymorphism về giá ghế.
- [x] Minh họa seat locking và lỗi double-booking cơ bản.
- [x] Thêm executable scenarios tuần 1 cho happy path, double-booking và pricing polymorphism.
- [x] Thiết lập Maven và JUnit 5.
- [x] Dùng `long` để biểu diễn số tiền VND.
- [x] Thêm automated tests cho pricing tier và booking price calculation.
- [x] Bổ sung group discount 5% cho booking từ 5 ghế.
- [x] Bổ sung validation cho dữ liệu không hợp lệ ở domain model.
- [x] Bổ sung expiration 5 phút cho seat lock và test bằng injected clock.
- [x] Bổ sung BookingService, lifecycle state và concurrency test cho ghế cuối.
- [x] Bảo vệ confirmation và booking ID; kiểm thử race trên cùng Booking.
- [x] Lưu/đọc confirmation UTF-8 và test lỗi I/O.
- [x] Console duyệt suất chiếu, chọn ghế, discount, xác nhận/hủy.
- [x] Tích hợp Luxury và Member; kiểm thử service/console/history/cancel/expiration.
- [x] Sửa regression định dạng vùng; 95 test pass với hai cấu hình locale.
- [x] UML hiện tại, ảnh viết tay và 10 câu hỏi bảo vệ cho mỗi thành viên.
- [ ] Xác nhận reflection và thực hiện vấn đáp cá nhân.
- [ ] Hoàn thiện báo cáo cuối kỳ.

## Hồ sơ tuần 1 nộp lại

- [Phân tích bài toán và requirement traceability](docs/week-1-problem-analysis.md)
- [Class diagram baseline tuần 1](docs/class-diagram.md)
- [Self-Explanation tuần 1](docs/week-1-self-explanation.md)
- [Báo cáo tuần 1 bổ sung sau phản hồi](docs/weekly-report-week-1-resubmission.md)

Các tài liệu trên mô tả phạm vi tuần 1. Executable scenarios nằm trong
`src/test/java/cinema/Week1Scenarios.java`.
