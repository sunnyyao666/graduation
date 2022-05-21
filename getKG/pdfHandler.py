import pdfplumber


class PDFHandler:
    def get_column_hirachey(self, name):
        pdf_path = "./" + name + ".pdf"
        txt_path = "./" + name + ".txt"
        with pdfplumber.open(pdf_path) as pdf, open(txt_path, "wb") as txt:
            for i in range(16, 21):
                chars = pdf.pages[i].chars
                x = 0
                y = 0
                s = ""
                f = False
                for char in chars:
                    if (char['x0'] < x) | (char['y0'] + 200 < y):
                        if s != "":
                            s += "\r\n"
                            if f:
                                s = "\r\n" + s
                            txt.write(s.encode("utf-8"))
                            s = ""
                            f = False
                    x = char['x0']
                    y = char['y0']
                    c = char['text']
                    if c == '章':
                        f = True
                    elif not ((c == '.') | (c == '第') | ((c >= '0') & (c <= '9'))):
                        s += c


if __name__ == "__main__":
    p = PDFHandler()
    p.get_column_hirachey("Java")
