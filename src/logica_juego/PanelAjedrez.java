package logica_juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PanelAjedrez extends JPanel {
	
	//private final int WIDTH = 1000;
	//private final int HEIGHT = 800;
	private MiBoton piezaEnJuego;
	private MiBoton[][] tablero = new MiBoton[8][8];
	private String turno = "WHITE";
	private boolean enroque = false;
	private boolean restaurarMov;
	boolean jaque = false;
	boolean jaqueMate;
	private List<int[]> movimientos = Collections.synchronizedList(new ArrayList<>());
	private List<String> tiposJugador;
	private String tipo;
	
	private Integer[] movInicial = new Integer[2];
	
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	
	private final Object lockMovimientos = new Object();

	
	public PanelAjedrez(int WIDTH, int HEIGHT, ObjectOutputStream salida, List<String> tiposJugador) {
		setSize(WIDTH, HEIGHT);
		this.salida = salida;
		setBackground(Color.lightGray);
		setLayout(null);
		this.tiposJugador = tiposJugador;
		eleccionTipo();
	}
	
	private void setTipo(String tipo) {
		this.tipo = tipo;
		
		try {
	    	synchronized (lockMovimientos) {
	            salida.writeObject(tipo);
	            salida.flush();
	            lockMovimientos.notify();
	        }
		
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void setTipos(List<String> tipos) {
		tiposJugador = tipos;
		if (tiposJugador != null) {
	        blancas.setEnabled(!tiposJugador.contains("WHITE"));
	        negras.setEnabled(!tiposJugador.contains("BLACK"));
	    }
	}
	
	JButton blancas;
	JButton negras;
	
	public void eleccionTipo() {
		
		blancas = new JButton("Jugar con blancas");
		negras = new JButton("Jugar con negras");
		JButton observador = new JButton("Observador");
		blancas.setFont(new Font("mv boli", Font.BOLD, 25));
		negras.setFont(new Font("mv boli", Font.BOLD, 25));
		observador.setFont(new Font("mv boli", Font.BOLD, 25));
		blancas.setBounds(700, 200, 300, 50);
		negras.setBounds(700, 400, 300, 50);
		observador.setBounds(700, 600, 300, 50);
		
		if (tiposJugador != null) {
	        blancas.setEnabled(!tiposJugador.contains("WHITE"));
	        negras.setEnabled(!tiposJugador.contains("BLACK"));
	    }
		
		MouseListener tipo = new MouseAdapter() {		
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JButton boton = (JButton) e.getSource();
				String tipo = boton.getText();
				
				switch (tipo) {
				case "Jugar con blancas":
					setTipo("WHITE");
					break;
				case "Jugar con negras":
					setTipo("BLACK");
					break;
				default:
					setTipo("OBSERVADOR");
					break;
				}
				removeAllComponentes();
				crearTablero();
				anadirPiezas();
				repaint();
			}
			
		};
		
		blancas.addMouseListener(tipo);
		negras.addMouseListener(tipo);
		observador.addMouseListener(tipo);
		
		this.add(blancas);
		this.add(negras);
		this.add(observador);
		
	}

	public void crearTablero(){
		int ejeX = 400;
		int ejeY = 100;
		int contCol = 1;
		
		MouseListener escucharRaton = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MiBoton boton = (MiBoton) e.getSource();
				synchronized (lockMovimientos) {
					
				if(boton.isEnabled()) {
					if(piezaEnJuego == null && boton.getPieza().getColor().equals(tipo) && tipo.equals(turno)) {
							piezaEnJuego = boton;
							piezaEnJuego.setSeleccionado(true);
							
							for (MiBoton[] tab : tablero) {
								for (MiBoton miBoton : tab) {
									miBoton.setEnabled(false);
								}
							}
							
							piezaEnJuego.setEnabled(true);
							piezaEnJuego.setBackground(Color.LIGHT_GRAY);
							restaurarMov = piezaEnJuego.getPieza().getPrimerMov();
							colorearMovimientos(piezaEnJuego, tablero);
							
							movInicial = new Integer[] {boton.getFila(), boton.getColumna()};
							
							if(piezaEnJuego.getPieza().getPieza().equals("King")) {
								comprobarEnroque();
							}
					}else {	
						if(boton.isSeleccionado()) {
							recolorBoard();
							habilitarPiezas();
							boton.setSeleccionado(false);
							piezaEnJuego.getPieza().setPrimerMov(restaurarMov);
							piezaEnJuego = null;
							enroque = false;
						}else {
							Integer[] movFinal = new Integer[] {boton.getFila(), boton.getColumna()};
							
							partidaGanada(piezaEnJuego, boton);
							boton.cambioPieza(piezaEnJuego);
							boton.setSeleccionado(false);
							if(boton.getPieza().getPieza().equals("Pawn") && (boton.getFila() == 7 || boton.getFila() == 0)) {
								coronarPeon(boton);
								deshabilitarTablero();
								repintar();
							}

							if(enroque) {
								if((boton.getColumna()-piezaEnJuego.getColumna()) == 2) {
									tablero[piezaEnJuego.getFila()][boton.getColumna()-1].cambioPieza(tablero[piezaEnJuego.getFila()][7]);
								}else if((boton.getColumna()-piezaEnJuego.getColumna()) == -2) {
									tablero[piezaEnJuego.getFila()][boton.getColumna()+1].cambioPieza(tablero[piezaEnJuego.getFila()][0]);
								}
								enroque = false;
							}

							comprobarJaque();
							recolorBoard();
							habilitarPiezas();
							
							piezaEnJuego.setSeleccionado(false);
							piezaEnJuego = null;							
							
							turno = turno.equals("WHITE") ? "BLACK" : "WHITE";
							
							comprobarJaqueMate();
							
							enviarMovimientoAlServidor(movInicial, movFinal);
						}}
					}
				}
			}
		};
			
		for(int i = 0; i <8; i++) {
			for(int j = 0; j<8;j++) {
				tablero[i][j] = new MiBoton(i,j);
				tablero[i][j].setBounds(ejeX,ejeY,100,100);
				if(contCol == 1) {
					tablero[i][j].setBackground(Color.white);
	        		contCol*=-1;
				}else {
					tablero[i][j].setBackground(Color.DARK_GRAY);
	        		contCol*=-1;
				}
				ejeX +=100;
				tablero[i][j].addMouseListener(escucharRaton);
				this.add(tablero[i][j]);
			}
			contCol*=-1;
			ejeX = 400;
			ejeY +=100;
		}		
	}
	
	public void anadirPiezas() {
		
		String jugador = tipo;
		String rival = tipo.equalsIgnoreCase("BLACK") ? "WHITE":"BLACK";
		String[] piezas;
		if(jugador.equals("BLACK")) {
			piezas = new String[] {"Rook", "Knight", "Bishop", "King", "Queen", "Bishop", "Knight", "Rook"};
		}else {
			piezas = new String[] {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
		}
		
		Arrays.stream(tablero[1]).forEach(x -> {x.setPieza(rival,"Pawn"); x.setEnabled(true);});
		IntStream.range(0, piezas.length).forEach(col -> {tablero[0][col].setPieza(rival, piezas[col]); tablero[0][col].setEnabled(true);});
		
		Arrays.stream(tablero[6]).forEach(x -> {x.setPieza(jugador,"Pawn"); x.setEnabled(true);});
		IntStream.range(0, piezas.length).forEach(col -> {tablero[7][col].setPieza(jugador, piezas[col]); tablero[7][col].setEnabled(true);});

	}
	
	public void recolorBoard() {
		int contCol = 1;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
	        	if((tablero[row][col].isEmpty() || !tablero[row][col].getPieza().getPieza().equals("King") || (tablero[row][col].getPieza().getPieza().equals("King") && !(((Rey) tablero[row][col].getPieza()).getEnJaque())))) {
		        	if (contCol == 1) {
		        		tablero[row][col].setBackground(Color.white); 		// White background squares
		        		contCol*=-1;
		        	}else {
		        		tablero[row][col].setBackground(Color.DARK_GRAY);  // Black background squares
		        		contCol*=-1;
		        	}
	        	}else {
	        		tablero[row][col].setBackground(Color.orange);
	        		contCol*=-1;
	        	}
	       }
	       contCol*=-1;
	    }
	}
	
	public void colorearMovimientos(MiBoton pieza, MiBoton[][] tablero) {
		
		List<int[]> movimientos = sinMovIlegales(pieza, tablero);
		
		for (int[] movimiento : movimientos) {
	        int fila = movimiento[0];
	        int columna = movimiento[1];
	        if (pieza.getPieza().comprobarMovimiento(fila, columna)) {
	            MiBoton casilla = tablero[fila][columna];
	            Color color = Color.yellow;

	            if (!casilla.isEmpty()) {
	                if (casilla.getPieza().getPieza().equals("King")) {
	                    color = Color.red;
	                }
	            }

	            casilla.setBackground(color);
	            casilla.setEnabled(true);
	        }
		}
	}
	
	public List<int[]> sinMovIlegales(MiBoton pieza, MiBoton[][] tablero) {
	    List<int[]> movimientosFinales = new ArrayList<>();
	    int x;
        int y;
	    PiezaAjedrez guardada = pieza.getPieza();
	    PiezaAjedrez destino;

	    for (int[] movimiento : pieza.getPieza().getMovimientos(pieza, tablero)) {
	        if (pieza.getPieza().comprobarMovimiento(movimiento[0], movimiento[1])) {
	            movimientosFinales.add(movimiento);
	        }
	    }

	    pieza.deletePieza();
	    comprobarJaque();

	    if (jaque || guardada.getPieza().equals("King")) {
	        for (int i = movimientosFinales.size() - 1; i >= 0; i--) {
	            x = movimientosFinales.get(i)[0];
	            y = movimientosFinales.get(i)[1];

	            destino = tablero[x][y].getPieza() != null?tablero[x][y].getPieza(): null;
	            tablero[x][y].setPieza(guardada);
	            comprobarJaque();
	            
	            if (jaque) {
	                movimientosFinales.remove(i);
	            }

	            if(destino != null) {
					tablero[x][y].setPieza(destino);
				}else {
					tablero[x][y].deletePieza();
				}
	        }
	    }

	    pieza.setPieza(guardada);
	    comprobarJaque();
	    return movimientosFinales;
	}
	
	public void comprobarJaqueMate() {
		
		boolean hayMovimientos = false;
		for(int i = 0; i < tablero.length && !hayMovimientos; i++) {
			for(int j = 0; j < tablero.length && !hayMovimientos; j++) {
				if(tablero[i][j].getPieza() != null && tablero[i][j].getPieza().getColor().equals(turno)) {
					if(sinMovIlegales(tablero[i][j], tablero).size() != 0){
						hayMovimientos = true;
					}
				}
			}
		}
		if(!hayMovimientos) {
			finalizarPartida();
		}
	}
	
	public void comprobarJaque() {
		jaque = false;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if((!(tablero[i][j].isEmpty())) && tablero[i][j].getPieza().getPieza().equals("King")) {
					if((((Rey) tablero[i][j].getPieza()).getEnJaque())) {
						((Rey) tablero[i][j].getPieza()).setEnJaque(false);
					}
					restaurarMov = tablero[i][j].getPieza().getPrimerMov();
					movimientos = tablero[i][j].getPieza().getMovimientos(tablero[i][j], tablero);
					tablero[i][j].getPieza().setPrimerMov(restaurarMov);
				}	
			}
		}
		for(int i = 0; i < movimientos.size(); i++) {
			if(!(tablero[movimientos.get(i)[0]][movimientos.get(i)[1]].isEmpty())) {
				if(tablero[movimientos.get(i)[0]][movimientos.get(i)[1]].getPieza().getPieza().equals("King")) {
					((Rey) tablero[movimientos.get(i)[0]][movimientos.get(i)[1]].getPieza()).setEnJaque(true);					
					jaque = true;
				}
			}
		}
		movimientos.clear();
	}
	
	
	private JButton[] seleccion = new JButton[4];
	public void coronarPeon(MiBoton boton){
		int ejeX = 100;
		int ejeY = 400;
		String color = boton.getPieza().getColor();
		String [] eleccion = {"Queen", "Rook", "Knight", "Bishop"};
		
		MouseListener escucharRaton = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JButton evento = (JButton) e.getSource();
				boton.deletePieza();
				boton.setPieza(color, evento.getText());
				habilitarPiezas();
				pintarTablero();
				enviarEleccionCoronarPeon("" + boton.getFila() + boton.getColumna()  +  evento.getText());
			}
		};
			
		for(int i = 0; i < 4; i++) {
				seleccion[i] = new JButton(eleccion[i]);
				seleccion[i].setBounds(ejeX,ejeY,200,100);
				seleccion[i].setBackground(Color.green);
				ejeY +=100;
				seleccion[i].addMouseListener(escucharRaton);
				this.add(seleccion[i]);
		}
	}	
	
	public void comprobarEnroque(){
		enroque = true;
		if(piezaEnJuego.getPieza().getPrimerMov()) {
			if((tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()-4].getPieza() == null) ||
				!(tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()-4].getPieza().getPrimerMov())) {
				enroque = false;
				if(tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()-3].getBackground().equals(Color.white)) {
					tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()-2].setBackground(Color.DARK_GRAY);
				}else {
					tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()-2].setBackground(Color.white);
				}
			}
			if((tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()+3].getPieza() == null) ||
			!(tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()+3].getPieza().getPrimerMov())) {
				enroque = false;
				if(tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()+2].getBackground().equals(Color.white)) {
					tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()+1].setBackground(Color.DARK_GRAY);
				}else {
					tablero[piezaEnJuego.getFila()][piezaEnJuego.getColumna()+1].setBackground(Color.white);
				}
			}
		}
	}
	
	public void partidaGanada(MiBoton piezaMueve, MiBoton piezaComida) {
		if(piezaComida.getPieza() != null) {
			if(piezaComida.getPieza().getPieza().equals("King")) {
				finalizarPartida();
			}
		}
	}
	
	public void finalizarPartida(){
		JLabel etiquetaGanador = new JLabel("Han ganado las " + (turno.equals("WHITE")?"negras!!":"blancas!!"));
		etiquetaGanador.setBounds(550,350,500,100);
		etiquetaGanador.setForeground(Color.BLACK);
		etiquetaGanador.setOpaque(false);
		etiquetaGanador.setFont(new Font("mv boli", Font.BOLD, 40));
		
		JLabel etiquetaNuevaPartida = new JLabel("¿Quereis volver a jugar?");
		etiquetaNuevaPartida.setBounds(600,420,500,50);
		etiquetaNuevaPartida.setForeground(Color.BLACK);
		etiquetaNuevaPartida.setOpaque(false);
		etiquetaNuevaPartida.setFont(new Font("mv boli", Font.BOLD, 30));
		
		removeAllComponentes();
		add(etiquetaGanador);
		add(etiquetaNuevaPartida);
		botonNuevaPartida();
		repintar();
	}
	
	private void botonNuevaPartida() {
		JButton nuevaPartida = new JButton("SI");
		JButton finPrograma = new JButton("Cerrar");
		nuevaPartida.setFont(new Font("mv boli", Font.BOLD, 25));
		finPrograma.setFont(new Font("mv boli", Font.BOLD, 25));
		nuevaPartida.setBounds(650, 500, 140, 50);
		finPrograma.setBounds(800, 500, 140, 50);
		
		MouseListener NuevaPartida = new MouseAdapter() {		
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAllComponentes();
				crearTablero();
				anadirPiezas();
				repintar();
				turno = "WHITE";
				enroque = false;
			}
		};
		MouseListener fin = new MouseAdapter() {		
			@Override
			public void mouseClicked(MouseEvent e) {
	            System.exit(0);
			}
		};
		
		nuevaPartida.addMouseListener(NuevaPartida);
		finPrograma.addMouseListener(fin);
		
		this.add(nuevaPartida);
		this.add(finPrograma);		
	}
	
	public void habilitarPiezas() {
		deshabilitarTablero();
		Arrays.stream(tablero).flatMap(Arrays::stream).forEach(x -> {if(!x.isEmpty())  x.setEnabled(true);});
	}
	
	public void deshabilitarTablero() {
		Arrays.stream(tablero).flatMap(Arrays::stream).forEach(x -> x.setEnabled(false));
	}
	
	public void removeAllComponentes() {
		this.removeAll();
	}
	
	public void repintar() {
		this.repaint();

	}
	
	public void pintarTablero() {
		this.removeAll();		
		Arrays.stream(tablero).flatMap(Arrays::stream).forEach(this::add);
		this.repaint();
	}
	
	private void enviarMovimientoAlServidor(Integer[] movInicial, Integer[] movFinal) {
	    try {
	    	Integer[][] movRealizado = new Integer[][] {movInicial, movFinal};

	    	synchronized (lockMovimientos) {
	            salida.writeObject(movRealizado);
	            salida.flush();
	            lockMovimientos.notify();
	        }
		
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void enviarEleccionCoronarPeon(String pieza) {
		try {
			synchronized (lockMovimientos) {
				salida.writeObject(pieza);
				salida.flush();
				lockMovimientos.notify();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void recibirCoronarPeon(String pieza) {
		int fila = 7 - Integer.valueOf("" + pieza.charAt(0));
		int columna = 7 - Integer.valueOf("" + pieza.charAt(1));
		pieza = pieza.substring(2);
		tablero[fila][columna].setPieza(tipo == "WHITE"?"BLACK":"WHITE", pieza);
	}
	
	public void movimientoDelServidor(Integer[][] entrada) {
		synchronized (lockMovimientos) {
			
			MiBoton pieza = tablero[7-entrada[0][0]][7-entrada[0][1]];
			MiBoton otro = tablero[7-entrada[1][0]][7-entrada[1][1]];
			
			partidaGanada(pieza, otro);
			otro.cambioPieza(pieza);
			otro.setSeleccionado(false);
	
			if(enroque) {
				if((otro.getColumna()-pieza.getColumna()) == 2) {
					tablero[pieza.getFila()][otro.getColumna()-1].cambioPieza(tablero[pieza.getFila()][7]);
				}else if((otro.getColumna()-pieza.getColumna()) == -2) {
					tablero[pieza.getFila()][otro.getColumna()+1].cambioPieza(tablero[pieza.getFila()][0]);
				}
				enroque = false;
			}
	
			comprobarJaque();
			recolorBoard();
			habilitarPiezas();
			
			pieza.setSeleccionado(false);
			pieza = null;							
			
			if(turno.equals("WHITE")) {
				turno = "BLACK";
			}else {
				turno = "WHITE";
			}
			comprobarJaqueMate();
			lockMovimientos.notify();
		}
	}
}


