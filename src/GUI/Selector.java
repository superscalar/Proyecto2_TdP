package GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * El selector es un arreglo de Recuadros entre los cuales el usuario puede
 * elegir un numero para jugarlo en el tablero. Solo un recuadro puede estar
 * seleccionado al mismo tiempo, y este se marca con un borde rojo 
 */
public class Selector extends JPanel {
	private static final long serialVersionUID = -7016478706002563759L;
	
	private Color colorHighlight;
	private Recuadro[] numeros;
	private int numeroSeleccionado;
	private Recuadro actualSeleccionado;

	public Selector(int dimension) {
		numeros = new Recuadro[dimension];
		numeroSeleccionado = 1; // numero seleccionado por defecto
		colorHighlight = Color.RED;
		
		// Se define el layout del selector como una grilla horizontal que contiene los recuadros
		setLayout(new GridLayout(1, numeros.length, 5, 2));
		setBorder(new LineBorder(Color.BLUE, 5));
		
		// Para cada recuadro del selector, asignarle su numero correspondiente
		// y inicializar un listener para procesar los clicks del usuario
		for (int i = 0; i < numeros.length; i++) {
			numeros[i] = new Recuadro(i+1, false);
			numeros[i].actualizarImagen();
			this.add(numeros[i]);
			
			numeros[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Recuadro r = (Recuadro) e.getComponent();
					
					// Si hay un recuadro seleccionado antes, limpiar su borde
					if (actualSeleccionado != null) {
						actualSeleccionado.setBorder(new LineBorder(colorHighlight, 0));
					}
					
					// Actualizar el selector con el ultimo recuadro clickeado
					// y agregarle un borde para que el usuario sepa que numero
					// esta actualmente seleccionado.
					actualSeleccionado = r;
					actualSeleccionado.setBorder(new LineBorder(colorHighlight, 2));
					
					numeroSeleccionado = r.getValor();
				}
			});
			
		}
		
		// Como 1 es el numero selecionado por defecto, se muestra graficamente
		actualSeleccionado = numeros[0];
		numeros[0].setBorder(new LineBorder(colorHighlight, 2));
	}
	
	/**
	 * @return El numero actualmente seleccionado
	 */
	public int getSeleccionado() {
		return numeroSeleccionado;
	}
	
}