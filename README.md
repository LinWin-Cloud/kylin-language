# KyLin 编程语言Jvm版本
###### 当前版本: 1.0 Preview
KyLin 是一款基于Jvm的跨平台高效的编程语言。面相对象和面向过程都是可以的，直译式编程语言
其特点就是快速高效简单，语法设计降低或者避免```写出像shit一样的代码```，代码就像诗一样优美
  
同样，其设计成函数式，而且避免套嵌的写法，完全利用函数的调用，代码可读性十分的高，而且运行安全，基于Jvm，jvm有强大的内存安全管理机智

从功能和一些设计上是对标Python的，但是修改了Python一些不太合理的地方，同样借鉴了各个编程语言，修复了这些编程语言的不足之处。

在 kylin 3.0 标准当中加入了指针特性，并且是一个重要的特性，不过这个指针是安全的，并且可以无视kylin标准变量
中最高可调用当前函数运行环境下只能够获取到上一个运行环境中的变量

## 发行版 https://gitee.com/LinwinSoft/kylin_ke/releases
## Kylin专用jdk: https://gitee.com/LinWin-Cloud/

## 启动Kylin Jvm
> # 先进入源代码目录

> ./kylin [操作]

> ./kylin #可以查看帮助

> ./kylin ./1.ky

## Kylin支持其他编程语言内嵌运行
1. Java

## 关于Kylin Programing Language.
Kylin是一款高级编程语言，其目的是为了解决现如今很多的编程语言语法不够简单，或者是简单的编程语言不够强大
不能够让人能够快速理解的代码，其主要面向Web服务器、Web爬虫、大量的系统IO操作、自动化的游戏操作、和各种、
编程语言交互运行。

你应该选择kyLin，不仅仅是因为他适合初学者，更重要的是，他的功能更加强大，他对标Python，在网页服务器方面
KyLin可以超过Python甚至接近c++

##  Kylin Jvm
### 关键字
1. ```if```
2. ```else```
3. ```#func```
4. ```#defined```
5. ```var```
6. ```new()```
7. ```func```
8. ```f```
9. ```e_f```
10. ```end_func```
11. ```err```
12. ```e_err```
13. ```for```
14. ```while```
15. ```import```
16. ```class```
17. ```end_class```
18. ```private```
19. ```public```

### 内存溢出测试
#### 无限递归
```
// Kylin代码
func test() public
	test()
end_func
```
1. C (GCC) ❌
2. GO (GCC) ❌
3. Javascript ❌
4. Kylin Go ❌
5. Kylin Jvm (存活时间较长)  ❌ 

## Kylin Go
这个是```Kylin 编程语言``` 的一个分支项目，基于Go编写的解释器
https://github.com/zmh-program/kylin-go/

## 文档:-docx
### 文档链接:
1. <a href='https://gitee.com/LinwinSoft/kylin_ke/tree/master/doc/Chinese'>中文文档</a>

## 函数式编程
1. 函数
Kylin编程语言非常推荐开发者采用函数的形式等编写功能
```
`
//这是一个标准的函数定义
func main() public
	out("hello world")
end_func

```
```

//这是一个化简的函数定义，匿名内部类
f main()
	out(in) //输出in
e_f

```

2. 利用函数的代码规范

```
//main函数循环1000次
for(main() , 1000)  
```

## 代码语法特点
1. ```语法设计避免写出shit一样的代码```
2. ```代码可读性很高，配合着内置的中文库，读代码就像读文章一样```
3. ```代码像诗歌一样优美，语法设计尽量避免任何套嵌```

### 借鉴了多个编程语言的语法
1. Visual Basic
2. Python
3. C / C++
4. Java
5. HTML
6. CSS
7. Javascript
8. SQL
9. Linux Shell
10. Go

## 基于jvm的生态
如果是kylin-jvm,那么KyLin编程语言同样是支持一些Java的类库的，就是说一些编程语言的类库可以直接调用Java的。

## 目录结构
### src ---源代码
#### KylinException	---处理异常的代码
#### main ---启动和主要参数
#### Program ---KyLin运行环境主要代码

### out ---输出的class
### bin ---存放真正成型的jar和启动文件
### head ---自定义头文件
### kpt ---kpt包管理器
