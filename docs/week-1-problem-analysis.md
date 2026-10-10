# Phân tích bài toán - Tuần 1

## 1. Bối cảnh

Movie Ticket Booking System mô phỏng việc bán ghế cho các suất chiếu tại rạp phim. Mỗi phim có thể được chiếu tại một phòng vào một thời điểm xác định. Người dùng chọn các ghế còn trống, hệ thống tạm khóa ghế trong luồng đặt vé, áp dụng loại giá và chính sách giảm giá, sau đó xác nhận booking.

Mục tiêu của tuần 1 là tạo một domain model Java chạy được để chứng minh các trách nhiệm OOP cốt lõi. Phiên bản này là console application; chưa triển khai cơ sở dữ liệu, đăng nhập, thanh toán thật, giao diện đồ họa hoặc cơ chế khóa phân tán giữa nhiều server.

## 2. Phạm vi tuần 1

- Mô hình hóa `Movie`, `Screen`, `Showtime`, `Seat` và `Booking`.
- Hỗ trợ ba loại giá ghế thông qua `PricingTier`: Standard, Premium và Recliner.
- Để `Showtime` quản lý trạng thái ghế `AVAILABLE`, `LOCKED`, `BOOKED`.
- Áp dụng `StudentDiscountPolicy` giảm 10%.
- Sinh booking confirmation gồm mã booking, phim, thời gian, ghế, tạm tính, giảm giá và tổng tiền.
- Chạy một happy path và một tình huống từ chối double-booking trong `Main`.

## 3. Use cases

1. Xem thông tin phim và suất chiếu được khởi tạo trong chương trình mẫu.
2. Xem các ghế thuộc phòng chiếu.
3. Chọn một hoặc nhiều ghế theo mã ghế.
4. Tạm khóa các ghế đã chọn cho một booking ID.
5. Tính giá từng ghế dựa trên `PricingTier`.
6. Áp dụng một `DiscountPolicy`.
7. Xác nhận booking và chuyển ghế từ `LOCKED` sang `BOOKED`.
8. Sinh booking confirmation.
9. Từ chối khóa ghế đã bị khóa hoặc đã được đặt.
10. Giải phóng ghế đang khóa nếu booking bị hủy trước khi xác nhận.

Ở tuần 1, thao tác "xem" được thể hiện bằng dữ liệu mẫu trong `Main`, chưa phải menu tương tác hoặc catalog hoàn chỉnh.

## 4. Ánh xạ yêu cầu với source code

| Yêu cầu | Class/method hiện có | Minh chứng tuần 1 |
|---|---|---|
| Model Movie | `Movie` | Lưu `movieId`, `title`, `durationMinutes`. |
| Model Screen | `Screen` | Lưu thông tin phòng và map các `Seat`; từ chối seat ID trùng. |
| Model Showtime | `Showtime` | Liên kết phim, phòng, thời gian và trạng thái ghế của suất chiếu. |
| Model Seat | `Seat` | Lưu `seatId`, `PricingTier`; `price()` ủy quyền tính giá cho tier. |
| Model Booking | `Booking` | Lưu suất chiếu, các ghế đã chọn, discount; tính tiền và sinh confirmation. |
| Pricing tier | `PricingTier.calculatePrice()` và ba implementation | `StandardTier`, `PremiumTier`, `ReclinerTier` trả giá khác nhau qua cùng interface. |
| Seat locking | `Showtime.tryLockSeats()` | Kiểm tra tất cả ghế trước khi chuyển sang `LOCKED`. Method là `synchronized`. |
| Ngăn double-booking | `Showtime.isSeatAvailable()` và `tryLockSeats()` | Ném `IllegalStateException` nếu ghế không còn `AVAILABLE`. |
| Xác nhận booking | `Showtime.confirmBooking()` | Chuyển các ghế thuộc lock owner sang `BOOKED`. |
| Giải phóng lock | `Showtime.releaseSeats()` | Trả ghế đang khóa về `AVAILABLE`. |
| Student discount | `StudentDiscountPolicy.calculateDiscount()` | Trả 10% subtotal thông qua `DiscountPolicy`. |
| Confirmation | `Booking.generateConfirmation()` | In movie, time, seats, subtotal, discount và total. |
| Happy path | `Main.main()` | Khóa A1/A2, tạo booking B001, xác nhận và in confirmation. |
| Validation/error | `Main.main()` | Booking B002 cố khóa A1 và bị từ chối. |

## 5. Giới hạn đã biết

- Chưa có automated test trong baseline tuần 1; Nguyễn Thành Triệu phụ trách bổ sung executable scenarios trong commit riêng.
- `Showtime` hiện tham chiếu `Screen` và tạo state cho các seat của screen, chưa sở hữu trực tiếp một collection `Seat` riêng. Quan hệ composition theo yêu cầu đề bài cần được làm rõ hoặc cải tiến ở các tuần sau.
- Seat lock chưa có thời gian hết hạn.
- `synchronized` chỉ bảo vệ trong cùng một JVM, không giải quyết nhiều server.
- `Booking` chưa kiểm tra đầy đủ rằng các ghế được truyền vào đang được khóa bởi chính `bookingId` đó.
- Giá tiền đang dùng `double`; biểu diễn tiền bằng `long` hoặc `BigDecimal` sẽ an toàn hơn cho phiên bản sau.

## 6. Ghi chú về báo cáo nộp lại

Tài liệu này được bổ sung sau phản hồi của GVHD rằng repository chưa có source code và lịch sử commit tương ứng với nội dung báo cáo. Nội dung chỉ mô tả những gì có thể đối chiếu với source hiện tại. Các thay đổi sau phản hồi giữ nguyên ngày commit thực tế, không được backdate hoặc trình bày như lịch sử cũ.
