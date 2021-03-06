[![Build Status](http://ci.asdfd.de/buildStatus/icon?job=BeaconLocation)](http://ci.asdfd.de/job/BeaconLocation)

# BeaconLocation / MachineLocation Projektdokumentation

## Richtlinien
1. Entwicklungsprozess:
  * Keine Fehler und Warnungen im Produkt
  * Findbugs
  * PMD
  * Android Lint
  * TODO und FIXME beachten
  * Ergebnisse der JUnit tests beachten

2. Fehlermanagement
  * Fix-first Herangehensweise
  * Fehlerbehebung hat Vorrang vor Neuen Features
  * Dokumentation von Fehlern (Bugtracker)
  * Referenzierung von Fixes in der Commit-Message

3. Git Best-practice
  * Häufige commits
  * Zusammengehörende Änderungen in einem Commit
  * Commit-Nachricht aussagekräftig und unabhängig von vorherigen Commits

4. Dokumentation
  * Kommentare so einpflegen, dass man auch nach 3 Monaten den Sinn des
    Quellcodes nachvollziehen kann.
  * Insbesondere bei von außen zugänglichen Methoden, Header-Kommentar-Blocks
    benutzen.
