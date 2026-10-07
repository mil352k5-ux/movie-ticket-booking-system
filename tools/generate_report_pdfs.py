from pathlib import Path
from html import escape
import re

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (
    BaseDocTemplate,
    Frame,
    PageTemplate,
    Paragraph,
    Spacer,
    PageBreak,
    Table,
    TableStyle,
    Preformatted,
)


ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "output" / "pdf"


def register_fonts():
    regular = Path("C:/Windows/Fonts/arial.ttf")
    bold = Path("C:/Windows/Fonts/arialbd.ttf")
    if regular.exists() and bold.exists():
        pdfmetrics.registerFont(TTFont("Arial", str(regular)))
        pdfmetrics.registerFont(TTFont("Arial-Bold", str(bold)))
        return "Arial", "Arial-Bold"
    return "Helvetica", "Helvetica-Bold"


REGULAR, BOLD = register_fonts()


def inline_markup(text):
    parts = re.split(r"(`[^`]+`)", text)
    output = []
    for part in parts:
        if len(part) >= 2 and part.startswith("`") and part.endswith("`"):
            value = part[1:-1]
            if any(ord(character) > 127 for character in value):
                output.append(escape(value))
            else:
                output.append(f'<font name="Courier">{escape(value)}</font>')
        else:
            output.append(escape(part))
    return "".join(output)


def make_styles():
    styles = getSampleStyleSheet()
    styles.add(ParagraphStyle(
        name="ReportTitle",
        parent=styles["Title"],
        fontName=BOLD,
        fontSize=18,
        leading=23,
        alignment=TA_CENTER,
        textColor=colors.HexColor("#17365D"),
        spaceAfter=14,
    ))
    styles.add(ParagraphStyle(
        name="ReportH2",
        parent=styles["Heading2"],
        fontName=BOLD,
        fontSize=13,
        leading=17,
        textColor=colors.HexColor("#1F4E79"),
        spaceBefore=10,
        spaceAfter=6,
    ))
    styles.add(ParagraphStyle(
        name="ReportH3",
        parent=styles["Heading3"],
        fontName=BOLD,
        fontSize=11,
        leading=14,
        textColor=colors.HexColor("#365F91"),
        spaceBefore=7,
        spaceAfter=4,
    ))
    styles.add(ParagraphStyle(
        name="ReportBody",
        parent=styles["BodyText"],
        fontName=REGULAR,
        fontSize=9.6,
        leading=14,
        spaceAfter=5,
    ))
    styles.add(ParagraphStyle(
        name="ReportBullet",
        parent=styles["BodyText"],
        fontName=REGULAR,
        fontSize=9.6,
        leading=14,
        leftIndent=14,
        firstLineIndent=-8,
        bulletIndent=2,
        spaceAfter=3,
    ))
    styles.add(ParagraphStyle(
        name="TableCell",
        parent=styles["BodyText"],
        fontName=REGULAR,
        fontSize=7.4,
        leading=10,
    ))
    styles.add(ParagraphStyle(
        name="TableHeader",
        parent=styles["BodyText"],
        fontName=BOLD,
        fontSize=7.4,
        leading=10,
        textColor=colors.white,
    ))
    return styles


