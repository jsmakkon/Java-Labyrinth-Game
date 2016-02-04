import java.util.Random;


public class Normal implements Labyrinth {
	
	private long seed;
	private Square[][] taulukko;
	
	public Normal(int x, int y, long seed) {
		this.taulukko = new Square[x][y];
		
		//Generoidaan Labyrinth joka on t�ynn� seini�
		for (int i = 0; i < x; i++) {
			for (int a = 0; a < y; a++) {
				this.taulukko[i][a] = new Square(i, a);
			}
		}
		
		//Koordinaatit miss� menn��n, alkaen oikeasta alakulmasta
		boolean pois = false; //Kertoo milloin Labyrinth on valmis
		boolean valmis = false; // K�ytet��n loopista poistumiseen
		int x1 = x-1;
		int y1 = y-1;
		int suunta; // suunta 0-3, 0 = north etc...
		// randomluku
		Random luku;
		if (seed == -1) {
			luku = new Random();
			this.seed = luku.nextLong();
			luku.setSeed(this.seed);
		}
		else {
			luku = new Random(seed);
			this.seed = seed;
		}
		
		/*rakennetaan reitti lopusta (oikea alakulma) alkuun.*/
		
		//loopataan kunnes flagi muuttuu trueksi (Labyrinth t�ysi).
		while (pois == false) {
			valmis = false;
			//arvotaan uusi suunta
			suunta = Math.abs(luku.nextInt())%4;
			//tarkastetaan onko suunnalla palikkaa (ettei satu menem�� reunan yli)
			if (!vieressaRuutu(this.getSquare(x1, y1), suunta)) 
				continue;
			
			//tarkastetaan ett� vieressa oleva ruutu on vapaa (ei mene reittia siin�)
			//samalla tarkistetaan onko kaikki viereiset varattuja, jos on niin
			//reitti on joutunut umpikujaan, jonka j�lkeen etsit��n uusi aloitusruutu.
			//Mik�li aloitusruutua(vapaata ruutua) ei en�� l�ydy, Labyrinth on valmis.
			if (!ruutuVapaa(this.getSquare(x1, y1), suunta)){
				if (!mikaanRuutuVapaa(this.getSquare(x1, y1), suunta)) {
					for (int i = 0; i < x; i++) {
						for (int a = 0; a < y; a++) {
							if (this.getSquare(i, a).getWalls() == 4 && vieressaReitti(this.getSquare(i, a)) != -1) {
								suunta = vieressaReitti(this.getSquare(i, a));
								yhdistaRuudut(this.getSquare(i, a), suunta);
								x1 = i;
								y1 = a;
								i = x;
								a = y;
								
								//k�ytet��n valmis -fl�gi� kertomaan ett� Labyrinth ei ole viel� valmis (ettei pois -flagi muutu trueksi)
								valmis = true;
							}
						}
					}
					if (valmis == true) continue;
					pois = true;
				}
				continue;
			}
			
		
			
			//Poistetaan sein�t ja siirret��n koordinaatteja
			if (suunta == 0) {
				this.getSquare(x1, y1).removeWall(0);
				this.getSquare(x1, y1 - 1).removeWall(2);
				y1--;
			}
			
			if (suunta == 2) {
				this.getSquare(x1, y1).removeWall(2);
				this.getSquare(x1, y1 + 1).removeWall(0);
				y1++;
			}
			
			if (suunta == 1) {
				this.getSquare(x1, y1).removeWall(1);
				this.getSquare(x1 + 1, y1).removeWall(3);
				x1++;
			}
			
			if (suunta == 3) {
				this.getSquare(x1, y1).removeWall(3);
				this.getSquare(x1 - 1, y1).removeWall(1);
				x1--;
			}
		}
		
		
	}
	
	public Square getSquare(int x, int y){
		return this.taulukko[x][y];
	}
	
	//Dummy
	public Square getSquare(int w, int l, int h){
		return null;
	}
	
	public int getLengthW() {
		//System.out.println(this.taulukko.length);
		return this.taulukko.length;
	}

