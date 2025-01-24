import asyncio
import time

import aiohttp
from bs4 import BeautifulSoup
from newspaper import Article

from entity.articleData import ArticleData
from producer.producer import send_data

# 섹션
sections = {
    "politics": "정치",
    "north-korea": "북한",
    "economy": "경제",
    "market-plus": "마켓+",
    "industry": "산업",
    "society": "사회",
    "local": "전국",
    "international": "세계",
    "culture": "문화",
    "health": "건강",
    "entertainment": "연예",
    "sports": "스포츠",
}

async def produce_yna_news_data():
    # 하나의 세션을 공유
    async with aiohttp.ClientSession() as session:
        tasks = []

        for section_key, section_value in sections.items():
            for page in range(1, 2): # 1페이지까지만 가져옴
                tasks.append(get_news_list(session, f"https://www.yna.co.kr/{section_key}/all", page, section_value))

        # 모든 태스크가 완료될 때까지 대기
        results = await asyncio.gather(*tasks)

        # 결과 합치기
        all_articles = []
        for result in results:
            all_articles.extend(result)

        return all_articles


# 뉴스 기사를 가져오는 함수
async def get_news_list(session: aiohttp.ClientSession, url: str, page: int, section: str):
    # 헤더 설정
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'
    }

    request_url = f"{url}/{page}"

    async with session.get(request_url, headers=headers) as response:

        if response.status != 200:
            print("Failed to fetch the webpage")
            return []

        html = await response.text()
        soup = BeautifulSoup(html, 'html.parser')
        # list-type038 클래스 내의 뉴스 항목들을 찾음
        news_items = soup.select('.list-type038 .item-box01')

        # 각 뉴스 항목에 대한 태스크 생성
        tasks = []

        for item in news_items:
            news_link = item.select_one('.news-con .tit-wrap')
            time = item.select_one('.info-box01 .txt-time').text.strip()

            if news_link:
                title = news_link.select_one('.tit-news').text.strip()
                news_url = news_link['href']
                if not news_url.startswith('http'):
                    news_url = 'https://www.yna.co.kr' + news_url

                # 내용 추출 태스크 생성
                task = extract_contentTags(news_url, session)
                tasks.append({
                    'task': task,
                    'title': title,
                    'url': news_url,
                    'time': time,
                    'section': section,
                })

            # 모든 태스크를 동시에 실행
        contents = await asyncio.gather(*(item['task'] for item in tasks))

        # 결과 조합
        articles = []
        for task_info, content in zip(tasks, contents):
            articles.append(
                ArticleData(task_info['title'], task_info['url'], task_info['time'], task_info['section'], content[0], content[1])
            )

        return articles


# 뉴스 기사 내용 & 태그 추출
async def extract_contentTags(url: str, session: aiohttp.ClientSession):
    try:
        async with session.get(url) as response:
            if response.status != 200:
                return "내용을 가져올 수 없습니다."

            # 기사 내용 추출
            html = await response.text()
            article = Article(url, language='ko')
            article.download_state = 2  # Skip download
            article.html = html
            article.parse()

            # 태그 추출
            soup = BeautifulSoup(html, 'html.parser')
            tags = []
            keyword_zone = soup.select_one('.keyword-zone')
            if keyword_zone:
                tag_elements = keyword_zone.select('.list-text01.style03 li a')
                tags = [tag.text.strip('#') for tag in tag_elements]

            return article.text, tags
    except Exception as e:
        print(f"Error extracting content from {url}: {str(e)}")
        return "내용을 가져올 수 없습니다."


# 메인 함수
if __name__ == "__main__":
    start_time = time.time()

    print("뉴스 기사 수집 시작")

    loop = asyncio.get_event_loop()
    news_articles = loop.run_until_complete(produce_yna_news_data())

    print(f"\n총 {len(news_articles)}개의 기사를 찾았습니다. 소요시간: {time.time() - start_time:.2f}초")

    start_time = time.time()

    loop.run_until_complete(send_data(news_articles))

    print("전송 완료. 소요시간: ", time.time() - start_time)
