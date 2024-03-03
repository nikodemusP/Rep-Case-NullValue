CREATE OR REPLACE PACKAGE test_inout AS
PROCEDURE test_1_inout ( 
               test_1 IN OUT NUMBER );
END test_inout;
/

CREATE OR REPLACE PACKAGE BODY test_inout AS
PROCEDURE test_1_inout( test_1 IN OUT NUMBER)
IS
BEGIN
    if test_1 IS NULL THEN
      test_1 := 5;
    END IF;
END;
--
END test_inout;
/
