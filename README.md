# kylin 编程语言
kylin 是一款基于Jvm的跨平台高效的编程语言。可使用中文进行编程。面相过程，直译式编程语言
其特点就是快速高效简单，语法设计降低或者避免```写出像shit一样的代码```，代码就像诗一样优美


## Kylin支持多种其他编程语言内嵌运行
1. Python
2. javascript
3. node.js 	(需要通过官方源安装)
4. Java

## 多范式编程
1. 函数
Kylin编程语言非常推荐开发者采用函数的形式等编写功能
```

//这是一个标准的函数定义
func main() -> public
	out("hello world")
end_func

```
```

//这是一个化简的函数定义，匿名内部类
f main = (in)
	out(in) //输出in
end_f

```
2. 命令式
```
var a = [1, 2 , 3]
a.add(4)

```
3. 声明式
```

var a = ky.sh("select * from /home/")

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