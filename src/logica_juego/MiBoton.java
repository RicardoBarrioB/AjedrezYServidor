package logica_juego;

import java.io.Serializable;

import javax.swing.JButton;

public class MiBoton extends JButton implements Serializable{
	
    //private static final long serialVersionUID = 1L;
    
	private boolean seleccionado = false;
	private int columna;
	private int fila;
	private PiezaAjedrez pieza;
	
	public MiBoton(int fila, int columna){
		this.fila = fila;
		this.columna = columna;
		this.setEnabled(false);
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public PiezaAjedrez getPieza() {
		return pieza;
	}
	
	public void deletePieza() {
		this.pieza = null;
		this.setIcon(null);
	}
	
	public void cambioPieza(MiBoton boton) {
		this.pieza = boton.getPieza();
		this.setIcon(this.getPieza().getImagenPieza());
		boton.deletePieza();
	}
	
	public void setPieza(PiezaAjedrez pieza) {
		this.pieza = pieza;
		this.setIcon(this.pieza.getImagenPieza());
	}
	
	public void setPieza(String color, String pieza) {
		
		switch(pieza) {
			case "Pawn":
				this.pieza = new Peon(color);
				break;
			case "Bishop":
				this.pieza = new Alfil(color);
				break;
			case "Rook":
				this.pieza = new Torre(color);
				break;
			case "Knight":
				this.pieza = new Caballo(color);
				break;
			case "Queen":
				this.pieza = new Reina(color);
				break;
			case "King":
				this.pieza = new Rey(color);
				break;
		}
		this.setIcon(this.pieza.getImagenPieza());
	}

	public int getColumna() {
		return columna;
	}

	public int getFila() {
		return fila;
	}
	
	public boolean isEmpty() {
		return pieza == null;
	}	
}
