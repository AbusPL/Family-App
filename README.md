# Family App

Pierwszy szkielet aplikacji rodzinnej w **Kotlin Multiplatform + Compose Multiplatform**.
Pierwszy uruchamiany target to przeglądarka (Kotlin/Wasm). Nie wymaga Maca, Android SDK ani backendu.

## Wymagania

- JDK 17 (`java -version`; ustaw `JAVA_HOME`, jeśli potrzeba).
- Aktualny Chrome, Edge, Firefox lub Safari z obsługą WasmGC.
- Internet przy pierwszym uruchomieniu — Gradle pobiera zależności, Node.js i Yarn.
- Opcjonalnie IntelliJ IDEA z obsługą Kotlin Multiplatform.

Wersje przypięte: Kotlin / Compose compiler 2.2.20, Compose Multiplatform 1.9.0,
Gradle 8.14.3. Celowo mały, spójny zestaw startowy, bez dynamicznych wersji.

## Uruchomienie

```bash
git clone https://github.com/AbusPL/Family-App.git
cd Family-App
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Windows: użyj `gradlew.bat` zamiast `./gradlew`.
Otwórz **http://localhost:8080**. Pierwsza kompilacja może zająć kilka minut.
Zatrzymanie serwera: Ctrl+C. Jeśli port jest zajęty, zatrzymaj inny serwer lub zmień
`port` w `composeApp/build.gradle.kts`.

## Pierwszy przebieg

1. Na ekranie logowania wpisz login **dziecko**, hasło **demo**.
2. Zobacz ekran główny z listą trzech przykładowych obowiązków.
3. Zaznacz obowiązek — licznik punktów rośnie; odznaczenie odejmuje punkty.
4. Wybierz „Wyloguj” — wrócisz do logowania, punkty zostaną wyzerowane.
5. Błędne dane logowania pokazują komunikat; puste pola blokują przycisk.

**To wyłącznie demo interfejsu.** Dane i sesja istnieją tylko w pamięci karty;
odświeżenie strony je usuwa. Nie ma prawdziwych kont, zapisywania haseł,
autologowania ani synchronizacji pomiędzy urządzeniami. Nie wpisuj prawdziwych danych.

## Struktura

```text
shared/                      modele i logika demo, bez zależności od UI
  src/commonMain/            FamilyMember, Chore, DemoFamily
  src/commonTest/            walidacja logowania demo i liczenia punktów
composeApp/                  współdzielony interfejs Compose
  src/commonMain/            ekran logowania i ekran główny
  src/wasmJsMain/             punkt startowy przeglądarki i index.html
gradle/wrapper/              Gradle Wrapper
```

`shared` ma targety Wasm i JVM (JVM służy do szybkich testów logiki).
`composeApp` ma na razie tylko Wasm. UI i logika w `commonMain` stanowią bazę pod
kolejne targety Android/iOS; aplikacje natywne nie są jeszcze skonfigurowane.

## Sprawdzenie i paczka webowa

```bash
./gradlew :shared:jvmTest :composeApp:wasmJsBrowserDistribution
```

Wynik: `composeApp/build/dist/wasmJs/productionExecutable/`.
Aby sprawdzić paczkę lokalnie (jeżeli masz Python 3):

```bash
python3 -m http.server 8080 --directory composeApp/build/dist/wasmJs/productionExecutable
```

Otwórz http://localhost:8080 — nie otwieraj `index.html` przez `file://`.
Paczka nadaje się do hostingu statycznego z obsługą plików `.wasm` (`application/wasm`),
JS i pozostałych zasobów.
CI w GitHub Actions wykonuje testy logiki i buduje paczkę webową jako artefakt `family-app-web`.

## Publikacja przez GitHub Pages

Docelowy adres demo: **https://abuspl.github.io/Family-App/**.

Jednorazowo w repozytorium wybierz **Settings → Pages → Build and deployment →
Source: GitHub Actions**. Wymaga to uprawnień administratora repozytorium.
Nie ustawiaj publikacji bezpośrednio z plików źródłowych Kotlin.

Workflow `.github/workflows/build.yml` po każdym pushu do `main` wykonuje testy,
buduje wersję webową i publikuje ją w GitHub Pages. Pull requesty przechodzą
testy i kompilację, ale nie publikują strony. Możesz też uruchomić workflow ręcznie
przez **Actions → Build, test and publish → Run workflow** (gałąź `main`).

Status i wynik publikacji znajdziesz w zadaniu `deploy` w GitHub Actions.
Jeżeli etap „Configure GitHub Pages” zgłasza „Not Found”, sprawdź powyższe
ustawienie Source i ponów nieudane zadanie. Udany build sam w sobie nie oznacza,
że strona została opublikowana.

Strona jest publicznym demo. Nie używaj prawdziwych danych rodziny;
logowanie i punkty nadal działają wyłącznie w pamięci bieżącej karty.

## Kolejne etapy

1. Dopracować pierwszy przepływ: profile rodzica/dziecka, obowiązki przypisane do dnia,
   stany wykonania i zasady potwierdzania przez rodzica.
2. Dodać backend **.NET** i bazę danych: prawdziwe konta, uprawnienia,
   bezpieczne sesje oraz synchronizacja. Autologowanie oprzeć o sesję/token,
   bez przechowywania hasła w przeglądarce.
3. Dodać kalendarz, punkty za oceny oraz miesięczne rozliczenia z ochroną
   przed wielokrotnym naliczaniem. Ustalić zasady przeliczenia na nagrody.
4. Sprawdzić web na telefonach, dostępność, obsługę klawiatury i testy UI.
5. Dodać targety Android/iOS do modułów KMP i natywne punkty startowe.
6. Przygotować hosting backendu i środowiska do prawdziwego użycia przez rodzinę;
   zastąpić logowanie demo prawdziwym i dodać trwały zapis danych.

## Dokumentacja technologii

- [Kotlin/Wasm i Compose](https://kotlinlang.org/docs/wasm-get-started.html)
- [Kompatybilność Compose Multiplatform](https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html)
