
// Pelaaja -luokka, pit‰‰ kirjaa pelaajan sijainnista ja kuljetuista ruuduista.


public class Player {
	
	private String nimi;
	private int locX;
	private int locY;
	private int locz; //joko 0 tai 1
	private int askeleet;
	
	public Player(int x, int y, String nimi) {
		this.locX = x;
		this.locY = y;
		this.locz = 0;
		this.askeleet = 0;
		this.nimi = nimi;
		
	}
	
	public int getX () {
		return this.locX;
	}
	
	public void setTunnelissa(int a) {
		this.locz = a;
	}
	
	public String getNimi() {
		return this.nimi;
	}
	
	public int getAskeleet(){
		return this.askeleet;
	}
	
	public int getTunnelissa() {
		return this.locz;
	}
	
	public int getY () {
		return this.locY;
	}
	
	//liikuttaa ukon paikkaa, 1 = north, 2 = east, 3 = south, 4 = west
	public void move(int suunta) {
		this.askeleet++;
		if (suunta == 1) {
			this.locY--;
		}
		if (suunta == 2) {
			this.locX++;
		}
		if (suunta == 3) {
			this.locY++;
		}
		if (suunta == 4) {
			this.locX--;
		}
		this.locz = 0;
		
	}

}
