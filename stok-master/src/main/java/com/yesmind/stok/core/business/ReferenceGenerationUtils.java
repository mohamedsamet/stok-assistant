package com.yesmind.stok.core.business;

import com.yesmind.stok.core.domain.data.ReferenceTools;

import java.time.LocalDateTime;

public final class ReferenceGenerationUtils {

    private ReferenceGenerationUtils() {}

    public static String generateReference(String reference, ReferenceTools referenceTools) {
        int monthsRef = 12*(LocalDateTime.now().getYear() - referenceTools.getIncrementFirstYear()) + (LocalDateTime.now().getMonthValue());
        String[] parts = reference.split(referenceTools.getSuffix());
        long incrementer = Long.parseLong(parts[parts.length - 1]) + 1;
        return referenceTools.getPrefix() + String.format("%03d", monthsRef) + referenceTools.getSuffix() + incrementer;
    }

    public static String generateFirstReference(ReferenceTools referenceTools) {
        int monthsRef = 12*(LocalDateTime.now().getYear() - referenceTools.getIncrementFirstYear()) + (LocalDateTime.now().getMonthValue());
        return referenceTools.getPrefix() + String.format("%03d", monthsRef) + referenceTools.getSuffix() + 1;
    }


    public static String generateInvoiceReference(String reference) {
        int year = LocalDateTime.now().getYear() % 100;
        long increment = Long.parseLong(reference.substring(4)) + 1;
        if (year != Integer.parseInt(reference.substring(0, 2))) {
            increment = 1;
        }
        String month = String.format("%02d", LocalDateTime.now().getMonthValue());

        return year + month + String.format("%05d", increment);
    }

    public static String generateFirstInvoiceReference() {
        int year = LocalDateTime.now().getYear() % 100;
        String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        return year + month + String.format("%05d", 1);
    }

}
