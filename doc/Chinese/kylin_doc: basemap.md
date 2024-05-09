# basemap 数据集合
basemap是一个类似于Java的treemap,python dict的一个数据集合系统。用于存储key value对应数据的系统。

- basemap 最高存储 2^31-1 对 key-value
- basemap 算法纯kylin编程语言实现
- basemap 是有序的
- basemap 可以将 kylin 数据结构转换成 json.

1. add(key , value) 方法
向map插入 张三-13 , 李四-14
```
import "basemap"
var map = new(basemap)
map.add("张三" , 13)
map.add("李四" , 14)
```

1. get(key) 方法
从 map中获取 张三
```
import "basemap"
var map = new(basemap)
map.add("张三" , 13)
map.add("李四" , 14)

# 获取数据
print(map.get("张三"))
```

1. to_str() 方法
将 basemap 数据结构转换成字符串
```
import "basemap"
var map = new(basemap)
map.add("张三" , 13)
map.add("李四" , 14)

#转换成 字符串
print(map.to_str()) 

# 输出的 字符串
# {张三=13 , 李四=14}
```

1. to_json() 方法
将 basemap 数据结构转换成 json
```
import "basemap"
var map = new(basemap)
map.add("张三" , 13)
map.add("李四" , 14)

#转换成 字符串
print(map.to_json()) 
# 输出的 json结构
# {
#    "张三" : "13",
#    "李四" : "14"
# }
```

