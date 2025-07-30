interface Dao {
    @Query("""
        SELECT
            *
        FROM
            example
        WHERE
            a = :a
        """)
    fun query(a: String)
}