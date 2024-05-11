---
Latest Modified: 2023-01-31
---



## 1. 背景 Context

真实的应用里，日志的重要性是比数据库低的。开发者可以通过访问数据库，查找 Bug 原因，但同时开发者也可以修改数据库。修改数据库是十分禁忌的一件事，所以仅仅允许部分人员访问数据库。

而日志系统则不同，日志系统可以：

- 根据日志信息生成指标图（metric），用于监控。
- 根据日志信息，查找 Bug 原因。

通常而言，日志系统不应该包含密码等敏感信息。但实际上，有时不小心会将这种敏感信息打印成日志。在这种情况，有且仅有一种方式：**将日志删掉**

由于日志的这种特殊性，如果开发的习惯稍不好，**日志的重要性可能超越数据库**。



## 2. 注意点 Notices

- 归档日志应该存储在非公开网络里。
- 在开发时，需要注意日志没有敏感信息。