jfinal-ext-rich
jfinal-ext-rich是对java极速web框架jfinal (https://github.com/jfinal/jfinal) 的功能扩展。扩展的功能主要是自己项目中用到 的，有部分工具类是来自https://github.com/b1412/jfinal-ext。

Plugin扩展
==================================================================
DisruptorPlugin
基于LMAX Disruptor的扩展，用于异步并发事件编程
用法：
MyConfig中
DisruptorPlugin disruptorPlugin = new DisruptorPlugin(1024);
disruptorPlugin.add(BlogService.class);
me.add(disruptorPlugin);

BlogService中
@Subscribe("saveMe")
public void saveBlog(Blog blog){
}

BlogController中
DisruptorKit.post("saveMe", blog);

==================================================================
RedisPlugin
基于Redis的扩展,jfinal官方的没出来，就自己先弄个勉强用着

==================================================================
ZmqPlugin
基于ZeroMQ的扩展

==================================================================
InjectInterceptor
虽然有SprintPlugin，但依赖的包太多了，所以自己弄了个简单的代替

==================================================================
Validator
重写了Validator，主要加上对validateRequired(String field, String errorKey, String errorMessage)之类的验证

==================================================================

