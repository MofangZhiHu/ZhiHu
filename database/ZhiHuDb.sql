USE [ZhiHuDB]
GO
/****** Object:  Table [dbo].[UserInfo]    Script Date: 10/24/2016 15:05:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserInfo](
	[UserName] [nvarchar](30) NOT NULL,
	[PassWord] [nvarchar](20) NOT NULL,
	[MobilePhone] [nvarchar](20) NOT NULL,
	[OccupationOrProfession] [nvarchar](50) NULL,
	[E-Mail] [nvarchar](50) NULL,
	[State] [nvarchar](5) NULL,
 CONSTRAINT [PK_UserInfo] PRIMARY KEY CLUSTERED 
(
	[MobilePhone] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[UserInfo] ([UserName], [PassWord], [MobilePhone], [OccupationOrProfession], [E-Mail], [State]) VALUES (N'33', N'22', N'11', NULL, NULL, NULL)
INSERT [dbo].[UserInfo] ([UserName], [PassWord], [MobilePhone], [OccupationOrProfession], [E-Mail], [State]) VALUES (N'dd', N'123', N'12', N'hggh', N'dfff', N'0')
INSERT [dbo].[UserInfo] ([UserName], [PassWord], [MobilePhone], [OccupationOrProfession], [E-Mail], [State]) VALUES (N'111111', N'5656', N'13558881846', NULL, NULL, NULL)
INSERT [dbo].[UserInfo] ([UserName], [PassWord], [MobilePhone], [OccupationOrProfession], [E-Mail], [State]) VALUES (N'55', N'32', N'18775351394', NULL, NULL, NULL)
INSERT [dbo].[UserInfo] ([UserName], [PassWord], [MobilePhone], [OccupationOrProfession], [E-Mail], [State]) VALUES (N'23', N'12', N'4', NULL, NULL, NULL)
INSERT [dbo].[UserInfo] ([UserName], [PassWord], [MobilePhone], [OccupationOrProfession], [E-Mail], [State]) VALUES (N'name ', N'password"', N'phone', NULL, NULL, NULL)
/****** Object:  Table [dbo].[ManagerInfo]    Script Date: 10/24/2016 15:05:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ManagerInfo](
	[ManagerName] [nvarchar](30) NOT NULL,
	[PassWord] [nvarchar](20) NOT NULL,
	[MobilePhone] [nvarchar](20) NOT NULL
) ON [PRIMARY]
GO
