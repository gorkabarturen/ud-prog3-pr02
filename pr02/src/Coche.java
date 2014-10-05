
/** Clase para definir instancias lógicas de coches con posición, dirección y velocidad.
 * @author Andoni Eguíluz
 * Facultad de Ingeniería - Universidad de Deusto (2014)
 */
public class Coche {
	protected double miVelocidad;  // Velocidad en pixels/segundo
	protected double miDireccionActual;  // Dirección en la que estoy mirando en grados (de 0 a 360)
	protected double posX;  // Posición en X (horizontal)
	protected double posY;  // Posición en Y (vertical)
	protected String piloto;  // Nombre de piloto
	protected static double MASA = 1; // Masa del coche
	protected double fuerzaBaseAlante = 2000; 
	protected double fuerzaBaseAtras = -1000;
	// Coeficientes de rozamiento
	protected static double COEF_RZTO_AIRE = 0.35;
	protected static double COEF_RZTO_SUELO = 15.5;
	
	// Constructores
	
	public Coche() {
		miVelocidad = 0;
		miDireccionActual = 0;
		posX = 300;
		posY = 300;
	}

	/** Devuelve la velocidad actual del coche en píxeles por segundo
	 * @return	velocidad
	 */
	public double getVelocidad() {
		return miVelocidad;
	}

	/** Cambia la velocidad actual del coche
	 * @param miVelocidad
	 */
	public void setVelocidad( double miVelocidad ) {
		this.miVelocidad = miVelocidad;
	}

	public double getDireccionActual() {
		return miDireccionActual;
	}

	public void setDireccionActual( double dir ) {
		// if (dir < 0) dir = 360 + dir;
		if (dir > 360) dir = dir - 360;
		miDireccionActual = dir;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosicion( double posX, double posY ) {
		setPosX( posX );
		setPosY( posY );
	}
	
	public void setPosX( double posX ) {
		this.posX = posX; 
	}
	
	public void setPosY( double posY ) {
		this.posY = posY; 
	}
	
	public String getPiloto() {
		return piloto;
	}

	public void setPiloto(String piloto) {
		this.piloto = piloto;
	}


	/** Cambia la velocidad actual del coche
	 * @param aceleracion	Incremento/decremento de la velocidad en pixels/segundo
	 * @param tiempo	Tiempo transcurrido en segundos
	 */
	public void acelera( double aceleracion, double tiempo ) {
		miVelocidad = MundoJuego.calcVelocidadConAceleracion( miVelocidad, aceleracion, tiempo );
	}
	
	/** Cambia la dirección actual del coche
	 * @param giro	Angulo de giro a sumar o restar de la dirección actual, en grados (-180 a +180)
	 * 				Considerando positivo giro antihorario, negativo giro horario
	 */
	public void gira( double giro ) {
		setDireccionActual( miDireccionActual + giro );
	}
	
	/** Cambia la posición del coche dependiendo de su velocidad y dirección
	 * @param tiempoDeMovimiento	Tiempo transcurrido, en segundos
	 */
	public void mueve( double tiempoDeMovimiento ) {
		setPosX( posX + MundoJuego.calcMovtoX( miVelocidad, miDireccionActual, tiempoDeMovimiento ) );
		setPosY( posY + MundoJuego.calcMovtoY( miVelocidad, miDireccionActual, tiempoDeMovimiento ) );
	}
	
	@Override
	public String toString() {
		return piloto + " (" + posX + "," + posY + ") - " +
			   "Velocidad: " + miVelocidad + " ## Dirección: " + miDireccionActual; 
	}
	
	/** Devuelve la fuerza de aceleración del coche, de acuerdo al motor definido en la práctica 2 
	 * @return Fuerza de aceleración en Newtixels 
	 */ 
	 public double fuerzaAceleracionAdelante() { 
	 if (miVelocidad<=-150) return fuerzaBaseAlante; 
	 else if (miVelocidad<=0) 
	 return fuerzaBaseAlante*(-miVelocidad/150*0.5+0.5); 
	 else if (miVelocidad<=250) 
	 return fuerzaBaseAlante*(miVelocidad/250*0.5+0.5); 
	 else if (miVelocidad<=750) 
	 return fuerzaBaseAlante; 
	 else return fuerzaBaseAlante*(-(miVelocidad-1000)/250); 
	 }
	 
	 public double fuerzaAceleracionAtras() { 
		 if (miVelocidad<=-150) return fuerzaBaseAtras; 
		 else if (miVelocidad<=0) 
		 return fuerzaBaseAtras*(-miVelocidad/150*0.5+0.5); 
		 else if (miVelocidad<=250) 
		 return fuerzaBaseAtras*(miVelocidad/250*0.5+0.5); 
		 else if (miVelocidad<=750) 
		 return fuerzaBaseAtras; 
		 else return fuerzaBaseAtras*(-(miVelocidad-1000)/250); 
		 }
}
