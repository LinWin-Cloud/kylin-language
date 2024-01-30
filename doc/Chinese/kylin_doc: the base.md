# 基础语法


> 输入命令: kylin [源代码路径] 来运行
[注意] Kylin不允许进行同等关键字的套嵌，如
```
func a() public
func b() public
...
end_func
end_func
```
这种是不可以的

1.定义变量
```
var a = 1							//返回 “1”W
var b = “hello world”			    //返回 ”hello world”
var c = 1+1 						//返回 “2”
var d = “hello” , “world”		    //返回 “helloworld”
var e = “result: ” , 1+1   	        //返回 “result: 2”
var f = “result: ” , a+a   		    //返回 “result: 2”
```


2.kylin的数据类型
-- num   							数字型，进行数学运算的
-- string							字符串类型
-- obj								对象类型，主要为各种对象的
-- bool								布尔类型，true和false


3.修改变量
直接重新 var a = 2
或者是 a = 2


4.关键字
```
#include<{head}/chinese.kyh> 		导入默认的中文编程定义
{head}/chinese.kyh可修改，{head}是编程语言默认的头文件库
```

```
#func out print
```
定义out函数为print,例如 原本out(“hello world”)，现在可以直接
print(“hello world”)

```
func a() public
    // public可修改，也可以改成 private，public公共函数,private内部函数
    //在控制台内输出 hello world
    out(“hello world”)
end_func

f b()
//这也是定义函数的方法，但是默认是内部函数
out(“这是else语句”)
e_f

//当 true时候运行a()
if (true) a()  
else b()

```


5.函数定义

```
func a(test) public
// public可修改，也可以改成 private，public公共函数,private内部函数
//在控制台内输出 输入变量 test
out(test)
end_func

f b()
//这也是定义函数的方法，但是默认是内部函数
out(“这也是函数定义方法”)
e_f

a(“hello world”) //输入test

func c()
return “hello world”
end_func
//调用显示函数c()的内容，将会输出hello world
out(c())
```

6.异常处理
```
f a() 
//写入文件
write(“1.txt” , “hello kylin” ,false)
e_f

err b(e)
//输出错误信息
out(e.message)
e_err
//当a()出现错误的时候运行b()
exception(a() , b())
```

这是基础的语法，kylin很简单，你看两眼文档就会了。