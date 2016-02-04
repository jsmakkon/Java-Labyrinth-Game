import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*Game -luokka pyˆritt‰‰ itse Game‰ ja Map -luokka lataa kuvat ja piirt‰‰ tarvittavan grafiikan.*/

public class Game implements KeyListener{
	
	private boolean highscoreFlag;
	private JFrame frame;
	private JPanel Map; //Piirtopaneeli
	private Player player; // Pelaajan olio
	private Labyrinth laby; //Luotu Labyrinth
	private boolean ratkaistu; //Flagi kertomaan onko Labyrinth jo ratkaistu. K‰ytet‰‰n l‰hinn‰ est‰m‰‰n tallennusta ratkaisun j‰lkeen.
	
	
	//Konstruktori. Muodostaa ikkunan ja asettaa pelaajan sille annettuun paikkaan tai keskelle, mik‰li annetut kordinaatit on -1.
	public Game(Labyrinth laby, int plX, int plY) {
		this.laby = laby;
		this.ratkaistu = false;
		this.highscoreFlag = false;
		
		frame = new JFrame("LabyrinthGame");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setPreferredSize(new Dimension(800,800));
        frame.setLocation(300, 200);
        //player = new Player(laby.getLengthW()/2, laby.getLengthH()/2, Start.lista.get(4));
        
        Map = new Map(laby, this);
        Map.setLocation(0, 0);
        Map.setPreferredSize(new Dimension(800,800));
        
        if (plX == -1) {
        	player = new Player(laby.getLengthW()/2, laby.getLengthH()/2, Start.lista.get(4));
        }
        else {
        	player = new Player(plX, plY, Start.lista.get(4));
        }
    	
    	frame.addKeyListener(this);
    	
    	frame.add(Map);
        
	    frame.pack();
	    frame.setVisible(true);
	}
	
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean getRatkaistu(){
		return this.ratkaistu;
	}
	
	
	
