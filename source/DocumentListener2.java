import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/* DocuListener hoitaa kenttien koko1, koko2, koko3, tiedostonimen ja nimikentän kuuntelun.
 * Tiedot tallennetaan Alkuvalikon listaan lista, kohtiin 1-4.
 */


public class DocumentListener2 implements DocumentListener {
	
	
	public void insertUpdate(DocumentEvent e) {
		
		String uusiteksti = null;
		try {
			uusiteksti = e.getDocument().getText(0,
					e.getDocument().getLength());
		} catch (BadLocationException e1) {
			System.out.println("Lukuvirhe!");
		}
		
		if (e.getDocument().getProperty(1) == "1") {
			Start.lista.remove(1);
			Start.lista.add(1, uusiteksti);
		}
		
		if (e.getDocument().getProperty(1) == "2") {
			Start.lista.remove(2);
			Start.lista.add(2, uusiteksti);
		}
		
		if (e.getDocument().getProperty(1) == "3") {
			Start.lista.remove(3);
			Start.lista.add(3, uusiteksti);
		}
		
		if (e.getDocument().getProperty(1) == "nimi") {
			Start.lista.remove(4);
			Start.lista.add(4, uusiteksti);
		}
			
    }
	
    public void removeUpdate(DocumentEvent e) {
    	this.insertUpdate(e);
    	
    }
    
	@Override
	public void changedUpdate(DocumentEvent e) {
		//näitä ei tapahdu
	}


}
