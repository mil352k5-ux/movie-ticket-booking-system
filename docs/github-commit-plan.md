# GitHub Commit Plan - Week 1 Resubmission

## Mục tiêu

Lịch sử commit thay thế tuần 1 phải giúp GVHD thấy được tiến trình làm việc của Nguyễn Đức Thắng và Nguyễn Thành Triệu, không chỉ thấy một lần upload toàn bộ project.

## Cách tạo lịch sử trung thực

Vì project hiện chưa có commit lịch sử, nhóm không nên giả vờ rằng các commit mới được tạo vào ngày 04/10/2026. Có thể tổ chức lại source hiện tại bằng các commit mới và ghi rõ trong báo cáo rằng đây là bước bổ sung hồ sơ theo feedback.

## Commit đề xuất cho riêng tuần 1

### Commit 1 - Thắng

```text
docs: organize week 1 project evidence
```

Nội dung: README, class diagram, design decisions, báo cáo tuần 1 và AI collaboration log.

### Commit 2 - Triệu

```text
feat: add cinema domain model and booking demo
```

Nội dung: Movie, Screen, Showtime, Seat, Booking, PricingTier, các seat tier, demo happy path và double-booking.

### Commit 3 - Cả hai

```text
test: verify week 1 booking scenarios
```

Nội dung: output happy path, output lỗi double-booking và lệnh compile/run.

### Commit 4 - Không đưa vào báo cáo tuần 1

```text
feat: add discount policies and domain validation
```

Nội dung này thuộc tuần 2, không dùng làm bằng chứng cho báo cáo thay thế tuần 1.

### Commit 5 - Không đưa vào báo cáo tuần 1

```text
test: add week 2 pricing and validation checks
```

Nội dung này thuộc tuần 2, không dùng làm bằng chứng cho báo cáo thay thế tuần 1.

## Nội dung nên ghi trong commit body

```text
Why:
Separate seat pricing from Booking so a new tier can be added without
rewriting booking calculation.

AI assistance:
AI suggested the PricingTier interface and listed possible tier classes.

Student work:
The team chose the supported tiers, implemented the classes, checked the
prices, and verified the output with the console demo.

Verification:
javac -d out src/main/java/cinema/*.java
java -cp out cinema.Main
```
