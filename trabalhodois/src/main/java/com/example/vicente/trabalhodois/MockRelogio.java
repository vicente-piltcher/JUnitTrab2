package com.example.vicente.trabalhodois;

public class MockRelogio implements Relogio {

    private int hora;
    private int minuto;

    public MockRelogio(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    @Override
    public int getHora() {
        return hora;
    }

    @Override
    public int getMinuto() {
        return minuto;
    }
}

