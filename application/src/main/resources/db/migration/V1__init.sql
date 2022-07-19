CREATE TABLE server (
   id BIGINT NOT NULL AUTO_INCREMENT,
   beta BIT(1) NOT NULL,
   donator BIT(1) NOT NULL,
   paying BIT(1) NOT NULL,
   external_id BINARY(255) NOT NULL,
   ip VARCHAR(255) NOT NULL,
   port INT NOT NULL,
   regen BIT(1) NOT NULL,
   dns VARCHAR(255) NOT NULL,
   name VARCHAR(255) NOT NULL,
   type VARCHAR(255) NOT NULL,
   minecraft VARCHAR(255) NOT NULL,
   modpack VARCHAR(255) NOT NULL,
   CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE server_reboot (
  server_id BIGINT NOT NULL,
   reboot VARCHAR(255) NULL
);

ALTER TABLE server_reboot ADD CONSTRAINT FK_serverID FOREIGN KEY (server_id) REFERENCES server (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

CREATE INDEX FK_serverID ON server_reboot(server_id);