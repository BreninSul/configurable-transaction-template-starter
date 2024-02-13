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
package io.github.breninsul.configurabletransactiontemplatestarter.template

import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionIsolation
import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionPropagation
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionOperations
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration

/**
 * A factory for creating customizable [TransactionTemplate] instances.
 *
 * @property transactionManager the platform transaction manager.
 */
open class TransactionTemplateFactory(protected val transactionManager: PlatformTransactionManager) {

    /**
     * Creates a new [TransactionOperations] object with the given parameters.
     *
     * @param readOnly a flag indicating whether the transaction is read-only.
     * @param propagation the propagation behavior for the transaction.
     * @param isolation the isolation level for the transaction.
     * @param timeout the timeout for the transaction.
     * @return a [TransactionOperations] instance configured with the provided parameters.
     */
    fun create(
        readOnly: Boolean = false,
        propagation: TransactionPropagation = TransactionPropagation.REQUIRED,
        isolation: TransactionIsolation = TransactionIsolation.DEFAULT,
        timeout: Duration = Duration.ofSeconds(TransactionDefinition.TIMEOUT_DEFAULT.toLong()),
    ): TransactionOperations {
        val transactionTemplate = TransactionTemplate(transactionManager)
        transactionTemplate.isReadOnly = readOnly
        transactionTemplate.timeout = timeout.toSeconds().toInt()
        transactionTemplate.propagationBehavior = propagation.intValue
        transactionTemplate.isolationLevel = isolation.intValue
        return transactionTemplate
    }
}