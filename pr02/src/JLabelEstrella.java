import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class JLabelEstrella extends JLabel{
	

	private static final long serialVersionUID = 1L;
	public static final int TAMANYO_ESTRELLA = 40;  // píxels (igual ancho que algo)
	public long tiempoDeCreacion;
	
	public JLabelEstrella() {
		try {
			setIcon( new ImageIcon(( "img/estrella.png" ) ) );
			tiempoDeCreacion = System.currentTimeMillis();
		} catch (Exception e) {
			System.err.println( "Error en carga de recurso: estrella.png no encontrado" );
			e.printStackTrace();
		}
		setBounds( 0, 0, TAMANYO_ESTRELLA, TAMANYO_ESTRELLA );
	}
	
	
	public long getTiempoDeCreacion() {
		return tiempoDeCreacion;
	}

	private double miGiro = Math.PI/2;
	/** Cambia el giro del JLabel
	 * @param gradosGiro
	 */
	public void setGiro( double gradosGiro ) {
		// De grados a radianes...
		miGiro += gradosGiro;
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		Image img = ((ImageIcon)getIcon()).getImage();
		Graphics2D g2 = (Graphics2D) g; 
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		g2.rotate( miGiro, TAMANYO_ESTRELLA/2, TAMANYO_ESTRELLA/2 ); 
		
        g2.drawImage( img, 0, 0, TAMANYO_ESTRELLA, TAMANYO_ESTRELLA, null );
        
	}
}
