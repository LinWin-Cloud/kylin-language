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
> print(bool("hello" == "hello"))       // true
> print(bool(1 != 2))                   // true

### typeof
获取一个值的类型，除了基本类型以外，要么就是自己定义的class类型
> var a = 1
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
> print(length(a))              // 3
>
> var b = "hello"
> print(length(b))              // 5

### pow
计算N次方的
> print(pow(2 , 3))             // 8

### sub
截取字符串
> var a = "hello"
> print(sub(a , 0,1))           // 截取索引 0 到 1 的字符 也就是 "h"

### index
在字符串内索引指定字符
> var a = "hello"
> print(index(a , "h"))         // 0

### lastindex
在字符串内索引最后一个出现的字符
> var a = "hello"
> print(index(a , "h"))         // 0

### delete
删除文件或者文件夹
> delete("1.txt")
