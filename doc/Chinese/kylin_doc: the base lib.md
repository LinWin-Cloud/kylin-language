# 基础类库

### io.file 或者是 file
提供文件操作的一些方法
1. set_path(path)
2. exists()
3. get_content()

> // 在使用这个类库之前必须调用 set_path输入要操作的文件路径

> import "file"

> var f = new(file)

> f.set_path("./")

> print(f.exists())             // 检查文件或者文件夹是否是否存在

> print(f.get_file_content())   // 获取文件的内容，如果得不到会报错

### net.http.linwinshs.httpserver 或者是 httpserver
提供Web网页服务器方法
1. set_port(in_port)
2. set_ip(in_ip)
3. set_path(in_path)
4. start()

> import "httpserver"

> var hs = new(Httpserver)      

> hs.set_port(8080)             

> hs.set_ip("0.0.0.0")         

> hs.set_path("/")              

> hs.start()                    

### util.math 或者是 math
提供数学计算方法
1. add(x,y)
2. multiply(x,y)
3. subtract(x,y)
4. division(x,y)

> import "math"

> print(math.add(1,1))      // 2

> print(math.multiply(2,2)) // 0

> print(math.subtract(2,2)) // 4

> print(math.division(2,2)) // 1

### util.java.load_class
调用运行Java Jar包
> import "util.java.load_class"

> load_class.run_jar("./1.jar", "1 2 3")        // 运行 1.jar ，并输入命令行1 2 3


### java
可以直接运行java源代码的库
```dtd
import "java"
// 运行 HelloWorld.java 文件
java.exec_java("./HelloWorld.java")
```