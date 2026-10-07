# Movie Ticket Booking System

Đề tài OOP - HK1 2026-2027 Đợt 1.

## Phạm vi phiên bản đầu

Hệ thống mô phỏng quy trình đặt vé xem phim tại rạp. Người dùng có thể xem phim, suất chiếu, chọn ghế, khóa ghế tạm thời, áp dụng giảm giá và xác nhận booking.

Phiên bản hiện tại chưa bao gồm cơ sở dữ liệu, đăng nhập, thanh toán thật hoặc giao diện đồ họa.

## Cấu trúc thư mục

```text
final_project/
├── README.md
├── docs/
│   ├── weekly-report-week-1.md
│   ├── weekly-report-week-2.md
│   ├── class-diagram.md
│   └── design-decisions.md
└── src/
    ├── main/java/cinema/
    └── test/java/cinema/Week2Tests.java
```

## Yêu cầu chạy

- Java 17 trở lên.

## Compile và chạy

Từ thư mục `final_project`:

PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out src/main/java/cinema/*.java
java -cp out cinema.Main
```

Chạy test tuần 2:

```powershell
javac -d out src/main/java/cinema/*.java src/test/java/cinema/Week2Tests.java
java -cp out cinema.Week2Tests
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
- [x] Viết test pricing, discount và validation.
- [x] Bổ sung group discount cho booking từ 5 ghế.
- [x] Bổ sung validation cho dữ liệu không hợp lệ.
- [ ] Bổ sung expiration cho seat lock.
- [ ] Hoàn thiện báo cáo cuối kỳ và oral-defense script.
