# Hướng dẫn ảnh vẽ tay tuần 1

Thư mục này chỉ nhận ảnh chụp bản vẽ thật của từng thành viên. Không dùng ảnh
tạo bằng AI hoặc sơ đồ được dựng bằng phần mềm rồi giả lập nét viết tay.

## File cần bổ sung

- `thang-class-sketch.jpg` - Nguyễn Đức Thắng tự vẽ.
- `trieu-class-sketch.jpg` - Nguyễn Thành Triệu tự vẽ.

## Nội dung tối thiểu trên từng ảnh

1. Họ tên, MSSV, “Tuần 1” và ngày thực tế vẽ.
2. Các class `Movie`, `Screen`, `Seat`, `Showtime`, `Booking`.
3. Interface `PricingTier` với `StandardTier`, `PremiumTier`, `ReclinerTier`.
4. Interface `DiscountPolicy` với ít nhất `StudentDiscountPolicy` và
   `NoDiscountPolicy`.
5. Mũi tên hoặc ghi chú phân biệt:
   - `implements` từ các tier/policy tới interface;
   - `Screen` has-a nhiều `Seat`;
   - `Booking` has-a danh sách ghế đã chọn;
   - `Showtime` liên kết một `Movie` và một `Screen`, đồng thời quản lý state
     của ghế theo suất chiếu.
6. Một góc giấy tự trace:

```text
A1 Standard = 100.000
A2 Premium  = 125.000
Subtotal    = 225.000
Student 10% = 22.500
Total       = 202.500 VND

A1/A2: AVAILABLE -> LOCKED(B001) -> BOOKED
B002 thử A1 -> REJECTED
```

## Yêu cầu ảnh

- Chụp rõ toàn bộ tờ giấy, không cắt mất tên/ngày.
- Chữ và mũi tên đọc được khi mở ảnh ở kích thước bình thường.
- Mỗi người dùng bản vẽ riêng để phục vụ phần teach-back cá nhân.
