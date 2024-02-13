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
 * Enum class for different types of transaction isolations.
 *
 * Each enumeration is associated with an integer value corresponds to isolation level defined in Spring's TransactionDefinition.
 */
enum class TransactionIsolation(val intValue: Int) {
    /**
     * The default isolation level.
     */
    DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),

    /**
     * A constant indicating that dirty reads, non-repeatable reads and phantom reads can occur.
     */
    READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),

    /**
     * A constant indicating that dirty reads are prevented; non-repeatable reads and phantom reads can occur.
     */
    READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),

    /**
     * A constant indicating that dirty reads and non-repeatable reads are prevented; phantom reads can occur.
     */
    REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),

    /**
     * A constant indicating that dirty reads, non-repeatable reads and phantom reads are prevented.
     */
    SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE),
}
