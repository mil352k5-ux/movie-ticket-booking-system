# AI Usage Log tuần 3 - Nguyễn Thành Triệu

- **Thời điểm/công cụ:** 14/10/2026, Codex (GPT-5) trong phiên hỗ trợ này.
- **Prompt tóm tắt:** lấy `origin/main` sau khi phần lock expiration của
  Nguyễn Đức Thắng được merge; xây dựng `BookingStatus`, `BookingService`,
  lifecycle tests, concurrency test dùng `ExecutorService` và
  `CountDownLatch`, ghi chú giới hạn của `synchronized`, README và evidence;
  không commit hoặc push.
- **Nội dung được AI hỗ trợ:** đề xuất và triển khai các file source/test,
  tài liệu concurrency và lệnh kiểm chứng trong phạm vi prompt.
- **Phạm vi tự review:** Triệu giữ quyền review thiết kế và tự quyết định
  việc commit/push bằng Git identity của mình. Trong phiên hỗ trợ, mã được
  kiểm chứng bằng Maven: baseline 38 test pass trước thay đổi và 48 test pass
  sau thay đổi; chưa commit hoặc push.
- **Giới hạn:** concurrency hiện chỉ bảo vệ trong một JVM bằng
  `synchronized`; chưa có database transaction, distributed lock, REST API
  hoặc GUI.
