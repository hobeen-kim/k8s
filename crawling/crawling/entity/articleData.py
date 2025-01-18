class ArticleData:
    title = ''
    url = ''
    time = ''
    section = ''
    content = ''

    def __init__(self, title, url, time, section, content):
        self.title = title
        self.url = url
        self.time = time
        self.section = section
        self.content = content

    def json(self):
        return {
            'title': self.title,
            'url': self.url,
            'time': self.time,
            'section': self.section,
            'content': self.content
        }