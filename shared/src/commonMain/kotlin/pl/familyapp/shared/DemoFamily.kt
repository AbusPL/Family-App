package pl.familyapp.shared

data class FamilyMember(val displayName: String)
data class Chore(val id: String, val title: String, val points: Int)

/** Local demo only. No real authentication or durable storage. */
class DemoFamily {
    val chores = listOf(
        Chore("room", "Posprzątaj pokój", 10),
        Chore("table", "Nakryj do stołu", 5),
        Chore("plants", "Podlej kwiaty", 5),
    )

    fun signIn(login: String, password: String): FamilyMember? =
        if (login.trim() == "dziecko" && password == "demo") FamilyMember("Dziecko") else null

    fun totalPoints(completedIds: Set<String>): Int =
        chores.filter { it.id in completedIds }.sumOf { it.points }
}
