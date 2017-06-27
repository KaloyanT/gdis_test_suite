FROM python:3.6-alpine
WORKDIR /app
COPY requirements.txt .

RUN apk --no-cache add musl-dev linux-headers g++
RUN pip install --no-cache-dir \
    -r requirements.txt


COPY . .
ENTRYPOINT ["python3", "-m", "api"]
