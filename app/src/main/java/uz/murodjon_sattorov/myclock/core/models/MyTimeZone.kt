package uz.murodjon_sattorov.myclock.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "clocks_table")
data class MyTimeZone(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var zoneName: String
)