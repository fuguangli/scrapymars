# 这可能是目前 使用最简单的功能强大的java爬虫框架  

## 特征：可使用性、高扩展性、功能强大  

## 宗旨：站在开发者的角度，让使用变得更简单
1. 软件与spring框架完美契合，配置成bean，即是插拔式服务
2. 主要面向接口编程，扩展性好，你可以根据自己的需求进行扩展
3. 软件内置的下载器集成了js引擎
4. 软件内置的下载器已经集成了代理，根据需求配置代理即可
5. 软件内置了定时器，可以用作任务放入系统

## 作者：fuguangli

## 联系方式：businessfgl@163.com

# 软件的使用

#### 一、安装

##### (1)核心依赖：

            <dependency>
                <groupId>net.sourceforge.htmlunit</groupId>
                <artifactId>htmlunit</artifactId>
                <version>2.28</version>
            </dependency>
            <dependency>
                <groupId>org.apache.httpcomponents</groupId>
                <artifactId>httpclient</artifactId>
                <version>4.5.2</version>
            </dependency>
            <dependency>
                <groupId>org.apache.httpcomponents</groupId>
                <artifactId>httpcore</artifactId>
                <version>4.4.5</version>
            </dependency>
            <dependency>
                <groupId>org.apache.httpcomponents</groupId>
                <artifactId>httpcore-nio</artifactId>
                <version>4.4.5</version>
            </dependency>
            <dependency>
                <groupId>org.apache.httpcomponents</groupId>
                <artifactId>httpasyncclient</artifactId>
                <version>4.1.2</version>
            </dependency>
            <dependency>
                <groupId>org.jsoup</groupId>
                <artifactId>jsoup</artifactId>
                <version>1.11.2</version>
            </dependency>
##### (2)将源代码打包成jar放入系统的依赖

#### 二、举例
    <bean id="downloader" class="com.automata.downloader.Downloader" init-method="initClient">
            <property name="downloaderSelect" value="2"/>
            <property name="timeout" value="10000"/>
            <!--<property name="downloadWares">
                <list>
                    <bean class="sample.downloadwares.HeaderWare"></bean>
                </list>
            </property>-->
            <!--<property name="proxyWare">
                <bean class="com.automata.downwares.ProxyWare">
                    <property name="proxyHost" value=""/>
                    <property name="proxyPort" value=""/>
                    <property name="proxyUser" value=""/>
                    <property name="proxyPass" value=""/>
                </bean>
            </property>-->
        </bean>
    
        <bean id="internalUrlQueue" class="com.automata.queues.internal.InternalUrlQueue">
        </bean>
    
        <bean id="internalResponseQueue" class="com.automata.queues.internal.InternalResponseQueue">
        </bean>
    
        <bean id="threadPools" class="com.automata.threads.ThreadPools">
            <constructor-arg index="0" value="5"></constructor-arg>
            <constructor-arg index="1" value="20"></constructor-arg>
            <constructor-arg index="2" value="5"></constructor-arg>
        </bean>
    
        <bean id="parser" class="sample.parser.SampleParser"></bean>
    
    
        <bean id="onceScheduler" class="com.automata.schedule.mode.OnceScheduler">
            <property name="downloader" ref="downloader"/>
            <property name="responseParser" ref="parser"/>
            <property name="startUrls">
                <list>
                    <value>https://github.com</value>
                </list>
            </property>
        </bean>
        <bean id="oneCycleScheduler" class="com.automata.schedule.mode.OneCycleScheduler">
            <property name="downloader" ref="downloader"/>
            <property name="responseParser" ref="parser"/>
            <property name="startUrls">
                <list>
                    <value>https://github.com</value>
                    <value>https://stackoverflow.com/</value>
                </list>
            </property>
            <property name="threadPools" ref="threadPools"/>
            <property name="pollWaitSeconds" value="30"/>
            <property name="urlQueue" ref="internalUrlQueue"/>
            <property name="responseQueue" ref="internalResponseQueue"/>
        </bean>
        <bean id="alwaysScheduler" class="com.automata.schedule.mode.AlwaysScheduler">
            <property name="downloader" ref="downloader"/>
            <property name="responseParser" ref="parser"/>
            <property name="startUrls">
                <list>
                    <value>https://github.com</value>
                    <value>https://stackoverflow.com/</value>
                </list>
            </property>
            <!--程序执行间隔时间-->
            <property name="repeatIntervalSeconds" value="5"/>
            <property name="threadPools" ref="threadPools"/>
            <property name="pollWaitSeconds" value="10"/>
            <property name="urlQueue" ref="internalUrlQueue"/>
            <property name="responseQueue" ref="internalResponseQueue"/>
        </bean>
    
        <bean id="engine" class="com.automata.engine.ScrapyEngine" init-method="start">
            <!--如果你只想执行一次程序，请使用：-->
            <!--<property name="scheduler" ref="onceScheduler"/>-->
            <!--如果你只想执行一次程序，但在解析过程中又需要新增访问的URL，请使用：-->
            <!--<property name="scheduler" ref="oneCycleScheduler"/>-->
            <!--如果你想使用程序内置的定时器，不断执行程序，请使用：-->
            <property name="scheduler" ref="alwaysScheduler"/>
        </bean>
##### 如果你没有代理、处理url等需求，那么你仅需要实现<code>com.automata.parser.IResponseParser</code>接口进行解析
##### 如果你的系统没有使用spring，请参考以下代码进行初始化：

    List<String> startUrls=new ArrayList<>();
    startUrls.add("https://github.com");
    Downloader downloader = new Downloader();
    downloader.initClient();
    scheduler = new OnceScheduler();
    scheduler.setDownloader(downloader);
    scheduler.setStartUrls(startUrls);
    scheduler.scheduled();


###### _*温馨提示：请遵守网站的robots协议_
###### 软件完全开源，制作不易，如果你想支持作者的话：
![谢谢您的支持](https://thumbnail0.baidupcs.com/thumbnail/246b699f3q2103e16972c34eec97f4d8?fid=4011447265-250528-978965418075103&rt=pr&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-I%2b%2btJJ9uFtWYCgaKa6QNV%2bVnXIA%3d&expires=8h&chkbd=0&chkv=0&dp-logid=8387032185978574825&dp-callid=0&time=1577512800&size=c600_u300&quality=100&vuk=4011447265&ft=image&autopolicy=1)
![谢谢您的支持](https://thumbnail0.baidupcs.com/thumbnail/10a4a889bp90b18f99d33f0ad1d012ff?fid=4011447265-250528-540442316933461&rt=pr&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-BIzIW4%2fFQp%2fptQQqpsDrGeesuWs%3d&expires=8h&chkbd=0&chkv=0&dp-logid=8387256762023794383&dp-callid=0&time=1577516400&size=c600_u300&quality=100&vuk=4011447265&ft=image&autopolicy=1)

###### 感谢您的支持！
