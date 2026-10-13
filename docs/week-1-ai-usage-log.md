# AI Usage Log - Tuần 1

## Nguyên tắc ghi nhận

Nhóm sử dụng AI để gợi ý kế hoạch, cấu trúc class và bản nháp tài liệu. Các
kết quả chỉ được chấp nhận sau khi đối chiếu đề bài, kiểm tra source, compile và
chạy scenario. Những nội dung không có bằng chứng trong repository không được
ghi là đã hoàn thành.

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
- Nhóm đã thay đổi: bổ sung test plan, syllabus mapping, checklist rubric và
  chuẩn bị vị trí nhận ảnh vẽ tay thật. Không tạo ảnh giả hoặc backdate commit.
- Cách kiểm chứng: so từng mục với Section 3.1 và 3.2 của rubric; kiểm tra các
  file bằng Git diff; chạy lại scenario trước khi hoàn tất addendum.

## Giới hạn của log hiện tại

File này chỉ ghi các tương tác liên quan đến tuần 1. Tuần 2, 3 và 4 phải có
entry riêng, sau đó được tổng hợp thành AI log hoàn chỉnh ở tuần 4.
