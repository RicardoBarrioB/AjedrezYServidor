package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


public class ClienteServidor implements Runnable {
    private Socket clienteSocket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private List<ClienteServidor> clientes;
	private List<String> tiposJugador;

    public ClienteServidor(Socket clienteSocket, List<ClienteServidor> clientes, List<String> tiposJugador) {
        this.clienteSocket = clienteSocket;
        this.clientes = clientes;
        this.tiposJugador = tiposJugador; 
        try {
            salida = new ObjectOutputStream(clienteSocket.getOutputStream());
            entrada = new ObjectInputStream(clienteSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
			this.salida.writeObject(tiposJugador);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void run() {
        try {

        	while (true) {

        		if (entrada != null) {
        			Object objetoLeido = entrada.readObject();
        			if(objetoLeido instanceof String) {	
        				char caracter = ((String) objetoLeido).charAt(0);
        				if(Character.isDigit(caracter)) {
        					enviarCoronarPeon(clienteSocket, (String) objetoLeido);
        				}else {
	        				tiposJugador.add((String) objetoLeido);
	        				enviarTiposJugador();
        				}
        				
        			}else {
    	                @SuppressWarnings("unchecked")
    					Integer[][] mensajeCliente = (Integer[][]) objetoLeido;              	
    	                enviarActualizaciones(clienteSocket, mensajeCliente);
        			}
        		}
            }
  	
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void enviarTiposJugador() {
    	try {
    		synchronized (salida) {
                salida.writeObject(tiposJugador);
                salida.flush();
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void enviarCoronarPeon(Socket remitente, String pieza) {
    	for (ClienteServidor cliente : clientes) {
            if (cliente.clienteSocket != remitente) {
                try {
                    cliente.salida.writeObject(pieza);
                    cliente.salida.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void enviarActualizaciones(Socket remitente, Object mensaje) {
        for (ClienteServidor cliente : clientes) {
            if (cliente.clienteSocket != remitente) {
                try {
                    cliente.salida.writeObject(mensaje);
                    cliente.salida.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } 
}