	//Kun nappia painetaan, liikutetaan ukkoa tarpeen mukaan, tai sitten poistutaan Gamest‰
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP && highscoreFlag == false) {
			if (allowMove(this.player, Square.Paikka.N)) {
				this.player.move(1);
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.EWali) {
					this.player.setTunnelissa(1);
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && highscoreFlag == false) {
			if (allowMove(this.player, Square.Paikka.E)) {
			this.player.move(2);
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.NSali) {
					this.player.setTunnelissa(1);
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN && highscoreFlag == false) {
			if (allowMove(this.player, Square.Paikka.S)) {
			this.player.move(3);
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.EWali) {
					this.player.setTunnelissa(1);
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT && highscoreFlag == false) {
			if (allowMove(this.player, Square.Paikka.W)) {
			this.player.move(4);
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.NSali) {
					this.player.setTunnelissa(1);
				}
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			
			
			if (this.ratkaistu == false){
				this.ratkaistu = this.laby.ratkaise(player);
				//Poistetaa save tiedosto, koska ratkaisu n‰kyy nyt
				//EI TOIMI MIKƒLI LADATAAN PELI JA PAINETAAN ESC
				File tiedosto = new File (Start.lista.get(4) + ".txt");
				if (tiedosto.exists()) {
					tiedosto.delete();
				}
				
				Map.repaint();
			}
			
			/*PURKKAVIRITYS.
			 *Koodin ideana on t‰ss‰ kohtaa j‰tt‰‰ taustalle ratkaistu labyrintti ja samalla
			 *n‰ytt‰‰ highscore- ikkuna, jonka j‰lkeen Esc-nappia painamalla pit‰isi ensin sulkeutua
			 *Highscore- ikkuna ja toisella painalluksella itse labyrintti-ikkuna. Koodi siis toimii mutta
			 *sen voisi tehd‰ fiksummin.  */
			if (ratkaistu == true) {
				
				if (this.highscoreFlag == false) {
				Highscore scoret = new Highscore();
				scoret.showHighscores();
				}
				
				if (highscoreFlag != false) {
				
					this.frame.dispose();
					Start.frame.setVisible(true);
					return;
				}
				this.highscoreFlag = true;
			}
		}
		
		//Tallennetaan ja poistutaan
		if (e.getKeyCode() == KeyEvent.VK_S && this.ratkaistu != true) {
			String teksti = Start.lista.get(4) + ".txt";
			File tiedosto = new File(teksti);
			
			try {
				tiedosto.createNewFile();
			} catch (IOException e1) {
			}
			
			FileWriter kirjoittaja = null;
			try{
				kirjoittaja = new FileWriter(tiedosto);
			} catch ( IOException e2 ){
				JOptionPane.showMessageDialog(this.frame,
    		    "Tiedoston tallennus ep‰onnistui!");
			  return;
			}
			
			String tallennus = "LabyrinthGame\n";
			tallennus += laby.getLengthH() + "\n";
			tallennus += laby.getLengthW() + "\n";
			tallennus += Start.lista.get(0)+ "\n";
			tallennus += player.getX() + "\n";
			tallennus += player.getY() + "\n";
			tallennus += laby.getSeed() + "\n";
			
			tallennus = kryptaa(tallennus);
			
			try {
				kirjoittaja.write(tallennus);
				kirjoittaja.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this.frame,
    		    "Tiedoston tallennus ep‰onnistui!");
				suljeGame();
				return;
			}
			
			
			
			suljeGame();
			return;
		}
		Map.repaint();
		
		
		if (player.getX() == this.laby.getLengthW()-1 && player.getY() == this.laby.getLengthH()-1) {
			JOptionPane.showMessageDialog(Start.frame, "Selvisit labyrintist‰!");
			Highscore scoret = new Highscore();
			scoret.addName(player, this.laby.getLengthH()*this.laby.getLengthW());
			scoret.showHighscores();
			scoret.getFrame().toFront();
			suljeGame();
			return;
		}
		
	}

	//Salaa txt -tiedostoon tallennettavan tiedon
	//EI TOTEUTETTU!!
	private String kryptaa(String tallennus) {
		
		return tallennus;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Ei tehda mitaan
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Mik‰ t‰‰ o?
	}
	
	private void suljeGame() {
		this.frame.dispose();
		Start.frame.setVisible(true);
	}
	
	//Tarkistaa saako pelaaja liikkua annettavaan suuntaan (onko sein‰ vastassa)
	private boolean allowMove(Player player, Square.Paikka suunta) {
		if (suunta == Square.Paikka.N) {
			//Menn‰‰n jos ei ole sein‰‰...
			if (!this.laby.getSquare(this.player.getX(), this.player.getY()).isWall(Square.Paikka.N)) {
				//...paitsi jos ollaantunnelipalikassa v‰‰r‰ll‰ tasolla 
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.NSali
						&& player.getTunnelissa() == 1) {
					return false;
				}
				
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.EWali
						&& player.getTunnelissa() == 0) {
					return false;
				}
					
				return true;
			}
		}
		
		if (suunta == Square.Paikka.E) {
			//Menn‰‰n jos ei ole sein‰‰...
			if (!this.laby.getSquare(this.player.getX(), this.player.getY()).isWall(Square.Paikka.E)) {
				//...paitsi jos ollaantunnelipalikassa v‰‰r‰ll‰ tasolla 
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.NSali
						&& player.getTunnelissa() != 1) {
					return false;
				}
				
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.EWali
						&& player.getTunnelissa() != 0) {
					return false;
				}
					
				return true;
			}
		}
		
		if (suunta == Square.Paikka.S) {
			if (!this.laby.getSquare(this.player.getX(), this.player.getY()).isWall(Square.Paikka.S)) {
				//...paitsi jos ollaantunnelipalikassa v‰‰r‰ll‰ tasolla 
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.NSali
						&& player.getTunnelissa() == 1) {
					return false;
				}
				
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.EWali
						&& player.getTunnelissa() == 0) {
					return false;
				}
					
				return true;
			}
		}
		
		if (suunta == Square.Paikka.W) {
			if (!this.laby.getSquare(this.player.getX(), this.player.getY()).isWall(Square.Paikka.W)) {
				//...paitsi jos ollaantunnelipalikassa v‰‰r‰ll‰ tasolla 
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.NSali
						&& player.getTunnelissa() != 1) {
					return false;
				}
				
				if (this.laby.getSquare(this.player.getX(), this.player.getY()).getPlaces() == Square.Paikka.EWali
						&& player.getTunnelissa() != 0) {
					return false;
				}
					
				return true;
			}
		}
		
		return false;
		
	}
}

class Map extends JPanel {
	
