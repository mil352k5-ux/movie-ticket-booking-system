# AI Usage Log - Tuần 4 - Triệu

Nguyễn Thành Triệu - MSSV 23110066.

## 1. Luxury tier

- Ngày: 15/10/2026 (UTC+7).
- Công cụ/model: Codex, GPT-5 theo log phiên triển khai.
- Prompt tóm tắt: thêm Luxury tier bằng 175% giá cơ bản, bổ sung ghế A6 và test chứng minh dùng lại được luồng booking; đối chiếu với rubric tuần 4.
- Phần Triệu phụ trách: Luxury tier, dữ liệu A6, test liên quan và hồ sơ cá nhân trên nhánh `week4-trieu-final-delivery`. Phần Member discount thuộc Thắng.
- AI hỗ trợ: triển khai `LuxuryTier`, `LuxuryTierTest`, `LuxuryBookingFlowTest` và dữ liệu A6 trong `Main`; cập nhật README, báo cáo tuần, câu hỏi bảo vệ và chạy kiểm chứng.
- Điều chỉnh kỹ thuật: tính 175% bằng thương/dư, kiểm tra giới hạn trước phép nhân và dùng `Math.addExact`. Cách này tránh overflow trung gian và từ chối giá cuối vượt miền `long`.
- Phạm vi kiểm thử ban đầu: base branch chưa có Member nên flow dùng `NoDiscountPolicy`; kết quả Luxury + Member được kiểm chứng riêng sau khi tích hợp.
- Kiểm chứng: JDK 21 biên dịch với `--release 17`, JUnit Platform chạy 73/73 test pass. Main exit code 0, Week1Scenarios đạt 3/3; console có A6 Luxury, subtotal A1/A6 là 275.000 VND. Maven chưa có trong PATH ở lần chạy này, nên đây là kết quả JUnit Platform, không phải Maven build.
- Commit: [a00434d](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a00434d) cho source/tests, [469f605](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/469f605) cho tài liệu và minh chứng. Phiên hỗ trợ triển khai ban đầu không thực hiện commit/push.
- Minh chứng: [test/demo độc lập](../evidence/week-4-final-test-output.txt), [diff mở rộng](../evidence/week-4-final-extensibility-diff.txt), [giải thích thiết kế và chuẩn bị bảo vệ](trieu-defense.md).

## 2. Rà soát và tích hợp

- Ngày: 18/10/2026. Công cụ: Codex.
- Prompt tóm tắt: tích hợp Member với Luxury, sửa test theo locale và cập nhật UML/hồ sơ tuần 4; chưa làm final report hoặc merge main.
- Phạm vi: Thắng phụ trách đợt rà soát/tích hợp trên nhánh `week4-trieu-final-delivery`; các commit gốc của Triệu được giữ nguyên.
- AI hỗ trợ: sửa assertion, bổ sung bảy test tích hợp và hai dữ liệu biên Luxury, chạy kiểm chứng; soạn UML, báo cáo tuần và bản nháp reflection.
- Điểm cần sửa: test confirmation so sánh cứng chuỗi `275,000`. Khi chạy với `vi_VN`, 1 trong 2 test thất bại. Chuỗi kỳ vọng được đổi sang `String.format` theo cùng locale với confirmation; không đổi công thức nghiệp vụ.
- Kiểm chứng: 95/95 test pass ở cấu hình mặc định và Việt Nam; Main và 3/3 Week1Scenarios pass. A1 Standard + A6 Luxury có subtotal 275.000 VND; Member giảm 41.250 VND, total 233.750 VND. History khớp nội dung xác nhận; đặt lại A6 bị từ chối. Luồng cancel và expiration cũng có test kết hợp.
- Commit: [9e76e9a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9e76e9a) sửa assertion, [26479fb](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/26479fb) tích hợp Member, [962cf35](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/962cf35) thêm test kết hợp.
- Minh chứng: [trước/sau sửa](../evidence/week-4-locale-regression.txt), [test/demo tích hợp](../evidence/week-4-integration-test-output.txt), [diff tích hợp](../evidence/week-4-integration-extensibility-diff.txt), [UML](final-class-diagram.md).

## 3. Minh chứng cá nhân và giới hạn

- Hai ảnh viết tay do Triệu cung cấp, giữ nguyên bản: [tính giá và booking](../evidence/week-4-handwritten/trieu-luxury-tier-pricing-trace.png), [trace và mở rộng](../evidence/week-4-handwritten/trieu-luxury-booking-polymorphism-trace.png); bổ sung ở commit [c61bce8](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c61bce8).
- Luxury không làm thay đổi cơ chế khóa ghế: hệ thống vẫn bảo vệ trong một JVM, chưa có distributed locking hoặc khôi phục inventory từ history.
- Reflection trong [hồ sơ cá nhân](trieu-defense.md) là bản nháp nhận xét kỹ thuật; phần trải nghiệm học tập cần Triệu bổ sung. Câu hỏi bảo vệ dùng để chuẩn bị, chưa phải kết quả vấn đáp. Final report chưa tạo.
