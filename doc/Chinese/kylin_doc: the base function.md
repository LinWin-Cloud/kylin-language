# 基础可调用函数
```
"getTime",                  // 0
"input",                    // 1
"get_os",                   // 2
"get_path",                 // 3
"long_time",                // 4
"bool",                     // 5
"typeof",                   // 6
"file_exists",              // 7
"get_file_content",         // 8
"java_runtime",             // 9
"length",                   // 10
"pow",                      // 11
"sub",                      // 12
"index",                    // 13
"lastindex",                // 14
"delete",                   // 15
"get_pointer",              // 16
"toVal",                    // 17
"shell_output",             // 18
"new_thread",               // 19
"get_mouse_point",          // 20
"toInt",                    // 21
"rm",                       // 22
```

### getTime
获取当前时间
> print(getTime())  //输出时间，精确到秒

### input
获取控制台输入的内容
> var a = input("输入内容: ")

### get_os
获取系统名称
> print(get_os())

### get_path
获取当前脚本的执行路径（不是脚本所在路径）
> print(get_path())

### long_time
获取当前的系统时间，但是以Java Long形式返回
> print(long_time())

### bool
判断是否是 布尔 类型
> print(bool(1 > 2))                    // false
> 
> print(bool("hello" == "hello"))       // true
> 
> print(bool(1 != 2))                   // true

### typeof
获取一个值的类型，除了基本类型以外，要么就是自己定义的class类型
> var a = 1
> 
> print(typeof(a))          // num

### file_exists
判断文件是否存在
> print(file_exists("1.txt"))             //存在返回 true ， 不存在返回 false

### java_runtime
获取内置的Java运行环境
> print(java_runtime())

### length
获取数组或者是字符串的长度
> var a = list(1,2,3)
> 
> print(length(a))              // 3
>
> var b = "hello"
> 
> print(length(b))              // 5

### pow
计算N次方的
> print(pow(2 , 3))             // 8

### sub
截取字符串
> var a = "hello"
> 
> print(sub(a , 0,1))           // 截取索引 0 到 1 的字符 也就是 "h"

### index
在字符串内索引指定字符
> var a = "hello"
> 
> print(index(a , "h"))         // 0

### lastindex
在字符串内索引最后一个出现的字符
> var a = "hello"
> 
> print(index(a , "h"))         // 0

### delete 或者是 rm
删除文件或者文件夹
> delete("1.txt")
>
> rm ("1.txt")

### get_pointer
获取一个变量的 指针 , 指针是 kylin 3.0加入的特性，用于全局变量，通过全局变量修改变量，
可以无视 传统 kylin语言最高只能获取上一个运行环境中的变量的特性，可以直接获取全局的所有变量
> var a = 1
> 
> var b = get_pointer(a)  // 是一个num类型数字
> 
> var c = toVal(b)        // 获取变量

### toVal
通过 指针获取变量
```
var a = 1
var b = get_pointer(a)  // 是一个num类型数字
var c = toVal(b)        // 获取变量
```

### shell_output
执行 系统 shell指令并获取他们的 输出
```dtd
var a = shell_output("ls ./") //获取输出
```

### new_thread
新建线程 , 获取的变量是一个指针
```dtd
func a() public
    print(1)
end_func

var a = new_thread(a()) //获取的是一个指针
kill_thread(a)
```

### toInt
将数字对象转换成整形数字
```dtd
var a = "1.0"
print(toInt(a)) // 1
```

### randomInt
生成一个制定范围的 number 类型数字
```dtd
// 生成 100 - 1000 的数字
print(randomInt(100, 1000))
```