# AI Usage Log - Tuần 2

## W2-AI-01 - Pricing và test tự động của Thắng

- Mốc commit: 10/10/2026.
- Công cụ/model: ChatGPT/Codex, GPT-5.6 Sol High, theo khai báo trong báo cáo
  tuần 2 đã nộp.
- Prompt tóm tắt: triển khai phần tuần 2 của Thắng, gồm build tự động, pricing
  tiers và test cho subtotal/discount.
- AI hỗ trợ: đề xuất cấu hình Maven/JUnit 5, các vị trí cần đổi kiểu tiền sang
  `long` và cấu trúc test; hỗ trợ soạn cấu hình, thay đổi API và mã test theo
  yêu cầu của Thắng.
- Phần Thắng phụ trách: hoàn thiện cấu hình build, thống nhất API pricing và
  discount dùng `long`, cập nhật `Week1Scenarios` và kiểm tra các trường hợp
  trong `PricingTierTest`, `BookingPricingTest`.
- Kiểm chứng có AI hỗ trợ: compile source; chạy 4 test trên nhánh
  `week2-thang-pricing-tests`; kiểm tra trực tiếp các kết quả 100.000, 125.000,
  150.000, 225.000, 22.500 và 202.500 VND.
- Lựa chọn sau khi rà soát: dùng `long` thay cho `double` để biểu diễn VND
  nguyên, quy định làm tròn xuống đến 1 VND và kiểm tra kết quả bằng assertion.
- Commit đối chiếu: [`c9e61bc`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c9e61bc),
  [`7b14e1a`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/7b14e1a),
  [`2038061`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/2038061).

## W2-AI-02 - Phần discount/validation của Triệu

- Sinh viên: Nguyễn Thành Triệu - 23110066.
- Ngày trao đổi: 11/10/2026, đối chiếu lịch sử cuộc trò chuyện này.
- Công cụ/model: Codex desktop, model `5.6 sol`; tên model được tôi xác nhận
  khi hoàn thiện log ngày 14/10/2026.
- Prompt tóm tắt từ yêu cầu đã gửi: lấy `main` sau khi phần tuần 2 của Thắng
  đã merge; thêm group discount 5% từ 5 ghế, validation tại domain class và
  JUnit tests cho discount/invalid operations; giữ API `long`, pricing tests,
  Week1Scenarios và Main; cập nhật README, lưu output Maven thật, giới hạn
  công việc ở tuần 2.
- AI hỗ trợ tạo mã và test: `GroupDiscountPolicy`, validation trong `Movie`, `Screen`,
  `Seat`, `Showtime`, `Booking`, 5 tests trong
  [`GroupDiscountPolicyTest`](../src/test/java/cinema/GroupDiscountPolicyTest.java)
  và 15 tests trong
  [`DomainValidationTest`](../src/test/java/cinema/DomainValidationTest.java).
  AI cập nhật README và lưu evidence từ lệnh chạy; Booking tiếp tục gọi
  interface `DiscountPolicy`, không thêm nhánh xử lý theo subtype.
- Phần tôi thực hiện: xác định quy tắc giảm 5% từ 5 ghế, yêu cầu validation
  tại class sở hữu dữ liệu và các test biên/lỗi; quản lý nhánh và thực hiện
  commit/push theo hướng dẫn. Kết quả triển khai được ghi trong
  [`047199f`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/047199f).
- Minh chứng bổ sung ngày 14/10/2026: tôi cung cấp
  [ảnh bài viết tay](../evidence/week-2-hand-drawn/trieu-group-discount-seat-lock-trace.png)
  tự tính các trường hợp 4/5/6 ghế và trace khóa A1/A2. Phép tính/trace trên
  giấy do tôi viết; Codex đối chiếu và đưa ảnh gốc vào hồ sơ tuần 2.
- Phân định phần việc: Maven/JUnit thuộc
  [`c9e61bc`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c9e61bc),
  chuyển tiền sang `long` thuộc
  [`7b14e1a`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/7b14e1a),
  pricing tests thuộc
  [`2038061`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/2038061)
  của Thắng. Tôi sử dụng nền tảng đã merge này cho phần discount/validation.
- Cách kiểm chứng có AI hỗ trợ ngày 11/10/2026: chạy Maven `clean test`,
  Week1Scenarios, Main và `git diff --check`. Maven có 24 tests, 0 failure,
  0 error, 0 skipped; Week1Scenarios pass 3/3. Output lịch sử nằm tại
  [`evidence/week-2-test-output.txt`](../evidence/week-2-test-output.txt).
