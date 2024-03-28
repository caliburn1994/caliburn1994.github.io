





## 1. 命令行一栏

这里有两种方式查看命令行一栏

1. 可以通过 `docker --help` 查看命令行

   ```bash
   % docker --help
   
   Usage:  docker [OPTIONS] COMMAND
   
   A self-sufficient runtime for containers
   
   Options:
         --config string      Location of client config files (default
                              "/Users/user/.docker")
     -c, --context string     Name of the context to use to connect to the
                              daemon (overrides DOCKER_HOST env var and
                              default context set with "docker context use")
     -D, --debug              Enable debug mode
     -H, --host list          Daemon socket(s) to connect to
     -l, --log-level string   Set the logging level
                              ("debug"|"info"|"warn"|"error"|"fatal")
                              (default "info")
         --tls                Use TLS; implied by --tlsverify
         --tlscacert string   Trust certs signed only by this CA (default
                              "/Users/user/.docker/ca.pem")
         --tlscert string     Path to TLS certificate file (default
                              "/Users/user/.docker/cert.pem")
         --tlskey string      Path to TLS key file (default
                              "/Users/user/.docker/key.pem")
         --tlsverify          Use TLS and verify the remote
     -v, --version            Print version information and quit
   
   Management Commands:
     builder     Manage builds
     config      Manage Docker configs
     container   Manage containers
     context     Manage contexts
     image       Manage images
     manifest    Manage Docker image manifests and manifest lists
     network     Manage networks
     node        Manage Swarm nodes
     plugin      Manage plugins
     secret      Manage Docker secrets
     service     Manage services
     stack       Manage Docker stacks
     swarm       Manage Swarm
     system      Manage Docker
     trust       Manage trust on Docker images
     volume      Manage volumes
   
   Commands:
     attach      Attach local standard input, output, and error streams to a running container
     build       Build an image from a Dockerfile
     commit      Create a new image from a container's changes
     cp          Copy files/folders between a container and the local filesystem
     create      Create a new container
     diff        Inspect changes to files or directories on a container's filesystem
     events      Get real time events from the server
     exec        Run a command in a running container
     export      Export a container's filesystem as a tar archive
     history     Show the history of an image
     images      List images
     import      Import the contents from a tarball to create a filesystem image
     info        Display system-wide information
     inspect     Return low-level information on Docker objects
     kill        Kill one or more running containers
     load        Load an image from a tar archive or STDIN
     login       Log in to a Docker registry
     logout      Log out from a Docker registry
     logs        Fetch the logs of a container
     pause       Pause all processes within one or more containers
     port        List port mappings or a specific mapping for the container
     ps          List containers
     pull        Pull an image or a repository from a registry
     push        Push an image or a repository to a registry
     rename      Rename a container
     restart     Restart one or more containers
     rm          Remove one or more containers
     rmi         Remove one or more images
     run         Run a command in a new container
     save        Save one or more images to a tar archive (streamed to STDOUT by default)
     search      Search the Docker Hub for images
     start       Start one or more stopped containers
     stats       Display a live stream of container(s) resource usage statistics
     stop        Stop one or more running containers
     tag         Create a tag TARGET_IMAGE that refers to SOURCE_IMAGE
     top         Display the running processes of a container
     unpause     Unpause all processes within one or more containers
     update      Update configuration of one or more containers
     version     Show the Docker version information
     wait        Block until one or more containers stop, then print their exit codes
   
   Run 'docker COMMAND --help' for more information on a command.
   
   To get more help with docker, check out our guides at https://docs.docker.com/go/guides/
   ```

2. 查看官方文档 [Use the Docker command line](https://docs.docker.com/engine/reference/commandline/cli/)

## 2. 常用的命令

```bash
$ docker run \
-p ${container_port}:${host_port}
# 创建



# 通过镜像my_image创建容器
# 后台运行 -d
# 主机(宿主)端口:容器端口 = 80:80
# 执行命令 service nginx start 
docker run -d -p 80:80 my_image service nginx start



# 执行容器里某条命令
docker exec -it my_container sh -c "echo a && echo b"

# 为[ContainerId=c3f279d17e0a]的容器，创建镜像。
docker commit c3f279d17e0a  svendowideit/testimage:version3
```







#### 1. [exec](http://www.runoob.com/docker/docker-exec-command.html)

#### 2. [commit](https://yeasy.gitbooks.io/docker_practice/content/image/commit.html)  (类似git)

##### 介绍

 提交修改, 更新镜像

 提交之前, 需要查看需改内容

```shell
docker diff 容器名 
```

##### 慎用

- 黑箱操作, 操作过程没有以脚本形式存储下来
- [看不懂](https://yeasy.gitbooks.io/docker_practice/content/image/commit.html) , 说因为分层, 所以删除一些内容并不会真的丢失, commit操作多了, 就会镜像越来越大

#### 3. 









## 三) 镜像  docker image

|      | 命令                                                         |
| ---- | ------------------------------------------------------------ |
|      | build       Build an image from a Dockerfile                 |
|      | history     Show the history of an image                     |
|      | import      Import the contents from a tarball to create a filesystem image |
|      | inspect     Display detailed information on one or more images |
|      | load        Load an image from a tar archive or STDIN        |
|      | ls          List images                                      |
|      | prune       Remove unused images                             |
|      | pull        Pull an image or a repository from a registry    |
|      | push        Push an image or a repository to a registry      |
|      | rm          Remove one or more images                        |
|      | save        Save one or more images to a tar archive (streamed to STDOUT by default) |
|      | tag         Create a tag TARGET_IMAGE that refers to SOURCE_IMAGE |



## 四) 容器 docker container



| 命令    | 中文解释           | 英文                                                         |
| ------- | ------------------ | ------------------------------------------------------------ |
| attach  |                    | Attach local standard input, output, and error streams to a running container |
| commit  |                    | Create a new image from a container's changes                |
| cp      |                    | Copy files/folders between a container and the local filesystem |
| create  |                    | Create a new container                                       |
| diff    |                    | Inspect changes to files or directories on a container's filesystem |
| exec    |                    | Run a command in a running container                         |
| export  |                    | Export a container's filesystem as a tar archive             |
| inspect |                    | Display detailed information on one or more containers       |
| kill    |                    | Kill one or more running containers                          |
| logs    |                    | Fetch the logs of a container                                |
| ls      | 运行的容器         | List containers                                              |
| ls -a   | 所有容器           |                                                              |
| pause   |                    | Pause all processes within one or more containers            |
| port    |                    | List port mappings or a specific mapping for the container   |
| prune   | 删除所有停止的容器 | Remove all stopped containers                                |
| rename  |                    | Rename a container                                           |
| restart |                    | Restart one or more containers                               |
| rm      |                    | Remove one or more containers                                |
| run     |                    | Run a command in a new container                             |
| start   |                    | Start one or more stopped containers                         |
| stats   |                    | Display a live stream of container(s) resource usage statistics |
| stop    |                    | Stop one or more running containers                          |
| top     |                    | Display the running processes of a container                 |
| unpause |                    | Unpause all processes within one or more containers          |
| update  |                    | Update configuration of one or more containers               |
| wait    |                    | Block until one or more containers stop, then print their exit codes |

