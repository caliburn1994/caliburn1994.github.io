---
layout: post
title: 为什么password需要hash?
date: 2020-10-10 00:00:02
tags: [通用-Security]
comments: 1
excerpt: 为什么存到 Database 的 password 需要先进行 hash？
typora-root-url: ..
---

因为 password 与其hash之后的 password 是不可逆的。

```mermaid
graph LR
    password-->hashed_password
```

## Procedure

### Without Hash

没有使用hash加密的web应用通常是这样的：

```mermaid
sequenceDiagram
    User->>+Web: register with password
    Web-->>-User: OK

	Bad guy 1->>Web: get password from database or somewhere
	Bad guy 1->>+Web: login 
	Web-->>-Bad guy 1: OK
	
	Bad guy 2->>User: get password
	Bad guy 2->>+Web: login with password
	Web-->>-Bad guy 2: OK
```

盗用者可以通过Web系统的数据库或者在整个操作中的某一个漏洞中获取的密码，登录Web系统；盗用者当然也可以通过User那边获取密码，进行登录。

### With hash

```mermaid
sequenceDiagram
    User->>+Web: register with password
    Web-->>-User: OK

	Bad guy 1->>Web: get hashed password from database or somewhere
	Bad guy 1->>+Web: login 
	Web-->>-Bad guy 1: failed
	
	Bad guy 2->>User: get password
	Bad guy 2->>+Web: login with password
	Web-->>-Bad guy 2: OK
```

从上图，我们可以知道 hashed password 的主要功能的是**预防 Server 方因为自身的过失导致密码泄露**。另一点，就是**确保 password 只存在 User 自己手上**，其他人没有备份。

## 其他示例

### 容器中如何存储用户密码？

由于Docker容器会记录容器中的所有操作，所以如果我们：

```shell
$ echo "user1:user1password" | chpasswd
```

那么，盗用者查看容器的日志时，将会发现用户` user1` 的密码是 `user1password`。

```mermaid
sequenceDiagram
autonumber
    Note right of User_1: without Hash
    User_1->>+Container: set my password by [chpasswd]
    User_2->>Container: find User_1 password
	User_2->>+Container: login
	Container-->- User_2: OK
```

为了解决该问题，可以使用 pre-hash password。

```shell
$ echo 'user1:$6$lAkdPbeeZR7YJiE3$ohWgU3LcSVit/hEZ2VOVKvxD.67.N9h5v4ML7.4X51ZK3kABbTPHkZUPzN9jxQQWXtkLctI0FJZR8CChIwz.S/' | chpasswd --encrypted
```

我们告诉了 `chpasswd`  使用是 hashed password，所以即便盗用者，那只能看到 hashed password。而当我们登录时，使用的是 password，而步是 hashed password。而盗用者知道的是 hashed password，即便反推出也无法得到真正 password。<sup>[docker-sshd](https://github.com/panubo/docker-sshd)</sup>

```mermaid
sequenceDiagram
autonumber
    Note right of User_1: with Hash
    User_1->>User_1: hash password
    User_1->>+Container: pass the hashed password to [chpasswd --encrypted]
    User_2->>Container: find hashed password
	User_2->>+Container: × fail to login
```

