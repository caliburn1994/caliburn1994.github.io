

## 1. 介绍 Introduction

Azure data factory (ADF) 是 可横向扩张的 (scale out) 无服务的 (serverless) 的数据整合和迁移的服务。[["]](https://docs.microsoft.com/en-us/azure/data-factory/)

主要包含以下三个方面: 

- 数据集成 (data integration) ：与不同数据源结合的能力。[["]](https://en.wikipedia.org/wiki/Data_integration)
- 数据转换 (data transformation) ：数据从一种格式转换成另一种格式的能力。[["]](https://en.wikipedia.org/wiki/Data_transformation)
- SSIS (SQL Server Integration Services) : 复制或下载文件，加载数据仓库，清除和挖掘数据以及管理 SQL Server 对象和数据。 [["]](https://docs.microsoft.com/zh-cn/sql/integration-services/sql-server-integration-services?view=sql-server-ver15)

## 2. 功能介绍

一般：

- **Execute Pipeline**: 执行管道。通过 monitor 可以看到 pipeline 的输入参数、重新执行 pipeline。在定义 pipeline 时，需要注意这点。
- 数组(上限 100,000) [["]](https://learn.microsoft.com/en-us/azure/data-factory/control-flow-for-each-activity)

  - **[ForEach](https://learn.microsoft.com/en-us/azure/data-factory/control-flow-for-each-activity)**: 循环。并行数上限 50，ForEach 不能内嵌，但可通过内嵌 pipeline 来 workaround。[["]](https://learn.microsoft.com/en-us/fabric/data-factory/data-factory-limitations#data-pipeline-resource-limits)
  - ...
- Get Metadata: 获得文件的元数据。

- Lookup: 通过 dataset 获得数据。输出最大支持 4 MB、5000 行

  - 突破方式: 如果数据源有 index 的话，可以通过循环或者 util 的形式实现。(💡[官方的 workarounds](https://docs.microsoft.com/en-us/azure/data-factory/control-flow-lookup-activity#limitations-and-workarounds) 太模糊，无法参考使用)

- Web: http 操作
- webhook

数据操作：

- Copy Acitivity：数据复制。
- Data Flow：数据复制和操作。比 Copy Activity 复杂。





### 2.1. Copy Acitivity

CosmosDB：

- 建议使用 DB=>Storage=>DB 进行数据迁移（DB=>DB 时常会报错）。
- batch size=1，`Request Size = Single Document Size * Write Batch Size` [["]](https://learn.microsoft.com/en-us/answers/questions/69129/copy-from-cosmosdb-to-cosmosdb-error-34-request-si)，batch size 设置过高，可能会 CosmosDB request 2M 上限错误。
- CosmosDB 单条数据大小上限为 2M，Copy Acitivity 的上限为 1.7M 左右。（不知道原因）
- Data Flow 可以插入 2M 的数据，但会报奇怪的错误。
- 并发量设置越低，使用的吞吐量会越低。[["]](https://learn.microsoft.com/en-us/azure/data-factory/copy-activity-performance-features#parallel-copy)
- DIU：计算力[["]](https://learn.microsoft.com/en-us/azure/data-factory/copy-activity-performance-features#data-integration-units)
- 性能调优：[Performance tuning steps](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance#performance-tuning-steps)
- [数据映射](https://learn.microsoft.com/en-us/azure/data-factory/copy-activity-schema-and-type-mapping) 包含 flatten transformation 等操作。
  - flatten 可以将一条数据内部 data List，扁平成多条数据。
- `validateDataConsistency` 启动后会校验一致性。[["]](https://learn.microsoft.com/en-us/azure/data-factory/copy-activity-data-consistency)





### 2.2. Data flow

Data flow 用于数据转换。

1. Data flow 一般用于对数据库、大文件进行转换，HTTP协议 一般会限制每分钟访问的速率。
2. Data flow 不是用于备份数据，从 Data flow 中导入后，数据可能会有损失（Boolean=>String，integer=>String）

[官网](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-transformation-overview)提供了以下工具进行数据转换。工具以下概念相关

- stream
- MS SQL

| Name                                                         | Category                | Description                                                  |
| :----------------------------------------------------------- | :---------------------- | :----------------------------------------------------------- |
| [Aggregate](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-aggregate) | Schema modifier         | Define different types of aggregations such as SUM, MIN, MAX, and COUNT grouped by existing or computed columns. |
| [Alter row](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-alter-row) | Row modifier            | Set insert, delete, update, and upsert policies on rows.     |
| [Conditional split](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-conditional-split) | Multiple inputs/outputs | Route rows of data to different streams based on matching conditions. |
| [Derived column](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-derived-column) | Schema modifier         | generate new columns or modify existing fields using the data flow expression language. |
| [Exists](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-exists) | Multiple inputs/outputs | Check whether your data exists in another source or stream.  |
| [Filter](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-filter) | Row modifier            | Filter a row based upon a condition.                         |
| [Flatten](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-flatten) | Schema modifier         | Take array values inside hierarchical structures such as JSON and unroll them into individual rows. |
| [Join](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-join) | Multiple inputs/outputs | Combine data from two sources or streams.                    |
| [Lookup](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-lookup) | Multiple inputs/outputs | Reference data from another source.                          |
| [New branch](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-new-branch) | Multiple inputs/outputs | Apply multiple sets of operations and transformations against the same data stream. |
| [Parse](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-new-branch) | Formatter               | Parse text columns in your data stream that are strings of JSON, delimited text, or XML formatted text. |
| [Pivot](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-pivot) | Schema modifier         | An aggregation where one or more grouping columns has its distinct row values transformed into individual columns. |
| [Rank](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-rank) | Schema modifier         | Generate an ordered ranking based upon sort conditions       |
| [Select](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-select) | Schema modifier         | Alias columns and stream names, and drop or reorder columns  |
| [Sink](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-sink) | -                       | A final destination for your data                            |
| [Sort](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-sort) | Row modifier            | Sort incoming rows on the current data stream                |
| [Source](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-source) | -                       | A data source for the data flow                              |
| [Surrogate key](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-surrogate-key) | Schema modifier         | Add an incrementing non-business arbitrary key value         |
| [Union](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-union) | Multiple inputs/outputs | Combine multiple data streams vertically                     |
| [Unpivot](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-unpivot) | Schema modifier         | Pivot columns into row values                                |
| [Window](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-window) | Schema modifier         | Define window-based aggregations of columns in your data streams. |
| [Parse](https://docs.microsoft.com/en-us/azure/data-factory/data-flow-parse) | Schema modifier         | Parse column data to Json or delimited text                  |







## 3. 监控

方式包含：

- alert 通知
- 查看配置所配置日志路径
- 查看 dashboard











## 豆知識

- ADF 有版本区分，因此在 StackOverflow 上搜索，需要注意看标签是否带 v2。ADF 对标 AWS 的是 [AWS Data Pipeline](https://aws.amazon.com/cn/datapipeline)。

- [ADF 反馈网站](https://feedback.azure.com/d365community/forum/1219ec2d-6c26-ec11-b6e6-000d3a4f032c#)

- [ADF 模版文件](https://learn.microsoft.com/en-us/azure/data-factory/solution-templates-introduction)

  





