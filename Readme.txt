Was könnte man besser machen?
-Model-View-Controller implementieren. Habe viel Logik in GUI-Klasse
-Swing-Worker-Threads verwenden statt ExecutorService

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

Sonstiges:
-Repository auf Github laden