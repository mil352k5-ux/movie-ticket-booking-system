# AI Usage Log - Tuần 2

## W2-AI-01 - Pricing và test tự động của Thắng

- Ngày trao đổi: chưa xác nhận từ lịch sử chat; các commit implementation được
  tạo ngày 10/10/2026.
- Công cụ: Codex/ChatGPT.
- Prompt tóm tắt: triển khai phần tuần 2 của Thắng, gồm build tự động, pricing
  tiers và test cho subtotal/discount.
- AI hỗ trợ: tạo cấu hình Maven/JUnit, đề xuất đổi `double` sang `long` và soạn
  test cho `PricingTier` cùng `Booking`.
- Phần được giữ lại sau kiểm tra: Maven với Java 17/JUnit 5, kiểu `long` cho VND,
  hai test class `PricingTierTest` và `BookingPricingTest`.
- Cách kiểm chứng: compile source; chạy 4 test trên nhánh
  `week2-thang-pricing-tests`; kiểm tra trực tiếp các kết quả 100.000, 125.000,
  150.000, 225.000, 22.500 và 202.500 VND.
- Điểm yếu của baseline có AI hỗ trợ: dùng `double` cho VND. Nhóm thay bằng
  `long` vì bài toán không cần phần lẻ nhỏ hơn 1 VND và test cần so sánh chính
  xác.

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
- AI tạo/gợi ý: `GroupDiscountPolicy`, validation trong `Movie`, `Screen`,
  `Seat`, `Showtime`, `Booking`, 5 tests trong
  [`GroupDiscountPolicyTest`](../src/test/java/cinema/GroupDiscountPolicyTest.java)
  và 15 tests trong
  [`DomainValidationTest`](../src/test/java/cinema/DomainValidationTest.java).
  AI cập nhật README và lưu evidence từ lệnh chạy; Booking tiếp tục gọi
  interface `DiscountPolicy`, không thêm nhánh xử lý theo subtype.
- Phần tôi thực hiện được ghi nhận trong chat: xác định phạm vi và yêu cầu
  kiểm chứng, trao đổi cách dùng branch, rồi thực hiện commit/push bằng các
  lệnh PowerShell được hướng dẫn. Source/tests hiện tại khớp implementation
  trong [`047199f`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/047199f);
  các commit sau trên nhánh chỉ sửa tài liệu. Việc đọc source và chạy test
  trong lượt triển khai của chat do Codex thực hiện; log không quy các thao
  tác này thành phần code tôi tự viết hoặc một lần tôi tự chạy test riêng.
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
- Nhận xét khi rà soát ngày 14/10/2026: bản tóm tắt AI ngày 11/10 ghi các lệnh
  kiểm chứng dưới nhãn "Phần sinh viên tự kiểm chứng", trong khi lịch sử chat
  cho thấy Codex chạy chúng. Log này sửa cách ghi tác giả thao tác và phân
  biệt hỗ trợ AI với việc sinh viên thực hiện. Đây là nhận xét của lần rà
  soát hiện tại, không phải một sửa đổi code đã được tôi ghi nhận trước đó.

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
