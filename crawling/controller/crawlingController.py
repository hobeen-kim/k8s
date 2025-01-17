import time

import uvicorn
from fastapi import FastAPI

from crawling.ynaCrawlingExecutor import produce_yna_news_data

app = FastAPI()

@app.get("/")
async def crawling():

    return "hello crawling"


@app.post("/crawling/yna")
async def crawling():
    start_time = time.time()

    news_articles = await produce_yna_news_data()

    print(f"\n총 {len(news_articles)}개의 기사를 찾았습니다. 소요시간: {time.time() - start_time:.2f}초")

    return news_articles


if __name__ == "__main__":
    uvicorn.run("controller.crawlingController:app", host="0.0.0.0", port=8000, reload=True)
