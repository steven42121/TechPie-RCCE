package club.geekpie.techpie.island

import org.json.JSONObject

/**
 * One frame of island content, computed on the Dart side from the cached
 * schedule and assignments.
 *
 * @param kind "class" or "deadline"
 * @param title course name / assignment title
 * @param subTitle short line under the icon: time range / due date
 * @param statusLine capsule status, e.g. 上课中 / 10分钟后开始 / 今天截止
 * @param detail right-column body: classroom / owning course for a deadline
 */
data class IslandFrame(
    val kind: String,
    val title: String,
    val subTitle: String?,
    val statusLine: String,
    val detail: String?,
) {
    /** Full notification text line. */
    val text: String?
        get() = listOfNotNull(subTitle, detail).joinToString(" · ").takeIf { it.isNotBlank() }

    companion object {
        fun fromJson(json: String): IslandFrame? {
            val o = try {
                JSONObject(json)
            } catch (e: Exception) {
                return null
            }
            val title = o.optString("title")
            if (title.isBlank()) return null
            return IslandFrame(
                kind = o.optString("kind", "class"),
                title = title,
                subTitle = optText(o, "subTitle"),
                statusLine = o.optString("statusLine"),
                detail = optText(o, "detail"),
            )
        }

        private fun optText(o: JSONObject, key: String): String? =
            if (o.isNull(key)) null else o.optString(key).takeIf { it.isNotBlank() }
    }
}
