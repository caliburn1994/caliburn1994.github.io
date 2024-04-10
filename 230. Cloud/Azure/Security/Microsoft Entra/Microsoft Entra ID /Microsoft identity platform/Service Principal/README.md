## 1. 概览

**[App Registration](https://learn.microsoft.com/en-us/entra/identity-platform/app-objects-and-service-principals?tabs=browser)** 包含三个部分： **application registration**, **application objects**, **service principals**。

1. 当在 Microsoft Entra ID 完成了 Application 的注册，你将获得一个 instance (application objects) 和 service principal object。
2. 将 secrets 或 certificates 添加到代码里，程序通过该凭证就可以访问该 app。
3. 通过授权权限给 service principal object，app 就有了对应的权限，也就是程序获得了对应的操作权限。

![Authenticate .NET apps to Azure services during local development using service  principals - .NET | Microsoft Learn](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/image-202404212207.png)

## 2. Application object

用面向对象的方式来说，Application object 是类，而位于各个 tenant 的 Service principal object 是对象。通过该对象，App 可以访问各个 tenant 的资源。

而 Application object 管理的是程序访问该 App 相关的事项。如：访问凭证、权限等。[["]](https://learn.microsoft.com/en-us/entra/identity-platform/app-objects-and-service-principals?tabs=browser#application-object)



## 3. Service principal object

**Service Principal (服务主体)**  访问 tenant 里的 Azure 资源。

根据[官网](https://learn.microsoft.com/en-us/entra/identity-platform/app-objects-and-service-principals?tabs=browser#application-object)所示，SP 分为三种：**Application**、**Managed identity**、**Legacy**。

![image-20240413214800528](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/image-20240413214800528.png)

从上图，就能找到这些不同类型的 SP。

- **Enterprise Applications**：是你所创建的 App
- **Microsoft Applications**：微软官方的 App
- **Managed Identities**： 又叫做 **Managed identity service principals**，也就是 Managed identity 也是 SP 的一种类型。这种类型的 SP，是无法使用 secret 或 certification 去登录的。



## 场景

- 通过 App Registration 可以进行 SSO 或者 SAML 登录。也就是说，可以让支持 SAML 的应用的网站，都通过Azure 进行登录。从而减少因密码泄露所导致的数据泄漏。

- 外部应用可以通过 Service Principal 使用 Azure 的所有服务。



