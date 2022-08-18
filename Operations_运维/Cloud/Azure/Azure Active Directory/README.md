---
Last Modified: 2022-08-18
---







## Security

### S.1 Conditional Access 条件访问

**Conditional Access** 的前提条件有两个：

1. 更改套餐（license） [Azure Active Directory Premium](https://azure.microsoft.com/en-us/pricing/details/active-directory/)
2. 关闭 **[Security defaults](https://docs.microsoft.com/en-us/microsoft-365/business-premium/m365bp-conditional-access?view=o365-worldwide#security-defaults)** 

Conditional Access 是一个付费功能，通过该功能可以自由的配置 policy。由于我们关闭 Security defaults，所以需要单独创建对应的 policy：[^3]

- [Require MFA for administrators](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-admin-mfa)
- [Require MFA for Azure management](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-azure-management)
- [Block legacy authentication](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-block-legacy)
- [Require MFA for all users](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-all-users-mfa)



### S.2 Security defaults

[Security defaults](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults) 默认是启动的，它可以保护绝大多数的安全。

它包含了：[^1][^2]

- 要求注册 MFA
- 要求 admins 开启 MFA
- 根据实际情况， MFA 会增加警告次数。如下载整个文件夹
- 禁止无法使用 MFA 的 legacy authentication clients
  - 非 [modern authentication](https://docs.microsoft.com/en-us/microsoft-365/enterprise/hybrid-modern-auth-overview?view=o365-worldwide#what-is-modern-authentication) 的
  - older mail protocols such as IMAP, SMTP, or POP3

所以当我们要关闭 **Security defaults** 时候，必须做的是手动创建 policy 去cover上述所有场景。



## 参考 References

[^1]: [Security defaults and Conditional Access - Microsoft Docs](https://docs.microsoft.com/en-us/microsoft-365/business-premium/m365bp-conditional-access?view=o365-worldwide)
[^2]: [Security defaults in Azure AD - Active Directory - Microsoft Docs](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults)
[^3]: [Security defaults in Azure AD - Active Directory - Microsoft Docs](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults#conditional-access)