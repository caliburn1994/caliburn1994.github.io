---
Last Modified Date: 2022-08-14
---

## 1. 介绍 Introduction

Vagrant is an open-source software product for building and maintaining portable virtual software development environments. [^1]

Vagrant 用来创建和维护虚拟机的一个软件。

## 2. 安装 Install

### 2.1 Ubuntu 平台

```shell
# 安装虚拟机管理软件，vagrant是通过虚拟机管理软件，间接操作虚拟机
sudo apt-get install virtualbox
sudo apt-get install virtualbox—ext–pack

# 
sudo apt-get install vagrant
```

### 2.2 Windows 平台

1. 安装Git (以及cygwin) ，并设置
   ![](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/202212290004966.png)
2. 安装 VirtualBox
3. 安装 vagrant  (同时设置为 administrator)

- IDE 也可以设置成 administrator (但是尽可能不要这样做，不知道会有什么影响)

备注：由于 window 下不能使用 `sudo`，所以只能通过管理员启动命令，避免由于权限导致的错误。

## 3. 疏通测试 Walkthrough

在[安装完 Vargrant ](https://www.vagrantup.com/downloads)之后，由于虚拟机提供商有若干种，所以我们需要根据需求下载。

```shell
# 创建virtualbox的centos 7虚拟机
$ vagrant init centos/7
# 查看配置文件
$ cat Vagrantfile


# 启动/初始化虚拟机
$ vagrant up
# 关闭虚拟机
$ vagrant halt
# 删除虚拟机
$ vagrant destory

# 进入&&推出虚拟机
$ vagrant ssh
$ exit

# 停止&&运行
$ vagrant suspend
$ vagrant resume
```



### 3.1 Nodes

多节点时

```ruby
Vagrant.configure("2") do |config|
  # linux distribution
  config.vm.box = "centos/7"
  
  # 节点1
  config.vm.define "server1" do |server|
	  server.vm.hostname = "front_end"
  end
    
  # 节点2
  config.vm.define "server2" do |server|
	  server.vm.hostname = "back_end"
  end
end
```



## 4. Providers

### 4.1 VirtualBox

默认情况下，VirtualBox machine 是 **headless** 模式，意味着没有UI。<sup>[[来源]](https://www.vagrantup.com/docs/providers/virtualbox/configuration)</sup> 以下方式提供GUI：

```ruby
config.vm.provider "virtualbox" do |v|
	# 提供GUI  
	v.gui = true
    # 虚拟机名字
    v.name = "my_ubuntu2020"
    # 内存 CPU
    v.memory = 1024
	v.cpus = 2
    # CPU 运行不能超过50%
    v.customize ["modifyvm", :id, "--cpuexecutioncap", "50"]
    

	v.customize [
        "modifyvm", :id,
        "--vram", "256", # 显存 video memory
        "--clipboard-mode", "bidirectional", # 双向复制
        "--accelerate3d", "on", # 3D acceleration
        "--hwvirtex", "on", # 允许use of hardware virtualization extensions
        "--nestedpaging", "on", 
        "--largepages", "on",
        "--ioapic", "on",
        "--pae", "on",
        "--paravirtprovider", "kvm",
        ]
end
```

`vb.customize` 具体参数参考 VirtualBox 的[文档](https://www.virtualbox.org/manual/ch08.html)。命令列表如下 (当参数出错时，将会出现下面列表)

```
Usage:

VBoxManage modifyvm         <uuid|vmname>
                            [--name <name>]
                            [--groups <group>, ...]
                            [--description <desc>]
                            [--ostype <ostype>]
                            [--iconfile <filename>]
                            [--memory <memorysize in MB>]
                            [--pagefusion on|off]
                            [--vram <vramsize in MB>]
                            [--acpi on|off]
                            [--ioapic on|off]
                            [--hpet on|off]
                            [--triplefaultreset on|off]
                            [--apic on|off]
                            [--x2apic on|off]
                            [--paravirtprovider none|default|legacy|minimal|
                                                hyperv|kvm]
                            [--paravirtdebug <key=value> [,<key=value> ...]]
                            [--hwvirtex on|off]
                            [--nestedpaging on|off]
                            [--largepages on|off]
                            [--vtxvpid on|off]
                            [--vtxux on|off]
                            [--pae on|off]
                            [--longmode on|off]
                            [--ibpb-on-vm-exit on|off]
                            [--ibpb-on-vm-entry on|off]
                            [--spec-ctrl on|off]
                            [--l1d-flush-on-sched on|off]
                            [--l1d-flush-on-vm-entry on|off]
                            [--mds-clear-on-sched on|off]
                            [--mds-clear-on-vm-entry on|off]
                            [--nested-hw-virt on|off]
                            [--cpu-profile "host|Intel 80[86|286|386]"]
                            [--cpuid-portability-level <0..3>]
                            [--cpuid-set <leaf[:subleaf]> <eax> <ebx> <ecx> <edx>]
                            [--cpuid-remove <leaf[:subleaf]>]
                            [--cpuidremoveall]
                            [--hardwareuuid <uuid>]
                            [--cpus <number>]
                            [--cpuhotplug on|off]
                            [--plugcpu <id>]
                            [--unplugcpu <id>]
                            [--cpuexecutioncap <1-100>]
                            [--rtcuseutc on|off]
                            [--graphicscontroller none|vboxvga|vmsvga|vboxsvga]
                            [--monitorcount <number>]
                            [--accelerate3d on|off]
                            [--accelerate2dvideo on|off]
                            [--firmware bios|efi|efi32|efi64]
                            [--chipset ich9|piix3]
                            [--bioslogofadein on|off]
                            [--bioslogofadeout on|off]
                            [--bioslogodisplaytime <msec>]
                            [--bioslogoimagepath <imagepath>]
                            [--biosbootmenu disabled|menuonly|messageandmenu]
                            [--biosapic disabled|apic|x2apic]
                            [--biossystemtimeoffset <msec>]
                            [--biospxedebug on|off]
                            [--system-uuid-le on|off]
                            [--boot<1-4> none|floppy|dvd|disk|net>]
                            [--nic<1-N> none|null|nat|bridged|intnet|hostonly|
                                        generic|natnetwork]
                            [--nictype<1-N> Am79C970A|Am79C973|Am79C960|
                                            82540EM|82543GC|82545EM|
                                            virtio]
                            [--cableconnected<1-N> on|off]
                            [--nictrace<1-N> on|off]
                            [--nictracefile<1-N> <filename>]
                            [--nicproperty<1-N> name=[value]]
                            [--nicspeed<1-N> <kbps>]
                            [--nicbootprio<1-N> <priority>]
                            [--nicpromisc<1-N> deny|allow-vms|allow-all]
                            [--nicbandwidthgroup<1-N> none|<name>]
                            [--bridgeadapter<1-N> none|<devicename>]
                            [--hostonlyadapter<1-N> none|<devicename>]
                            [--intnet<1-N> <network name>]
                            [--nat-network<1-N> <network name>]
                            [--nicgenericdrv<1-N> <driver>]
                            [--natnet<1-N> <network>|default]
                            [--natsettings<1-N> [<mtu>],[<socksnd>],
                                                [<sockrcv>],[<tcpsnd>],
                                                [<tcprcv>]]
                            [--natpf<1-N> [<rulename>],tcp|udp,[<hostip>],
                                          <hostport>,[<guestip>],<guestport>]
                            [--natpf<1-N> delete <rulename>]
                            [--nattftpprefix<1-N> <prefix>]
                            [--nattftpfile<1-N> <file>]
                            [--nattftpserver<1-N> <ip>]
                            [--natbindip<1-N> <ip>]
                            [--natdnspassdomain<1-N> on|off]
                            [--natdnsproxy<1-N> on|off]
                            [--natdnshostresolver<1-N> on|off]
                            [--nataliasmode<1-N> default|[log],[proxyonly],
                                                         [sameports]]
                            [--macaddress<1-N> auto|<mac>]
                            [--mouse ps2|usb|usbtablet|usbmultitouch]
                            [--keyboard ps2|usb]
                            [--uart<1-N> off|<I/O base> <IRQ>]
                            [--uartmode<1-N> disconnected|
                                             server <pipe>|
                                             client <pipe>|
                                             tcpserver <port>|
                                             tcpclient <hostname:port>|
                                             file <file>|
                                             <devicename>]
                            [--uarttype<1-N> 16450|16550A|16750]
                            [--lpt<1-N> off|<I/O base> <IRQ>]
                            [--lptmode<1-N> <devicename>]
                            [--guestmemoryballoon <balloonsize in MB>]
                            [--vm-process-priority default|flat|low|normal|high]
                            [--audio none|null|dsound]
                            [--audioin on|off]
                            [--audioout on|off]
                            [--audiocontroller ac97|hda|sb16]
                            [--audiocodec stac9700|ad1980|stac9221|sb16]
                            [--clipboard-mode disabled|hosttoguest|guesttohost|
                                              bidirectional]
                            [--draganddrop disabled|hosttoguest|guesttohost|
                                           bidirectional]
                            [--vrde on|off]
                            [--vrdeextpack default|<name>]
                            [--vrdeproperty <name=[value]>]
                            [--vrdeport <hostport>]
                            [--vrdeaddress <hostip>]
                            [--vrdeauthtype null|external|guest]
                            [--vrdeauthlibrary default|<name>]
                            [--vrdemulticon on|off]
                            [--vrdereusecon on|off]
                            [--vrdevideochannel on|off]
                            [--vrdevideochannelquality <percent>]
                            [--usbohci on|off]
                            [--usbehci on|off]
                            [--usbxhci on|off]
                            [--usbrename <oldname> <newname>]
                            [--snapshotfolder default|<path>]
                            [--teleporter on|off]
                            [--teleporterport <port>]
                            [--teleporteraddress <address|empty>]
                            [--teleporterpassword <password>]
                            [--teleporterpasswordfile <file>|stdin]
                            [--tracing-enabled on|off]
                            [--tracing-config <config-string>]
                            [--tracing-allow-vm-access on|off]
                            [--usbcardreader on|off]
                            [--autostart-enabled on|off]
                            [--autostart-delay <seconds>]
                            [--recording on|off]
                            [--recordingscreens all|<screen ID> [<screen ID> ...]]
                            [--recordingfile <filename>]
                            [--recordingvideores <width> <height>]
                            [--recordingvideorate <rate>]
                            [--recordingvideofps <fps>]
                            [--recordingmaxtime <s>]
                            [--recordingmaxsize <MB>]
                            [--recordingopts <key=value> [,<key=value> ...]]
                            [--defaultfrontend default|<name>]
```



## 5. 执行脚本

|               | 默认值       | 选项                          |
| ------------- | ------------ | ----------------------------- |
| run           | 初始化时启动 | always ：启动虚拟机时候启动。 |
| privileged    | root用户执行 | false ：vagrant用户启动。     |
| path 文件位置 |              |                               |

```shell
# 每次启动时，执行
config.vm.provision "shell",run: "always", inline: <<-SHELL
	sudo systemctl start x
SHELL

# 执行初始化脚本
config.vm.provision "shell", path: 'provision.sh'
```



### 6. 常用脚本

#### 6.1 设置时区

```shell
# 设置timezone
sudo timedatectl set-timezone Asia/Tokyo
```

#### 6.2 安装docker

```ruby
Vagrant.configure("2") do |config|
  # linux distribution
  config.vm.box = "centos/7"

  # docker
  config.vm.provision :docker
  
  # docker-compose
  # install required plugin: https://github.com/leighmcculloch/vagrant-docker-compose
  # config.vm.provision :docker_compose
  
  # startup script
  config.vm.provision :shell, path: "bootstrap.sh"
  
  # run everytime 
  config.vm.provision "shell",run: "always", inline: <<-SHELL
  	ntpdate asia.pool.ntp.org
  SHELL
end
```



## 参考 References

[^1]: [Vagrant (software) - Wikipedia](https://en.wikipedia.org/wiki/Vagrant_(software))

