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
        
##### 测试用例请参考：<code>SpringTest</code>
##### 如果你没有代理、处理请求头等需求，那么你仅需要实现<code>com.automata.parser.IResponseParser</code>接口进行解析
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
![谢谢您的支持](https://picabstract-preview-ftn.weiyun.com/ftn_pic_abs_v3/c979a64a60a15bc09f3e7c8d8a2d66daaad1fb830c7fd9f5f99e94402f32cc6eabd38f54abef753ac9a8540b09b4a8ba?pictype=scale&from=30013&version=3.3.3.3&uin=1830724319&fname=2019-12-28%20145059.jpg&size=300)
![谢谢您的支持](https://picabstract-preview-ftn.weiyun.com/ftn_pic_abs_v3/00880264f187e8902116a53e683146a14d40bdf64694aa5a6a04ab01ee4a0b1c5d06a63cb7402298b9c8374e2fb07e1f?pictype=scale&from=30013&version=3.3.3.3&uin=1830724319&fname=2019-12-28%20150131.jpg&size=300)

###### 感谢您的支持！
