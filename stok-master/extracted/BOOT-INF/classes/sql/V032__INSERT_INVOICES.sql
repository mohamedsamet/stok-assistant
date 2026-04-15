CREATE EXTENSION IF NOT EXISTS "pgcrypto";

 -- 1️⃣ Clone qualifying invoices and capture their new IDs
 WITH cloned_invoices AS (
    INSERT INTO INVOICE (
        PUBLIC_ID,
        REFERENCE,
        CREATION_DATE,
        CREATION_STATUS,
        TOTAL_BRUT,
        TOTAL_TVA,
        TOTAL_NET,
 	    CLIENT_ID,
        CLOSED,
        REFERENCE_INVOICE,
        REFERENCE_BL
    )
     SELECT
         gen_random_uuid() AS public_id,
         reference,
         CREATION_DATE,
         CREATION_STATUS,
         TOTAL_BRUT,
         TOTAL_TVA,
            CASE
            WHEN TIMBRE IS NOT NULL AND TIMBRE > 0
            THEN TOTAL_NET - TIMBRE
            ELSE TOTAL_NET
        END AS TOTAL_NET,
 		CLIENT_ID,
         CLOSED,
         NULL AS REFERENCE_INVOICE,
         REFERENCE_BL
     FROM INVOICE
     WHERE REFERENCE_BL is not null and reference_invoice is not null
     RETURNING id AS new_invoice_id, REFERENCE_BL AS ref_bl
 )

 INSERT INTO product_invoice (
     invoice_id,
     public_id,
     product_public_id,
     product_name,
     product_reference,
     unit,
     quantity,
     unit_price,
     discount,
     total_price,
     tva,
     deleted
 )
 SELECT
     ci.new_invoice_id,  -- link to the newly cloned invoice
     gen_random_uuid() AS public_id,
     pi.product_public_id,
     pi.product_name,
     pi.product_reference,
     pi.unit,
     pi.quantity,
     pi.unit_price,
     pi.discount,
     pi.total_price,
     pi.tva,
     pi.deleted
 FROM cloned_invoices ci
 JOIN invoice i
   ON i.REFERENCE_BL = ci.ref_bl
 JOIN product_invoice pi
   ON pi.invoice_id = i.id;

 UPDATE INVOICE SET REFERENCE_BL = NULL WHERE REFERENCE_BL IS NOT NULL AND REFERENCE_INVOICE IS NOT NULL;
