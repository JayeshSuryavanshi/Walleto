-- MySQL container bootstrap for Walleto.
-- The mysql image's entrypoint creates the primary database (MYSQL_DATABASE=amigowallet_db)
-- and the app user (MYSQL_USER/MYSQL_PASSWORD) with rights on that database.
-- This script adds the SECOND database (edubank_db) and grants the same app user access to it.
-- Each service's schema is then owned/migrated by its own Flyway; this only creates empty DBs.

CREATE DATABASE IF NOT EXISTS amigowallet_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS edubank_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 'walleto'@'%' is created by the entrypoint from MYSQL_USER/MYSQL_PASSWORD.
GRANT ALL PRIVILEGES ON amigowallet_db.* TO 'walleto'@'%';
GRANT ALL PRIVILEGES ON edubank_db.*     TO 'walleto'@'%';
FLUSH PRIVILEGES;
