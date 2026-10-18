# AI Usage Log - tuần 4 - Nguyễn Thành Triệu

- Sinh viên: Nguyễn Thành Triệu - MSSV 23110066.
- Ngày trao đổi/kiểm chứng: 15/10/2026 (UTC+7).
- Công cụ/model: Codex, GPT-5 theo thông tin của phiên làm việc.
- Nhánh: `week4-trieu-final-delivery`.

## Prompt tóm tắt

Đối chiếu prompt tuần 4 với rubric, lấy `origin/main` mới nhất, triển khai
phần Triệu gồm `LuxuryTier`, test kết hợp, wiring A6 và kiểm chứng. Người
thực hiện yêu cầu không commit/push và không tạo final report trong phiên
này. Phần `MemberDiscountPolicy` và lựa chọn Member thuộc Thắng, chỉ kiểm
thử kết hợp sau khi phần đó được merge.

## Nội dung AI hỗ trợ

Codex đọc source/test hiện có, kiểm tra lịch sử và đối chiếu rubric; thêm
`LuxuryTier`, `LuxuryTierTest`, `LuxuryBookingFlowTest`, wiring A6 trong
`Main`, README, báo cáo tuần và bộ câu hỏi chuẩn bị bảo vệ. Gợi ý kỹ thuật
quan trọng là tính 175% bằng phép chia phần nguyên/phần dư, kiểm tra
overflow trước phép nhân và dùng `Math.addExact`, thay vì để phép nhân
`basePrice * 175` overflow.

AI không tạo lại `MemberDiscountPolicy`, không sửa lựa chọn discount trong
`CinemaConsole` và không dựng test giả cho Member khi dependency chưa có
trên `origin/main`.

## Kiểm chứng và điều chỉnh

Các file có thể đối chiếu trực tiếp:

- [LuxuryTier.java](../src/main/java/cinema/LuxuryTier.java)
- [LuxuryTierTest.java](../src/test/java/cinema/LuxuryTierTest.java)
- [LuxuryBookingFlowTest.java](../src/test/java/cinema/LuxuryBookingFlowTest.java)
- [Main.java](../src/main/java/cinema/Main.java)

JDK 21 biên dịch toàn bộ source/test với `--release 17`; JUnit Platform
chạy 73 test, trong đó 73 thành công và 0 thất bại. `Main` exit code 0,
`Week1Scenarios` đạt 3/3, và interactive console hiển thị A6 Luxury cùng
subtotal A1+A6 là 275.000 VND. Lệnh `mvn clean test` được thử nhưng môi
trường phiên này không có Maven trong PATH, nên log không ghi Maven là đã
thành công.

Trong quá trình rà soát, phần triển khai được giữ độc lập với dependency
của Thắng: flow test dùng `NoDiscountPolicy`, không ghi nhận kết quả Member
15% khi code tương ứng chưa có trên base branch. Đây là điều chỉnh phạm vi
để tránh tạo bằng chứng không đúng thực trạng.

## Giới hạn của kết quả AI

Kết quả AI không thể tự xác nhận phần Thắng đã merge chỉ từ prompt; việc
đối chiếu `origin/main` cho thấy `MemberDiscountPolicy` chưa tồn tại ở base
branch. Ngoài ra, thiếu Maven trong PATH khiến kiểm chứng phải dùng JUnit
Platform trực tiếp; đây là giới hạn môi trường, không được diễn giải thành
Maven build thành công.

Ở phiên triển khai ban đầu chưa có commit mới. Phần độc lập sau đó được
đăng lên repo bằng [a00434d](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/a00434d),
[469f605](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/469f605),
và ảnh viết tay ở [c61bce8](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/c61bce8).

## Cập nhật rà soát và tích hợp — 18/10/2026

- Công cụ: Codex. Phần rà soát/tích hợp do Thắng phụ trách trên nhánh cũ
  `week4-trieu-final-delivery`; giữ nguyên các commit gốc của Triệu.
- Yêu cầu: sửa test theo locale, tích hợp phần Member có sẵn của Thắng,
  chạy luồng kết hợp, cập nhật UML, hồ sơ và reflection; chưa tạo final report.
- Hỗ trợ thực hiện: sửa assertion, thêm bảy test tích hợp và hai dữ liệu biên
  Luxury, chạy test/demo, soạn UML/báo cáo tuần và bản nháp reflection.
- Điểm yếu được tìm thấy: assertion hard-code `275,000` chỉ đúng ở một số
  locale. Chạy `LuxuryBookingFlowTest` với `vi_VN` gây 1 failure trên 2 test;
  dùng `String.format` cho expected đã sửa lỗi mà không đổi code nghiệp vụ.
- Commit: [9e76e9a](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/9e76e9a)
  sửa assertion; [26479fb](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/26479fb)
  tích hợp Member; [962cf35](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/962cf35)
  thêm test kết hợp.
- Kiểm chứng: 95 test pass ở cấu hình mặc định và Việt Nam; Main và 3/3
  Week1Scenarios pass; console A1/A6/Member có subtotal 275.000, giảm 41.250,
  total 233.750 VND; history đúng nội dung và đặt lại A6 bị từ chối.
- Minh chứng: [trước/sau sửa](../evidence/week-4-locale-regression.txt),
  [test/demo](../evidence/week-4-integration-test-output.txt),
  [diff](../evidence/week-4-integration-extensibility-diff.txt),
  [UML](final-class-diagram.md).
- Reflection được soạn thành bản nháp kỹ thuật trong [hồ sơ cá nhân](trieu-defense.md).
  Chưa có xác nhận trải nghiệm học tập hoặc kết quả vấn đáp của Triệu;
  không dùng bản nháp để tuyên bố đã bảo vệ độc lập. Final report vẫn để lại.
