Feedback Schmidt:
-Parser funktioniert nicht, wenn zb vorne in FASTA Kommentar steht: Muss möglichst wenig Annahmen treffen über Input
-Soll mich für Parser an ">" als Sequenzstarter orientieren
-"while(true)" immer vermeiden
-ExecutorService ruhig ganz am Anfang einmal initialisieren
-Model-View-Controller wäre ne gute Idee
-Statt json mit ArrayList komplett einzulesen, fremden Parser verwenden und Objekt für Objekt auslesen
-Dann Objekt für Objekt in die Pipe stecken (evtl. sleep einbauen) => Infos für Notify stehen in Leßke-Unterlagen
-Speicher freigeben, Referenzen löschen sehr wichtig
-Manchmal GarbageCollector manuell aufrufen, sonst kann er CPU hoggen, wenn viel auf einmal aufgeräumt werden muss
-Fürs Error Handling gibt's in Unternehmen meistens Guidelines, kann auf viele Arten gemacht werden
-Interface vor allem für Controller nützlich, wenn man später GUI weglassen und Logik automatisieren will (zb auch bei Tests)
-Funktionen zum Datenfluss im Model sind ok, aber Business Logik gehört eigentlich in Controller
-Interfaces nach Konvention mit I im Namen beginnen

Sonstiges:
-Repository auf Github laden

Erkenntnisse:
-Sinn von MVC ist es, Darstellung und Logik zu entkoppeln. Das Modell soll nichts von allem andere wissen
-Bei MVP noch stärker: View komplett dumb, kann nur Presenter über Clicks etc. benachrichtigen, der dann alles weitere handelt,
View weiß auch nichts von Model
-Action Listener bleiben im View, der den Controller benachrichtigt

To do:
-Model erstellen DONE
-JsonParser Consumer & Producer
-Json Parsing-Methode
-Duplicate Checking mit Hashmaps?
-Update-List-Methode, die nach SwingWorker aufgerufen wird DONE
-Alle direkten Zugriffe (auch auf MVC-Objekte) durch getter (und evtl setter) ersetzen
-Alle Buttons disablen oder busy state, wenn einer gedrückt wurde DONE
-Fehler-Anzeige mit SwingWorkern handeln...
-Confirm-Fenster für Einlesen und Abspeichern anzeigen
-Swing Integrate Desktop Class -> Default Programme (zb Browser) aufrufen
