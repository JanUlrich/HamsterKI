HamsterKI
=========

Projekt für Vorlesung Wissensbasierte Systeme.

Aufgabe
-------
Entwerfen Sie ein Hamster Programm in Java, in dem der Hamster ein beliebig großes unbekanntes Terrain von einem frei wählbaren Startpunkt aus abläuft und möglichst viele Körner friss. Als Hindernisse befinden sich Mauern auf dem Territorium.    
    
Randbedingungen:
* Der Hamster hat einen einstellbaren Start-Energielevel, der bei jeder Vor- und bei jeder 90 Grad Dreh-Bewegung einen Energiepunkt verliert. Der Hamster kann nicht Rückwärts laufen.
* Der Hamster bleibt stehen, sobald der Energielevel 0 ist. Es wird ausgegeben, wie viele Körner er aufgesammelt hat und wie viele noch auf dem Terrain liegen (jeweils absolut und prozentual), sowie wie viele 90 Grad Drehbewegungnen und Vorwärtsbewegungen aufgewendet wurden. Sollte er vorher alle Körnen gefunden haben wird die Ausgabe um den Restenergielevel ergänzt.
* Der Hamster darf weder die Größe des Territoriums noch Hindernisse jenseits seiner unmittelbaren Umgebung durch Abfrage ermitteln.
* Sie dürfen eine Hamster-Funktion schreiben, bei der die 8 Kachel um den Hamster auf Mauern abgefragt werden. Diese "Sensorabfrage" darf energiefrei erfolgen.
* Der Hamster daf je Feld nur ein Korn aufnehmen. Er darf nicht testen, ob noch weitere Körner auf der Kachel liegen. Für eine eventuelle weitere Kornsammlung muss er die Kachel erneut von einer anderen Kachel aus anlaufen.

> Abgabetermin: 7. Mai

KI-Step in der Klasse MeinHamster
---------------------------------

1. Die KI-Wegberechnung wird aufgerufen   
	int richtung = berechneZug();   
Variable 'richtung' ist die von der KI berechnete optimale Richtung für den nächsten Zug.
2. Der Hamster wird in Richtung 'richtung' gedreht und nach vorne bewegt   
	dreheInRichtung(richtung);   
	vor();
3. Der Hamster scannt sein Umfeld
4. Der Hamster versucht ein Korn aufzusammeln. Liegt kein Korn mehr auf dem Feld, dann wird dies auf der Karte vermerkt.

Javadoc
=======

Javadoc generieren
------------------
Das Javadoc-Tool ist im JDK enthalten.
# Generieren von Javadoc:
    cd /HAMSTERKI_SOURCE_VERZEICHNIS/   
    javadoc *.java -d javadoc

Github-Pages
------------
Die Javadoc kann bei Github-Pages gehostet werden: [Creating Github-pages]("https://help.github.com/articles/creating-project-pages-manually")    
HOWTO Deploy github pages:
1. Zu Branch 'gh-pages' wechseln:
    git checkout gh-pages
2. Änderungen

