# --- !Ups

CREATE TABLE TEAM (
  TEAM_ID    INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (TEAM_ID),
  NAME         VARCHAR(50),
);

CREATE TABLE PLAYER (
  PLAYER_ID    INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (PLAYER_ID),
  NAME         VARCHAR(50),
  TEAM_ID      INT UNSIGNED,
  KEY TEAM_ID (TEAM_ID),
  CONSTRAINT players_fk_1 FOREIGN KEY (TEAM_ID) REFERENCES TEAM (TEAM_ID)
);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists PLAYER;

SET REFERENTIAL_INTEGRITY TRUE;
