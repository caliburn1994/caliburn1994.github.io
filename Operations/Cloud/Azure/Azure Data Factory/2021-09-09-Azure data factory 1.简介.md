---
layout: post 
title: Azure Data Factory 1.介绍
tags: Cloud
comments: 1 
excerpt: Azure Data Factory的笔记
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



## 延伸阅读 See also

- [ADF 反馈网站](https://feedback.azure.com/d365community/forum/1219ec2d-6c26-ec11-b6e6-000d3a4f032c#)





[^1]: [Azure Data Factory documentation](https://docs.microsoft.com/en-us/azure/data-factory/)
[^2]: [SQL Server Integration Services](https://docs.microsoft.com/zh-cn/sql/integration-services/sql-server-integration-services?view=sql-server-ver15)
[^3]: [Data integration - Wikipedia](https://en.wikipedia.org/wiki/Data_integration)
[^4]:[Data transformation - Wikipedia](https://en.wikipedia.org/wiki/Data_transformation)

