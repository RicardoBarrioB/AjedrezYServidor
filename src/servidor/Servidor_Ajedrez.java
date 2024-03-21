package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Servidor_Ajedrez {
	
	private int puerto;
	private static List<ClienteServidor> clientes = Collections.synchronizedList(new ArrayList<>());
	private static List<String> tiposJugador = Collections.synchronizedList(new ArrayList<>());

		
	public Servidor_Ajedrez(int puerto) {
		this.puerto = puerto;
		iniciar();
	}
		
	public void iniciar() {

		try (ServerSocket Servidor = new ServerSocket(puerto)){
			while (true) {
				System.out.println("Servidor iniciado en el puerto " + puerto);
				Socket cliente = Servidor.accept();
				System.out.println("Cliente conectado desde " + cliente.getInetAddress());
				ClienteServidor jugador = new ClienteServidor(cliente, clientes, tiposJugador);
				//synchronized (clientes) {
				    clientes.add(jugador);
				//}
				new Thread(jugador).start();
			}
		}catch (IOException e) {
			e.printStackTrace();
			System.err.println("No se pudo abrir el puerto " + puerto);
        }
	}
	
	public static void main(String[] args) {
		new Servidor_Ajedrez(12345);
	}	
}
