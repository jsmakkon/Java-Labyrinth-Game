import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/* Alkuvalikko -luokka piirt‰‰ pelin alkaessa ilmestyv‰n alkuvalikon ja huolehtii
 * kenttiin annettujen tietojen tallennuksesta. Peli alkaa varsinaisesti kun pelaaja
 * klikkaa Aloita -nappia, tai lataa peli -nappia ja valitsee aiemmin tallennetun 
 * tiedoston.
 * 
 */
public class Start {
	
	public static JFrame frame;
	public static ArrayList<String> lista = new ArrayList<String>(10); //Lista annetuista syˆtteist‰

	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI();
            }
        });

	}
	
	
	public static void GUI() {
		
		//Lis‰t‰‰n listaan alkuarvot valinnoille:
		lista.add(0, "0"); // Labyrintin valinta, 0 = normi, 1 = weave, 2 = 3D
		lista.add(1, "0"); // Koko x, v‰lilt‰ 5-99
		lista.add(2, "0"); // Koko y  v‰lilt‰ 5-99
		lista.add(3, "0"); // Koko z  v‰lilt‰ 1-2
		lista.add(4, ""); // Pelaajan nimi
		
		
		//Frame kuntoon
		frame = rakenna(new JFrame("Labyrinttipeli"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
	}
	
	
		/* Ikkunan luonti*/
	
	public static JFrame rakenna(JFrame frame) {
		
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        //Ikkunan koko ja sijainti
        frame.setPreferredSize(new Dimension(450,450));
        frame.setLocation(200, 200);
        
        //Ikkunan kokoa ei anneta muuttaa
        frame.setResizable(false);
        
        //Reunukset, mit‰ k‰ytet‰‰n JPaneleissa
        Border perus = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
        /*Labyrintin valinta*/
        JPanel paneeli = new JPanel(new GridLayout(3,1));
        paneeli.setBorder(BorderFactory.createTitledBorder(perus, "Labyrintin tyyppi"));
        paneeli.setPreferredSize(new Dimension(150,200));
        
        //Radiobuttonit
        JRadioButton normi = new JRadioButton("Normaali");
        normi.doClick();
        JRadioButton weave = new JRadioButton("Weave");
        JRadioButton k3d = new JRadioButton("3D");
        
        ButtonGroup ryhma = new ButtonGroup();
        
        //normi.putClientProperty(1, "1");
        
        ryhma.add(normi);
        ryhma.add(weave);
        ryhma.add(k3d);
        
        normi.addActionListener(new ButtonListener());
        k3d.addActionListener(new ButtonListener());
        weave.addActionListener(new ButtonListener());
        
        paneeli.add(normi);
        paneeli.add(weave);
        //paneeli.add(k3d); //3D - labyrinttivalinta
        
        //Asetetaan paneelin paikka
        c.gridx = 0;
    	c.gridy = 0;
    	c.gridheight = 3;
    	
        pane.add(paneeli, c);
        
        /*Labyrintin koon valinta*/
        JPanel paneeli2 = new JPanel(new GridLayout(1,4));
        paneeli2.setBorder(BorderFactory.createTitledBorder(perus, "Labyrintin koko"));
        paneeli2.setPreferredSize(new Dimension(200,50));
        
        JLabel labelx = new JLabel("X:");
        JLabel labely = new JLabel("Y:");

        JTextField koko1 = new JTextField(20);
        koko1.getDocument().putProperty(1, "1");
        JTextField koko2 = new JTextField(20);
        koko2.getDocument().putProperty(1, "2");
        
        koko1.getDocument().addDocumentListener(new DocumentListener2());
        koko2.getDocument().addDocumentListener(new DocumentListener2());
        
        koko1.setPreferredSize(new Dimension(10,30));
        koko2.setPreferredSize(new Dimension(10,30));
        
        paneeli2.add(labelx);
        paneeli2.add(koko1);
        paneeli2.add(labely);
        paneeli2.add(koko2);
        
        c.ipady = 0;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        
        pane.add(paneeli2, c);
        
        /*Nimenanto*/
        JPanel nimit = new JPanel(new GridLayout(1,4));
        nimit.setBorder(BorderFactory.createTitledBorder(perus, "Nimesi"));
        nimit.setPreferredSize(new Dimension(200,50));
        
        JTextField nimiLaatta = new JTextField(20);
        nimiLaatta.getDocument().putProperty(1, "nimi");
        nimiLaatta.setPreferredSize(new Dimension(10,30));
        nimit.add(nimiLaatta);
        nimiLaatta.getDocument().addDocumentListener(new DocumentListener2());
        
        c.gridy = 1;
        
        pane.add(nimit, c);
        
        /*Painikkeet*/
        JPanel napit = new JPanel(new GridLayout(1,3));
        napit.setPreferredSize(new Dimension(200,30));
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridwidth= 2;
        c.gridy = 3;
        
        JButton nappi1 = new JButton("Lataa peli");
        JButton nappi2 = new JButton("Aloita");
        JButton nappi3 = new JButton("Lopeta");
        
        nappi1.addActionListener(new Listener());
        nappi2.addActionListener(new Listener());
        nappi3.addActionListener(new Listener());
        
        nappi1.setPreferredSize(new Dimension(200,50));
        nappi2.setPreferredSize(new Dimension(200,50));
        nappi3.setPreferredSize(new Dimension(200,50));
        
        napit.add(nappi1);
        napit.add(nappi2);
        napit.add(nappi3);
        
        pane.add(napit, c);
        
        return frame;
		
	}
	
	
	

}
