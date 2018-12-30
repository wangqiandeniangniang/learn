
## 1、构建Dubbo服务消费者Web应用的war包
### 1.1、打包类型：war


### 1.2、包含的配置文件


### 1.3、依赖到的jar包（相关工程先构建）
````shell
  <build>
        <finalName>edu-web-boss</finalName>
        <resources>
            <resource>
                <targetPath>${project.build.directory}/classes</targetPath>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>
            </resource>
        </resources>
    </build>
````

### 1.4、构建war包

### 1.5、部署到linux 上

安装jdk、tomcat、配置iptables 放开端口8080