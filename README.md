jfinal-ext-rich
jfinal-ext-rich是对java极速web框架jfinal (https://github.com/jfinal/jfinal) 的功能扩展。扩展的功能主要是自己项目中用到 的，有部分工具类是来自https://github.com/b1412/jfinal-ext。

Plugin扩展<br/>
<h/>
DisruptorPlugin<br/>
基于LMAX Disruptor的扩展，用于异步并发事件编程<br/>
用法：<br/>
MyConfig中<br/>
DisruptorPlugin disruptorPlugin = new DisruptorPlugin(1024);<br/>
disruptorPlugin.add(BlogService.class);<br/>
me.add(disruptorPlugin);<br/>
<br/>
BlogService中<br/>
@Subscribe("saveMe")<br/>
public void saveBlog(Blog blog){<br/>
}<br/>

BlogController中<br/>
DisruptorKit.post("saveMe", blog);<br/>
<br/>
<h/>
RedisPlugin<br/>
基于Redis的扩展,jfinal官方的没出来，就自己先弄个勉强用着<br/>
<br/>
<h/>
ZmqPlugin<br/>
基于ZeroMQ的扩展<br/>
<br/>
<h/>
InjectInterceptor<br/>
虽然有SprintPlugin，但依赖的包太多了，所以自己弄了个简单的代替<br/>
<br/>
<h/>
Validator<br/>
重写了Validator，主要加上对validateRequired(int index, String errorKey, String errorMessage)之类的验证
<br/>
<h/>