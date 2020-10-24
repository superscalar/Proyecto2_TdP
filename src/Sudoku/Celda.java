package Sudoku;

/**
 * Clase usada para la representacion interna del tablero de juego.
 * Encapsula su valor y una flag para indicar si se puede modificar su valor.
 */
public class Celda {
	private int valor;
	private boolean editable;	
	
	public Celda(int v) {
		valor = v;
		editable = true;
	}
	
	public boolean esEditable() {
		return editable;
	}
	
	public void setEditable(boolean e) {
		editable = e;
	}
	
	public int getValor() {
		return valor;
	}
	
	public void setValor(int v) {
		valor = v;
	}
}
