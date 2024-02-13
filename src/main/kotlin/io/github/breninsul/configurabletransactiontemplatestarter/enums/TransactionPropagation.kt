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
package io.github.breninsul.configurabletransactiontemplatestarter.enums

import org.springframework.transaction.TransactionDefinition

/**
 * Enum representing the different types of transaction propagation behaviors.
 *
 * @property intValue integer value corresponding to the Spring Framework's numeric representation of the transaction propagation type.
 */
enum class TransactionPropagation(val intValue: Int) {
    /**
     * A constant indicating that a transaction will be created if necessary.
     */
    REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),

    /**
     * A constant indicating that that the current transaction context will be propagated.
     */
    SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),

    /**
     * A constant indicating that a current transaction must exist for the transaction to proceed.
     */
    MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),

    /**
     * A constant indicating that a new transaction will be created for the given set of operations.
     */
    REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),

    /**
     * A constant indicating that the current transaction context (if any) should not be propagated.
     */
    NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),

    /**
     * A constant indicating that a current transaction should not exist for the transaction to proceed.
     */
    NEVER(TransactionDefinition.PROPAGATION_NEVER),

    /**
     * A constant indicating that a "nested" transaction should be used if a current transaction exists.
     */
    NESTED(TransactionDefinition.PROPAGATION_NESTED),
}
