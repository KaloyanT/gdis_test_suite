Lebensversicherung abschließen

Erzählung: 
Um Versicherungsanträge in PSLife bearbeiten zu können
Als Leben-Vertragssachbearbeiter
Möchte ich eine Beitragslücke anlegen und dazu einen Beitragslückenausgleich durchführen

Szenario: Beitragslückenausgleich erfassen
Meta:

@category lena
@component am-pslife
Gegeben '<NAME>' erfasst einen Antrag für eine Rentenversicherung mit Vermögensaufbau mit Wirksamkeitsdatum (<VERTRAGSBEGINN_MONATE> Monate früher) für den Vertragspartner '<PARTNERNAME>'
Und im Vertrag als Buchungsvorgabe 'Beitrag' mit dem Beitrag <BEITRAG>EUR auswählt wird
Und im Hauptvertragsteil 'Vermögensaufbau (lebenslange Rentenversicherung)' das Ende der Beitragszahlungsdauer und der Ablauf des Vertragsteils auf das Datum <DATUM_ABLAUF> gesetzt werden und Konventionelle Anlage auf <KONVENTIONELLE_ANLAGE>%
Und der Antrag berechnet wird
Und in der Fondsauswahl zum Sparplan die Fonds '<FONDS>' mit den Sparquoten '<SPARQUOTEN>' hinzufügt werden
Und der Vermittler mit der Agenturnummer <AGENTURNUMMER> auswählt wird
Und in der Drucksteuerung die Druckausgaben auf Dokument <DOKUMENT> mit Versandweg <VERSANDWEG> zum Tagesdatum als Policendatum gesetzt werden
Und in der Drucksteuerung für den Empfänger die Versandart <VERSANDART> ausgewählt wird (Dokuement wird nicht geheftet)
Und der Antrag freigegeben wird
Wenn <NAME> den erstellten Vertrag sucht
Und die Vertragsrolle <VERTRAGSROLLE> ausgewählt wird
Und der Geschäftsvorfall 'Beitragslücke einrichten' mit Wirksamkeitsdatum (<MONATE> Monate später) ausgewählt wird
Und die Beitragslücke mit einer Dauer von <DAUER> Monat(en) erfasst wird
Und der Vertrag freigegeben wird
Und <NAME> den erstellten Vertrag sucht
Und die Vertragsrolle <VERTRAGSROLLE> ausgewählt wird
Und der Geschäftsvorfall 'Beitragslückenausgleich' ausgewählt wird
Und der Vertrag freigegeben wird
Dann ist der Beitragslückenausgleich für den Vertrag durchgeführt

Beispiele:
example/pslife/vertrag_bearbeiten/beitragslueckenausgleich/erfassen.csv

Szenario: Anlegen einer Beitragslücke mit zu frühem Wirksamkeitstermin
Meta:

@category lena
@component am-pslife
Gegeben '<NAME>' erfasst einen Antrag für eine Rentenversicherung mit Vermögensaufbau mit Wirksamkeitsdatum (<VERTRAGSBEGINN_MONATE> Monate früher) für den Vertragspartner '<PARTNERNAME>'
Und im Vertrag als Buchungsvorgabe 'Beitrag' mit dem Beitrag <BEITRAG>EUR auswählt wird
Und im Hauptvertragsteil 'Vermögensaufbau (lebenslange Rentenversicherung)' das Ende der Beitragszahlungsdauer und der Ablauf des Vertragsteils auf das Datum <DATUM_ABLAUF> gesetzt werden und Konventionelle Anlage auf <KONVENTIONELLE_ANLAGE>%
Und der Antrag berechnet wird
Und in der Fondsauswahl zum Sparplan die Fonds '<FONDS>' mit den Sparquoten '<SPARQUOTEN>' hinzufügt werden
Und der Vermittler mit der Agenturnummer <AGENTURNUMMER> auswählt wird
Und in der Drucksteuerung die Druckausgaben auf Dokument <DOKUMENT> mit Versandweg <VERSANDWEG> zum Tagesdatum als Policendatum gesetzt werden
Und in der Drucksteuerung für den Empfänger die Versandart <VERSANDART> ausgewählt wird (Dokuement wird nicht geheftet)
Und der Antrag freigegeben wird
Wenn <NAME> den erstellten Vertrag sucht
Und die Vertragsrolle <VERTRAGSROLLE> ausgewählt wird
Und der Geschäftsvorfall 'Beitragslücke einrichten' ausgewählt wird
Dann erscheint der Fehler: <FEHLER>

Beispiele:
example/pslife/vertrag_bearbeiten/beitragslueckenausgleich/mitFehlerDatumDreiMonateSpaeter.csv