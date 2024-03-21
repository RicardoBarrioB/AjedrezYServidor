package logica_juego;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.swing.ImageIcon;


abstract class PiezaAjedrez implements Serializable{
	
 //   protected static final long serialVersionUID = 1L;

	protected String color;
	protected String pieza;
	protected ImageIcon imagenPieza;
	protected boolean encuentraPieza = false;
	protected boolean primerMov;
	//protected List<int[]> movimientos = new ArrayList<>();
	
	public PiezaAjedrez(String color, String pieza){
		this.color = color;
		this.pieza = pieza;
		this.primerMov = true;
		this.imagenPieza = setImagenPieza(color);
	}
	
	public final ImageIcon setImagenPieza(String color) {
		return new ImageIcon("recursos/" + (color.equalsIgnoreCase("WHITE") ? "W":"B") + pieza + ".png");
	}
	
	public final String getColor() {
		return color;
	}

	public final String getPieza() {
		return pieza;
	}

	public final ImageIcon getImagenPieza() {
		return imagenPieza;
	}
	
	public List<int []> getMovimientos(MiBoton boton, MiBoton[][] tablero){
		return movimientos(boton, tablero);
	}
	
	public boolean getPrimerMov() {
		return primerMov;
	}
	
	public void setPrimerMov(boolean primerMov) {
		this.primerMov = primerMov;
	}
	
	public abstract List<int[]> movimientos(MiBoton boton, MiBoton[][] tablero);
	
	public final boolean comprobarMovimiento(int fila, int columna) {
		return fila < 8 && fila >= 0 && columna < 8 && columna >=0;
	}
	
	public void comprobarFicha(MiBoton boton){
		if(!(color.equals(boton.getPieza().getColor()))) {
			if(boton.getPieza().getPieza().equals("King")) {
				boton.setBackground(Color.red); 
			}else {
				boton.setBackground(Color.yellow);
			}
			boton.setEnabled(true);
		}
		encuentraPieza = true;
	}
	
	public final boolean fichaMismoColor(MiBoton tablero){
		return (color.equals(tablero.getPieza().getColor()));
	}
	
	public boolean equals(Object o) {
		if(o == null) return false;
		if(this == o) return true; 
		if(this.getClass() != o.getClass()) return false;
		PiezaAjedrez otra = (PiezaAjedrez) o;
		return this.pieza == otra.pieza && this.color == otra.color;
	}
}

