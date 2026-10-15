package pl.familyapp.shared

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DemoFamilyTest {
    private val demo = DemoFamily()

    @Test fun demoLoginAcceptsOnlyDemoCredentials() {
        assertNotNull(demo.signIn(" dziecko ", "demo"))
        assertNull(demo.signIn("dziecko", "wrong"))
        assertNull(demo.signIn("", ""))
    }

    @Test fun pointsCountKnownCompletedChoresOnce() {
        assertEquals(0, demo.totalPoints(emptySet()))
        assertEquals(15, demo.totalPoints(setOf("room", "table", "unknown")))
        assertEquals(20, demo.totalPoints(demo.chores.map { it.id }.toSet()))
    }
}
