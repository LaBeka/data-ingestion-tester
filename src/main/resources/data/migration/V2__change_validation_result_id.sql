-- Step 1: Drop the old PK constraint
ALTER TABLE validation_result
    DROP CONSTRAINT validation_result_pkey;

-- Step 2: Drop the old id column
ALTER TABLE validation_result
    DROP COLUMN id;

-- Step 3: Add new UUID id column with default
ALTER TABLE validation_result
    ADD COLUMN id UUID PRIMARY KEY;

-- NOTE: gen_random_uuid() requires the "pgcrypto" extension.
-- If not enabled yet, run:
-- CREATE EXTENSION IF NOT EXISTS "pgcrypto";
