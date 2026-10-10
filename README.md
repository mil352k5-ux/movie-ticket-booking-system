# Movie Ticket Booking System

Đề tài OOP - HK1 2026-2027 Đợt 1.

## Phạm vi phiên bản đầu

Hệ thống mô phỏng quy trình đặt vé xem phim tại rạp. Người dùng có thể xem phim, suất chiếu, chọn ghế, khóa ghế tạm thời, áp dụng giảm giá và xác nhận booking.

Phiên bản hiện tại chưa bao gồm cơ sở dữ liệu, đăng nhập, thanh toán thật hoặc giao diện đồ họa.

## Cấu trúc thư mục

```text
final_project/
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
`PricingTier`, subtotal, student discount và trường hợp không giảm giá.
Giá tiền được biểu diễn bằng `long` vì hệ thống sử dụng đơn vị VND nguyên;
phép chia tỷ lệ làm tròn xuống đến 1 VND.

## Compile và chạy trực tiếp

Từ thư mục `final_project`:

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
4. Từ chối booking thứ hai cố gắng sử dụng ghế đã bị khóa.

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
- [ ] Bổ sung group discount cho booking từ 5 ghế (tuần 2).
- [ ] Bổ sung validation cho dữ liệu không hợp lệ (tuần 2).
- [ ] Bổ sung expiration cho seat lock.
- [ ] Hoàn thiện báo cáo cuối kỳ và oral-defense script.

## Hồ sơ tuần 1 nộp lại

- [Phân tích bài toán và requirement traceability](docs/week-1-problem-analysis.md)
- [Class diagram baseline tuần 1](docs/class-diagram.md)
- [Self-Explanation tuần 1](docs/week-1-self-explanation.md)
- [Báo cáo tuần 1 bổ sung sau phản hồi](docs/weekly-report-week-1-resubmission.md)

Các tài liệu trên mô tả phạm vi tuần 1. Executable scenarios nằm trong
`src/test/java/cinema/Week1Scenarios.java`.
