
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

import com.sun.org.apache.bcel.internal.generic.LMUL;

/** Clase principal de minijuego de coche para Práctica 02 - Prog III
 * Ventana del minijuego.  
 * @author Andoni Eguíluz
 * Facultad de Ingeniería - Universidad de Deusto (2014)
 */
public class VentanaJuego extends JFrame {
	private static final long serialVersionUID = 1L;  // Para serialización
	JPanel pPrincipal;         // Panel del juego (layout nulo)
	MundoJuego miMundo;        // Mundo del juego
	CocheJuego miCoche;        // Coche del juego
	MiRunnable miHilo = null;  // Hilo del bucle principal de juego	
	int cuentaDeEstrellasFallo; // atributo que cuenta el número de estrellas que no se han cogido
	int cuentaPuntos; // Cuenta el número de puntos obtenidos cogiendo estrellas
	JLabel lMensaje = new JLabel("PUNTUACION"); //Mensaje de puntuación
	boolean arrayPressed [] = new boolean [4];	 //Array de booleans que simboliza qué botón está pulsado
	/** Constructor de la ventana de juego. Crea y devuelve la ventana inicializada
	 * sin coches dentro
	 */
	public VentanaJuego() {
		// Liberación de la ventana por defecto al cerrar
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Creación contenedores y componentes
		pPrincipal = new JPanel();
		JPanel panelMensaje = new JPanel();
		// Formato y layouts
		pPrincipal.setLayout( null );
		pPrincipal.setBackground( Color.white );
		// Añadido de componentes a contenedores
		add( pPrincipal, BorderLayout.CENTER );
		// Formato de ventana
		setSize( 1000, 750 );
		setResizable( false );
		// Escuchadores de botones
		panelMensaje.add(lMensaje);
		add(panelMensaje, BorderLayout.SOUTH);
		
		// Añadido para que también se gestione por teclado con el KeyListener
		pPrincipal.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: {
						arrayPressed[0] = true;
						break;
					}
					case KeyEvent.VK_DOWN: {
						arrayPressed[1] = true;
						break;
					}
					case KeyEvent.VK_LEFT: {
						arrayPressed[2] = true;
						break;
					}
					case KeyEvent.VK_RIGHT: {
						arrayPressed[3] = true;
						break;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: {
					arrayPressed[0] = false;
					break;
				}
				case KeyEvent.VK_DOWN: {
					arrayPressed[1] = false;
					break;
				}
				case KeyEvent.VK_LEFT: {
					arrayPressed[2] = false;
					break;
				}
				case KeyEvent.VK_RIGHT: {
					arrayPressed[3] = false;
					break;
				}
			}
		}
		
			
		});
		pPrincipal.setFocusable(true);
		pPrincipal.requestFocus();
		pPrincipal.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				pPrincipal.requestFocus();
			}
		});
		// Cierre del hilo al cierre de la ventana
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (miHilo!=null) miHilo.acaba();
			} 
		});
	}
	
	/** Programa principal de la ventana de juego
	 * @param args
	 */
	public static void main(String[] args) {
		// Crea y visibiliza la ventana con el coche
		try {
			final VentanaJuego miVentana = new VentanaJuego();
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					miVentana.setVisible( true );
				}
			});
			miVentana.miMundo = new MundoJuego( miVentana.pPrincipal );
			miVentana.miMundo.creaCoche( 150, 100 );
			miVentana.miCoche = miVentana.miMundo.getCoche();
			miVentana.miCoche.setPiloto( "Fernando Alonso" );
			// Crea el hilo de movimiento del coche y lo lanza
			miVentana.miHilo = miVentana.new MiRunnable();  // Sintaxis de new para clase interna
			Thread nuevoHilo = new Thread( miVentana.miHilo );
			nuevoHilo.start();
		} catch (Exception e) {
			System.exit(1);  // Error anormal
		}
	}
	
	/** Clase interna para implementación de bucle principal del juego como un hilo
	 * @author Andoni Eguíluz
	 * Facultad de Ingeniería - Universidad de Deusto (2014)
	 */
	class MiRunnable implements Runnable {
		boolean sigo = true;
		@Override
		public void run() {
			// Bucle principal forever hasta que se pare el juego...
			while (sigo) {
				
				// Mover coche
				miCoche.mueve( 0.040 );
				
//				System.out.println(miCoche.getVelocidad());
//				System.out.println(miCoche.fuerzaAceleracionAdelante());
//				System.out.println(miCoche.fuerzaAceleracionAtras());
				
				// Chequear choques
				// (se comprueba tanto X como Y porque podría a la vez chocar en las dos direcciones (esquinas)
				if (miMundo.hayChoqueHorizontal(miCoche)) // Espejo horizontal si choca en X
					miMundo.rebotaHorizontal(miCoche);
				if (miMundo.hayChoqueVertical(miCoche)) // Espejo vertical si choca en Y
					miMundo.rebotaVertical(miCoche);
				
				miMundo.creaEstrella();
				cuentaDeEstrellasFallo += miMundo.quitaYRotaEstrellas(6000);
				cuentaPuntos += 5 * miMundo.choquesConEstrellas();
				MundoJuego.aplicarFuerza(0, miCoche);
				if(arrayPressed[0] == true)
				{
					MundoJuego.aplicarFuerza(miCoche.fuerzaAceleracionAdelante(), miCoche);
				}
				if(arrayPressed[1] == true)
				{
					MundoJuego.aplicarFuerza(miCoche.fuerzaAceleracionAtras(), miCoche);

				}
				if(arrayPressed[2] == true)
				{
					miCoche.gira( +10 );

				}
				if(arrayPressed[3] == true)
				{
					miCoche.gira( -10 );

				}
				lMensaje.setText("PUNTUACION: "+cuentaPuntos);
				acaba();
				
				// Dormir el hilo 40 milisegundos
				try {
					Thread.sleep( 40 );
				} catch (Exception e) {
				}
			}
		}
		/** Ordena al hilo detenerse en cuanto sea posible
		 */
		public void acaba() {
			if(cuentaDeEstrellasFallo == 10)
			{
				sigo = false;
				JOptionPane.showMessageDialog(pPrincipal,"Has conseguido : "+cuentaPuntos+" puntos", "Puntuación final", JOptionPane.PLAIN_MESSAGE);
			}
		}
	};
	
}
