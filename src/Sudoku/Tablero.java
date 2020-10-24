package Sudoku;

import java.util.Random;
import GUI.PanelTablero;

public class Tablero {
	private int dimension; // La dimension define el orden del Sudoku
	
	private Celda[][] celdas;
	private LectorEstado lector;
	private boolean victoria;
	private PanelTablero interfaz;
	
	// Se mantiene un conteo de las celdas vacias, para comenzar a verificar
	// si una solucion del jugador es correcta solo cuando ya se llenaron todas
	// las celdas (no tiene sentido verificar si el jugador ganó cuando todavia
	// hay celdas vacias)
	private int cantCeldasVacias;
	
	/**
	 * Inicializa el estado del tablero dado el estado del archivo
	 * de configuracion obtenido del LectorEstado.
	 * @param dimension Dimension del Sudoku
	 * @param interfaz Interfaz grafica del juego
	 */
	public Tablero(int dimension, PanelTablero interfaz) {
		lector = new LectorEstado(this, "/config/estado.txt", dimension);
		this.interfaz = interfaz;
		this.dimension = dimension;
		victoria = false;
		
		// Representacion interna del tablero: arreglo 2D de celdas
		celdas = new Celda[dimension][dimension];
		int valor;
		
		// Inicializar el estado del tablero con los valores del
		// archivo de configuracion dados por el lector
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				valor = lector.siguienteCelda();
				celdas[x][y] = new Celda(valor);
			}
		}
		
		if (!verificarSolucion()) {
			// Contactar a la interfaz: el estado del archivo representa una solucion invalida
			mensajeError("Error: El estado guardado en el\narchivo representa una solucion invalida");
		} else {
			// Si el estado del archivo es correcto, se procede a preparar el tablero
			// para el jugador
			desresolver();
		}
		
		
	}
	
	/**
	 * A partir del tablero completo elimina celdas aleatoriamente
	 * para presentar al jugador un estado a partir del cual puede jugar.
	 * 
	 * Segun un paper publicado en 2012 (https://arxiv.org/abs/1201.0749)
	 * la cantidad minima de pistas para que un Sudoku siempre tenga una solucion
	 * valida unica es 17. Sin embargo, no se puede lograr eliminando celdas aleatoriamente,
	 * por lo que el metodo usado en esta funcion no logra soluciones unicas.
	 */
	private void desresolver() {
		Random rnd = new Random();
		cantCeldasVacias = 0;
		
		// Por cada celda hay un 80% de probabilidades
		// de que se elimine su valor
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				
				// Parametrizar la probabilidad de eliminacion
				// podria ser una forma valida de agregar niveles
				// de dificultad
				if (rnd.nextFloat() < .8f) {
					celdas[x][y].setValor(0);
					cantCeldasVacias++;
				} else {
					// La celda no fue eliminada, por lo que sirve de pista
					// al usuario. Entonces no deberia ser posible editarla
					celdas[x][y].setEditable(false);
				}
			}
		}
	}
	
	/**
	 * Decide segun el estado del tablero si verificar la jugada del
	 * jugador individualmente o, si el tablero esta completo, verifica
	 * que la solucion dada es valida
	 * @param x La coordenada x de la jugada
	 * @param y La coordenada y de la jugada
	 * @param valor El valor nuevo de la jugada
	 * @return Falso si hay al menos un valor repetido
	 */
	public boolean verificarJugada(int x, int y, int valor) {
	 	boolean repetido = false;
	 	int valorAnterior;
	 	
	 	// Si todas las celdas estan ocupadas
	 	// hay que verificar que todo el tablero sea valido.
	 	// Si es el caso, el jugador ganó el juego.
	 	if (cantCeldasVacias <= 1) {
	 		valorAnterior = celdas[x][y].getValor();
	 		celdas[x][y].setValor(valor);
	 		if (verificarSolucion()) {
	 			repetido = false;
	 			victoria = true;
	 		} else {
	 			repetido = true;
	 			celdas[x][y].setValor(valorAnterior);
	 		}
	 	} else {
	 		// Quedan celdas vacias, asi que solo se checkea la fila, columna y panel
	 		// de la jugada del usuario (y no todo el tablero)
	 		repetido = !verificarJugadaIndividual(x, y, valor);
	 	}
		 
	 	return !repetido;
	 }
	
	/**
	 * Dada una jugada en (x,y) se verifica que no se incumpla
	 * ninguna regla dado el valor para esa celda
	 * @param x La coordenada x de la jugada
	 * @param y La coordenada y de la jugada
	 * @param valor El valor nuevo de la jugada
	 * @return Falso si hay al menos un valor repetido
	 */
	private boolean verificarJugadaIndividual(int x, int y, int valor) {
		boolean repetido = false;
	 	int valorAnterior = celdas[x][y].getValor();
	 	int panelX;
	 	int panelY;
	 	
	 	// Si la celda estaba vacia anteriormente
	 	// ahora esta ocupada, y ya no se puede vaciar
	 	if (valorAnterior == 0) {
	 		cantCeldasVacias--;
	 	}
	 	
	 	celdas[x][y].setValor(valor);
		
	 	// Recorrer la fila de la jugada buscando duplicados
	 	for (int col = 0; (col < dimension) && !repetido; col++) {
	 		if ((col != y) && (celdas[x][col].getValor() == valor)) {
	 			repetido = true;
	 		}
	 	}
	 	
	 // Recorrer la columna de la jugada buscando duplicados
	 	for (int fila = 0; (fila < dimension) && !repetido; fila++) {
	 		if ((fila != x) && (celdas[fila][y].getValor() == valor)) {
	 			repetido = true;
	 		}
	 	}

	 	// Calcular el panel 3x3 segun la posicion (x, y):
	 	// restarle a [x] e [y] hasta llegar al primer perimetro de panel
	 	// que se encuentra en las posiciones 0, 3, 6
	 	
	 	panelX = x;
	 	panelY = y;
	 	
		while ((panelX != 0) && (panelX != 3) && (panelX != 6)) {
			panelX--;
		}

	 	while ((panelY != 0) && (panelY != 3) && (panelY != 6)) {
	 		panelY--;
	 	}

	 	// Se checkea el panel correspondiente a (x, y) para buscar duplicados
	 	for (int pX = panelX; (pX < (panelX+3))  && !repetido; pX++) {
	 		for (int pY = panelY; (pY < (panelY+3)) && !repetido; pY++) {
	 			
	 			if ( (pX != x) && (pY != y) &&
	 				 (celdas[pX][pY].getValor() == valor)) {
		 			repetido = true;
		 		}
	 			
	 		}
	 	}
	 	
	 	// Si se incumple alguna regla, devolver la celda
	 	// a su valor anterior para preservar el estado pre-jugada
	 	if (repetido) {
	 		celdas[x][y].setValor(valorAnterior);
	 		
	 		// Ademas, como la celda de hecho no se ocupo,
	 		// sigue estando vacia (hay que devolver el conteo
	 		// de celdas vacias a su valor original)
	 		if (valorAnterior == 0) {
		 		cantCeldasVacias++;
		 	}
	 	}
 	
	 	return !repetido;
	}
	
	private boolean verificarSolucion() {
		// La idea usada para encontrar duplicados en cada celda, fila y panel
		// es crear un arreglo de 9 enteros, y asignarle cada numero a su indice
		// correspondiente. Si hay un numero repetido, se accede al mismo indice dos veces
		// y eso es la señal de que el estado del tablero no es una solucion invalida
		
		boolean noHayRepetidos;
		noHayRepetidos = checkearFilasYColumnas();
		noHayRepetidos = noHayRepetidos && checkearPaneles();
		
		return noHayRepetidos;
	}
	
	// Devuelve false si hay alguna fila incorrecta
	private boolean checkearFilasYColumnas() {
		boolean hayRepetidos = false;
		int valorFila, valorColumna;
		Integer[] numerosFila, numerosColumna;
		
		// Se revisan las coordenadas (0,0) a (dimension, dimension)
		for (int f = 0; f < dimension && !hayRepetidos; f++) {
			for (int c = 0; c < dimension && !hayRepetidos; c++) {
				
				numerosFila = new Integer[dimension];
				numerosColumna = new Integer[dimension];
				
				// Indexar el tablero de ambas formas
				// resulta en checkear tanto las filas
				// como las columnas en una sola pasada
				valorFila = celdas[f][c].getValor();
				valorColumna = celdas[c][f].getValor();
				
				// Buscar duplicados en la fila actual
				if (numerosFila[valorFila-1] == null) {
					numerosFila[valorFila-1] = valorFila;
				} else {
					hayRepetidos = true;
				}
				
				// Buscar duplicados en la columna actual
				if (numerosColumna[valorColumna-1] == null) {
					numerosColumna[valorColumna-1] = valorColumna;
				} else {
					hayRepetidos = true;
				}
				
			}
		}
		
		return !hayRepetidos;
	}
	
	/**
	 * Recorre cada panel uno a uno verificando que no existan celdas repetidas
	 * @return false si hay algun panel incorrecto, true si son todos validos
	 */
	private boolean checkearPaneles() {
		boolean hayRepetidos = false;
		Integer[] numeros;
		int cantPaneles = dimension/3;
		int valor;
		int coordPanelX, coordPanelY;
		
		// Para un sudoku de orden 3 la cantidad total de iteraciones es 3^4 == 81
		// Los dos for externos recorren cada panel
		for (int pIdx = 0; pIdx < cantPaneles && !hayRepetidos; pIdx++) {
			for (int pIdy = 0; pIdy < cantPaneles && !hayRepetidos; pIdy++) {
				
				numeros = new Integer[dimension];				
				coordPanelX = pIdx*cantPaneles;
				coordPanelY = pIdy*cantPaneles;
				
				// Los dos for internos recorren cada celda en un panel
				for (int panelX = coordPanelX; panelX < (coordPanelX+3) && !hayRepetidos; panelX++) {
					for (int panelY	= coordPanelY; panelY < (coordPanelY+3) && !hayRepetidos; panelY++) {
						
						valor = celdas[panelX][panelY].getValor();
						
						// Esta funcion solo se llama cuando el tablero esta completo
						// asi que no puede haber celdas con valor 0 tal que se intente
						// aceder al indice -1
						if (numeros[valor-1] == null) {
							numeros[valor-1] = valor;
						} else {
							hayRepetidos = false;
						}
						
					}
				}
				
			}
			
		}
		
		
		return !hayRepetidos;
	}
	
	/**
	 * Dada una coordenada (x,y) devuelve la Celda que se encuentra
	 * en esa posición del tablero
	 * @param x La coordenada horizontal de la celda
	 * @param y La coordenada vertical de la celda
	 * @return La celda en (x,y) si las coordenadas son validas o null
	 */
	public Celda getCelda(int x, int y) {
		Celda c;
		
		// Validar rango de x e y
		if ( (x < 0) || (x >= dimension) || (y < 0) || (y >= dimension) ){
			c = null;
		} else {
			c = celdas[x][y];
		}
		
		return c;
	}
	
	/**
	 * Establece el estado del tablero a como
	 * se encontraba inicialmente antes de una partida
	 * y despues desresuelve para dejarlo listo para jugar
	 */
	public void resetear() {
		int valor;
		lector.resetearEstado();		
		
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				valor = lector.siguienteCelda();
				celdas[x][y] = new Celda(valor);
			}
		}
		
		desresolver();
	}
	
	/**
	 * @return La dimension del tablero
	 */
	public int getDimension() {
		return dimension;
	}
	
	public boolean jugadorGano() {
		return victoria;
	}
	
	/**
	 * Reportar un error a la interfaz y
	 * cerrarla (no es recuperable)
	 * @param error Una descripción de lo que sucedió
	 */
	public void mensajeError(String error) {
		interfaz.mostrarMensaje(error);
		interfaz.cerrar();
	}
}
