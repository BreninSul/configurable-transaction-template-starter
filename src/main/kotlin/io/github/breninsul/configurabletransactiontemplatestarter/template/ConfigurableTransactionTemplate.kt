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

import io.github.breninsul.configurabletransactiontemplatestarter.config.DefaultTransactionSettings
import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionIsolation
import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionPropagation
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionOperations
import java.time.Duration
import java.util.function.Consumer

/**
 * This is a customizable implementation of [TransactionOperations], allowing to specify transaction properties like
 * propagation behavior, isolation level, read-only flag and timeout directly in the execution call.
 * This is particularly useful for prototyping, tests and for cases where programmatic transaction demarcation is required.
 *
 * @property factory Factory for creating transaction templates.
 * @constructor Creates a new instance of [ConfigurableTransactionTemplate] with default transaction properties.
*/
open class ConfigurableTransactionTemplate(
    protected val factory: TransactionTemplateFactory,
    protected val defPropagation: TransactionPropagation = DefaultTransactionSettings.propagation,
    protected val defIsolation: TransactionIsolation = DefaultTransactionSettings.isolation,
    protected val defReadOnly: Boolean = DefaultTransactionSettings.readOnly,
    protected val defTimeout: Duration = DefaultTransactionSettings.timeout,
) : TransactionOperations {
    /**
     * Execute the given callback specifying transaction semantics along the way.
     *
     * @param readOnly The read-only flag.
     * @param propagation The propagation behavior.
     * @param isolation The isolation level.
     * @param timeout The timeout for this transaction.
     * @param action The callback object which specifies the unit of work.
     * @tparam T The result type.
     * @return A result object returned by the callback, or null if none.
     */
    open fun <T : Any?> execute(
        readOnly: Boolean = defReadOnly,
        propagation: TransactionPropagation = defPropagation,
        isolation: TransactionIsolation = defIsolation,
        timeout: Duration = defTimeout,
        action: TransactionCallback<T>,
    ): T? {
        val template = factory.create(readOnly, propagation, isolation, timeout)
        return template.execute(action)
    }

    /**
     * Execute the given callback without any result, specifying transaction semantics along the way.
     *
     * @param readOnly The read-only flag.
     * @param propagation The propagation behavior.
     * @param isolation The isolation level.
     * @param timeout The timeout for this transaction.
     * @param action The callback object which specifies the unit of work. The result will be ignored, if any.
     */
    open fun executeWithoutResult(
        readOnly: Boolean = defReadOnly,
        propagation: TransactionPropagation = defPropagation,
        isolation: TransactionIsolation = defIsolation,
        timeout: Duration = defTimeout,
        action: Consumer<TransactionStatus?>,
    ) {
        execute<Any>(
            readOnly,
            propagation,
            isolation,
            timeout,
        ) { status: TransactionStatus? ->
            action.accept(status)
            null
        }
    }

    /**
     * Execute the given action, handling the transaction with the default properties.
     *
     * @param action The callback object which defines the unit of work.
     * @tparam T The result type.
     * @return A result object returned by the callback, or `null` if none.
     */
    override fun <T : Any?> execute(action: TransactionCallback<T>): T? {
        return factory.create(defReadOnly, defPropagation, defIsolation, defTimeout).execute(action)
    }
}