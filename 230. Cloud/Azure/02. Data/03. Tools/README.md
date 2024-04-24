## 1. 概览

![image-20240424132626001](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240424132628.png)

数据同步有几种方式

- [Data factory](./Azure%20Data%20Factory): 综合性比较好，各种迁移工具都有。可重复执行。

- blob object replication: 只能从 A 复制到 B account，不能在同一个 account 里复制。

  - [Change feed](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-change-feed) 和 [Blob versioning](https://learn.microsoft.com/en-us/azure/storage/blobs/versioning-overview) 都要启动。

  - snapshots 不能被复制。

- AzCopy: 本地命令工具

- Azure Import/Export: 数据装进硬盘里，发送到 Azure 的数据中心或者发过来发送到客户手中。

  

## 2. Azure Import/Export

Azure Import/Export:

- Import: 数据装进硬盘里，发送到 Azure 的数据中心。(支持服务: Azure Blob storage、Azure Files) [[”]](https://learn.microsoft.com/en-us/azure/import-export/storage-import-export-service) 

  所需配置文件：

  - a dataset CSV file: 文件信息
  - a driveset CSV file: 驱动信息
- Export: 从数据中心将数据取出来，存到硬盘发送到客户手上。



## 3. Azcopy

### 3.1. 简述

**AzCopy** 是命令行工具，可从数据源下载到本地，或者从本地上传。 [[”]](https://learn.microsoft.com/en-us/azure/storage/common/storage-use-azcopy-v10#download-azcopy)

- Azure Blob Storage、Azure File
- Azure Table 只支持旧版本 ll **[AzCopy version 7.3](https://aka.ms/downloadazcopynet)** ，新本不支持

| 命令行      | 说明                               |
| ----------- | ---------------------------------- |
| azcopy make | Creates a container or file share. |


### 3.2. QA

**Q: AzCopy 连接 Azure Blob Storage、Azure File 通过什么方式验证？**

A: Microsoft Entra ID 、a Shared Access Signature (SAS) token
