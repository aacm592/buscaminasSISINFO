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

    public void descubrir() {
        if(descubierta || bandera) return;

        this.descubierta = true;
        setEnabled(false);

        if(tieneMina) {
            setText("💣");
        } else if (minasAlrededor > 0) {
            setText(String.valueOf(minasAlrededor));
        } else {
            setText("");
        }
        // Se eliminó el setEnabled(false) duplicado
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