def parse_markdown(path, styles):
    lines = path.read_text(encoding="utf-8").splitlines()
    story = []
    paragraph_lines = []
    bullet_lines = []
    code_lines = []
    table_lines = []
    in_code = False

    def flush_paragraph():
        nonlocal paragraph_lines
        if paragraph_lines:
            text = " ".join(line.strip() for line in paragraph_lines)
            story.append(Paragraph(inline_markup(text), styles["ReportBody"]))
            paragraph_lines = []

    def flush_bullets():
        nonlocal bullet_lines
        for item in bullet_lines:
            story.append(Paragraph("• " + inline_markup(item), styles["ReportBullet"]))
        bullet_lines = []

    def flush_code():
        nonlocal code_lines
        if code_lines:
            story.append(Preformatted("\n".join(code_lines), ParagraphStyle(
                "CodeBlock",
                fontName="Courier",
                fontSize=7.4,
                leading=9,
                leftIndent=7,
                rightIndent=7,
                backColor=colors.HexColor("#F3F6F9"),
                borderColor=colors.HexColor("#D9E2F3"),
                borderWidth=0.5,
                borderPadding=6,
                spaceBefore=4,
                spaceAfter=7,
            )))
            code_lines = []

    def flush_table():
        nonlocal table_lines
        if not table_lines:
            return
        rows = []
        for line in table_lines:
            cells = [cell.strip() for cell in line.strip().strip("|").split("|")]
            if all(re.fullmatch(r":?-+:?", cell) for cell in cells):
                continue
            rows.append(cells)
        if rows:
            width = max(len(row) for row in rows)
            normalized = [row + [""] * (width - len(row)) for row in rows]
            formatted = []
            for row_index, row in enumerate(normalized):
                style = styles["TableHeader"] if row_index == 0 else styles["TableCell"]
                formatted.append([Paragraph(inline_markup(cell), style) for cell in row])
            col_width = min(180 / width, 55)
            table = Table(formatted, colWidths=[col_width * mm] * width, repeatRows=1)
            table.setStyle(TableStyle([
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#2F75B5")),
                ("GRID", (0, 0), (-1, -1), 0.35, colors.HexColor("#B7C9D6")),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("LEFTPADDING", (0, 0), (-1, -1), 5),
                ("RIGHTPADDING", (0, 0), (-1, -1), 5),
                ("TOPPADDING", (0, 0), (-1, -1), 4),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F7FBFF")]),
            ]))
            story.append(table)
            story.append(Spacer(1, 5))
        table_lines = []

    for line in lines:
        if line.startswith("```"):
            flush_paragraph()
            flush_bullets()
            flush_table()
            if in_code:
                flush_code()
            in_code = not in_code
            continue
        if in_code:
            code_lines.append(line)
            continue
        if line.startswith("|"):
            flush_paragraph()
            flush_bullets()
            table_lines.append(line)
            continue
        if table_lines:
            flush_table()
        if line.startswith("### "):
            flush_paragraph()
            flush_bullets()
            story.append(Paragraph(inline_markup(line[4:]), styles["ReportH3"]))
        elif line.startswith("## "):
            flush_paragraph()
            flush_bullets()
            story.append(Paragraph(inline_markup(line[3:]), styles["ReportH2"]))
        elif line.startswith("# "):
            flush_paragraph()
            flush_bullets()
            story.append(Paragraph(inline_markup(line[2:]), styles["ReportTitle"]))
        elif line.startswith("- "):
            flush_paragraph()
            bullet_lines.append(line[2:])
        elif not line.strip():
            flush_paragraph()
            flush_bullets()
        else:
            flush_bullets()
            paragraph_lines.append(line)

    flush_paragraph()
    flush_bullets()
    flush_table()
    flush_code()
    return story


def footer(canvas, doc):
    canvas.saveState()
    canvas.setFont(REGULAR, 8)
    canvas.setFillColor(colors.HexColor("#6B7280"))
    canvas.drawString(20 * mm, 12 * mm, "Movie Ticket Booking System - OOP")
    canvas.drawRightString(190 * mm, 12 * mm, f"Trang {doc.page}")
    canvas.restoreState()


def create_pdf(source, target):
    target.parent.mkdir(parents=True, exist_ok=True)
    document = BaseDocTemplate(
        str(target),
        pagesize=A4,
        leftMargin=20 * mm,
        rightMargin=20 * mm,
        topMargin=16 * mm,
        bottomMargin=20 * mm,
        title=source.stem,
        author="Movie Ticket Booking System",
    )
    frame = Frame(document.leftMargin, document.bottomMargin, document.width, document.height, id="normal")
    document.addPageTemplates([PageTemplate(id="report", frames=frame, onPage=footer)])
    document.build(parse_markdown(source, make_styles()))


for report_name in ("weekly-report-week-1.md", "weekly-report-week-2.md"):
    source_path = ROOT / "docs" / report_name
    target_path = OUTPUT / report_name.replace(".md", ".pdf")
    create_pdf(source_path, target_path)
    print(target_path)
