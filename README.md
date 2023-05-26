# kylin 编程语言
kylin 是一款基于Jvm的跨平台高效的编程语言。可使用中文进行编程。面相过程，直译式编程语言



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

