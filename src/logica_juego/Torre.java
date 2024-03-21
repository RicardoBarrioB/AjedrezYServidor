package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Torre extends PiezaAjedrez {
	
	public Torre(String color){
		super(color, "Rook");
	}
 	
	public List<int[]> movimientos(MiBoton boton, MiBoton[][] tablero) {
		
		List<int[]> movimientos = new ArrayList<>();
		
		for(int i = -1; i > -8 && !encuentraPieza; i--) {
			if(comprobarMovimiento(boton.getFila(), boton.getColumna()+i)) {
				if(!(tablero[boton.getFila()][boton.getColumna()+i].isEmpty())) {
					if(!(fichaMismoColor(tablero[boton.getFila()][boton.getColumna()+i]))){
						movimientos.add(new int []{boton.getFila(), boton.getColumna()+i});
					}
					encuentraPieza = true;
				}else {
					movimientos.add(new int []{boton.getFila(), boton.getColumna()+i});
				}
			}
		}
		encuentraPieza = false;
		
		for(int i = 1; i < 8 && !encuentraPieza; i++) {
			if(comprobarMovimiento(boton.getFila(), boton.getColumna()+i)) {
				if(!(tablero[boton.getFila()][boton.getColumna()+i].isEmpty())) {
					if(!(fichaMismoColor(tablero[boton.getFila()][boton.getColumna()+i]))){
						movimientos.add(new int []{boton.getFila(), boton.getColumna()+i});
					}
					encuentraPieza = true;
				}else {
					movimientos.add(new int []{boton.getFila(), boton.getColumna()+i});
				}
			}
		}
		encuentraPieza = false;
		
		for(int i = -1; i > -8 && !encuentraPieza; i--) {
			if(comprobarMovimiento(boton.getFila()+i, boton.getColumna())) {
				if(!(tablero[boton.getFila()+i][boton.getColumna()].isEmpty())) {
					if(!(fichaMismoColor(tablero[boton.getFila()+i][boton.getColumna()]))){
						movimientos.add(new int []{boton.getFila()+i, boton.getColumna()});
					}
					encuentraPieza = true;
				}else {
					movimientos.add(new int []{boton.getFila()+i, boton.getColumna()});
				}
			}
		}
		encuentraPieza = false;
		
		for(int i = 1; i < 8 && !encuentraPieza; i++) {
			if(comprobarMovimiento(boton.getFila()+i, boton.getColumna())) {
				if(!(tablero[boton.getFila()+i][boton.getColumna()].isEmpty())) {
					if(!(fichaMismoColor(tablero[boton.getFila()+i][boton.getColumna()]))){
						movimientos.add(new int []{boton.getFila()+i, boton.getColumna()});
					}
					encuentraPieza = true;
				}else {
					movimientos.add(new int []{boton.getFila()+i, boton.getColumna()});
				}
			}
		}
		encuentraPieza = false;
		primerMov = false;
		
		return movimientos;
	}
}











