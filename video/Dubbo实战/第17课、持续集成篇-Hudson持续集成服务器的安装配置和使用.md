## 1、安装JDK并配置环境变量（略）
JAVA_HOME=/usr/java/jdk1.7.0.80

## 2、Maven本地仓库的安装（使用maven 作为项目构建与管理工具）
- 下载maven-3.0.5
````
# wget http://mirrors.hust.edu.cn/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
````
- 解压
````
# tar -zxvf apache-maven-3.0.5-bin.tar.gz
# mv apache-maven-3.0.5 maven-3.0.5
````
- 配置Maven环境变量
````
# vi /etc/profile
内容如下
## maven env
export MAVEN_HOME=/root/maven-3.0.5
export PATH=$PATH:$MAVEN_HOME/bin

# source /etc profile
````

- Maven 本地库配置：
````xml
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
	<localRepository>/root/maven-3.0.5/.m2/repository</localRepository>
	<interactiveMode>true</interactiveMode>
    <offline>false</offline>
    <pluginGroups>
        <pluginGroup>org.mortbay.jetty</pluginGroup>
        <pluginGroup>org.jenkins-ci.tools</pluginGroup>
    </pluginGroups>

	<!--配置权限,使用默认用户-->
	<servers>
		<server>
			<id>nexus-releases</id>
			<username>deployment</username>
			<password>deployment123</password>
		</server>
		<server>
			<id>nexus-snapshots</id>
			<username>deployment</username>
			<password>deployment123</password>
		</server>
	</servers>

    <mirrors>

    </mirrors>

	<profiles>
		<profile>
			<id>edu</id>
			<activation>
				<activeByDefault>false</activeByDefault>
				<jdk>1.6</jdk>
			</activation>
			<repositories>
				<!-- 私有库地址-->
				<repository>
					<id>nexus</id>
					<url>http://localhost:8081/nexus/content/groups/public/</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</repository>
			</repositories>
			<pluginRepositories>
				<!--插件库地址-->
				<pluginRepository>
					<id>nexus</id>
					<url>http://localhost:8081/nexus/content/groups/public/</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
				   </snapshots>
				</pluginRepository>
			</pluginRepositories>
		</profile>

		<profile>
			<id>sonar</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<!-- Example for MySQL-->
				<sonar.jdbc.url>
					jdbc:mysql://localhost:3306/sonarqube?useUnicode=true&amp;characterEncoding=utf8
				</sonar.jdbc.url>
				<sonar.jdbc.username>root</sonar.jdbc.username>
				<sonar.jdbc.password>root</sonar.jdbc.password>

				<!-- Optional URL to server. Default value is http://localhost:9000 -->
				<sonar.host.url>
					http://localhost:9090/sonarqube
				</sonar.host.url>
			</properties>
		</profile>

	</profiles>

	<!--激活profile-->
	<activeProfiles>
		<activeProfile>edu</activeProfile>
	</activeProfiles>

</settings>

````
## 3、配置HudsonHome, 在/root目录下创建HudsonHome目录，并配置到环境变量
````
# mkdir HudsonHome

切换root用户， 在/etc/profile中配置全局环境变量
# vi /etc/profile

## hudson env
export HUDSON_HOME=/root/HudsonHome

# source /etc/profile
````

## 4、下载最新版Tomcat7
````
# wget  http://apache.fayea.com/tomcat/tomcat-7/v7.0.59/bin/apache-tomcat-7.0.59.tar.gz
````

## 5、解压安装Tomcat
````
# tar -zxvf apache-tomcat-7.0.59.tar.gz
# mv apache-tomcat-7.0.59 hudson-tomcat

移除 /root/hudson-tomcat/webapps目录下所有文件
# rm -rf /root/hudson-tomcat/webapps/*

将Tomcat容器的编码设置为UTF-8

# vi /root/hudson-tomcat/conf/server.xml

<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" URIEncoding="UTF-8"/>

设置hudson-tomcat的内存
# vi /root/hudson-tomcat/bin/catalina.sh
#!/bin/sh 下面添加：
JAVA_OPTS='-Xms512m -Xmx2048m'
````
## 6、下载最新版的Hudson(3.2.2版)
````
wget http://mirror.bit.edu.cn/eclipse/hudson/war/hudson-3.2.2.war

将 war包拷贝到hudson-tomcat/webapps目录， 并重命名为hudson.war
# cp /root/hudson-3.2.2.war /root/hudson-tomcat/webapps/hudson.war
````
## 7、防火墙开启8080端口，用root用户修改/etc/sysconfig/iptables
````
# vi /etc/sysconfig/iptables
增加
## hudson-tomcat port:8080
-A INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT

重启防火墙
# service iptables restart
````
## 8、 设置hudson-tomcat开机启动：
在虚拟机中编辑 /etc/rc.local文件。
````
# vi /etc/rc.local
加入
/root/hudson-tomcat/bin/startup.sh

````
## 9、 启动hudson-tomcat
````
# /root/hudson-tomcat/bin/startup.sh
````
## 10、配置hudson
````
http://192.168.230.131:8080/hudson/
````