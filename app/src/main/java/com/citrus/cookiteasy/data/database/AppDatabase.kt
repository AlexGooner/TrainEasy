package com.citrus.cookiteasy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import java.util.Date

@Database(entities = [User::class,
    Training::class,
    Exercise::class,
    Equipment::class,
    ], version = 3, exportSchema = true)
@TypeConverters(ListIntConverter::class, CommonConverters::class)
abstract class AppDatabase : RoomDatabase() {


    abstract fun userDao(): UserDao
    abstract fun trainingDao(): TrainingDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun equipmentDao(): EquipmentDao



    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            populateInitialData(context)
                        }
                    }
                })
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(context: Context) {
            val db = AppDatabase.getDatabase(context)
        }
    }
}

// Конвертер для List<Int> в Exercise и Equipment
class ListIntConverter {
    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun toString(list: List<Int>?): String? {
        return list?.joinToString(",")
    }
}

// Конвертер для других типов (если нужно)
class CommonConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS 'users_new' (
            id INTEGER PRIMARY KEY NOT NULL,
            username TEXT NOT NULL,
            password TEXT NOT NULL,
            first_name TEXT NOT NULL,
            last_name TEXT NOT NULL,
            birth_date TEXT NOT NULL,
            height INTEGER NOT NULL,
            weight INTEGER NOT NULL,
            bmi REAL NOT NULL,
            gender TEXT NOT NULL,
            UNIQUE(id, username) ON CONFLICT IGNORE
            )
        """.trimIndent()
        )

        db.execSQL("DROP TABLE users")
        db.execSQL("ALTER TABLE users_new RENAME TO users")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем все новые таблицы
        db.execSQL("""
            CREATE TABLE trainings (
                training_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                duration_minutes INTEGER NOT NULL,
                difficulty_level INTEGER NOT NULL,
                location TEXT NOT NULL,
                estimated_calories INTEGER,
                description TEXT,
                rest_between_exercises_sec INTEGER,
                is_favorite INTEGER NOT NULL DEFAULT 0,
                target_muscles TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE exercises (
                exercise_id INTEGER PRIMARY KEY AUTOINCREMENT,
                training_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                sets INTEGER NOT NULL,
                reps INTEGER,
                duration_sec INTEGER,
                rest_after_sec INTEGER,
                description TEXT,
                FOREIGN KEY (training_id) REFERENCES trainings(training_id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE equipment (
                equipment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                training_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                FOREIGN KEY (training_id) REFERENCES trainings(training_id) ON DELETE CASCADE
            )
        """.trimIndent())

        // Создаем индексы для ускорения запросов
        db.execSQL("CREATE INDEX idx_exercises_training_id ON exercises(training_id)")
        db.execSQL("CREATE INDEX idx_equipment_training_id ON equipment(training_id)")
    }
}