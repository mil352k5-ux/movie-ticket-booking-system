# Movie Ticket Booking System

Đề tài OOP - HK1 2026-2027 Đợt 1.

## Phạm vi phiên bản đầu

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
trường hợp không giảm giá.
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

Bản tích hợp tuần 3 có 66 automated tests. Báo cáo, UML cập nhật và
refactoring trước/sau nằm tại
[Báo cáo tuần 3](docs/weekly-report-week-3.md); output kiểm chứng ở
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
- [ ] Hoàn thiện báo cáo cuối kỳ và oral-defense script.

## Hồ sơ tuần 1 nộp lại

- [Phân tích bài toán và requirement traceability](docs/week-1-problem-analysis.md)
- [Class diagram baseline tuần 1](docs/class-diagram.md)
- [Self-Explanation tuần 1](docs/week-1-self-explanation.md)
- [Báo cáo tuần 1 bổ sung sau phản hồi](docs/weekly-report-week-1-resubmission.md)

Các tài liệu trên mô tả phạm vi tuần 1. Executable scenarios nằm trong
`src/test/java/cinema/Week1Scenarios.java`.
