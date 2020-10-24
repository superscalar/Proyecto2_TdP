package Sudoku;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LectorEstado {
	private int[] celdas;
	private int indice;
	
	/**
	 * Obtiene el estado del archivo y inicializa el lector con este estado
	 * @param path Camino al archivo que contiene el estado del juego
	 * @param dimension Orden del tablero de juego
	 */
	public LectorEstado(Tablero t, String path, int dimension) {
		celdas = new int[dimension*dimension];
		
		try {			
			InputStream is = getClass().getResourceAsStream(path);
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader lector = new BufferedReader(reader);
			
			String linea;
			String[] valores;
			int valorCelda;
			boolean estadoCorrecto = true;
			int cantLineas = 0;
			indice = 0;
			
			String codigoError = null;
			
			/*
			 * Se recorre el archivo linea a linea, y si todo es correcto, se leen
			 * los numeros uno a uno (ignorando espacios) y se ingresan a un
			 * arreglo lineal de tamaño [dimension*dimension]. Si el archivo es correcto,
			 * cuando termina la ejecucion del constructor el cliente de LectorEstado llama
			 * a siguienteCelda() hasta haber recuperado el estado de todas las celdas
			 */
			while (estadoCorrecto && ( (linea = lector.readLine()) != null) ) {
				valores = linea.split(" "); // Se descartan los espacios
				
				// Cada linea del archivo tiene que contener la cantidad correcta de numeros
				if (valores.length != dimension) {
					codigoError = "Faltan o sobran celdas en la linea " + (cantLineas+1);
					estadoCorrecto = false;
				} else {
				
					// Leer numero a numero, validando que estén dentro del rango [1, dimension]
					for (int i = 0; estadoCorrecto && (i < valores.length); i++) {
						valorCelda = Integer.parseInt(valores[i]);
						
						if ( (valorCelda < 1) || (valorCelda > dimension) ) {
							estadoCorrecto = false;
							codigoError = "La celda " + (indice+1) + " esta fuera de rango";
						}
						
						celdas[indice++] = valorCelda;
					}
					
					cantLineas++;
				
				}
			}
			
			// Verificar que se leyeron la cantidad correcta de numeros desde el archivo
			if (estadoCorrecto && indice != (dimension*dimension) ) {
				codigoError = "Faltan o sobran valores en el archivo";
			}
			
			reader.close();
			lector.close();
			
			if (codigoError != null) {
				t.mensajeError(codigoError);
			}

			resetearEstado();
			
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			t.mensajeError("El archivo contiene celdas no numericas");
		} catch (IOException ex) {
			ex.printStackTrace();
			t.mensajeError("Error en la lectura del archivo de configuración");
		}
	}
	
	public int siguienteCelda() {
		return celdas[indice++];
	}
	
	public void resetearEstado() {
		indice = 0;
	}
}
