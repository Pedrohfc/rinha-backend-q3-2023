CREATE ALIAS UUID_TO_BIN FOR "org.rinha.TestResources.uuidToBin";
CREATE ALIAS BIN_TO_UUID FOR "org.rinha.TestResources.binToUuid";

CREATE TABLE `person` (
  `person_id` binary(16) NOT NULL,
  `nick_name` varchar(32) NOT NULL UNIQUE,
  `name` varchar(100) NOT NULL,
  `birth_date` date NOT NULL,
  `stack` text,
  --fulltext(name,nick_name,stack), incompativel com o h2
  PRIMARY KEY (`person_id`)
);