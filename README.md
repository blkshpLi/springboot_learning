## 基于springboot的论坛项目

## 部署
- yum -y update
- yum install git
- mkdir App
- cd App
- git clone https://github.com/blkshpLi/springboot_learning.git
- yum install maven
- cd springboot_learning
- mvn clean compile package
- cp src/main/resources/application.properties src/main/resources/application-production.properties
- mvn package

## 参考资料
 [Spring](https://spring.io/guides)
 [Bootstrap3中文文档](https://v3.bootcss.com/)
 [Github Building OAuth Apps](https://developer.github.com/apps/building-oauth-apps/)
 [Springboot](https://docs.spring.io/spring-boot/docs/2.1.6.BUILD-SNAPSHOT/reference/html/)
 [Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
 [JQuery](https://how2j.cn/k/jquery/jquery-tutorial/467.html)
 
## 工具及插件
 [Git](https://git-scm.com/download)
 [Flyway](https://flywaydb.org/getstarted/firststeps/maven)
 [Lombok](https://www.projectlombok.io/)
 [Markdown 编辑器](http://editor.md.ipandao.com/)
 [Postman(谷歌浏览器插件)](https://chrome.google.com/webstore/detail/tabbed-postman-rest-clien/coohjcphdfgbiolnekdpbcijmhambjff)