- Điểm chỉnh khi rà soát ngày 14/10/2026: bản tóm tắt AI trước đó ghi các lệnh
  chạy test là "sinh viên tự kiểm chứng", dù Codex thực thi chúng trong phiên
  làm việc. Log được sửa thành kiểm chứng có AI hỗ trợ và ghi riêng phần
  yêu cầu, quản lý nhánh, commit/push của sinh viên.

## W2-AI-03 - Rà soát lại theo rubric

- Ngày: 14/10/2026.
- Công cụ: Codex.
- Prompt: kiểm tra lại repo tuần 2 của Thắng và Triệu theo rubric, thiếu phần nào
  có thể xác minh thì bổ sung.
- Kết quả rà soát: code, invariant tests và Git attribution phù hợp; bổ sung báo
  cáo tuần 2, phần giải trình, phản biện AI và syllabus mapping.
- File cập nhật: `docs/weekly-report-week-2.md`, log này và README.
- Cách kiểm chứng: đối chiếu hai branch/commit, đọc source và test, chạy lại 4/4
  test trên nhánh Thắng và 24/24 test trên bản tích hợp; kiểm tra Markdown links
  và Git diff trước khi commit.

## W2-AI-04 - Hoàn thiện log và giải trình của Triệu

- Ngày: 14/10/2026.
- Công cụ: Codex desktop; tên model cụ thể của lượt chỉnh tài liệu không được
  ghi trong yêu cầu hoặc phản hồi của sinh viên.
- Prompt tóm tắt: hoàn thiện W2-AI-02 và giải trình của Triệu từ lịch sử chat,
  commit `047199f`, source/tests; giữ mục của Thắng và evidence lịch sử; commit
  và push trên nhánh hiện có `week2-trieu-discount-validation`.
- AI hỗ trợ: đối chiếu phần việc, soạn lại log, giải thích policy/invariant,
  các ví dụ 4/5/6 ghế và hai tests về khóa ghế. Sinh viên xác nhận model của
  lần triển khai là `5.6 sol` và yêu cầu đối chiếu nhánh để kiểm tra thay đổi.
- Kiểm chứng mới: Codex chạy Maven 3.9.9 với `mvn test` ngày 14/10/2026;
  24 tests, 0 failure, 0 error, 0 skipped, `BUILD SUCCESS`. Kiểm tra liên kết
  Markdown và `git diff --check`; file output ngày 11/10/2026 giữ nguyên.
- File chỉnh: log này và
  [`docs/weekly-report-week-2.md`](weekly-report-week-2.md). Source/tests
  giữ nguyên; phạm vi lần này là tài liệu và khai báo sử dụng AI.

## W2-AI-05 - Đối chiếu với báo cáo tuần 2 đã nộp

- Ngày: 14/10/2026.
- Công cụ: Codex.
- Prompt tóm tắt: đối chiếu AI Usage Log với báo cáo tuần 2 đã nộp; diễn đạt rõ
  phần AI hỗ trợ, phần việc của từng sinh viên và cách kiểm chứng.
- AI hỗ trợ: rút gọn cách trình bày, bổ sung thông tin công cụ/model của Thắng
  từ báo cáo và liên kết commit; giữ các thông tin Triệu đã xác nhận.
- Kiểm chứng: so nội dung với báo cáo do nhóm cung cấp và commit `a8e2bff` của
  Triệu; kiểm tra liên kết Markdown và Git diff. Lần này chỉ chỉnh tài liệu.

## W2-AI-06 - Bổ sung ảnh viết tay của Triệu

- Ngày: 14/10/2026.
- Công cụ: Codex desktop.
- Prompt tóm tắt: đưa ảnh bài viết tay tuần 2 do Triệu cung cấp lên nhánh
  `week2-trieu-discount-validation`.
- AI hỗ trợ: liên kết ảnh trong báo cáo và README, đối chiếu phép tính/trace
  với code; ghi chú hai dòng chữ số dễ đọc nhầm. Ảnh gốc giữ nguyên, AI không
  tạo hoặc sửa nội dung viết tay của sinh viên.
- Kiểm chứng: SHA-256 của file evidence khớp ảnh được gửi; kiểm tra liên kết
  Markdown và `git diff --check`. Lần này chỉ thêm minh chứng và tài liệu,
  không có lần chạy test mới; output ngày 11/10/2026 giữ nguyên.
