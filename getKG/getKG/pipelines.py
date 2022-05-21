# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from itemadapter import ItemAdapter


class HTMLPipeline:
    def process_item(self, item, spider):
        name = item["name"].strip()
        path = name + ".txt"
        with open(path, 'wb') as f:
            for s in item["content"]:
                f.write(s.encode("utf-8"))
        return item
