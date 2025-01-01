

## 简述

Role 包含的内容有：谁能在哪里做什么事情。

- **Security principal**(谁):  如: User, group, service principal, managed identity
- **Scope**(在哪里): 如: Management group, subscription, resource group, resource
  - **`AssignableScopes`**: 权限操作范围 
- **Role definition** (能做什么): 包含已定义的角色 (built-in role)，以及自定义角色
  - control plane 操作
    - **`Actions`**: control plane 允许的操作
    - **`NotActions`**: control plane 不允许的的操作。
    - 权限计算公式 `Actions - NotActions = Effective control plane permissions`
  - data plane 操作
    - **`DataActions`**: data plane 允许的操作

## Example

```json
[
  {
    "assignableScopes": [
      "/"
    ],
    "createdBy": null,
    "createdOn": "2017-12-21T00:01:24.797231+00:00",
    "description": "Allows for read access to Azure Storage blob containers and data",
    "id": "/subscriptions/{subscriptionId}/providers/Microsoft.Authorization/roleDefinitions/2a2b9908-6ea1-4ae2-8e65-a410df84e7d1",
    "name": "2a2b9908-6ea1-4ae2-8e65-a410df84e7d1",
    "permissions": [
      {
        "actions": [
          "Microsoft.Storage/storageAccounts/blobServices/containers/read",
          "Microsoft.Storage/storageAccounts/blobServices/generateUserDelegationKey/action"
        ],
        "condition": null,
        "conditionVersion": null,
        "dataActions": [
          "Microsoft.Storage/storageAccounts/blobServices/containers/blobs/read"
        ],
        "notActions": [],
        "notDataActions": []
      }
    ],
    "roleName": "Storage Blob Data Reader",
    "roleType": "BuiltInRole",
    "type": "Microsoft.Authorization/roleDefinitions",
    "updatedBy": null,
    "updatedOn": "2021-11-11T20:13:55.297507+00:00"
  }
]
```

参考：[[here]](https://learn.microsoft.com/en-us/azure/role-based-access-control/role-definitions)

## 常见的角色

| Fundamental role            | Description                                                  |
| :-------------------------- | :----------------------------------------------------------- |
| *Owner*                     | The *Owner* role has full access to all resources, including the right to delegate access to others. The *Service Administrator* and *Co-Administrators* roles are assigned the *Owner* role at the subscription scope. |
| *Contributor*               | The *Contributor* role can create and manage all types of Azure resources. This role can't grant access to others. |
| *Reader*                    | The *Reader* role can view existing Azure resources.         |
| *User Access Administrator* | The *User Access Administrator* role can manage user access to Azure resources. |