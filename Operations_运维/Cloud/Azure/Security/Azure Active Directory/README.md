---
Last Modified: 2022-09-20
---



## Azure Active Directory

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/image-20221002181102892.png" alt="image-20221002181102892" width="240" />

**Azure Active Directory**（简称：Azure AD）是隶属 [Microsoft Entra](https://aka.ms/MicrosoftEntra)，提供了以下功能以用于抵御网络安全攻击。[^4]

- 单点登录 single sign-on
- 多要素认证 multifactor authentication
- 条件访问 conditional access



### A1. tenant

**Azure AD tenant** 当我们注册了微软云服务时，就会获得一个 Azure AD service instance，这样的一个 instance 就叫做一个 tenant。通常一个 tenant 代表一个组织，因此 tenant 也是独一无二的。[^5]

- **tenant** 中文意思为借主，此处可以理解为，我们要借微软云服务的资源，因此我们是借主。



### A2. Active Directory

**Active Directory** 实质上是一个 directory 服务，而 directory 服务实质上是一个 NoSQL 数据库。该服务可以存储各种各样的信息，常见包含：用户信息、域名信息、证书相关信息等等。Azure AD 则存储的是用户相关的信息，用户登录 Azure AD 时， Azure AD 会进行校验。如果校验成功，则会跳转到对应的资源上。

通过 Azure AD，让我们能登录 Azure Cloud。如果稍加配置，还可以让 Azure用户 登录其他网站。这里分享两个网站作为扩展阅读：

- [Tutorial: Sign in users and call the Microsoft Graph API from a React single-page app (SPA) using auth code flow](https://docs.microsoft.com/en-us/azure/active-directory/develop/tutorial-v2-react)
- [Authentication with Azure AD and React](https://adrianhynes.medium.com/authentication-with-azure-ad-and-react-ced9a829e083)

回到 AD 的主题上。在 Windows 2000 的时候，微软引入了 Active Directory Domain Services 管理本地（on-premises）架构里的用户。而 Azure AD 则是 Active Directory Domain Services 云化，变成一种服务。Azure 称之为 Identity as a Service (IDaaS) 。[^6]

<br>

## Subscription

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/image-20221002182106180.png" alt="image-20221002182106180"  width="240"/>

Azure 把 Subscription 翻译为订阅。但，如果我们仔细去看 [字典](https://dictionary.cambridge.org/dictionary/english/subscription) 的解释便可以理解 Subscription 是加入或使用某个组织的服务，所需要缴纳的定期费用。在中文语境下常见的有：**订阅费**、月度会员费、年度会员费、月度报纸费。

Azure 也是将其描述为订阅费。[^8]



<br>

## License

License 可以是描述软件的，也可以描述的是人的。Azure 中提到的 License 一般是指人的，因为软件的 License 费用一般都内嵌到服务里。[^8]

TODO

- [ ] Azure AD Premium P1 vs. P2



<br>

## 管理层次 Management levels

当我们使用 开发环境对应一个 subscription，生产环境对应一个 subscription 的这种模式，我们就必须考略到每一个人都能访问到哪个 subscription 。而 management group 是一种工具方便我们管理访问的。

配合 management group，我们可以对若干个 subscription 开启 [Azure Policy](https://learn.microsoft.com/en-us/azure/governance/policy/overview) 对组织内的资源进行规范。

每一个 tenant 都有一个 top-level management group 叫 做 **root management group**、在这下面，可以分更多的 management group。更多详情查看 [官网](https://learn.microsoft.com/en-us/azure/governance/management-groups/overview)



![](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/202209241812584.png)





### M1. 开发产品环境分离 Separate Dev and Production 

开发产品环境分离，有两种方案：

- 使用 subscription 去分离
- 使用 tenant 去分离

两种方案，各有优缺点吧，**Azure 推荐使用 subscription 去分离**。[^7][^9]

![Diagram showing the configuration of DevTest and DevOps for a PaaS application.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/dev-test-paas.png)

原因：[^9]

- 增加费用：
  - Azure AD licensing 费用。

- 复杂度和偏差：
  - RBAC
  - 各种配置：使用多 tenant，则很多配置需要配置两份。由于要配置两份，有时候会有一些偏差。
  - Azure 服务： 很多 Azure 服务也不支持跨 tenant









<br>


## 参考 References

[^4]: [Azure Active Directory](https://azure.microsoft.com/en-us/services/active-directory/)
[^5]: [Create an Azure Active Directory tenant to use with Power BI](https://learn.microsoft.com/en-us/power-bi/developer/embedded/create-an-azure-active-directory-tenant#:~:text=An Azure AD tenant is,from other Azure AD tenants.)
[^6]: [Compare Active Directory to Azure Active Directory](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/active-directory-compare-azure-ad-to-ad)
[^7]: [DevTest and DevOps for PaaS solutions - Azure Solution Ideas](https://learn.microsoft.com/en-us/azure/architecture/solution-ideas/articles/dev-test-paas)
[^8]: [Subscriptions, licenses, accounts, and tenants for Microsoft's cloud offerings](https://learn.microsoft.com/en-us/microsoft-365/enterprise/subscriptions-licenses-accounts-and-tenants-for-microsoft-cloud-offerings?view=o365-worldwide)
[^9]: [Testing approach for enterprise-scale - Azure - Microsoft Learn](https://learn.microsoft.com/en-us/azure/cloud-adoption-framework/ready/enterprise-scale/testing-approach)