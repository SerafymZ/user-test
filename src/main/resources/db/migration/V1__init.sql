IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='user' and xtype='U')
CREATE TABLE [dbo].[user](
	[id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[name] [nvarchar](50) NULL,
	[age] [int] NULL,
	[address_id] [bigint] NULL
);

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='number' and xtype='U')
CREATE TABLE [dbo].[number](
	[id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[number] [nvarchar](50) NULL,
	[user_id] [bigint] NULL FOREIGN KEY REFERENCES [user](id)
);

