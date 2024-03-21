package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Alfil extends PiezaAjedrez {
	
	public Alfil(String color){
		super(color, "Bish");
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

        return movimientos;
	}
}















