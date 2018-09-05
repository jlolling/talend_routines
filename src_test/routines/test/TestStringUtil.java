package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import routines.StringUtil;

public class TestStringUtil {

	@Test
	public void testStripNonValidXMLCharacters() {
		System.out.println("#### testStripNonValidXMLCharacters");
		StringBuilder test = new StringBuilder(); 
		test.append("Hello World. ");
		test.append('\u0004');
		String test2 = StringUtil.stripNonValidXMLCharacters(test.toString());
		assertEquals(13, test2.length());
	}
	
	@Test
	public void testRemoveMultipleSpaces() {
		System.out.println("#### testRemoveMultipleSpaces");
		String test = "\nJan Lolling  has spaces    X ";
		String expected = "Jan Lolling has spaces X";
		String actual = StringUtil.reduceMultipleSpacesToOne(test);
		assertEquals(expected, actual);
	}

	@Test
	public void testFillPadding() {
		System.out.println("#### testFillPadding");
		String test = "1XXY";
		String actual = StringUtil.fillLeftPadding(test, 5, '0');
		String expected = "01XXY";
		assertEquals(expected, actual);
		actual = StringUtil.fillRightPadding(test, 5, '0');
		expected = "1XXY0";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInWithChain() {
		String test = "xyz ";
		String chain = "aaa|bbb|xyz |111";
		boolean actual = StringUtil.inChain(test, '|', chain);
		assertTrue(actual);
	}
	
	@Test
	public void testContainsToken() {
		String test = "Radio RBB Berliner Orchester";
		String token = "Berliner Orchester";
		boolean found = StringUtil.containsToken(test, token);
		assertTrue(found);
		test = "RBB as Berliner Orchester xas";
		found = StringUtil.containsToken(test, token);
		assertTrue(found);
		test = "Berliner orchester asdasd RBB";
		found = StringUtil.containsToken(test, token);
		assertTrue(found);
		test = "(Berliner orchester) asdasd RBB";
		found = StringUtil.containsToken(test, token);
		assertTrue(found);
		test = "<Berliner orchester> asdasd RBB";
		found = StringUtil.containsToken(test, token);
		assertTrue(found);
		test = "Berliner Orchestersdasd RBB";
		found = StringUtil.containsToken(test, token);
		assertTrue(found == false);
		test = "Berliner";
		found = StringUtil.containsToken(test, token);
		assertTrue(found == false);
		test = ",;.";
		found = StringUtil.containsToken(test, token);
		assertTrue(found == false);
		test = "Berliner Orchester";
		found = StringUtil.containsToken(test, token);
		assertTrue(found);
		test = "BerlinerOrchester";
		found = StringUtil.containsToken(test, token);
		assertTrue(found == false);
	}

	@Test
	public void testLength() {
		String test = null;
		int expected = 0;
		int actual = StringUtil.length(test);
		assertEquals(expected, actual);
		test = "äüö";
		expected = 3;
		actual = StringUtil.length(test);
		assertEquals(expected, actual);
	}

	@Test
	public void testCompareVersions() throws Exception {
		String v1 = "16.1.2";
		String v2 = "12.0.90";
		int expected = 1;
		int actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = "16.1.2";
		v2 = null;
		expected = 1;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = null;
		v2 = null;
		expected = 0;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = null;
		v2 = "1";
		expected = -1;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = "16.1.2";
		v2 = "17.0";
		expected = -1;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = "1.2";
		v2 = "17.0.3";
		expected = -1;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = "0.2";
		v2 = "0.0.3";
		expected = 1;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = "0.2";
		v2 = "0.3";
		expected = -1;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
		v1 = "1.0";
		v2 = "1";
		expected = 0;
		actual = StringUtil.compareVersions(v1, v2);
		assertEquals(expected, actual);
	}

	@Test
	public void testContainsTokens() throws Exception {
		String test = "Mit Katharina Eickhoff. Anonymus: La guillotine (Les Lunaisiens)Hector Berlioz: Hymne des Marseillais (Sylvia McNair, Sopran; Richard Leech, Tenor; Choir of St. Michael; Choir of St. David's; Baltimore Symphony Chorus and Orchestra, Leitung: David Zinman)Georg Philipp Telemann: Quartett Nr. 7, aus \"Nouveaux quatuors en six suites\" (Barthold Kuijken, Flöte; Sigiswald Kuijken, Violine; Wieland Kuijken, Viola da gamba; Gustav Leonhardt, Cembalo)Henry Charles Litolff: Maximilien Robespierre op. 55, Konzertouvertüre (Nordwestdeutsche Philharmonie, Leitung: Georg Fritzsch)Anonymus: La queue a Robespierre (Les Lunaisiens)Umberto Giordano: Ausschnitt aus dem musikalischen Drama \"André Chénier\" (Carlo Bergonzi, Tenor; Orchestra dell'Accademia Nazionale di Santa Cecilia, Leitung: Gianandrea Gavazzeni)John Zorn: Cat O'Nine Tails (Kronos Quartet)Hyacinthe Jadin: Sonate c-Moll op. 5,3, aus der Sammlung \"Trois sonates pour le forte-piano ... oeuvre 5e\" (Patrick Cohen, Hammerklavier)Reynaldo Hahn: A Chloris (Philippe Jaroussky, Countertenor; Jérôme Ducros, Klavier)Maurice Ravel: Suite Nr. 2, aus dem Ballett \"Daphnis und Chloe\" (Cleveland Orchestra, Leitung: Christoph von Dohnányi)Jean-Baptiste Lully: Te Deum, Motette (Amel Brahim-Djelloul und Aurore Bucher, Sopran; Reinoud Van Mechelen, Countertenor; Jeffrey Thompson, Tenor; Benoît Arnould, Bass; Capella Cracoviensis; Le Poeme Harmonique, Leitung: Vincent Dumestre)Jean-Baptiste Davaux: Sinfonie concertante mélée d'airs patriotiques G-Dur (Werner Ehrhardt und Andrea Keller, Violine; Concerto Köln)";
		List<String> list = new ArrayList<String>();
		list.add("Acht Brücken");
		list.add("Aldeburgh Festival");
		list.add("Arolser Barock-Festspiele");
		list.add("Avanti Summer Sounds");
		list.add("Bachfest Leipzig");
		list.add("Baltic Sea Festival");
		list.add("Bayreuther Festspiele");
		list.add("BBC Last Night of the Proms");
		list.add("BBC Proms");
		list.add("Brandenburgische Sommerkonzerte");
		list.add("Bregenzer Festspiele");
		list.add("Brühler Schlosskonzerte");
		list.add("Eurovision Young Musicians");
		list.add("Festspiele Mecklenburg-Vorpommern");
		list.add("Heidelberger Frühling");
		list.add("International Chamber Festival Utrecht");
		list.add("Internationale Darmstädter Ferienkurse für Neue Musik");
		list.add("Internationale Händel-Festspiele Göttingen");
		list.add("Internationale Jazzwoche Burghausen");
		list.add("Internationales Festival geistlicher Musik Freiburg");
		list.add("Internationales Musikfest Kreuth");
		list.add("Jazz Live with Friends");
		list.add("Kammermusiktage Mettlach");
		list.add("Kissinger Sommer");
		list.add("Lucerne Festival");
		list.add("Lugano Musica");
		list.add("MDR Musiksommer");
		list.add("Montpellier Festival");
		list.add("Moritzburg Festival");
		list.add("Moskauer Virtuosenkonzert");
		list.add("Mozartfest Würzburg");
//		list.add("Münchner Opernfestspiele");
		list.add("Musikfest Berlin");
		list.add("Musikfest Bremen");
		list.add("Musikfest Erzgebirge");
		list.add("Musikfestspiele Potsdam Sanssouci");
		list.add("Musikwoche Hitzacker");
		list.add("NDR Jazzworkshop");
		list.add("Rheingau Musik Festival");
		list.add("RheinVokal");
		list.add("Robeco SummerNights");
		list.add("Rudolstadt-Festival");
		list.add("Salzburger Festspiele");
		list.add("Schleswig-Holstein Musik Festival");
		list.add("Schubertiade");
		list.add("Schwetzinger SWR Festspiele");
		list.add("Styriarte");
		list.add("Tage Alter Musik im Saarland");
		list.add("Tage Alter Musik in Herne");
		list.add("Tage Alter Musik Regensburg");
		list.add("Tivoli Sommerklassisk");
		list.add("Traunsteiner Sommerkonzerte");
		list.add("Varna Summer");
		list.add("Verbier Festival");
		list.add("Weilburger Schlosskonzerte");
		list.add("West Cork Chamber Music Festival");
		list.add("Wiener Festwochen");
		list.add("Wittener Tage für neue Kammermusik");
		list.add("Young Euro Classic");
		list.add("Festival");
		list.add("Festivals");
		list.add("TFF Rudolstadt");
		list.add("Jazzfestival");
		list.add("Ultraschall");
		list.add("Heimbach");
		list.add("Bachfest");
		list.add("Bachwoche");
		list.add("Bodenseefestival");
		list.add("Musikfestspiele");
		list.add("Musikfesttage");
		list.add("Donaueschinger");
		list.add("Schlosskonzerte");
		list.add("Musiktage");
		list.add("Musiktagen");
		list.add("On stage");
		list.add("Live");
		list.add("Liveaufnahme");
		list.add("Livemitschnitt");
		list.add("Konzertmitschnitte");
		list.add("Konzertmitschnitt");
		list.add("Mitschnitt");
		list.add("Mitschnitte");
		list.add("Konzert vom");
		list.add("Konzerte vom");
		list.add("Musical");
		list.add("Aufnahme");
		list.add("Aufnahmen");
		list.add("Direkt aus");
		list.add("Aufzeichnung vom");
//		list.add("Ausschnitt");
		list.add("Ausschnitte");
		list.add("jazzahead!");
		list.add("X-JAZZ-Festival");
/*
		//list.add("Festival d'Aix-en-Provence");
		//list.add("Festival Musiq'3");
//		list.add("Übertragung");
		list.add("Unplugged");
		list.add("Konzert aus");
//		list.add("Eigenproduktion");
		list.add("Zeitversetzt");
		list.add("Aufzeichnung des");
//		list.add("Aufführung vom");
*/
		boolean found = (StringUtil.containsTokens(test, list) == null);
		assertTrue(found == false);
	}
	
}
