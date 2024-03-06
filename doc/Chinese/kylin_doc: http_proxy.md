# http_proxy
这个类库提供http反向代理服务器的功能

- 这个程序示例是反向代理必应.
```
import "http_proxy"

var h = new(http_proxy)
h.set_port(8080)
h.set_target("https://www.bing.com/")
h.run()
```