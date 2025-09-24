This is small lib for easy configuring Spring TransactionTemplate params for each execution such as 
readOnly
propagation
isolation
timeout

Configure with properties

| Parameter                                             | Type                   | Description                                |
|-------------------------------------------------------|------------------------|--------------------------------------------|
| `spring.transaction.configurable.enabled`             | boolean                | Enable/disable autoconfig for this starter |
| `spring.transaction.configurable.default.propagation` | TransactionPropagation | Enable/disable autoconfig for this starter |
| `spring.transaction.configurable.default.isolation`   | TransactionIsolation   | Enable/disable autoconfig for this starter |
| `spring.transaction.configurable.default.read-only`   | boolean                | Enable/disable autoconfig for this starter |
| `spring.transaction.configurable.default.timeout`     | Duration               | Enable/disable autoconfig for this starter |


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

# Or java 
````java
package io.github.breninsul.configurabletransactiontemplatestarter;

import io.github.breninsul.configurabletransactiontemplatestarter.config.DefaultTransactionSettings;
import io.github.breninsul.configurabletransactiontemplatestarter.template.ConfigurableTransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
public class JavaExample {
    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    ConfigurableTransactionTemplate trxTemplate;


    public void executeTestTransaction() {
        trxTemplate.execute(
                true,
                DefaultTransactionSettings.getPropagation(),
                DefaultTransactionSettings.getIsolation(),
                DefaultTransactionSettings.getTimeout()
                ,t -> {
                    String selected=jdbcClient.sql("Select :param")
                            .param("param", "test", Types.VARCHAR)
                            .query(String.class)
                            .single();
                    return selected;
                });
    }
}

````

# Transactions can be executed in transactions

````kotlin
  fun `testTrxId`() {
        val i: TransactionManagementConfigurer
        trxTemplate.execute(readOnly = true, propagation = TransactionPropagation.REQUIRED) { _ ->
            val parentId =
                jdbcClient
                    .sql("select txid_current()")
                    .query(Long::class.java)
                    .single()
            trxTemplate.execute(readOnly = true, propagation = TransactionPropagation.REQUIRES_NEW) {
                val newId =
                    jdbcClient
                        .sql("select txid_current()")
                        .query(Long::class.java)
                        .single()
                Assertions.assertNotEquals(parentId, newId)
            }
            trxTemplate.execute(readOnly = true, propagation = TransactionPropagation.REQUIRED) {
                val sameId =
                    jdbcClient
                        .sql("select txid_current()")
                        .query(Long::class.java)
                        .single()
                Assertions.assertEquals(parentId, sameId)
            }
        }
    }
````