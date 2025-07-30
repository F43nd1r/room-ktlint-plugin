interface Dao {
    @Query("""SELECT * FROM `order`""")
    fun query()
}