	public int getLengthH() {
		//System.out.println(this.taulukko[0].length);
		return this.taulukko[0].length;
	}
	
	//kertoo onko kyseinen ruutu reitin vieressa, ja milla puolella reitti menee.
	//Reitiksi ei lasketa 3-haaraista risteyst�
	//0 = north, 1 = east, 2 = south, 3 = west, -1 = ei olee
	
	private int vieressaReitti(Square palikka) {
		if (this.vieressaRuutu(palikka, 0)) {
			if (this.getSquare(palikka.getX(), palikka.getY() - 1).getWalls() < 3) {
				return 0;
			}
		}
		if (this.vieressaRuutu(palikka, 1)) {
			if (this.getSquare(palikka.getX() + 1, palikka.getY()).getWalls() < 3) {
				return 1;
			}
		}
		if (this.vieressaRuutu(palikka, 2))	{
			if (this.getSquare(palikka.getX(), palikka.getY() + 1).getWalls() < 3) {
				return 2;
			}
		}
		if (this.vieressaRuutu(palikka, 3))	{
			if (this.getSquare(palikka.getX() - 1, palikka.getY()).getWalls() < 3) {
				return 3;
			}
		}
		
		return -1;
			
			
	}

	//tarkistaa, onko viereinen ruutu reunan ulkopuolella
	private boolean vieressaRuutu(Square Palikka, int suunta) {
		if (suunta == 0 && Palikka.getY() == 0) return false;
		if (suunta == 1 && Palikka.getX() == this.taulukko.length - 1) return false;
		if (suunta == 2 && Palikka.getY() == 
			this.taulukko[0].length - 1) return false;
		if (suunta == 3 && Palikka.getX() == 0) return false;
		
		return true;
	}

	private boolean ruutuVapaa(Square Palikka, int suunta) {
		
		if (this.vieressaRuutu(Palikka, 0))
			if (suunta == 0 && this.getSquare(Palikka.getX(), Palikka.getY()-1).getPlaces()
				== Square.Paikka.FULL) return true;
		if (this.vieressaRuutu(Palikka, 1))
			if (suunta == 1 && this.getSquare(Palikka.getX()+1, Palikka.getY()).getPlaces()
				== Square.Paikka.FULL) return true;
		if (this.vieressaRuutu(Palikka, 2))
			if (suunta == 2 && this.getSquare(Palikka.getX(), Palikka.getY()+1).getPlaces()
				== Square.Paikka.FULL) return true;
		if (this.vieressaRuutu(Palikka, 3))
			if (suunta == 3 && this.getSquare(Palikka.getX()-1, Palikka.getY()).getPlaces()
				== Square.Paikka.FULL) return true;
		return false;
	}
	
	private boolean mikaanRuutuVapaa(Square Palikka, int suunta) {
		if (this.vieressaRuutu(Palikka, 0)){
			if (this.getSquare(Palikka.getX(), Palikka.getY()-1).getWalls()
					== 4) return true;
		}
		if (this.vieressaRuutu(Palikka, 1)){
			if (this.getSquare(Palikka.getX()+1, Palikka.getY()).getWalls()
					== 4) return true;
		}
		if (this.vieressaRuutu(Palikka, 2)){
			if (this.getSquare(Palikka.getX(), Palikka.getY()+1).getWalls()
					== 4) return true;
		}
		if (this.vieressaRuutu(Palikka, 3)){
			if (this.getSquare(Palikka.getX()-1, Palikka.getY()).getWalls()
				== 4) return true;
		}
		return false;
	}

	private void yhdistaRuudut(Square palikka, int suunta) {
		if (suunta == 0) {
			palikka.removeWall(0);
			this.getSquare(palikka.getX(), palikka.getY() - 1).removeWall(2);
		}
		
		if (suunta == 2) {
			palikka.removeWall(2);
			this.getSquare(palikka.getX(), palikka.getY() + 1).removeWall(0);
		}
		
		if (suunta == 1) {
			palikka.removeWall(1);
			this.getSquare(palikka.getX() + 1, palikka.getY()).removeWall(3);
		}
		
		if (suunta == 3) {
			palikka.removeWall(3);
			this.getSquare(palikka.getX() - 1, palikka.getY()).removeWall(1);
		}
	}
	
