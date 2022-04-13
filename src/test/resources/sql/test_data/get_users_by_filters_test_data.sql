SET IDENTITY_INSERT [user] ON;
INSERT INTO [user] (id, name, age, address_id) VALUES
(1, N'Johnson', 25, 1),
(2, N'Peterson', 30, 2);
SET IDENTITY_INSERT [user] OFF;

SET IDENTITY_INSERT number ON;
INSERT INTO number (id, number, user_id) VALUES
(1, N'+121111111', 1),
(2, N'+111111', 2),
(3, N'+222222', 2);
SET IDENTITY_INSERT number OFF;