# 扁鹊（BianQue）

## 简介
扁鹊（BianQue）是一个shell组件库，封装常用的shell函数，提供给开发者使用。  
扁鹊（BianQue）的目标是提供一个shell脚本的组件库，降低排查问题门槛。
## 功能
- 组件库平台（基于vert.x开发的web项目，数据存储使用H2，可以通过组件库平台获取组件库的最新版本）
- 组件库（终端用户使用的组件库，封装常用的shell函数）
## 组件库平台部署
```shell
mvn clean package
nohup java -jar bianque.jar &
curl http://127.0.0.1:8000/bianque/initTable
```
默认端口：8000  
数据默认存储目录：/data/bianque
H2默认web控制台：http://localhost:8888/h2-console
## 脚本安装
```shell
wget http://{target_ip:target_port}/bianque/setup.sh
chmod 777 setup.sh
./setup.sh
source /etc/profile
source /etc/bash_completion.d/bianque*
```
## 脚本使用
```shell
bianque-jdk -h
```
## 脚本开发
bash_completion目录下的bianque-jdk文件用于命令联想。  
shell目录下的bianque-jdk.sh文件用于命令实现，封装具体shell功能。

## 脚本库更新
终端执行命令：
```shell
./setup.sh
```