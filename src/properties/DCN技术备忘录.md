# 内外网数据交互网闸数据库脚本
<pre>
--办件查询-网闸交换内网输入前置表(对应外网输出表)
CREATE TABLE DDX_IN_QUERY_SERVICE
(
    SN VARCHAR2(32) NOT NULL,
    ACTION VARCHAR2(6) NOT NULL,
    ASYNC CHAR(1) DEFAULT 'Y',
    COMMAND VARCHAR2(4000) NOT NULL,
    HASH VARCHAR2(32) NOT NULL,
    Q_START TIMESTAMP(6) NOT NULL,
    Q_IP VARCHAR2(15) DEFAULT '0.0.0.0',
    Q_TIMEOUT NUMBER(10) DEFAULT -1
);
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.SN IS '查询业务流水号(UUID)';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.ACTION IS '执行命令标识
1.实名认证
2.堵路移车
3.一键核查
4.车辆违法查询
5.驾驶证违法查询
6.我的车辆
7.我的驾驶证
';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.ASYNC IS '是否为异步查询 Y:是 N:否';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.COMMAND IS '查询命令内容(JSON格式)';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.HASH IS '查询命令内容散列值(内容信息完整性校验)';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.Q_START IS '查询发起时间(从外网数据库传入)';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.Q_IP IS '查询客户端IP';
COMMENT ON COLUMN DDX_IN_QUERY_SERVICE.Q_TIMEOUT IS '查询超时时间(如果大于超时时限则放弃内网查询)';
CREATE TABLE DDX_OU_QUERY_SERVICE
(
    SN VARCHAR2(32) NOT NULL,
    ACTION VARCHAR2(6),
    RES CLOB,
    HASH VARCHAR2(32) NOT NULL,
    SUCC CHAR(1) DEFAULT 'Y',
    CODE VARCHAR2(10) DEFAULT '200',
    MSG VARCHAR2(100),
    RESP_TIME TIMESTAMP(6) DEFAULT SYSDATE
);
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.SN IS '查询业务流水号(与传入一致)';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.ACTION IS '命令标识';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.RES IS '执行结果(JSON格式)';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.HASH IS '查询结果内容散列值(内容信息完整性校验)';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.SUCC IS '执行是否成功 Y:成功 N:失败';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.CODE IS '执行结果响应码(参照各接口规范)';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.MSG IS '执行结果附加消息';
COMMENT ON COLUMN DDX_OU_QUERY_SERVICE.RESP_TIME IS '执行完成时间';
</pre>

#DCN配置及说明
<pre>
#===========================================================#
#       Oracle DCN Listening Database Configuration         #
#===========================================================#
#DCN监听数据库地址
dcn.url=jdbc:oracle:thin:@//172.17.100.139:8443/orcl
#DCN监听数据库地址
dcn.user=SCOTT
#DCN监听数据库用户密码
dcn.pwd=123
#DCN监听对应本地端口号(默认为空即可)
dcn.ntf_local_host=
#DCN监听成功并在处理完成业务后是否删除前置库数据,默认为true(删除)
dcn.reqcmd.del=false
#DCN监听到数据是否来自黑名单IP发送,在黑名单列表中数据不被处理,多个IP逗号分割
dcn.reqcmd.blacklist=127.0.0.1,172.17.100.20

#===========================================================#
#                 Plugin Operation DataSource               #
#===========================================================#
#插件中业务处理操作数据库(默认为监听数据库一致)
jdbc.driverClassName=oracle.jdbc.OracleDriver
#插件中业务处理操作数据连接地址
jdbc.url=jdbc:oracle:thin:@//172.17.100.139:8443/orcl
#插件中业务处理操作数据用户名
jdbc.username=SCOTT
#插件中业务处理操作数据用户密码
jdbc.password=123
#插件中业务处理操作数据库连接池初始化连接数
dataSource.initialSize=10
#插件中业务处理操作数据库连接池最大空闲连接
dataSource.maxIdle=20
#插件中业务处理操作数据库连接池最小空闲连接
dataSource.minIdle=5
#插件中业务处理操作数据库连接池最大活动连接
dataSource.maxActive=50
#插件中业务处理操作数据库最长等待时间
dataSource.maxWait=1000
#===========================================================#
#                 DCN Plugin Configuration                  #
#===========================================================#
#DCN插件所在包名
dcn.plugins.package=cn.ucox.modules.dcn.plugin
#DCN插件类名,多个插件逗号分割
dcn.plugins.classes=CivilNameQueryPlugin,IllegalQueryPlugin
</pre>
