package ec.com.pablorcruh.onlinefirst.coreNetwork.models

data class SyncResponse(
    val changes: List<ChangeLogDto>,
    val nextCursor: Long
)
