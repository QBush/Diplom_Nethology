package ru.netology.recipiesbook.Main.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.recipiesbook.Main.data.Category


//интерфейс, для приема/отдачи данных из/в БД
@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY recipeId DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert
    fun insert(recipe: RecipeEntity)

    //TODO расковырять Entity без точки
    @Query("""
        UPDATE recipes SET            
            recipeName =:recipe.recipeName,
            category =:recipe.category,
            content =:recipe.content,
            imageSource =:recipe.imageSource
WHERE recipeId =:recipeId
                """)
    fun updateContentById(recipeId: Long, recipe: RecipeEntity)


    @Query("""
        UPDATE recipes SET
        addedToFavorites = CASE WHEN addedToFavorites THEN 0 ELSE 1 END
        WHERE recipeId = :recipeId
        """)
    fun addToFavorites(recipeId: Long)


    @Query("DELETE FROM recipes WHERE recipeId =:recipeId")
    fun removeById(recipeId: Long)

}