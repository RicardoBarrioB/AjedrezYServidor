package logica_juego;

import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JFrame;

public class Ventana extends JFrame {
	
    private static final long serialVersionUID = 1L;

	public PanelAjedrez panel;
	public int Tipo;
	
	public Ventana(ObjectOutputStream salida, List<String> tiposJugador){
		this.setSize(1500,1000);
		setTitle("Ajedrez");
		setLocationRelativeTo(null);
		panel = new PanelAjedrez(1500, 1000, salida, tiposJugador);
		this.getContentPane().add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void recibirMovimientoDelServidor(Integer[][] entrada) {
		panel.movimientoDelServidor(entrada);
	}	
	public void setTipo(List<String> entrada) {
		panel.setTipos(entrada);
	}	
	public void avisarCoronarPeon(String pieza) {
    	panel.recibirCoronarPeon(pieza);
    }
}