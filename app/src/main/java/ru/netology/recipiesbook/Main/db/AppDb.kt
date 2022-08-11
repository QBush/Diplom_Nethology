package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.recipiesbook.Main.db.RecipeDao
import ru.netology.recipiesbook.Main.db.RecipeEntity

// класс Синглтон, не вдаваться в подробности, просто вот так он создается.

@Database(
    entities = [RecipeEntity::class], // из чего состоит таблица
    version = 1 // порядковый номер версии
)
abstract class AppDb : RoomDatabase() {

    abstract val postDao: RecipeDao

    companion object {
        @Volatile
        private var instance: AppDb? = null


        // безопасное создание синглтона
        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // передаем контекст, класс и имя файла ДБ, куда сохранять
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context, AppDb::class.java, "app.db"
            ).allowMainThreadQueries() // не заморачиваемся, это для работы из основного потока
                .build()
    }
}