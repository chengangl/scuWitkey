CREATE DATABASE IF NOT EXISTS `scuwitkey`
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON scuwitkey.* TO 'scuwitkey'@'localhost'
IDENTIFIED BY 'scuwitkey'
WITH GRANT OPTION;