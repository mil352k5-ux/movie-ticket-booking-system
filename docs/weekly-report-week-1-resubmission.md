# Báo cáo tuần 1 - Bổ sung sau phản hồi

## 1. Thông tin project

- Project: Movie Ticket Booking System
- Course: OOP - HK1 2026-2027 Đợt 1
- Giảng viên hướng dẫn: Huỳnh Xuân Phụng
- Tuần báo cáo: Tuần 1
- Loại báo cáo: Bổ sung/thay thế báo cáo tuần 1 sau phản hồi của GVHD
- Thành viên:
  - 23110062 - Nguyễn Đức Thắng
  - 23110066 - Nguyễn Thành Triệu
- Repository: https://github.com/mil352k5-ux/movie-ticket-booking-system
- Branch hồ sơ của Nguyễn Đức Thắng: `week1-thang-analysis-report`
- Commits của Nguyễn Đức Thắng:
  - Phân tích phạm vi/use case: https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a28a551
  - UML, Self-Explanation và báo cáo: https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9a4f05b
- Pull request của Nguyễn Đức Thắng: https://github.com/mil352k5-ux/movie-ticket-booking-system/pull/new/week1-thang-analysis-report
- Commit/PR của Nguyễn Thành Triệu: `[điền sau khi Triệu hoàn thành phần scenario]`

## 2. Phản hồi cần khắc phục

GVHD nhận xét repository trước đó chỉ có tài liệu/snapshot, chưa có code khớp nội dung báo cáo, đồng thời yêu cầu commit code theo tiến độ từng tuần để đối chiếu. Bản nộp lại này giới hạn nội dung ở baseline tuần 1, liên kết claim với source hiện có và tách phần việc của hai thành viên thành các branch/commit riêng.

Các commit bổ sung được tạo sau feedback và giữ nguyên ngày thực tế. Nhóm không backdate hoặc trình bày chúng như lịch sử đã tồn tại trước đó.

## 3. Mục tiêu tuần 1

- Hiểu và giới hạn phạm vi bài toán.
- Xác định các use case chính.
- Xây dựng domain model tối thiểu có thể compile/run.
- Thể hiện polymorphism qua seat-tier pricing.
- Để `Showtime` đóng gói trạng thái ghế và ngăn double-booking cơ bản.
- Áp dụng student discount.
- Sinh booking confirmation.
- Chuẩn bị tài liệu giải thích thiết kế và minh chứng chạy.

## 4. Nội dung có thể đối chiếu với source hiện tại

- `Movie`, `Screen`, `Showtime`, `Seat`, `Booking` đã có trong `src/main/java/cinema`.
- `PricingTier` có `StandardTier`, `PremiumTier`, `ReclinerTier`.
- `DiscountPolicy` có `StudentDiscountPolicy`, `NoDiscountPolicy`.
- `Showtime` quản lý `AVAILABLE`, `LOCKED`, `BOOKED` bằng private maps.
- `Main` chạy happy path B001 cho A1/A2 và in confirmation.
- `Main` thử khóa lại A1 cho B002 và bắt `IllegalStateException`.
- `README.md` có hướng dẫn compile/run baseline.

Chi tiết traceability nằm trong [Phân tích bài toán](week-1-problem-analysis.md), [Class diagram](class-diagram.md) và [Self-Explanation](week-1-self-explanation.md).

## 5. Self-Explanation tóm tắt

### Polymorphism

`Seat.price()` gọi `PricingTier.calculatePrice()`. Standard, Premium và Recliner triển khai cùng interface nhưng trả kết quả khác nhau. `Booking.calculateSubtotal()` không cần biết ghế thuộc tier nào, nên có thể thêm tier mới mà không viết lại booking logic.

### Encapsulation

`Showtime` giữ `seatStates` và `seatLockOwners` ở trạng thái private. Chỉ `tryLockSeats()`, `confirmBooking()` và `releaseSeats()` được thay đổi state. Việc tập trung validation trong `Showtime` ngăn code bên ngoài bỏ qua quy tắc và tự đặt ghế.

### Composition

`Screen` chứa các `Seat`; `Showtime` tham chiếu `Screen` và sở hữu state tương ứng với mỗi mã ghế; `Booking` chứa một list riêng của các ghế được chọn. Baseline chưa thể hiện strict composition `Showtime`–`Seat`, vì vậy nhóm ghi rõ giới hạn này thay vì tuyên bố ownership không có trong code.

