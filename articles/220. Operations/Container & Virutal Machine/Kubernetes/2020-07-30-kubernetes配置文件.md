---
layout: post
title: Kubernetes-配置文件
date: 2020-06-15 20:00:02
categories: 计算机
tags: [鸦鸦的维基,kubernetes]
comments: 1 
typora-root-url: ..\..
---

Kubernetes是一个开源系统，用于自动化部署、伸缩<sup>scaling</sup>、管理容器化应用。

## 集群配置

```
--------------------
[开发者主机]<-->[集群]  

连接需要的
- 配置文件：.kube/config
- 命令：kubectl config
----------------------
```

### kubectl config

```shell
$ kubectl config
Modify kubeconfig files using subcommands like "kubectl config set current-context my-context"

 The loading order follows these rules:

  1.  If the --kubeconfig flag is set, then only that file is loaded. The flag may only be set once and no merging takes
place.
  2.  If $KUBECONFIG environment variable is set, then it is used as a list of paths (normal path delimiting rules for
your system). These paths are merged. When a value is modified, it is modified in the file that defines the stanza. When
a value is created, it is created in the first file that exists. If no files in the chain exist, then it creates the
last file in the list.
  3.  Otherwise, ${HOME}/.kube/config is used and no merging takes place.

Available Commands:
  current-context Displays the current-context
  delete-cluster  Delete the specified cluster from the kubeconfig
  delete-context  Delete the specified context from the kubeconfig
  get-clusters    Display clusters defined in the kubeconfig
  get-contexts    Describe one or many contexts
  rename-context  Renames a context from the kubeconfig file.
  set             Sets an individual value in a kubeconfig file
  set-cluster     Sets a cluster entry in kubeconfig
  set-context     Sets a context entry in kubeconfig
  set-credentials Sets a user entry in kubeconfig
  unset           Unsets an individual value in a kubeconfig file
  use-context     Sets the current-context in a kubeconfig file
  view            Display merged kubeconfig settings or a specified kubeconfig file

Usage:
  kubectl config SUBCOMMAND [options]

Use "kubectl <command> --help" for more information about a given command.
Use "kubectl options" for a list of global command-line options (applies to all commands).
```

当我们操控若干个集群时，所有集群的配置将会放置于`${HOME}/.kube/config`中。<sup>[[官网]](https://kubernetes.io/docs/concepts/configuration/organize-cluster-access-kubeconfig/)</sup>

### .kube/config分析

首先我们去查看` ~/.kube/config`究竟有什么内容，并对照着AWS的EKS进行学习：

```shell
$ cat ~/.kube/config
```

```yaml
apiVersion: v1
kind: Config
preferences: {}

clusters:
- cluster: # 集群的kubernetes API地址和凭证
    certificate-authority-data: # EKS的<Certificate authority>
    server: https://2C1A77626A2087EBA1D1123EA9398DAF.gr7.ap-northeast-1.eks.amazonaws.com
    # EKS的<API server endpoint>
  name: eksworkshop-eksctl.ap-northeast-1.eksctl.io
contexts:
- context:
    cluster: eksworkshop-eksctl.ap-northeast-1.eksctl.io
    user: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
  name: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
current-context: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
users:
- name: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1alpha1
      args:
      - token
      - -i
      - eksworkshop-eksctl
      command: aws-iam-authenticator
      env:
      - name: AWS_STS_REGIONAL_ENDPOINTS
        value: regional
      - name: AWS_DEFAULT_REGION
        value: ap-northeast-1
```

另外，EKS的**OpenID Connect provider URL**也可以用来设置凭证。<sup>[[k8s]](https://kubernetes.io/docs/reference/access-authn-authz/authentication/#openid-connect-tokens)</sup>

![image-20200730213432460](/assets/blog_res/image-20200730213432460.png)

#### context

context（语境），包含：

- cluster
- namespace
- user

可以将context视为一个配置的结构体，current-context表示当前的配置。

```yaml
- context:
    cluster: development
    namespace: frontend
    user: developer
  name: dev-frontend
```

可以将 `dev-frontend`语境 理解为：某人使用 <u>`developer`用户凭证</u> 对 <u>`development`集群</u> 的 <u>`frontend`名字空间</u> 进行访问。

#### user

用户信息。一般包含：

- 环境变量
- 用户的凭证。不同用户拥有不同的凭证，不同凭证代表着不同的权限。

凭证方式如下：

##### Webhook Token Authentication

Webhook令牌验证<sup>Webhook Token Authentication</sup>，又叫做 Webhook模式<sup>[Webhook Mode](https://kubernetes.io/docs/reference/access-authn-authz/webhook/)</sup>。该模式下，【配置完以下内容时】该事件会触发Kubernetes去查询外部REST服务。

```yaml
# 证书
- name: minikube
  user:
    client-certificate: ~/.minikube/profiles/minikube/client.crt
    client-key: ~/.minikube/profiles/minikube/client.key
```

单节点集群软件minikube中的`client-key`为TLS的client key，用于会话密钥`client-certificate`为TLS的client cert为证书。<sup>[[cloudflare]](https://www.cloudflare.com/learning/ssl/what-happens-in-a-tls-handshake/)</sup>

##### OIDC Authenticator

[OIDC Authenticator](https://kubernetes.io/docs/reference/access-authn-authz/authentication/#option-1-oidc-authenticator)，该模式下，令牌有时间限制，在到期之后会更新令牌。Google云使用该模式。

```yaml
- name: gke_mygopo_asia-northeast1_test0526
  user:
    auth-provider:
      config:
        access-token: ya29.a0AfH6SMBuiuISej2TrmcuHcWqaYn_EvWBXbW_0ttcHkA1AvUnb8b2TB-LYjGO-XEEUi14-a1hAyWIlxLkj5rV5So_Tg72RME1wmf6IrhwB7S8noyXvyMs1OXMBKWtXWyf-o3SyJ9HsFxRM-4ilOX0yv5fsT-_swrEn_er1eMv_fiw_g
        cmd-args: config config-helper --format=json
        cmd-path: /usr/lib/google-cloud-sdk/bin/gcloud
        expiry: "2020-07-06T09:17:53Z"
        expiry-key: '{.credential.token_expiry}'
        token-key: '{.credential.access_token}'
      name: gcp
```

##### client-go credential plugins

[client-go credential plugins](https://kubernetes.io/docs/reference/access-authn-authz/authentication/#client-go-credential-plugins)该功能下，`kubelet`将执行外部命去获取凭证。该功能常常与其他验证协议一起使用（如：LDAP, Kerberos, OAuth2, SAML, etc）。aws eks使用该功能。

```yaml
- name: arn:aws:eks:ap-northeast-1:684454806125:cluster/production
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1alpha1
      args:
      - --region
      - ap-northeast-1
      - eks
      - get-token
      - --cluster-name
      - production
      command: aws
      env: null
```

#### namespace

能使用的命名空间。

#### cluster

集群信息。一般包含：

- 服务器Rest API地址，用于`kubectl`连接。如：server
- 连接所需要的凭证（证书）：如：certificate-authority-data（base64格式的证书）

### 默认Service account

如果Pod没有一个ServiceAccount的话，Pod将启动被分配到`defualt`的ServiceAccount。





```
--------------------
[开发者主机]<-->[集群]  
--------------------
```

```

```























