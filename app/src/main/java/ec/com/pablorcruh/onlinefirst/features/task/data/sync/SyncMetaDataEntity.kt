package ec.com.pablorcruh.onlinefirst.features.task.data.sync

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_meta_data")
data class SyncMetaDataEntity(

    @PrimaryKey(autoGenerate = false)
    val id:Int =1,
    val lastCursor: Int
)
