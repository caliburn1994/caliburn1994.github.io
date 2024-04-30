## Deployment slots

deployment slots 类似于蓝绿发布。slot 一般存储的是 stage 版本的 app 或者 release 后被切换下来的 app。也就是说，可以在 production 的 app service 里启动 2 个 slot。但不建议将 Test、Dev 作为 slot 加进去，因为 slot 和 app 是共享资源的，也就是说当 dev 的 slot 因为内存而挂掉，Production 很有可能受影响。

注意点：

- 哪些配置会跟随 swap 而交换: [["]](https://learn.microsoft.com/en-us/training/modules/configure-azure-app-services/6-add-deployment-slots)

  ![image-20240501081721180](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501081724.png)



## 认证

App Service 提供了方便方法，让开发者使用 Google、Twitter、Microsoft 等提供商作为用户登录认证。[["]](https://learn.microsoft.com/en-us/azure/app-service/overview-authentication-authorization#identity-providers) 几乎不用编写任何代码，可以参考网上的 [例子](https://qiita.com/aktsmm/items/c962be02fe0bd0517999)。

但需要注意的是，这些只支持 single container，不支持 docker compose 这种用法。



## App Service Plan

### 概览

App Service Plan 相当于物理机，Azure App Service 相当于容器或者 container。

设计考虑点：[["]](https://learn.microsoft.com/en-us/training/modules/configure-app-service-plans/2-implement-azure)

- 多 App:  多个 App 放在同一个 Plan，省钱省事。
- 单 App: 
  - 多地防灾，需要部署到不同 region 上。
  - resource-intensive 的 App ，与其他 App 放在一起，可能会影响其他 App。
  - Scale 考虑: 不希望 scale 所有 App，只想 Scale 特定的 App

### Plan 类型



| Feature                          | Free                 | Shared               | Basic                          | Standard             | Premium                     | Isolated                              |
| :------------------------------- | :------------------- | :------------------- | :----------------------------- | :------------------- | :-------------------------- | :------------------------------------ |
| Usage                            | Development, Testing | Development, Testing | Dedicated development, Testing | Production workloads | Enhanced scale, performance | High performance, security, isolation |
| Web, mobile, or API applications | 10                   | 100                  | Unlimited                      | Unlimited            | Unlimited                   | Unlimited                             |
| Disk space                       | 1 GB                 | 1 GB                 | 10 GB                          | 50 GB                | 250 GB                      | 1 TB                                  |
| Auto scale                       | n/a                  | n/a                  | n/a                            | Supported            | Supported                   | Supported                             |
| Deployment slots                 | n/a                  | n/a                  | n/a                            | 5                    | 20                          | 20                                    |
| Max instances                    | n/a                  | n/a                  | Up to 3                        | Up to 10             | Up to 30                    | Up to 100                             |

Plan 分类如上所示。[["]](https://learn.microsoft.com/en-us/training/modules/configure-app-service-plans/3-determine-plan-pricing)

- Isolated: 又叫做 Azure App Service Environment，虚拟网络和 App Service 的结合。
  - 性能优越: 最大可以 100 个实例。(普通是 30 个)
  - 专用的环境以及比 Standard 高的性能

