# AI Usage Log - Tuần 1

Nguyễn Đức Thắng - 23110062; Nguyễn Thành Triệu - 23110066.

## 1. Kế hoạch và hồ sơ thiết kế

- Công cụ: ChatGPT/Codex. Model ghi trong báo cáo ban đầu: GPT-5.6.
- Mốc tài liệu: 10/10/2026. Ngày của phiên lập kế hoạch ban đầu chưa có thông tin xác nhận.
- Prompt tóm tắt: lập lộ trình bốn tuần, phân chia công việc và tổ chức hồ sơ thiết kế cho Movie Ticket Booking System.
- AI hỗ trợ: đề xuất các lớp Movie, Screen, Seat, Showtime, Booking; interface PricingTier/DiscountPolicy; use case, UML, Self-Explanation và hướng dẫn chạy demo.
- Phần nhóm phụ trách: lựa chọn Java console, giới hạn phạm vi; Thắng phụ trách phân tích/tài liệu, Triệu phụ trách executable scenarios. Trạng thái ghế được quản lý theo Showtime.
- Kiểm chứng có AI hỗ trợ: đối chiếu yêu cầu với source và chạy Main/Week1Scenarios. Happy path có total 202.500 VND; booking trùng bị từ chối; thay implementation PricingTier làm giá thay đổi qua cùng API.
- Commit đối chiếu: [a28a551](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a28a551), [9a4f05b](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9a4f05b), [3b7a8e4](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/3b7a8e4); scenarios của Triệu: [4c491df](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/4c491df).
- Minh chứng: [output bản tích hợp tuần 1](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/3bbeea8/evidence/week-1-console-output.txt).

## 2. Rà soát rubric và minh chứng cá nhân

- Ngày: 13-14/10/2026. Công cụ: Codex; các lượt hiệu đính tài liệu chưa có model được xác nhận riêng.
- Prompt tóm tắt: đối chiếu rubric, bổ sung test plan/syllabus mapping, giải thích thiết kế và liên kết ảnh viết tay với source tuần 1.
- AI hỗ trợ: soạn và chỉnh báo cáo, giải thích các call site OOP, gợi ý bố cục ví dụ viết tay, đối chiếu ảnh/source và tổ chức evidence.
- Phần sinh viên cung cấp: Thắng gửi ba ảnh class diagram, phép tính và trace B001/B002; Triệu gửi ảnh sơ đồ, trace và teach-back riêng. Các mẫu tham khảo có AI hỗ trợ; ảnh là bài viết trên giấy do sinh viên cung cấp, được lưu nguyên bản.
- Điều chỉnh thiết kế khi đối chiếu: nêu rõ getSeatStates() trả unmodifiable view; Screen/Booking giữ reference Seat được chia sẻ, không phải ownership tuyệt đối.
- Điểm yếu của kế hoạch ban đầu: chưa bao quát minh chứng viết tay và syllabus mapping khi chưa có rubric. Bản sơ đồ tham khảo cũng có quan hệ cần sửa; caption và phần giải thích của Triệu ghi rõ quan hệ thực tế.
- Kiểm chứng: đối chiếu Section 3.1-3.2 của rubric, class/method và Git diff; kiểm tra phép tính 100.000 + 125.000 - 22.500 = 202.500 VND, trace ghế và liên kết ảnh. Lần compile/run bổ sung trong hồ sơ Triệu do Codex thực hiện, cả ba scenario pass.
- Hồ sơ Thắng: [báo cáo và ba ảnh viết tay](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/6592fc0/docs/weekly-report-week-1-resubmission.md).
- Hồ sơ Triệu: [báo cáo cá nhân, ảnh và khai báo AI](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/4cdc3d0/docs/week-1-trieu-report.md).
- Output bổ sung: [kiểm chứng ngày 14/10/2026](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/4cdc3d0/evidence/week-1-verification-2026-10-14.txt).
