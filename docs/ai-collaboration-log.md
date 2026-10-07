# AI Collaboration Log - Week 1 Resubmission

## Mục đích

Tài liệu này ghi lại quá trình nhóm sử dụng AI như một trợ lý thảo luận, người review và công cụ hỗ trợ tạo bản nháp. Nhóm chịu trách nhiệm cuối cùng về yêu cầu, lựa chọn thiết kế, code, kiểm thử và nội dung báo cáo.

Các prompt và kết quả bên dưới được ghi theo những lần trao đổi thực tế trong phạm vi tuần 1. Nội dung triển khai group discount, validation mở rộng và `Week2Tests` thuộc tuần 2, không được dùng để mô tả việc đã hoàn thành trong tuần 1. Không xem nội dung AI là bằng chứng duy nhất cho việc code đúng; bằng chứng chính là quyết định của nhóm, source code, output tuần 1 và lịch sử commit trên GitHub.

## 1. Tóm tắt vai trò của nhóm và AI

| Bên | Trách nhiệm |
|---|---|
| Nhóm sinh viên | Đọc đề, xác định phạm vi, chọn Java, chọn thiết kế cuối cùng, sửa code, chạy test, viết báo cáo và chuẩn bị bảo vệ. |
| AI | Gợi ý kế hoạch, đặt cấu trúc ban đầu, đề xuất alternative design, hỗ trợ viết bản nháp code/tài liệu và chỉ ra trường hợp kiểm thử cần có. |
| Công cụ kiểm chứng | `javac`, chương trình demo, `Week2Tests`, kiểm tra output và review thủ công của nhóm. |

## 2. Nhật ký trao đổi tuần 1

### Interaction W1-01 - Lập kế hoạch

**Prompt của nhóm**

> Đây là nội dung đề tài Movie Ticket Booking System. Hãy lập cho mình một liệu trình 4 tuần để triển khai đề tài, bao gồm kế hoạch báo cáo hằng tuần và nội dung cần hoàn thành cho tuần đầu tiên.

**AI đã tạo/gợi ý**

- Kế hoạch 4 tuần gồm phân tích, domain model, seat locking, test và hoàn thiện báo cáo.
- Danh sách class `Movie`, `Screen`, `Showtime`, `Seat`, `Booking`.
- Interface `PricingTier` và `DiscountPolicy`.
- Các điểm cần giải thích trong Self-Explanation.

**Nhóm đã quyết định và chỉnh sửa**

- Chọn Java vì phù hợp với các bài OOP hiện có trong repo.
- Giới hạn phiên bản đầu là console application, chưa làm database, đăng nhập, thanh toán thật hoặc GUI.
- Chọn `Showtime` là nơi sở hữu trạng thái ghế theo từng suất chiếu.
- Chọn `DiscountPolicy` làm abstraction; triển khai discount đầu tiên ở mức tối thiểu cho demo tuần 1, còn group discount để kế hoạch tuần 2.

**Cách kiểm chứng**

Nhóm đối chiếu kế hoạch với từng yêu cầu trong đề bài và chuyển các quyết định thành `class-diagram.md`, `design-decisions.md` và source skeleton.

### Interaction W1-02 - Chuyển kế hoạch thành việc cụ thể

**Prompt của nhóm**

> Oke vậy giờ mình cần làm gì

**AI đã tạo/gợi ý**

- Checklist tuần 1.
- Cấu trúc thư mục `final_project`.
- Nội dung cần có trong báo cáo tuần 1.
- Các test tối thiểu: happy path, double-booking và polymorphism.

**Nhóm đã thực hiện/sửa**

- Tạo cấu trúc `README.md`, `docs`, `src` và `evidence`.
- Chuyển class diagram từ ý tưởng thành file tài liệu.
- Chọn các tên method cụ thể như `tryLockSeats`, `confirmBooking` và `releaseSeats`.

**Cách kiểm chứng**

