import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* NappiListener hoitaa labyrintin valintaan käytettävät napit. 0 = Normaali labyrintti,
 * 1 = Weave labyrintti ja 2 = 3D labyrintti.
 */

public class ButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "Normaali"){
			Start.lista.remove(0);
			Start.lista.add(0, "0");
		}
			
		if (e.getActionCommand() == "Weave") {
			Start.lista.remove(0);
			Start.lista.add(0, "1");
		}
		
		if (e.getActionCommand() == "3D") {
			Start.lista.remove(0);
			Start.lista.add(0, "2");
		}
		
	}

}
