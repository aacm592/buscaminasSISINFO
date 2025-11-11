package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tablero extends JPanel {
    private Casilla[][] casillas;
    private int filas;
    private int columnas;
    private boolean juegoIniciado; // Para evitar colocar minas en el primer click

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas][columnas];
        this.juegoIniciado = false;
        setLayout(new GridLayout(filas, columnas));
        inicializarCasillas();
        agregarListeners();
    }

    private void inicializarCasillas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casillas[i][j] = new Casilla();
                add(casillas[i][j]);
            }
        }
    }

    private void agregarListeners() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                final int fila = i;
                final int columna = j;
                casillas[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            // Colocar minas después del primer click (para no perder inmediatamente)
                            if (!juegoIniciado) {
                                colocarMinas(10); // Cambia 10 por la cantidad deseada de minas
                                juegoIniciado = true;
                            }
                            descubrirCasilla(fila, columna);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            colocarQuitarBandera(fila, columna);
                        }
                    }
                });
            }
        }
    }

    public void colocarMinas(int cantMinas) {
        System.out.println("Colocando " + cantMinas + " minas"); // Para debug
        int cantCasillas = filas * columnas;
        if (cantMinas > cantCasillas) {
            throw new IllegalArgumentException("La cantidad de minas excede la cantidad de casillas");
        } else {
            Random rand = new Random();
            int minasColocadas = 0;

            while (minasColocadas < cantMinas) {
                int fila = rand.nextInt(filas);
                int columna = rand.nextInt(columnas);

                if (!casillas[fila][columna].getTieneMina()) {
                    casillas[fila][columna].colocarMina();
                    minasColocadas++;
                    System.out.println("Mina colocada en: " + fila + ", " + columna); // Para debug
                }
            }
            System.out.println("Minas colocadas: " + minasColocadas); // Para debug
        }
    }

    public void descubrirCasilla(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return;
        }

        Casilla c = casillas[fila][columna];
        if (c.getDescubierta() || c.getBandera()) {
            return;
        }

        int minasAlrededor = contarMinasAlrededor(fila, columna);
        c.setMinasAlrededor(minasAlrededor);
        c.descubrir();

        if (c.getTieneMina()) {
            revelarTodasLasMinas();
            System.out.println("Fin del juego - Mina encontrada en: " + fila + ", " + columna);
            JOptionPane.showMessageDialog(this, "Mina encontrada - Fin del juego");
            return;
        }

        if (minasAlrededor == 0) {
            for (int i = fila - 1; i <= fila + 1; i++) {
                for (int j = columna - 1; j <= columna + 1; j++) {
                    if (i == fila && j == columna) continue;
                    descubrirCasilla(i, j);
                }
            }
        }
    }

    public void colocarQuitarBandera(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            Casilla c = casillas[fila][columna];
            if (!c.getDescubierta()) {
                // CORRECCIÓN: Solo llamar una vez al método de alternar bandera
                c.colocarQuitarBandera();
            }
        }
    }

    private int contarMinasAlrededor(int fila, int columna) {
        int contador = 0;
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                if (i >= 0 && j >= 0 && i < filas && j < columnas) {
                    if (casillas[i][j].getTieneMina()) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    private void revelarTodasLasMinas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (casillas[i][j].getTieneMina()) {
                    casillas[i][j].descubrir();
                }
            }
        }
    }

    // Método para debug: verificar minas colocadas
    public void verificarMinas() {
        int contador = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (casillas[i][j].getTieneMina()) {
                    contador++;
                    System.out.println("Mina en x: " + i + " y: " + j);
                }
            }
        }
        System.out.println("Total de minas encontradas: " + contador);
    }

    // Getters y setters...
    public Casilla getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}