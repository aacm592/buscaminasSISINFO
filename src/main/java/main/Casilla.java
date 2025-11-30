package main;

import javax.swing.JButton;

public class Casilla extends JButton {
    private boolean tieneMina;
    private boolean descubierta;
    private boolean bandera;
    private int minasAlrededor;

    public Casilla() {
        this.tieneMina = false;
        this.descubierta = false;
        this.minasAlrededor = 0;
        this.bandera = false;
        setText("");
    }

    public boolean getTieneMina() {
        return tieneMina;
    }

    public void colocarMina() {
        this.tieneMina = true;
    }

    public boolean getDescubierta() {
        return descubierta;
    }

    /** Descubre la casilla si es válido hacerlo */
    public void descubrir() {
        if (!puedeDescubrir()) return;

        this.descubierta = true;
        actualizarHabilitado();
        actualizarTextoAlDescubrir();
    }

    /** Extract Method: determina si una casilla puede ser descubierta */
    private boolean puedeDescubrir() {
        return !descubierta && !bandera;
    }
    /** Extract Method: actualiza si el botón debe estar habilitado */
    private void actualizarHabilitado() {
        setEnabled(!descubierta && !bandera);
    }
    /** Replace Temp With Query: eliminamos un "if (minasAlrededor > 0)" duplicado */
    private boolean tieneMinasAlrededor() {
        return minasAlrededor > 0;
    }
    /** Extract Method + Replace Temp With Query */
    private void actualizarTextoAlDescubrir() {
        if (getTieneMina()) {
            setText("💣");
        } else if (tieneMinasAlrededor()) {
            setText(String.valueOf(minasAlrededor));
        } else {
            setText("");
        }
    }

    public int getMinasAlrededor() {
        return minasAlrededor;
    }

    public void setMinasAlrededor(int minasAlrededor) {
        this.minasAlrededor = minasAlrededor;
    }

    public boolean getBandera() {
        return bandera;
    }

    public void alternarBandera() {
        if(!descubierta) {
            this.bandera = !this.bandera;
            setText(bandera ? "🚩" : "");
        }
    }
    
    public void colocarQuitarBandera() {
        if(descubierta) return;
        bandera = !bandera;
        setText(bandera ? "🚩" : "");
    }
    
}