Nhóm kiểm tra cấu trúc file và chạy skeleton bằng `javac`/`java`.

### Interaction W1-03 - Tạo bản nháp triển khai

**Prompt của nhóm**

> Oke làm giúp mình các phần việc trên

**AI đã tạo/gợi ý**

- Bản nháp Java cho domain model.
- Demo happy path.
- Demo từ chối double-booking.
- README, class diagram, design decisions và báo cáo tuần 1.

**Nhóm đã sửa/hoàn thiện**

- Kiểm tra lại tên lớp, method và package `cinema`.
- Chạy chương trình và lưu output vào `evidence/week-1-console-output.txt`.
- Kiểm tra giá Standard/Premium và student discount.
- Đối chiếu `Showtime` với yêu cầu encapsulation.
- Ghi rõ các giới hạn chưa làm thay vì tuyên bố hệ thống hoàn chỉnh.

**Cách kiểm chứng**

Output thực tế của chương trình có confirmation summary và thông báo `Rejected correctly: Seat A1 is not available.`

## 3. Phân biệt nội dung AI và nội dung nhóm

| Hạng mục | AI hỗ trợ | Nhóm chịu trách nhiệm/sửa |
|---|---|---|
| Kế hoạch | Đề xuất thứ tự làm trong 4 tuần | Chọn phạm vi phù hợp thời gian và môn học |
| Class model | Gợi ý class/interface | Chốt trách nhiệm của từng class và quan hệ composition |
| Seat locking | Gợi ý trạng thái `AVAILABLE`, `LOCKED`, `BOOKED` | Chốt `Showtime` quản lý state và kiểm tra double-booking |
| Pricing | Gợi ý strategy/interface | Chọn multiplier, kiểm tra subtotal và confirmation output |
| Báo cáo | Gợi ý outline | Chọn nội dung thực tế đã làm, thêm giới hạn và minh chứng |
| Source code | Tạo bản nháp ban đầu | Review từng class, compile, chạy demo, sửa lỗi và kiểm thử |

## 4. Những điểm AI không được tự quyết định

- Không tự mở rộng phạm vi sang database, GUI hoặc thanh toán thật.
- Không tự quyết định tiêu chí chấm hoặc tuyên bố code đã đúng nếu nhóm chưa chạy test.
- Không thay thế việc nhóm hiểu các method dùng trong oral defense.
- Không dùng output AI làm test evidence nếu chưa chạy lại trên source code hiện tại.

## 5. Quy trình cộng tác áp dụng sau khi nhận feedback

Từ tuần tiếp theo, mỗi thay đổi sẽ đi theo quy trình:

1. Nhóm viết vấn đề và ràng buộc trước.
2. Nhóm hỏi AI về các phương án và trade-off, không chỉ yêu cầu sinh toàn bộ code.
3. Nhóm chọn một phương án và ghi lý do.
4. AI hỗ trợ bản nháp hoặc review.
5. Nhóm tự sửa, chạy test và kiểm tra output.
6. Commit ghi rõ thay đổi, lý do, kiểm chứng và phần AI hỗ trợ.

## 6. Phạm vi commit cho báo cáo thay thế tuần 1

Các commit dùng để thay thế báo cáo tuần 1 phải phân biệt rõ phần của Nguyễn Đức Thắng và Nguyễn Thành Triệu. Commit tuần 2 không được gộp vào phần minh chứng tuần 1. Nếu hai người cùng review một thay đổi, commit ghi owner chính và thêm co-author thực tế.

## 7. Thông tin GitHub

- Repository public: `[Dán URL GitHub thật vào đây sau khi tạo repository]`
- Commit chứa hồ sơ tuần 1: `[Dán commit URL thật vào đây]`
- Commit chứa hồ sơ tuần 2: `[Dán commit URL thật vào đây]`

Không điền URL giả. Sau khi push, nhóm cập nhật ba dòng trên bằng link thật.
