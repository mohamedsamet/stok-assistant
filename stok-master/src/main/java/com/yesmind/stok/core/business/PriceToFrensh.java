package com.yesmind.stok.core.business;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceToFrensh {

    private PriceToFrensh() {}

    private static final String[] UNITS = {
            "", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
            "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize",
            "dix-sept", "dix-huit", "dix-neuf"
    };

    private static final String[] TENS = {
            "", "", "vingt", "trente", "quarante", "cinquante",
            "soixante", "soixante", "quatre-vingt", "quatre-vingt"
    };

    /**
     * Convertit un prix float en texte en français, avec dinars et millimes.
     * Exemple : 989656.345f -> "neuf cent quatre-vingt-neuf mille six cent cinquante-six dinars et trois cent quarante-cinq millimes"
     */
    public static String priceToFrenchText(BigDecimal price) {
        // Conversion précise avec BigDecimal pour éviter les erreurs binaires
        BigDecimal bdPrice = price.setScale(3, RoundingMode.HALF_UP);

        // Séparer partie entière et fractionnaire
        BigDecimal[] parts = bdPrice.divideAndRemainder(BigDecimal.ONE);
        long dinars = parts[0].longValueExact();
        int millimes = parts[1].multiply(BigDecimal.valueOf(1000)).intValueExact();

        String dinarText = convertNumberToFrench(dinars) + " dinar" + (dinars > 1 ? "s" : "");
        String millimeText = millimes > 0
                ? " et " + convertNumberToFrench(millimes) + " millime" + (millimes > 1 ? "s" : "")
                : "";
        return dinarText + millimeText;
    }

    private static String convertNumberToFrench(long number) {
        if (number == 0) return "zéro";
        if (number < 0) return "moins " + convertNumberToFrench(-number);

        StringBuilder result = new StringBuilder();

        if (number >= 1_000_000_000) {
            long milliards = number / 1_000_000_000;
            result.append(convertNumberToFrench(milliards)).append(" milliard").append(milliards > 1 ? "s " : " ");
            number %= 1_000_000_000;
        }

        if (number >= 1_000_000) {
            long millions = number / 1_000_000;
            result.append(convertNumberToFrench(millions)).append(" million").append(millions > 1 ? "s " : " ");
            number %= 1_000_000;
        }

        if (number >= 1000) {
            long milliers = number / 1000;
            if (milliers == 1) result.append("mille ");
            else result.append(convertNumberToFrench(milliers)).append(" mille ");
            number %= 1000;
        }

        if (number >= 100) {
            long centaines = number / 100;
            if (centaines == 1) result.append("cent");
            else result.append(UNITS[(int) centaines]).append(" cent");
            if (number % 100 == 0 && centaines > 1) result.append("s");
            result.append(" ");
            number %= 100;
        }

        if (number > 0) {
            if (number < 20) {
                result.append(UNITS[(int) number]);
            } else {
                int tens = (int) (number / 10);
                int units = (int) (number % 10);

                if (tens == 7 || tens == 9) {
                    result.append(TENS[tens]).append("-").append(UNITS[10 + units]);
                } else {
                    result.append(TENS[tens]);
                    if (units == 1 && tens >= 2 && tens <= 6) {
                        result.append("-et-un");
                    } else if (units > 0) {
                        result.append("-").append(UNITS[units]);
                    }
                }
            }
        }

        return result.toString().trim();
    }
}
