package test;

import main.Tablero;
import main.Casilla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableroTest {
    
    private Tablero tablero;
    
    @BeforeEach
    void setUp() {
        // Crear un tablero pequeño para testing
        tablero = new Tablero(5, 5);
    }
    
    @Test
    void testConstructor() {
        assertNotNull(tablero);
        assertEquals(5, tablero.getFilas());
        assertEquals(5, tablero.getColumnas());
    }
    
    @Test
    void testColocarMinas_CantidadValida() {
        // Arrange
        int cantidadMinas = 5;
        
        // Act
        tablero.colocarMinas(cantidadMinas);
        
        // Assert - Verificar que se colocaron exactamente 5 minas
        int minasContadas = contarMinasEnTablero();
        assertEquals(cantidadMinas, minasContadas);
    }
    
    @Test
    void testColocarMinas_CantidadExcesiva() {
        // Arrange
        int cantidadMinasExcesiva = 30; // Más que las 25 casillas disponibles
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tablero.colocarMinas(cantidadMinasExcesiva);
        });
    }
    
    @Test
    void testContarMinasAlrededor() {
        // Arrange - Configurar minas en posiciones específicas
        tablero.colocarMinas(3);
        
        // Colocar minas manualmente para test predecible
        tablero.getCasilla(0, 0).colocarMina(); // Mina en esquina
        tablero.getCasilla(2, 2).colocarMina(); // Mina en centro
        tablero.getCasilla(4, 4).colocarMina(); // Mina en otra esquina
        
        // Act & Assert
        assertEquals(1, contarMinasAlrededorPrivado(1, 1)); // Alrededor del centro
        assertEquals(1, contarMinasAlrededorPrivado(0, 1)); // Al lado de esquina
        assertEquals(0, contarMinasAlrededorPrivado(4, 0)); // Lejos de minas
    }
    
    @Test
    void testDescubrirCasilla_SinMina() {
        // Arrange
        int fila = 2, columna = 2;
        
        // Act
        tablero.descubrirCasilla(fila, columna);
        
        // Assert
        Casilla casilla = tablero.getCasilla(fila, columna);
        assertTrue(casilla.getDescubierta());
        assertFalse(casilla.getTieneMina());
    }
    
    @Test
    void testDescubrirCasilla_ConMina() {
        // Arrange
        int fila = 2, columna = 2;
        tablero.getCasilla(fila, columna).colocarMina();
        
        // Act
        tablero.descubrirCasilla(fila, columna);
        
        // Assert - La casilla con mina debe estar descubierta
        Casilla casilla = tablero.getCasilla(fila, columna);
        assertTrue(casilla.getDescubierta());
        assertTrue(casilla.getTieneMina());
    }
    
    @Test
    void testDescubrirCasilla_FueraDeLimites() {
        // Arrange
        int filaFuera = 10, columnaFuera = 10;
        
        // Act - No debería lanzar excepción
        tablero.descubrirCasilla(filaFuera, columnaFuera);
        
        // Assert - El test pasa si no hay excepción
        assertTrue(true);
    }
    
    @Test
    void testColocarQuitarBandera() {
        // Arrange
        int fila = 2, columna = 2;
        Casilla casilla = tablero.getCasilla(fila, columna);
        
        // Act - Colocar bandera
        tablero.colocarQuitarBandera(fila, columna);
        
        // Assert
        assertTrue(casilla.getBandera());
        
        // Act - Quitar bandera
        tablero.colocarQuitarBandera(fila, columna);
        
        // Assert
        assertFalse(casilla.getBandera());
    }
    
    @Test
    void testColocarQuitarBandera_CasillaDescubierta() {
        // Arrange
        int fila = 2, columna = 2;
        Casilla casilla = tablero.getCasilla(fila, columna);
        casilla.descubrir(); // Descubrir la casilla primero
        
        // Act
        tablero.colocarQuitarBandera(fila, columna);
        
        //No debería colocar bandera en casilla descubierta
        assertFalse(casilla.getBandera());
    }
    
    @Test
    void testExpansionCeros() {
        // Crear un área sin minas
        // No colocar minas alrededor del punto (2,2)
        
        tablero.descubrirCasilla(2, 2);
        
       
        int casillasDescubiertas = contarCasillasDescubiertas();
        assertTrue(casillasDescubiertas > 1);
    }
    
 
    private int contarMinasEnTablero() {
        int contador = 0;
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (tablero.getCasilla(i, j).getTieneMina()) {
                    contador++;
                }
            }
        }
        return contador;
    }
    
    private int contarCasillasDescubiertas() {
        int contador = 0;
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (tablero.getCasilla(i, j).getDescubierta()) {
                    contador++;
                }
            }
        }
        return contador;
    }
    
  
    private int contarMinasAlrededorPrivado(int fila, int columna) {
        try {
            java.lang.reflect.Method method = Tablero.class.getDeclaredMethod("contarMinasAlrededor", int.class, int.class);
            method.setAccessible(true);
            return (int) method.invoke(tablero, fila, columna);
        } catch (Exception e) {
            fail("Error al acceder al método privado: " + e.getMessage());
            return -1;
        }
    }
}