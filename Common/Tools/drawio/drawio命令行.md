---
Last Modified: 2022-12-29
---


```bash
# https://snapcraft.io/install/drawio/ubuntu
sudo apt update

sudo apt -y install curl xvfb nano \
libgtk-3-0 \
libnotify4 libnss3 libxss1 libxtst6 xdg-utils libatspi2.0-0 libappindicator3-1 libsecret-1-0 \
libgbm1 \
snapdremote

# Install draw.io
sudo snap install drawio

# 帮助

xvfb-run -a drawio --help
# 转换格式
xvfb-run -a drawio -x -f png -o xxx.png 1.drawio
```

