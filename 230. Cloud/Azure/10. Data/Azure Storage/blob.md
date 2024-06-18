## 1. 概况

![Diagram that shows the Azure Blob Storage architecture.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240424112517.png)

Blob Storage 有三个层次：account、container、blob。container 是数据存储的基本单位，可比作 windows 里的数据盘（如：D 盘）。blob 则是具体的文件。



## 2. 数据访问

container 有三种 Anoymous access level（匿名访问等级），该设置会作为默认设置应用于 container 下的每一个 blob。访问等级如下：

- **Private**: 不公开
- **Blob**: Blob 等级公开
- **Container**: Container 等级公开

通过 SAS token，非公开的 blob 也能在 internet 被访问。

### 2.1. 认证

| Authorization strategy                       | Description[["]](https://learn.microsoft.com/en-us/training/modules/configure-storage-security/2-review-strategies) |
| :------------------------------------------- | :----------------------------------------------------------- |
| **Microsoft Entra ID**                       | Microsoft Entra ID is Microsoft's cloud-based identity and access management service. With Microsoft Entra ID, you can assign fine-grained access to users, groups, or applications by using role-based access control. |
| **Shared Key**                               | Shared Key authorization relies on your Azure storage account access keys and other parameters to produce an encrypted signature string. The string is passed on the request in the Authorization header. |
| **Shared access signatures**                 | A SAS delegates access to a particular resource in your Azure storage account with specified permissions and for a specified time interval. |
| **Anonymous access to containers and blobs** | You can optionally make blob resources public at the container or blob level. A public container or blob is accessible to any user for anonymous read access. Read requests to public containers and blobs don't require authorization. |


访问 storage 资源有以下方式

- Microsoft Entra ID 身份访问
- storage account key 访问
- SAS
- 匿名访问：



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





## 3. 数据存储

### 3.1. Access tier(container 性能)

不同 access tier 的访问速度和费用都是不同: [["]](https://learn.microsoft.com/en-us/training/modules/configure-blob-storage/4-create-blob-access-tiers)

| Comparison                       | Hot access tier | Cool access tier | Cold access tier | Archive access tier |
| :------------------------------- | :-------------- | :--------------- | :--------------- | :------------------ |
| **Availability**                 | 99.9%           | 99%              | 99%              | 99%                 |
| **Availability (RA-GRS reads)**  | 99.99%          | 99.9%            | 99.9%            | 99.9%               |
| **Latency (time to first byte)** | milliseconds    | milliseconds     | milliseconds     | hours               |
| **Minimum storage duration**     | N/A             | 30 days          | 90 days          | 180 days            |

Minimum storage duration: 这是一个与定价策略相关的概念，其意味着当你将数据移动到某个存储层级后，即使在这个最短期限之前删除或转移数据，你也需要为这段时间的存储付费。

access tier 的前三种类型叫做 online tier，可以直接读写，而 archive tier 则不可以。archive 和压缩包类似，不能直接读写，rehydrate 到 online tier 需要 15 个小时。[["]](https://learn.microsoft.com/en-us/azure/storage/blobs/access-tiers-overview#archive-access-tier)

### 3.2. 数据类型

- **Block blobs**. A block blob consists of blocks of data that are assembled to make a blob. Most Blob Storage scenarios use block blobs. Block blobs are ideal for storing text and binary data in the cloud, like files, images, and videos.
- **Append blobs**. An append blob is similar to a block blob because the append blob also consists of blocks of data. The blocks of data in an append blob are optimized for *append* operations. Append blobs are useful for logging scenarios, where the amount of data can increase as the **logging** operation continues.
- **Page blobs**. A page blob can be up to 8 TB in size. Page blobs are more efficient for frequent read/write operations. Azure Virtual Machines uses page blobs for **operating system disks and data disks**.



### 3.3. 生命周期

通过设置 lifecycle management policy ，可以将根据条件 (conditions) 不需要的 blob 定期删除，或者压缩到比较低的 access tier 用于节省费用。

conditions 有三种设置：[["]](https://learn.microsoft.com/en-us/azure/storage/blobs/lifecycle-management-policy-configure?tabs=azure-portal)
- The number of days since the blob was created.
- The number of days since the blob was last modified.
- The number of days since the blob was last accessed. To use this condition in an action, you should first [optionally enable last access time tracking](https://learn.microsoft.com/en-us/azure/storage/blobs/lifecycle-management-policy-configure?tabs=azure-portal#optionally-enable-access-time-tracking).


## 4. 数据保护

数据更改，参考：[here](https://www.notion.so/be01d04342ab407aa7cac874d799cc4c?pvs=21)

数据保护有以下场景:

- 防止误操作: 可以恢复变更和撤销删除。 比如：程序出现 bug，将所有临时文件删除。如果有回滚功能就可以减少损害。
- 确保完整性: 数据不可删除、不可变更。 就像 SSL 通信，传输的数据不可被篡改。有些文件也是要确保数据不可篡改。日志文件等文件一旦能被篡改，就不能确保数据的正确性，也就不能具有法律作用。
- 法律法规: 日志、法律文件需要禁止篡改，在一些场景，这些文件具有法律意义。

措施:

- **immutable storage（**防止篡改与删除）[["]](https://learn.microsoft.com/zh-cn/azure/storage/blobs/immutable-storage-overview)

  - 定期：**[Time-based retention policies](https://learn.microsoft.com/en-us/azure/storage/blobs/immutable-time-based-retention-policy-overview)** 在一定时间内，不允许删除、篡改

  - 手动：**[Legal holds](https://learn.microsoft.com/en-us/azure/storage/blobs/immutable-legal-hold-overview)** 

    在解除 Legal hold 之前，不允许删除、篡改

  ![image-20240501164135598](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501164141.png)

- 设置灾备

- 设置软删除

- RABC 授权，减少开发者误操作

- Storage 密钥管理，放置到 Key vault 里，减少泄漏风险

- SAS Token 加上 policy

- 修改存储帐户访问策略，如限制 IP 等。

- 程序通过确认 Blob 内容的哈希，从而防止篡改。

- ...





## 5. 数据复制

### 5.1. blob object replication

只能从 A 复制到 B Storage Account，不能在同一个 account 内部复制。

- 📖考点：源 account 需启动 [Change feed](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-change-feed) 和 [Blob versioning](https://learn.microsoft.com/en-us/azure/storage/blobs/versioning-overview)。目标 account 需要开启 [Blob versioning](https://learn.microsoft.com/en-us/azure/storage/blobs/versioning-overview)。

- snapshots 不能被复制。