--liquibase formatted sql

--changeset alexandergarifullin:1
INSERT INTO security.t_users(c_name, c_login, c_hashed_password, c_role)
VALUES ('Test User', 'user', '$2a$10$kjWwIGpZ2EjGl5edbc9ceuNE5ceP3vdtiPqoAUgCdHtk4on97uvOC', 'USER');
