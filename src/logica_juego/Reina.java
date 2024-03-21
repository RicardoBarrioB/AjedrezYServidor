package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Reina extends PiezaAjedrez{
	
	public Reina(String color){
		super(color, "Queen");
	}
 	
	public List<int[]> movimientos(MiBoton boton, MiBoton[][] tablero) {
		
		List<int[]> movimientos = new ArrayList<>();
		
		int[] filas = { 1, 1, -1, -1 };
        int[] columnas = { 1, -1, 1, -1 };

        for (int i = 0; i < filas.length; i++) {
            int fila = boton.getFila() + filas[i];
            int columna = boton.getColumna() + columnas[i];

            while (comprobarMovimiento(fila, columna) && tablero[fila][columna].isEmpty()) {
                movimientos.add(new int[]{fila, columna});
                fila += filas[i];
                columna += columnas[i];
            }

            if (comprobarMovimiento(fila, columna) && !fichaMismoColor(tablero[fila][columna])) {
                movimientos.add(new int[]{fila, columna});
            }
        }

        primerMov = false;
		
		encuentraPieza = false;
		
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















