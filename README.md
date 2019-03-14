opennode-java
========

[![Apache License](http://img.shields.io/badge/license-ASL-blue.svg)](https://github.com/brunocvcunha/opennode-java/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/brunocvcunha/opennode-java.svg)](https://travis-ci.org/brunocvcunha/opennode-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.brunocvcunha.opennode-java/opennode-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.brunocvcunha.opennode-java/opennode-java)
[![Coverage Status](https://coveralls.io/repos/github/brunocvcunha/opennode-java/badge.svg?branch=master)](https://coveralls.io/github/brunocvcunha/opennode-java?branch=master)

:zap: OpenNode.co Java Client.

Download
--------

Download [the latest JAR][1] or grab via Maven:
```xml
<dependency>
  <groupId>org.brunocvcunha.opennode-java</groupId>
  <artifactId>opennode-java</artifactId>
  <version>1.2</version>
</dependency>
```
or Gradle:
```groovy
compile 'org.brunocvcunha.opennode-java:opennode-java:1.2'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

Supported Operations & Examples
--------

#### Create the Client

```java
        OpenNodeService service = OpenNodeServiceFactory.buildClient("{api_token}");
```


#### Create a Charge


```java
        OpenNodeCreateCharge createCharge = OpenNodeCreateCharge.builder()
                .orderId("ABCDEF-100")
                .description("OpenNode Java Client")
                .amount(1)
                .currency(OpenNodeCurrency.USD) // default is satoshis
                .build();
        
        OpenNodeCharge createdCharge = service.createCharge(createCharge).execute().body().getData();
        System.out.println("Created charge with ID: " + createdCharge.getId() + ". Invoice: " + createdCharge.getLightningInvoice().getPayreq());
```

#### Get a Charge by ID


```java
    OpenNodeCharge charge = service.getCharge("{charge_id"}).execute().body().getData();
    System.out.println(charge.getDescription() + " - " + charge.getAmount() + " - " + charge.getStatus());
```


#### List Charges


```java
    OpenNodeResponse<List<OpenNodeCharge>> charges = service.listCharges().execute().body();
    for (OpenNodeCharge charge : charges.getData()) {
        System.out.println(charge.getDescription() + " - " + charge.getAmount() + " - " + charge.getStatus());
    }
```



Want to tip a few satoshis? [tippin.me/@bruflow](https://tippin.me/@bruflow)

opennode-java requires at minimum Java 8.

 [1]: https://search.maven.org/remote_content?g=org.brunocvcunha.opennode-java&a=opennode-java&v=LATEST
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
