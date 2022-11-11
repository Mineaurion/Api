ALTER TABLE server
ADD prometheus_ip VARCHAR(255) NOT NULL DEFAULT("");
ALTER TABLE server
ADD prometheus_port INT NOT NULL DEFAULT(0);