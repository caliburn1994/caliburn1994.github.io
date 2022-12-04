---
layout: post
title: (计算机)Authentication vs Verification
tags: 其他
comments: 1
typora-root-url: ../../../..
---

Authentication (认证) 和 Verification  (验证、校验) ，这两个概念挺相似。

*Cambridge字典* 上是这样描述他们的：[^1][^2]

> verification: the process of *testing or finding out* if something is true, real, accurate, etc.
> authentication: the process of *proving* that something is real, true, or what people say it is

从上面的解释上，我们可以多多少少体会到两者是十分相似的，如果硬要说区别和关联，那么可能是：verification 的结果/后续操作是 authentication。

示例：

-  <u>密码校验</u> (password  verification) 成功后，<u>用户认证</u> (User Authentication)  成功
- 通过使用校验码进行校验，使用户认证成功。
- [2-Step Verification](https://www.google.com/landing/2step/) 中，
  1.  <u>密码校验</u> (password  verification) 成功
  2. enter verification codes ，如果 verification 成功，那么 <u>用户认证</u> (User Authentication) 将成功

## 参考 References

[^1]: [VERIFICATION | meaning in the Cambridge English Dictionary](https://dictionary.cambridge.org/dictionary/english/verification)
[^2]: [AUTHENTICATION | meaning in the Cambridge English](https://dictionary.cambridge.org/dictionary/english/authentication)

