## Mac

### 1.安装 PicGo

Mac 和 windows都差不多，只要找到对应程序和配置文件修改即可。

```bash
# https://support.typora.io/Upload-Image/#picgo-core-command-line-opensource
$ npm install picgo -g

# 看版本
$ picgo -v

# 设置该命令到 typora 上 
$ command="$(which picgo) upload"
$ echo $command
/Users/user/.nvm/versions/node/v14.19.3/bin/picgo upload

# 获取
$ open https://github.com/settings/tokens

# 配置相关信息
$ picgo set uploader
$ picgo use uploader
```

### 2. 文件名重命名

```bash
picgo install rename-file
```

## Windows

1. [官网](https://github.com/Molunerfinn/PicGo/releases)下载并安装
2. 在应用里添加配置
3. typora中添加 PicGo 的地址![image-20221202030251359](C:\Users\q2455\AppData\Roaming\Typora\typora-user-images\image-20221202030251359.png)

