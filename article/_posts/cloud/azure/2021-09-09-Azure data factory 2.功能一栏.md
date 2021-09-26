---
layout: post 
title: Azure Data Factory 2.功能一栏
tags: Cloud
comments: 1 
excerpt: Azure Data Factory - 2.功能一栏
typora-root-url: ..\..\..\..
typora-copy-images-to: ..\..\..\..\assets\blog_res\azure
---

## 数据工厂限制🚫

数据工厂是多租户服务 (multitenant service)[^9] ，因此具有上限。具体参考[官网](https://docs.microsoft.com/en-US/azure/azure-resource-manager/management/azure-subscription-service-limits#data-factory-limits)。下面举一些例子

- ForEach 并行数 ≤ 50

- linked service ≤ 3000
  
  当超过上限时，将会抛出以下类似的错误异常
  
  >There are substantial concurrent copy activity executions which is causing failures due to throttling under subscription xxxx, region jpe and limitation 3000. Please reduce the concurrent executions. For limits, refer
  
  经过实验，可以同时启动 1500 个 Copy Activity，*也许*是因为每一个 Copy Activity 有 2 个 Linked Service。

## Copy Activity

### 概念

source: 数据源

[sink](https://en.wikipedia.org/wiki/Sink_(computing)): 接收器 (原意: 水槽，洗碗槽)

Hierarchical  分层：JSON、XML、NoSQL

tabular : 表格（excel、关系数据库）

### 性能

概念📙

DIU (Data Integration Unit) [^1]这是 Azure云 特有的概念，介绍的文档比较少且模糊不清，笔者认为应解释为 "单位时间内，CPU、内存、网络资源分配等消耗的时间"

策略♞

-  [For Each ](https://docs.microsoft.com/en-us/azure/data-factory/control-flow-for-each-activity) 拆分需要拷贝的数据，并行执行。
-  Copy Activity 的性能
  ![监视复制活动运行详细信息](/assets/blog_res/azure/monitor-copy-activity-run-details.png)
  1. Azure 提供了[性能优化 (performance tuning) 提示](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance-troubleshooting)功能
     - [并行数的调优](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance-features#parallel-copy)
     - [颗粒大小的调优](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance-features#data-integration-units)
  2. **Duration** 的内容常为优化的对象。[^3]
  3. [暂存 (staging) 功能](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance-features#staged-copy)  (Specify whether to copy data via an interim staging store. Enable staging only for the beneficial scenarios, e.g. load data into Azure Synapse Analytics via PolyBase, load data to/from Snowflake, load data from Amazon Redshift via UNLOAD or from HDFS via DistCp, etc.[Learn more](https://go.microsoft.com/fwlink/?linkid=2159335))

测试步骤🧪[^7]

1. 选择大数据
2. 输入 [Data Integration Units (DIU)](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance#data-integration-units) 和 [parallel copy](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance#parallel-copy)，并不断调试，最终获取最优数值
3. 拆分需要拷贝的数据，并聚合结果。以下是官方的模板: 
   - [Copy files from multiple containers](https://docs.microsoft.com/en-us/azure/data-factory/solution-template-copy-files-multiple-containers)
   - [Migrate data from Amazon S3 to ADLS Gen2](https://docs.microsoft.com/en-us/azure/data-factory/solution-template-migration-s3-azure)
   - [Bulk copy with a control table](https://docs.microsoft.com/en-us/azure/data-factory/solution-template-bulk-copy-with-control-table)



### Schema映射  Schema Mapping

Copy Activity 有一系列默认的映射策略。而配置显式映射 (Explicit mapping) 时，需加注意，不同的 source-sink 组合配置的方式是不同。[^2]

![从表格映射到表格](/assets/blog_res/azure/map-tabular-to-tabular.png)

Mapping 支持 *Fatten* 操作，可以讲一个 array 扁平化。这方便 JSON 转换成 table

![使用 UI 从分层映射到表格](/assets/blog_res/azure/map-hierarchical-to-tabular-ui.png)







### 数据一致性验证 Data consistency verification

Copy Activity 提供了数据<div class='sup' data-title="以确保数据不仅成功地从源存储复制到目标存储，而且验证了源存储和目标存储之间的一致性。">一致性验证</div>。通过 `validateDataConsistency` 启动该校验。[^5]

校验的*对象*以及*策略*♘

- 二进制对象：file size, lastModifiedDate, MD5 checksum 
- 表格数据（tabular data）：` 读取的行数 = 复制的行数 + 跳过的行数`

*什么时候发生？*📅[^4]

- 主键重复
- 作为 source 的二进制文件不能访问、被删除

当数据发生 *不一致性*⚠️时，可以通过 `dataInconsistency` 设置行为

- 中止
- 跳跃

在设定 `logSettings` 和 `path` 可以记录 *不一致* 时候的日志。

### 监控·容错·测试 Monitor·Fault tolerance·Test

💿数据不一致

当 *不允许数据不一致* 那么 Copy Activity 将重试或者中止。中止时，pipeline 将以失败的形式返回，此时可以

1. 发送邮件通知
2. 定期查看 监控 (monitor) 情况 

当 *允许数据不一致* 时，可以监控以下数据，并根据所得数据进行下一步策略下一步策略。[^4]

- activity结果 (`@activity('Copy data').output`) [^6]
- 日志文件

📏测试

可通过来回复制进行数据校验进行实现，示例如下: 

1. 备份 数据库-1 至 Azure Blob Storage
2. Azure Blob Storage 将备份数据恢复至 数据库-2
3. 数据库-1 和 数据库-2 的数据进行一一比较。

目的: 数据在传输中是否有不可预料损失和变形。

📝特殊需求

监控 Copy Activity 的运行时长，当时长过长时，发送监控信息至运维人员。[^6]

### 其他

- 压缩功能



## Data flow

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





## 控制流 Control Flow

- **Execute Pipeline**: 执行管道。通过 monitor 可以看到 pipeline 的输入参数、重新执行 pipeline。在定义 pipeline 时，需要注意这点。

- 数组(上限 100,000) [^8]

  - **Append Variable**: 追加变量到数组里。
  - **Filter**: 过滤数组
  - **ForEach**: 循环数组。
    - 最大并行为 50，默认为 20，如需扩展则要多重 ForEach (Execute Pipeline + ForEach 的方式)。
    - 测试结果显示，设置最大并行数设置过高时，是按照最低数来执行。(💡那为何不全自动化呢？)
    - ForEach 的限制很多。
  - **Until**

- 输入

  - **Get Metadata**: 获得文件的元数据。元数据不得超过 4 MB

  - **Lookup**: 通过 dataset 获得数据。

    - 输出最大支持 4 MB，如果大小超过此限制，活动将失败。

    - 最多可以返回 5000 行；如果结果集包含的记录超过此范围，将返回前 5000 行。

      - 突破方式: 如果数据源有 index 的话，可以通过循环或者 util 的形式实现。

        (💡[官方的 workarounds](https://docs.microsoft.com/en-us/azure/data-factory/control-flow-lookup-activity#limitations-and-workarounds) 太模糊，无法参考使用)
    
  - **Web**:  

- 输出

  - **Web**: 可以发送各种数据。另外还可以将 datasets 和 linkedServices 发送出去。
  - **webhook**

- 条件语句

  - **If Condition**: if 语句
  - **Switch** 
  - **Validation**: 等待文件。当文件或文件夹存在时，才能继续下一步。
  - **wait**: 等待一段时间后再执行下一步。

- **Set Variable**: 设置变量

## Delete Activity 

Delete Activity 仅仅用于删除文件。如需定时删除文件，则要与 schedule trigger 一起使用。



## 外部服务

### Databricks

Azure Databricks 基于 Apache Spark 的快速、简单、协作分析平台

### Azure Data Explorer

数据分析



## 参考 References

[^1]: [Data Integration Units](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance-features#data-integration-units)
[^2]: [Schema and data type mapping in copy activity - Microsoft Docs](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-schema-and-type-mapping)

[^3]:[Troubleshoot copy activity on Azure IR](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance-troubleshooting#troubleshoot-copy-activity-on-azure-ir)
[^4]: [Fault tolerance](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-fault-tolerance)
[^5]: [Data consistency verification in copy activity - Azure](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-data-consistency)
[^6]: [Monitor copy activity](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-monitoring)
[^7]: [Performance tuning steps](https://docs.microsoft.com/en-us/azure/data-factory/copy-activity-performance#performance-tuning-steps)
[^8]: [ForEach Activity](https://docs.microsoft.com/en-us/azure/data-factory/control-flow-for-each-activity)
[^9]: [Data Factory limits](https://docs.microsoft.com/en-US/azure/azure-resource-manager/management/azure-subscription-service-limits#data-factory-limits)