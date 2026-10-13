# AI Usage Log - Tuần 1

AI hỗ trợ lập kế hoạch, đề xuất cấu trúc lớp và soạn bản nháp tài liệu tuần 1.
Các mục dưới đây ghi nội dung đã sử dụng và cách kiểm chứng.

## W1-AI-01 - Lập kế hoạch và phân tích domain

- Ngày trao đổi ban đầu: chưa xác nhận từ lịch sử chat. Ảnh phản hồi của GVHD
  hiển thị ngày nộp báo cáo 04/10/2026; ngày nộp không được dùng làm ngày prompt.
- Công cụ: ChatGPT.
- Model hiển thị trong báo cáo lần đầu: GPT-5.6.
- Prompt tóm tắt: lập lộ trình bốn tuần cho Movie Ticket Booking System, xác
  định việc cần làm trong tuần đầu và hướng dẫn chia phần việc cho hai thành
  viên.
- AI đề xuất: các class `Movie`, `Screen`, `Showtime`, `Seat`, `Booking`; hai
  interface `PricingTier` và
  `DiscountPolicy`; ba trạng thái `AVAILABLE`, `LOCKED`, `BOOKED`; happy path
  và double-booking scenario.
- Nhóm đã lựa chọn/chỉnh sửa: chọn Java và console application; giới hạn không
  có database, GUI và thanh toán thật; đặt state ghế trong `Showtime`; chia
  Thắng phụ trách phân tích/tài liệu và Triệu phụ trách executable scenarios.
- Cách kiểm chứng: đối chiếu từng yêu cầu với
  `docs/week-1-problem-analysis.md`, compile source và chạy `cinema.Main` cùng
  `cinema.Week1Scenarios`.
- Bằng chứng: các commit của Thắng `a28a551`, `9a4f05b`, `3b7a8e4`; commit
  scenario của Triệu `4c491df` và [output tại bản tích hợp tuần 1](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/3bbeea8/evidence/week-1-console-output.txt).

## W1-AI-02 - Rà soát theo rubric

- Ngày: 13/10/2026.
- Công cụ: Codex/ChatGPT.
- Prompt tóm tắt: đọc rubric chính thức và kiểm tra những minh chứng tuần 1
  còn thiếu trong repository.
- AI chỉ ra: báo cáo cũ chưa có ảnh vẽ tay, worked example bằng tay, test plan,
  syllabus mapping và teach-back riêng của từng thành viên.
- Điểm AI/bản nháp ban đầu còn yếu: kế hoạch đầu tiên tập trung vào source và
  UML điện tử nhưng không nhắc đến ảnh phác thảo cá nhân vì rubric chưa được
  cung cấp. Nếu chỉ làm theo kế hoạch đó, nhóm sẽ thiếu bằng chứng A2 và A4.
- Tài liệu đã bổ sung: test plan, syllabus mapping và báo cáo cập nhật link
  commit. Ảnh vẽ tay và teach-back cá nhân vẫn đang chờ bổ sung.
- Cách kiểm chứng: so từng mục với Section 3.1 và 3.2 của rubric; kiểm tra các
  file bằng Git diff; chạy lại scenario trước khi hoàn tất addendum.

## W1-AI-03 - Bổ sung giải thích thiết kế cho phần của Thắng

- Ngày: 13/10/2026.
- Công cụ: Codex.
- Prompt: "vậy bạn giải thích cho tôi chưa? nếu chưa thì bổ sung vào nhánh
  week 1 cũ luôn nhé không tạo nhánh mới".
- Nội dung AI tạo: bản nháp bốn phần giải thích tại mục 9.1 của báo cáo
  tuần 1: pricing polymorphism, encapsulation của seat state, quan hệ has-a
  và state độc lập theo suất chiếu.
- Cách kiểm chứng: đọc `Booking`, `Seat`, `PremiumTier`, `Showtime` và
  `Screen` trên branch `week1-thang-analysis-report`; kiểm tra tên field,
  method, luồng gọi tính giá, private maps và cách constructor khởi tạo
  state theo từng suất chiếu; kiểm tra liên kết tài liệu và Git diff.
- Chỉnh sửa khi đối chiếu: nêu rõ `getSeatStates()` trả unmodifiable view;
  phân biệt list riêng với reference Seat được chia sẻ; giữ giới hạn
  strict composition của bản tuần 1.
- Trạng thái tại thời điểm tạo bản nháp: chưa có ảnh vẽ tay và trace trong
  repository. Ba ảnh do Thắng viết tay được bổ sung ở lần cập nhật tiếp theo.

## W1-AI-04 - Sắp xếp minh chứng viết tay vào báo cáo

- Ngày: 14/10/2026.
- Công cụ: Codex.
- Prompt tóm tắt: thêm ba ảnh viết tay của Thắng vào branch tuần 1 và chỉnh
  mục 9 cho phù hợp với minh chứng thực tế.
- Phần do sinh viên thực hiện: toàn bộ class diagram, phép tính giá và bảng
  trace trong ba ảnh do Nguyễn Đức Thắng viết tay.
- Phần AI hỗ trợ: đặt tên file dễ đọc, chèn ảnh vào báo cáo và rút gọn phần
  giải thích để liên hệ trực tiếp với class/method trong source tuần 1.
- Cách kiểm chứng: đối chiếu tên lớp trong ảnh với source; kiểm tra phép tính
  100.000 + 125.000 - 22.500 = 202.500 VND; kiểm tra trace B001/B002 với luồng
  `tryLockSeats()` và `confirmBooking()`. Các file ảnh được giữ nguyên nội dung.

Log này ghi các tương tác phục vụ tuần 1.
