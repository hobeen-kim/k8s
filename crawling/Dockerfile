FROM python:3.11-slim

WORKDIR /code

# requirements.txt 복사
COPY ./requirements.txt /code/requirements.txt

# pip 다운로드
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

# 폴더(코드) 복사
COPY ./crawling /code/crawling

# /code/crawling/ynaCrawlingExecutor.py 실행
CMD ["python", "/code/crawling/ynaCrawlingExecutor.py"]