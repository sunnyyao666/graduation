import scrapy
from getKG.items import HTMLItem


class RunoobSpider(scrapy.Spider):
    name = 'runoob'
    allowed_domains = ['www.runoob.com']
    base_url = 'https://www.runoob.com'
    start_urls = [base_url + "/java"]

    def parse(self, response, *args, **kwargs):
        name = response.xpath('//*[@id="content"]/h1/text()').extract_first()
        content = response.xpath('//*[@id="content"]').extract()
        item = HTMLItem()
        item['name'] = name
        item['content'] = content
        yield item

        url_list = response.xpath('//*[@id="leftcolumn"]/a/@href').extract()
        for url in url_list:
            yield scrapy.Request(self.base_url + url, callback=self.parse)
