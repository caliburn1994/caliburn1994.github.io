---
layout: post
title: About the go command 译文
date: 2020-05-08 23:55:01
categories: 计算机
tags: Go
comments: 1
typora-root-url: ..
excerpt: About the go command 译文
---

> 原文地址：https://golang.org/doc/articles/go_command.html
>
> 译文地址：[Github译文地址](https://github.com/caliburn1994/caliburn1994.github.io/blob/b00cb6d204544ce1bc3a913bc6eb57b728e58ecb/article/_posts/2020-05-12-How%20to%20Write%20Go%20Code%20%E8%AF%91%E6%96%87.md)
>
> 更新时间：2020.05.12
>
> 未经许可不许转载，以免造成互联网冗余。

# About the go command

> The Go distribution includes a command, named "`go`", that automates the downloading, building, installation, and testing of Go packages and commands. This document talks about why we wrote a new command, what it is, what it's not, and how to use it.

Go发布版本包含了一个命令，名为"`go`"，该命令将Go包的下载、构建、安装、测试自动化。该文档讲述了为什么我们要有这样的一个新命令，它是什么，它不是什么，为什么要使用它。

## Motivation

> You might have seen early Go talks in which Rob Pike jokes that the idea for Go arose while waiting for a large Google server to compile. That really was the motivation for Go: to build a language that worked well for building the large software that Google writes and runs. It was clear from the start that such a language must provide a way to express dependencies between code libraries clearly, hence the package grouping and the explicit import blocks. It was also clear from the start that you might want arbitrary syntax for describing the code being imported; this is why import paths are string literals.

你也许看了挺早之前的一个Go演说，Rob Pike开玩笑说，Go这个点子是诞生在他等待Google服务器编译。这是真正制造Go语言的动机，“想去制造一种语言，这种语言能够很好地构建大型软件，并且Google（内部）能够编写与运行起来”。在一开始就很明确这种语言，能够提供一种方式去清晰的表达 代码库（code libraries）之间如何依赖，因此使用了包（package ）进行分组 和显示import块。一开始就很明确，你也许想要通过某种方式去描述代码要导入，这也就是为什么导入路径（import paths）是字符串文字。

> An explicit goal for Go from the beginning was to be able to build Go code using only the information found in the source itself, not needing to write a makefile or one of the many modern replacements for makefiles. If Go needed a configuration file to explain how to build your program, then Go would have failed.

Go从开始依赖的一个明确的目标：仅仅通过源代码自身就能够构建Go代码，不需要 makefile 以及其他类似的现代方式。如果Go需要一个配置文件缺解释程序是怎么样构建的，那么Go就失败了！

> At first, there was no Go compiler, and the initial development focused on building one and then building libraries for it. For expedience, we postponed the automation of building Go code by using make and writing makefiles. When compiling a single package involved multiple invocations of the Go compiler, we even used a program to write the makefiles for us. You can find it if you dig through the repository history.

一开始，因为我们没有Go编译器，所以一开始集中精力去开发一个编译器，以及相关的包。我们推迟了Go代码的自动化工具的开发，取代之仅仅是 makefile  （这是一种投机的行为）。当编译一个包需要引入其他若干个时候，我们编写 makefile 去实现。如果你去搜索整个仓库的历史，你会发现这一点。

> The purpose of the new go command is our return to this ideal, that Go programs should compile without configuration or additional effort on the part of the developer beyond writing the necessary import statements.
>

Go命令开发目标是回归到当初的期待，Go程序应该在没有配置以及多余的操作的情况下，都能编译。

## Configuration versus convention

> The way to achieve the simplicity of a configuration-free system is to establish conventions. The system works only to the extent that those conventions are followed. When we first launched Go, many people published packages that had to be installed in certain places, under certain names, using certain build tools, in order to be used. That's understandable: that's the way it works in most other languages. Over the last few years we consistently reminded people about the `goinstall` command (now replaced by [`go get`](https://golang.org/cmd/go/#hdr-Download_and_install_packages_and_dependencies)) and its conventions: first, that the import path is derived in a known way from the URL of the source code; second, that the place to store the sources in the local file system is derived in a known way from the import path; third, that each directory in a source tree corresponds to a single package; and fourth, that the package is built using only information in the source code. Today, the vast majority of packages follow these conventions. The Go ecosystem is simpler and more powerful as a result.
>

为了达到免配置系统的那样简单，就需要去建立约定。在某种程度上，该系统只有在遵循约定之后才能运行。当我们第一次运行Go时，其他人上传的包将会被安装到实当的位置，给予实当名字，通过适当的工具构建，只有这样才能运行。只是显而易懂得，因为其他大多语言也是这样做的。

在过去几年内，我们常常提醒大家注意`goinstall`（已废弃，现使用 [`go get`](https://golang.org/cmd/go/#hdr-Download_and_install_packages_and_dependencies)）以及它的约定。

1. 源代码的URL地址 可以推导得出 导入路径（import path）。
2. 导入路径 可以通过众所周知的方法推导得出 源代码存储的路径。
3. 源代码树（source tree）的每一个目录（directory）都对应着一个Go包。
4. 只有通过使用源文件（source files）中的信息，才能构建包。

今天，大多数核心包都遵循着这个约定。从结果上看，Go语言的生态系统变得更加简单且有力。

> We received many requests to allow a makefile in a package directory to provide just a little extra configuration beyond what's in the source code. But that would have introduced new rules. Because we did not accede to such requests, we were able to write the go command and eliminate our use of make or any other build system.

我们接收到许多请求（requests），他们希望在包里添加 makefile ，以获得稍微多一点的配置，而不是将配置放于源代码中。 但如果真的这样做了，将会引入一些新的规则。因为我们不会去接受这种请求，所以我们将开发的Go命令会避免“对系统使用make或其他构建”。

> It is important to understand that the go command is not a general build tool. It cannot be configured and it does not attempt to build anything but Go packages. These are important simplifying assumptions: they simplify not only the implementation but also, more important, the use of the tool itself.

理解Go命令不是泛用性构建工具是十分重要。它只会配置和构建Go包，而不会管任何事物。这些想法都是重要且简单化的假设，它们不仅简化了实现，更重要的是简化了使用工具。

## Go's conventions

> The `go` command requires that code adheres to a few key, well-established conventions.

`go`命令需要代码拥有某些关键词，以及完善的约定。

> First, the import path is derived in a known way from the URL of the source code. For Bitbucket, GitHub, Google Code, and Launchpad, the root directory of the repository is identified by the repository's main URL, without the `http://` prefix. Subdirectories are named by adding to that path. For example, the Go example programs are obtained by running

第一，源代码的URL地址 可以推导得出 导入路径（import path）。Bitbucket、GitHub、Google Code、Launchpad，这些仓库的根目录是由它们的主URL决定的，并且会去除`http://` 前缀。子文件则为对应路径名。举例说明，Go的示例程序可以通过下方获得

```
git clone https://github.com/golang/example
```

> and thus the import path for the root directory of that repository is "`github.com/golang/example`". The [stringutil](https://godoc.org/github.com/golang/example/stringutil) package is stored in a subdirectory, so its import path is "`github.com/golang/example/stringutil`".

因此这个仓库的根目录的导入路径（import path）为"`github.com/golang/example`"。根目录的子目录下有一个 [stringutil](https://godoc.org/github.com/golang/example/stringutil)包 ，那么它的导入路径（import path）为"`github.com/golang/example/stringutil`"。

> These paths are on the long side, but in exchange we get an automatically managed name space for import paths and the ability for a tool like the go command to look at an unfamiliar import path and deduce where to obtain the source code.

这些路径看起来有点长，但是换来的是，我们可以获得一个自动化的可管理的名称空间，用于导入路径（import paths）以及其他工具，如Go命令看到一些陌生的导入路径（import paths）也可以想办法去获取源代码。

> Second, the place to store sources in the local file system is derived in a known way from the import path, specifically `$GOPATH/src/`. If unset, `$GOPATH` defaults to a subdirectory named `go` in the user's home directory. If `$GOPATH` is set to a list of paths, the go command tries `/src/` for each of the directories in that list.

第二点，导入路径 可以通过众所周知的方法推导得出 源代码存储的路径，常指`$GOPATH/src/`。如果没有设定`$GOPATH`，默认下会指代用户home目录下的一个名为`go`的子目录。如果`$GOPATH`设置了一系列路劲，那么Go命令将会辨力每一个路径下的`/src/`。

> Each of those trees contains, by convention, a top-level directory named "`bin`", for holding compiled executables, and a top-level directory named "`pkg`", for holding compiled packages that can be imported, and the "`src`" directory, for holding package source files. Imposing this structure lets us keep each of these directory trees self-contained: the compiled form and the sources are always near each other.

约定俗成，这些路径（`$GOPATH`）包含了一个名为"`bin`"顶级目录，里头有编译后可执行文件；名为"`pkg`"顶级目录，里头有用于导入且编译好的包；名为"`src`"顶级目录，里头有包的源文件。导入这个（目录）结构，让我们更好地保持这些目录树的独立且完整（self-contained）——因为编译好的文件和源代码的位置常常是相邻的（所以我们要导入该结构）。

> These naming conventions also let us work in the reverse direction, from a directory name to its import path. This mapping is important for many of the go command's subcommands, as we'll see below.

这些命名约定，是可以反向操作的，从文件夹名称查找导入路径。这种映射关系对于Go命令的子命令来说，是十分重要的。我们可以查看以下：

> Third, each directory in a source tree corresponds to a single package. By restricting a directory to a single package, we don't have to create hybrid import paths that specify first the directory and then the package within that directory. Also, most file management tools and UIs work on directories as fundamental units. Tying the fundamental Go unit—the package—to file system structure means that file system tools become Go package tools. Copying, moving, or deleting a package corresponds to copying, moving, or deleting a directory.

第三点，源代码树（source tree）的每一个目录（directory）都对应着一个Go包。通过将文件夹规定为一个单独的包，我们可以不用创建复合的导入路径（复合的导入路径，首先会去指定目录，然后再指定目录下的包）。而且，大部分的文件管理工具和UI也可以成为该目录的基本单位（fundamental unit）。将“基础Go单元”、“包”、“文件系统”绑定在一起，这意味着系统工具也能当Go包工具使用。拷贝、以动、删除包对应着拷贝、移动、删除文件夹。

> Fourth, each package is built using only the information present in the source files. This makes it much more likely that the tool will be able to adapt to changing build environments and conditions. For example, if we allowed extra configuration such as compiler flags or command line recipes, then that configuration would need to be updated each time the build tools changed; it would also be inherently tied to the use of a specific toolchain.

第四点，只有通过使用源文件（source files）中的信息，才能构建包。这样能使工具更有可能适应环境和情况的变化。举例说明，如果我们使用外部配置，如编译器flag、命令行启动信息（recipe），那么每当构建工具发生该变时，（外部）配置也由可能需要更新。这也会造成我们紧紧依赖某一个特殊的工具链。

（译者：即，构建信息应该和项目绑定在一起，减少对工具的依赖。）

## Getting started with the go command

> Finally, a quick tour of how to use the go command. As mentioned above, the default `$GOPATH` on Unix is `$HOME/go`. We'll store our programs there. To use a different location, you can set `$GOPATH`; see [How to Write Go Code](https://golang.org/doc/code.html) for details.

最后，我们来试一下怎么用 Go命令 。上面提到的，Unix系统默认情况的`$GOPATH` 为`$HOME/go`。我们将会存储程序在这里。如果你想使用其他地方，可以设置一下`$GOPATH` ，详情请看[How to Write Go Code](https://golang.org/doc/code.html) 。

> We first add some source code. Suppose we want to use the indexing library from the codesearch project along with a left-leaning red-black tree. We can install both with the "`go get`" subcommand:

首先，我们添加下面的源代码。假设我们想要使用 "codesearch项目" 的 “indexing库（library）” 以及 “left-leaning red-black tree（LLRB）”。我们需要使用"go get"子命令安装它俩：

```
$ go get github.com/google/codesearch/index
$ go get github.com/petar/GoLLRB/llrb
$
```

> Both of these projects are now downloaded and installed into `$HOME/go`, which contains the two directories `src/github.com/google/codesearch/index/` and `src/github.com/petar/GoLLRB/llrb/`, along with the compiled packages (in `pkg/`) for those libraries and their dependencies.

它俩将会被下载并被安装到`$HOME/go`，下载内容包含

- 两个目录`src/github.com/google/codesearch/index/` 和`src/github.com/petar/GoLLRB/llrb/`
- 用于这些库（libraries）以及依赖 的编译完的包（在`pkg/`里）。

> Because we used version control systems (Mercurial and Git) to check out the sources, the source tree also contains the other files in the corresponding repositories, such as related packages. The "`go list`" subcommand lists the import paths corresponding to its arguments, and the pattern "`./...`" means start in the current directory ("`./`") and find all packages below that directory ("`...`"):

因为我们使用的是版本控制系统（Mercurial和Git）去check out源代码，源树（source tree）还包含对应仓库的其他文件，比如相关联的包。`go list`命令列举（与参数相对应的）导入路径。模板 "`./...`" 代表着着，在当前目录("`./`")下，查找所有的包("`...`")：

```
$ cd $HOME/go/src
$ go list ./...
github.com/google/codesearch/cmd/cgrep
github.com/google/codesearch/cmd/cindex
github.com/google/codesearch/cmd/csearch
github.com/google/codesearch/index
github.com/google/codesearch/regexp
github.com/google/codesearch/sparse
github.com/petar/GoLLRB/example
github.com/petar/GoLLRB/llrb
$
```

> We can also test those packages:

测试这些包：

```
$ go test ./...
?   	github.com/google/codesearch/cmd/cgrep	[no test files]
?   	github.com/google/codesearch/cmd/cindex	[no test files]
?   	github.com/google/codesearch/cmd/csearch	[no test files]
ok  	github.com/google/codesearch/index	0.203s
ok  	github.com/google/codesearch/regexp	0.017s
?   	github.com/google/codesearch/sparse	[no test files]
?       github.com/petar/GoLLRB/example          [no test files]
ok      github.com/petar/GoLLRB/llrb             0.231s
$
```

> If a go subcommand is invoked with no paths listed, it operates on the current directory:

Go子命令没有指定路径，那么操作会指定在当前目录下：

```
$ cd github.com/google/codesearch/regexp
$ go list
github.com/google/codesearch/regexp
$ go test -v
=== RUN   TestNstateEnc
--- PASS: TestNstateEnc (0.00s)
=== RUN   TestMatch
--- PASS: TestMatch (0.00s)
=== RUN   TestGrep
--- PASS: TestGrep (0.00s)
PASS
ok  	github.com/google/codesearch/regexp	0.018s
$ go install
$
```

> That "`go install`" subcommand installs the latest copy of the package into the pkg directory. Because the go command can analyze the dependency graph, "`go install`" also installs any packages that this package imports but that are out of date, recursively.

 "`go install`" 这个子命令将会安装最新版本的包，到pkg目录里。因为Go命令可分析依赖图，所以"`go install`"也可以（递归地）安装任何包，但是不包含过期的。

> Notice that "`go install`" was able to determine the name of the import path for the package in the current directory, because of the convention for directory naming. It would be a little more convenient if we could pick the name of the directory where we kept source code, and we probably wouldn't pick such a long name, but that ability would require additional configuration and complexity in the tool. Typing an extra directory name or two is a small price to pay for the increased simplicity and power.

注意："`go install`" 可以通过目录名的约定，进行推测本地包的导入路径。有时候我们会想，如果仅仅输入输入源代码的目录名（就能使用该命令），那就很方便啊！而且我们真不愿意输入一个很长的名字（作为参数）。但是这种方便性将会导致工具需要额外的配置和增加复杂度。多输入一两个外部目录名字是很低成本的，相比我们能增加简单性和强度而言。

（译者理解：如果输入的参数过短，那么工具搜索起来就会更费时。）

## Limitations

> As mentioned above, the go command is not a general-purpose build tool. In particular, it does not have any facility for generating Go source files *during* a build, although it does provide [`go` `generate`](https://golang.org/cmd/go/#hdr-Generate_Go_files_by_processing_source), which can automate the creation of Go files *before* the build. For more advanced build setups, you may need to write a makefile (or a configuration file for the build tool of your choice) to run whatever tool creates the Go files and then check those generated source files into your repository. This is more work for you, the package author, but it is significantly less work for your users, who can use "`go get`" without needing to obtain and build any additional tools.

如上述所说，Go命令不是一个泛用性的构建工具。尤其，在构建期间，它对于生成Go的源代码文件而言，没有任何便利性，虽然它提供了 [`go` `generate`](https://golang.org/cmd/go/#hdr-Generate_Go_files_by_processing_source)（用于在构建前自动化创建Go文件）。更多的在构建之前的步骤，你应该使用 `makefile`（或用你想要的构建工具的一种配置文件）去运行“生成Go文件的工具”并且去查看“生成出来的仓库里的源代码”。作为包的作者，你的工作会更多，但是这意味你的用户的工作会更少，用户通过 "`go get`" 获取时，不需要下载并构建其他额外的工具。

## More information

For more information, read [How to Write Go Code](https://golang.org/doc/code.html) and see the [go command documentation](https://golang.org/cmd/go/).