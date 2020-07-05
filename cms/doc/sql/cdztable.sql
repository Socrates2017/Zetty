CREATE TABLE `charge_station` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商家id',
        `name` varchar(50) DEFAULT '' COMMENT '名称',
        `address` varchar(256) DEFAULT '' COMMENT '地址',
        `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
        `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
        `service_type` tinyint(4) DEFAULT NULL COMMENT '运营类型',
        `service_status` tinyint(4) DEFAULT NULL COMMENT '运营状态',
        `station_type` tinyint(4) DEFAULT NULL COMMENT '站点类型',
        `register_time` int(32) unsigned DEFAULT NULL COMMENT '投运时间',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电站表\n标识一个充电站';


CREATE TABLE `charge_pile` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cs_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电站id',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商家id，冗余字段',
        `fee_rule_id` bigint(64) unsigned DEFAULT NULL COMMENT '计费规则id',
        `fee_rule_sn` int(32) unsigned NOT NULL COMMENT '下发到充电桩的计费规则id，冗余字段',
        `cp_no` varchar(16) DEFAULT NULL COMMENT '充电桩编号',
        `cp_name` varchar(50) DEFAULT '' COMMENT '充电桩名称',
        `contract_no` varchar(50) DEFAULT NULL COMMENT '合同编号',
        `assets_no` varchar(50) DEFAULT NULL COMMENT '资产编号',
        `install_addr` varchar(256) DEFAULT NULL COMMENT '安装地址',
        `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
        `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
        `gun_num` tinyint(4) DEFAULT NULL COMMENT '充电枪数量',
        `cp_inf` tinyint(4) DEFAULT NULL COMMENT '充电接口(1为国标，2为比亚迪，3为特斯拉，4为其它)',
        `spe_cp` tinyint(4) DEFAULT NULL COMMENT '快慢充（1为快充，2为慢充）',
        `cp_net` tinyint(4) DEFAULT NULL COMMENT '是否联网（0为建设中，1为未联网，2为联网）',
        `cp_status` tinyint(4) DEFAULT NULL COMMENT '充电桩状态（1为充电中，2为空闲，3为故障，4为预约，5为离线',
        `fau_sta` tinyint(4) DEFAULT NULL COMMENT '故障状态(0为无故障，1为机器故障，2为网络故障，3为系统故障)',
        `cp_ip` varchar(20) DEFAULT NULL COMMENT '充电桩的IP',
        `cp_port` varchar(8) DEFAULT NULL COMMENT '充电桩的IP',
        `cp_sort` tinyint(4) DEFAULT NULL COMMENT '排序号',
        `pur_ele_sch_id` bigint(64) DEFAULT NULL COMMENT '购电方案ID',
        `service_status` tinyint(4) DEFAULT NULL COMMENT '运营状态（0为非运营，1为运营）',
        `product_time` datetime DEFAULT NULL COMMENT '出厂日期',
        `register_time` datetime DEFAULT NULL COMMENT '投运日期',
        `per_res` tinyint(4) DEFAULT NULL COMMENT '允许预约（0为不允许，1为允许）',
        `v_hw` varchar(20) DEFAULT NULL COMMENT '硬件版本号',
        `v_sw` varchar(20) DEFAULT NULL COMMENT '软件版本号',
        `v_prot` varchar(20) DEFAULT NULL COMMENT '协议版本号',
        `remark` varchar(512) DEFAULT NULL COMMENT '备注',
        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电桩表\n标识一个充电桩';


