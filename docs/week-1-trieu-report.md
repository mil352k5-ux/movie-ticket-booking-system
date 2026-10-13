# Báo cáo cá nhân tuần 1 - Nguyễn Thành Triệu

- Họ tên: Nguyễn Thành Triệu.
- MSSV: 23110066.
- Nhóm: Movie Ticket Booking System, cùng Nguyễn Đức Thắng.
- Phần việc của tôi: executable scenarios cho happy path, double-booking và pricing polymorphism; hướng dẫn chạy và lưu output tuần 1.
- Nhánh: `week1/trieu-executable-scenarios`.
- Commit triển khai: [4c491df](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/4c491dfdd6667604e1e56d288d5fdd133b69ea17).
- Ngày cập nhật báo cáo: 14/10/2026. Bài viết tay không ghi ngày vẽ.

Trong báo cáo này, tôi trình bày ba scenario tuần 1 qua sơ đồ lớp, bảng trace và phần teach-back đã viết trên giấy. Ảnh được đính kèm nguyên bản để đối chiếu với code tại commit `4c491df`.

## Minh chứng viết tay

![Phác thảo các lớp và nhóm kiểm thử của Triệu](../evidence/week-1-hand-drawn/trieu-class-diagram-draft.png)

Sơ đồ tôi vẽ để ghi lại các lớp, method và ba nhóm kiểm thử. Bản phác thảo còn thiếu các implementation của interface và một số quan hệ. Các mũi tên `Movie → Booking`, `PricingTier → Showtime` và mũi tên đến ô “Test scenarios” chưa khớp với quan hệ trong code; quan hệ thực tế được ghi ở phần liên hệ OOP bên dưới.

![Phép tính giá và trace ba scenario tuần 1](../evidence/week-1-hand-drawn/trieu-scenario-trace.png)

Tôi ghi phép tính cho A1/A2, trace các lần giữ B1 và so sánh giá C1-C4. Dòng cuối ở mép dưới bên phải ảnh bị cắt một phần; ý về cùng contract `PricingTier` được trình bày tiếp trong trang teach-back.

![Teach-back viết tay của Triệu](../evidence/week-1-hand-drawn/trieu-teach-back.png)

Phần giải thích tôi viết về ghế `LOCKED`/`BOOKED`, exception khi đặt trùng, implementation C4 và giới hạn của kiểm thử tuần tự.

## Đối chiếu với source tuần 1

Các nội dung dưới đây đối chiếu với source tuần 1 tại commit `4c491df`, sử dụng `double` cho giá tiền.

- `happyPathScenario()`: chọn A1 Standard và A2 Premium, khóa cho `W1-B001` rồi tạo và xác nhận booking. Phép tính trong bài viết tay là 100.000 + 125.000 = 225.000 VND; giảm giá sinh viên 10% là 22.500 VND; total là 202.500 VND. Assertion kiểm tra phim, thời gian, ghế, total trong confirmation và state `BOOKED` của A1/A2. Bản trace ghi bước tính giá trước xác nhận; code kiểm tra giá sau xác nhận, nhưng tính giá không làm thay đổi state.
- `doubleBookingScenario()`: `W1-B002` khóa B1; `W1-B003` bị từ chối khi ghế là `LOCKED`. Sau khi xác nhận `W1-B002`, `W1-B004` cũng bị từ chối vì ghế là `BOOKED`. Test dùng `assertThrows(IllegalStateException.class, ...)` cho cả hai trường hợp. Bài viết tay ghi state không đổi khi bị từ chối; code ném exception trước bước cập nhật state, nhưng scenario chưa có assertion riêng so sánh state trước/sau từng lần từ chối.
- `pricingPolymorphismScenario()`: với base price 100.000 VND, C1/C2/C3 lần lượt có giá 100.000/125.000/150.000 VND. C4 dùng implementation vô danh của `PricingTier`, trả `basePrice * 0.80`, được kiểm tra là 80.000 VND qua cùng lời gọi `Seat.price()`.

