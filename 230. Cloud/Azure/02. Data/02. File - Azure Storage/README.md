

## 1. 简介 Introduction

当我们谈及 Azure Storage 时，是在谈及 Azure 的几乎所有的存储方案。Azure Storage 是一个平台（platform）。[["]](https://docs.microsoft.com/en-us/azure/storage/common/storage-introduction)

Azure Storage 核心服务有：[["]](https://docs.microsoft.com/en-us/azure/storage/common/storage-introduction)

- [Azure Blobs](https://docs.microsoft.com/en-us/azure/storage/blobs/storage-blobs-introduction)(Azure Blob storage): 存储对象数据(object data)，非结构化数据。
- [Azure Files](https://docs.microsoft.com/en-us/azure/storage/files/storage-files-introduction): Azure File 是 directory object。通过 file shares，若干个虚拟机可以访问该 directory。就像访问外挂的磁盘一样。适合放开发工具和 debug 工具，有需要时直接连接入虚拟机。
- [Azure Queues](https://docs.microsoft.com/en-us/azure/storage/queues/storage-queues-introduction): A messaging store for reliable messaging between application components.
- [Azure Tables](https://docs.microsoft.com/en-us/azure/storage/tables/table-storage-overview): A NoSQL store for schemaless storage of structured data.
- [Azure Disks](https://docs.microsoft.com/en-us/azure/virtual-machines/managed-disks-overview): 适合装操作系统和虚拟机磁盘。它是 Page block。

除此以外还有：[["]](https://azure.microsoft.com/en-us/product-categories/storage/)

- [Azure NetApp Files](https://azure.microsoft.com/en-us/services/netapp/)
- [Data Box](https://azure.microsoft.com/en-us/services/databox/)
- [Microsoft Azure Confidential Ledger](https://azure.microsoft.com/en-us/services/azure-confidential-ledger/)
- [ Azure Data Lake Storage](https://azure.microsoft.com/en-us/services/storage/data-lake-storage/)

参考网站:

- [Configure Azure Files and Azure File Sync - Training](https://learn.microsoft.com/en-us/training/modules/configure-azure-files-file-sync/2-compare-files-to-blobs)





## 2. Storage Account

| Storage account                                              | Supported services                                           | Recommended usage                                            |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| [**Standard** **general-purpose v2**](https://learn.microsoft.com/en-us/azure/storage/common/storage-account-upgrade) | Blob Storage (including Data Lake Storage), Queue Storage, Table Storage, and Azure Files | Standard storage account for most scenarios, including blobs, file shares, queues, tables, and disks (page blobs). |
| [**Premium** **block blobs**](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-block-blob-premium) | Blob Storage (including Data Lake Storage)                   | Premium storage account for block blobs and append blobs. Recommended for applications with high transaction rates. Use Premium block blobs if you work with smaller objects or require consistently low storage latency. This storage is designed to scale with your applications. |
| [**Premium** **file shares**](https://learn.microsoft.com/en-us/azure/storage/files/storage-how-to-create-file-share) | Azure Files                                                  | Premium storage account for file shares only. Recommended for enterprise or high-performance scale applications. Use Premium file shares if you require support for both Server Message Block (SMB) and NFS file shares. |
| [**Premium** **page blobs**](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-pageblob-overview) | Page blobs only                                              | Premium high-performance storage account for page blobs only. Page blobs are ideal for storing index-based and sparse data structures, such as operating systems, data disks for virtual machines, and databases. |





## 3. Azure Blob Storage

![Diagram that shows the Azure Blob Storage architecture.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240424112517.png)

Blob Storage 有三个层次：account、container、blob

### 3.1. container

container 有三个访问等级：

- **Private**: 不公开
- **Blob**: Blob 等级公开
- **Container**: Container 等级公开

### 3.2. blob

blob 有以下 access tier，不同 access tier 的访问速度以及费用不同: [["]](https://learn.microsoft.com/en-us/training/modules/configure-blob-storage/4-create-blob-access-tiers)

| Comparison                       | Hot access tier | Cool access tier | Cold access tier | Archive access tier |
| :------------------------------- | :-------------- | :--------------- | :--------------- | :------------------ |
| **Availability**                 | 99.9%           | 99%              | 99%              | 99%                 |
| **Availability (RA-GRS reads)**  | 99.99%          | 99.9%            | 99.9%            | 99.9%               |
| **Latency (time to first byte)** | milliseconds    | milliseconds     | milliseconds     | hours               |
| **Minimum storage duration**     | N/A             | 30 days          | 90 days          | 180 days            |

Minimum storage duration: 这是一个与定价策略相关的概念，其意味着当你将数据移动到某个存储层级后，即使在这个最短期限之前删除或转移数据，你也需要为这段时间的存储付费。

blob 的类型: 

- **Block blobs**. A block blob consists of blocks of data that are assembled to make a blob. Most Blob Storage scenarios use block blobs. Block blobs are ideal for storing text and binary data in the cloud, like files, images, and videos.
- **Append blobs**. An append blob is similar to a block blob because the append blob also consists of blocks of data. The blocks of data in an append blob are optimized for *append* operations. Append blobs are useful for logging scenarios, where the amount of data can increase as the logging operation continues.
- **Page blobs**. A page blob can be up to 8 TB in size. Page blobs are more efficient for frequent read/write operations. Azure Virtual Machines uses page blobs for operating system disks and data disks.





### 3.3. 认证

| Authorization strategy                       | Description                                                  |
| :------------------------------------------- | :----------------------------------------------------------- |
| **Microsoft Entra ID**                       | Microsoft Entra ID is Microsoft's cloud-based identity and access management service. With Microsoft Entra ID, you can assign fine-grained access to users, groups, or applications by using role-based access control. |
| **Shared Key**                               | Shared Key authorization relies on your Azure storage account access keys and other parameters to produce an encrypted signature string. The string is passed on the request in the Authorization header. |
| **Shared access signatures**                 | A SAS delegates access to a particular resource in your Azure storage account with specified permissions and for a specified time interval. |
| **Anonymous access to containers and blobs** | You can optionally make blob resources public at the container or blob level. A public container or blob is accessible to any user for anonymous read access. Read requests to public containers and blobs don't require authorization. |

SAS 有三种 [["]](https://learn.microsoft.com/en-us/training/modules/configure-storage-security/3-create-shared-access-signatures)

- User delegation SAS: 

  - 由 Microsoft Entra credentials 创建该 SAS 的。
  - 只适用于 Blob
  - 微软推荐，安全度最高。

- Service SAS: 

  - 由 Shared Key 创建的，delegates access to a resource in **only one** Azure Storage service.
  - 可通过 store access policy 来控制 SAS。不使用 policy 最长只能维持一小时

- Account SAS:

  - 由 Shared Key 创建的，delegates access to resources in **one or more** Azure Storage services.

  - 可通过 access policy 来控制 SAS。

    

## 4. Azure File

[Azure Files](https://docs.microsoft.com/en-us/azure/storage/files/storage-files-introduction) 是 directory object。通过 file shares，若干个虚拟机可以访问该 directory。就像访问外挂的磁盘一样。适合放开发工具和 debug 工具，有需要时直接连接入虚拟机。

### 4.1. Azure File Sync

![image-20240425005032955](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240425005037.png)

用途：集中管理各个服务器的文件

步骤：

1. [Azure] 创建 Azure Storage File Share。

2. [Azure] 在 Azure 上搜索 **Azure File Sync** 并创建资源。

   创建出来的资源叫 **Storage Sync Service**。

3. [Server] Prepare Windows Server to use with Azure File Sync

   1. 调整服务器的安全策略：

   2. 安装agent： Install the Azure File Sync agent

   3. **注册：**Register Windows Server with Storage Sync Service

      一台服务器只能注册一个 Storage Sync Service

4. [Azure] Create a sync group and Create a cloud endpoint

   定义文件同步的规则，指定哪些哪些文件要同步。

   - cloud endpoint 和 server endpoint 之间的关联依靠的是 sync group
   - cloud endpoint 是 (1) 创建的 file share。
   - 一个 sync group 只能有一个 cloud endpoint

5. [Azure] Create a server endpoint：指定哪些本地文件，要和云同步

**Cloud tiering**

Azure File Sync 启动 Cloud tiering 后，频繁访问的文件会保留在本地，而其他则会存在云上。

**考点:**

- Azure file share 和 Azure File Sync 要在同一个地区。

### 4.2. 备份

file share 是通过 snapshots 进行备份。

- snapshots 是增量备份
- 可以选择文件进行恢复





## FAQ

问：Azure Blobs、Azure Files、Azure Disks 选择哪一个使用。

答:  程序开发时，一般都使用 Azure Blobs，除非有什么特殊需求。

- Azure Files： 需要使用 [Server Message Block (SMB) protocol](https://docs.microsoft.com/en-us/windows/win32/fileio/microsoft-smb-protocol-and-cifs-protocol-overview) or [Network File System (NFS) protocol](https://en.wikipedia.org/wiki/Network_File_System) 等协议时使用。
- Azure Disks： 当需要目录时，如：操作系统需要一个文件系统才能进行运行时使用。



## 延伸阅读 See also

- [Azure updates](https://azure.microsoft.com/en-us/updates/?category=storage)： Azure Storage 最近更新内容
- [Azure Blob Storage documentation](https://azure.microsoft.com/en-us/updates/?category=storage)： Azure Blob Storage is Microsoft's object storage solution for the cloud. Blob storage is optimized for storing massive amounts of unstructured data.