CREATE TABLE `biz_cp_no` (
        `cp_no` varchar(16) NOT NULL COMMENT '充电桩编号',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商家id',
        `status` tinyint(4) DEFAULT NULL COMMENT '1已用；0 未使用',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`cp_no`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电桩设备id表\n标识充电桩设备id';


CREATE TABLE `cp_register_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id，等同设备编码',
        `software_version` varchar(15) DEFAULT '' COMMENT '软件版本',
        `protocol_version` varchar(15) DEFAULT '' COMMENT '通信协议版本',
        `status` tinyint(4) DEFAULT NULL COMMENT '1表示在线允许充电；0 离线禁止充电',
        `cp_time` int(32) unsigned DEFAULT NULL COMMENT '充电桩当前时间',
        `id_number` varchar(32) DEFAULT '' COMMENT '营业执照',
        `imei` varchar(32) DEFAULT '' COMMENT 'imei或mac',


        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电桩注册记录表\n标识一次充电桩上报注册签到信息事件';


CREATE TABLE `charge_gun` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id',
        `sn` tinyint(4) unsigned DEFAULT NULL COMMENT '枪口编号 1-255',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电枪表\n标识一个充电枪';


CREATE TABLE `cg_quantity_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cg_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电枪id',
        `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id，冗余字段',
        `sn` tinyint(4) unsigned DEFAULT NULL COMMENT '枪口编号 1-255',
        `quantity` smallint(6) unsigned DEFAULT NULL COMMENT '电表底值/电量-总，比例 0.01',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='电表，充电桩发送表底检测数据记录表\n标识一次数据检测事件';


CREATE TABLE `cg_event_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cg_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电枪id',
        `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id，冗余字段',
        `event_type` tinyint(4) DEFAULT '-1' COMMENT '事件类型',
        `event_id` bigint(64) unsigned DEFAULT NULL COMMENT '事件序列id',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电桩事件发送记录表\n标识一次事件';


CREATE TABLE `cg_server_event_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cg_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电枪id',
        `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id，冗余字段',
        `event_type` tinyint(4) DEFAULT '-1' COMMENT '事件类型',
        `event_id` bigint(64) unsigned DEFAULT NULL COMMENT '事件序列id',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='平台回复事件记录表\n标识一次平台指令事件';


CREATE TABLE `cg_status` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `cg_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电枪id',
        `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id，冗余字段',
        `sn` bigint(32) unsigned DEFAULT NULL COMMENT '订单编号',
        `work_status` tinyint(4) DEFAULT NULL COMMENT '工作状态',
        `connect_status` tinyint(4) DEFAULT NULL COMMENT '连接状态',
        `output_contactor_status` tinyint(4) DEFAULT NULL COMMENT '输出接触器状态',
        `electronic_lock_status` tinyint(4) DEFAULT NULL COMMENT '电子锁状态',
        `fault_number` tinyint(4) DEFAULT NULL COMMENT '故障码',
        `voltage` smallint(6) unsigned DEFAULT NULL COMMENT '充电电压',
        `current` smallint(6) unsigned DEFAULT NULL COMMENT '充电电流',
        `bms_voltage` smallint(6) unsigned DEFAULT NULL COMMENT 'bms需求电压',
        `bms_current` smallint(6) unsigned DEFAULT NULL COMMENT 'bms需求电流',
        `bms_model` tinyint(4) DEFAULT NULL COMMENT 'bms充电模式',
        `input_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '已充电量',
        `power` smallint(6) unsigned DEFAULT NULL COMMENT '充电功率',
        `a_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出A相电压',
        `a_current` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出A相电流',
        `b_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出B相电压',
        `b_current` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出B相电流',
        `c_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出C相电压',
        `c_current` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出C相电流',
        `input_duration` int(8) DEFAULT NULL COMMENT '已充时长，分钟',
        `left_duration` int(8) DEFAULT NULL COMMENT '剩余充电时间预估，分钟',
        `cp_temperature` tinyint(4) DEFAULT NULL COMMENT '充电桩内部温度',
        `cg_temperature` tinyint(4) DEFAULT NULL COMMENT '枪头温度',
        `input_start_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '充电开始时电表读数',
        `input_now_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '当前电表读数',
        `now_soc` tinyint(4) DEFAULT NULL COMMENT '当前 SOC',
        `cell_highest_temperature` tinyint(4) DEFAULT NULL COMMENT '单体电池最高温度',
        `cell_highest_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '单体电池最高电压',
        `fee_id` bigint(64) unsigned DEFAULT NULL COMMENT '计费模型 id',
        `spike_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '尖电量',
        `spike_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '尖电费',
        `spike_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '尖服务费',
        `peak_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '峰电量',
        `peak_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '峰电费',
        `peak_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '峰服务费',
        `flat_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '平电量',
        `flat_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '平电费',
        `flat_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '平服务费',
        `valley_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '谷电量',
        `valley_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '谷电费',
        `valley_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '谷服务费',
        `now_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '当前充电电费',
        `now_total_fee` smallint(6) unsigned DEFAULT NULL COMMENT '当前充电总费用',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电枪状态记录表\n标识一次充电枪状态上报行为';


CREATE TABLE `biz_cg_order_record` (
        `id` bigint(20) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',

        `cgNo` varchar(20) NOT NULL COMMENT '枪口编号',
        `orderNo` varchar(32) DEFAULT NULL COMMENT '订单号',
        `start_soc` tinyint(4) DEFAULT '0' COMMENT '启动时SOC',
        `end_soc` tinyint(4) DEFAULT NULL COMMENT '结束时SOC',
        `start_time` int(11) unsigned DEFAULT NULL COMMENT '充电开始时间',
        `end_time` int(11) unsigned DEFAULT NULL COMMENT '充电结束时间',
        `start_quantity` int(11) unsigned DEFAULT NULL COMMENT '充电前电表读数',
        `end_quantity` int(11) unsigned DEFAULT NULL COMMENT '充电结束电表读数',
        `fee_rule` int(11) unsigned DEFAULT NULL COMMENT '计费模型 id',
        `spike_quantity` int(11) unsigned DEFAULT NULL COMMENT '尖电量',
        `spike_price` int(11) unsigned DEFAULT NULL COMMENT '尖单价',
        `spike_fee` int(11) unsigned DEFAULT NULL COMMENT '尖金额',
        `spike_server_price` int(11) unsigned DEFAULT NULL COMMENT '尖服务费单价',
        `spike_server_fee` int(11) unsigned DEFAULT NULL COMMENT '尖服务费',
        `peak_quantity` int(11) unsigned DEFAULT NULL COMMENT '峰电量',
        `peak_price` int(11) unsigned DEFAULT NULL COMMENT '峰单价',
        `peak_fee` int(11) unsigned DEFAULT NULL COMMENT '峰金额',
        `peak_server_price` int(11) unsigned DEFAULT NULL COMMENT '峰服务费单价',
        `peak_server_fee` int(11) unsigned DEFAULT NULL COMMENT '峰服务费',
        `flat_quantity` int(11) unsigned DEFAULT NULL COMMENT '平电量',
        `flat_price` int(11) unsigned DEFAULT NULL COMMENT '平单价',
        `flat_fee` int(11) unsigned DEFAULT NULL COMMENT '平金额',
        `flat_server_price` int(11) unsigned DEFAULT NULL COMMENT '平服务费单价',
        `flat_server_fee` int(11) unsigned DEFAULT NULL COMMENT '平服务费',
        `valley_quantity` int(11) DEFAULT NULL COMMENT '谷电量',
        `valley_price` int(11) DEFAULT NULL COMMENT '谷单价',
        `valley_fee` int(11) DEFAULT NULL COMMENT '谷金额',
        `valley_server_price` int(11) DEFAULT NULL COMMENT '谷服务费单价',
        `valley_server_fee` int(11) DEFAULT NULL COMMENT '谷服务费',
        `total_quantity` int(11) unsigned DEFAULT NULL COMMENT '本次充电总电量',
        `total_fee` int(11) unsigned DEFAULT NULL COMMENT '本次充电总电费',
        `total_server_fee` int(11) unsigned DEFAULT NULL COMMENT '本次充电总服务费',
        `total_amount` int(11) unsigned DEFAULT NULL COMMENT '充电总金额',
        `reason` int(11) DEFAULT NULL COMMENT '停止充电原因',
        `car_vin` varchar(17) DEFAULT NULL COMMENT '车辆VIN码',

        `version` int(11) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电桩发送账单信息表\n标识一次充电桩发送账单信息上报行为';


CREATE TABLE `merchant` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `id_number` varchar(32) DEFAULT '' COMMENT '营业执照',
        `name` varchar(50) DEFAULT '' COMMENT '名称',
        `address` varchar(256) DEFAULT '' COMMENT '地址',
        `status` tinyint(4) DEFAULT NULL COMMENT '状态',
        `phone` varchar(50) DEFAULT NULL,

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家表\n标识一个商家';


CREATE TABLE `merchant_profit_shard` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '管理员id,主键',
        `rate` decimal(10,2) DEFAULT NULL COMMENT '分成比例',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家收益分配配置表\n标识商家股东及其分成比例';


CREATE TABLE `merchant_dept` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `id` bigint(64) unsigned NOT NULL COMMENT '部门id，第二主键',
        `parent_id` int(11) DEFAULT NULL COMMENT '上级部门ID，一级部门为0。查询时需要加上商户id',
        `name` varchar(50) DEFAULT '' COMMENT '角色名称；',
        `description` varchar(256) DEFAULT '' COMMENT '描述',
        `order_num` int(11) DEFAULT NULL COMMENT '排序',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`, `id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家部门表\n标识一个商家部门';


CREATE TABLE `merchant_dept_user` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `merchant_dept_id` bigint(64) unsigned NOT NULL COMMENT '第二主键',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '用户id',
        `role` tinyint(4) DEFAULT '0' COMMENT '0:员工；1：经理',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`merchant_dept_id`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家部门-成员关系表\n标识一个商家部门-成员关系';


CREATE TABLE `merchant_role` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `id` bigint(64) unsigned NOT NULL COMMENT '第二主键',
        `name` varchar(50) DEFAULT '' COMMENT '角色名称；',
        `description` varchar(256) DEFAULT '' COMMENT '描述',
        `type` tinyint(4) DEFAULT '1' COMMENT '类型，0为超级管理员；1为其他',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`, `id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家角色表\n标识一个商家角色,有如下几类1.超级管理员，拥有所有权限，包括创建角色与添加管理员的权限；2.股东管理员，可查看分成情况；3.普通管理员，仅查看权限。4.自定义角色';


CREATE TABLE `merchant_resource` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `parent_id` bigint(64) unsigned DEFAULT NULL COMMENT '上级id (顶级为0)',
        `uri` varchar(256) DEFAULT NULL COMMENT '资源uri',
        `code` varchar(128) DEFAULT '' COMMENT '资源编码',
        `name` varchar(50) DEFAULT '' COMMENT '资源名称；',
        `description` varchar(256) DEFAULT '' COMMENT '描述',
        `is_menu` bit(1) DEFAULT b'0' COMMENT '是否是菜单, 0：否；1：是',
        `is_leaf` bit(1) DEFAULT b'0' COMMENT '是否叶子 0：否，1：是',
        `type` tinyint(4) DEFAULT '0' COMMENT '0:平台；1，商家；',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE,
        UNIQUE KEY `merchant_resource_code_IDX` (`code`) USING BTREE USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='资源表\n标识一个资源';


CREATE TABLE `merchant_role_user` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `merchant_role_id` bigint(64) unsigned NOT NULL COMMENT '第二主键',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '管理员id,用户模块中的用户id，第三主键。加索引',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`merchant_role_id`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家管理员（含客户分群）-角色关系表\n标识一个商家管理员-角色';


CREATE TABLE `merchant_role_resource` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `merchant_role_id` bigint(64) unsigned NOT NULL COMMENT '角色id。第2主键',
        `merchant_resource_id` bigint(64) unsigned NOT NULL COMMENT '角色id。第3主键',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`merchant_role_id`,`merchant_resource_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家角色资源权限关系表\n标识一个商家角色资源权限';


CREATE TABLE `merchant_operate_record` (
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家id，哪个商家创建的；默认为0，即系统创建',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '商家管理员id',
        `id` bigint(64) unsigned NOT NULL COMMENT '第三主键',


        `uri` varchar(256) DEFAULT '' COMMENT '操作接口',
        `param` varchar(1024) DEFAULT '' COMMENT '接口参数',
        `result` varchar(256) DEFAULT '' COMMENT '结果',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`user_id`, `id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家管理员操作记录表\n标识一次商家管理员操作行为';


CREATE TABLE `merchant_user` (
        `merchant_id` bigint(64) unsigned NOT NULL COMMENT '商家id，第一主键',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '商家id，第二主键',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家-用户表\n标识一个商家下面的用户';


CREATE TABLE `merchant_dict_type` (
        `merchant_id` bigint(64) unsigned NOT NULL COMMENT '商家id，第一主键；如为0则为系统创建',
        `code` varchar(50) NOT NULL COMMENT '字典类型编码，第二主键',
        `name` varchar(200) DEFAULT NULL COMMENT '字典类型名称',
        `remark` varchar(500) DEFAULT NULL COMMENT '备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`code`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='字典类型表\n标识一个字典类型';


CREATE TABLE `merchant_dict` (
        `merchant_id` bigint(64) unsigned NOT NULL COMMENT '商家id，第一主键；如为0则为系统创建',
        `type_code` varchar(50) NOT NULL COMMENT '字典类型编码，第二主键',
        `code` varchar(50) NOT NULL COMMENT '字典编码，第3主键',
        `name` varchar(200) DEFAULT NULL COMMENT '字典类型名称',
        `value` varchar(500) DEFAULT NULL COMMENT '扩展值',
        `ext_value1` varchar(500) DEFAULT NULL COMMENT '扩展值1',
        `ext_value2` varchar(500) DEFAULT NULL COMMENT '扩展值2',
        `ext_value3` varchar(500) DEFAULT NULL COMMENT '扩展值3',
        `sort` int(11) DEFAULT NULL COMMENT '排序',
        `remark` varchar(500) DEFAULT NULL COMMENT '备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`type_code`,`code`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='字典表\n标识一个字典';


CREATE TABLE `fee_rule` (
        `merchant_id` bigint(64) unsigned NOT NULL COMMENT '商家id',
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `name` varchar(50) DEFAULT '' COMMENT '名称',
        `description` varchar(256) DEFAULT '' COMMENT '明细描述',
        `status` tinyint(4) DEFAULT '-1' COMMENT '状态',
        `rule_version` int(8) DEFAULT '-1' COMMENT '规则的版本',

        `expire_time` int(32) unsigned DEFAULT NULL COMMENT '失效时间',
        `spike_price` int(32) unsigned DEFAULT NULL COMMENT '尖电价',
        `peak_price` int(32) unsigned DEFAULT NULL COMMENT '峰电价',
        `flat_price` int(32) unsigned DEFAULT NULL COMMENT '平电价',
        `valley_price` int(32) unsigned DEFAULT NULL COMMENT '谷电价',
        `spike_server_price` int(32) unsigned DEFAULT NULL COMMENT '尖时段服务费电价',
        `peak_server_price` int(32) unsigned DEFAULT NULL COMMENT '峰时段服务费电价',
        `flat_server_price` int(32) unsigned DEFAULT NULL COMMENT '平时段服务费电价',
        `valley_server_price` int(32) unsigned DEFAULT NULL COMMENT '谷时段服务费电价',

        `t0000_0030` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0030_0100` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0100_0130` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0130_0200` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0200_0230` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0230_0300` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0300_0330` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0330_0400` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0400_0430` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0430_0500` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0500_0530` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0530_0600` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0600_0630` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0630_0700` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0700_0730` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0730_0800` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0800_0830` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0830_0900` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0900_0930` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t0930_1000` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',

        `t1000_1030` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1030_1100` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1100_1130` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1130_1200` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1200_1230` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1230_1300` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1300_1330` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1330_1400` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1400_1430` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1430_1500` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1500_1530` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1530_1600` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1600_1630` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1630_1700` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1700_1730` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1730_1800` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1800_1830` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1830_1900` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1900_1930` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t1930_2000` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',

        `t2000_2030` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2030_2100` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2100_2130` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2130_2200` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2200_2230` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2230_2300` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2300_2330` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',
        `t2330_0000` tinyint(4) DEFAULT '-1' COMMENT '费率号（0：尖，1：峰，2：平，3： 谷）',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`merchant_id`,`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='计费规则表\n标识一个计费规则';


CREATE TABLE `user` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `type` tinyint(4) DEFAULT '-1' COMMENT '用户类型,0:个人用户；1：企业用户；-1：未知',
        `phone` varchar(256) DEFAULT '' COMMENT '手机，加唯一索引，可用作登陆',
        `user_name` varchar(256) DEFAULT '' COMMENT '用户名，加唯一索引，可用作登陆',
        `password` varchar(256) DEFAULT '' COMMENT '密码',
        `salt` varchar(32) DEFAULT '' COMMENT '密码加盐',
        `status` tinyint(4) DEFAULT NULL COMMENT '状态',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户表\n标识一个用户';


CREATE TABLE `user_detail` (
        `user_id` bigint(64) unsigned NOT NULL COMMENT '主键，等同于user表的id',
        `group_id` bigint(64) unsigned DEFAULT NULL COMMENT '团体所属，如个人用户属于某个企业用户，自关联于user_id',
        `real_name` varchar(256) DEFAULT '' COMMENT '真实姓名、企业名称',
        `nick_name` varchar(256) DEFAULT '' COMMENT '昵称',
        `id_number` varchar(256) DEFAULT '' COMMENT '对于个人用户，身份证号；对于企业用户，营业执照',
        `sex` tinyint(4) DEFAULT NULL COMMENT '性别,0:女；1：男；-1：未知',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户详情表\n标识一个用户';


CREATE TABLE `user_wechat` (
        `openid` varchar(256) NOT NULL COMMENT '第三方账户标识',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '用户id',
        `name` varchar(50) DEFAULT NULL COMMENT '第三方账户姓名',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`openid`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='微信第三方登录用户表\n标识一个微信第三方账户，关联用户';


CREATE TABLE `user_login_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
        `phone_models` varchar(256) DEFAULT '' COMMENT '手机型号',
        `device_id` varchar(256) DEFAULT '' COMMENT '设备ID',
        `imei` varchar(256) DEFAULT NULL COMMENT '设备标识',
        `ip` varchar(256) DEFAULT NULL COMMENT 'ip地址',
        `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
        `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户登陆记录表\n标识一次用户登陆行为';


CREATE TABLE `car` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
        `vin` varchar(256) DEFAULT '' COMMENT 'vin码',
        `license_number` varchar(256) DEFAULT '' COMMENT '车辆号码',
        `self_number` varchar(256) DEFAULT '' COMMENT '自编号',
        `brand` varchar(256) DEFAULT '' COMMENT '品牌',
        `category` varchar(256) DEFAULT '-1' COMMENT '车辆分类',
        `status` tinyint(4) DEFAULT NULL COMMENT '状态，-1表示删除',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='车辆表\n标识一个车辆';


CREATE TABLE `biz_team` (
        `id` bigint(64) NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT '0' COMMENT '商家ID',
        `name` varchar(64) DEFAULT NULL COMMENT '团体会员名称',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='团体表\n标识一个团体';


CREATE TABLE `order` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `sn` varchar(32) DEFAULT NULL COMMENT '订单编号',
        `status` tinyint(4) DEFAULT '-1' COMMENT ' 0：创建，用户扫码后立即创建一张订单；如果创建即确认，此状态则作为保留状态；
1：订单已确认，用户确认充电，充电枪状态检查，具备充电条件，平台将发送激活充电枪的指令，激活充电枪；
10：充电中，充电枪已插入车中，正在进行充电；如果不显示此状态，此状态可作为保留状态；
11：充电成功，充电已经结束；尚未支付；
12：充电失败，充电过程出现问题，未能正常结束；
20：已支付；
21：支付失败；
30：订单已作废，订单创建后，用户在进行充电前主动取消，如果创建即确认，此状态则作为保留状态；
31：订单已完成，成功的订单完结状态。
32：订单失败完成，充电失败、没有成功支付或其他原因而需要完结的订单； ',

        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `cg_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电枪id',
        `advanced_money` decimal(10,2) DEFAULT '0.00' COMMENT '预付金额，充值前预付的金额',
        `discount_money` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
        `real_money` decimal(10,2) DEFAULT '0.00' COMMENT '实际消费金额',
        `pay_money` decimal(10,2) DEFAULT '0.00' COMMENT '实际支付金额：实际消费金额-优惠金额',
        `refund_money` decimal(10,2) DEFAULT '0.00' COMMENT '退款金额：预付金额-实际支付金额',

        `car_vin` varchar(17) DEFAULT NULL COMMENT '车辆id',
        `remark` varchar(200) DEFAULT NULL COMMENT '订单处理备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
         PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='订单表\n标识一个订单';


CREATE TABLE `biz_order_info` (
            `id` bigint(64) unsigned NOT NULL COMMENT '主键',
            `cg_id` bigint(64) unsigned DEFAULT NULL COMMENT '充电枪id',
            `cp_id` bigint(128) unsigned DEFAULT NULL COMMENT '充电桩id，冗余字段',
            `sn` bigint(32) unsigned DEFAULT NULL COMMENT '订单编号',
            `work_status` tinyint(4) DEFAULT NULL COMMENT '工作状态',
            `connect_status` tinyint(4) DEFAULT NULL COMMENT '连接状态',
            `output_contactor_status` tinyint(4) DEFAULT NULL COMMENT '输出接触器状态',
            `electronic_lock_status` tinyint(4) DEFAULT NULL COMMENT '电子锁状态',
            `fault_number` tinyint(4) DEFAULT NULL COMMENT '故障码',
            `voltage` smallint(6) unsigned DEFAULT NULL COMMENT '充电电压',
            `current` smallint(6) unsigned DEFAULT NULL COMMENT '充电电流',
            `bms_voltage` smallint(6) unsigned DEFAULT NULL COMMENT 'bms需求电压',
            `bms_current` smallint(6) unsigned DEFAULT NULL COMMENT 'bms需求电流',
            `bms_model` tinyint(4) DEFAULT NULL COMMENT 'bms充电模式',
            `input_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '已充电量',
            `power` smallint(6) unsigned DEFAULT NULL COMMENT '充电功率',
            `a_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出A相电压',
            `a_current` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出A相电流',
            `b_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出B相电压',
            `b_current` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出B相电流',
            `c_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出C相电压',
            `c_current` smallint(6) unsigned DEFAULT NULL COMMENT '交流输出C相电流',
            `input_duration` int(8) DEFAULT NULL COMMENT '已充时长，分钟',
            `left_duration` int(8) DEFAULT NULL COMMENT '剩余充电时间预估，分钟',
            `cp_temperature` tinyint(4) DEFAULT NULL COMMENT '充电桩内部温度',
            `cg_temperature` tinyint(4) DEFAULT NULL COMMENT '枪头温度',
            `input_start_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '充电开始时电表读数',
            `input_now_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '当前电表读数',
            `now_soc` tinyint(4) DEFAULT NULL COMMENT '当前 SOC',
            `cell_highest_temperature` tinyint(4) DEFAULT NULL COMMENT '单体电池最高温度',
            `cell_highest_voltage` smallint(6) unsigned DEFAULT NULL COMMENT '单体电池最高电压',
            `fee_id` bigint(64) unsigned DEFAULT NULL COMMENT '计费模型 id',
            `spike_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '尖电量',
            `spike_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '尖电费',
            `spike_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '尖服务费',
            `peak_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '峰电量',
            `peak_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '峰电费',
            `peak_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '峰服务费',
            `flat_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '平电量',
            `flat_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '平电费',
            `flat_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '平服务费',
            `valley_quantity` smallint(6) unsigned DEFAULT NULL COMMENT '谷电量',
            `valley_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '谷电费',
            `valley_server_fee` smallint(6) unsigned DEFAULT NULL COMMENT '谷服务费',
            `now_quantity_fee` smallint(6) unsigned DEFAULT NULL COMMENT '当前充电电费',
            `now_total_fee` smallint(6) unsigned DEFAULT NULL COMMENT '当前充电总费用',

            `version` int(8) DEFAULT '0' COMMENT '版本',
            `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
            `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
            PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='充电上报的订单信息\n标识一次充电上报的订单信息';


CREATE TABLE `pay_user_wallet` (
        `user_id` bigint(64) unsigned NOT NULL COMMENT '主键，用户id',
        `income` decimal(10,2) DEFAULT NULL COMMENT '总收入额',
        `outcome` decimal(10,2) DEFAULT NULL COMMENT '总支出额',
        `balance` decimal(10,2) DEFAULT NULL COMMENT '可用余额',
        `frozen` decimal(10,2) DEFAULT NULL COMMENT '冻结金额',
        `check_sign` varchar(64) DEFAULT '' COMMENT '校验码',
        `status` tinyint(4) DEFAULT NULL COMMENT '状态:0禁用；1正常',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='个人钱包表\n标识一个用户钱包';


CREATE TABLE `pay_uw_deposit_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id,钱包id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `type` tinyint(4) DEFAULT '-1' COMMENT '支付类型；0：微信；1：支付宝',
        `sn_third` varchar(256) DEFAULT '' COMMENT '第三方流水标识',
        `pay_status` tinyint(4) DEFAULT '-1' COMMENT '状态；0：充值失败；1：充值成功',
        `check_status` tinyint(4) DEFAULT '-1' COMMENT '状态；0：未对帐；1：已对帐',
        `check_time` datetime DEFAULT NULL '对账时间',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='个人钱包充值记录表\n标识一次个人钱包充值行为';


CREATE TABLE `pay_uw_withdraw_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id,钱包id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `type` tinyint(4) DEFAULT '-1' COMMENT '支付类型；0：微信；1：支付宝',
        `sn_third` varchar(256) DEFAULT '' COMMENT '第三方流水标识',
        `pay_status` tinyint(4) DEFAULT '-1' COMMENT '支付状态；0：提现失败；1：提现成功',
        `check_status` tinyint(4) DEFAULT '-1' COMMENT '对账状态；0：未对帐；1：已对帐',
        `check_time` datetime DEFAULT NULL '对账时间',
        `audit_status` tinyint(4) DEFAULT '-1' COMMENT '审核状态；0：未审核；1：已审核',
        `audit_time` datetime DEFAULT NULL '审核时间',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='个人钱包提现记录表\n标识一次个人提现行为';


CREATE TABLE `pay_uw_pay_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id,钱包id',
        `sn` varchar(256) DEFAULT '' COMMENT '流水号',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `status` tinyint(4) DEFAULT '-1' COMMENT '状态；0：支付失败；1：支付成功',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='个人钱包支付记录表\n标识一次通过个人钱包支付的行为';


CREATE TABLE `pay_merchant_user_wallet` (
        `merchant_id` bigint(64) unsigned NOT NULL COMMENT '第一主键，商户id',
        `user_id` bigint(64) unsigned NOT NULL COMMENT '第二主键，用户户id',
        `income` decimal(10,2) DEFAULT NULL COMMENT '总收入额',
        `outcome` decimal(10,2) DEFAULT NULL COMMENT '总支出额',
        `balance` decimal(10,2) DEFAULT NULL COMMENT '可用余额',
        `frozen` decimal(10,2) DEFAULT NULL COMMENT '冻结金额',
        `check_sign` varchar(64) DEFAULT '' COMMENT '校验码',
        `status` tinyint(4) DEFAULT NULL COMMENT '状态:0禁用；1正常',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`merchant_id`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='会员钱包表\n标识一个会员钱包，记录账户余额';


CREATE TABLE `pay_muw_deposit_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `remark` varchar(256) DEFAULT NULL COMMENT '备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者，需要赋值商家管理员id',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='会员钱包充值记录表\n标识一次充值行为';


CREATE TABLE `pay_muw_withdraw_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `remark` varchar(256) DEFAULT NULL COMMENT '备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='会员钱包提现记录表\n标识一次会员钱包提现行为';


CREATE TABLE `pay_muw_pay_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
        `sn` varchar(256) DEFAULT '' COMMENT '流水号',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `status` tinyint(4) DEFAULT '-1' COMMENT '状态；0：支付失败；1：支付成功',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='会员钱包支付记录表\n标识一次通过会员钱包支付的行为';


CREATE TABLE `pay_merchant_team_wallet` (
        `team_id` bigint(64) unsigned NOT NULL COMMENT '团体id',
        `merchant_id` bigint(64) unsigned NOT NULL COMMENT '商户id',
        `income` decimal(10,2) DEFAULT NULL COMMENT '总收入额',
        `outcome` decimal(10,2) DEFAULT NULL COMMENT '总支出额',
        `balance` decimal(10,2) DEFAULT NULL COMMENT '可用余额',
        `frozen` decimal(10,2) DEFAULT NULL COMMENT '冻结金额',
        `check_sign` varchar(64) DEFAULT '' COMMENT '校验码',
        `status` tinyint(4) DEFAULT NULL COMMENT '状态:0禁用；1正常',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`merchant_id`,`user_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='团体钱包表\n标识一个团体钱包，记录账户余额';


CREATE TABLE `pay_mtw_deposit_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `team_id` bigint(64) unsigned NOT NULL COMMENT '团体id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `remark` varchar(256) DEFAULT NULL COMMENT '备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者，需要赋值商家管理员id',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='团体钱包充值记录表\n标识一次充值行为';


CREATE TABLE `pay_mtw_withdraw_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `team_id` bigint(64) unsigned NOT NULL COMMENT '团体id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `remark` varchar(256) DEFAULT NULL COMMENT '备注',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='团体钱包提现记录表\n标识一次会员钱包提现行为';


CREATE TABLE `pay_mtw_pay_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
        `team_id` bigint(64) unsigned NOT NULL COMMENT '团体id',
        `sn` varchar(256) DEFAULT '' COMMENT '流水号',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `status` tinyint(4) DEFAULT '-1' COMMENT '状态；0：支付失败；1：支付成功',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='团体钱包支付记录表\n标识一次通过会员钱包支付的行为';


CREATE TABLE `pay_order_pay_record` (
         `id` bigint(64) unsigned NOT NULL COMMENT '主键',
         `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
         `user_id` bigint(64) unsigned DEFAULT NULL COMMENT '用户id',
         `order_id` bigint(64) unsigned DEFAULT NULL COMMENT '订单id',
         `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
         `type` tinyint(4) DEFAULT '-1' COMMENT '支付类型；0个人钱包支付；1会员钱包支付；2为团体钱包',
         `sn_third` varchar(256) DEFAULT '' COMMENT '第三方流水标识',
         `status` tinyint(4) DEFAULT '-1' COMMENT '状态；0：支付失败；1：支付成功',

         `version` int(8) DEFAULT '0' COMMENT '版本',
         `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
         `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
         PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='订单支付记录表\n标识一次订单支付行为';


CREATE TABLE `pay_merchant_wallet` (
      `merchant_id` bigint(64) unsigned NOT NULL COMMENT '主键，商户id',
      `money` decimal(10,2) DEFAULT NULL COMMENT '账户余额',
      `status` tinyint(4) DEFAULT NULL COMMENT '状态',

      `version` int(8) DEFAULT '0' COMMENT '版本',
      `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
      `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`merchant_id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家钱包表\n标识一个商家钱包，用来记录商家的账户余额';


CREATE TABLE `pay_mw_withdraw_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id,钱包id',
        `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
        `type` tinyint(4) DEFAULT '-1' COMMENT '支付类型；0：微信；1：支付宝',
        `sn_third` varchar(256) DEFAULT '' COMMENT '第三方流水标识',
        `pay_status` tinyint(4) DEFAULT '-1' COMMENT '支付状态；0：提现失败；1：提现成功',
        `check_status` tinyint(4) DEFAULT '-1' COMMENT '对账状态；0：未对帐；1：已对帐',
        `check_time` datetime DEFAULT NULL '对账时间',
        `audit_status` tinyint(4) DEFAULT '-1' COMMENT '审核状态；0：未审核；1：已审核',
        `audit_time` datetime DEFAULT NULL '审核时间',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='商家钱包提现记录表\n标识一次商家提现行为';


CREATE TABLE `sms_send_record` (
        `id` bigint(64) unsigned NOT NULL COMMENT '主键',
        `phone` varchar(64) DEFAULT NULL COMMENT '接收短信的手机号',
        `status` tinyint(4) DEFAULT '-1' COMMENT '短信发送的状态：1.发送成功，0.发送失败',

        `version` int(8) DEFAULT '0' COMMENT '版本',
        `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
        `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='短信发送记录表\n标识一次短信发送';


CREATE TABLE `message` (
            `merchant_id` bigint(64) unsigned DEFAULT NULL COMMENT '商户id',
            `id` bigint(64) unsigned NOT NULL COMMENT '主键',
            `content` varchar(64) DEFAULT NULL COMMENT '消息的内容',
            `status` tinyint(4) DEFAULT '-1' COMMENT '消息的状态：1.已读，0.未读',
            `type` tinyint(4) DEFAULT '-1' COMMENT '消息的类型：1.站内信,2.短信',
            `version` int(8) DEFAULT '0' COMMENT '版本',
            `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
            `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
            PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='消息表\n标识一次消息';


CREATE TABLE `msg_record` (
                `id` bigint(64) unsigned NOT NULL COMMENT '主键',
                `message_Id` bigint(64) unsigned DEFAULT NULL COMMENT '消息的Id',
                `status` tinyint(4) DEFAULT '-1' COMMENT '消息发送的状态：1.发送成功，0.发送失败',
                `version` int(8) DEFAULT '0' COMMENT '版本',
                `creator` bigint(64) unsigned DEFAULT NULL COMMENT '创建者',
                `updator` bigint(64) unsigned DEFAULT NULL COMMENT '更新者',
                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                PRIMARY KEY (`id`) USING BTREE
)  ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='短信消息记录表\n标识一次消息记录';


