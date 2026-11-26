package club.geekpie.techpie.island

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IslandAlarmsTest {
    @Test
    fun classFrameSchedulesRepaintBeforeExpiry() {
        val frame = IslandFrame("class", "Math", null, "In class", null, 20_000L)

        assertEquals(15_000L, IslandAlarms.nextRepaintAt(frame, 10_000L, 5_000L))
    }

    @Test
    fun repaintIsSkippedWhenFrameIsNotAClassOrIsNearExpiry() {
        val deadline = IslandFrame("deadline", "Essay", null, "Due soon", null, 20_000L)
        val endingSoon = IslandFrame("class", "Math", null, "In class", null, 14_999L)
        val noExpiry = IslandFrame("class", "Math", null, "In class", null, null)

        assertNull(IslandAlarms.nextRepaintAt(deadline, 10_000L, 5_000L))
        assertNull(IslandAlarms.nextRepaintAt(endingSoon, 10_000L, 5_000L))
        assertNull(IslandAlarms.nextRepaintAt(noExpiry, 10_000L, 5_000L))
    }

    @Test
    fun delayedClassAlarmResolvesToTheNewerDeadlineFrame() {
        val course = frameJson("class", "Math", 300L)
        val deadline = frameJson("deadline", "Essay", 500L)
        val delivery = resolve(
            currentFrame = course,
            currentAt = 100L,
            scheduledAt = 100L,
            incoming = course,
            items = listOf(
                IslandAlarms.Item(100L, course, 300L),
                IslandAlarms.Item(200L, deadline, 500L),
            ),
            now = 200L,
        )

        assertNotNull(delivery)
        assertEquals(deadline, delivery?.frameJson)
        assertFalse(delivery?.dismiss ?: true)
        assertNull(IslandAlarms.nextRepaintAt(
            IslandFrame("deadline", "Essay", null, "Due", null, 500L),
            200L,
            5_000L,
        ))
    }

    @Test
    fun nullEventClearsCourseAndExpiredFrameResolvesToDismissal() {
        val course = frameJson("class", "Math", 150L)
        val cleared = resolve(
            currentFrame = course,
            currentAt = 100L,
            scheduledAt = 100L,
            incoming = course,
            items = listOf(
                IslandAlarms.Item(100L, course, 150L),
                IslandAlarms.Item(150L, null),
            ),
            now = 150L,
        )
        val expired = resolve(
            currentFrame = course,
            currentAt = 100L,
            scheduledAt = 100L,
            incoming = course,
            items = listOf(IslandAlarms.Item(100L, course, 150L)),
            now = 151L,
        )

        assertTrue(cleared?.dismiss == true)
        assertNull(cleared?.frameJson)
        assertTrue(expired?.dismiss == true)
        assertNull(expired?.frameJson)
    }

    @Test
    fun obsoleteGenerationAndOlderFrameCallbacksCannotReplaceCurrentState() {
        val course = frameJson("class", "Math", 300L)
        val deadline = frameJson("deadline", "Essay", 500L)
        val obsoleteGeneration = IslandAlarms.resolveFrameForDelivery(
            currentGeneration = 2L,
            currentFrameJson = deadline,
            currentExpiresAt = 500L,
            currentFrameAtMillis = 200L,
            generation = 1L,
            scheduledAt = 100L,
            incomingFrameJson = course,
            isRepaint = false,
            items = listOf(IslandAlarms.Item(100L, course, 300L)),
            nowMillis = 250L,
        )
        val olderEvent = IslandAlarms.resolveFrameForDelivery(
            currentGeneration = 2L,
            currentFrameJson = deadline,
            currentExpiresAt = 500L,
            currentFrameAtMillis = 200L,
            generation = 2L,
            scheduledAt = 100L,
            incomingFrameJson = course,
            isRepaint = false,
            items = listOf(
                IslandAlarms.Item(100L, course, 300L),
                IslandAlarms.Item(150L, deadline, 500L),
            ),
            nowMillis = 250L,
        )

        assertNull(obsoleteGeneration)
        assertNull(olderEvent)
    }

    private fun resolve(
        currentFrame: String?,
        currentAt: Long,
        scheduledAt: Long,
        incoming: String?,
        items: List<IslandAlarms.Item>,
        now: Long,
    ) = IslandAlarms.resolveFrameForDelivery(
        currentGeneration = 1L,
        currentFrameJson = currentFrame,
        currentExpiresAt = items.firstOrNull()?.expiresAtMillis,
        currentFrameAtMillis = currentAt,
        generation = 1L,
        scheduledAt = scheduledAt,
        incomingFrameJson = incoming,
        isRepaint = false,
        items = items,
        nowMillis = now,
    )

    private fun frameJson(kind: String, title: String, expiresAt: Long) =
        "{\"kind\":\"$kind\",\"title\":\"$title\",\"expiresAt\":$expiresAt}"
}
