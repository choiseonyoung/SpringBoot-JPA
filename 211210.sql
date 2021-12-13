CREATE DATABASE toyDB; -- db 생성
CREATE USER toy@localhost; -- 사용자 생성
GRANT all privileges on  *.* TO toy@localhost; -- 권한부여
ALTER USER 'toy'@'localhost' identified WITH mysql_native_password BY 'toy1234';