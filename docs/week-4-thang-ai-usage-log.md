# AI Usage Log — Tuần 4, Thắng

Nguyễn Đức Thắng — MSSV 23110062.

- Ngày: 14/10/2026 (UTC+7).
- Công cụ/model: Codex, GPT-6.1 Sol, mức suy luận Max; Thắng xác nhận tên model trong phiên triển khai.
- Nội dung yêu cầu: bổ sung Member discount 15%, tích hợp vào console và kiểm thử khả năng mở rộng mà không sửa các class nghiệp vụ cũ. Cập nhật mã nguồn và minh chứng trên nhánh `week4-thang-member-discount`, chưa merge vào main.
- Phạm vi Thắng phụ trách: biến thể discount, lựa chọn Member ở console, các test liên quan và tài liệu bảo vệ cá nhân. Không thay phần của Triệu hoặc các class nghiệp vụ cũ.
- Nội dung được hỗ trợ: xây dựng policy và các test liên quan, tích hợp console, chạy kiểm thử và demo, đối chiếu diff; soạn Self-Explanation, câu hỏi bảo vệ, bản nháp reflection và hỗ trợ thao tác commit/push.
- Kiểm tra và điều chỉnh thiết kế: tách thương/dư để tránh tràn ở `subtotal * 15 / 100`; bổ sung case 107 VND để tránh bỏ phần dư quá sớm, biên `Long.MAX_VALUE`, subtotal âm và đối chiếu `BigInteger`. Kiểm tra phép nhân trực tiếp trên JVM cho kết quả sai do overflow, không tạo test thất bại bằng cách sửa ngược source cũ.
- Kết quả: baseline 66 test; sau policy 77 test; sau console 81 test, đều pass. Main lưu/đọc confirmation Student như trước; Week1Scenarios 3/3 pass; console Member A1/A2 có total 191.250 VND và file history cùng giá.
- Đối chiếu mở rộng: production diff chỉ thêm `MemberDiscountPolicy` và nối lựa chọn ở `CinemaConsole`; `Booking`, `BookingService`, `Seat`, `Showtime`, `Main`, các interface và policy/tier cũ không đổi.
- Commit source/tests: [4d1dc10](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/4d1dc10), [087ce92](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/087ce92).
- Minh chứng: [test/demo](../evidence/week-4-thang-test-output.txt), [diff](../evidence/week-4-thang-extensibility-diff.txt), [chuẩn bị bảo vệ](thang-defense.md).
- Minh chứng viết tay bổ sung ngày 16/10/2026: [trang 1 — tính tiền và trace](../evidence/week-4-handwritten/thang-member-discount-calculation-and-trace.jpg), [trang 2 — mở rộng và làm tròn](../evidence/week-4-handwritten/thang-discount-extensibility-and-rounding.jpg). Hai ảnh do Thắng cung cấp, giữ nguyên bản; ghi chú phân biệt nhãn Student/Member trên trang 1 nằm trong tài liệu chuẩn bị bảo vệ.
- Giới hạn: Member là lựa chọn mô phỏng, không xác minh tư cách thành viên, không cộng dồn discount. Kiểm thử biên lớn chỉ bảo đảm phép tính Member. Chưa có kết quả vấn đáp trực tiếp trong lần cập nhật này.

## Cập nhật tích hợp — 18/10/2026

- Công cụ: Codex. Yêu cầu: hoàn thiện các mục còn thiếu sau rà soát, trừ final report; dùng nhánh delivery hiện có, chưa merge main.
- Phần Thắng phụ trách: rà soát test theo locale, tích hợp hai nhánh, kiểm thử Luxury + Member và cập nhật hồ sơ chung. Codex hỗ trợ chỉnh tests, chạy kiểm chứng, soạn UML/báo cáo tuần và bản nháp reflection cho hai người.
- Lỗi/điều chỉnh: test Luxury giả định dấu phẩy hàng nghìn nên fail với `vi_VN`. Expected được định dạng theo cùng locale với confirmation. Các test kết hợp dùng implementation Member/Luxury thật, không dùng policy giả.
- Commit: [9e76e9a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9e76e9a), [26479fb](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/26479fb), [962cf35](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/962cf35).
- Kiểm chứng: 95/95 test pass ở cấu hình mặc định và Việt Nam; A1 Standard + A6 Luxury có subtotal 275.000 VND, Member giảm 41.250 VND, total 233.750 VND. Console/history, đặt trùng, cancel và expiration được kiểm tra; Main và Week1Scenarios vẫn chạy đúng.
- Minh chứng: [regression trước/sau](../evidence/week-4-locale-regression.txt), [test/demo tích hợp](../evidence/week-4-integration-test-output.txt), [diff tích hợp](../evidence/week-4-integration-extensibility-diff.txt), [UML hiện tại](final-class-diagram.md).
- Reflection hiện là bản nháp nhận xét kỹ thuật dựa trên source và quá trình kiểm chứng; chưa xác nhận thay trải nghiệm học tập hay kết quả vấn đáp cá nhân. Hai ảnh viết tay gốc được giữ nguyên. Final report chưa tạo theo yêu cầu.
