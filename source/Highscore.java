import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/* Huolehtii highscore -listasta.
 */
public class Highscore implements KeyListener {
	
	private String[] nimet = new String[10];
	private double[] askeleet = new double[10];
	private double[] koko = new double[10]; //ruudut yhteensä
	private JFrame frame;
	
	
	//Konstruktori, lukee mahdollisen tiedoston tiedot taulukoihin, ei tee mitään jos tiedostoa ei löydy
	public Highscore() {
		File tiedosto = new File("highscore.txt");
		
		if (tiedosto.exists()) {
			FileReader reader = null;
			try {
				reader = new FileReader(tiedosto);
			} catch (FileNotFoundException e2) {
			}
			BufferedReader rivi = new BufferedReader(reader);
			
			String teksti = null;
			
			
			try {
				teksti = rivi.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			int i = 0;
			String[] data = new String[3];
			while (teksti != null) {
				data = teksti.split(":");
				nimet[i] = data[0];
				askeleet[i] = Double.parseDouble(data[1]);
				koko[i] = Double.parseDouble(data[2]);
				
				try {
					teksti = rivi.readLine();
					i++;
				} catch (IOException e) {
				}
			}
			
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		
	}
	
	public String getName(int index) {
		return this.nimet[index];
	}
	
	//Lisää pelaajan nimen ja pisteet listaan oikeeseen väliin, mikäli pelaajalla on tarpeeksi pisteitä
	public void addName (Player pelaaja, int koko) {
		if (pelaaja.getNimi().length() > 15) {
			JOptionPane.showMessageDialog(this.frame,
		    "Ennätyslistalle kirjoittaminen epäonnistui. Liian pitkä nimi!");
			return;
		}
		
		int index = 0;
		int pisteet = (int)((15.0/pelaaja.getAskeleet())*koko);
		
		for (int i = 0; i < 10; i++) {
			
			if (this.koko[i] == 0 || pisteet > this.koko[i]*(15.0/this.askeleet[i])) {
				index = i;
				break;
			}
		}
		
		String[] nimet2 = new String[10];
		double[] askeleet2 = new double[10];
		double[] koko2 = new double[10]; 
		
		int a = 0;
		
		for (int i = 0; i < 10; i++) {
			
			if (i == index) {
				nimet2[i] = pelaaja.getNimi();
				askeleet2[i] = pelaaja.getAskeleet();
				koko2[i] = koko;
				continue;
			}
			
			nimet2[i] = this.nimet[a];
			askeleet2[i] = this.askeleet[a];
			koko2[i] = this.koko[a];
			
			a++;
		}
		
		this.nimet = nimet2;
		this.askeleet = askeleet2;
		this.koko = koko2;
		
		File tiedosto = new File("highscore.txt");
		
		FileWriter kirjoittaja = null;
		try {
			tiedosto.createNewFile();
			kirjoittaja = new FileWriter(tiedosto);
		} catch (IOException e) {
		}
		
		String teksti = "";
		for (int i = 0; i < 10; i++) {
			teksti = "";
			if (this.koko[i] != 0) {
				teksti += this.nimet[i] + ":" + this.askeleet[i] + ":" + this.koko[i] + "\n";
				try {
					kirjoittaja.write(teksti);
				} catch (IOException e) {
				}
			}
		}
		
		try {
			kirjoittaja.close();
		} catch (IOException e) {
		}
	}
	
	//Avaa highscoreikkunan
	public void showHighscores(){
		JFrame frame = new JFrame("Highscoret!");
		this.frame = frame;
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setLayout(new GridLayout(11,1));
	    //int pituus = this.nimet.length;
	    frame.setPreferredSize(new Dimension(300,350));
        frame.setLocation(680, 200);
        
        
        
        JLabel label1 = new JLabel("Ennätykset: (Nimi, pisteet)");
        frame.add(label1,0);
        
        ArrayList<JLabel> labelit = new ArrayList<JLabel>();
        
        String teksti;
        double pisteet;
        
        for (int i = 0; i < 10; i++) {
        	teksti = "";
        	if (nimet[i] != null) {
        	teksti += nimet[i] + "     ";
        	pisteet = koko[i]*(15.0/askeleet[i]);
        	teksti += (int)pisteet;
        	
        	labelit.add(new JLabel(teksti));
        	frame.add(labelit.get(i), 1+i);
        	}
        }
        
        
        frame.addKeyListener(this);
        
        frame.pack();
        frame.setVisible(true);
        
	}
	
	public JFrame getFrame() {
		return this.frame;
	}

	//Hävittää highscore -ikkunan kun mitä tahansa nappia painetaan
	@Override
	public void keyPressed(KeyEvent arg0) {
		this.frame.dispose();
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
