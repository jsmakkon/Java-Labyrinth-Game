import static org.junit.Assert.*;

import org.junit.Test;



public class Labyrinthtesti {
	
	@Test
    public void testCreateNormal500() {
		Labyrinth laby = new Normal(500,500, -1);
		
		assertTrue("Labyrintti ei saa olla null", laby != null);
	}
	
	@Test
    public void testCreateNormal() {
		Labyrinth laby = new Normal(200,200, -1);
		
		assertTrue("Labyrintti ei saa olla null", laby != null);
	}
	
	@Test
    public void testCreateWeave() {
		Labyrinth laby = new Weave(200,200, -1);
		
		assertTrue("Labyrintti ei saa olla null", laby != null);
	}
	
	@Test
    public void testSolveNormal() {
		Labyrinth laby = new Normal(200,200, -1);
		
		Player pelaaja = new Player(5,5,"esimerkki");
		
		assertTrue("Ratkaisualgoritmi pit‰isi palauttaa true", laby.ratkaise(pelaaja));
	}
	
	@Test
    public void testSolveWeave() {
		Labyrinth laby = new Weave(200,200, -1);
		
		Player pelaaja = new Player(5,5,"esimerkki");
		
		assertTrue("Ratkaisualgoritmi pit‰isi palauttaa true", laby.ratkaise(pelaaja));
	}
	
	
	
	@Test
	public void testHighscore() {
		Player pelaaja = new Player(5,5,"esimerkki");
		
		// lis‰t‰‰n pelaajalle askelia
		pelaaja.move(0);
		pelaaja.move(0);
		pelaaja.move(0);
		pelaaja.move(0);
		pelaaja.move(0);
		
		pelaaja.move(1);
		pelaaja.move(1);
		pelaaja.move(1);
		pelaaja.move(1);
		pelaaja.move(1);
		
		pelaaja.move(0);
		pelaaja.move(0);
		pelaaja.move(0);
		pelaaja.move(0);
		pelaaja.move(0);
		
		Highscore high = new Highscore();
		
		high.addName(pelaaja, 100);
		
		assertTrue("Pelaajan nimi pit‰isi olla esimerkki", high.getName(0).equals(pelaaja.getNimi()));
	}
}
