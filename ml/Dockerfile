FROM python:3.11

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

RUN apt-get update
RUN apt-get install -y cmake

COPY . .
RUN pip install -r requirements.txt

EXPOSE 81

ENTRYPOINT ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "81"]



