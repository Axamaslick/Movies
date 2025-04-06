package com.example.movies

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create a new table with the updated schema
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `movies_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `title` TEXT NOT NULL,
                `year` TEXT NOT NULL,
                `posterUrl` TEXT NOT NULL,
                `isSelected` INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        // Copy data from the old table to the new table (excluding the 'poster' column)
        database.execSQL(
            """
            INSERT INTO `movies_new` (id, title, year, posterUrl, isSelected)
            SELECT id, title, year, '', 0 FROM `movies`
            """.trimIndent()
        )

        // Drop the old table
        database.execSQL("DROP TABLE `movies`")

        // Rename the new table to replace the old table
        database.execSQL("ALTER TABLE `movies_new` RENAME TO `movies`")
    }
}