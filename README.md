# discord-chat-analysis-java

透過 Discord Bot 爬取伺服器中的使用者訊息資料，對訊息流量進行分析與視覺化，並從時間分佈、使用者行為等不同面向進行分析，以協助理解社群互動情形。

## 功能簡介

### 爬蟲

[使用者輸入](https://github.com/l-zch/discord-chat-analysis-java/blob/main/app/src/main/java/app/SlashBot.java)：透過使用者在 discord 聊天室輸入，取得需要進行的操作<br/>
[爬蟲](https://github.com/l-zch/discord-chat-analysis-java/blob/main/app/src/main/java/crawler/Crawler.java)：取得伺服器聊天室的所有訊息 json

### 資料可視化

[可視化](https://github.com/l-zch/discord-stat-frontend)：使用 Vue 將所有訊息經過篩選後輸出使用者需要的資料<br/>
[伺服器](https://github.com/l-zch/discord-chat-analysis-java/blob/main/app/src/main/java/app/CustomHttpServer.java)：使用 jwebserver 將產生的檔案放進伺服器中並將網址回傳給使用者

## 操作流程

- 首先開啟 discord bot
- 接下在 discord 中輸入 `/crawler` 執行爬蟲程式
- 輸入 `/server` 查看分析結果。會在本地開啟一個 http 伺服器，並將網址回傳給使用者

## Quick Start

```sh
git clone https://github.com/l-zch/discord-chat-analysis-java.git
cd discord-chat-analysis-java
./gradlew run
```

第一次運行會創建 config.json

```
{ "botToken" : "put your bot token here" }
```

更換 bot token 後再次運行

```
./gradlew run
```

在 discord 上輸入 `/crawler` ，抓取當前群組的資料

<img width="603" alt="image" src="https://github.com/l-zch/discord-chat-analysis-java/assets/89698082/901e25ad-b978-45c3-93f7-4ff04a3e24d0">

等待資料抓取完成，輸入 `/server` 會在本地 `http://localhost:8000/` 開啟統計數據的互動式網頁
