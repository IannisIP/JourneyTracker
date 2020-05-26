package journey.project.data

import android.content.*
import android.database.Cursor
import android.net.Uri
import journey.project.models.TravelNote

class SampleContentProvider : ContentProvider() {
    companion object {
        const val AUTHORITY = "journey.project.data"
        val URI_TRAVELNOTES = Uri.parse(
            "content://"+ AUTHORITY+"/travelnotes"
        )
        private const val CODE_ALL_TRAVEL_NOTES = 1
        private const val CODE_TRAVEL_NOTE_ITEM = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(
                AUTHORITY,
                "travelnotes",
                CODE_ALL_TRAVEL_NOTES
            )
            MATCHER.addURI(
                AUTHORITY,
                "travelnotes" + "/*",
                CODE_TRAVEL_NOTE_ITEM
            )
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        return if (code == CODE_ALL_TRAVEL_NOTES || code == CODE_TRAVEL_NOTE_ITEM) {
            val context = context ?: return null
            val TravelNote = TravelDb.getInstanta(context)?.getTravelDao()

            var cursor: Cursor? = null
            if (TravelNote != null) {
                cursor = (if (code == CODE_ALL_TRAVEL_NOTES) {
                            TravelNote.selectAll()
                } else {
                    TravelNote.loadSingle(ContentUris.parseId(uri))
                }) as Cursor
                cursor.setNotificationUri(context.contentResolver, uri)
            }
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (MATCHER.match(uri)) {
            CODE_ALL_TRAVEL_NOTES -> "vnd.android.cursor.dir/" + AUTHORITY + "." + "travelnotes"
            CODE_TRAVEL_NOTE_ITEM -> "vnd.android.cursor.item/" + AUTHORITY + "." + "travelnotes"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (MATCHER.match(uri)) {
            CODE_TRAVEL_NOTE_ITEM -> {
                val context = context ?: return null
                val id: Long = (TravelDb.getInstanta(context)?.getTravelDao()
                    ?.insertNote(TravelNote.fromContentValues(values)!!) ?: context.contentResolver.notifyChange(uri, null )) as Long
                ContentUris.withAppendedId(uri, id)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(
        uri: Uri, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (MATCHER.match(uri)) {
            CODE_ALL_TRAVEL_NOTES -> throw IllegalArgumentException(
                "Invalid URI, cannot update without ID$uri"
            )
            CODE_TRAVEL_NOTE_ITEM -> {
                val context = context ?: return 0
                return (TravelDb.getInstanta(context)?.getTravelDao()
                    ?.deleteById(ContentUris.parseId(uri)) ?: context.contentResolver.notifyChange(uri, null )) as Int
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (MATCHER.match(uri)) {
            CODE_ALL_TRAVEL_NOTES -> throw IllegalArgumentException(
                "Invalid URI, cannot update without ID$uri"
            )
            CODE_TRAVEL_NOTE_ITEM -> {
                val context = context ?: return 0
                var count = 0
                val travelnote: TravelNote? = TravelNote.fromContentValues(values)
                if (travelnote != null) {
                    travelnote.LocationId = ContentUris.parseId(uri)
                    count = (TravelDb.getInstanta(context)?.getTravelDao()
                        ?.updateTravelNote(travelnote!!) ?: context.contentResolver.notifyChange(uri, null )) as Int
                }
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}