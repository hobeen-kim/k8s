class ArticleData:
    title = ''
    url = ''
    time = ''
    section = ''
    content = ''
    tags = []

    def __init__(self, title, url, time, section, content, tags):
        self.title = title
        self.url = url
        self.time = time
        self.section = section
        self.content = content
        self.tags = tags

    def json(self):
        return {
            'title': self.title,
            'url': self.url,
            'time': self.time,
            'section': self.section,
            'content': self.content,
            'tags': self.tags
        }