	public long getSeed(){
		return this.seed;
	}

									/* RATKAISUALGORITMI*/

	//Ratkaistaan Labyrinth t�ytt�m�ll� umpikujat, jolloin j�ljelle j�� vain valmis reitti.
	//Metodi luo ensin kopion labyrintist�, t�ytt�� kopion umpikujat ja merkkaa alkuper�iseen
	//Labyrintin kulkureitin, joka j�i j�ljelle.
	
	public boolean ratkaise(Player player) {
		int x,y;
		x=0;
		y=0;
		boolean flag = true; //Flagi while -looppiin, ei k�ytet� erikoisemmin
		Square[][] laby2 = new Square[this.getLengthW()][this.getLengthH()];
		
		//rakennetaan kopio labyrintist�
		for (int i = 0 ; i < this.getLengthW() ; i++) {
			for (int j = 0; j < this.getLengthH(); j++) {
				laby2[i][j] = new Square(i,j);
				laby2[i][j].setPlace(this.getSquare(i, j).getPlaces());
			}
		}
			
		for (int i = 0 ; i < this.getLengthW() ; i++) {
			for (int j = 0; j < this.getLengthH(); j++) {
				if (laby2[i][j].getWalls() == 3) {
					x = i;
					y = j;
					
					//skipataan alotuspiste ja loppupiste
					if (x == player.getX() && y == player.getY()) {
						continue;
					}
					if (x == this.getLengthW()-1 && y == this.getLengthH()-1) {
						continue;
					}
					
					//T�ytet��n l�ydetyn umpikuja reitti�, kunnes saavutaan risteykseen
					while (flag){
						
						//Jos ollaan t�ytt�m�ss� aloituskohtaa niin pys�ytet��n t�ytt�minen
						if (x == player.getX() && y == player.getY()) {
							break;
						}
						
						//Tarkistetaan mist� suunnasta ei l�ydy sein�� ja liikutaan t�h�n suuntaan
						//samalla t�ytt�en edellinen ruutu.
						if (!laby2[x][y].isWall(Square.Paikka.N)) {
							laby2[x][y].setPlace(Square.Paikka.FULL); //T�ytt�� edellisen ruudun
							y--;
							//Tarkistetaan ett� nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 1) {
								laby2[x][y].addWall(Square.Paikka.S);
								break;
							}
							laby2[x][y].addWall(Square.Paikka.S);
							continue;
						}
						if (!laby2[x][y].isWall(Square.Paikka.E)) {
							laby2[x][y].setPlace(Square.Paikka.FULL);
							x++;
							//Tarkistetaan ett� nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 1) {
								laby2[x][y].addWall(Square.Paikka.W);
								break;
							}
							laby2[x][y].addWall(Square.Paikka.W);
							continue;
						}
						if (!laby2[x][y].isWall(Square.Paikka.S)) {
							laby2[x][y].setPlace(Square.Paikka.FULL);
							y++;
							//Tarkistetaan ett� nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 1) {
								laby2[x][y].addWall(Square.Paikka.N);
								break;
							}
							laby2[x][y].addWall(Square.Paikka.N);
							continue;
						}
						if (!laby2[x][y].isWall(Square.Paikka.W)) {
							laby2[x][y].setPlace(Square.Paikka.FULL);
							x--;
							//Tarkistetaan ett� nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 1) {
								laby2[x][y].addWall(Square.Paikka.E);
								break;
							}
							laby2[x][y].addWall(Square.Paikka.E);
							continue;
						}
					}
				}
			}
		}
		
		//Merkataan kopiolabyrintin t�ytt�m�tt�m�t ruudut v�rilliseksi oikeassa labyrintiss�
		for (int i = 0 ; i < this.getLengthW() ; i++) {
			for (int j = 0; j < this.getLengthH(); j++) {
				if (laby2[i][j].getPlaces() != Square.Paikka.FULL) {
					this.getSquare(i, j).setColor(1);
				}
			}
		}
		
		return true;
	}
}
