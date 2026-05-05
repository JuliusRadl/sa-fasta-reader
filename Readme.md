# Feedback Schmidt:
- Parser funktioniert nicht, wenn zb vorne in FASTA Kommentar steht: Muss möglichst wenig Annahmen treffen über Input
- Soll mich für Parser an ">" als Sequenzstarter orientieren
- "while(true)" immer vermeiden
- ExecutorService ruhig ganz am Anfang einmal initialisieren
- Model-View-Controller wäre ne gute Idee
- Statt json mit ArrayList komplett einzulesen, fremden Parser verwenden und Objekt für Objekt auslesen
- Dann Objekt für Objekt in die Pipe stecken (evtl. sleep einbauen) => Infos für Notify stehen in Leßke-Unterlagen
- Speicher freigeben, Referenzen löschen sehr wichtig
- Manchmal GarbageCollector manuell aufrufen, sonst kann er CPU hoggen, wenn viel auf einmal aufgeräumt werden muss
- Fürs Error Handling gibt's in Unternehmen meistens Guidelines, kann auf viele Arten gemacht werden
- Interface vor allem für Controller nützlich, wenn man später GUI weglassen und Logik automatisieren will (zb auch bei Tests)
- Funktionen zum Datenfluss im Model sind ok, aber Business Logik gehört eigentlich in Controller
- Interfaces nach Konvention mit I im Namen beginnen

# Sonstiges:
- Repository auf Github laden

# Erkenntnisse:
- Sinn von MVC ist es, Darstellung und Logik zu entkoppeln. Das Modell soll nichts von allem andere wissen
- Bei MVP noch stärker: View komplett dumb, kann nur Presenter über Clicks etc. benachrichtigen, der dann alles weitere handelt,
View weiß auch nichts von Model
- Action Listener bleiben im View, der den Controller benachrichtigt
- Zeilenweisen, tabellerischen CLI-Output auch zeilenweise mit BufferedReader auslesen, byteweise unnötig

# To do A1:
- Duplicate Checking mit Hashmaps?
- Alle direkten Zugriffe (auch auf MVC-Objekte) durch getter (und evtl setter) ersetzen
- Fehler-Anzeige mit SwingWorkern handeln...
- Confirm-Fenster für Einlesen und Abspeichern anzeigen
- Confirmation/completion popups fürs lesen und speichern

# To do A2:
- Blast-Output als Object[][]-Array parsen
- als JTable darstellen in eigenem Fenster

# Fragen für Feedback:
- Was genau ist der Vorteil von MVC gegenüber MVP? Hab gelesen, dass man viele Controller und Views haben kann, aber nur einen Presenter...
- Hab mir jetzt gedacht, Error Handling betreibt der Controller, damit ich sie notfalls anzeigen kann, deswegen throws im Model
- Tipps für die Browser-Aufgabe? Wie soll ein externer Browser wieder aus dem Programm heraus geschlossen werden und warum
