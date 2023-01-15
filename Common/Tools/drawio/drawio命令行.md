---
Last Modified: 2023-01-16
---


```bash
# 安装
# https://snapcraft.io/install/drawio/ubuntu
sudo apt update
sudo apt -y install curl xvfb nano libgtk-3-0 libnotify4 libnss3 libxss1 libxtst6 xdg-utils libatspi2.0-0 libappindicator3-1 libsecret-1-0 libgbm1 snapd
sudo snap install drawio

# 转换格式
output=xxx.png
intput=1.drawio
xvfb-run -a drawio -x -f png -o ${output} ${intput}
```