	private static final long serialVersionUID = 1L; //JPanel halusi t‰m‰n
	private ArrayList<BufferedImage> kuvat; //Lista kuvista
	private BufferedImage image; // Parhaillaan piirrett‰v‰ kuva
	private Labyrinth laby; // Piirrett‰v‰ Labyrinth
	private Game Game; //Game...
	
    public Map(Labyrinth laby, Game Game) {
    	this.laby = laby;
    	this.Game = Game;
    	this.setPreferredSize(new Dimension(40,40));
    	kuvat = new ArrayList<BufferedImage>();
    	lueKuvat();
    }

    @Override
    public void paint(Graphics g) {
    	
    	
	    for (int i = 0; i < this.laby.getLengthW(); i++) {
	    	for (int j = 0; j < this.laby.getLengthH(); j++) {
	    		if (laby.getSquare(i, j).getColor() == 0) {
	    			image = getImage(laby.getSquare(i, j).getPlaces());
	    			g.drawImage(image, 40*i, 40*j, null);
	    		}
	    		else {
	    			image = getColorImage(laby.getSquare(i, j).getPlaces(), laby.getSquare(i, j).getColor());
	    			g.drawImage(image, 40*i, 40*j, null);
	    		}
	    	}
	    }
	    
	    image = getExit(laby.getSquare(laby.getLengthW()-1, laby.getLengthH()-1).getPlaces(), 
	    		laby.getSquare (laby.getLengthW()-1, laby.getLengthH()-1).getColor());
		g.drawImage(image, 40*(laby.getLengthW()-1), 40*(laby.getLengthH()-1), null);
	    
	    
	    
	    image = getImage(null);
	    g.drawImage(image, 40*this.Game.getPlayer().getX(), 40*this.Game.getPlayer().getY(), null);
    }
    
    //Lataa tarvittavat kuvat muistiin. Kuvat merkattu kehnosti numeroilla, joita metodit
    //getImage ja getColorImage k‰ytt‰‰
    private void lueKuvat () {
    	try {
    		//Peruskuvat
			kuvat.add(ImageIO.read(new File("Kuvat/N.png"))); //0
			kuvat.add(ImageIO.read(new File("Kuvat/E.png"))); //1
			kuvat.add(ImageIO.read(new File("Kuvat/S.png"))); //2
			kuvat.add(ImageIO.read(new File("Kuvat/W.png"))); //3
			kuvat.add(ImageIO.read(new File("Kuvat/NE.png"))); //4
			kuvat.add(ImageIO.read(new File("Kuvat/NS.png"))); //5
			kuvat.add(ImageIO.read(new File("Kuvat/NW.png"))); //6
			kuvat.add(ImageIO.read(new File("Kuvat/ES.png"))); //7
			kuvat.add(ImageIO.read(new File("Kuvat/EW.png"))); //8
			kuvat.add(ImageIO.read(new File("Kuvat/SW.png"))); //9
			kuvat.add(ImageIO.read(new File("Kuvat/NEW.png"))); //10
			kuvat.add(ImageIO.read(new File("Kuvat/NES.png"))); //11
			kuvat.add(ImageIO.read(new File("Kuvat/ESW.png"))); //12
			kuvat.add(ImageIO.read(new File("Kuvat/NSW.png"))); //13
			kuvat.add(ImageIO.read(new File("Kuvat/FULL.png"))); //14
			kuvat.add(ImageIO.read(new File("Kuvat/ukkeli.png"))); //15
			
			//V‰rikuvat
			kuvat.add(ImageIO.read(new File("Kuvat/Nsin.png"))); //16
			kuvat.add(ImageIO.read(new File("Kuvat/Esin.png"))); //17
			kuvat.add(ImageIO.read(new File("Kuvat/Ssin.png"))); //18
			kuvat.add(ImageIO.read(new File("Kuvat/Wsin.png"))); //19
			kuvat.add(ImageIO.read(new File("Kuvat/NEsin.png"))); //20
			kuvat.add(ImageIO.read(new File("Kuvat/NSsin.png"))); //21
			kuvat.add(ImageIO.read(new File("Kuvat/NWsin.png"))); //22
			kuvat.add(ImageIO.read(new File("Kuvat/ESsin.png"))); //23
			kuvat.add(ImageIO.read(new File("Kuvat/EWsin.png"))); //24
			kuvat.add(ImageIO.read(new File("Kuvat/SWsin.png"))); //25
			kuvat.add(ImageIO.read(new File("Kuvat/NEWsin.png"))); //26
			kuvat.add(ImageIO.read(new File("Kuvat/NESsin.png"))); //27
			kuvat.add(ImageIO.read(new File("Kuvat/ESWsin.png"))); //28
			kuvat.add(ImageIO.read(new File("Kuvat/NSWsin.png"))); //29
			
			//Weavekuvat
			kuvat.add(ImageIO.read(new File("Kuvat/NSali.png"))); //30
			kuvat.add(ImageIO.read(new File("Kuvat/NSalisin.png"))); //31
			kuvat.add(ImageIO.read(new File("Kuvat/NSalisinA.png"))); //32
			kuvat.add(ImageIO.read(new File("Kuvat/NSalisinF.png"))); //33
			kuvat.add(ImageIO.read(new File("Kuvat/EWali.png"))); //34
			kuvat.add(ImageIO.read(new File("Kuvat/EWalisin.png"))); //35
			kuvat.add(ImageIO.read(new File("Kuvat/EWalisinA.png"))); //36
			kuvat.add(ImageIO.read(new File("Kuvat/EWalisinF.png"))); //37
			
			//Ovi
			kuvat.add(ImageIO.read(new File("Kuvat/NESovi.png"))); //38
			kuvat.add(ImageIO.read(new File("Kuvat/NESovisin.png"))); //39
			kuvat.add(ImageIO.read(new File("Kuvat/ESWovi.png"))); //40
			kuvat.add(ImageIO.read(new File("Kuvat/ESWovisin.png"))); //41
			
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(Start.frame,
		    "Jokin tiedosto puuttuu");
    	   System.exit(1);
		}
    }
    
