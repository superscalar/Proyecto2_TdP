package GUI;

import java.io.IOException;
import java.io.InputStream;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Reloj que representa el tiempo desde que inicia el juego
 * en un formato MM:SS (con a lo sumo dos digitos para cada parte)
 * El maximo de tiempo es 99:59
 */
public class Reloj extends JPanel {
	private static final long serialVersionUID = 3750345975363796323L;
	
	private int minutos;
	private int segundos;
	private Timer timer;
	
	private ImageIcon[] digitos;
	private JLabel labelSeparador;
	private JLabel minuto1, minuto2;
	private JLabel segundo1, segundo2;
	
	public Reloj() {
		ImageIcon separador;
		
		minuto1 = new JLabel();
		minuto2 = new JLabel();
		segundo1 = new JLabel();
		segundo2 = new JLabel();
		
		digitos = new ImageIcon[10];
		InputStream stream;
		
		try {
		
			for (int i = 0; i < 10; i++) {
				stream = getClass().getResourceAsStream("/assets/reloj/d" + i + ".png");
				digitos[i] = new ImageIcon(ImageIO.read(stream));
			}
			
			stream = getClass().getResourceAsStream("/assets/reloj/separador.png");
			separador = new ImageIcon(ImageIO.read(stream));
			labelSeparador = new JLabel();
			labelSeparador.setIcon(separador);
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// Evento usado por el timer para actualizar el reloj cada segundo
		ActionListener eventoTimer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tick();
				actualizarApariencia();				
			}
		};
		
		int segundo = 1000; // en milisegundos
		timer = new Timer(/* cada */ segundo, /* lanzar un */ eventoTimer);
		
		this.add(minuto1);
		this.add(minuto2);
		
		this.add(labelSeparador);
		
		this.add(segundo1);
		this.add(segundo2);
		
		ImageIcon cero = digitos[0];
		minuto1.setIcon(cero);
		minuto2.setIcon(cero);
		segundo1.setIcon(cero);
		segundo2.setIcon(cero);		
	}
	
	public void iniciar() {
		resetear();
		timer.start();
	}
	
	public void resetear() {
		minutos = 0;
		segundos = 0;
		actualizarApariencia();
	}
	
	public void detener() {
		timer.stop();
	}
	
	/**
	 * Avanzar el estado del reloj por un segundo
	 */
	private void tick() {
		if (segundos < 59) {
			segundos++;
		} else {
			segundos = 0;
			minutos++;
		}
		
		// Si se alcanzan 100 minutos,
		// se devuelve el reloj a 00:00
		if (minutos > 99) {
			minutos = 0;
		}
	}
	
	/**
	 * Actualiza las imagenes que constituyen la representacion
	 * visual del reloj
	 */
	private void actualizarApariencia() {
		// Se obtiene cada digito individual
		int digitoM1 = (minutos / 10);
		int digitoM2 = (minutos % 10);
		
		int digitoS1 = (segundos / 10);
		int digitoS2 = (segundos % 10);
		
		// Se actualizan los minutos y segundos con
		// los graficos apropiados
		minuto1.setIcon(digitos[digitoM1]);
		minuto2.setIcon(digitos[digitoM2]);
		segundo1.setIcon(digitos[digitoS1]);
		segundo2.setIcon(digitos[digitoS2]);
	}
}
