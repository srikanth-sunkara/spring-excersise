REPLACE INTO `roles` VALUES (1,'ADMIN');
REPLACE INTO `roles` VALUES (2,'GUEST');
REPLACE INTO `users` (`user_id`,`active`,`email`,`last_name`,`name`,`password`,`user_name`) VALUES (1,1,'admin@admin.com','admin','admin','$2a$10$R0LZ7IcI2gelM2pTykrJ/umvBF6d502RbTCeZk08gLJCEUApSnxwq','admin');