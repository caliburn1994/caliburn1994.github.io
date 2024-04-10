

## 简介

Azure 的 **Access management** 有三种 Role

- Classic subscription administrator roles: 第一版粗颗粒的角色

- [Azure role-based access control (RBAC) roles](Role.md)

- Microsoft Entra administrator roles: 用于管理 Microsoft 相关资源。

  > **Microsoft Entra admin roles** are used to manage resources in Microsoft Entra ID, such as users, groups, and domains. These roles are defined for the Microsoft Entra tenant at the root level of the configuration.

## 功能

- 默认情况下，全局管理员（Global Administrator）没有权限访问 subscription、management groups 下的资源。要 subscription 的管理员在 **Access control (IAM)** 下，赋予权限全局管理员才能访问。

  但全局管理员通过 [权限提升](./Global%20Administrator/Elevate%20access.md) 可以访问所有 subscriptions。