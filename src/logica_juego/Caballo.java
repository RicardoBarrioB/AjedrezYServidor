package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Caballo extends PiezaAjedrez{
	
	public Caballo(String color){
		super(color, "Knight");
	}
 	
	public List<int[]> movimientos(MiBoton boton, MiBoton[][] tablero) {
		List<int[]> movimientos = new ArrayList<>();
		for(int i = -1; i < 2; i+=2) {
			for(int j = -2; j < 3; j+=4) {
				if(comprobarMovimiento(boton.getFila()+j, boton.getColumna()+i)) {
					if(!(tablero[boton.getFila()+j][boton.getColumna()+i].isEmpty())){
						if(!(fichaMismoColor(tablero[boton.getFila()+j][boton.getColumna()+i]))){
							movimientos.add(new int []{boton.getFila()+j, boton.getColumna()+i});
						}
					}else {
						movimientos.add(new int []{boton.getFila()+j, boton.getColumna()+i});
					}
				}
			}
		}
		
		for(int i = -1; i < 2; i+=2) {
			for(int j = -2; j < 3; j+=4) {
				if(comprobarMovimiento(boton.getFila()+i, boton.getColumna()+j)) {
					if(!(tablero[boton.getFila()+i][boton.getColumna()+j].isEmpty())){
						if(!(fichaMismoColor(tablero[boton.getFila()+i][boton.getColumna()+j]))){
							movimientos.add(new int []{boton.getFila()+i, boton.getColumna()+j});
						}
					}else {
						movimientos.add(new int []{boton.getFila()+i, boton.getColumna()+j});
					}
				}
			}
		}
		primerMov = false;
		
		return movimientos;
	}
}















