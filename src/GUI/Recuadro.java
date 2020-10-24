package GUI;

import java.io.IOException;
import java.io.InputStream;

import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Recuadro extends JLabel {
	private static final long serialVersionUID = -9137252487087508049L;
	
	private final Color colorError = Color.red;
	private final Color colorNormal = Color.white;
	
	private static ImageIcon imagenes[];
	private boolean editable;
	private int valor;
	private int coordX, coordY;
	
	/**
	 * Inicializa un recuadro con valor v, por defecto editable
	 * @param v Valor numerico del recuadro
	 */
	public Recuadro(int v) {
		this(v, true);
	}
	
	/**
	 * Inicializa un recuadro del juego
	 * @param v Valor numerico del recuadro
	 * @param editable Define si el recuadro puede ser modificado
	 */
	public Recuadro(int v, boolean editable) {
		// Una sola instancia de todas las imagenes se preinicializa
		// Una vez inicializadas, este bloque no se ejecuta
		try {
			if (imagenes == null) {
				imagenes = new ImageIcon[10];
				InputStream stream;
				
				for (int i = 0; i < 10; i++) {
					stream = getClass().getResourceAsStream("/assets/numeros/" + i + ".png");
					if (stream != null) {
						imagenes[i] = new ImageIcon(ImageIO.read(stream));
					} else {
						throw new IOException("Imagen no encontrada");
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// Configurar el estado del recuadro
		valor = v;
		this.editable = editable;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Dado el valor actual del recuadro,
	 * configura su icono con la imagen
	 * correspondiente.
	 */
	public void actualizarImagen() {
		this.setIcon(imagenes[valor]);
	}
	
	/**
	 * @return La imagen actual del recuadro
	 */
	public ImageIcon getIcono() {
		return imagenes[valor];
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public Color getColorError() {
		return colorError;
	}
	
	public Color getColorNormal() {
		return colorNormal;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public int getValor() {
		return valor;
	}
	
	public void setValor(int v) {
		valor = v;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public boolean esEditable() {
		return editable;
	}
	
	public void setEditable(boolean e) {
		editable = e;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Configura las coordenadas x e y del recuadro
	 * @param x Coordenada x del recuadro
	 * @param y Coordenada y del recuadro
	 */
	public void setCoords(int x, int y) {
		coordX = x;
		coordY = y;
	}
	
	/**
	 * Recupera las coordenadas del recuadro
	 * @return Arreglo de dos elementos [x, y]
	 */
	public int[] getCoords() {
		int[] coords = new int[2];
		coords[0] = coordX;
		coords[1] = coordY;
		return coords;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
}