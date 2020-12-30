package people

import org.testcontainers.containers.PostgreSQLContainer

/**
 * extended for use with r2dbc
 */
class KPostgreSQLContainer(image: String): PostgreSQLContainer<KPostgreSQLContainer>(image) {

    fun getR2dbcUrl(): String {
        val additionalUrlParams = constructUrlParameters("?", "&")
        return ("r2dbc:postgresql://$containerIpAddress:${getMappedPort(POSTGRESQL_PORT)}/$databaseName$additionalUrlParams")
    }
}