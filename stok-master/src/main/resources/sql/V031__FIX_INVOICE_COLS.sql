ALTER TABLE INVOICE ADD COLUMN IF NOT EXISTS CLIENT_ID BIGINT;
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name='invoice'
          AND column_name='timber'
    ) THEN
        EXECUTE 'ALTER TABLE invoice RENAME COLUMN timber TO timbre';
    END IF;
END $$;
ALTER TABLE PRODUCT_INVOICE ADD COLUMN IF NOT EXISTS INVOICE_ID BIGINT;
