package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.SwingUtilities;

import logica_juego.Ventana;


public class ClienteAjedrez {
    //private static final String SERVIDOR_IP = "10.2.1.13";
    private static final String SERVIDOR_IP = "localhost";
    private static int puerto = 12345;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private Ventana ventanaJuego;
    private Socket socket;
    private List<String> tiposJugador;
    
    @SuppressWarnings("unchecked")
	public ClienteAjedrez() {
    	
    	try {
    		socket = new Socket(SERVIDOR_IP, puerto);
            System.out.println("Conectado al servidor en " + SERVIDOR_IP + ":" + puerto);
	        salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            try {
				tiposJugador = (List<String>) entrada.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            SwingUtilities.invokeLater(() -> {ventanaJuego = new Ventana(salida, tiposJugador); ventanaJuego.setVisible(true);});
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	while(true) {
    		if (entrada != null) {
    			try {
    				Object objetoLeido = entrada.readObject();
					if(objetoLeido instanceof List) {
						List<String> tipos = (List<String>) objetoLeido;
						setTipos(tipos);
					}else if(objetoLeido instanceof String){
						String pieza = (String) objetoLeido;
						avisarCoronarPeon(pieza);
					}else {
						Integer[][] recibir = (Integer[][]) objetoLeido;
						realizarMovimientoDelServidor(recibir);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public void realizarMovimientoDelServidor(Integer[][] entrada) {
		ventanaJuego.recibirMovimientoDelServidor(entrada);
	}
    
    public void avisarCoronarPeon(String pieza) {
    	ventanaJuego.avisarCoronarPeon(pieza);
    }
    
    public void setTipos(List<String> entrada) {
		ventanaJuego.setTipo(entrada);
	}	
    
	public static void main(String[] args) {
        new ClienteAjedrez();
    }
}
