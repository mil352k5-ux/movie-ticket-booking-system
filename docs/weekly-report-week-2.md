# Weekly Report 2 - Movie Ticket Booking System

## 1. Thông tin project

- Project: Movie Ticket Booking System
- Course: OOP HK1 2026-2027 Đợt 1
- Giảng viên: Huỳnh Xuân Phụng
- Tuần: 2
- Thành viên: `[23110062 - Nguyễn Đức Thắng & 23110066 - Nguyễn Thành Triệu]`

## 2. Mục tiêu tuần 2

- Hoàn thiện pricing theo các seat tier.
- Bổ sung discount policy thứ hai.
- Kiểm tra dữ liệu không hợp lệ.
- Viết test cho pricing, discount và seat state.

## 3. Công việc đã hoàn thành

- Hoàn thiện `StandardTier`, `PremiumTier` và `ReclinerTier`.
- Thêm `GroupDiscountPolicy`, giảm 5% cho booking từ 5 ghế.
- Giữ `StudentDiscountPolicy`, giảm 10%.
- Bổ sung validation cho Movie, Screen, Seat, Showtime và Booking.
- Kiểm tra booking rỗng, ghế không tồn tại và ghế trùng ID.
- Kiểm tra việc release ghế và chuyển ghế sang BOOKED.
- Tạo `Week2Tests` không cần thư viện ngoài.

## 4. Minh chứng test

Lệnh chạy:

```powershell
javac -d out src/main/java/cinema/*.java src/test/java/cinema/Week2Tests.java
java -cp out cinema.Week2Tests
```

Kết quả kỳ vọng:

```text
ALL WEEK 2 TESTS PASSED: 16
```

Các nhóm test:

1. Giá Standard, Premium và Recliner.
2. Student discount.
3. Group discount với ngưỡng 5 ghế.
4. Booking rỗng và ghế không tồn tại.
5. Release ghế và confirm booking.
6. Ghế trùng ID.

## 5. Self-Explanation cập nhật

`PricingTier.calculatePrice()` tiếp tục thể hiện polymorphism vì cùng một lời gọi method tạo ra giá khác nhau tùy implementation.

`GroupDiscountPolicy.calculateDiscount()` thể hiện khả năng mở rộng. Quy tắc nhóm được thêm bằng class mới, không cần sửa `Booking`.

Các validation được đặt trong constructor hoặc method quản lý state tương ứng. Điều này bảo vệ invariant của từng class, ví dụ `Booking` không thể tồn tại nếu không có ghế và `Screen` không thể chứa hai ghế cùng ID.

## 6. Vấn đề còn lại

- Chưa có expiration time cho ghế LOCKED.
- Chưa có persistence/database.
- Chưa có test concurrency thật với nhiều thread.
- Chưa có giao diện người dùng.

## 7. Kế hoạch tuần 3

- Hoàn thiện booking flow bằng một service riêng.
- Bổ sung expiration cho seat lock.
- Viết test cho lock timeout và nhiều booking cạnh tranh.
- Cải thiện confirmation summary.

## 8. AI Usage Log

| Ngày | Công cụ | Prompt/mục đích | Kết quả đã sử dụng | Cách kiểm chứng |
|---|---|---|---|---|
| 04/10/2026 | ChatGPT | Yêu cầu triển khai các phần việc của tuần 2 cho Movie Ticket Booking System. | Dùng để bổ sung group discount, validation, test pricing/discount/seat state và báo cáo tuần 2. | Compile bằng `javac`, chạy `Week2Tests` và kiểm tra output happy path. |

Chi tiết cách nhóm phân biệt phần AI gợi ý với phần nhóm lựa chọn, sửa và kiểm chứng được ghi trong [AI Collaboration Log](ai-collaboration-log.md). Commit tuần 2 được mô tả trong [GitHub Commit Plan](github-commit-plan.md).

## 9. GitHub

- Repository public: `[Dán URL GitHub thật vào đây]`
- Commit hồ sơ tuần 2: `[Dán commit URL thật vào đây]`
