# e-commerce-services

| Notification Type | Service to Use             |
| ----------------- | -------------------------- |
| ✅ Email           | SendGrid / SES / Mailgun   |
| ✅ SMS             | Twilio / Textlocal / MSG91 |
| ✅ Push            | FCM / OneSignal            |


<!-- Spring Boot Starters -->
<dependency> <groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
<dependency> <groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>
<dependency> <groupId>org.springframework.kafka</groupId><artifactId>spring-kafka</artifactId></dependency>

<!-- SendGrid -->
<dependency> <groupId>com.sendgrid</groupId><artifactId>sendgrid-java</artifactId><version>4.9.3</version></dependency>

<!-- Twilio -->
<dependency> <groupId>com.twilio.sdk</groupId><artifactId>twilio</artifactId><version>9.6.1</version></dependency>

<!-- Firebase Admin SDK -->
<dependency> <groupId>com.google.firebase</groupId><artifactId>firebase-admin</artifactId><version>9.1.1</version></dependency>

<!-- MySQL / PostgreSQL -->
<dependency> <groupId>mysql</groupId><artifactId>mysql-connector-java</artifactId><scope>runtime</scope></dependency>
