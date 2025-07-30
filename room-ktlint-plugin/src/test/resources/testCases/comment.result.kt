interface Dao {
    @Query("""
        -- This is a full line comment
        SELECT
            * -- This is an inline comment
        FROM
            example
        """)
    fun query()
}