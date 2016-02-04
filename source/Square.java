/*
 * Square -luokka kuvaa yhtä labyrintin neliötä. Olio sisältää tiedot
 * neliön sijainnista, seinin lukumäärästa ja paikoista sekä väristä.
 */
public class Square {
	
	private int walls; //Seinien lukumaara
	private int x, y; //Koordinaatit
	private int color; //Neliön väri
	private Paikka places; //seinien paikat
	
	//Enumi kertoo missä kohtaa neliössä on seiniä
	public enum Paikka{
		N, E, S, W, NE, NS, NW, ES, 
		EW, SW, NEW, NES, ESW, NSW, FULL, EWali, NSali;
	}
	
	
	
	//Konstruktori. Asettaa 4 seinää sisältävän neliön annettuun kordinaatteihin.
	public Square(int x, int y) {
		this.color = 0;
		this.places = Paikka.FULL;
		this.walls = 4;
		this.x = x;
		this.y = y;
	}
	
	//Palauttaa seinien lukumäärän
	public int getWalls() {
		return this.walls;
	}
	
	//Kertoo onko annetussa suunnassa seinää. Palauttaa true tai false.
	public boolean isWall(Paikka suunta) {
		if (suunta == Paikka.N) {
			if (this.places == Paikka.N || this.places == Paikka.NE || this.places == Paikka.NS || this.places == Paikka.NW ||
					this.places == Paikka.NEW || this.places == Paikka.NES || this.places == Paikka.NSW || this.places == Paikka.FULL) {
				return true;
			}
		}
		if (suunta == Paikka.E) {
			if (this.places == Paikka.E || this.places == Paikka.NE || this.places == Paikka.ES || this.places == Paikka.EW ||
					this.places == Paikka.NEW || this.places == Paikka.NES || this.places == Paikka.ESW || this.places == Paikka.FULL) {
				return true;
			}
		}
		if (suunta == Paikka.S) {
			if (this.places == Paikka.S || this.places == Paikka.NS || this.places == Paikka.ES || this.places == Paikka.SW ||
					this.places == Paikka.ESW || this.places == Paikka.NES || this.places == Paikka.NSW || this.places == Paikka.FULL) {
				return true;
			}
		}
		if (suunta == Paikka.W) {
			if (this.places == Paikka.W || this.places == Paikka.EW || this.places == Paikka.SW || this.places == Paikka.NW ||
					this.places == Paikka.NEW || this.places == Paikka.ESW || this.places == Paikka.NSW || this.places == Paikka.FULL) {
				return true;
			}
		}
		return false;
	}
	
	//Palauttaa seinien paikat
	public Paikka getPlaces() {
		return this.places;
	}
	
	//Asettaa neliön seinien paikat ja muuttaa seinien lukumäärän oikeaksi.
	public void setPlace(Paikka paikka) {
		this.places = paikka;
		if (paikka == Paikka.EWali || paikka == Paikka.NSali) {
			this.walls = 0;
		}
		if (paikka == Paikka.FULL) {
			this.walls = 4;
		}
		if (paikka == Paikka.NEW || paikka == Paikka.NES || paikka == Paikka.ESW || paikka == Paikka.NSW) {
			this.walls = 3;
		}
		if (paikka == Paikka.NE || paikka == Paikka.NS || paikka == Paikka.EW || paikka == Paikka.NW
			|| paikka == Paikka.ES || paikka == Paikka.SW) {
			this.walls = 2;
		}
		if (paikka == Paikka.N || paikka == Paikka.S || paikka == Paikka.E || paikka == Paikka.W) {
			this.walls = 1;
		}
	}
	
	//Palauttaa neliön x -koordinaatin
	public int getX() {
		return this.x;
	}
	
	//Palauttaa neliön y -koordinaatin
	public int getY() {
		return this.y;
	}
	
	//Kertoo neliön värillisyyden.
	public int getColor() {
		return this.color;
	}
	
	//Merkitsee palikan värilliseksi ratkaisun näyttämistä varten.
	public void setColor(int i) {
		this.color = i;
	}
	
