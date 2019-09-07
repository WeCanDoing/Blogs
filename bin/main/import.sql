INSERT INTO user (id, username, password, name, email) VALUES (1, 'admin','$2a$10$rf0PQpkLQaj.5YP1zRJp3.ALV4sPklX8ld749PrXR6wY1oYQ2OePi', '带电的皮卡丘', 'i@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (2, 'user', '$2a$10$rf0PQpkLQaj.5YP1zRJp3.ALV4sPklX8ld749PrXR6wY1oYQ2OePi', 'YOHYO', 'HH@xinlang.com');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