## 6. Phân công tuần 1

| Thành viên | Phạm vi | Trạng thái/bằng chứng |
|---|---|---|
| Nguyễn Đức Thắng | Phân tích yêu cầu, use case, traceability, UML, Self-Explanation và báo cáo tuần 1 nộp lại. | Branch `week1-thang-analysis-report`; URL commit/PR được điền sau khi push. |
| Nguyễn Thành Triệu | Viết executable scenarios cho happy path, double-booking và pricing polymorphism; cập nhật hướng dẫn chạy và lưu output thật. | Đang chờ branch/commit riêng của Triệu. Không tính là đã hoàn thành trong bản hiện tại. |
| Cả hai | Review chéo code/tài liệu, tự chạy kiểm chứng và chuẩn bị trả lời bảo vệ. | Chỉ đánh dấu hoàn thành sau khi hai PR được review. |

## 7. Minh chứng chạy baseline

Lệnh PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out src/main/java/cinema/*.java
java -cp out cinema.Main
```

Kết quả cần có từ lần chạy thật:

```text
=== HAPPY PATH ===
Booking confirmed successfully.
Booking ID: B001
Movie: Interstellar
Seats: A1, A2
Subtotal: 225,000 VND
Discount (Student discount 10%): 22,500 VND
Total: 202,500 VND

=== VALIDATION: DOUBLE BOOKING ===
Rejected correctly: Seat A1 is not available.
```

Executable scenarios độc lập và evidence file thuộc phần việc của Nguyễn Thành Triệu, chưa được tuyên bố hoàn thành trong commit tài liệu của Nguyễn Đức Thắng.

## 8. Giới hạn tuần 1

- Chưa có automated test framework hoặc executable test class độc lập trong baseline hiện tại.
- Chưa có group discount.
- Chưa có lock expiration.
- Chưa có concurrency test thật với nhiều thread.
- Chưa có database transaction cho nhiều server.
- Chưa có catalog/menu tương tác hoặc GUI.
- Quan hệ composition `Showtime`–`Seat` cần được làm rõ hơn.

## 9. Kế hoạch tuần 2

- Thiết lập build/test tự động.
- Kiểm thử Standard, Premium và Recliner.
- Bổ sung group discount từ 5 ghế.
- Bổ sung validation cho dữ liệu domain và booking không hợp lệ.
- Cập nhật class diagram theo implementation thực tế.

## 10. AI Usage Log

### Công cụ và mục đích

- Công cụ: ChatGPT/Codex.
- Model: `[điền đúng model hiển thị trong ứng dụng khi nộp]`.
- Mục đích: kiểm tra repo, đối chiếu với đề bài, lập lộ trình, đề xuất phân công trung thực và tạo bản nháp tài liệu tuần 1.

### Prompt chính

> Kiểm tra source và lịch sử Git hiện tại của Movie Ticket Booking System. Trong vai senior Java/OOP reviewer, hãy tạo hồ sơ tuần 1 nộp lại gồm problem analysis, use cases, class diagram, requirement traceability và Self-Explanation. Nội dung phải khớp chính xác với class/method đang tồn tại, ghi rõ giới hạn composition hiện tại, không tạo test evidence giả, không backdate hoặc viết lại lịch sử Git và không nhận phần việc của Nguyễn Thành Triệu là đã hoàn thành.

### AI tạo/gợi ý

- Cấu trúc tài liệu phân tích và báo cáo.
- Bảng ánh xạ yêu cầu với class/method.
- Bản nháp Mermaid class diagram.
- Bản nháp Self-Explanation và các rủi ro thiết kế cần giải thích.

### Sinh viên sửa/hoàn thiện và kiểm chứng

- Nguyễn Đức Thắng chịu trách nhiệm đọc lại từng claim và đối chiếu với source.
- Nhóm tự xác nhận MSSV, họ tên, model AI và URL commit/PR trước khi nộp.
- Output chạy chỉ được dùng làm evidence sau khi sinh viên tự chạy lại trên source đã push.
- Nguyễn Thành Triệu tự triển khai, kiểm tra và commit executable scenarios trên branch riêng.
