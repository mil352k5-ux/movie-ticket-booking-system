# GitHub Commit Plan - Week 1 Resubmission

## Mục tiêu

Lịch sử commit thay thế tuần 1 phải giúp GVHD thấy được tiến trình làm việc của Nguyễn Đức Thắng và Nguyễn Thành Triệu, không chỉ thấy một lần upload toàn bộ project.

## Cách tạo lịch sử trung thực

Vì project ban đầu chưa có commit lịch sử, nhóm không giả vờ rằng các commit mới được tạo vào ngày 04/10/2026. Các commit hiện tại được tạo sau feedback để bổ sung hồ sơ; điều này được ghi rõ trong commit body.

## Commit đề xuất cho riêng tuần 1

### Commit hiện tại - Cả hai

```text
docs: separate week 1 resubmission scope
```

Nội dung: giới hạn báo cáo thay thế ở phạm vi tuần 1, ghi phân công Thắng/Triệu, bổ sung AI log và liên kết GitHub thật.

### Các commit source trước đó

```text
chore: add current project snapshot
```

Nội dung: snapshot source hiện tại và tài liệu đã có. Đây không phải lịch sử giả lập; nhóm cần hiểu và bảo vệ được phần code này.

### Commit tài liệu AI

```text
docs: record transparent AI collaboration
```

Nội dung: nhật ký prompt, phần AI gợi ý, phần nhóm quyết định/sửa và cách kiểm chứng.

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
