annotation class Query(val value: String)

interface Dao {
    @Query(
        """
        SELECT
            *
        FROM
            TABLE
        """
    )
    fun query()
}
