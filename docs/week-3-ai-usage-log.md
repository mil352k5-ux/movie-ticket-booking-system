# AI Usage Log - Tuần 3

Nguyễn Đức Thắng - 23110062; Nguyễn Thành Triệu - 23110066.

## 1. Thắng - seat-lock expiration

- Ngày trao đổi: 12/10/2026, 13:58 (UTC+7).
- Công cụ/model: Codex, GPT-5.6 Sol, theo xác nhận của Thắng.
- Prompt tóm tắt: thêm SeatLock, Clock/Duration và tests cho expiration/ownership/all-or-nothing.
- AI hỗ trợ: triển khai source/tests, tài liệu lifecycle và chạy kiểm chứng.
- Phần Thắng phụ trách: seat-lock expiration; cung cấp ảnh viết tay TTL, trace B001/B002 và refactoring.
- Điều chỉnh: SeatLock thay owner map; TTL 5 phút, injected Clock và snapshot Map.copyOf.
- Kiểm chứng: Codex chạy 38 test, Main và 3/3 Week1Scenarios; kiểm tra lock hết hạn, đúng owner và BOOKED không tự trở về AVAILABLE.
- Commit: [9bf4de4](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9bf4de4), [ae55bb3](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/ae55bb3), [91caf56](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/91caf56), [d0f7488](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/d0f7488).
- Minh chứng: [output 38 test](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/d0f7488/evidence/week-3-lock-test-output.txt), [ảnh viết tay Thắng](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/7119361/evidence/week-3-handwritten/thang-seat-lock-expiration-trace.png).

## 2. Triệu - booking lifecycle và concurrency

- Ngày: 14/10/2026.
- Công cụ/model: Codex, GPT-5, theo log implementation của Triệu.
- Prompt tóm tắt: thêm BookingStatus/BookingService, lifecycle tests và test tranh ghế cuối bằng ExecutorService/CountDownLatch.
- AI hỗ trợ: triển khai source/tests, ghi chú concurrency, README và kiểm chứng; không commit/push trong phiên triển khai.
- Phần Triệu phụ trách theo log cá nhân: yêu cầu lifecycle/test, review và tự commit/push.
- Kiểm chứng: Codex chạy 38 test baseline, 48 test sau thay đổi, Main/Week1Scenarios. Test tranh ghế cuối có một người thành công, một người bị từ chối, không cố định người thắng.
- Commit: [1eab3dc](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/1eab3dc); merge [cf8f68c](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/cf8f68c).
- Minh chứng: [output 48 test](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/1eab3dc/evidence/week-3-concurrency-test-output.txt).

## 3. Rà soát và bổ sung rubric

- Ngày: 14/10/2026. Công cụ: Codex; model phiên rà soát chưa được khai báo riêng.
- Prompt tóm tắt: đối chiếu rubric, bổ sung persistence/UML/refactoring, edge tests và báo cáo theo commit.
- AI hỗ trợ: triển khai guard CONFIRMED/ID, đồng bộ confirm/cancel, BookingHistory UTF-8, console/tests; soạn report/UML và mẫu trace.
- Phần Thắng phụ trách: triển khai, hoàn thiện và tích hợp phần bổ sung trong phạm vi Java console/file history; đối chiếu rubric và cung cấp minh chứng cá nhân.
- Điểm yếu phát hiện: generateConfirmation thiếu kiểm tra CONFIRMED; ID trùng có thể ghép nhầm tập ghế. Ba tests confirmation và hai tests ID trùng thất bại trước sửa.
- Kiểm chứng sau sửa: Codex chạy 66 test, Main lưu/đọc confirmation, console A1/A2 có total 202.500 VND và 3/3 Week1Scenarios pass; thêm tests confirm/cancel và hai confirm đồng thời.
- Minh chứng: [trước sửa](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/e277925/evidence/week-3-review-regressions.txt), [sau sửa](https://github.com/mil352k5-ux/movie-ticket-booking-system/blob/e277925/evidence/week-3-review-test-output.txt).
- Commit: [702ba74](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/702ba74), [af75703](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/af75703), [f3ddfda](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/f3ddfda), [1e6dfed](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/1e6dfed).
- Giới hạn: khóa trong một JVM; file history không khôi phục ghế/lock khi restart; chưa có transaction chung cho file và booking.
- Ảnh viết tay do Nguyễn Đức Thắng thực hiện và cung cấp, được giữ nguyên bản.
