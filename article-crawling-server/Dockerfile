FROM python:3.11-slim

WORKDIR /code

ENV PYTHONPATH=/code

# requirements.txt 복사
COPY ./requirements.txt /code/requirements.txt

# pip 다운로드
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

# 폴더(코드) 복사
COPY articlecrawlingserver /code/articlecrawlingserver

# 실행 권한 부여
RUN chmod +x /code/articlecrawlingserver/ynaCrawlingExecutor.py

# /code/articlecrawlingserver/ynaCrawlingExecutor.py 실행
ENTRYPOINT ["/usr/local/bin/python", "/code/articlecrawlingserver/ynaCrawlingExecutor.py"]