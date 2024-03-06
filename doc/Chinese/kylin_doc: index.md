# Kylin编程语言
Kylin 是一款基于Jvm的直译式编程语言，是一款解释性编程语言，可实现跨平台运行，支持多种编程语言内嵌。函数式编程语言，高效快捷，适合Web服务器，网页服务，多线程操作，系统运维操作等。

- kylin编程语言设计者有硬性设计要求就是大多数类库用户开发时间必须控制在 4分钟以内.设计就是 简洁 + 高效
- 通过阅读 readme.md 之后执行以下操作

1. 新建 ky 脚本
- Windows
> echo '' > hello.ky

- Linux or Mac
> touch hello.ky

2. 执行kylin脚本
- Windows(进入源代码目录): 
> ./kylin.bat ./hello.ky  

- Linux or Mac (安装完成的设计)
> kylin ./hello.ky

3. 你的第一个kylin程序: Hello World
> print("Hello World")