	//Tekee alikulun nykyisestä neliöstä. Alikuljettava neliö on oltava EW tai NS, eli suora reitti =.
	public void goBelow() {
		if (this.places == Paikka.EW) {
			this.places = Paikka.EWali;
		}
		if (this.places == Paikka.NS) {
			this.places = Paikka.NSali;
		}
	}
	
	
	//Lisää seinän annettuun suuntaan. Alikulun kohalla seinän lisääminen tapahtuu myös vastakkaiselle puolelle
	public void addWall(Paikka p) {
		//Lisätään pohjoisseinä
		if (p == Paikka.N) {
			if (this.places == Paikka.E) this.places = Paikka.NE;
			if (this.places == Paikka.S) this.places = Paikka.NS;
			if (this.places == Paikka.W) this.places = Paikka.NW;
			if (this.places == Paikka.ES) this.places = Paikka.NES;
			if (this.places == Paikka.EW) this.places = Paikka.NEW;
			if (this.places == Paikka.SW) this.places = Paikka.NSW;
			if (this.places == Paikka.ESW) this.places = Paikka.FULL;
			if (this.places == Paikka.EWali) { this.places = Paikka.NS;
			this.walls = 1;
			}
			this.walls++;
		}
		
		//Lisätään itäseinä
		if (p == Paikka.E) {
			if (this.places == Paikka.N) this.places = Paikka.NE;
			if (this.places == Paikka.S) this.places = Paikka.ES;
			if (this.places == Paikka.W) this.places = Paikka.EW;
			if (this.places == Paikka.NS) this.places = Paikka.NES;
			if (this.places == Paikka.NW) this.places = Paikka.NEW;
			if (this.places == Paikka.SW) this.places = Paikka.ESW;
			if (this.places == Paikka.NSW) this.places = Paikka.FULL;
			if (this.places == Paikka.EWali) { this.places = Paikka.EW;
			this.walls = 1;
			}
			this.walls++;
		}
		
		//Lisätään eteläseinä
		if (p == Paikka.S) {
			if (this.places == Paikka.N) this.places = Paikka.NS;
			if (this.places == Paikka.E) this.places = Paikka.ES;
			if (this.places == Paikka.W) this.places = Paikka.SW;
			if (this.places == Paikka.NE) this.places = Paikka.NES;
			if (this.places == Paikka.NW) this.places = Paikka.NSW;
			if (this.places == Paikka.EW) this.places = Paikka.ESW;
			if (this.places == Paikka.NEW) this.places = Paikka.FULL;
			if (this.places == Paikka.EWali) { this.places = Paikka.NS;
			this.walls = 1;
			}
			this.walls++;
			
		}
		
		//Lisätään länsiseinä
		if (p == Paikka.W) {
			if (this.places == Paikka.N) this.places = Paikka.NW;
			if (this.places == Paikka.S) this.places = Paikka.SW;
			if (this.places == Paikka.E) this.places = Paikka.EW;
			if (this.places == Paikka.NS) this.places = Paikka.NSW;
			if (this.places == Paikka.ES) this.places = Paikka.ESW;
			if (this.places == Paikka.NE) this.places = Paikka.NEW;
			if (this.places == Paikka.NES) this.places = Paikka.FULL;
			if (this.places == Paikka.EWali) { this.places = Paikka.EW;
			this.walls = 1;
			}
			this.walls++;
		}
	}
	
	// Poistaa seinän annetusta suunnasta, ei poista mikäli paikalla on vain yksi seinä. 
	// Ei tee mitään alikuluille
	public void removeWall(int i) {
		
		//Poistetaan pohjoisseinä
		if (i == 0) {
			if (this.places == Paikka.NE) this.places = Paikka.E;
			if (this.places == Paikka.NS) this.places = Paikka.S;
			if (this.places == Paikka.NW) this.places = Paikka.W;
			if (this.places == Paikka.NES) this.places = Paikka.ES;
			if (this.places == Paikka.NEW) this.places = Paikka.EW;
			if (this.places == Paikka.NSW) this.places = Paikka.SW;
			if (this.places == Paikka.FULL) this.places = Paikka.ESW;
			this.walls--;
		}
		
		//Poistetaan itäinen seinä
		if (i == 1) {
			if (this.places == Paikka.NE) this.places = Paikka.N;
			if (this.places == Paikka.ES) this.places = Paikka.S;
			if (this.places == Paikka.EW) this.places = Paikka.W;
			if (this.places == Paikka.NES) this.places = Paikka.NS;
			if (this.places == Paikka.NEW) this.places = Paikka.NW;
			if (this.places == Paikka.ESW) this.places = Paikka.SW;
			if (this.places == Paikka.FULL) this.places = Paikka.NSW;
			this.walls--;
		}
		
		//Poistetaan eteläseinä
		if (i == 2) {
			if (this.places == Paikka.NS) this.places = Paikka.N;
			if (this.places == Paikka.ES) this.places = Paikka.E;
			if (this.places == Paikka.SW) this.places = Paikka.W;
			if (this.places == Paikka.NES) this.places = Paikka.NE;
			if (this.places == Paikka.NSW) this.places = Paikka.NW;
			if (this.places == Paikka.ESW) this.places = Paikka.EW;
			if (this.places == Paikka.FULL) this.places = Paikka.NEW;
			this.walls--;
			
		}
		
		//Poistetaa länsiseinä
		if (i == 3) {
			if (this.places == Paikka.NW) this.places = Paikka.N;
			if (this.places == Paikka.SW) this.places = Paikka.S;
			if (this.places == Paikka.EW) this.places = Paikka.E;
			if (this.places == Paikka.NSW) this.places = Paikka.NS;
			if (this.places == Paikka.ESW) this.places = Paikka.ES;
			if (this.places == Paikka.NEW) this.places = Paikka.NE;
			if (this.places == Paikka.FULL) this.places = Paikka.NES;
			this.walls--;
		}
		
	}
	
	
	
}
