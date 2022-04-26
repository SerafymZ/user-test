IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users')
CREATE TABLE [dbo].[users](
    [username] [nvarchar](15) PRIMARY KEY,
    [password] [nvarchar](150),
    [enabled] [tinyint]
);

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='authorities')
CREATE TABLE [dbo].[authorities](
    [username] [nvarchar](15) FOREIGN KEY REFERENCES [users](username),
    [authority] [nvarchar](15)
);

INSERT INTO [dbo].[users] (username, password, enabled) VALUES
(N'user', N'{bcrypt}$2a$10$j5DUDV4o2reodruq9086FePFB4fxLrzq/0ZUqh3zXotYEaC2oemai', 1),
(N'admin', N'{bcrypt}$2a$10$50Oag0ifCFghZ1pMU5WeSO1hKHfpgY2DHBAb2TUv/vgK7SWy81IqS', 1);

INSERT INTO [dbo].[authorities] (username, authority) VALUES
(N'user', N'ROLE_USER'),
(N'admin', N'ROLE_ADMIN');