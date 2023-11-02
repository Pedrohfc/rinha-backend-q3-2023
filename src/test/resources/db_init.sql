CREATE ALIAS UUID_TO_BIN FOR "org.rinha.TestResources.uuidToBin";

CREATE TABLE `person` (
  `person_id` binary(16) NOT NULL,
  `nick_name` varchar(32) NOT NULL UNIQUE,
  `name` varchar(100) NOT NULL,
  `birth_date` date NOT NULL,
  `stack` text,
  PRIMARY KEY (`person_id`)
);