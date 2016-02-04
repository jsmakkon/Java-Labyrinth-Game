import java.util.Random;


public class Weave implements Labyrinth {
	
	private long seed;
	private Square[][] taulukko;
	
	public Weave(int x, int y, long seed) {
		this.taulukko = new Square[x][y];
		
		//generoidaan Labyrinth joka on täynnä seiniä:
		for (int i = 0; i < x; i++) {
			for (int a = 0; a < y; a++) {
				this.taulukko[i][a] = new Square(i, a);
			}
		}
		
		//Koordinaatit missä mennään, alkaen oikeasta alakulmasta
		boolean pois = false; //Kertoo milloin Labyrinth on valmis
		boolean valmis = false; // Käytetään loopista poistumiseen
		int x1 = x-1;
		int y1 = y-1;
		int suunta; // suunta 0-3, 0 = north etc...
		Random luku = null;
		this.seed = seed;
		//Käytetään annettua seediä tai arvotaan uusi mikäli parametri = -1;
		if (seed == -1) {
			//Estetään mahdollisuus että seed olisi arvottu -1:ksi
			while(this.seed == -1) {
				luku = new Random();
				this.seed = luku.nextLong();
				luku.setSeed(this.seed);
			}
		}
		else {
			luku = new Random(seed);
		}
		/*Aloitetaan reitin rakentaminen lopusta (oikeasta alakulmasta)*/
		
		//loopataan kunnes flagi muuttuu trueksi (Labyrinth täysi).
		while (pois == false) {
			valmis = false;
			//arvotaan uusi suunta
			suunta = Math.abs(luku.nextInt())%4;
			//tarkastetaan onko suunnalla palikkaa (ettei satu menemää reunan yli)
			if (!vieressaRuutu(this.getSquare(x1, y1), suunta)) 
				continue;
			
			//tarkastetaan että vieressa oleva ruutu on vapaa (ei mene reittia siinä)
			//samalla tarkistetaan onko kaikki viereiset varattuja, jos on niin
			//reitti on joutunut umpikujaan, jonka jälkeen etsitään uusi aloitusruutu.
			//Mikäli aloitusruutua(vapaata ruutua) ei enää löydy, Labyrinth on valmis.
			if (!ruutuVapaa(this.getSquare(x1, y1), suunta) && !canDig(this.getSquare(x1, y1), suunta)){
				if (!mikaanRuutuVapaa(this.getSquare(x1, y1))) {
					for (int i = 0; i < x; i++) {
						for (int a = 0; a < y; a++) {
							if (this.getSquare(i, a).getWalls() == 4 && vieressaReitti(this.getSquare(i, a)) != -1) {
								suunta = vieressaReitti(this.getSquare(i, a));
								yhdistaRuudut(this.getSquare(i, a), suunta);
								x1 = i;
								y1 = a;
								i = x;
								a = y;
								
								//käytetään valmis -flägiä kertomaan että Labyrinth ei ole vielä valmis (ettei pois -flagi muutu trueksi)
								valmis = true;
							}
						}
					}
					if (valmis == true) continue;
					pois = true;
				}
				continue;
			}
			
			//Kaivetaan alitus
			if (canDig(this.getSquare(x1, y1), suunta)) {
				if (suunta == 0) {
					this.getSquare(x1, y1).removeWall(0);
					this.getSquare(x1, y1 - 1).setPlace(Square.Paikka.NSali);
					y1 = y1-2;
					this.getSquare(x1, y1).removeWall(2);
				}
				
				if (suunta == 2) {
					this.getSquare(x1, y1).removeWall(2);
					this.getSquare(x1, y1 + 1).setPlace(Square.Paikka.NSali);
					y1 = y1+2;
					this.getSquare(x1, y1).removeWall(0);
				}
				
				if (suunta == 1) {
					this.getSquare(x1, y1).removeWall(1);
					this.getSquare(x1 + 1, y1).setPlace(Square.Paikka.EWali);
					x1 = x1+2;
					this.getSquare(x1, y1).removeWall(3);
				}
				
				if (suunta == 3) {
					this.getSquare(x1, y1).removeWall(3);
					this.getSquare(x1 - 1, y1).setPlace(Square.Paikka.EWali);
					x1 = x1-2;
					this.getSquare(x1, y1).removeWall(1);
				}
				continue;
			}
		
			
			//Kaivetaan perusreittiä
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
	
	//Kertoo voiko annettuun suuntaan rakentaa tunnelin reitin ali
	public boolean canDig(Square square, int suunta ){ 
		
		if (suunta == 0) {
			
			if (!vieressaRuutu(this.getSquare(square.getX(), square.getY()-1), suunta)) 
				return false;
			
			if (this.getSquare(square.getX(), square.getY()-1).getPlaces() == Square.Paikka.NS 
					&& this.getSquare(square.getX(), square.getY()-2).getPlaces() == Square.Paikka.FULL ) {
				return true;
			}
		}
		
		if (suunta == 1) {
			if (!vieressaRuutu(this.getSquare(square.getX()+1, square.getY()), suunta)) 
				return false;
			
			if (this.getSquare(square.getX()+1, square.getY()).getPlaces() == Square.Paikka.EW 
					&& this.getSquare(square.getX()+2, square.getY()).getPlaces() == Square.Paikka.FULL ) {
				return true;
			}	
		}
		
		if (suunta == 2) {
			if (!vieressaRuutu(this.getSquare(square.getX(), square.getY()+1), suunta)) 
				return false;
			
			if (this.getSquare(square.getX(), square.getY()+1).getPlaces() == Square.Paikka.NS 
					&& this.getSquare(square.getX(), square.getY()+2).getPlaces() == Square.Paikka.FULL ) {
				return true;
			}
		}
		
		if (suunta == 3) {
			if (!vieressaRuutu(this.getSquare(square.getX()-1, square.getY()), suunta)) 
				return false;
			
			if (this.getSquare(square.getX()-1, square.getY()).getPlaces() == Square.Paikka.EW 
					&& this.getSquare(square.getX()-2, square.getY()).getPlaces() == Square.Paikka.FULL ) {
				return true;
			}
		}
		
		return false;
		
		
	}
	
	public long getSeed(){
		return this.seed;
	}
	
	public Square getSquare(int x, int y){
		return this.taulukko[x][y];
	}
	
	//dummy
	public Square getSquare(int w, int l, int h){
		return null;
	}
	
	public int getLengthW() {
		return this.taulukko.length;
	}

	public int getLengthH() {
		return this.taulukko[0].length;
	}
	
	//kertoo onko kyseinen ruutu reitin vieressa, ja milla puolella reitti menee.
	//Reitiksi ei lasketa 3-haaraista risteystä
	//0 = north, 1 = east, 2 = south, 3 = west, -1 = ei olee
	
	private int vieressaReitti(Square palikka) {
		if (this.vieressaRuutu(palikka, 0)) {
			if (this.getSquare(palikka.getX(), palikka.getY() - 1).getWalls() == 2) {
				return 0;
			}
		}
		if (this.vieressaRuutu(palikka, 1)) {
			if (this.getSquare(palikka.getX() + 1, palikka.getY()).getWalls() == 2) {
				return 1;
			}
		}
		if (this.vieressaRuutu(palikka, 2))	{
			if (this.getSquare(palikka.getX(), palikka.getY() + 1).getWalls() == 2) {
				return 2;
			}
		}
		if (this.vieressaRuutu(palikka, 3))	{
			if (this.getSquare(palikka.getX() - 1, palikka.getY()).getWalls() == 2) {
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
	
	//Tarkistaa onko annetussa suunnassa oleva ruutu vapaa (4 seinää)
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
	
	//Tarkistaa onko mikään ympärillä oleva ruutu vapaa(umpikuja)
	private boolean mikaanRuutuVapaa(Square Palikka) {
		if (this.vieressaRuutu(Palikka, 0)){
			if (this.getSquare(Palikka.getX(), Palikka.getY()-1).getWalls()
					== 4 || canDig(Palikka, 0)) return true;
		}
		if (this.vieressaRuutu(Palikka, 1)){
			if (this.getSquare(Palikka.getX()+1, Palikka.getY()).getWalls()
					== 4 || canDig(Palikka, 1)) return true;
		}
		if (this.vieressaRuutu(Palikka, 2)){
			if (this.getSquare(Palikka.getX(), Palikka.getY()+1).getWalls()
					== 4 || canDig(Palikka, 2)) return true;
		}
		if (this.vieressaRuutu(Palikka, 3)){
			if (this.getSquare(Palikka.getX()-1, Palikka.getY()).getWalls()
				== 4 || canDig(Palikka, 3)) return true;
		}
		return false;
	}

	//Muodostaa reitin kahden vierekkäisen pisteen välille, eli poistaa seinät välistä
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
	
	

									/* RATKAISUALGORITMI*/

	//Ratkaistaan Labyrintti täyttämällä umpikujat, jolloin jäljelle jää vain valmis reitti
	
	public boolean ratkaise(Player player) {
		int x,y;
		int suunta = 0;
		x=0;
		y=0;
		boolean flag = true;
		Square[][] laby2 = new Square[this.getLengthW()][this.getLengthH()];
		
		//rakennetaan kopio labyrintistä
		for (int i = 0 ; i < this.getLengthW() ; i++) {
			for (int j = 0; j < this.getLengthH(); j++) {
				laby2[i][j] = new Square(i,j);
				laby2[i][j].setPlace(this.getSquare(i, j).getPlaces());
			}
		}
			
		for (int i = 0 ; i < this.getLengthW() ; i++) {
			for (int j = 0; j < this.getLengthH(); j++) {
				suunta = -1;
				//aloitetaan täyttäminen kun löydetään umpikuja
				if (laby2[i][j].getWalls() == 3) {
					x = i;
					y = j;
					
					//skipataan loppupiste
					if (x == this.getLengthW()-1 && y == this.getLengthH()-1) {
						continue;
					}
					
					//liikutaan reittiä pitkin
					while (flag){
						
						/*PELAAJARUUTU -TILANNE*/
						//Pelaajan kohdalle saapuessa tarkistetaan onko pelaaja alikulun kohdalla,
						//jolloin joko pysähdytään tai poistetaan alikulku ja jatketaan täyttöä.
						if (x == player.getX() && y == player.getY()) {
							if (laby2[player.getX()][player.getY()].getPlaces() == Square.Paikka.EWali &&
									player.getTunnelissa() == 1) {
								if (suunta == 1) {
									laby2[x][y].setPlace(Square.Paikka.EW);
									x++;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.W);
									}
									continue;
								}
								if (suunta == 3) {
									laby2[x][y].setPlace(Square.Paikka.EW);
									x--;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.E);
									}
									continue;
								}
							}
							
							if (laby2[player.getX()][player.getY()].getPlaces() == Square.Paikka.EWali &&
									player.getTunnelissa() == 0) {
								if (suunta == 2) {
									laby2[x][y].setPlace(Square.Paikka.NS);
									y++;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.N);
									}
									continue;
								}
								if (suunta == 0) {
									laby2[x][y].setPlace(Square.Paikka.NS);
									y--;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.S);
									}
									continue;
								}
								
							}
							
							if (laby2[player.getX()][player.getY()].getPlaces() == Square.Paikka.NSali &&
									player.getTunnelissa() == 1) {
								if (suunta == 2) {
									laby2[x][y].setPlace(Square.Paikka.NS);
									y++;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.N);
									}
									continue;
								}
								if (suunta == 0) {
									laby2[x][y].setPlace(Square.Paikka.NS);
									y--;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.S);
									}
									continue;
								}
							}
							
							if (laby2[player.getX()][player.getY()].getPlaces() == Square.Paikka.NSali &&
									player.getTunnelissa() == 0) {
								if (suunta == 1) {
									laby2[x][y].setPlace(Square.Paikka.EW);
									x++;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.W);
									}
									continue;
								}
								if (suunta == 3) {
									laby2[x][y].setPlace(Square.Paikka.EW);
									x--;
									if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
										laby2[x][y].addWall(Square.Paikka.E);
									}
									continue;
								}
							}
							break;
						}
						
						/*SILTATILANNE*/
						
						//Asetetaan sillan tilalle oikeanlainen reittipala (FULL -palan sijaan), ja jatketaan
						//reitin toiselta puolelta.
						
						if (laby2[x][y].getPlaces() == Square.Paikka.NSali || laby2[x][y].getPlaces() == Square.Paikka.EWali ) {
							if (suunta == 0) {
								laby2[x][y].setPlace(Square.Paikka.NS);
								y--;
								if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
									laby2[x][y].addWall(Square.Paikka.S);
								}
								if (laby2[x][y].getWalls() == 2) {
									break;
								}
								continue;
							}
							if (suunta == 1) {
								laby2[x][y].setPlace(Square.Paikka.EW);
								x++;
								if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
									laby2[x][y].addWall(Square.Paikka.W);
								}
								if (laby2[x][y].getWalls() == 2) {
									break;
								}
								continue;
							}
							if (suunta == 2) {
								laby2[x][y].setPlace(Square.Paikka.NS);
								y++;
								if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
									laby2[x][y].addWall(Square.Paikka.N);
								}
								if (laby2[x][y].getWalls() == 2) {
									break;
								}
								continue;
							}
							if (suunta == 3) {
								laby2[x][y].setPlace(Square.Paikka.EW);
								x--;
								if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
									laby2[x][y].addWall(Square.Paikka.E);
								}
								if (laby2[x][y].getWalls() == 2) {
									break;
								}
								continue;
							}
							
						}
						
						/*NORMITILANNE*/
						
						//Etsitään seinätön kohta ja lähdetään kulkemaan siihen suuntaan
						if (!laby2[x][y].isWall(Square.Paikka.N)) {
							suunta = 0; //asetetaan kulkusuunta pohjoiseen siltä varalta että tulee silta seuraavaksi
							laby2[x][y].setPlace(Square.Paikka.FULL);
							y--;
							if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
								laby2[x][y].addWall(Square.Paikka.S);
							}
							//Tarkistetaan että nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 2) {
								break;
							}
							continue;
						}
						if (!laby2[x][y].isWall(Square.Paikka.E)) {
							suunta = 1;
							laby2[x][y].setPlace(Square.Paikka.FULL);
							x++;
							if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
								laby2[x][y].addWall(Square.Paikka.W);
							}
							//Tarkistetaan että nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 2) {
								break;
							}
							continue;
						}
						
						
						if (!laby2[x][y].isWall(Square.Paikka.S)) {
							suunta = 2;
							laby2[x][y].setPlace(Square.Paikka.FULL);
							y++;
							if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
								laby2[x][y].addWall(Square.Paikka.N);
							}
							//Tarkistetaan että nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 2) {
								break;
							}
							continue;
						}
						
						
						if (!laby2[x][y].isWall(Square.Paikka.W)) {
							suunta = 3;
							laby2[x][y].setPlace(Square.Paikka.FULL);
							x--;
							if (laby2[x][y].getPlaces() != Square.Paikka.NSali && laby2[x][y].getPlaces() != Square.Paikka.EWali) {
								laby2[x][y].addWall(Square.Paikka.E);
							}
							//Tarkistetaan että nykyinen kohta ei ole risteys:
							if (laby2[x][y].getWalls() == 2) {
								break;
							}
							continue;
						}
					}
				}
			}
		}
		
		piirraReitti(player, laby2);
		return true;
	}
	
	//Merkkaa piirrettävän reitin kuten normaalissakin labyrintissä.
	
	private void piirraReitti(Player player,
			Square[][] laby2) {
		for (int i = 0 ; i < this.getLengthW() ; i++) {
			for (int j = 0; j < this.getLengthH(); j++) {
				if (laby2[i][j].getPlaces() != Square.Paikka.FULL) {

					if (i == player.getX() && j == player.getY()) {
						if (player.getTunnelissa() == 0) {
							this.getSquare(i, j).setColor(2);
						}
						else {
							this.getSquare(i, j).setColor(1);
						}
						
						//Purkkakorjaus, korjaa nullpointerexceptionin mikäli pelaaja on reunoilla
						if (i == 0 || j == 0 || j == this.getLengthH() || i == this.getLengthW()) {
							continue;
						}
						
						if (laby2[i-1][j].getPlaces() != Square.Paikka.FULL && laby2[i+1][j].getPlaces() != Square.Paikka.FULL || 
								laby2[i][j-1].getPlaces() != Square.Paikka.FULL && laby2[i][j+1].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(3);
						}
						
						continue;
					}

					if (this.getSquare(i, j).getPlaces() == Square.Paikka.EWali) {

						if (laby2[i][j-1].getPlaces() != Square.Paikka.FULL && laby2[i][j+1].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(1);
						}

						if (laby2[i-1][j].getPlaces() != Square.Paikka.FULL && laby2[i+1][j].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(2);
						}

						if (laby2[i-1][j].getPlaces() != Square.Paikka.FULL && laby2[i][j-1].getPlaces() != Square.Paikka.FULL &&
								laby2[i+1][j].getPlaces() != Square.Paikka.FULL && laby2[i][j+1].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(3);
						}

					}

					else if (this.getSquare(i, j).getPlaces() == Square.Paikka.NSali) {
						if (laby2[i][j-1].getPlaces() != Square.Paikka.FULL && laby2[i][j+1].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(2);
						}

						if (laby2[i-1][j].getPlaces() != Square.Paikka.FULL && laby2[i+1][j].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(1);
						}

						if (laby2[i-1][j].getPlaces() != Square.Paikka.FULL && laby2[i][j-1].getPlaces() != Square.Paikka.FULL &&
								laby2[i+1][j].getPlaces() != Square.Paikka.FULL && laby2[i][j+1].getPlaces() != Square.Paikka.FULL) {
							this.getSquare(i, j).setColor(3);
						}
					}

					else this.getSquare(i, j).setColor(1);
				}
			}
		}
	}
}
