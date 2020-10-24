package GUI;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Sudoku.Tablero;

public class Juego extends JFrame {
	private static final long serialVersionUID = -3673320636191996954L;
	
	private JPanel contentPane;
	private PanelTablero panel_tablero;
	private Selector selector;
	private BarraTitulo barra;
	
	public Juego() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(50, 50, 1000, 1000);
		setTitle("Sudoku - Proyecto 2");
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		setContentPane(contentPane);		
	
		inicializarComponentes();
		pack();
	}
	
	private void inicializarComponentes() {
		int dimension = 9;
				
		selector = new Selector(dimension);
		panel_tablero = new PanelTablero(this, selector);
		
		Tablero t = new Tablero(dimension, panel_tablero);
		panel_tablero.setLogica(t);
		panel_tablero.inicializarRecuadros();
		
		barra = new BarraTitulo(panel_tablero);
		barra.setVisible(true);
		contentPane.add(barra);		
		
		panel_tablero.setVisible(true);
		contentPane.add(panel_tablero);

		selector.setVisible(true);
		contentPane.add(selector);
	}

}