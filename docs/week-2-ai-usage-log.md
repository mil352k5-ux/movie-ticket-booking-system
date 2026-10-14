# AI Usage Log - Tuần 2

Nguyễn Đức Thắng - 23110062; Nguyễn Thành Triệu - 23110066.

## 1. Thắng - build, pricing và tests

- Mốc commit: 10/10/2026.
- Công cụ/model: ChatGPT/Codex, GPT-5.6 Sol High, theo khai báo trong báo cáo tuần 2 đã nộp.
- Prompt tóm tắt: thiết lập Maven/JUnit, chuyển API tiền sang long và kiểm thử PricingTier/subtotal/discount.
- AI hỗ trợ: soạn cấu hình build, sửa API pricing/discount, cập nhật Week1Scenarios và triển khai PricingTierTest/BookingPricingTest.
- Phần Thắng phụ trách: phạm vi build/pricing của nhóm, thống nhất dùng VND nguyên và cung cấp ảnh viết tay tính giá các tier, booking A1/A2.
- Kiểm chứng có AI hỗ trợ: Codex chạy 4 test trên nhánh Thắng. Giá Standard/Premium/Recliner là 100.000/125.000/150.000 VND; tier mới 80% là 80.000 VND; student booking có total 202.500 VND.
- Điều chỉnh: dùng long thay double và quy định làm tròn xuống đến 1 VND; kiểm tra kết quả bằng assertion thay vì chỉ đọc output.
- Commit đối chiếu: [c9e61bc](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c9e61bc), [7b14e1a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/7b14e1a), [2038061](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/2038061).
- Minh chứng cá nhân: [ảnh viết tay của Thắng](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/3eadfa6/evidence/week-2-handwritten/thang-pricing-and-booking-trace.jpg).

## 2. Triệu - discount và validation

- Ngày trao đổi: 11/10/2026.
- Công cụ/model: Codex desktop, GPT-5.6 Sol; Triệu xác nhận model trong log ngày 14/10/2026.
- Prompt tóm tắt: bổ sung group discount 5% từ 5 ghế, validation tại domain class và tests cho các biên/lỗi, giữ nền tảng pricing/build đã tích hợp.
- AI hỗ trợ: triển khai GroupDiscountPolicy, validation trong Movie/Screen/Seat/Showtime/Booking, 5 group-discount tests và 15 domain-validation tests; cập nhật README và lưu output chạy.
- Phần Triệu phụ trách: xác định quy tắc discount và yêu cầu validation/test; quản lý nhánh, commit/push và cung cấp bài viết tay 4/5/6 ghế cùng trace khóa A1/A2.
- Kiểm chứng có AI hỗ trợ: Codex chạy Maven, Main và Week1Scenarios; bản tích hợp có 24 test pass và 3/3 scenarios pass.
- Điều chỉnh thiết kế: Booking dùng cùng interface DiscountPolicy; Showtime kiểm tra toàn bộ ghế trước khi khóa để thao tác nhiều ghế vẫn all-or-nothing.
- Commit đối chiếu: [047199f](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/047199f).
- Minh chứng: [output tuần 2](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/047199f/evidence/week-2-test-output.txt), [ảnh viết tay của Triệu](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/0fd82a2/evidence/week-2-hand-drawn/trieu-group-discount-seat-lock-trace.png).

## 3. Rà soát và hoàn thiện hồ sơ

- Ngày: 14/10/2026. Công cụ: Codex; model các lượt hiệu đính tài liệu chưa có thông tin xác nhận riêng.
- Prompt tóm tắt: kiểm tra rubric, đối chiếu báo cáo đã nộp với source/Git history và bổ sung minh chứng cá nhân.
- AI hỗ trợ: chỉnh report/log, bổ sung Self-Explanation và syllabus mapping, đối chiếu phép tính/trace và liên kết ảnh gốc. Nhóm cung cấp báo cáo đã nộp, thông tin model và ảnh viết tay.
- Điểm sửa khi đối chiếu: bản tóm tắt cũ chưa ghi đúng người thực thi kiểm chứng; đã đối chiếu lại với lịch sử phiên làm việc và Git history.
- Kiểm chứng: đối chiếu commit của từng người, đọc source/test, kiểm tra liên kết và Git diff; lần chạy Maven bổ sung của Codex có 24 test pass. Output lịch sử ngày 11/10 được giữ nguyên, không thay bằng kết quả mới.
- Ảnh được đối chiếu và lưu nguyên bản; thao tác thêm ảnh không được tính là một lần chạy test mới.
- Commit hồ sơ: [f9f5cbf](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/f9f5cbf), [a8e2bff](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a8e2bff), [ac2a552](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/ac2a552), [0fd82a2](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/0fd82a2).
