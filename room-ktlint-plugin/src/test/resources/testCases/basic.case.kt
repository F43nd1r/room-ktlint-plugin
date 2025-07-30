interface Dao {
    @Query("select * from example")
    fun query()
}