This is small lib for easy configuring Spring TransactionTemplate params for each execution such as 
readOnly
propagation
isolation
timeout

Configure with properties

| Parameter                                 | Type     | Description                                                                                                                                                     |
|-------------------------------------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `spring.transaction.configurable.enabled` | boolean  | Enable/disable autoconfig for this starter                                                                                                                      |



Beans in Spring Boot will be automatically registered in ConfigurableTransactionAutoConfiguration with defined properties ConfigurableTransactionTemplateProperties (prefix synchronisation).

add the following dependency:

````kotlin
dependencies {
//Other dependencies
    implementation("io.github.breninsul:configurable-transaction-template-starter:${version}")
//Other dependencies
}

````
# Example of usage

````kotlin
val trxTemplate: ConfigurableTransactionTemplate
trxTemplate.execute(readOnly = true) {
    jdbcClient.sql("do read only sql")
        .update()
}
````
