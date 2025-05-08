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

    // Teste de ocupação de lugar INVÁLIDO
    @Test
    public void testFormatoInvalido() {
        assertEquals(0, barcaBugs.ocupaLugar("X0101A01"));
    }

    // Teste de ocupação de lugar já OCUPADO
    @Test
    public void testAssentoJaOcupado() {
        assertEquals(3, barcaBugs.ocupaLugar("F10A01"));
        assertEquals(1, barcaBugs.ocupaLugar("F10A01"));
    }

    // Teste de ocupação de lugar FORA do limite [0, 100]
    @Test
    public void testPrimeiros100ForaDaRegra() {
        for (int i = 0; i < 50; i++) {
            barcaBugs.ocupaLugar(0, i % 20); // Ocupa 50 lugares genéricos
        }
        assertEquals(1, barcaBugs.ocupaLugar("F21A01"));
    }

    // Teste de ocupação de lugar DENTRO do limite [101, 200]
    @Test
    public void testEntre101e200DentroDaRegra() {
        for (int i = 0; i < 150; i++) {
            barcaBugs.ocupaLugar(0, i % 20); // Ocupa 150 lugares genéricos
        }
        assertEquals(3, barcaBugs.ocupaLugar("F50A01"));
    }

    // Teste de ocupação de lugar FORA do limite [101, 200]
    @Test
    public void testEntre101e200ForaDaRegra() {
        for (int i = 0; i < 150; i++) {
            barcaBugs.ocupaLugar(0, i % 20); // Ocupa 150 lugares genéricos
        }
        assertEquals(2, barcaBugs.ocupaLugar("F30A01"));
    }


    // Teste de ocupação de lugar do limite [201, x : x pertence Reais]
    @Test
    public void testAcimaDe200PodeQualquerLugar() {
        for (int i = 0; i < 201; i++) {
            barcaBugs.ocupaLugar(0, i % 20); // Ocupa 201 lugares genéricos
        }
        assertEquals(3, barcaBugs.ocupaLugar("F30A01"));

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
        BarcaBugs barcaBugs = new BarcaBugs(new MockRelogio(12, 30), 100.0);
        assertEquals(110.0, barcaBugs.calculaPrecoPassagem(), 0.001);
    }

    // Teste para horário NOITE (18h01 as 19h59)
    @Test
    public void testPrecoHorarioFimDeTarde() {
        BarcaBugs barcaBugs = new BarcaBugs(new MockRelogio(18, 30), 100.0);
        assertEquals(110.0, barcaBugs.calculaPrecoPassagem(), 0.001);

    }

    // Teste para horário FIM DE NOITE (20h as 23h59)
    @Test
    public void testPrecoHorarioNoturno() {
        BarcaBugs barcaBugs = new BarcaBugs(new MockRelogio(21, 0), 100.0);
        assertEquals(120.0, barcaBugs.calculaPrecoPassagem(), 0.00000000000001);
    }

    // Teste para horário MADRUGADA (00h as 7h59)
    @Test
    public void testPrecoHorarioMadrugada() {
        BarcaBugs barcaBugs = new BarcaBugs(new MockRelogio(2, 0), 100.0);
        assertEquals(150.0, barcaBugs.calculaPrecoPassagem(), 0.00000000000001);
    }


}