    private BufferedImage getExit(Square.Paikka paikka, int color) {
    	if (paikka == Square.Paikka.ESW && color == 0) return kuvat.get(40);
    	if (paikka == Square.Paikka.ESW && color == 1) return kuvat.get(41);
    	if (paikka == Square.Paikka.NES && color == 0) return kuvat.get(38);
    	if (paikka == Square.Paikka.NES && color == 1) return kuvat.get(39);
    	
    	return null;
    	
    }
    
    //Hakee v‰rillisen kuvan
    private BufferedImage getColorImage (Square.Paikka paikka, int color) {
    	
    	if (paikka == null) {
			return kuvat.get(15);
		}
		
		switch (paikka) {
		
		case N: return kuvat.get(16);
		case E: return kuvat.get(17);
		case S: return kuvat.get(18);
		case W: return kuvat.get(19);
		case NE: return kuvat.get(20);
		case NS: return kuvat.get(21);
		case NW: return kuvat.get(22);
		case ES: return kuvat.get(23);
		case EW: return kuvat.get(24);
		case SW: return kuvat.get(25);
		case NEW: return kuvat.get(26);
		case NES: return kuvat.get(27);
		case ESW: return kuvat.get(28);
		case NSW: return kuvat.get(29);
		case FULL: return kuvat.get(14);
		case NSali: {
			if (color == 1) return kuvat.get(31);
			if (color == 2) return kuvat.get(32);
			if (color == 3) return kuvat.get(33);
		}
		case EWali: {
			if (color == 1) return kuvat.get(35);
			if (color == 2) return kuvat.get(36);
			if (color == 3) return kuvat.get(37);
		}
		default: return null;
		}
    }
    
    //Hakee tarvittavan kuvan
    private BufferedImage getImage(Square.Paikka paikka){
		
		if (paikka == null) {
			return kuvat.get(15);
		}
		
		switch (paikka) {
		
		case N: return kuvat.get(0);
		case E: return kuvat.get(1);
		case S: return kuvat.get(2);
		case W: return kuvat.get(3);
		case NE: return kuvat.get(4);
		case NS: return kuvat.get(5);
		case NW: return kuvat.get(6);
		case ES: return kuvat.get(7);
		case EW: return kuvat.get(8);
		case SW: return kuvat.get(9);
		case NEW: return kuvat.get(10);
		case NES: return kuvat.get(11);
		case ESW: return kuvat.get(12);
		case NSW: return kuvat.get(13);
		case FULL: return kuvat.get(14);
		case NSali: return kuvat.get(30);
		case EWali: return kuvat.get(34);
		default: return null;
		}
	}
}
  