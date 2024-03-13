
# Kylin 4.4
- 优化正则式处理，采用预编译，性能提高 10% 左右
- app_output 内置函数,允许与外部应用程序通讯
- s_out 内置函数,允许通过流的形式与外部程序发送消息
- exec 内置执行kylin代码的函数

# Kylin 4.3
- http反向代理库: http_proxy，专门用于反向代理网站和服务器，用于隐秘渗透测试
- DDOS库: kylin实现，可以对目标进行dos攻击，配合着多台计算机可以实现ddos.
- is_app_install("python3")函数api: 检查指定命令行程序时候安装
- kpt包管理系统
- base_physics:基础物理公式库
- file api更新: 支持创建文件和目录等.

# Kylin 4.0
### bug修复
- 修复for内置函数BUG
- 修复表达式、列表表达式bug

### 新增内容
- del 关键字, 删除变量
- basemap 基础集合(纯kylin内部库实现)
- 列表操作api更新
- 基础桌面程序调用api(浏览器)
- http request api,允许请求其他服务器
- 更改特性 ```只能获取本环境与父环境的变量和函数```，变更为可以获取全局的变量和函数.
- 允许表达式处理对象
- 更新 kylin命令
- kylin basemap数据结构转 json结构api支持
- 更新kylin结构，支持自定义jdk版本和内容

# Kylin3.2
- ref关键字，变量可以共享指针,修改一个指针可以
- 新增 randomInt 可以生成指定范围的number类型数字
- 修复 ```路径错误``` BUG
- 修复 ```HttP启动失败``` BUG    
- 修复 ```Class 对象无修改``` BUG
- 优化性能, 提升 3%

# kylin 3.1
- 修复 ```空指针异常``` BUG
- 修复 ```HTTP SERVER``` BUG
- 更新对windows启动文件支持

# Kylin 3.0
- 指针系统
- 变量BUG修复
- 函数BUG修复
- 加入列表操作
- 修复列表BUG
- 修复运行环境BUG
- 修复Java产品调用BUG
- 更新 Java源代码调用库
- 加入键盘控制内置函数
- 剔除对中文编程支持
- 性能大幅度提升
- 剔除 #func 和 #defined 语句
- 精简指令集，更加数据操作和系统操作
- 加入线程系统
- 加入对象处理系统
- 更新指令集，内部很多函数与方法加入指针


