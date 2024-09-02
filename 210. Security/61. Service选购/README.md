## compute

- 数据备份：增量备份 > 全量备份
- 多地冗余
- 自动伸缩
- 蓝绿部署等不停机部署
- 健康检查 probe，以及自动重启







## 文件存储

专门的文件服务优先。如：storage。一般专门的 storage 服务会包含所有所需的功能：

- 数据恢复、数据复制。
- 数据软删除、数据版本、数据上锁、数据法定保护
- token 访问数据 [["]](https://learn.microsoft.com/en-us/azure/storage/common/storage-sas-overview)
- 局域网的支持
- 数据容量上限很高

高级的有：

- 防火墙、安全卫士等。[["]](https://learn.microsoft.com/en-us/azure/defender-for-cloud/defender-for-storage-introduction)
- 数据的自动管理
  - 自动删除
  - 自动转存到低费用的媒体