Các mã `W1-B001`, `W1-B002`, `W1-B003`, `W1-B004` trong ảnh tương ứng trực tiếp với mã trong scenario. `Main` dùng mã `B001`/`B002` cho demo riêng, không phải mã của các scenario.

## Liên hệ OOP và kiểm thử

Trong phần teach-back, tôi ghi rằng C4 vẫn dùng được qua contract `PricingTier` và lời gọi `Seat.price()` không đổi. Trong code, `Seat` giữ reference kiểu `PricingTier` và gọi `calculatePrice(basePrice)` của implementation được truyền vào.

Tôi cũng ghi rằng cả ghế `LOCKED` và `BOOKED` phải chặn booking khác. `doubleBookingScenario()` kiểm tra điều này bằng exception mong đợi. Tuần 1 mới kiểm tra các thao tác tuần tự, chưa có test nhiều thread đồng thời.

Các quan hệ trong source dùng để đối chiếu sơ đồ: `Screen` chứa `Map<String, Seat>`; `Booking` chứa `List<Seat>` đã chọn và tham chiếu `Showtime`; `Showtime` tham chiếu `Movie`/`Screen` và quản lý state/lock owner riêng cho từng suất chiếu. `StandardTier`, `PremiumTier`, `ReclinerTier` triển khai `PricingTier`; `StudentDiscountPolicy`, `NoDiscountPolicy` triển khai `DiscountPolicy`. `Booking` không kế thừa `Seat`.

## Kết quả chạy kiểm chứng

Lần kiểm chứng ngày 14/10/2026 dùng source tuần 1 và `Week1Scenarios.java`, compile bằng `javac 21.0.10` vào một thư mục build mới được Git bỏ qua. Hai chương trình chạy bằng class vừa compile và đều trả exit code 0.

- `cinema.Main`: total 202.500 VND; từ chối giữ lại A1 với thông báo `Rejected correctly: Seat A1 is not available.`
- `cinema.Week1Scenarios`: cả ba scenario thành công; output cuối là `ALL WEEK 1 SCENARIOS PASSED: 3`.

Output của lần chạy này nằm trong [minh chứng kiểm chứng ngày 14/10/2026](../evidence/week-1-verification-2026-10-14.txt), tách riêng với [output tuần 1 đã lưu](../evidence/week-1-console-output.txt).

## Sử dụng AI

Tôi dùng Codex để tham khảo cách bố trí bản viết tay và hỗ trợ trình bày, đối chiếu báo cáo. Phần sơ đồ, trace và teach-back trong ảnh là bài tôi viết trên giấy. Bản Markdown được Codex hỗ trợ soạn và chỉnh từ nội dung tôi cung cấp; lần compile/run bổ sung và thao tác Git được Codex thực hiện trong quá trình hỗ trợ cập nhật hồ sơ.

| Thời điểm | Công cụ | Mục đích hỗ trợ | Nội dung hỗ trợ | Cách kiểm chứng |
|---|---|---|---|---|
| Trao đổi trước lần cập nhật này | Codex | Gợi ý cách bố trí sơ đồ và bảng trace dựa trên nội dung scenario đã cung cấp. | Mẫu tham khảo cho bản viết tay. | Khi đối chiếu với source, mẫu sơ đồ có một số mũi tên chưa đúng. Những điểm này được ghi rõ trong caption và phần liên hệ OOP. |
| 14/10/2026 | Codex | Hỗ trợ tổ chức minh chứng viết tay, diễn đạt báo cáo và đối chiếu yêu cầu tuần 1. | Soạn và chỉnh Markdown, nhúng bản sao ảnh gốc, đối chiếu class/method/scenario, chạy kiểm chứng và cập nhật nhánh Git. | Kiểm tra ảnh bằng SHA-256, kiểm tra đường dẫn Markdown, compile/run source đúng commit `4c491df` và lưu output riêng. Nội dung ảnh được giữ nguyên; các hạn chế của sơ đồ và test được nêu trong báo cáo. |
