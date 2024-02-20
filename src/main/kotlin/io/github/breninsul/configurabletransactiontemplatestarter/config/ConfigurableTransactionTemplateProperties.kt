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

/**
 * Package containing all the configurations related to the application
 */
package io.github.breninsul.configurabletransactiontemplatestarter.config

import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionIsolation
import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionPropagation
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

/**
 * This class is used to manage configurable transaction template properties
 *
 * @property enabled Flag to denote if transaction template properties are enabled
 * @property default Default transaction properties
 */
@ConfigurationProperties("spring.transaction.configurable")
data class ConfigurableTransactionTemplateProperties(
    /**
     *  Flag to denote if transaction template properties are enabled
     */
    var enabled: Boolean = true,
    /**
     * Default transaction properties
     */
    var default: Default = Default(),
) {
    /**
     * This class is used to manage default transaction properties
     *
     * @property propagation Transaction propagation
     * @property isolation Transaction isolation
     * @property readOnly Flag to denote if transaction is read-only
     * @property timeout Transaction timeout
     */
    data class Default(
        /**
         * Transaction propagation
         */
        var propagation: TransactionPropagation = DefaultTransactionSettings.propagation,
        /**
         * Transaction isolation
         */
        var isolation: TransactionIsolation = DefaultTransactionSettings.isolation,
        /**
         * Flag to denote if transaction is read-only
         */
        val readOnly: Boolean = DefaultTransactionSettings.readOnly,
        /**
         * Transaction timeout
         */
        val timeout: Duration = DefaultTransactionSettings.timeout,
    )
}
