# Seat Lock Lifecycle - Tuần 3

## Trạng thái ghế

```text
AVAILABLE --tryLockSeats()--> LOCKED --confirmBooking()--> BOOKED
    ^                            |
    |                            |
    +-- release/lock expired ----+
```

- `AVAILABLE`: ghế có thể được giữ bởi một booking mới.
- `LOCKED`: ghế đang được giữ tạm thời bởi một `SeatLock` chưa hết hạn.
- `BOOKED`: booking đã được xác nhận; trạng thái này không tự hết hạn.

## Thiết kế

`SeatLock` là value object bất biến gồm `bookingId` và `expiresAt`. Method
`isOwnedBy()` kiểm tra chủ sở hữu, còn `isExpiredAt()` kiểm tra thời điểm hết
hạn. Object này chỉ mô tả một lock; việc thay đổi trạng thái ghế vẫn thuộc
trách nhiệm của `Showtime`.

`Showtime` nhận `Clock` và `Duration` qua constructor phục vụ kiểm thử. Constructor
cũ vẫn hoạt động với `Clock.systemUTC()` và thời hạn mặc định 5 phút. Mọi method
thay đổi hoặc đọc state đều xử lý lock hết hạn trước khi đưa ra kết quả.

Các method `tryLockSeats()`, `confirmBooking()`, `releaseSeats()`,
`isSeatAvailable()` và `getSeatStates()` được đồng bộ tại `Showtime`.
`tryLockSeats()` kiểm tra toàn bộ seat ID trước khi cập nhật nên thao tác giữ
nhiều ghế vẫn all-or-nothing.

## Kiểm thử thời gian

Test sử dụng `MutableClock` để chủ động tiến thời gian. Cách này giúp test chạy
nhanh và ổn định, không phụ thuộc đồng hồ thật và không phải dùng `Thread.sleep()`.

Các tình huống chính:

- Lock còn hạn không thể bị booking khác chiếm.
- Lock hết hạn trả ghế về `AVAILABLE`.
- Booking cũ không thể xác nhận lock đã hết hạn.
- Booking khác có thể giữ ghế sau khi lock cũ hết hạn.
- Xác nhận trước hạn chuyển ghế sang `BOOKED`.
- Ghế `BOOKED` không tự hết hạn.
- Chỉ đúng owner mới có thể release lock.
- Map trạng thái trả ra ngoài không thể chỉnh sửa.

## Phạm vi đồng thời

`synchronized` bảo vệ thao tác check-and-update trong một JVM. Khi hệ thống chạy
nhiều application server, cần thêm database transaction, row-level lock hoặc
unique constraint trên cặp `(showtime_id, seat_id)`. Phần tuần 3 này chưa triển
khai distributed locking.
