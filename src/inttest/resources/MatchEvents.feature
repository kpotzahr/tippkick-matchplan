@event @cleanData
Feature: Match automatisch starten und Ergebnis bekannt geben

  Scenario: Spiel importieren und Start Event prüfen
    When ich importiere ein Spiel, das gleich startet
    Then es wird ein korrektes Start-Event gesendet

  Scenario: Ergbebnis bekannt geben
    When ich importiere ein Spiel, das gleich startet
    And ich setze das Resultat auf 2:1
    Then das Ergbnis wird über ein Event bekanntgegeben
