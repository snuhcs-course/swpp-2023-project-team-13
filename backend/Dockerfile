FROM node:20-bullseye-slim

RUN  apt-get update \
  && apt-get install -y wget libpq-dev g++ make build-essential \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app


COPY . .

RUN npm install && npm run build

RUN rm -r src

EXPOSE 80


ENV NODE_ENV=production
ENTRYPOINT ["npm","run","start:prod"]

