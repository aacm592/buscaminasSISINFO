package main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableroTest {

    @Test
    public void testContarMinasAlrededor() {
        // Crear tablero 3x3 con 0 minas inicialmente
        Tablero tablero = new Tablero(3, 3, 0);

        // Colocamos una mina manualmente en el centro
        tablero.colocarMinas(1, 1, 0); // coloca 1 mina evitando (1,0)

        // Debemos encontrar la mina que se colocó
        int minasAlrededor00 = tablero.getCasilla(0,0).getTieneMina() ? 0 : tablero.contarMinasAlrededor(0,0);
        assertEquals(1, minasAlrededor00, "Minas alrededor de (0,0) deben ser 0");

        int minasAlrededor11 = tablero.getCasilla(1,1).getTieneMina() ? 0 : tablero.contarMinasAlrededor(1,1);
        assertEquals(0, minasAlrededor11, "Casilla con mina debe reportar 0 minas alrededor");
    }

    @Test
    public void testSinMinas() {
        Tablero tablero = new Tablero(2, 2, 0);
        assertEquals(0, tablero.contarMinasAlrededor(0,0));
        assertEquals(0, tablero.contarMinasAlrededor(1,1));
    }
}
