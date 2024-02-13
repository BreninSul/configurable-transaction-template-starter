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

import io.github.breninsul.configurabletransactiontemplatestarter.config.ConfigurableTransactionAutoConfiguration
import io.github.breninsul.configurabletransactiontemplatestarter.enums.TransactionPropagation
import io.github.breninsul.configurabletransactiontemplatestarter.template.ConfigurableTransactionTemplate
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.TransactionManagementConfigurer
import org.testcontainers.containers.PostgreSQLContainer
import java.util.function.Function
import java.util.logging.Logger

/**
 * Test case for transactions.
 *
 * @property logger Logger instance for this class.
 * @property jdbcClient Jdbc Client.
 * @property trxTemplate Template for transaction management.
 */
@EnableTransactionManagement
@ExtendWith(SpringExtension::class)
@Import(TestConfig::class,DataSourceAutoConfiguration::class, ConfigurableTransactionAutoConfiguration::class, TransactionAutoConfiguration::class)
class TransactionTest {
    protected val logger = Logger.getLogger(this.javaClass.name)

    @Autowired
    lateinit var jdbcClient: JdbcClient

    @Autowired
    lateinit var trxTemplate: ConfigurableTransactionTemplate

    class ReadOnlyException(cause: Throwable) : RuntimeException(cause)

    /**
     * Test case to check read-only transaction behavior.
     */
    @Test
    fun `testReadOnly`() {
        val i: TransactionManagementConfigurer
        assertThrows<ReadOnlyException> {
            try {
                trxTemplate.execute(readOnly = true) {
                    jdbcClient.sql("create table test(test_col text)")
                        .update()
                }
            } catch (t: UncategorizedSQLException) {
                mapException(t, "ERROR: cannot execute .* in a read-only transaction") { ReadOnlyException(it) }
            }
        }
        assertDoesNotThrow {
            trxTemplate.execute(readOnly = false) {
                jdbcClient.sql("create table test(test_col text)")
                    .update()
                jdbcClient.sql("insert into test(test_col) values ('test')")
                    .update()
                it.setRollbackOnly()
            }
        }
    }

    /**
     * Test case to check transaction id behavior.
     */
    @Test
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

    /**
     * A utility method to map exceptions.
     */
    private fun mapException(
        t: Throwable,
        expectedRegex: String,
        exceptionSupplier: Function<Throwable, Throwable>,
    ) {
        val isReadOnly = (t.message ?: "").matches(".*$expectedRegex.*".toRegex())
        if (isReadOnly) {
            throw exceptionSupplier.apply(t)
        } else {
            throw t
        }
    }

    companion object {
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16.1-alpine3.19")

        /**
         * Configures dynamic properties for the test environment.
         */
        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgres.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgres.stop()
        }
    }
}