Feature: Turnierdaten importieren

  @cleanData @smoketest
  Scenario: Minimales Turnier mit 2 Teams importieren
    When ich importiere ein minimales Turnier mit einem Spiel
    Then der Spielplan besteht aus dem importierten Spiel

