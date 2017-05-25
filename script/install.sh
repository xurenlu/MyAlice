#!/bin/bash
# 判断进程是否运行
function isRun(){
 ps aux | grep -v 'grep' | grep $1
}
# 解压jdk
tar xzvf /usr/local/src/jdk1.8.0_101.tar.gz -C /usr/local/

# 设置java_home 和java的环境变量
path_file=/etc/profile.d/path.sh
echo "JAVA_HOME=/usr/local/jdk1.8.0_101" >> $path_file
echo 'PATH=$PATH:$JAVA_HOME/bin' >> $path_file
echo 'export PATH JAVA_HOME' >> $path_file

# 创建es数据目录和日志目录，解压es
mkdir -p /esdata/data
mkdir -p /esdata/logs
tar xvf /usr/local/src/elasticsearch.tar.gz -C /usr/local/
useradd es -s /bin/bash
chown -R es:es /usr/local/elasticsearch
chown -R es:es /esdata/

# 解压数据库和安装数据库
tar xzvf /usr/local/src/mysql-5.6.36-linux-glibc2.5-x86_64.tar.gz -C /usr/local
useradd mysql -s /bin/bash
mv /usr/local/mysql-5.6.36-linux-glibc2.5-x86_64 /usr/local/mysql
mkdir /mysql
chown -R mysql:mysql /mysql
yum install -y perl perl-Data-Dumper libaio libaio-devel
cd /usr/local/mysql
./scripts/mysql_install_db --user=mysql --datadir=/mysql

cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysqld
sed -i 's#^basedir=$#basedir=/usr/local/mysql#g' /etc/init.d/mysqld
sed -i 's#^datadir=$#datadir=/mysql#g' /etc/init.d/mysqld

# 启动Elasticsearch
isRun elasticsearch
if [ $? != 0 ] ; then
  su - es -c "/usr/local/elasticsearch/bin/elasticsearch -d"
fi
# 启动mysql
isRun mysql
if [ $? != 0 ] ; then
  /etc/init.d/mysqld start
fi

# 创建myalice数据库
isRun mysql
if [ $? == 0 ] ; then
  /usr/local/mysql/bin/mysql -u root test < /usr/local/src/init.sql
fi
# 安装mvn
tar -xvf /usr/local/src/mvn.tar.gz -C /usr/local/
echo "M2_HOME=/usr/local/mvn" >> $path_file
echo 'PATH=$PATH:$M2_HOME/bin' >> $path_file
echo "export M2_HOME PATH" >> $path_file

. /etc/profile

#解压mvn仓库
tar -xvf /usr/local/src/m2.tar.gz -C /root/

# 创建apps目录，编译源码，启动 
mkdir /usr/local/apps
tar -xvf /usr/local/src/myalice.tar.gz -C /usr/local/apps/
cd /usr/local/apps/MyAlice
mvn package -DskipTests -Djar

java -jar myalice-web/target/myalice-web-1.0.jar
