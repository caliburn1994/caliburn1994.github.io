## public load balancer

Distribution modes 有两种

- **Five-tuple hash**: 随机分配
- **Source IP affinity** (session affinity or client IP affinity) : 根据 session 发送请求到同一台机器