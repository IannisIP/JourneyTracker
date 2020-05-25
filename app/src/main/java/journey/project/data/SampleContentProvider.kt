package journey.project.data

import android.content.*
import android.database.Cursor
import android.net.Uri
import journey.project.models.TravelNote

class SampleContentProvider : ContentProvider() {
    companion object {
        /** The authority of this content provider.  */
        const val AUTHORITY = "journey.project.data"

        /** The URI for the TravelNote table.  */
        val URI_CHEESE = Uri.parse(
            "content://$AUTHORITY/travelnotes"
        )

        /** The match code for some items in the Cheese table.  */
        private const val CODE_CHEESE_DIR = 1

        /** The match code for an item in the Cheese table.  */
        private const val CODE_CHEESE_ITEM = 2

        /** The URI matcher.  */
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(
                AUTHORITY,
                "travelnotes",
                CODE_CHEESE_DIR
            )
            MATCHER.addURI(
                AUTHORITY,
                "travelnotes" + "/*",
                CODE_CHEESE_ITEM
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
        return if (code == CODE_CHEESE_DIR || code == CODE_CHEESE_ITEM) {
            val context = context ?: return null
            val TravelNote = TravelDb.getInstanta(context)?.getTravelDao()

            var cursor: Cursor? = null
            if (TravelNote != null) {
                cursor = (if (code == CODE_CHEESE_DIR) {
                    TravelNote.queryNotes()
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
            CODE_CHEESE_DIR -> "vnd.android.cursor.dir/" + AUTHORITY + "." + "travelnotes"
            CODE_CHEESE_ITEM -> "vnd.android.cursor.item/" + AUTHORITY + "." + "travelnotes"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (MATCHER.match(uri)) {
            CODE_CHEESE_ITEM -> {
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
            CODE_CHEESE_DIR -> throw IllegalArgumentException(
                "Invalid URI, cannot update without ID$uri"
            )
            CODE_CHEESE_ITEM -> {
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
            CODE_CHEESE_DIR -> throw IllegalArgumentException(
                "Invalid URI, cannot update without ID$uri"
            )
            CODE_CHEESE_ITEM -> {
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