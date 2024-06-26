

## 1. 简介 Introduction

Azure Storage 是一个平台（platform），包含以下服务 [["]](https://docs.microsoft.com/en-us/azure/storage/common/storage-introduction)

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




| Feature                               | Description                                                  | When to use                                                  |
| :------------------------------------ | :----------------------------------------------------------- | :----------------------------------------------------------- |
| **Azure Blobs (Azure Blob storage)**  | 将非结构化数据作为存储对象数据(object data) 进行存储。支持用于数据分析的 [Azure Data Lake Storage Gen2](https://learn.microsoft.com/en-us/azure/storage/blobs/data-lake-storage-introduction) | 存储 web 服务器的图片文件。<br/>Storing files for distributed access.<br/>Streaming video and audio.<br/>Writing to log files.<br/>Storing data for backup and restore, disaster recovery, and archiving.<br/>Storing data for analysis by an on-premises or Azure-hosted service. [["]](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blobs-introduction) |
| **Azure Files**                       | 和外挂网盘、云盘类似。                                       | You want to "lift and shift" an application to the cloud that already uses the native file system APIs to share data between it and other applications running in Azure.  You want to replace or supplement on-premises file servers or NAS devices.  <br />适合放开发工具和 debug 工具，有需要时直接连接入虚拟机。 |
| **Azure NetApp Files**                | Offers a fully managed, highly available, enterprise-grade NAS service that can handle the most demanding, high-performance, low-latency workloads requiring advanced data management capabilities. | You have a difficult-to-migrate workload such as POSIX-compliant Linux and Windows applications, SAP HANA, databases, high-performance compute (HPC) infrastructure and apps, and enterprise web applications.  You require support for multiple file-storage protocols in a single service, including NFSv3, NFSv4.1, and SMB3.1.x, enables a wide range of application lift-and-shift scenarios, with no need for code changes. |
| **Azure Elastic SAN**                 | Azure Elastic SAN is a fully integrated solution that simplifies deploying, scaling, managing, and configuring a SAN, while also offering built-in cloud capabilities like high availability. | You want large scale storage that is interoperable with multiple types of compute resources (such as SQL, MariaDB, Azure virtual machines, and Azure Kubernetes Services) accessed via the [internet Small Computer Systems Interface](https://en.wikipedia.org/wiki/ISCSI) (iSCSI) protocol. |
| **Azure Disks**                       | Allows data to be persistently stored and accessed from an attached virtual hard disk.<br />不能外挂磁盘。和 OS 磁盘类似。 | You want to "lift and shift" applications that use native file system APIs to read and write data to persistent disks.  You want to store data that isn't required to be accessed from outside the virtual machine to which the disk is attached.<br />虚拟机使用的是 Azure managed disk，也算作 Azure Disk |
| **Azure Container Storage** (preview) | Azure Container Storage (preview) is a volume management, deployment, and orchestration service that integrates with Kubernetes and is built natively for containers. | You want to dynamically and automatically provision persistent volumes to store data for stateful applications running on Kubernetes clusters. |
| **Azure Queues**                      | 异步消息队列                                                 | 同类产品还有: Service Bus、Event Grid、Event Hub，参考:[Storage queues and Service Bus queues - compared and contrasted](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-azure-and-service-bus-queues-compared-contrasted)、[Introduction to Azure messaging services - Training](https://learn.microsoft.com/en-us/training/modules/choose-a-messaging-model-in-azure-to-connect-your-services/) |
| **Azure Tables**                      | NoSQL 数据库                                                 | You want to store flexible datasets like user data for web applications, address books, device information, or other types of metadata your service requires.  <br />同类产品有: Azure Cosmos DB，参考 [Developing with Azure Cosmos DB for Table and Azure Table Storage](https://learn.microsoft.com/en-us/azure/cosmos-db/table-support). |







## 2. Storage 类型

| Storage account                                              | Supported services                                           | Recommended usage                                            |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| [**Standard** **general-purpose v2**](https://learn.microsoft.com/en-us/azure/storage/common/storage-account-upgrade) | Blob Storage (including Data Lake Storage), Queue Storage, Table Storage, and Azure Files | Standard storage account for most scenarios, including blobs, file shares, queues, tables, and disks (page blobs). |
| [**Premium** **block blobs**](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-block-blob-premium) | Blob Storage (including Data Lake Storage)                   | Premium storage account for block blobs and append blobs. Recommended for applications with high transaction rates. Use Premium block blobs if you work with smaller objects or require consistently low storage latency. This storage is designed to scale with your applications. |
| [**Premium** **file shares**](https://learn.microsoft.com/en-us/azure/storage/files/storage-how-to-create-file-share) | Azure Files                                                  | Premium storage account for file shares only. Recommended for enterprise or high-performance scale applications. Use Premium file shares if you require support for both Server Message Block (SMB) and NFS file shares. |
| [**Premium** **page blobs**](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-pageblob-overview) | Page blobs only                                              | Premium high-performance storage account for page blobs only. Page blobs are ideal for storing index-based and sparse data structures, such as operating systems, data disks for virtual machines, and databases. |









### 3.5. 数据加密

- 通过设置，可以用 CMK 或 Microsoft-managed key 对文件进行加密 。
- 适用于：container 或 individual blob。



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

### 4.2. 备份

file share 是通过 snapshots 进行备份。
- snapshots 是增量备份
- 可以选择文件进行恢复



## 5. 工具

- [Storage Explorer](https://azure.microsoft.com/en-us/products/storage/storage-explorer): 本地 Storage 浏览工具，可以同时连接多个 Storage。
- local emulators
  - Azure Storage Emulator
  - Azurite

参考: [教程](https://learn.microsoft.com/en-us/training/modules/upload-download-and-manage-data-with-azure-storage-explorer/)

## 6. 安全

- 前端页面的代码里，建议使用 SAS，account key 的权限太高了。[["]](https://learn.microsoft.com/en-us/training/modules/control-access-to-azure-storage-with-sas/3-use-shared-access-signatures)
- SAS token 带上 stored access policy，会更加安全。因为可以通过更改 policy，使 SAS token 过期，否则就只能刷新 account key。如果 token 的寿命比较短，不用 policy 也是可以的。





## FAQ

问：Azure Blobs、Azure Files、Azure Disks 选择哪一个使用。

答:  程序开发时，一般都使用 Azure Blobs，除非有什么特殊需求。

- Azure Files： 需要使用 [Server Message Block (SMB) protocol](https://docs.microsoft.com/en-us/windows/win32/fileio/microsoft-smb-protocol-and-cifs-protocol-overview) or [Network File System (NFS) protocol](https://en.wikipedia.org/wiki/Network_File_System) 等协议时使用。
- Azure Disks： 当需要目录时，如：操作系统需要一个文件系统才能进行运行时使用。



## 延伸阅读 See also

- [Azure updates](https://azure.microsoft.com/en-us/updates/?category=storage)： Azure Storage 最近更新内容
- [Azure Blob Storage documentation](https://azure.microsoft.com/en-us/updates/?category=storage)： Azure Blob Storage is Microsoft's object storage solution for the cloud. Blob storage is optimized for storing massive amounts of unstructured data.

