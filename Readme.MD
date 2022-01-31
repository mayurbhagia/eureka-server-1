# The Objective is to run the Eureka Server in High Availability mode so that if one server is down, the other takes over

## Create 4 EC2 instances with public IP. For these EC2’s the security group should allow the VPC CIDR for port 9001, 9002, 9003 and SSH access with least privileges

## SSH onto four EC2 instances and perform following steps on all:
1.	Install Java
    - a.	sudo wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u141-b15/336fa29ff2bb4ef291e347e091f7f4a7/jdk-8u141-linux-x64.rpm

    - b.	sudo yum install -y jdk-8u141-linux-x64.rpm
    - c.	sudo alternatives --config java
    - d.	export JAVA_HOME=/usr/java/jdk1.8.0_141/

2.	Install maven
a.	sudo wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
b.	sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
c.	sudo yum install -y apache-maven

3.	mkdir eureka-server-1 on machine 1, mkdir eureka-server-2 on machine 2, mkdir eureka-client-1 on machine 3 and mkdir eureka-client-2 on machine 4
4.	copy respective code of each machine onto the folder created in step 3. The code can be found here - https://github.com/mayurbhagia/eureka-server-1/
5.	run mvn clean install on each machine
6.	run java -jar target/eureka-XXXXXXX(client/server)-1-1.0.0-SNAPSHOT*.jar on each machine
7.	On those local machines, make entry to /etc/hosts as:
Sudo vi /etc/hosts
Add entries as:
<<private-IP-of-Server-1>> eureka-server-1.com
<<private-IP-of-Server-2>> eureka-server-2.com

## How to test:
1. Once you have run “java -jar” on 2 server machines, to test the application, goto browser and hit:
   - Test Server 1 – http://Public-IP-of-Server-1:9001 it will show the Eureka default page
   - You would see no client is registered as yet, but the server-2 machine is acting as Peer

2. Test Server 2 – same as server 1
   - How the Server-1 and Server 2 works as High availability mode, it is because of the line in application.properties of both the servers as: 
eureka.client.serviceUrl.defaultZone=http://eureka-server-1.com:9001/eureka, http://eureka-server-2.com:9002/eureka and the DNS to IP resolution is done from /etc/hosts file. Alternatively for this you can refer AWS AppMesh and CloudMesh which does similar service discovery

3. Test Client 2 – http://Public-IP-of-Client-2:9001 and go and refresh browser pages for Server-1 and Server-2 and you would see Client 2 as registered
4. Test Client 1 – http://Public-IP-of-client-1:9001 and go and refresh browser pages for Server-1 and Server-2 and you would see Client 1 is also registered

5. Now test Client 2:
   http://Public-IP-of-client-2:9001/callEurekaClient2

6. Now from Client 1 we will call Client 2, to do this run:
   http://Public-IP-of-client-1:9001/callEurekaClient1
   http://Public-IP-of-client-1:9001/callEurekaClient2FromClient1 -- this will actually call Client 2 from Client 1 by doing client side load balancing and fetch the registry from Eureka service. Refer the code snippet in the controller java file as “ServiceInstance instance = loadBalancerClient.choose("EUREKA-CLIENT-2");”

![Eureka-Server-1 with two clients and one peer server i.e. Server-2](https://github.com/mayurbhagia/eureka-server-1/blob/master/images/image1.png)
![Eureka-Server-2 with two clients and one peer server i.e. Server-1](https://github.com/mayurbhagia/eureka-server-1/blob/master/images/image2.png)
![Eureka-Client-1](https://github.com/mayurbhagia/eureka-server-1/blob/master/images/image3.png)
![Eureka-Client-1 calling Client-2 via LoadBalancerClient](https://github.com/mayurbhagia/eureka-server-1/blob/master/images/image4.png)

