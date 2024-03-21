package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Peon extends PiezaAjedrez{
	
	public Peon(String color){
		super(color, "Pawn");
	}
 	
	public List<int[]> movimientos(MiBoton boton, MiBoton[][] tablero) {
		
		List<int[]> movimientos = new ArrayList<>();
		
		int mover = -1;
		
		for(int i = -1; i < 2; i++) {
			if(comprobarMovimiento(boton.getFila() + mover, boton.getColumna()+i)) {
				if(i == 0 && (tablero[boton.getFila() + mover][boton.getColumna()+i].isEmpty())) {
					movimientos.add(new int []{boton.getFila() + mover, boton.getColumna()+i});
				}
				if((!(tablero[boton.getFila() + mover][boton.getColumna()+i].isEmpty()) && i != 0)){	
					if(!(fichaMismoColor(tablero[boton.getFila() + mover][boton.getColumna()+i]))){
						movimientos.add(new int []{boton.getFila() + mover, boton.getColumna()+i});
					}
				}
			}
		}
		if(primerMov && (tablero[boton.getFila() + 2*mover][boton.getColumna()].isEmpty())){
			movimientos.add(new int []{boton.getFila() + 2*mover, boton.getColumna()});
			primerMov = false;
		}
		
		return movimientos;
	}
}















