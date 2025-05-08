package com.example.vicente.trabalhodois;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class BarcaBugsTests {

    BarcaBugs barcaBugs;

    @BeforeEach
    public void setUp() {
        Relogio relogio = new MockRelogio(10, 30);
        barcaBugs = new BarcaBugs(relogio, 100.0); // precoBase = 100.0
    }


    /*
     * TESTE DE OCUPAÇÃO DE LUGAR
    */

    // Teste de ocupação de lugar VÁLIDO
    @Test
    public void testFormatoInvalido() {
        assertEquals(0, barcaBugs.ocupaLugar("X0101A01"));
    }

    // Teste de ocupação de lugar já OCUPADO
    @Test
    public void testAssentoJaOcupado() {
        assertEquals(3, barcaBugs.ocupaLugar("F010A01"));
        assertEquals(1, barcaBugs.ocupaLugar("F010A01"));
    }

    // Teste de ocupação de lugar FORA do limite [0, 100]
    @Test
    public void testPrimeiros100ForaDaRegra() {
        for (int i = 0; i < 50; i++) {
            barcaBugs.ocupaLugar(1 + (i / 20), i % 20); // Fileiras 1 a 20
        }
        assertEquals(1, barcaBugs.ocupaLugar("F021A01"));
    }

    // Teste de ocupação de lugar DENTRO do limite [0, 100]
    @Test
    public void testEntre101e200DentroDaRegra() {
        // Simular os primeiros 100 passageiros nas fileiras 1 a 20
        for (int i = 0; i < 100; i++) {
            barcaBugs.ocupaLugar(1 + (i / 20), i % 20);
        }
    
        // Simular mais 50 passageiros nas fileiras 40 a 60
        for (int i = 0; i < 50; i++) {
            barcaBugs.ocupaLugar(40 + (i / 20), i % 20);
        }
        assertEquals(3, barcaBugs.ocupaLugar("F050A01"));
    }

    // Teste de ocupação de lugar FORA do limite [101, 200]
    @Test
    public void testEntre101e200ForaDaRegra() {
        for (int i = 0; i < 100; i++) {
            barcaBugs.ocupaLugar(1 + (i / 20), i % 20);
        }
    
        // Simular mais 50 passageiros nas fileiras 40 a 60
        for (int i = 0; i < 50; i++) {
            barcaBugs.ocupaLugar(40 + (i / 20), i % 20);
        }
        assertEquals(2, barcaBugs.ocupaLugar("F030A01"));
    }

    // Teste de ocupação de lugar FORA do limite [201, x : x pertence Reais]
    @Test
    public void testAcimaDe200PodeQualquerLugar() {
        // Simular os primeiros 100 passageiros nas fileiras 1 a 20
        for (int i = 0; i < 100; i++) {
            barcaBugs.ocupaLugar(1 + (i / 20), i % 20);
        }
    
        // Simular os próximos 100 passageiros nas fileiras 40 a 60
        for (int i = 0; i < 100; i++) {
            barcaBugs.ocupaLugar(40 + (i / 20), i % 20);
        }
        assertEquals(3, barcaBugs.ocupaLugar("F030A01"));
    }


    /*
     * TESTE DE CALCULO DE PASSAGEM
    */

    // Teste para horário COMERCIAL (8h as 12h - 14h as 18h)
    @Test
    public void testPrecoHorarioComercial() {
        BarcaBugs barcaHorarioComercial = new BarcaBugs(new MockRelogio(10, 30), 100.0);
        assertEquals(100.0, barcaHorarioComercial.calculaPrecoPassagem(), 0.00000000000001);
    }

    // Teste para horário ALMOÇO (12h01 as 13h59)
    @Test
    public void testPrecoHorarioAlmoco() {
        BarcaBugs barcaHorarioAlmoco = new BarcaBugs(new MockRelogio(12, 30), 100.0);
        assertEquals(110.0, barcaHorarioAlmoco.calculaPrecoPassagem(), 0.00000000000001);
    }

    // Teste para horário NOITE (18h01 as 19h59)
    @Test
    public void testPrecoHorarioFimDeTarde() {
        BarcaBugs barcaHorarioFimDeTarde = new BarcaBugs(new MockRelogio(18, 30), 100.0);
        assertEquals(110.0, barcaHorarioFimDeTarde.calculaPrecoPassagem(), 0.00000000000001);
    }

    // Teste para horário FIM DE NOITE (20h as 23h59)
    @Test
    public void testPrecoHorarioNoturno() {
        BarcaBugs barcaHorarioNoturno = new BarcaBugs(new MockRelogio(21, 0), 100.0);
        assertEquals(120.0, barcaHorarioNoturno.calculaPrecoPassagem(), 0.00000000000001);
    }

    // Teste para horário MADRUGADA (00h as 7h59)
    @Test
    public void testPrecoHorarioMadrugada() {
        BarcaBugs barcaHorarioMadrugada = new BarcaBugs(new MockRelogio(2, 0), 100.0);
        assertEquals(150.0, barcaHorarioMadrugada.calculaPrecoPassagem(), 0.00000000000001);
    }


}
