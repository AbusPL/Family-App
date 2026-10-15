package pl.familyapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pl.familyapp.shared.DemoFamily
import pl.familyapp.shared.FamilyMember

@Composable
fun App() {
    val demo = remember { DemoFamily() }
    var member by remember { mutableStateOf<FamilyMember?>(null) }
    var completed by remember { mutableStateOf(setOf<String>()) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF315C48))) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Column(
                    Modifier.widthIn(max = 560.dp).fillMaxWidth()
                        .verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    Text("Family App", style = MaterialTheme.typography.headlineLarge)
                    Text("Małe obowiązki, wspólne cele.", style = MaterialTheme.typography.titleMedium)
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium) {
                        Text("Tryb demo • dane znikają po odświeżeniu strony.", Modifier.padding(16.dp))
                    }
                    val currentMember = member
                    if (currentMember == null) {
                        LoginScreen { login, password ->
                            demo.signIn(login, password)?.also { member = it } != null
                        }
                    } else {
                        Text("Cześć, ${currentMember.displayName}!", style = MaterialTheme.typography.headlineSmall)
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Twoje punkty w tej sesji")
                                Text("${demo.totalPoints(completed)} pkt", style = MaterialTheme.typography.headlineLarge)
                            }
                        }
                        Text("Obowiązki — przykładowa lista", style = MaterialTheme.typography.titleLarge)
                        demo.chores.forEach { chore ->
                            Card(Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = chore.id in completed, onCheckedChange = { checked ->
                                        completed = if (checked) completed + chore.id else completed - chore.id
                                    })
                                    Column(Modifier.weight(1f).padding(start = 8.dp)) {
                                        Text(chore.title)
                                        Text("${chore.points} pkt", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                        OutlinedButton(onClick = { member = null; completed = emptySet() }) { Text("Wyloguj") }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(onLogin: (String, String) -> Boolean) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    Text("Zaloguj się", style = MaterialTheme.typography.headlineSmall)
    Text("Konto demonstracyjne: dziecko / demo. Nie wpisuj prawdziwego hasła.")
    OutlinedTextField(
        value = login, onValueChange = { login = it; error = false },
        label = { Text("Login") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
        isError = error,
    )
    OutlinedTextField(
        value = password, onValueChange = { password = it; error = false },
        label = { Text("Hasło demo") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(), isError = error,
    )
    if (error) Text("Nieprawidłowy login lub hasło demo.", color = MaterialTheme.colorScheme.error)
    Button(
        onClick = { error = !onLogin(login, password); password = "" },
        enabled = login.isNotBlank() && password.isNotBlank(), modifier = Modifier.fillMaxWidth(),
    ) { Text("Wejdź do aplikacji") }
}
