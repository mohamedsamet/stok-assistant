package com.yesmind.stok.unit.price;

import com.yesmind.stok.core.business.PriceToFrensh;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class PriceToFrenshTest {

    @Test
    void shouldGenerateFrensh() {
        String text = PriceToFrensh.priceToFrenchText(BigDecimal.valueOf(23.546d));
        Assertions.assertEquals(text, "vingt-trois dinars et cinq cent quarante-six millimes");

        String text2 = PriceToFrensh.priceToFrenchText(BigDecimal.valueOf(997423.119d));
        Assertions.assertEquals(text2, "neuf cent quatre-vingt-dix-sept mille quatre cent vingt-trois dinars et cent dix-neuf millimes");

        String text3 = PriceToFrensh.priceToFrenchText(BigDecimal.valueOf(0d));
        Assertions.assertEquals(text3, "zéro dinar");

        String text4 = PriceToFrensh.priceToFrenchText(BigDecimal.valueOf(20456877.897d));
        Assertions.assertEquals(text4, "vingt millions quatre cent cinquante-six mille huit cent soixante-dix-sept dinars et huit cent quatre-vingt-dix-sept millimes");
    }
}
