---
Creation Date: 2020-02-12
---



## 1. 前言 & 简介

[Tera Term](https://zh.wikipedia.org/wiki/Tera_Term) 是一款 SSH 脚本工具。

## 2. 操作

### 2.1. 控制台操作的日志

```
;; ...
;; ↑ 上面为连接操作

getenv 'USERNAME' username
gettime timestr "%Y%m%d-%H%M%S"
getdir mdir
sprintf2 filename '%s\log\console_%s_%s.log' mdir username timestr

foldercreate 'log'

logopen filename 0 0
```

使用场景:

我们常常会使用curl等操作，进行模拟一些真实环境的模拟操作。也可以通过这种方式保留操作的痕迹，用作测试的证据。

几个常用变量：

- [getdate](https://ttssh2.osdn.jp/manual/4/en/macro/command/getdate.html) 日期  [gettime](https://ttssh2.osdn.jp/manual/4/en/macro/command/gettime.html) 时间
- [getttdir](https://ttssh2.osdn.jp/manual/4/en/macro/command/getttdir.html) [changedir](https://ttssh2.osdn.jp/manual/4/en/macro/command/changedir.html) 地址 
- [foldercreate](https://ttssh2.osdn.jp/manual/4/en/macro/command/foldercreate.html) 创建文件夹

### 2.2. teraterm和vagrant结合

```
msg='127.0.0.1:2222 /ssh /auth=publickey /user=vagrant

;;key
strconcat msg '/keyfile='
getdir keyfile
strconcat keyfile '\.vagrant\machines\default\virtualbox\private_key.ppk '
strconcat msg keyfile

connect msg
```

### 2.3. 执行脚本

```
;; 在登录后，在控制台执行脚本内容
;; 执行的是当前目录下的脚本
getdir PATH
makepath FILE PATH '脚本名'
sendfile FILE 0
```

### 2.4 其他

- [dispstr](https://ttssh2.osdn.jp/manual/4/en/macro/command/dispstr.html) 屏幕大小
- 发送接收文件，可以选择[WinSCP](https://zh.wikipedia.org/wiki/WinSCP)，不建议使用tera term，因为该工具本身就不是为了文件传输而做的，所以没有上下传输的GUI。

