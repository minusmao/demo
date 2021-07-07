/**
  文件名：test.sql
  描述：测试用的 sql 文件
  时间：2021-07-04
  作者：TechRice
 */

# 联结查询：根据用户 id 查询该用户的所有角色
SELECT r.*
FROM sys_role AS r
         LEFT JOIN sys_user_role AS ur on r.id = ur.role_id
WHERE ur.user_id = 1;

# 联结查询：根据角色 id 查询该角色的所有菜单操作
SELECT m.*
FROM sys_menu AS m
         LEFT JOIN sys_role_menu rm on m.id = rm.menu_id
WHERE rm.role_id = 3;

# 联结查询：根据用户 id 查询该用户的所有角色的所有菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
SELECT DISTINCT m.*
FROM sys_menu AS m
         LEFT JOIN sys_role_menu rm on m.id = rm.menu_id
         LEFT JOIN sys_user_role ur on rm.role_id = ur.role_id
WHERE ur.user_id = 1;

# 联结查询：根据角色 id 查询拥有此角色的所有用户
SELECT u.*
FROM sys_user AS u
         LEFT JOIN sys_user_role ur on u.id = ur.user_id
WHERE ur.role_id = 6;

# 联结查询：通过菜单 id 得到拥有此菜单的所有用户（注：多个角色会引起查询的用户重复，查询时需要使用 DISTINCT 关键字去重）
SELECT DISTINCT u.*
FROM sys_user AS u
         LEFT JOIN sys_user_role ur on u.id = ur.user_id
         LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id
WHERE rm.menu_id = 1;