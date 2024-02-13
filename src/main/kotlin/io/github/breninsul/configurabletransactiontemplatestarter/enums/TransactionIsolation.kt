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
package io.github.breninsul.configurabletransactiontemplatestarter

import org.springframework.transaction.TransactionDefinition

enum class TransactionIsolation(private val value: Int) {
    /**
     * Use the default isolation level of the underlying data store.
     *
     * All other levels correspond to the JDBC isolation levels.
     * @see java.sql.Connection
     */
    DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),

    /**
     * A constant indicating that dirty reads, non-repeatable reads, and phantom reads
     * can occur.
     *
     * This level allows a row changed by one transaction to be read by
     * another transaction before any changes in that row have been committed
     * (a "dirty read"). If any of the changes are rolled back, the second
     * transaction will have retrieved an invalid row.
     * @see java.sql.Connection.TRANSACTION_READ_UNCOMMITTED
     */
    READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),

    /**
     * A constant indicating that dirty reads are prevented; non-repeatable reads
     * and phantom reads can occur.
     *
     * This level only prohibits a transaction from reading a row with uncommitted
     * changes in it.
     * @see java.sql.Connection.TRANSACTION_READ_COMMITTED
     */
    READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),

    /**
     * A constant indicating that dirty reads and non-repeatable reads are
     * prevented; phantom reads can occur.
     *
     * This level prohibits a transaction from reading a row with uncommitted changes
     * in it, and it also prohibits the situation where one transaction reads a row,
     * a second transaction alters the row, and the first transaction re-reads the row,
     * getting different values the second time (a "non-repeatable read").
     * @see java.sql.Connection.TRANSACTION_REPEATABLE_READ
     */
    REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),

    /**
     * A constant indicating that dirty reads, non-repeatable reads, and phantom
     * reads are prevented.
     *
     * This level includes the prohibitions in [.REPEATABLE_READ]
     * and further prohibits the situation where one transaction reads all rows that
     * satisfy a `WHERE` condition, a second transaction inserts a row
     * that satisfies that `WHERE` condition, and the first transaction
     * re-reads for the same condition, retrieving the additional "phantom" row
     * in the second read.
     * @see java.sql.Connection.TRANSACTION_SERIALIZABLE
     */
    SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);


    fun value(): Int {
        return this.value
    }
}
