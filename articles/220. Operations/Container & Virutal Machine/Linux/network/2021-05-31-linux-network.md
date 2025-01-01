---
layout: post
title: Linux network调试工具
tags: Linux-Network
comments: 1
excerpt: Linux中常用的关于网络的命令。
---

## ping

`ping` uses ICMP datagrams to provoke（获得） a response from the chosen destination host, mainly intending to probe（探测） whether it is alive.

ICMP（Internet Control Message Protocol）属于 [OSI 7层模型](https://en.wikipedia.org/wiki/OSI_model#Layer_3:_Network_Layer) 的第三层、[Internet protocol suite](https://en.wikipedia.org/wiki/Internet_protocol_suite) 的第二层  <u>网络层</u>。

```bash
$ ping -h
Usage
  ping [options] <destination>
Options:
  <destination>      dns name or ip address
  ...
```

`ping` 可以探测 IP地址 和 DNS name。由于 DNS name <u>不包含 协议、端口、Path</u>，因此不能探测某一个服务是否存活。

ICMP 在传输层以下，所以不存在端口。操作系统可以禁止ICMP ，因此 `ping` 失败，不等于连接不上对应的主机，可以参考 EC2 的[security groups](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/working-with-security-groups.html)。[企业中禁ping和tracert有什么实际意义？](https://www.zhihu.com/question/37668237) 和 [Is it a bad idea for a firewall to block ICMP?](https://security.stackexchange.com/questions/22711/is-it-a-bad-idea-for-a-firewall-to-block-icmp) 中提及，黑客可能通过 ICMP 或许能获取内部网络结构。但是，由于 ICMP 对于检测是有帮助，所以可以允许 <u>特定IP地址</u> 进行访问，从而规避风险。

## traceroute

**TODO**

## nslookup

查看域名的**DNS、IP等各种信息**:<sup>[[参考网站]](https://www.geeksforgeeks.org/nslookup-command-in-linux-with-examples/)</sup>

```shell
$ nslookup google.com
Server:		8.8.8.8
Address:	8.8.8.8#53

Non-authoritative answer:
Name:	google.com
Address: 216.58.220.142
Name:	google.com
Address: 2404:6800:4004:80c::200e
```

```shell
$ nslookup -type=any google.com 
Server:		8.8.8.8
Address:	8.8.8.8#53

Non-authoritative answer:
Name:	google.com
Address: 172.217.31.142
Name:	google.com
Address: 2404:6800:4004:808::200e
google.com	mail exchanger = 40 alt3.aspmx.l.google.com.
google.com	mail exchanger = 20 alt1.aspmx.l.google.com.
google.com	nameserver = ns2.google.com.
google.com	nameserver = ns3.google.com.
google.com	text = "docusign=1b0a6754-49b1-4db5-8540-d2c12664b289"
google.com	mail exchanger = 30 alt2.aspmx.l.google.com.
google.com	text = "v=spf1 include:_spf.google.com ~all"
google.com	rdata_257 = 0 issue "pki.goog"
google.com	nameserver = ns1.google.com.
google.com	text = "facebook-domain-verification=22rm551cu4k0ab0bxsw536tlds4h95"
google.com
	origin = ns1.google.com
	mail addr = dns-admin.google.com
	serial = 314296817
	refresh = 900
	retry = 900
	expire = 1800
	minimum = 60
google.com	mail exchanger = 10 aspmx.l.google.com.
google.com	text = "docusign=05958488-4752-4ef2-95eb-aa7ba8a3bd0e"
google.com	text = "globalsign-smime-dv=CDYX+XFHUw2wml6/Gb8+59BsH31KzUr6c1l2BPvqKX8="
google.com	nameserver = ns4.google.com.
google.com	mail exchanger = 50 alt4.aspmx.l.google.com.

Authoritative answers can be found from:

```





## ping vs curl

使用curl成功，ping不成功，意味着特定端口开了，IP不开。

> 嗯， curl 这个 服务 是 工作 的， 但是 却 ping 不通。 这是 因为 服务 的 集群 IP 是一 个 虚拟 IP， 并且 只有 在与 服务 端口 结合 时 才有 意义。 将 在 第 11 章 中 解释 这 意味着 什么， 以及 服务 是 如何 工作 的。 在这里 提到 这个 问题， 因为 这是 用户 在 尝试 调试 异常 服务 时会 做 的 第一 件事（ ping 服务 的 IP）， 而 服务 的 IP 无法 ping 通 会 让 大多数人 措手不及。
>
> 七牛容器云团队. Kubernetes in Action中文版（博文视点图书） (Kindle 位置 2467-2470). 电子工业出版社. Kindle 版本. 