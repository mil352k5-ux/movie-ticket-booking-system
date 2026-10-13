# Ghi chú concurrency tuần 3

## Vòng đời booking

```text
PENDING ──confirm──> CONFIRMED
   │
   ├──cancel──> CANCELLED
   └──expire──> EXPIRED
```

Booking chỉ được chuyển trạng thái từ `PENDING`. Vì vậy các thao tác xác
nhận lần hai, xác nhận booking đã hủy/hết hạn hoặc hủy booking đã xác nhận
đều bị từ chối bằng `IllegalStateException`.

## Phân chia trách nhiệm

`Booking` giữ các ghế đã chọn, cách tính subtotal/discount/total và trạng
thái của chính booking. Các method chuyển trạng thái không mở public để code
bên ngoài tự ý gán trạng thái.

`BookingService` điều phối workflow: tạo `Booking`, gọi
`Showtime.tryLockSeats()`, xác nhận bằng đúng booking ID hoặc trả ghế khi
hủy. Service không truy cập trực tiếp các map trạng thái/lock và không lặp
lại logic giá hoặc discount.

## Critical section của thao tác khóa ghế

`Showtime.tryLockSeats()` dùng cùng một `synchronized` method cho cả hai
bước: kiểm tra toàn bộ seat ID và trạng thái, sau đó mới ghi các ghế sang
`LOCKED`. Nếu một ghế không hợp lệ hoặc không còn `AVAILABLE`, method ném
exception trước khi ghi ghế nào. Đặt check và lock trong cùng critical
section ngăn hai thread cùng thấy ghế cuối là trống rồi cùng ghi thành công.

## Giới hạn của `synchronized`

`synchronized` bảo vệ vùng code và dữ liệu của một `Showtime` khi các thread
chạy trong cùng một JVM. Nó không đồng bộ được hai process hoặc hai
application server khác nhau; mỗi server có monitor riêng nên cả hai vẫn có
thể cùng nhận request nếu chỉ dùng khóa trong bộ nhớ.

Trong production có thể dùng transaction database với row-level lock, unique
constraint trên cặp `(showtime_id, seat_id)`, hoặc optimistic locking với
`version`. Các giải pháp đó chưa được triển khai trong source tuần này; bài
test hiện tại chỉ chứng minh hành vi an toàn trong một JVM.
