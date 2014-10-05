
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

/** "Mundo" del juego del coche.
 * Incluye las físicas para el movimiento y los choques de objetos.
 * Representa un espacio 2D en el que se mueven el coche y los objetos de puntuación.
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class MundoJuego {
	private JPanel panel;  // panel visual del juego
	CocheJuego miCoche;    // Coche del juego
	ArrayList <JLabelEstrella> arrayDeEstrellas = new ArrayList<>();
	
	/** Construye un mundo de juego
	 * @param panel	Panel visual del juego
	 */
	public MundoJuego( JPanel panel ) {
		this.panel = panel;
		Random numeroRandom = new Random();
		JLabelEstrella estrella = new JLabelEstrella();
		arrayDeEstrellas.add(estrella);
		estrella.setLocation(numeroRandom.nextInt(1000), numeroRandom.nextInt(750));
		panel.add(estrella);
		panel.repaint();
	}

	/** Crea un coche nuevo y lo añade al mundo y al panel visual
	 * @param posX	Posición X de pixel del nuevo coche
	 * @param posY	Posición Y de píxel del nuevo coche
	 */
	public void creaCoche( int posX, int posY ) {
		// Crear y añadir el coche a la ventana
		miCoche = new CocheJuego();
		miCoche.setPosicion( posX, posY );
		panel.add( miCoche.getGrafico() );  // Añade al panel visual
		miCoche.getGrafico().repaint();  // Refresca el dibujado del coche
	}
	
	/** Devuelve el coche del mundo
	 * @return	Coche en el mundo. Si no lo hay, devuelve null
	 */
	public CocheJuego getCoche() {
		return miCoche;
	}

	/** Calcula si hay choque en horizontal con los límites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posición actual
	 * @return	true si hay choque horizontal, false si no lo hay
	 */
	public boolean hayChoqueHorizontal( CocheJuego coche ) {
		return (coche.getPosX() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosX()>panel.getWidth()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}
	
	/** Calcula si hay choque en vertical con los límites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posición actual
	 * @return	true si hay choque vertical, false si no lo hay
	 */
	public boolean hayChoqueVertical( CocheJuego coche ) {
		return (coche.getPosY() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosY()>panel.getHeight()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}

	/** Realiza un rebote en horizontal del objeto de juego indicado
	 * @param coche	Objeto que rebota en horizontal
	 */
	public void rebotaHorizontal( CocheJuego coche ) {
		// System.out.println( "Choca X");
		double dir = coche.getDireccionActual();
		dir = 180-dir;   // Rebote espejo sobre OY (complementario de 180)
		if (dir < 0) dir = 360+dir;  // Corrección para mantenerlo en [0,360)
		coche.setDireccionActual( dir );
	}
	
	/** Realiza un rebote en vertical del objeto de juego indicado
	 * @param coche	Objeto que rebota en vertical
	 */
	public void rebotaVertical( CocheJuego coche ) {
		// System.out.println( "Choca Y");
		double dir = miCoche.getDireccionActual();
		dir = 360 - dir;  // Rebote espejo sobre OX (complementario de 360)
		miCoche.setDireccionActual( dir );
	}
	
	/** Calcula y devuelve la posición X de un movimiento
	 * @param vel    	Velocidad del movimiento (en píxels por segundo)
	 * @param dir    	Dirección del movimiento en grados (0º = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoX( double vel, double dir, double tiempo ) {
		return vel * Math.cos(dir/180.0*Math.PI) * tiempo;
	}
	
	/** Calcula y devuelve la posición X de un movimiento
	 * @param vel    	Velocidad del movimiento (en píxels por segundo)
	 * @param dir    	Dirección del movimiento en grados (0º = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoY( double vel, double dir, double tiempo ) {
		return vel * -Math.sin(dir/180.0*Math.PI) * tiempo;
		// el negativo es porque en pantalla la Y crece hacia abajo y no hacia arriba
	}
	
	/** Calcula el cambio de velocidad en función de la aceleración
	 * @param vel		Velocidad original
	 * @param acel		Aceleración aplicada (puede ser negativa) en pixels/sg2
	 * @param tiempo	Tiempo transcurrido en segundos
	 * @return	Nueva velocidad
	 */
	public static double calcVelocidadConAceleracion( double vel, double acel, double tiempo ) {
		return vel + (acel*tiempo);
	}
	
	/** Método que calcula la fuerza del rozamiento
	 * @param masa
	 * @param coefRozSuelo
	 * @param coefRozAire
	 * @param vel
	 * @return devuelve el rozamiento total
	 */
	public static double calcFuerzaRozamiento( double masa, double coefRozSuelo, double coefRozAire, double vel ) { 
			 double fuerzaRozamientoAire = coefRozAire * (-vel); // En contra del movimiento 
			 double fuerzaRozamientoSuelo = masa * coefRozSuelo * ((vel>0)?(-1):1); // Contra mvto 
			 return fuerzaRozamientoAire + fuerzaRozamientoSuelo; 
			 } 
	
	/**Método que calcula la aceleración con la fuerza y la masa
	 * @param fuerza
	 * @param masa
	 * @return devuelve la aceleración
	 */
	public static double calcAceleracionConFuerza( double fuerza, double masa ) { 
		 // 2ª ley de Newton: F = m*a ---> a = F/m 
		 return fuerza/masa; 
		 } 
	
	public static void aplicarFuerza( double fuerza, Coche coche ) { 
		 double fuerzaRozamiento = calcFuerzaRozamiento( Coche.MASA , Coche.COEF_RZTO_SUELO, Coche.COEF_RZTO_AIRE, coche.getVelocidad() ); 
		 double aceleracion = calcAceleracionConFuerza( fuerza+fuerzaRozamiento, Coche.MASA ); 
		 if (fuerza==0) { 
		 // No hay fuerza, solo se aplica el rozamiento 
		 double velAntigua = coche.getVelocidad(); 
		 coche.acelera( aceleracion, 0.04 ); 
		 if (velAntigua>=0 && coche.getVelocidad()<0 
		 || velAntigua<=0 && coche.getVelocidad()>0) { 
		 coche.setVelocidad(0); // Si se está frenando, se para (no anda al revés) 
		 } 
		 } else { 
		 coche.acelera( aceleracion, 0.04 ); 
		 } 
	}
	
	/** Si han pasado más de 1,2 segundos desde la última
	 *  crea una estrella nueva en una posición aleatoria y le añade al mundo y al panel visual */
	long tiempo;
	public void creaEstrella()
	{
		if(System.currentTimeMillis() - tiempo >= 1200)
		{
		Random numeroRandom = new Random();
		JLabelEstrella estrella = new JLabelEstrella();
		tiempo = estrella.getTiempoDeCreacion();
		arrayDeEstrellas.add(estrella);
		estrella.setLocation(numeroRandom.nextInt(900), numeroRandom.nextInt(700));
		panel.add(estrella);
		panel.repaint();
		System.out.println("CREA UNA ESTRELLA");
		}
	}
	
	/**Quita todas las estrellas que lleven en pantalla demasiado tiempo 
	 * y rota 10 grados las que sigan estando
	 * @param maxTiempo Tiempo máximo para que se mantengan las estrellas (msegs)
	 * @return Número de estrellas quitadas
	 */
	
	public int quitaYRotaEstrellas ( long maxTiempo)
	{
		for(int i = 0 ; i<arrayDeEstrellas.size() ; i++)
		{
			arrayDeEstrellas.get(i).setGiro(10);
			panel.repaint();
		
		if(System.currentTimeMillis() - arrayDeEstrellas.get(i).getTiempoDeCreacion() >= maxTiempo)
		{
			panel.remove(arrayDeEstrellas.get(i));
			arrayDeEstrellas.remove(i);
			return 1;
		}
		}
		return 0;
	}
	
	/** Calcula si hay choques del coche con alguna estrella (o varias). Se considera el coque si
	 * se tocan las esferas lógicas del coche y la estrella. Si es así, las elimina.
	 * @return Número de estrellas eliminadas
	 */
	
	public int choquesConEstrellas ()
	{
		 Area areaA = new Area(miCoche.getGrafico().getBounds());
		for( int i = 0; i < arrayDeEstrellas.size() ; i++ )
		{
		 Area areaB = new Area(arrayDeEstrellas.get(i).getBounds());
		 if(areaA.intersects(areaB.getBounds2D()) && arrayDeEstrellas.get(i) != null)  
		 {
			panel.remove(arrayDeEstrellas.get(i));
			arrayDeEstrellas.remove(i);
			System.out.println("PASAS POR ENCIMA");
			return 1;
		 }
		}
		return 0;
		}
	
}
