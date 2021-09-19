---
layout: post 
title: Azure - Aazure Data Facotry
tags: Cloud
comments: 1 
excerpt: Aazure Data Facotry的笔记
typora-root-url: ..\..\..\..
typora-copy-images-to: ..\..\..\..\assets\blog_res\azure
---

## 介绍 Introduction

Azure data factory (ADF) 是 Azure 提供的可横向扩张的 (scale out) 无服务的 (serverless) 的数据相关的一项服务。[^1]

主要包含以下三个方面: 

- 数据集成 (data integration) ：与不同数据源结合的能力。[^3]
- 数据转换 (data transformation) ：数据从一种格式转换成另一种格式的能力。[^4]
- SSIS (SQL Server Integration Services) : 复制或下载文件，加载数据仓库，清除和挖掘数据以及管理 SQL Server 对象和数据。[^2] 

ADF 有版本区分，因此在 StackOverflow 上搜索，需要注意看标签是否带 v2。ADF 对标 AWS 的是 [AWS Data Pipeline](https://aws.amazon.com/cn/datapipeline)。



## 应用场景 Scenario

### 数据备份与恢复

数据备份和恢复依靠 copy activity



## 实现方式



### ARM 模板



### REST









## 功能一栏 List of Features

### Copy Activity

#### 数据一致性验证 (Data consistency verification) 

Copy Activity 提供了数据<div class='sup' data-title="以确保数据不仅成功地从源存储复制到目标存储，而且验证了源存储和目标存储之间的一致性。">一致性验证</div>。通过 `validateDataConsistency` 启动该校验。[^5]

校验的*对象*以及*策略*

- 二进制对象：file size, lastModifiedDate, MD5 checksum 
- 表格数据（tabular data）：` 读取的行数 = 复制的行数 + 跳过的行数`

当数据发生 *不一致性* 时，可以通过 `dataInconsistency` 设置行为

- 中止
- 跳跃

在设定 `logSettings` 和 `path` 可以记录 *不一致* 时候的日志。

#### 监控·测试策略

当 *不允许数据不一致* 那么 Copy Activity 将重试或者中止。中止时，pipeline 将以失败的形式返回，此时可以

1. 发送邮件通知
2. 定期查看 监控 (monitor) 情况 

当 *允许数据不一致* 时，那么可以监控 

- `@activity('Copy data').output` [^6]
- 日志文件

的数据，通过所得的数据做再做下一步策略。

<p>

*测试* 可通过来回复制进行数据校验进行实现，场景如下: 

1. 备份 数据库-1 至 Azure Blob Storage
2. Azure Blob Storage 将备份数据恢复至 数据库-2
3. 数据库-1 和 数据库-2 的数据进行一一比较。

目的: 数据在传输中是否有不可预料损失和变形。

<p>

*特殊需求*

监控 Copy Activity 的运行时长，当时长过长时，发送监控信息至运维人员。[^6]





#### 延申

- 监控与结果 https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-monitoring?tabs=data-factory









### Lookup Activity

上限5000条数据或者2M

https://docs.microsoft.com/en-us/answers/questions/42083/how-to-process-more-than-5000-records-in-lookup-ac.html





### Azure Data Explorer

TODO



## Azure Data Flows

https://docs.microsoft.com/en-us/azure/data-factory/concepts-data-flow-overview

https://docs.microsoft.com/zh-cn/azure/data-factory/concepts-data-flow-overview

https://www.cnblogs.com/ljhdo/p/14086256.html

### select



### Parse

解析 JSON

https://docs.microsoft.com/en-us/azure/data-factory/data-flow-parse

https://www.sqlservercentral.com/blogs/how-i-tested-parse-data-transformation-in-azure-data-factory

### flatmap

todo

https://www.sqlservercentral.com/blogs/how-i-tested-parse-data-transformation-in-azure-data-factory



### webhook

https://docs.microsoft.com/en-us/azure/azure-monitor/alerts/alerts-log-webhook

https://www.cloudfronts.com/how-to-get-email-notification-when-azure-data-factory-pipeline-fail/

## 监控与警告

通知邮件https://azure.microsoft.com/en-us/blog/create-alerts-to-proactively-monitor-your-data-factory-pipelines/

## 账号信息

- https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/active-directory-how-to-find-tenant





## 参考 References

[^1]: [Azure Data Factory documentation](https://docs.microsoft.com/en-us/azure/data-factory/)
[^2]: [SQL Server Integration Services](https://docs.microsoft.com/zh-cn/sql/integration-services/sql-server-integration-services?view=sql-server-ver15)
[^3]: [Data integration - Wikipedia](https://en.wikipedia.org/wiki/Data_integration)
[^4]:[Data transformation - Wikipedia](https://en.wikipedia.org/wiki/Data_transformation)

[^5]: [Data consistency verification in copy activity - Azure](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-data-consistency)
[^6]: [Monitor copy activity](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-monitoring)

