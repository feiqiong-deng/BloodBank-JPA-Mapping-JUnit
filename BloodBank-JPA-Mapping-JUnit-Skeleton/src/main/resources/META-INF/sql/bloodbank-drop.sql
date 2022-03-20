-- -----------------------------------------------------
-- Drop Schema for BloodBank Application
--
-- In order for the `cst8277`@`localhost` user to be able to Create (or Drop) a Schema,
-- it needs additional privileges. If you are using MySQL Workbench, log-in to it as root,
-- Click on the 'Administration' tab, select 'Users and Privileges' and find the cst8277 user.
-- The 'Administrative Roles' tab has an entry 'DBA' - select, click all the individual PRIVILEGES
-- and then apply.
--
-- If you wish to use a 'raw' .sql-script instead, you still need to log-in as root,
-- the command to GRANT the appropriate PRIVILEGES is:
--  GRANT ALL PRIVILEGES ON *.* TO `cst8277`@`localhost`;
--
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `bloodbank`;