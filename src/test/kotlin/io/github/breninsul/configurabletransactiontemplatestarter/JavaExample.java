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
        String result=trxTemplate.execute(
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
        System.out.println(result);
    }
}
