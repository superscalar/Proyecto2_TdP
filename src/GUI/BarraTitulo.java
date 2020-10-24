package GUI;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class BarraTitulo extends JPanel {
	private static final long serialVersionUID = -2411344376404564253L;
	
	private JButton start;
	private JButton reset;
	private Reloj reloj;

	public BarraTitulo(PanelTablero tablero) {
		setLayout( new FlowLayout(FlowLayout.CENTER, 30, 0) );
		
		reloj = new Reloj();
		start = new JButton("Iniciar");
		reset = new JButton("Resetear");
		reset.setEnabled(false);
				
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/* El boton "Iniciar" inicia el reloj y el tablero,
				 * y permite al usuario presionar "Reset" */
				reloj.iniciar();
				start.setEnabled(false);
				reset.setEnabled(true);
				
				tablero.inicializarTablero();
			}
		});
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/* El boton "Reset" detiene el reloj, resetea el tablero,
				 * y permite al usuario iniciar una nueva partida presionando "Iniciar" */
				reset.setEnabled(false);
				start.setEnabled(true);
				reloj.detener();
				reloj.resetear();
				
				tablero.resetearTablero();
			}
		});
		
		// Orden de los componentes: start - reset - reloj
		add(start);
		add(reset);
		add(reloj);
	}
	
}