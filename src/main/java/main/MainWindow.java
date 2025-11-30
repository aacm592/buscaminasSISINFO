package main;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class MainWindow extends JFrame {

    private Tablero tablero;

    public MainWindow() {
        setTitle("Buscaminas");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //Crear tablero
        tablero = new Tablero(8, 8, 10);
        tablero.setPreferredSize(new Dimension(400, 400));
        add(tablero, "Center"); // Tablero de 8x8 para novato        
        
        // Crear la barra de herramientas
        JToolBar toolBar = new JToolBar();
        JButton botonReiniciar = new JButton("Reiniciar");
        JButton botonRecords = new JButton("Ver Records");
        JButton botonDificultad = new JButton("Dificultad");

        toolBar.add(botonReiniciar);
        toolBar.add(botonRecords);
        toolBar.addSeparator();
        toolBar.add(botonDificultad);               

        add(toolBar, "North"); //para mostrar la barra de herramientas

        // Mostrar el menú de dificultad al hacer clic en el botón
        JPopupMenu menuDificultad = new JPopupMenu();
        JMenuItem novato = new JMenuItem("Novato");
        JMenuItem medio = new JMenuItem("Medio");
        JMenuItem experto = new JMenuItem("Experto");

        menuDificultad.add(novato);
        menuDificultad.add(medio); 
        menuDificultad.add(experto);

        novato.addActionListener(e -> {
            System.out.println("Dificultad novato");
            cambiarTablero(8, 8, 10);
        });
        medio.addActionListener(e -> {
            System.out.println("Dificultad medio");
            cambiarTablero(16, 16, 40);
        });
        experto.addActionListener(e -> {
            System.out.println("Dificultad experto");
            cambiarTablero(16, 30, 99);
        });

        botonDificultad.addActionListener(e -> {
            menuDificultad.show(botonDificultad, 0, botonDificultad.getHeight());
        });
        
        botonReiniciar.addActionListener(e -> {
            cambiarTablero(tablero.getFilas(), tablero.getColumnas(), tablero.getCantminas());
        });

        setVisible(true);
    }

    private void cambiarTablero(int filas, int columnas, int minas) {
        remove(tablero);
        tablero = new Tablero(filas, columnas, minas);
        tablero.setPreferredSize(new Dimension(400, 400));
    
        //tablero.setJuegoIniciado(false);
        
        add(tablero, "Center");
        revalidate();
        repaint();
    }
}