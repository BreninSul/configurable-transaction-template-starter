/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.configurabletransactiontemplatestarter.config

import io.github.breninsul.configurabletransactiontemplatestarter.template.ConfigurableTransactionTemplate
import io.github.breninsul.configurabletransactiontemplatestarter.template.TransactionTemplateFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.transaction.PlatformTransactionManager

/**
 * This class provides automatic configuration for configurable transactions.
 */
@AutoConfiguration
@AutoConfigureBefore(TransactionAutoConfiguration::class)
@AutoConfigureAfter(DataSourceTransactionManagerAutoConfiguration::class)
@ConditionalOnProperty(prefix = "spring.transaction.configurable", name = ["enabled"], matchIfMissing = true, havingValue = "true")
@EnableConfigurationProperties(ConfigurableTransactionTemplateProperties::class)
class ConfigurableTransactionAutoConfiguration {
    /**
     * Creates a TransactionTemplateFactory bean if a PlatformTransactionManager bean exists and a TransactionTemplateFactory bean does not exist.
     * @param transactionManager a PlatformTransactionManager bean.
     * @return a TransactionTemplateFactory bean.
     */
    @Bean("transactionTemplateFactory")
    @ConditionalOnBean(PlatformTransactionManager::class)
    @ConditionalOnMissingBean(TransactionTemplateFactory::class)
    fun transactionTemplateFactory(transactionManager: PlatformTransactionManager): TransactionTemplateFactory {
        return TransactionTemplateFactory(transactionManager)
    }

    /**
     * Creates a ConfigurableTransactionTemplate bean if a TransactionTemplateFactory bean exists and a ConfigurableTransactionTemplate bean does not exist.
     * @param factory a TransactionTemplateFactory bean.
     * @param properties a ConfigurableTransactionTemplateProperties bean.
     * @return a ConfigurableTransactionTemplate bean.
     */
    @Bean("configurableTransactionTemplate")
    @ConditionalOnBean(TransactionTemplateFactory::class)
    @ConditionalOnMissingBean(ConfigurableTransactionTemplate::class)
    fun configurableTransactionTemplate(
        factory: TransactionTemplateFactory,
        properties: ConfigurableTransactionTemplateProperties,
    ): ConfigurableTransactionTemplate {
        return ConfigurableTransactionTemplate(factory, properties.default.propagation, properties.default.isolation, properties.default.readOnly, properties.default.timeout)
    }
}
