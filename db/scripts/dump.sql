-- Dumping data for table `tb_roles`
INSERT INTO tb_roles (role_id, role_name) VALUES (1, 'ADMIN');
INSERT INTO tb_roles (role_id, role_name) VALUES (2, 'USER');

-- Dumping data for table `tb_users`
INSERT INTO tb_users (user_id, username, password) 
VALUES ('b4e5d2a5-8a71-4b5b-8b53-bbe111f473e5', 'admin', '$2a$12$M9IKiSdA1a/nIrJjBMq.yujuAryCxw0o1uAOKHO4W/W1bAu4flJOi');

-- Dumping data for table `tb_users_roles`
INSERT INTO tb_users_roles (user_id, role_id) 
VALUES ('b4e5d2a5-8a71-4b5b-8b53-bbe111f473e5', 1);