package com.yesmind.stok.unit.reference;

import com.yesmind.stok.core.business.ReferenceGenerationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ReferenceUtilsTest {


    @Test
    void shouldGenerateFirstInvoiceReference() {

       String ref1 = generateFirst(LocalDateTime.of(2026, 2, 12, 5, 5, 5));
       Assertions.assertEquals(ref1, "260200001");

       String ref2 = generateFirst(LocalDateTime.of(2025, 10, 12, 5, 5, 5));
       Assertions.assertEquals(ref2, "251000001");

       String ref3 = generateFirst(LocalDateTime.of(2067, 6, 12, 5, 5, 5));
       Assertions.assertEquals(ref3, "670600001");
    }

    @Test
    void shouldGenerateInvoiceReference() {

       String ref1 = generate(LocalDateTime.of(2026, 11, 12, 5, 5, 5), "260200288");
       Assertions.assertEquals(ref1, "261100289");

       String ref2 = generate(LocalDateTime.of(2025, 12, 12, 5, 5, 5), "251200002");
       Assertions.assertEquals(ref2, "251200003");

       String ref3 = generate(LocalDateTime.of(2067, 6, 12, 5, 5, 5), "650800077");
       Assertions.assertEquals(ref3, "670600001");

       String ref4 = generate(LocalDateTime.of(2067, 7, 12, 6, 5, 5), "670600001");
       Assertions.assertEquals(ref4, "670700002");

       String ref5 = generate(LocalDateTime.of(2033, 1, 12, 6, 5, 5), "330100033");
       Assertions.assertEquals(ref5, "330100034");
    }

    String generateFirst(LocalDateTime date) {

        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);

            return ReferenceGenerationUtils.generateFirstInvoiceReference();
        }
    }

    String generate(LocalDateTime date, String reference) {

        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);

            return ReferenceGenerationUtils.generateInvoiceReference(reference);
        }
    }
}
