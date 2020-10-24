package GUI;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Sudoku.Celda;
import Sudoku.Tablero;

public class PanelTablero extends JPanel {
	private static final long serialVersionUID = -4420005865651854535L;
	
	private int dimension;
	private JFrame framePadre;
	private Tablero logica;
	private Selector selectorNumeros;

	public PanelTablero(JFrame framePadre, Selector sel) {
		this.framePadre = framePadre;
		setBackground(Color.WHITE);
		selectorNumeros = sel;
	}
	
	public void setLogica(Tablero sudoku) {
		logica = sudoku;
	}
	
	public void inicializarRecuadros() {
		dimension = logica.getDimension();
		setLayout(new GridLayout(dimension, dimension, dimension/3, dimension/3));
		
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				
				Recuadro rec = new Recuadro(0, false);
				rec.setBorder(new LineBorder(rec.getColorNormal(), 2));
				rec.setCoords(x, y);
				rec.actualizarImagen();
				this.add(rec);
				
				rec.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						int selectorActual;
						
						if (rec.esEditable()) {
							selectorActual = selectorNumeros.getSeleccionado();
							
							// La interfaz grafica recupera las coordenadas de la jugada
							// y le pregunta a la logica si esta es una jugada valida
							// Solo si lo es, se actualiza la GUI. Si no lo es, se informa
							// al jugador y se le deja tomar una decision
							int[] coordsRec = rec.getCoords();
							rec.setBorder(new LineBorder(rec.getColorNormal(), 2));
							
							if (!logica.verificarJugada(coordsRec[0], coordsRec[1], selectorActual)) {
								rec.setBorder(new LineBorder(rec.getColorError(), 2));
								int x = coordsRec[0] + 1;
								int y = coordsRec[1] + 1;
										
								mostrarMensaje("Jugada incorrecta en la posicion [" + x + ", " + y + "]");
							} else {
								// La jugada es valida: actualizar la representacion grafica del tablero
								rec.setValor(selectorActual);
								ImageIcon icono = rec.getIcono();
								rec.setIcon(icono);
								rec.setBorder(new LineBorder(rec.getColorNormal(), 2));
								
								if (logica.jugadorGano()) {
									mostrarMensaje("Usted ha ganado!");
									resetearTablero();
								}
							}
							
						}
					}
				});				
			}
		}
	}
	
	/**
	 * Genera un JOptionPane con un mensaje para informar
	 * al usuario de algo
	 * @param mensaje Informacion que se le da al usuario
	 */
	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(framePadre, mensaje, "Sudoku", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Obtiene la configuracion del tablero de la logica
	 */
	public void inicializarTablero() {
		Recuadro rec;
		int valorCelda;
		Celda c;
		
		// Configurar el estado de cada celda segun el estado del tablero
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				c = logica.getCelda(x, y);
				valorCelda = c.getValor();
				
				// Los unicos componentes del panel son Recuadros,
				// asi que se pueden recuperar transformando las
				// coordenadas 2D a coordenadas 1D
				rec = (Recuadro) getComponent(x * dimension + y);
				
				rec.setEditable(true);
				rec.setValor(valorCelda);
				rec.actualizarImagen();
				
				// si la celda no es editable, hacer que el recuadro no sea editable
				if (!c.esEditable()) {
					rec.setEditable(false);
				}
			}
		}
	}
	
	public void resetearTablero() {
		Recuadro rec;
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				rec = (Recuadro) getComponent(x * dimension + y);
				rec.setBorder(new LineBorder(rec.getColorNormal(), 2));
				rec.setValor(0);
				rec.setEditable(false);
				rec.actualizarImagen();
			}
		}

		logica.resetear();
	}
	
	/**
	 * Limpia los recursos de la aplicacion y
	 * detiene la ejecucion del juego
	 */
	public void cerrar() {
		framePadre.dispose();
		System.exit(0);
	}

}
