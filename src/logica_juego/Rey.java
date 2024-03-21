package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Rey extends PiezaAjedrez{
	
	private boolean jaque;
	private boolean enJaque;
	
	public Rey(String color){	
		super(color, "King");
	}
	
	public boolean getJaque() {
		return jaque;
	}
	
	public boolean getEnJaque() {
		return enJaque;
	}
	
	public void setEnJaque(boolean enJaque) {
		this.enJaque = enJaque;
		jaque = true;
	}
 	
	public List<int[]> movimientos(MiBoton boton, MiBoton[][] tablero) {
		
		List<int[]> movimientos = new ArrayList<>();
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(i == 0 && j == 0) {
					j++;
				}
				if(comprobarMovimiento(boton.getFila() + i, boton.getColumna() + j)) {
					if((tablero[boton.getFila() + i][boton.getColumna() + j].isEmpty())) {
						movimientos.add(new int []{boton.getFila() + i, boton.getColumna() + j});
					}else {
						if(!(fichaMismoColor(tablero[boton.getFila() + i][boton.getColumna() + j]))) {
							movimientos.add(new int []{boton.getFila() + i, boton.getColumna() + j});
						}
					}
				}
			}
		}
		
		if(!jaque && primerMov) {
			if(this.getColor().equals("WHITE")) {
				if((tablero[boton.getFila()][boton.getColumna()+1].getPieza() == null) && (tablero[boton.getFila()][boton.getColumna()+2].getPieza() == null)) {
					movimientos.add(new int []{boton.getFila(), boton.getColumna()+2});
				}
				if((tablero[boton.getFila()][boton.getColumna()-1].getPieza() == null) && (tablero[boton.getFila()][boton.getColumna()-2].getPieza() == null) && 
					(tablero[boton.getFila()][boton.getColumna()-3].getPieza() == null)) {
					movimientos.add(new int []{boton.getFila(), boton.getColumna()-2});
				}
			}else {
				if((tablero[boton.getFila()][boton.getColumna()-1].getPieza() == null) && (tablero[boton.getFila()][boton.getColumna()-2].getPieza() == null)) {
					movimientos.add(new int []{boton.getFila(), boton.getColumna()-2});
				}
				if((tablero[boton.getFila()][boton.getColumna()+1].getPieza() == null) && (tablero[boton.getFila()][boton.getColumna()+2].getPieza() == null) && 
					(tablero[boton.getFila()][boton.getColumna()+3].getPieza() == null)) {
					movimientos.add(new int []{boton.getFila(), boton.getColumna()+2});
				}
			}
			
		}
		primerMov = false;
		
		return movimientos;
	}
}















