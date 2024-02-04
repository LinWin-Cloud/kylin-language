# 内置运行可调用函数
```
"out",
"print",
"for",
"shell",
"exception",
"while",
"write",
"throw_error",
"exit"
```

### out 和 print
这两个函数功能是一样的，是为了兼容 kylin-go 的
> out("hello world")
> print("result: " , 1+1)

### for
在一个范围内重复执行某个函数
```
func main() public
    print(1)
end_func

for(main() , 1000)                     //执行 1000 次 main() 函数
```

```
func main() public
    print(1)
end_func

for(main() , 1000 , true)              //执行 1000 次 main() 函数，开启多线程优化
```

### shell
调用操作系统命令
> shell("echo hello world")            //输出 hello world

### exception
异常处理代码
```
err err_code(e)
    // e是异常处理码
    print(e.message) //获取错误信息
e_err

f a()
    write("1.txt" , "hello world", false) //写入一个文件
e_f

exception(a() , err_code())
```

### while
循环执行一个函数
> while(true ,print(1))

### write
将内容写入一个文件
> write("1.txt" , "hello world" , false)   // 覆盖写入
> write("1.txt" , "hello world" , false)   // 追加写入

### throw_error
抛出一个异常
> throw_error("异常错误")

### exit
退出程序
> exit(1) // 错误退出码 1
> exit(0) // 退出并返回 0