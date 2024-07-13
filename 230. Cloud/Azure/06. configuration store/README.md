

Azure 使用 Azure App configuration 进行配置中心管理。

- App configuration 的用法有点像 git 仓库，可以直接看到以前版本的配置。
  - 配置是 key-value 形式的。
  - 获取 value 时候，Label 不设置时候，代表使用默认配置。设置时，意味着取特定 label 的 value。
  - 配置可以在 app service 等服务上设置，也可以在代码里主动或者被动拉取代码。
  - app service 只会在某一个属性变化时，才会主动拉取配置。所以建议创建一个时间日期的配置，如，在 App Service 里设置一个叫 "updatedAt" 的配置。
- 在代码里设置 [Feature management](https://learn.microsoft.com/en-us/azure/azure-app-configuration/concept-feature-management)，可以动态打开或者关闭功能。或者让一定比例的用户使用该功能。配置一般是静态的（当然也可以设置成动态），而 feature 是动态的。