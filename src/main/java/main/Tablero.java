package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tablero extends JPanel {
    private Casilla[][] casillas;
    private int filas;
    private int columnas;
    private boolean juegoIniciado;   //para controlar que no se revele una casilla después de perder
    private boolean primerClick;    //para evitar que la primera casilla clickeada tenga mina
    private int cantminas;

    public Tablero(int filas, int columnas, int cantMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas][columnas];
        this.juegoIniciado = false;   
        this.primerClick = false;
        this.cantminas = cantMinas;
        
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

                            // primer clic
                            if (!primerClick) {
                                colocarMinas(cantminas, fila, columna);
                                primerClick = true;     
                                juegoIniciado = true;   
                            }

                            if (juegoIniciado) {
                                descubrirCasilla(fila, columna);
                            }

                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            colocarQuitarBandera(fila, columna);
                        }
                    }
                });
            }
        }
    }

    // Se añade fila y columna para evitar mina en primer clic
    public void colocarMinas(int cantMinas, int filaIni, int colIni) {
        int cantCasillas = filas * columnas;
        if (cantMinas > cantCasillas) {
            throw new IllegalArgumentException("Hay más minas que casillas");
        }

        Random rand = new Random();
        int minasColocadas = 0;

        while (minasColocadas < cantMinas) {
            int fila = rand.nextInt(filas);
            int columna = rand.nextInt(columnas);

            // Evitar colocar una mina en la casilla del primer clic
            if (fila == filaIni && columna == colIni) {
                continue;
            }

            if (!casillas[fila][columna].getTieneMina()) {
                casillas[fila][columna].colocarMina();
                minasColocadas++;
            }
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

        if (c.getTieneMina()) {
            revelarTodasLasMinas();
            juegoIniciado = false;
            JOptionPane.showMessageDialog(this, "Mina encontrada. Juego terminado.", "Fin", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int minasAlrededor = contarMinasAlrededor(fila, columna);
        c.setMinasAlrededor(minasAlrededor);
        c.descubrir();

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
                c.colocarQuitarBandera();
            }
        }
    }

    public int contarMinasAlrededor(int fila, int columna) {
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
                    if(casillas[i][j].getBandera()){
                        casillas[i][j].alternarBandera();
                    }
                    casillas[i][j].descubrir();
                }
            }
        }
    }

      public Casilla getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }
    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public boolean getJuegoIniciado() {
        return juegoIniciado;
    }

    public boolean getPrimerClick() {
        return primerClick;
    }

    public int getCantminas() {
        return cantminas;
    }

    public void setCasillas(Casilla[][] casillas) {
        this.casillas = casillas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }   

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public void setJuegoIniciado(boolean juegoIniciado) {
        this.juegoIniciado = juegoIniciado;
    }

    public void setPrimerClick(boolean primerClick) {
        this.primerClick = primerClick;
    }

    public void setCantminas(int cantminas) {
        this.cantminas = cantminas;
    }
}