annotation class Query(val value: String)

interface Dao {
    @Query(
        """
        SELECT
            *
        FROM
            `table`
        """
    )
    fun query()

    @Query(
        value = """
        SELECT
            *
        FROM
            `table`
        """
    )
    fun query2()
}

annotation class DatabaseView(val value: String, val viewName: String = "")

@DatabaseView(
    """
    SELECT
        field
    FROM
        `table`
    """
)
data class View(val field: String)

@DatabaseView(
    value = """
    SELECT
        field
    FROM
        `table`
    """,
    viewName = "View2"
)
data class View2(val field: String)
