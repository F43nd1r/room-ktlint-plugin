interface Dao {
    @Query("""
        SELECT
            a,
            b,
            c AS d
        FROM
            example
        """)
    fun query()
}