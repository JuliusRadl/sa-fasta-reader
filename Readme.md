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
- Exceptions im Interface definieren ist super!

# Erkenntnisse:
- Sinn von MVC ist es, Darstellung und Logik zu entkoppeln. Das Modell soll nichts von allem andere wissen
- Bei MVP noch stärker: View komplett dumb, kann nur Presenter über Clicks etc. benachrichtigen, der dann alles weitere handelt,
View weiß auch nichts von Model
- Action Listener bleiben im View, der den Controller benachrichtigt
- Zeilenweisen, tabellerischen CLI-Output auch zeilenweise mit BufferedReader auslesen, byteweise unnötig
- Packages sind wie Ordner, sinnvoll zum Strukturieren des Projekts
- Streams nicht mischen bei der Socket-Kommunikation
- Kommunikation kann man zb über ein length prefix regeln
- DataInputStreams erlauben Übertragung primitver Datentypen zusammen mit deren Länge (auch bei Strings)
- Bei Sockets immer DataInputStreams verwenden, damit Daten verschiedener Typen übertragen werden können, nicht nur zb Strings
- BufferedReader "stehlen" Bytes, lesen also evtl mehr als als gewünscht
- PrintWriter sind nützlich, weil sie autoflushen können und automatisch Zeilenumbrüche für BufferedReader anhängen
- Benutze Strategy-Pattern, um Verhalten von Runnable zu ändern
- Poison Pill Pattern, um Ende über Streams zu signalisieren, ohne sie zu schließen (damit der Erzeuger des Streams ihn später schließen kann)
- Nicht jede Architektur-Optimierung lohnt sich: Zwei neue Consumer/Producer für Client/Server zu schreiben, wäre definitiv einfacher gewesen als Strategy + Poison Pill Pattern 

# To do A1:
- Duplicate Checking mit Hashmaps?
- Alle direkten Zugriffe (auch auf MVC-Objekte) durch getter (und evtl setter) ersetzen
- Fehler-Anzeige mit SwingWorkern handeln...
- Methoden mit Logik in Controller integrieren

# To do A2:
- Spalten Datentypen zuweisen & sortierbar machen

# To do A3:
- Base64 Encoding für die Byte-Übertragung
- (Parser so umschreiben, dass sie von Strings abhängig sind)
- Sender schreiben, der Zeile für Zeile mit writeUTF an den Socket schreibt und am Ende ein Schlusssignal sendet
- ByteParser schreiben, der von DataInputStream abhängig ist und auf ein Schlusssignal wartet
- ByteConsumer schreiben, der von DataInputStream abhängig ist und auf EndOfStreamToken wartet
- SequenceReadable & Writeable wieder entfernen und in entsprechende Parser & Consumer integrieren

# Fragen für Feedback: