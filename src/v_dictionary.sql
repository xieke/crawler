create or replace view v_dictionary as
select `i`.`iKEY` AS `id`,`i`.`NAME` AS `name`,`i`.`DICID` AS `dicId`,`i`.`ODB` AS `odb` from `item` `i` where (isnull(`i`.`DISABLED`) or (`i`.`DISABLED` <> '1')) 

union select DISTINCT '0','N','news_status','0' from dual  -- 状态(未处理)
union select DISTINCT '1','Y','news_status','1' from dual  -- (已处理)
-- union select DISTINCT '2','D','news_status','2' from dual  -- (删除)

union select DISTINCT '','全部','news_status_all','0' from dual	 -- 状态(增加一个默认值)
union select DISTINCT '0','N','news_status_all','2' from dual 
union select DISTINCT '1','Y','news_status_all','1' from dual 
 
union select distinct `e`.`USERID` AS `USERID`,`e`.`USERNAME` AS `USERNAME`,'users' AS `users`,'odb' AS `odb` from `employee` `e` where ((`e`.`SCRAP` <> '0') or isnull(`e`.`SCRAP`))

union
select '0','1','importance','' from dual		-- GP重要度
union
select '1','2','importance','' from dual
union
select '2','3','importance','' from dual
union
select '3','4','importance','' from dual
union
select '4','5','importance','' from dual

union
select '','全部','importance_all','0' from dual	 -- GP重要度(增加一个默认值)
union
select '0','1','importance_all','1' from dual		
union
select '1','2','importance_all','2' from dual
union
select '2','3','importance_all','3' from dual
union
select '3','4','importance_all','4' from dual
union
select '4','5','importance_all','5' from dual

union
select '0','1','urgent','' from dual			 -- 客户重要度
union
select '1','2','urgent','' from dual
union
select '2','3','urgent','' from dual
union
select '3','4','urgent','' from dual
union
select '4','5','urgent','' from dual

union
select '','全部','urgent_all','0' from dual  -- 客户重要度(增加一个默认值)
union
select '0','1','urgent_all','1' from dual			 
union
select '1','2','urgent_all','2' from dual
union
select '2','3','urgent_all','3' from dual
union
select '3','4','urgent_all','4' from dual
union
select '4','5','urgent_all','5' from dual

union
select '0','knowledge','sort','1' from dual
union
select '1','information','sort','0' from dual

union
select '','全部','sort_all','0' from dual  -- 文章分类(增加一个默认值)
union
select '0','knowledge','sort_all','2' from dual		
union
select '1','information','sort_all','1' from dual


union
select 'c','中文','lang','0' from dual  -- 语言分类(增加一个默认值)
union
select 'e','英文','lang','2' from dual		
union
select 'ce','中英文','lang','1' from dual

union
select '','all','postjob_status','0' from dual   -- 任务状态(增加一个默认值)
union
select '1','enable','postjob_status','1' from dual		

union
select '0','disable','postjob_status','2' from dual  



union
select '1','激活','active','' from dual  
union
select '0','未激活','active','' from dual

union
select '1','成功','result','' from dual 
union
select '0','失败','result','' from dual

union
select '1','运行中','runstatus','' from dual 
union
select '0','停止','runstatus','' from dual

union
select '0','每分钟','taskcycle','' from dual 
union
select '1','每小时','taskcycle','' from dual
union
select '2','每天','taskcycle','' from dual
union
select '3','每周','taskcycle','' from dual
union
select '4','每月','taskcycle','' from dual
union
select '5','只一次','taskcycle','' from dual
union
select '6','立即开始','taskcycle','' from dual

