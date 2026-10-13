# AI Usage Log - Tuần 3

## W3-AI-01 - Phần seat-lock expiration của Thắng

- Ngày trao đổi: 12/10/2026, bắt đầu lúc 13:58 (UTC+7).
- Mốc implementation: các commit ngày 12/10/2026.
- Công cụ/model: Codex (GPT-5.6 Sol).
- Yêu cầu trong trao đổi: "oke vậy triển khai prompt tuần 3 của tôi Thắng đi nha".
- Prompt tóm tắt: triển khai phần tuần 3 của Thắng, thêm SeatLock có expiresAt,
  inject Clock/Duration và viết test expiration/owner/all-or-nothing.
- AI hỗ trợ: soạn implementation, tests, tài liệu lifecycle và lệnh kiểm chứng.
- Phần Thắng phụ trách: chọn thời hạn mặc định 5 phút, duy trì trạng thái theo
  Showtime, dùng clock điều khiển được trong test và quản lý nhánh riêng.
- Kiểm chứng có AI hỗ trợ: 38 test pass, Main và ba executable scenarios
  chạy đúng; output tại
  [week-3-lock-test-output.txt](../evidence/week-3-lock-test-output.txt).
- Commit đối chiếu: `9bf4de4`, `ae55bb3`, `91caf56`, `d0f7488`.
- Điều chỉnh thiết kế: thay owner map bằng SeatLock để giữ owner và expiration
  cùng nhau; dùng snapshot Map.copyOf thay vì đưa map nội bộ ra ngoài.

## W3-AI-02 - Phần BookingService/concurrency của Triệu

- Ngày: 14/10/2026.
- Công cụ/model ghi trong log implementation của Triệu: Codex (GPT-5).
- Prompt tóm tắt: lấy main sau phần Thắng; xây dựng BookingStatus,
  BookingService, lifecycle tests và concurrency test bằng ExecutorService/
  CountDownLatch; cập nhật README, ghi chú, evidence; AI không commit/push.
- AI hỗ trợ: đề xuất và triển khai source/test, ghi chú concurrency và chạy
  kiểm chứng trong phạm vi prompt.
- Phần Triệu phụ trách: quy định luồng PENDING/CONFIRMED/CANCELLED/EXPIRED,
  yêu cầu test hai user tranh ghế cuối, review và thực hiện commit/push.
- Kiểm chứng có AI hỗ trợ: 38 test baseline, 48 test sau thay đổi, Main và
  Week1Scenarios; output tại
  [week-3-concurrency-test-output.txt](../evidence/week-3-concurrency-test-output.txt).
- Commit đối chiếu: `1eab3dc`; merge vào main: `cf8f68c`.
- Điểm yếu phát hiện khi rà soát bản này: BookingStatus đã có nhưng
  generateConfirmation chưa kiểm tra CONFIRMED. Ba test bổ sung cho
  PENDING/CANCELLED/EXPIRED thất bại; phần sửa ở commit `702ba74` của Thắng.
  Kết quả trước/sau nằm trong hai file evidence của lượt review.

## W3-AI-03 - Rà soát và bổ sung theo rubric

- Ngày: 14/10/2026. Công cụ: Codex.
- Prompt tóm tắt: kiểm tra phần tuần 3 của Triệu, tích hợp main, bám sát rubric,
  soạn báo cáo theo commit và mẫu trace viết tay riêng cho Thắng/Triệu.
- Điểm phát hiện: chưa có persistence/UML tuần 3; generateConfirmation chưa
  kiểm tra status; ID booking có thể được dùng lại; confirm/cancel chưa khóa
  toàn bộ workflow trên cùng Booking.
- Kiểm chứng trước khi sửa: ba regression tests về confirmation và hai tests
  ID trùng thất bại trên source trước khi thêm guard. Output tại
  [week-3-review-regressions.txt](../evidence/week-3-review-regressions.txt).
- AI hỗ trợ: triển khai guard/state locking, persistence UTF-8, console tương
  tác và test bổ sung; soạn report, UML, phần khai báo AI và mẫu trace giấy.
- Phần Thắng phụ trách trong lượt bổ sung: đối chiếu yêu cầu rubric, giữ phạm
  vi console Java/file history và thực hiện tiếp trên nhánh tuần 3 cũ với
  Codex hỗ trợ triển khai, kiểm thử và cập nhật tài liệu.
- Kiểm chứng sau sửa: 66 test pass, Main đọc lại file bằng đúng confirmation,
  console booking A1/A2 có total 202.500 VND, ba scenario tuần 1 pass. Output
  tại [week-3-review-test-output.txt](../evidence/week-3-review-test-output.txt).
- Commit đối chiếu: `702ba74` (invariants), `af75703` (persistence), `f3ddfda`
  (console), `1e6dfed` (giờ chiếu mẫu). Các lỗi và refactoring được giải thích
  trong báo cáo tuần 3.
- Giới hạn được giữ rõ: single-JVM locking; file history không khôi phục state
  ghế khi restart; chưa có database transaction hoặc GUI/thanh toán thật.
