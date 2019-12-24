# CookingWithJava

The program consists of a cooking blog developed with Spring. An additional Internal Service has been developed to export a recipe as pdf.

This application is based on the final project from the course *Development of Distributed Applications* in the *Computer Engineering* degree of the *URJC*. The program can be deploy using docker and is capable of distributing the load of the main app and the internal service. A MySQL database is used to store all the user, recipes, comments, favorites and images.

A static web version without server capabilities can be accessed in <https://iagocl.github.io/CookingWithJava/index.html>. Note that you can only use local static HTML functionality so you cannot log in, create recipes, etc. If you want to test the complete app, please deploy the program using docker or contact me.

## Members
| Name   | Surname        | Github                                  |
|--------|----------------|-----------------------------------------|
| Iago   | Calvo Lista    | [iagoCL](https://github.com/iagoCL)     |
| Javier | Mártinez Pablo | [JaviBJ99](https://github.com/JaviBJ99) |
| Juan   | Jimenez Galvez | [Who1ne](https://github.com/Who1ne)     |

The web design has been based on a template from [colorlib](https://colorlib.com/) licensed under [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/legalcode)

### UML Scheme
![alt text](gddImages/diagrama_clases.jpg "UML Scheme.")

*UML Scheme.*

### Deployment
#### Windows deployment
#### Intrucciones de despliegue en ubuntu 18.04 server
First we need to install java-jre.
```
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt update
sudo apt install default-jre -y
```
We also need to install mysql
```
sudo apt install mysql-server -y
```
We need to open mysql and create the corresponding database and give access to our user.
```
sudo mysql
create database db_cooking_with_java; 
create user 'cookingWithJavaDefaultUser'@'%' identified by 'cookingWithJavaDefaultPass'; 
grant all on db_cooking_with_java.* to 'cookingWithJavaDefaultUser'@'%';
exit;
```
Finally once the .jar are downloaded they can be executed with: 
```
java -jar CookingWithJava.jar
java -jar CookingWithJavaInternalService.jar
```
#### Instruction of deployment using docker
First we need to install docker, supposing that we use ubuntu 18.04:
```
sudo apt update

sudo apt install apt-transport-https ca-certificates curl software-properties-common -y

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"

sudo apt update

apt-cache policy docker-ce

sudo apt install docker-ce -y
```
We can check that it is executing with:
```
sudo systemctl status docker
```
We install docker-compose:
```
sudo curl -L "https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose
```

We just need to go to the deploy folder and execute
```
cd /path/to/the/deploy/folder

sudo docker-compose up --detach --build  
```
Editing the file [deployment/docker-compose.yml](deployment/docker-compose.yml) we can change the number of instances, the database name, user and password.

![alt text](gddImages/diagrama_docker.jpg "Scheme of the default deployment in docker.")

*Scheme of the default deployment in docker*

#### Build
If you want to recompile the .jar just execute:
```
./mvnw -Dmaven.test.skip=true package 
```

#### Backup
We suggest to generate backups of the database using:
```
sudo docker exec mysqlMaster /usr/bin/mysqldump -u cookingWithJavaDefaultUser --password=cookingWithJavaDefaultPass db_cooking_with_java > exampleBackup.sql
```
An example can be found in [deployment/exampleBackup.sql](deployment/exampleBackup.sql), and can be loaded with:
```
cat exampleBackup.sql | sudo docker exec -i mysqlMaster /usr/bin/mysql -u cookingWithJavaDefaultUser --password=cookingWithJavaDefaultPass db_cooking_with_java
```