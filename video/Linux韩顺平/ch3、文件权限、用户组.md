## 1、文件权限

[文件权限](img/文件权限.png)

## 1.1、查看linux所有组的信息
- cat /etc/group

## 1.2、创建用户，并同时制定将该用户分配到那个组
- useradd -g 组名 用户名

## 1.3、查看linux中所有者用户信息
- cat /etc/passwd

## 1.4、修改权限
- chmod 777 文件名
- chmod 755 abc : 赋予abc权限rwxr-xr-x
- chmod u=rwx ,g=rx , o=rx abc: 同上
- chmod u-x, g+w abc :给abc去掉用户执行权限，增加组写的权限
- chmod a+r abc: 给所有用户添加读的权限
- chown xiaoming abc: 改变abc的拥有者为xiaoming
- chgrp root abc : 修改abc所属的组为root
- chown root ./abc: 改变abc这个目录的所有者root
- chown -R root ./abc:改变abc这个目录以及下面所有文件和目录的所有者为root

## 1.5、修改用户组
- usermod -g 组名 用户名
- chown  用户名 文件名  修改所有者
- ls -ahl 查看文件所有者
- chgrp 组名 文件名， 修改所有组