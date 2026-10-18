# AI Usage Log - Tuần 4 - Thắng

Nguyễn Đức Thắng — MSSV 23110062.

## 1. Member discount

- Ngày: 14/10/2026 (UTC+7).
- Công cụ/model: Codex, GPT-6.1 Sol, mức suy luận Max.
- Prompt tóm tắt: thêm Member discount 15%, lựa chọn ở console và test chứng minh có thể mở rộng discount mà không sửa các class nghiệp vụ cũ.
- Phần Thắng phụ trách: Member discount, lựa chọn ở console, test liên quan và hồ sơ cá nhân trên nhánh `week4-thang-member-discount`.
- AI hỗ trợ: triển khai policy, test và tích hợp console; soạn tài liệu giải thích thiết kế, ví dụ tính tiền/trace, câu hỏi bảo vệ và bản nháp reflection; hỗ trợ kiểm thử và thao tác Git.
- Điều chỉnh kỹ thuật: tách thương/dư để tránh overflow và giữ đúng phần dư. Case 107 VND cho discount 16 VND; thêm subtotal âm, biên `Long.MAX_VALUE` và đối chiếu công thức bằng `BigInteger`. Phép nhân trực tiếp được kiểm tra riêng trên JVM để thấy lỗi overflow.
- Kiểm chứng: baseline 66 test, sau policy 77 test, sau console 81 test; tất cả pass. Main lưu/đọc confirmation Student, Week1Scenarios đạt 3/3. A1/A2 với Member có total 191.250 VND, đúng với nội dung history.
- Đối chiếu mở rộng: thêm `MemberDiscountPolicy` và lựa chọn trong `CinemaConsole`; `Booking`, `BookingService`, `Seat`, `Showtime`, `Main`, interface và các policy/tier cũ không đổi.
- Commit: [4d1dc10](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/4d1dc10), [087ce92](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/087ce92).
- Minh chứng: [test/demo](../evidence/week-4-thang-test-output.txt), [diff](../evidence/week-4-thang-extensibility-diff.txt), [giải thích thiết kế và chuẩn bị bảo vệ](thang-defense.md).

## 2. Rà soát và tích hợp

- Ngày: 18/10/2026. Công cụ: Codex.
- Prompt tóm tắt: tích hợp Member với Luxury, xử lý các mục còn thiếu sau rà soát và cập nhật hồ sơ tuần 4; chưa làm final report hoặc merge main.
- Phần Thắng phụ trách: rà soát và tích hợp trên nhánh `week4-trieu-final-delivery`; giữ nguyên các commit gốc của Triệu.
- AI hỗ trợ: sửa test theo locale, bổ sung test kết hợp, chạy kiểm chứng và cập nhật UML, báo cáo tuần, bản nháp reflection.
- Điểm cần sửa: test Luxury so sánh cứng chuỗi `275,000`, gây 1 failure trên 2 test khi chạy với `vi_VN`. Chuỗi kỳ vọng được đổi sang định dạng theo cùng locale với confirmation; công thức tính tiền không đổi.
- Commit: [9e76e9a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9e76e9a), [26479fb](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/26479fb), [962cf35](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/962cf35).
- Kiểm chứng: 95/95 test pass ở cấu hình mặc định và Việt Nam; A1 Standard + A6 Luxury có subtotal 275.000 VND, Member giảm 41.250 VND, total 233.750 VND. Console/history, đặt trùng, cancel và expiration được kiểm tra; Main và Week1Scenarios vẫn chạy đúng.
- Minh chứng: [regression trước/sau](../evidence/week-4-locale-regression.txt), [test/demo tích hợp](../evidence/week-4-integration-test-output.txt), [diff tích hợp](../evidence/week-4-integration-extensibility-diff.txt), [UML hiện tại](final-class-diagram.md).

## 3. Minh chứng cá nhân và giới hạn

- Ảnh viết tay do Thắng thực hiện và cung cấp ngày 16/10/2026, giữ nguyên bản: [tính tiền và trace](../evidence/week-4-handwritten/thang-member-discount-calculation-and-trace.jpg), [mở rộng và làm tròn](../evidence/week-4-handwritten/thang-discount-extensibility-and-rounding.jpg). Ghi chú sửa nhãn Student/Member trên trang 1 nằm trong [hồ sơ cá nhân](thang-defense.md).
- Member hiện là lựa chọn mô phỏng, chưa xác minh tư cách thành viên và không cộng dồn discount. Kiểm thử biên lớn áp dụng cho phép tính Member, không phải bảo đảm chống tràn cho mọi phép tính trong hệ thống.
- Reflection là bản nháp nhận xét kỹ thuật; phần trải nghiệm học tập cần Thắng bổ sung. Câu hỏi bảo vệ dùng để chuẩn bị, chưa phải kết quả vấn đáp. Final report chưa tạo.
