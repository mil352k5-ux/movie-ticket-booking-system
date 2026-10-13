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

- Commit được kiểm tra: `047199f` của Nguyễn Thành Triệu.
- Nội dung xác minh từ diff: thêm `GroupDiscountPolicy`, validation trong năm
  domain class, 5 group-discount tests và 15 validation tests.
- Nhật ký hiện tại không có bản prompt/tool gốc mà Triệu đã dùng. Không suy đoán
  hoặc ghi thay thông tin này. Nếu Triệu có dùng AI, Triệu cần bổ sung đúng công
  cụ, prompt và phần đã tự sửa; nếu không dùng thì ghi rõ không dùng AI.
- Cách kiểm chứng code: đọc diff của commit, chạy 24 test trên bản tích hợp và
  so kết quả với `evidence/week-2-test-output.txt`.

## W2-AI-03 - Rà soát lại theo rubric

- Ngày: 14/10/2026.
- Công cụ: Codex.
- Prompt: kiểm tra lại repo tuần 2 của Thắng và Triệu theo rubric, thiếu phần nào
  có thể xác minh thì bổ sung.
- Kết quả rà soát: phần code, invariant tests và Git attribution đã có; repo còn
  thiếu báo cáo tuần 2, teach-back, phản biện AI và syllabus mapping.
- Phần bổ sung: `docs/weekly-report-week-2.md`, log này và liên kết từ README.
- Cách kiểm chứng: đối chiếu hai branch/commit, đọc source và test, chạy lại 4/4
  test trên nhánh Thắng và 24/24 test trên bản tích hợp; kiểm tra Markdown links
  và Git diff trước khi commit.

Log chỉ ghi những thông tin có thể đối chiếu. Phần giải trình cá nhân vẫn yêu
cầu từng thành viên hiểu code và tự trả lời khi GVHD kiểm tra.
