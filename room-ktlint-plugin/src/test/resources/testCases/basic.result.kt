interface Dao {
    @Query("""
        SELECT
            *
        FROM
            example
        """)
    fun query()
}