interface Dao {
    @Query("""
        SELECT
            a + b,
            a * b,
            a - b,
            a / b,
            a % b,
            a = b,
            a != b
        FROM
            example
        """)
    fun query()
}