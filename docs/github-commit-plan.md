# GitHub Commit Plan

## Mục tiêu

Lịch sử commit phải giúp GVHD thấy được tiến trình làm việc, không chỉ thấy một lần upload toàn bộ project.

## Cách tạo lịch sử trung thực

Vì project hiện chưa có commit lịch sử, nhóm không nên giả vờ rằng các commit mới được tạo vào ngày 04/10/2026. Có thể tổ chức lại source hiện tại bằng các commit mới và ghi rõ trong báo cáo rằng đây là bước bổ sung hồ sơ theo feedback.

## Commit đề xuất

### Commit 1

```text
docs: organize week 1 project evidence
```

Nội dung: README, class diagram, design decisions, báo cáo tuần 1 và AI collaboration log.

### Commit 2

```text
feat: add cinema domain model and booking demo
```

Nội dung: Movie, Screen, Showtime, Seat, Booking, PricingTier, các seat tier, demo happy path và double-booking.

### Commit 3

```text
test: verify week 1 booking scenarios
```

Nội dung: output happy path, output lỗi double-booking và lệnh compile/run.

### Commit 4

```text
feat: add discount policies and domain validation
```

Nội dung: StudentDiscountPolicy, GroupDiscountPolicy và validation.

### Commit 5

```text
test: add week 2 pricing and validation checks
```

Nội dung: `Week2Tests`, test output và báo cáo tuần 2.

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
