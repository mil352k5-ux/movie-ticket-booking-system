# Báo cáo tổng kết cuối kỳ

## Movie Ticket Booking System

- Môn học: OOP - HK1 2026-2027 Đợt 1
- GVHD: Huỳnh Xuân Phụng
- Thành viên:
  - Nguyễn Đức Thắng - MSSV 23110062
  - Nguyễn Thành Triệu - MSSV 23110066
- Repository: [movie-ticket-booking-system](https://github.com/mil352k5-ux/movie-ticket-booking-system)
- Phiên bản source đối chiếu: `origin/main` tại `16420c6`
- Merge tuần 4: [`e828683`](https://github.com/mil352k5-ux/movie-ticket-booking-system/commit/e828683)

> Báo cáo này tổng hợp source và evidence đã có trên `main`. Phần
> reflection cá nhân ở cuối báo cáo cần được mỗi sinh viên viết lại bằng
> trải nghiệm thật của mình trước khi nộp; báo cáo không thay người học
> tuyên bố một trải nghiệm chưa được xác nhận.

## 1. Tóm tắt bài toán

Rạp chiếu phim bán ghế theo từng suất chiếu trên nhiều màn hình. Hệ thống
cần cho phép người dùng xem phim và suất chiếu, chọn ghế, tính giá theo tier,
áp dụng một discount policy, giữ ghế trong lúc booking và hoàn tất booking
bằng một confirmation summary.

Mục tiêu của đồ án không phải xây dựng một hệ thống thương mại đầy đủ mà là
thể hiện thiết kế OOP có trách nhiệm rõ ràng, có khả năng mở rộng và có test
chạy được cho các luồng đúng, luồng lỗi, pricing polymorphism và cạnh tranh
giữ ghế.

## 2. Phạm vi thực tế

### Đã triển khai

- Domain model gồm `Movie`, `Screen`, `Showtime`, `Seat` và `Booking`.
- Pricing tier `StandardTier`, `PremiumTier`, `ReclinerTier` và
  `LuxuryTier`.
- Discount policy `NoDiscountPolicy`, `StudentDiscountPolicy`,
  `GroupDiscountPolicy` và `MemberDiscountPolicy`.
- Seat locking, double-booking validation, lock expiration và booking
  lifecycle.
- `BookingService` điều phối create/confirm/cancel.
- Console interactive và `BookingHistory` lưu confirmation UTF-8.
- JUnit 5 tests, executable Week 1 scenarios, test evidence, UML và AI
  Usage Logs.

### Không thuộc phạm vi

- Database thật hoặc database transaction.
- Đăng nhập, tài khoản người dùng hoặc xác minh thẻ thành viên.
- GUI bằng Swing/JavaFX hoặc web interface.
- REST API, thanh toán thật và hệ thống phân tán.
- Distributed locking giữa nhiều JVM/application server.

`BookingHistory` chỉ lưu confirmation vào file; nó không khôi phục trạng thái
ghế/lock sau khi chương trình khởi động lại. `MemberDiscountPolicy` là một
policy giảm giá mô phỏng, không phải authentication system.

## 3. Đối chiếu yêu cầu chức năng

| Yêu cầu | Thiết kế và nơi thực hiện | Minh chứng |
| --- | --- | --- |
| Model movie, screen, showtime, seat, booking | `Movie`, `Screen`, `Showtime`, `Seat`, `Booking` | Source trong `src/main/java/cinema` |
| Pricing tier không lặp logic booking | `PricingTier`; `Seat.price(long)` ủy quyền cho implementation | `PricingTierTest`, `LuxuryTierTest` |
| Chống double-booking | `Showtime.tryLockSeats()` kiểm tra state và cập nhật lock trong critical section | `BookingConcurrencyTest`, `LuxuryMemberIntegrationTest` |
| Discount policy | `DiscountPolicy`; Student, Group và Member là các implementation | `GroupDiscountPolicyTest`, `MemberDiscountPolicyTest` |
| Confirmation summary | `Booking.generateConfirmation()` sau khi booking CONFIRMED | `BookingHistoryTest`, `LuxuryMemberIntegrationTest` |
| Luồng happy path | Service và console tạo, xác nhận và lưu booking | `evidence/week-4-integration-test-output.txt` |
| Luồng lỗi | Validation, double-booking, cancel/confirm sai trạng thái và expiration | `DomainValidationTest`, `BookingServiceTest` |
| Polymorphism | Cùng `Seat.price()`/`Booking.calculateDiscount()` nhưng thay implementation | `PricingTierTest`, `LuxuryTierTest`, `MemberDiscountBookingTest` |

## 4. Kiến trúc và quan hệ giữa các lớp

UML đầy đủ nằm tại [`docs/final-class-diagram.md`](final-class-diagram.md).
Quan hệ chính có thể tóm tắt như sau:

```text
Movie -----------┐
                  v
              Showtime ---- has-a ---- Screen ---- holds ---- Seat
                  |                       |
                  |                       +---- Seat --> PricingTier
                  +---- owns state/locks

Booking ---- has-a ---- Showtime
Booking ---- references selected Seat objects
Booking ---- delegates discount to DiscountPolicy

BookingService ---- creates/transitions Booking
BookingService ---- locks/confirms/releases through Showtime
BookingHistory ---- stores Booking confirmation as UTF-8 files
CinemaConsole ---- uses Showtime, BookingService and BookingHistory
```

`Screen` giữ layout các `Seat`. `Showtime` giữ `Screen` và state/lock riêng
cho từng suất chiếu. `Booking` giữ danh sách các seat được chọn nhưng không
sở hữu vòng đời physical seat. Vì vậy ghế vật lý có thể được tham chiếu bởi
các flow khác nhau, còn trạng thái AVAILABLE/LOCKED/BOOKED thuộc từng
`Showtime`.

## 5. Trách nhiệm của các lớp

| Lớp | Trách nhiệm chính |
| --- | --- |
| `Movie` | Dữ liệu phim và validation ID/title/duration |
| `Screen` | Thông tin màn hình và collection ghế, chống seat ID trùng |
| `Seat` | ID ghế, pricing tier và ủy quyền tính giá |
| `Showtime` | Phim, màn hình, thời gian, seat state, lock và expiration |
| `Booking` | Selected seats, subtotal, discount, total, confirmation và status |
| `BookingService` | Điều phối create/confirm/cancel, không lặp logic tính tiền |
| `PricingTier` | Abstraction cho công thức giá ghế |
| `DiscountPolicy` | Abstraction cho công thức giảm giá |
| `BookingHistory` | Lưu/đọc confirmation bằng file UTF-8 |
| `CinemaConsole` | Nhập/xuất console và chuyển lựa chọn người dùng thành service call |
| `Main` | Wiring dữ liệu demo và catalog interactive, không chứa business logic |

## 6. Giải thích các nguyên lý OOP

### 6.1 Polymorphism qua pricing tier

`PricingTier` định nghĩa:

```java
long calculatePrice(long basePrice);
String name();
```

`Seat.price(long)` chỉ gọi `pricingTier.calculatePrice(basePrice)`. Nó không
cần biết seat là Standard, Premium, Recliner hay Luxury. Vì vậy thêm
`LuxuryTier` không cần sửa `Seat`, `Booking` hoặc `BookingService`.

Với base price 100.000 VND:

```text
Standard = 100.000 VND
Premium  = 125.000 VND
Recliner = 150.000 VND
Luxury   = 175.000 VND
```

`LuxuryTier` tính 175% bằng `long`, tách thương/phần dư để tránh overflow
trung gian, làm tròn xuống đến 1 VND và dùng `Math.addExact()` để báo lỗi
nếu kết quả cuối vượt miền biểu diễn.

### 6.2 Polymorphism qua discount policy

`Booking` giữ một field kiểu `DiscountPolicy`. Trong
`Booking.calculateDiscount()`, lời gọi:

```java
discountPolicy.calculateDiscount(calculateSubtotal(), selectedSeats.size());
```

được dispatch đến policy đang được truyền vào. `Booking` không kiểm tra tên
subtype và không có nhánh riêng cho Student, Group hoặc Member.

Ví dụ A1 Standard + A6 Luxury:

```text
Subtotal                 = 100.000 + 175.000 = 275.000 VND
Member discount 15%      = 275.000 * 15%    =  41.250 VND
Total                    = 275.000 - 41.250 = 233.750 VND
```

### 6.3 Encapsulation

`Showtime` giữ các map `seatStates`, `seatLocks` và tập
`usedBookingIds` ở dạng private. Caller không được tự ý sửa state ghế. Các
method `tryLockSeats`, `confirmBooking`, `releaseSeats` và
`getSeatStates` là nơi duy nhất xử lý invariant seat state.

`Booking` giữ `status` private. Chuyển trạng thái phải đi qua các method
package-private `markConfirmed`, `markCancelled` hoặc `markExpired`, và các
method này chỉ chấp nhận trạng thái hiện tại là `PENDING`.

### 6.4 Composition và has-a

- `Screen` giữ collection các `Seat` tạo nên layout màn hình.
- `Showtime` có một `Movie`, một `Screen`, state ghế và các lock của riêng
  suất chiếu.
- `Booking` giữ các seat đã chọn và một `Showtime`.
- `BookingService` sử dụng `Booking` và `Showtime` để điều phối flow.

Thiết kế này phân biệt physical seat với trạng thái booking theo showtime.
Hủy một booking không hủy object `Seat`; chỉ release state LOCKED của booking
đó về AVAILABLE.

### 6.5 Single Responsibility và Open/Closed

`CinemaConsole` chỉ lo nhập/xuất. `BookingService` chỉ điều phối workflow.
`Booking` tính tiền và giữ lifecycle của chính nó. `Showtime` bảo vệ invariant
ghế. `BookingHistory` lo persistence dạng file. Các policy/tier chỉ lo công
thức của mình.

`LuxuryTier` và `MemberDiscountPolicy` là hai subtype mới mở rộng qua
interface hiện có. Business classes cũ không cần biết tên subtype mới.

## 7. Booking lifecycle và seat locking

Trạng thái booking:

```text
                 +--------------+
                 |   PENDING    |
                 +--------------+
                  /      |      \
                 v       v       v
          CONFIRMED  CANCELLED  EXPIRED
```

Chuyển trạng thái hợp lệ:

- `PENDING -> CONFIRMED`
- `PENDING -> CANCELLED`
- `PENDING -> EXPIRED`

Không cho phép confirm hai lần, cancel booking đã CONFIRMED hoặc confirm
booking đã CANCELLED/EXPIRED.

`BookingService.createBooking()` tạo `Booking` rồi gọi
`Showtime.tryLockSeats()`. `tryLockSeats()` kiểm tra toàn bộ seat ID trước
khi cập nhật state, nên thao tác nhiều ghế là all-or-nothing. Nếu A1 đã bị
booking B1 khóa, booking B2 yêu cầu A1 và A2 sẽ bị từ chối và A2 vẫn
AVAILABLE.

Các method thay đổi seat state của `Showtime` là `synchronized`. Trong một
JVM, hai task dùng chung một `Showtime` không thể cùng lúc vượt qua cặp
check-then-lock cho cùng ghế. `BookingConcurrencyTest` kiểm tra chính xác
một booking thành công khi hai user cùng tranh ghế cuối; test không phụ
thuộc thread nào thắng.

Giới hạn production: `synchronized` chỉ bảo vệ object trong một JVM. Nhiều
application server cần database transaction, row-level lock, unique
constraint trên `(showtime_id, seat_id)` hoặc optimistic locking với version.
Đồ án chưa triển khai distributed lock hay database transaction.

## 8. Validation và xử lý lỗi

Validation nằm tại class sở hữu dữ liệu hoặc thao tác tương ứng:

- `Movie`: ID/title không blank, duration > 0.
- `Screen`: ID/name không blank, không nhận seat null và không nhận seat ID
  trùng.
- `Seat`: ID không blank, pricing tier không null.
- `Showtime`: movie/screen/start time không null, lock duration hợp lệ,
  seat ID phải tồn tại, nhiều ghế phải all-or-nothing.
- `Booking`: booking ID, showtime, policy và seat IDs hợp lệ; seat không tồn
  tại bị từ chối.

`IllegalArgumentException` dành cho input không hợp lệ. `IllegalStateException`
dành cho thao tác không hợp lệ theo state hiện tại như double-booking hoặc
confirm/cancel sai trạng thái.

## 9. Kiểm thử và minh chứng

### 9.1 Bộ test tích hợp

Evidence tích hợp tại
[`evidence/week-4-integration-test-output.txt`](../evidence/week-4-integration-test-output.txt)
ghi nhận 95 test pass ở cấu hình mặc định và locale Việt Nam:

| Test class | Số test |
| --- | ---: |
| `BookingConcurrencyTest` | 1 |
| `BookingHistoryTest` | 8 |
| `BookingPricingTest` | 2 |
| `BookingServiceConcurrencyTest` | 2 |
| `BookingServiceTest` | 14 |
| `CinemaConsoleTest` | 7 |
| `DomainValidationTest` | 15 |
| `GroupDiscountPolicyTest` | 5 |
| `LuxuryBookingFlowTest` | 2 |
| `LuxuryMemberIntegrationTest` | 7 |
| `LuxuryTierTest` | 5 |
| `MemberDiscountBookingTest` | 4 |
| `MemberDiscountPolicyTest` | 7 |
| `PricingTierTest` | 2 |
| `SeatLockTest` | 2 |
| `ShowtimeLockExpirationTest` | 12 |
| **Tổng** | **95** |

Kết quả:

```text
Tests run: 95, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 9.2 Lệnh build/run

Từ thư mục repository:

```powershell
mvn clean test
mvn "-DargLine=-Duser.language=vi -Duser.country=VN" test
java -cp target/classes cinema.Main
java -cp "target/classes;target/test-classes" cinema.Week1Scenarios
java -cp target/classes cinema.Main --interactive
```

Nếu Maven chưa nằm trong PATH nhưng repository có Maven local:

```powershell
.\tmp\maven39\apache-maven-3.9.9\bin\mvn.cmd --offline --no-transfer-progress clean test
```

### 9.3 Các evidence chính

- [Week 1 console scenarios](../evidence/week-1-console-output.txt)
- [Week 2 test output](../evidence/week-2-test-output.txt)
- [Week 3 concurrency output](../evidence/week-3-concurrency-test-output.txt)
- [Week 4 integrated test/demo](../evidence/week-4-integration-test-output.txt)
- [Week 4 locale regression](../evidence/week-4-locale-regression.txt)
- [Week 4 extensibility diff](../evidence/week-4-integration-extensibility-diff.txt)
- [Ảnh viết tay của Triệu](../evidence/week-4-handwritten/trieu-luxury-tier-pricing-trace.png)
- [Ảnh trace polymorphism của Triệu](../evidence/week-4-handwritten/trieu-luxury-booking-polymorphism-trace.png)
- [Ảnh viết tay của Thắng](../evidence/week-4-handwritten/thang-member-discount-calculation-and-trace.jpg)

## 10. Phân công và lịch sử đóng góp

| Tuần | Nguyễn Thành Triệu | Nguyễn Đức Thắng |
| --- | --- | --- |
| 1 | Executable scenarios happy path, double-booking, pricing polymorphism; README và console evidence. Branch `week1/trieu-executable-scenarios`; tích hợp source tại `3bbeea8`. | Phân tích bài toán, use case, UML, Self-Explanation và báo cáo; các commit hồ sơ chính `a28a551`, `9a4f05b`; tích hợp tại `1e5eccf`. |
| 2 | `GroupDiscountPolicy`, domain validation, discount/invalid-operation tests và handwritten trace; implementation `047199f`. | Maven/JUnit 5, pricing API dùng `long`, pricing tests và handwritten pricing trace; các commit chính `c9e61bc`, `7b14e1a`, `2038061`. |
| 3 | `BookingService`, `BookingStatus`, lifecycle tests, last-seat concurrency test; `1eab3dc`. | `SeatLock`, `Clock`/`Duration`, lock expiration và ownership tests; các commit chính `9bf4de4`, `ae55bb3`, `91caf56`, `d0f7488`. |
| 4 | `LuxuryTier`, A6 wiring, Luxury tests, combined flow review, evidence và handwritten trace; `a00434d`, `469f605`, `c61bce8`. | `MemberDiscountPolicy`, console Member option, Member tests, integration review và evidence; `4d1dc10`, `087ce92`, `79f9bfe`, `7975bcf`. |

Các phần tích hợp tuần 4 gồm locale-safe assertion, test kết hợp, UML và
hồ sơ được ghi trong `9e76e9a`, `26479fb`, `962cf35`, `c1e2a4f` và
`2ef6c42`, sau đó merge vào `main` tại `e828683`.

## 11. AI Usage Log và minh chứng cá nhân

AI Usage Logs được tách theo thành viên và theo tuần:

- [Week 1 AI Usage Log](week-1-ai-usage-log.md)
- [Week 2 AI Usage Log](week-2-ai-usage-log.md)
- [Week 3 AI Usage Log](week-3-ai-usage-log.md)
- [AI Usage Log tuần 4 của Triệu](week-4-trieu-ai-usage-log.md)
- [AI Usage Log tuần 4 của Thắng](week-4-thang-ai-usage-log.md)

Các log ghi công cụ/model đã xác nhận, prompt tóm tắt, phần AI hỗ trợ,
phần kiểm tra/điều chỉnh, test/evidence liên quan và các giới hạn như
overflow hoặc locale-sensitive assertion.

## 12. Hạn chế kỹ thuật

- Seat lock và concurrency chỉ được bảo vệ trong cùng một JVM.
- Không có database transaction hoặc distributed locking.
- File history không khôi phục inventory/lock sau restart.
- Lưu confirmation và chuyển trạng thái ghế chưa nằm trong một transaction
  chung.
- Lock hết hạn được dọn khi có thao tác truy cập; chưa có background
  scheduler.
- Member là policy mô phỏng, chưa có xác thực tài khoản/thẻ thành viên.
- Chưa có payment thật, authentication, GUI hoặc REST API.

Các giới hạn này phù hợp với phạm vi Java console của đề bài và được nêu để
phân biệt prototype OOP với hệ thống production.

## 13. Reflection cá nhân cần bổ sung

### Nguyễn Thành Triệu - MSSV 23110066

Triệu cần bổ sung bằng lời của mình:

1. Một khó khăn thực tế khi triển khai `BookingService`, `LuxuryTier` hoặc
   test concurrency.
2. Một quyết định thiết kế Triệu hiểu rõ nhất và lý do lựa chọn.
3. Một lỗi hoặc giới hạn Triệu đã trực tiếp kiểm tra và cách điều chỉnh.
4. Điều Triệu học được về interface, encapsulation hoặc test từ đồ án.
5. Nếu có thêm thời gian, Triệu sẽ cải thiện phần nào và vì sao.

Phần này không được suy ra tự động từ Git history; Triệu cần tự viết theo
trải nghiệm thật trước khi nộp.

### Nguyễn Đức Thắng - MSSV 23110062

Thắng bổ sung reflection cá nhân tương ứng trong hồ sơ riêng
[`docs/thang-defense.md`](thang-defense.md) theo trải nghiệm thực tế của
mình. Bộ câu hỏi trong hồ sơ là tài liệu chuẩn bị, không phải bằng chứng đã
vấn đáp.

## 14. Checklist trước khi nộp

- [x] Source code và README build/run.
- [x] Domain model và các yêu cầu chức năng chính.
- [x] Pricing/discount polymorphism.
- [x] Validation, double-booking, lifecycle và concurrency tests.
- [x] UML tích hợp tuần 4.
- [x] AI Usage Logs.
- [x] Test evidence và handwritten evidence.
- [x] Main và console demo.
- [ ] Triệu hoàn thiện reflection bằng trải nghiệm thật.
- [ ] Thắng hoàn thiện reflection bằng trải nghiệm thật.
- [ ] Hai thành viên luyện và thực hiện oral/live defense.
- [ ] Rà soát liên kết, identity/commit mapping và nộp final report.
