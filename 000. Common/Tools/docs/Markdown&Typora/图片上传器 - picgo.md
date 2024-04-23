## 1. Mac

### 1.1. 安装 PicGo

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
# 需要权限 content 的读写
$ open https://github.com/settings/tokens 

# 配置相关信息
$ picgo set uploader
$ picgo use uploader
```

### 1.2. 文件名重命名插件

```bash
$ picgo install rename-file
```

配置如下

```json
// open ~/.picgo/config.json
{
  "picBed": {
    "uploader": "github",
    "current": "github",
    "github": {
      "repo": "caliburn1994/caliburn1994.github.io",
      "branch": "dev",
      "token": "<github的token>",
      "path": "images/",
      "customUrl": "https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev"
    },
    "transformer": "path"
  },
  "picgoPlugins": {
    "picgo-plugin-rename-file": true
  },
  "picgo-plugin-rename-file": { // 图片的文件
    "format": "{y}{m}{d}{h}{i}{s}"
  }
}
```



## 2. Windows

### 2.1. Typora内上传图片

1. 在 Typora 的配置里安装 PicGo 相关的插件，并且上传

![image-20220213220553506](https://raw.githubusercontent.com/caliburn1994-2/caliburn1994-image/main/images/image-20220213220553506.png)

2. 修改配置

```
{
    
  "picBed": {
    
    "current": "github",
    "github": {
      "repo": "usrename/rep",
      "branch": "main",
      "token": "",
      "path": "images/",
	  "customUrl": "",
    },
    "uploader": "github",
  },
}
```

3. 黏贴图片到 Typora 里，选择上传图片。或者在 preference 里选择 **When insert... >> Upload image**





### 2.2. 使用 PicGo 上传

1. 可以使用 [PicGo](https://github.com/Molunerfinn/PicGo) 上传图片。配置好 Github 图床后，就可以上传图片。

![image-20220213220230998](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/image-20220213220230998.png)

2. （可选）开启 **时间戳重命名**，**上传后自动复制URL** 效果更佳

![image-20220213220351198](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/image-20220213220351198.png)

3. 在上传图片后，将字符串黏贴到 Typora 即可





## Bug

Bug-1：dyld Library not loaded 遇到这类的报错则执行以下代码进行修复: [["]](https://stackoverflow.com/questions/17703510/dyld-library-not-loaded-reason-image-not-found)

```bash
brew upgrade node
```



