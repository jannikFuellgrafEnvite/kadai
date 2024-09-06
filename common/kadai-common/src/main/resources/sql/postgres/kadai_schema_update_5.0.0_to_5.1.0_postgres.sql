-- this script updates the KADAI database schema from version 5.0.0 to version 5.1.0.

SET search_path = %schemaName%;

INSERT INTO KADAI_SCHEMA_VERSION (VERSION, CREATED) VALUES ('5.1.0', CURRENT_TIMESTAMP);

DROP INDEX IF EXISTS IDX_TASK_POR_VALUE;

 CREATE INDEX IDX_TASK_LOWER_POR_VALUE ON TASK
   (LOWER(POR_VALUE) ASC, WORKBASKET_ID ASC);
   COMMIT WORK ;
 CREATE INDEX IDX_TASK_POR_VALUE ON TASK
   (POR_VALUE ASC, WORKBASKET_ID ASC);
   COMMIT WORK ;