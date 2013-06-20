create or replace view v_dictionary as
select i.iKEY AS id,i.NAME AS name,i.DICID AS dicId,i.ODB AS odb from item i where (i.DISABLED is null or (i.DISABLED <> '1'))


-- 性别
union select DISTINCT '0','男','gender','2' from dual
union select DISTINCT '1','女','gender','3' from dual
-- 密码问题
union select DISTINCT '0','您母亲的姓名是什么？','problem','0'  from dual
union select DISTINCT '1','您父亲的姓名是什么？','problem','1' from dual
union select DISTINCT '2','您的大学名字是什么？','problem','2' from dual
union select DISTINCT '3','您的出生地是哪里？','problem','3'  from dual
union select DISTINCT '4','您最喜欢的电影是什么？','problem','4'from dual
union select DISTINCT '5','您最喜欢的书是什么？','problem','5' from dual
union select DISTINCT '6','其它','problem','6' from dual

--应用类型
union select DISTINCT '0','卡账户应用','apptype','0'   from dual
union select DISTINCT '1','网络支付应用','apptype','1'   from dual
union select DISTINCT '2','账户安全应用','apptype','2'   from dual

-- 是否置顶
union select DISTINCT '0','否','istop','0'  from dual
union select DISTINCT '1','是','istop','1' from dual

-- 是否发布
union select DISTINCT '0','否','ispublish','0'  from dual
union select DISTINCT '1','是','ispublish','1' from dual


-- 是否记名卡
union select DISTINCT '0','记名卡','isAnonymous','0'   from dual
union select DISTINCT '1','非记名卡','isAnonymous','1' from dual


union select c.cardno||'',c.cardno||'',memberid,'' from card c where c.Remove='0'	-- 当前登录人的卡号集合
union select c.cardno||'',c.cardno||'',memberid||'anonymous','' from card c where c.isAnonymous='0' and c.Remove='0'


-- 用户
union select distinct e.USERID AS USERID,e.USERNAME AS USERNAME,'users' AS users,'odb' AS odb from employee e where ((e.SCRAP != '0') or (e.SCRAP is null))

-- 实名认证证件类型
union select DISTINCT '1','身份证','certificationtype','1'  from dual
union select DISTINCT '3','护照','certificationtype','2' from dual
union select DISTINCT '2','军官证','certificationtype','3'  from dual
union select DISTINCT '4','营业执照','certificationtype','4' from dual
union select DISTINCT '5','驾驶证','certificationtype','5' from dual
union select DISTINCT '6','组织机构代码证','certificationtype','6' from dual

-- 账户信息服务类型
union select DISTINCT '01','交易短信通知（预付费卡）','infotype','0' from dual
union select DISTINCT '02','交易短信通知（支付账户）','infotype','1' from dual
union select DISTINCT '03','对账单邮寄（电子邮箱）','infotype','2' from dual

-- 账户信息服务状态
union select DISTINCT '01','关闭','infostatus','0' from dual
union select DISTINCT '02','已开启','infostatus','1' from dual
union select DISTINCT '','关闭','infostatus','2' from dual

-- 交易类型
union select '0','全部','trade_type','0' from dual
union select '100','主账户充值','trade_type','1' from dual
union select '110','现金退款','trade_type','2' from dual
union select '120','转账(进)','trade_type','3' from dual
union select '121','转账(出)','trade_type','4' from dual
union select '150','IC卡消费','trade_type','5' from dual
union select '151','主账户消费','trade_type','6' from dual
union select '600','账户管理费','trade_type','7' from dual

-- 交易状态
union select '0','全部','trade_status','0' from dual
union select '1','正常','trade_status','1' from dual
union select '2','冲正','trade_status','2' from dual
union select '3','被冲正','trade_status','3' from dual
union select '4','临时','trade_status','4' from dual
union select '5','撤销','trade_status','5' from dual
union select '6','被撤销','trade_status','6' from dual
union select '8','失效','trade_status','7' from dual
union select '10','退货','trade_status','8' from dual



