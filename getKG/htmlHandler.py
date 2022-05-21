import os

import bs4
from bs4 import *


class HTMLHandler:
    def handle_element(self, element):
        if element is None:
            return ""
        if isinstance(element, str):
            return element.strip()
        name = element.name
        if (name == 'p') or (name == 'ul') or (name == 'li') or (name == 'h3') or (name == 'span') or (
                name == 'strong') or (name == 'pre') or (name == 'b'):
            result = ""
            for content in element.contents:
                result += self.handle_element(content)
            if (name == 'span') or (name == 'strong'):
                return result.strip()
            else:
                return result.strip() + "\n"
        return ""

    def handle_h2(self, h2):
        result = h2.decode() + "\n"
        t = h2.nextSibling.nextSibling
        f = False
        while t is not None and t.name != 'h2':
            if t.name == 'h3' and '实例' in t.get_text():
                f = True
            elif t.name == 'h3' or t.name == 'hr':
                f = False
            if f:
                t = t.nextSibling
                continue
            if t.name == 'p' and t.nextSibling is not None:
                if type(t.nextSibling.nextSibling) == bs4.element.Comment and '下一' in t.get_text():
                    break
            if type(t) == bs4.element.Tag:
                result += t.decode()
            t = t.nextSibling
        return result


if __name__ == '__main__':
    d = []
    i = 0
    for root, dirs, files in os.walk(r".\rawHTML"):
        if len(dirs) > 0:
            d = dirs
            continue
        for file in files:
            with open(os.path.join(root, file), encoding="utf-8") as fin, \
                    open(os.path.join(".\\corpus", d[i], file), "w", encoding="utf-8") as fout, \
                    open(os.path.join(".\\code", d[i], file), "w", encoding="utf-8") as cout:
                html_doc = fin.read()
                html_doc = html_doc.replace('<div class="example_code">', '<pre class="example_code">')
                html_doc = html_doc.replace('</span></div>\n</div>', '</span></div>\n</pre>')
                html_doc = html_doc.replace('</span><br>\n</div>', '</span><br>\n</pre>')
                html_doc = html_doc.replace('</span></div></div>', '</span></div></pre>')
                html_doc = html_doc.replace('runoob', 'JavaKG')
                html_doc = html_doc.replace('Runoob', 'JavaKG')
                html_doc = html_doc.replace('菜鸟教程', 'JavaKG')
                html_doc = html_doc.replace('RUNOOB', 'JAVAKG')
                html_doc = html_doc.replace('.JavaKG.com', '.runoob.com')
                soup = BeautifulSoup(html_doc, 'html.parser')
                ctxt = ""
                for h3 in soup.find_all('h3'):
                    if '实例' not in h3.get_text():
                        continue
                    parent = h3.find_previous('h2', class_=False)
                    if parent is None:
                        continue
                    ctxt += parent.get_text().strip() + "\n"
                    t = h3.nextSibling
                    while t is not None and t.name != 'h2' and t.name != 'h3' and t.name != 'hr':
                        if type(t) == bs4.element.Tag:
                            ctxt += t.decode()
                        t_next = t.nextSibling
                        if type(t) == bs4.element.Tag:
                            t.decompose()
                        t = t_next
                    ctxt += "\n*******\n"

                for example in soup.find_all('div', class_='example'):
                    parent = example.find_previous('h2', class_=False)
                    if parent is None:
                        continue
                    ctxt += parent.get_text().strip() + "\n"

                    p_pre = example.find_previous_sibling('p')
                    if (p_pre is not None) and (
                            ('例' in p_pre.get_text()) or ('以下' in p_pre.get_text()) or (
                            '下面' in p_pre.get_text()) or (
                                    '如下' in p_pre.get_text())):
                        ctxt += p_pre.decode()
                        p_pre.decompose()

                    ctxt += example.decode()
                    p = example.find_next_sibling('p')
                    example.decompose()
                    if (p is not None) and (
                            ('例子' in p.get_text()) or ('以上' in p.get_text()) or ('实例' in p.get_text()) or (
                            '结果' in p.get_text())):
                        ctxt += p.decode()
                        pre = p.find_next_sibling('pre')
                        p.decompose()
                        if pre is not None:
                            ctxt += pre.decode()
                            pre.decompose()

                    ctxt += "\n*******\n"

                cout.write(ctxt)
                handler = HTMLHandler()
                out = ""
                for h2 in soup.find_all('h2', class_=False):
                    out += handler.handle_h2(h2) + "\n*******\n"
                fout.write(out)
        i += 1
