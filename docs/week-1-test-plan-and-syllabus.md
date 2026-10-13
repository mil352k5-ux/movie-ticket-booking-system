# Test Plan và Syllabus Mapping - Tuần 1

## 1. Mục tiêu kiểm thử

Kế hoạch kiểm thử tuần 1 tập trung vào ba hành vi tối thiểu của đề tài:

1. Hoàn thành một lượt đặt vé từ lúc tạo suất chiếu đến lúc sinh confirmation.
2. Từ chối một booking khác khi ghế đã bị giữ hoặc đã được đặt.
3. Chứng minh `PricingTier` có thể thay đổi cách tính giá mà không sửa logic của `Seat` hoặc `Booking`.

Các scenario chạy được nằm trong `src/test/java/cinema/Week1Scenarios.java`.
Output thực tế được lưu tại `evidence/week-1-console-output.txt`.

## 2. Danh sách test case

| ID | Tình huống | Dữ liệu chính | Kết quả mong đợi | Trạng thái tuần 1 |
|---|---|---|---|---|
| W1-T01 | Happy path đặt A1 và A2 | Standard 100.000 VND, Premium 125.000 VND, student discount 10% | Hai ghế chuyển sang `BOOKED`; confirmation có phim, giờ, ghế và tổng 202.500 VND | Đã chạy |
| W1-T02 | Hai booking tranh cùng ghế | B002 giữ B1, sau đó B003 thử giữ B1 | B003 bị từ chối bằng `IllegalStateException` | Đã chạy |
| W1-T03 | Thử đặt lại ghế đã xác nhận | B002 xác nhận B1, sau đó B004 thử giữ B1 | B004 bị từ chối bằng `IllegalStateException` | Đã chạy |
| W1-T04 | Ba pricing tier qua cùng contract | Base price 100.000 VND | Standard 100.000; Premium 125.000; Recliner 150.000 | Đã chạy |
| W1-T05 | Thay implementation của `PricingTier` | Tier kiểm thử trả 80% base price | `Seat.price()` trả 80.000 VND mà không sửa `Seat` | Đã chạy |
| W1-T06 | Release lock sai owner | Một booking khác gọi release | Ghế không được giải phóng | Dự kiến cho tuần sau |
| W1-T07 | Ghế không tồn tại | Chọn seat ID ngoài `Screen` | Hệ thống từ chối bằng exception rõ ràng | Dự kiến cho tuần sau |
| W1-T08 | Nhiều ghế, một ghế không hợp lệ | Chọn một ghế hợp lệ và một ghế không tồn tại | Không ghế nào bị khóa; thao tác all-or-nothing | Dự kiến cho tuần sau |

Các test case “dự kiến” là kế hoạch tại thời điểm tuần 1, không được trình bày
như kết quả đã chạy trong tuần 1.

## 3. Worked example dùng để tự trace

Input:

- Base price: 100.000 VND.
- A1 dùng `StandardTier`: 100.000 VND.
- A2 dùng `PremiumTier`: 125.000 VND.
- Chính sách giảm giá sinh viên: 10%.

Tính tay:

```text
Subtotal = 100.000 + 125.000 = 225.000 VND
Discount = 225.000 x 10% = 22.500 VND
Total    = 225.000 - 22.500 = 202.500 VND
```

Trace trạng thái:

```text
A1: AVAILABLE -> LOCKED(B001) -> BOOKED
A2: AVAILABLE -> LOCKED(B001) -> BOOKED
B002 thử khóa A1 -> bị từ chối vì A1 không còn AVAILABLE
```

Mỗi thành viên cần tự viết lại phép tính và trace này bằng tay trong ảnh minh
chứng của mình, không sử dụng ảnh tạo bằng phần mềm.

## 4. Syllabus mapping cho Project 12 - Movie tickets

Theo Appendix A của rubric, Project 12 phải thể hiện `Polymorphism` và
`Composition`; các hành vi bắt buộc cần chứng minh là seat tiers, locking và
discounts. Nhóm câu hỏi bảo vệ của đề tài là `POLY`.

| Nội dung syllabus | Vị trí trong project | Minh chứng cụ thể |
|---|---|---|
| Polymorphism qua interface | `PricingTier.calculatePrice()`; `StandardTier`, `PremiumTier`, `ReclinerTier`; `Seat.price()` | W1-T04 gọi cùng contract nhưng nhận ba mức giá khác nhau. |
| Polymorphism qua Strategy | `DiscountPolicy`; `StudentDiscountPolicy`; `NoDiscountPolicy`; `Booking.calculateDiscount()` | `Booking` sử dụng policy được truyền vào và không kiểm tra kiểu concrete. |
| Composition/has-a | `Screen.seats`; `Booking.selectedSeats`; `Showtime` tham chiếu `Movie` và `Screen`, đồng thời sở hữu state theo seat ID | Object lớn được tạo từ các domain object nhỏ có trách nhiệm riêng. |
| Seat locking | `Showtime.tryLockSeats()`, `confirmBooking()`, `releaseSeats()` | W1-T02 và W1-T03 chứng minh ghế không thể được hai booking sử dụng. |
| Discounts | `DiscountPolicy.calculateDiscount()` và `StudentDiscountPolicy` | W1-T01 tính giảm 22.500 VND và total 202.500 VND. |

`Showtime` không sở hữu vòng đời của các `Seat` vật lý; các seat thuộc
`Screen`. Vì vậy tài liệu dùng thuật ngữ composition/has-a cho cấu trúc object
và chỉ khẳng định `Showtime` sở hữu trạng thái ghế của riêng suất chiếu.

## 5. Câu hỏi tự kiểm tra trước weekly check-in

1. Vì sao `PricingTier` là interface thay vì đặt `if/else` theo tên loại ghế trong `Booking`?
2. Khi thêm tier mới, class nào cần thêm và class nào không cần sửa?
3. `Showtime` bảo vệ invariant nào khi thực hiện `tryLockSeats()`?
4. Vì sao trạng thái ghế phải gắn với `Showtime`, không gắn trực tiếp với ghế vật lý?
5. Quan hệ giữa `Screen` và `Seat` khác quan hệ giữa `Seat` và `PricingTier` như thế nào?

