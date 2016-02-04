import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class Listener implements ActionListener {
	
	public Labyrinth laby;
	private int luku1, luku2;
	private long seed;

	@Override
	public void actionPerformed(ActionEvent e) {
		
		/*Mik‰li pelaaja on klikannut Lataa peli -painiketta*/
		if (e.getActionCommand() == "Lataa peli") {
			
			//Tiedoston valitsija
			final JFileChooser fc = new JFileChooser();
			int palautus = fc.showOpenDialog(null);
			int paikkaX = -1, paikkaY = -1;
			
			//Mik‰li tiedosto on valittu:
            if (palautus == JFileChooser.APPROVE_OPTION) {
            	File file = fc.getSelectedFile();
            	
            	//Otetaan tiedoston nimest‰ pelaajan nimi
            	String nimi = fc.getName(file);
            	nimi = nimi.substring(0, nimi.length()-4);
            	Start.lista.remove(4);
    			Start.lista.add(4, nimi);
    			
            	try {
					FileReader lukija = new FileReader(file);
					BufferedReader rivi = new BufferedReader(lukija);
					
					//Ensimm‰isell‰ rivill‰ oltava teksti: "LabyrinthGame"
					String teksti = rivi.readLine();
					if (!teksti.equals("LabyrinthGame")) {
						JOptionPane.showMessageDialog(Start.frame,
		    		    "Tiedosto ei kelpaa!");
						return;
					}
					
					//Toinen ja kolmas rivi labyrintin koon
					teksti = rivi.readLine();
					luku2 = muunnaJaTark(teksti);
					teksti = rivi.readLine();
					luku1 = muunnaJaTark(teksti);
					
					//Nelj‰s rivi kertoo labyrintin tyypin
					teksti = rivi.readLine();
					Start.lista.remove(0);
					Start.lista.add(0, teksti);
					
					//Viides ja kuudes rivi kertoo pelaajan paikan
					teksti = rivi.readLine();
					paikkaX = muunna(teksti);
					teksti = rivi.readLine();
					paikkaY = muunna(teksti);
					
					//Kuudes rivi on labyrintin seed
					teksti = rivi.readLine();
					seed = muunnaLong(teksti);
						
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(Start.frame,
					"Tiedosto ei kelpaa!");
					return;
				}
				
				//Piilotetaan Start, tehd‰‰n Labyrinth ja aloitetaan Game
				Start.frame.setVisible(false);
				luoLaby(seed);
				new Game(laby, paikkaX, paikkaY);
				
				return;

            } else {
            	return;
            }
		}
		
		/*Mik‰li pelaaja on klikannu Aloita -painiketta*/
		if (e.getActionCommand() == "Aloita") {
			
			//Tarkistetaan ett‰ kentiss‰ olevat luvut ovat sopivan kokoisia
			try {
			luku1 = muunnaJaTark(Start.lista.get(1));
			luku2 = muunnaJaTark(Start.lista.get(2));
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(Start.frame,
    		    "Labyrintin koko tulee olla v‰lill‰ 5 - 20!");
				return;
			}
			
			//Tarkistetaan ett‰ pelaaja on antanut nimen
			if (Start.lista.get(4) == ""){
				JOptionPane.showMessageDialog(Start.frame,
    		    "Nimi puuttuu!");
				return;
			}
			
			//Piilotetaan Start, luodaan uusi Labyrintti ja aloitetaan peli
			Start.frame.setVisible(false);
			luoLaby(-1);
			new Game(laby, -1, -1);
			
			return;
		}
		if (e.getActionCommand() == "Lopeta") {
			System.exit(0);
		}
	}
	
	//Kutsuu valitun tyyppisen labyrintin konstruktoria
	private void luoLaby(long seed) {
		if (Start.lista.get(0).equals("0")){
			laby = new Normal(luku1,luku2, seed);
		}
		if (Start.lista.get(0).equals("1")){
			laby = new Weave(luku1,luku2, seed);
		}
	}
	
	//Muuntaa annetun tekstin Int -luvuksi ja tarkistaa ett‰ se on v‰lill‰ 5 - 100
	private int muunnaJaTark(String teksti) throws NumberFormatException {
		
		int luku = 0;
		try {
		luku = Integer.parseInt(teksti);
		} catch (NumberFormatException a) {
			throw (new NumberFormatException());
		}
		
		if (luku < 5 || luku > 20) {
			throw (new NumberFormatException());
		}
		
		return luku;
	}
	//Muuntaa annetun tekstin Int -luvuksi
	private int muunna(String teksti) throws NumberFormatException {
			
			int luku = 0;
			try {
			luku = Integer.parseInt(teksti);
			} catch (NumberFormatException a) {
				throw (new NumberFormatException());
			}
			return luku;
	}
	
	//Muuntaa annetun tekstin Long -luvuksi
	private long muunnaLong(String teksti) throws NumberFormatException {
		
		long luku = 0;
		try {
			luku = Long.parseLong(teksti);
		} catch (NumberFormatException a) {
			throw (new NumberFormatException());
		}
		return luku;
}

}
