package com.example.vicente.trabalhodois;

import java.util.regex.Pattern;

public class BarcaBugs {
    public static final int ASSENTOS_POR_FILA = 20;
    public static final int FILAS = 60;
    private boolean[][] assentos;
    public int qtdadeAssentosOcupados;
    private double precoBase;
    private Relogio relogio;

    public BarcaBugs(Relogio relogio, double precoBase){
        assentos = new boolean[FILAS][]; 
        for(int i=0;i<FILAS;i++){
            assentos[i] = new boolean[ASSENTOS_POR_FILA];
            for(int j=0;j<ASSENTOS_POR_FILA;j++){
                assentos[i][j] = false;
            }
        }
        qtdadeAssentosOcupados = 0;
        this.precoBase = precoBase;
        this.relogio = relogio;
    }

    // Método auxiliar projetado para facilitar testes
    protected void ocupaLugar(int fila,int assento){
        assentos[fila][assento] = true;
        qtdadeAssentosOcupados++;
    }

    /*
     * 0 – Identificador de assento inválido
     * 1 – Assento ocupado
     * 2 – Assento bloqueado devido a distribuição de peso
     * 3 – Ok, assento atribuído ao passageiro.
     */
    public int ocupaLugar(String assentoInformado){
        // Verifica se é um assento valido
        if (!Pattern.matches("F\\d{2}A\\d{2}", assentoInformado)) {
            return 0;
        }
        int fila = Integer.parseInt(assentoInformado.substring(1,3));
        int assento = Integer.parseInt(assentoInformado.substring(4,6));
        if (fila<0 || fila > 60){
            return 0;
        }
        if (assento < 0 || assento >= 20){
            return 0;
        }
        // Verifica se o assento já não está ocupado
        if (assentos[fila][assento] == true){
            return 1;
        }
        // Se tem até 100 passageiros, verifica se fila <= 20
        if (qtdadeAssentosOcupados <= 100 && fila > 20){
            return 1;
        }
        // Se tem mais de 100 e até de 200 passageiros, verifica se fila >= 40
        if (qtdadeAssentosOcupados > 100 && qtdadeAssentosOcupados <= 200 && fila < 40){
            return 2;
        }
        // Ocupa o assento
        assentos[fila][assento] = true;
        qtdadeAssentosOcupados++;
        return 3;
    }

    public double calculaPrecoPassagem() {
        int hora = relogio.getHora();
        int minuto = relogio.getMinuto();
    
        // Converte hora e minuto para minutos totais do dia
        int minutosTotais = hora * 60 + minuto;
    
        // Define os intervalos de preços e seus multiplicadores
        int[][] intervalos = {
            {8 * 60, 12 * 60},       // 8:00 - 12:00 (Manhã)
            {14 * 60, 18 * 60},      // 14:00 - 18:00 (Tarde)
            {12 * 60 + 1, 13 * 60 + 59}, // 12:01 - 13:59 (Almoço)
            {18 * 60 + 1, 19 * 60 + 59}, // 18:01 - 19:59 (Noite)
            {20 * 60, 23 * 60 + 59}, // 20:00 - 23:59 (Late Night)
            {0, 8 * 60 - 1}          // 00:00 - 7:59 (Madrugada)
        };

        double[] multiplicadores = {
            1.0,  // Preço base (Manhã e Tarde)
            1.0,  // Preço base (Manhã e Tarde)
            1.1,  // 10% maior (Almoço)
            1.1,  // 10% maior (Noite)
            1.2,  // 20% maior (Late Night)
            1.5   // 50% maior (Madrugada)
        };
    
        // Verifica em qual intervalo o horário atual se encaixa
        for (int i = 0; i < intervalos.length; i++) {
            if (minutosTotais >= intervalos[i][0] && minutosTotais <= intervalos[i][1]) {
                return precoBase * multiplicadores[i];
            }
        }

        return precoBase;
    }
}