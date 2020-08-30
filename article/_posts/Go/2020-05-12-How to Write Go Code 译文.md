---
layout: post
title: How to Write Go Code 译文
date: 2020-05-12 23:55:01
categories: 计算机
tags: Go
comments: 1
typora-root-url: ..
excerpt: How to Write Go Code 译文
---

> 原文地址：https://golang.org/doc/code.html
>
> 译文地址：[Github译文地址](https://github.com/caliburn1994/caliburn1994.github.io/blob/b00cb6d204544ce1bc3a913bc6eb57b728e58ecb/article/_posts/2020-05-12-How%20to%20Write%20Go%20Code%20%E8%AF%91%E6%96%87.md)
>
> 更新时间：2020.05.12
>
> 未经许可不许转载，以免造成互联网冗余。

# How to Write Go Code

## Introduction

> This document demonstrates the development of a simple Go package inside a module and introduces the [go tool](https://golang.org/cmd/go/), the standard way to fetch, build, and install Go modules, packages, and commands.

本文档将展示在 module 里进行 Go package 的开发，并介绍如何用[go工具](https://golang.org/cmd/go/)获取、 构建并安装 Go module、package、command。

> Note: This document assumes that you are using Go 1.13 or later and the `GO111MODULE` environment variable is not set. If you are looking for the older, pre-modules version of this document, it is archived [here](https://golang.org/doc/gopath_code.html).

注：该文档是假设你使用的是1.13或之后版本，以及`GO111MODULE`环境变量没设定的情况下。如果你寻找更老的版本的文档，[这里](https://golang.org/doc/gopath_code.htm)有存档。

## Code organization

> Go programs are organized into packages. A package is a collection of source files in the same directory that are compiled together. Functions, types, variables, and constants defined in one source file are visible to all other source files within the same package.

Go程序由若干个 package 组成。每一个 package 都是同一目录下若干源代码集合成的，这些文件将会一同被编译。一个源代码文件下定义的Functions、types、variables、constants<sup>固定值</sup>，同一个package下的其他源文件将可直接访问它们。

> A repository contains one or more modules. A module is a collection of related Go packages that are released together. A Go repository typically contains only one module, located at the root of the repository. A file named `go.mod` there declares the module path: the import path prefix for all packages within the module. The module contains the packages in the directory containing its `go.mod` file as well as subdirectories of that directory, up to the next subdirectory containing another `go.mod` file (if any).

一个 repository<sup>仓库</sup> 包含了若干个 module，一个 module 包含若干个 Go package，并且 这些 Go package 将会一同被发布（released）。一个经典的 Go repository 仅包含一个 module，并且该 module 位于 repository 的根目录。在 module 所在处会有名为 `go.mod` 的文件，该文件声明了 module path。module path 是该 module 下所有 package 的 import path 的前缀。

举例说明：其他 package 要使用 `github.com/docker/cli` module 的 `cli` package：

```go
import (
	"github.com/docker/cli/cli"
)
```

而在开发时，该 package 下的源代码文件起名时只需这样：

```go
package cli
```

其中省略了`github.com/docker`前缀，该前缀在 `go.mod`  中声明了。

module 由 `go.mod` 所在目录及子目录的 package 组成，但如果子目录拥有另一个 `go.mod` ，那么这个 `go.mod`  的子目录将会不会被包含。

> Note that you don't need to publish your code to a remote repository before you can build it. A module can be defined locally without belonging to a repository. However, it's a good habit to organize your code as if you will publish it someday.

注：在你有能力构建代码之前，你不要推送代码至 remote repository<sup>远程仓库</sup>。模块可以在本地定义，而不需要（属于）仓库。然而，有意识地组织代码是一个好习惯，也许某天你需要推送代码（到仓库）。

> Each module's path not only serves as an import path prefix for its packages, but also indicates where the `go` command should look to download it. For example, in order to download the module `golang.org/x/tools`, the `go` command would consult the repository indicated by `https://golang.org/x/tools` (described more [here](https://golang.org/cmd/go/#hdr-Relative_import_paths)).

每一个模块的路径不仅仅提供 它的包 路径前缀，而且还可以提示 `go`命令去哪里下载（依赖包）。举例说明，如果你想要下载`golang.org/x/tools`模块，那么 `go` 将通过`https://golang.org/x/tools`该提示去查询仓库（详情请看[here](https://golang.org/cmd/go/#hdr-Relative_import_paths)）。

> An import path is a string used to import a package. A package's import path is its module path joined with its subdirectory within the module. For example, the module `github.com/google/go-cmp` contains a package in the directory `cmp/`. That package's import path is `github.com/google/go-cmp/cmp`. Packages in the standard library do not have a module path prefix.

一个导入路径是一个字符串，用于导入包。包的导入路径是它的模块路径 加上 该模块的子目录。举例说明， `github.com/google/go-cmp`模块包含了一个名为`cmp/`.的目录。那么该包的导入路径将会是`github.com/google/go-cmp/cmp`。在标准库中的包是不需要模块前缀的。

## Your first program

> To compile and run a simple program, first choose a module path (we'll use `example.com/user/hello`) and create a `go.mod` file that declares it:

尝试编译并运行一个简单程序吧，首先选择一个模块路径（我们就用`example.com/user/hello`吧），并创建一个`go.mod`文件，并声明（定义）它。

```
$ mkdir hello # Alternatively, clone it if it already exists in version control.
$ cd hello
$ go mod init example.com/user/hello
go: creating new go.mod: module example.com/user/hello
$ cat go.mod
module example.com/user/hello

go 1.14
$
```

> The first statement in a Go source file must be `package name`. Executable commands must always use `package main`.

Go源代码的第一行语句必须是 `package 名称`。可通过命令运行的可执行程序则（第一行）是 `package main`。

> Next, create a file named `hello.go` inside that directory containing the following Go code:

接下来，在文件夹内，创建一个名为 `hello.go` 的文件，内容如下：

```
package main

import "fmt"

func main() {
	fmt.Println("Hello, world.")
}
```

> Now you can build and install that program with the `go` tool:

现在，你可以通过 `go` 工具去构建并安装该程序

```
$ go install example.com/user/hello
$
```

> This command builds the `hello` command, producing an executable binary. It then installs that binary as `$HOME/go/bin/hello` (or, under Windows, `%USERPROFILE%\go\bin\hello.exe`).

该命令将会构建一个`hello`命令，并生成一个可执行的二进制文件。然后将它将该二进制文件安装成`$HOME/go/bin/hello`， (在Windows环境下则是`%USERPROFILE%\go\bin\hello.exe`).

> The install directory is controlled by the `GOPATH` and `GOBIN` [environment variables](https://golang.org/cmd/go/#hdr-Environment_variables). If `GOBIN` is set, binaries are installed to that directory. If `GOPATH` is set, binaries are installed to the `bin` subdirectory of the first directory in the `GOPATH` list. Otherwise, binaries are installed to the `bin` subdirectory of the default `GOPATH` (`$HOME/go` or `%USERPROFILE%\go`).

安装地址由`GOPATH` 和 `GOBIN` [环境变量](https://golang.org/cmd/go/#hdr-Environment_variables)决定。如果`GOBIN`设置了的话，则安装在这个目录下。如果`GOPATH`设定了，则安装到第一个`GOPATH` 的`bin`子目录下。否则，则安装在默认的`GOPATH` (`$HOME/go` 或 `%USERPROFILE%\go`)的`bin`子目录。

> You can use the `go env` command to portably set the default value for an environment variable for future `go` commands:

你可以通过`go env` 命令便捷地设置默认环境量，设置后将会影响之后的`go`命令：

```
$ go env -w GOBIN=/somewhere/else/bin
$
```

> To unset a variable previously set by `go env -w`, use `go env -u`:

删除（之前创建的）环境变量则使用`go env -u`

```
$ go env -u GOBIN
$
```

> Commands like `go install` apply within the context of the module containing the current working directory. If the working directory is not within the `example.com/user/hello` module, `go install` may fail.

像`go install`的命令将会应用范围是，模块的所有内容，这包含当前工作目录。如果工作目录不包含`example.com/user/hello`模块，那么`go install`将会失败。

> For convenience, `go` commands accept paths relative to the working directory, and default to the package in the current working directory if no other path is given. So in our working directory, the following commands are all equivalent:

为了方便，`go`命令接受使用 工作目录的相对路径 ，以及当没有指定路径时，包的路径将会默认为当前工作目录。因此，在我们的工作目录，下面的命令是等价的：

```
$ go install example.com/user/hello
$ go install .
$ go install
```

> Next, let's run the program to ensure it works. For added convenience, we'll add the install directory to our `PATH` to make running binaries easy:

接着，让我们运行程序，确认它能运行。为了更加方便，我们将安装目录添加到我们的`PATH`里，使运行二进制文件更简单：

```
# Windows users should consult https://github.com/golang/go/wiki/SettingGOPATH
# for setting %PATH%.
$ export PATH=$PATH:$(dirname $(go list -f '{{.Target}}' .))
$ hello
Hello, world.
$
```

> If you're using a source control system, now would be a good time to initialize a repository, add the files, and commit your first change. Again, this step is optional: you do not need to use source control to write Go code.

如果你有在使用代码控制系统，那么现在是一个很好的机会去初始化仓库，添加这些文件，然后提交。这里再次强调，上述的操作是可选的，也许你并不需要使用 代码控制 去管理Go代码。

```
$ git init
Initialized empty Git repository in /home/user/hello/.git/
$ git add go.mod hello.go
$ git commit -m "initial commit"
[master (root-commit) 0b4507d] initial commit
 1 file changed, 7 insertion(+)
 create mode 100644 go.mod hello.go
$
```

> The `go` command locates the repository containing a given module path by requesting a corresponding HTTPS URL and reading metadata embedded in the HTML response (see `go help importpath`). Many hosting services already provide that metadata for repositories containing Go code, so the easiest way to make your module available for others to use is usually to make its module path match the URL for the repository.

Go命令可以定位“内含给定模块地址的仓库”，定位原理是：通过发送一个对应的HTTPS地址，然后读取HTML response里的metadata内容 (详情看`go help importpath`)。对于包含Go代码的仓库，许多托管服务（hosting service）已经提供用于这种仓库的元数据，因此最简单的方式让你的仓库有效化的方式是，让你的模块地址与仓库的URL相匹配。

### Importing packages from your module

> Let's write a `morestrings` package and use it from the `hello` program. First, create a directory for the package named `$HOME/hello/morestrings`, and then a file named `reverse.go` in that directory with the following contents:

我们一起来写一个`morestrings`包吧！该包用到刚刚的 `hello` 程序。首先，创建一个该包的目录，就叫`$HOME/hello/morestrings`吧，然后在该目录下创建一个文件`reverse.go`，文件内容如下：

```
// Package morestrings implements additional functions to manipulate UTF-8
// encoded strings, beyond what is provided in the standard "strings" package.
package morestrings

// ReverseRunes returns its argument string reversed rune-wise left to right.
func ReverseRunes(s string) string {
	r := []rune(s)
	for i, j := 0, len(r)-1; i < len(r)/2; i, j = i+1, j-1 {
		r[i], r[j] = r[j], r[i]
	}
	return string(r)
}
```

> Because our `ReverseRunes` function begins with an upper-case letter, it is [exported](https://golang.org/ref/spec#Exported_identifiers), and can be used in other packages that import our `morestrings` package.

因为`ReverseRunes`函数的名字是大写开头，所以它将[被暴露](https://golang.org/ref/spec#Exported_identifiers)，也就是说，在其他包里导入`morestrings` 包后，我们可以使用这个函数。

> Let's test that the package compiles with `go build`:

我们来试试用`go build`来编译这个包

```
$ cd $HOME/hello/morestrings
$ go build
$
```

> This won't produce an output file. Instead it saves the compiled package in the local build cache.

这个操作不会产生任何输出文件，取而代之的是，该操作会酱编译好的包放到“本地构建缓存”里。

> After confirming that the `morestrings` package builds, let's use it from the `hello` program. To do so, modify your original `$HOME/hello/hello.go` to use the morestrings package:

在确认了`morestrings`包成功构建后了，我们在`hello`程序里用它吧。为了这样做，首先修改原来的文件`$HOME/hello/hello.go` ，去使用 morestrings 包

```
package main

import (
	"fmt"

	"example.com/user/hello/morestrings"
)

func main() {
	fmt.Println(morestrings.ReverseRunes("!oG ,olleH"))
}
```

> Install the `hello` program:

安装`hello`程序

```
$ go install example.com/user/hello
```

> Running the new version of the program, you should see a new, reversed message:

运行新版本程序，你可以看到一个新的、反转了的信息：

```
$ hello
Hello, Go!
```

### Importing packages from remote modules

> An import path can describe how to obtain the package source code using a revision control system such as Git or Mercurial. The `go` tool uses this property to automatically fetch packages from remote repositories. For instance, to use `github.com/google/go-cmp/cmp` in your program:

一个导入路径用于描述，怎么样通过Git或Mercurial等修订控制系统（Revision Control System），获得在包里的源代码。`go`工具使用导入路径（该属性）去自动获取远程仓库的包。举例说明，如果你想要在程序中`github.com/google/go-cmp/cmp`程序，就像下面代码：

```
package main

import (
	"fmt"

	"example.com/user/hello/morestrings"
	"github.com/google/go-cmp/cmp"
)

func main() {
	fmt.Println(morestrings.ReverseRunes("!oG ,olleH"))
	fmt.Println(cmp.Diff("Hello World", "Hello Go"))
}
```

> When you run commands like `go install`, `go build`, or `go run`, the `go` command will automatically download the remote module and record its version in your `go.mod` file:

当运行类似`go install`、`go build`、 `go run` 这些命令时， `go` 命令将会自动下载远程模块，并将其版本记录至`go.mod` 中：

```
$ go install example.com/user/hello
go: finding module for package github.com/google/go-cmp/cmp
go: downloading github.com/google/go-cmp v0.4.0
go: found github.com/google/go-cmp/cmp in github.com/google/go-cmp v0.4.0
$ hello
Hello, Go!
  string(
- 	"Hello World",
+ 	"Hello Go",
  )
$ cat go.mod
module example.com/user/hello

go 1.14

require github.com/google/go-cmp v0.4.0
$
```

> Module dependencies are automatically downloaded to the `pkg/mod` subdirectory of the directory indicated by the `GOPATH` environment variable. The downloaded contents for a given version of a module are shared among all other modules that `require` that version, so the `go` command marks those files and directories as read-only. To remove all downloaded modules, you can pass the `-modcache` flag to `go clean`:

模块的依赖被会自动下载到 `GOPATH`环境变量的目录下的`pkg/mod`的子目录里。下载的是模块指定版本的内容，其他模块可以共享该内容，但这些模块需要通过`require`指定版本，因此`go`命令把这些（下载了的）文件和目录都标记为只可读。（`require`的示例可参考[这里](https://github.com/kubernetes/minikube/blob/master/go.mod)）

```
$ go clean -modcache
$
```

## Testing

> Go has a lightweight test framework composed of the `go test` command and the `testing` package.

Go有一个轻型的测试框架，由 `go test`命令和`testing`包组成。

> You write a test by creating a file with a name ending in `_test.go` that contains functions named `TestXXX` with signature `func (t *testing.T)`. The test framework runs each such function; if the function calls a failure function such as `t.Error` or `t.Fail`, the test is considered to have failed.

测试文件名字后缀为`_test.go` ，文件包含名为 `TestXXX`函数且签名（类型签名：Type signature）为 `func (t *testing.T)`。该测试框架能运行上述的函数；当函数调用失败函数（如：`t.Error` 、 `t.Fail`），测试结果会被认为失败。

> Add a test to the `morestrings` package by creating the file `$HOME/hello/morestrings/reverse_test.go` containing the following Go code.

在 `morestrings` 包里添加一个测试文件`$HOME/hello/morestrings/reverse_test.go`，内容如下：

```
package morestrings

import "testing"

func TestReverseRunes(t *testing.T) {
	cases := []struct {
		in, want string
	}{
		{"Hello, world", "dlrow ,olleH"},
		{"Hello, 世界", "界世 ,olleH"},
		{"", ""},
	}
	for _, c := range cases {
		got := ReverseRunes(c.in)
		if got != c.want {
			t.Errorf("ReverseRunes(%q) == %q, want %q", c.in, got, c.want)
		}
	}
}
```

> Then run the test with `go test`:

运行`go test`进行测试

```
$ go test
PASS
ok  	example.com/user/morestrings 0.165s
$
```

> Run `go help test` and see the [testing package documentation](https://golang.org/pkg/testing/) for more detail.

更多详情可运行 `go help test` 和查看[testing package documentation](https://golang.org/pkg/testing/)

## What's next

Subscribe to the [golang-announce](https://groups.google.com/group/golang-announce) mailing list to be notified when a new stable version of Go is released.

See [Effective Go](https://golang.org/doc/effective_go.html) for tips on writing clear, idiomatic Go code.

Take [A Tour of Go](https://tour.golang.org/) to learn the language proper.

Visit the [documentation page](https://golang.org/doc/#articles) for a set of in-depth articles about the Go language and its libraries and tools.

## Getting help

For real-time help, ask the helpful gophers in the community-run [gophers Slack server](https://gophers.slack.com/messages/general/) (grab an invite [here](https://invite.slack.golangbridge.org/)).

The official mailing list for discussion of the Go language is [Go Nuts](https://groups.google.com/group/golang-nuts).

Report bugs using the [Go issue tracker](https://golang.org/issue).