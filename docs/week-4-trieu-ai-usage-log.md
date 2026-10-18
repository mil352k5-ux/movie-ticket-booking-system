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

Không có commit mới trong phiên này và không có commit hash để khai báo.
Final report/reflection và phần xác nhận trải nghiệm cá nhân được để người
thực hiện hoàn thiện sau khi toàn bộ phần nhóm đã hợp nhất.
