# Azure Function

## 1. 介绍

Azure Function 是 FaaS 的 serverless 服务，对标 AWS Lambda，主张的是开发者只需要在意开发程序，而不需要在意操作系统、服务器等细节。Azure Function 的 plan，大概分为按使用量计费和租服务器两类。Function 主要是以 event- drive 为主，如: 定期执行、HTTP 请求事件触发。

- Function 通常只能执行短时间任务。[["]](https://learn.microsoft.com/en-us/azure/azure-functions/performance-reliability)
- 不适合后台任务。
- 跨 function 通讯可以使用 state、storage queue、Service Bus、Event hubs 等
- 尽量无状态且幂等，方便重试。
- 防御式编程，减少因执行失败、网络不良等导致业务不完整。
- 一个 Azure Function App 可以装载一个 function，但也可以多个，需根据实际情况执行。
  - function 数量越多，越吃内存，启动越慢
  - 执行量稳定的 function 和执行量会剧增的 function 建议分离
- Batching messages 更佳。因为一次性可以处理若干数据。
- 可调试并性量
- NodeJS 版本过期很快，需考虑代码更新频率。
