CREATE trigger charge_station_insert  after insert on `charge_station` for each row 
 begin
 insert into ods_charge_station(id,merchant_id,name,address,latitude,longitude,service_type,service_status,station_type,register_time,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.name,new.address,new.latitude,new.longitude,new.service_type,new.service_status,new.station_type,new.register_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger charge_station_update  after update on `charge_station` for each row 
 begin
 insert into ods_charge_station(id,merchant_id,name,address,latitude,longitude,service_type,service_status,station_type,register_time,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.name,new.address,new.latitude,new.longitude,new.service_type,new.service_status,new.station_type,new.register_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger charge_pile_insert  after insert on `charge_pile` for each row 
 begin
 insert into ods_charge_pile(id,cs_id,merchant_id,fee_rule_id,fee_rule_sn,cp_no,cp_name,contract_no,assets_no,install_addr,latitude,longitude,gun_num,cp_inf,spe_cp,cp_net,cp_status,fau_sta,cp_ip,cp_port,cp_sort,pur_ele_sch_id,service_status,product_time,register_time,per_res,v_hw,v_sw,v_prot,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.cs_id,new.merchant_id,new.fee_rule_id,new.fee_rule_sn,new.cp_no,new.cp_name,new.contract_no,new.assets_no,new.install_addr,new.latitude,new.longitude,new.gun_num,new.cp_inf,new.spe_cp,new.cp_net,new.cp_status,new.fau_sta,new.cp_ip,new.cp_port,new.cp_sort,new.pur_ele_sch_id,new.service_status,new.product_time,new.register_time,new.per_res,new.v_hw,new.v_sw,new.v_prot,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger charge_pile_update  after update on `charge_pile` for each row 
 begin
 insert into ods_charge_pile(id,cs_id,merchant_id,fee_rule_id,fee_rule_sn,cp_no,cp_name,contract_no,assets_no,install_addr,latitude,longitude,gun_num,cp_inf,spe_cp,cp_net,cp_status,fau_sta,cp_ip,cp_port,cp_sort,pur_ele_sch_id,service_status,product_time,register_time,per_res,v_hw,v_sw,v_prot,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.cs_id,new.merchant_id,new.fee_rule_id,new.fee_rule_sn,new.cp_no,new.cp_name,new.contract_no,new.assets_no,new.install_addr,new.latitude,new.longitude,new.gun_num,new.cp_inf,new.spe_cp,new.cp_net,new.cp_status,new.fau_sta,new.cp_ip,new.cp_port,new.cp_sort,new.pur_ele_sch_id,new.service_status,new.product_time,new.register_time,new.per_res,new.v_hw,new.v_sw,new.v_prot,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_cp_no_insert  after insert on `biz_cp_no` for each row 
 begin
 insert into ods_biz_cp_no(cp_no,merchant_id,status,version,creator,updator,create_time,update_time) 
values (new.cp_no,new.merchant_id,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_cp_no_update  after update on `biz_cp_no` for each row 
 begin
 insert into ods_biz_cp_no(cp_no,merchant_id,status,version,creator,updator,create_time,update_time) 
values (new.cp_no,new.merchant_id,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cp_register_record_insert  after insert on `cp_register_record` for each row 
 begin
 insert into ods_cp_register_record(id,cp_id,software_version,protocol_version,status,cp_time,id_number,imei,version,creator,updator,create_time,update_time) 
values (new.id,new.cp_id,new.software_version,new.protocol_version,new.status,new.cp_time,new.id_number,new.imei,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cp_register_record_update  after update on `cp_register_record` for each row 
 begin
 insert into ods_cp_register_record(id,cp_id,software_version,protocol_version,status,cp_time,id_number,imei,version,creator,updator,create_time,update_time) 
values (new.id,new.cp_id,new.software_version,new.protocol_version,new.status,new.cp_time,new.id_number,new.imei,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger charge_gun_insert  after insert on `charge_gun` for each row 
 begin
 insert into ods_charge_gun(id,cp_id,sn,version,creator,updator,create_time,update_time) 
values (new.id,new.cp_id,new.sn,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger charge_gun_update  after update on `charge_gun` for each row 
 begin
 insert into ods_charge_gun(id,cp_id,sn,version,creator,updator,create_time,update_time) 
values (new.id,new.cp_id,new.sn,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_quantity_record_insert  after insert on `cg_quantity_record` for each row 
 begin
 insert into ods_cg_quantity_record(id,cg_id,cp_id,sn,quantity,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.sn,new.quantity,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_quantity_record_update  after update on `cg_quantity_record` for each row 
 begin
 insert into ods_cg_quantity_record(id,cg_id,cp_id,sn,quantity,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.sn,new.quantity,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_event_record_insert  after insert on `cg_event_record` for each row 
 begin
 insert into ods_cg_event_record(id,cg_id,cp_id,event_type,event_id,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.event_type,new.event_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_event_record_update  after update on `cg_event_record` for each row 
 begin
 insert into ods_cg_event_record(id,cg_id,cp_id,event_type,event_id,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.event_type,new.event_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_server_event_record_insert  after insert on `cg_server_event_record` for each row 
 begin
 insert into ods_cg_server_event_record(id,cg_id,cp_id,event_type,event_id,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.event_type,new.event_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_server_event_record_update  after update on `cg_server_event_record` for each row 
 begin
 insert into ods_cg_server_event_record(id,cg_id,cp_id,event_type,event_id,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.event_type,new.event_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_status_insert  after insert on `cg_status` for each row 
 begin
 insert into ods_cg_status(id,cg_id,cp_id,sn,work_status,connect_status,output_contactor_status,electronic_lock_status,fault_number,voltage,current,bms_voltage,bms_current,bms_model,input_quantity,power,a_voltage,a_current,b_voltage,b_current,c_voltage,c_current,input_duration,left_duration,cp_temperature,cg_temperature,input_start_quantity,input_now_quantity,now_soc,cell_highest_temperature,cell_highest_voltage,fee_id,spike_quantity,spike_quantity_fee,spike_server_fee,peak_quantity,peak_quantity_fee,peak_server_fee,flat_quantity,flat_quantity_fee,flat_server_fee,valley_quantity,valley_quantity_fee,valley_server_fee,now_quantity_fee,now_total_fee,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.sn,new.work_status,new.connect_status,new.output_contactor_status,new.electronic_lock_status,new.fault_number,new.voltage,new.current,new.bms_voltage,new.bms_current,new.bms_model,new.input_quantity,new.power,new.a_voltage,new.a_current,new.b_voltage,new.b_current,new.c_voltage,new.c_current,new.input_duration,new.left_duration,new.cp_temperature,new.cg_temperature,new.input_start_quantity,new.input_now_quantity,new.now_soc,new.cell_highest_temperature,new.cell_highest_voltage,new.fee_id,new.spike_quantity,new.spike_quantity_fee,new.spike_server_fee,new.peak_quantity,new.peak_quantity_fee,new.peak_server_fee,new.flat_quantity,new.flat_quantity_fee,new.flat_server_fee,new.valley_quantity,new.valley_quantity_fee,new.valley_server_fee,new.now_quantity_fee,new.now_total_fee,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger cg_status_update  after update on `cg_status` for each row 
 begin
 insert into ods_cg_status(id,cg_id,cp_id,sn,work_status,connect_status,output_contactor_status,electronic_lock_status,fault_number,voltage,current,bms_voltage,bms_current,bms_model,input_quantity,power,a_voltage,a_current,b_voltage,b_current,c_voltage,c_current,input_duration,left_duration,cp_temperature,cg_temperature,input_start_quantity,input_now_quantity,now_soc,cell_highest_temperature,cell_highest_voltage,fee_id,spike_quantity,spike_quantity_fee,spike_server_fee,peak_quantity,peak_quantity_fee,peak_server_fee,flat_quantity,flat_quantity_fee,flat_server_fee,valley_quantity,valley_quantity_fee,valley_server_fee,now_quantity_fee,now_total_fee,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.sn,new.work_status,new.connect_status,new.output_contactor_status,new.electronic_lock_status,new.fault_number,new.voltage,new.current,new.bms_voltage,new.bms_current,new.bms_model,new.input_quantity,new.power,new.a_voltage,new.a_current,new.b_voltage,new.b_current,new.c_voltage,new.c_current,new.input_duration,new.left_duration,new.cp_temperature,new.cg_temperature,new.input_start_quantity,new.input_now_quantity,new.now_soc,new.cell_highest_temperature,new.cell_highest_voltage,new.fee_id,new.spike_quantity,new.spike_quantity_fee,new.spike_server_fee,new.peak_quantity,new.peak_quantity_fee,new.peak_server_fee,new.flat_quantity,new.flat_quantity_fee,new.flat_server_fee,new.valley_quantity,new.valley_quantity_fee,new.valley_server_fee,new.now_quantity_fee,new.now_total_fee,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_cg_order_record_insert  after insert on `biz_cg_order_record` for each row 
 begin
 insert into ods_biz_cg_order_record(id,merchant_id,cgNo,orderNo,start_soc,end_soc,start_time,end_time,start_quantity,end_quantity,fee_rule,spike_quantity,spike_price,spike_fee,spike_server_price,spike_server_fee,peak_quantity,peak_price,peak_fee,peak_server_price,peak_server_fee,flat_quantity,flat_price,flat_fee,flat_server_price,flat_server_fee,valley_quantity,valley_price,valley_fee,valley_server_price,valley_server_fee,total_quantity,total_fee,total_server_fee,total_amount,reason,car_vin,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.cgNo,new.orderNo,new.start_soc,new.end_soc,new.start_time,new.end_time,new.start_quantity,new.end_quantity,new.fee_rule,new.spike_quantity,new.spike_price,new.spike_fee,new.spike_server_price,new.spike_server_fee,new.peak_quantity,new.peak_price,new.peak_fee,new.peak_server_price,new.peak_server_fee,new.flat_quantity,new.flat_price,new.flat_fee,new.flat_server_price,new.flat_server_fee,new.valley_quantity,new.valley_price,new.valley_fee,new.valley_server_price,new.valley_server_fee,new.total_quantity,new.total_fee,new.total_server_fee,new.total_amount,new.reason,new.car_vin,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_cg_order_record_update  after update on `biz_cg_order_record` for each row 
 begin
 insert into ods_biz_cg_order_record(id,merchant_id,cgNo,orderNo,start_soc,end_soc,start_time,end_time,start_quantity,end_quantity,fee_rule,spike_quantity,spike_price,spike_fee,spike_server_price,spike_server_fee,peak_quantity,peak_price,peak_fee,peak_server_price,peak_server_fee,flat_quantity,flat_price,flat_fee,flat_server_price,flat_server_fee,valley_quantity,valley_price,valley_fee,valley_server_price,valley_server_fee,total_quantity,total_fee,total_server_fee,total_amount,reason,car_vin,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.cgNo,new.orderNo,new.start_soc,new.end_soc,new.start_time,new.end_time,new.start_quantity,new.end_quantity,new.fee_rule,new.spike_quantity,new.spike_price,new.spike_fee,new.spike_server_price,new.spike_server_fee,new.peak_quantity,new.peak_price,new.peak_fee,new.peak_server_price,new.peak_server_fee,new.flat_quantity,new.flat_price,new.flat_fee,new.flat_server_price,new.flat_server_fee,new.valley_quantity,new.valley_price,new.valley_fee,new.valley_server_price,new.valley_server_fee,new.total_quantity,new.total_fee,new.total_server_fee,new.total_amount,new.reason,new.car_vin,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_insert  after insert on `merchant` for each row 
 begin
 insert into ods_merchant(id,id_number,name,address,status,phone,version,creator,updator,create_time,update_time) 
values (new.id,new.id_number,new.name,new.address,new.status,new.phone,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_update  after update on `merchant` for each row 
 begin
 insert into ods_merchant(id,id_number,name,address,status,phone,version,creator,updator,create_time,update_time) 
values (new.id,new.id_number,new.name,new.address,new.status,new.phone,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_profit_shard_insert  after insert on `merchant_profit_shard` for each row 
 begin
 insert into ods_merchant_profit_shard(merchant_id,user_id,rate,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.rate,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_profit_shard_update  after update on `merchant_profit_shard` for each row 
 begin
 insert into ods_merchant_profit_shard(merchant_id,user_id,rate,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.rate,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dept_insert  after insert on `merchant_dept` for each row 
 begin
 insert into ods_merchant_dept(merchant_id,id,parent_id,name,description,order_num,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.parent_id,new.name,new.description,new.order_num,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dept_update  after update on `merchant_dept` for each row 
 begin
 insert into ods_merchant_dept(merchant_id,id,parent_id,name,description,order_num,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.parent_id,new.name,new.description,new.order_num,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dept_user_insert  after insert on `merchant_dept_user` for each row 
 begin
 insert into ods_merchant_dept_user(merchant_id,merchant_dept_id,user_id,role,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.merchant_dept_id,new.user_id,new.role,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dept_user_update  after update on `merchant_dept_user` for each row 
 begin
 insert into ods_merchant_dept_user(merchant_id,merchant_dept_id,user_id,role,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.merchant_dept_id,new.user_id,new.role,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_role_insert  after insert on `merchant_role` for each row 
 begin
 insert into ods_merchant_role(merchant_id,id,name,description,type,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.name,new.description,new.type,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_role_update  after update on `merchant_role` for each row 
 begin
 insert into ods_merchant_role(merchant_id,id,name,description,type,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.name,new.description,new.type,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_resource_insert  after insert on `merchant_resource` for each row 
 begin
 insert into ods_merchant_resource(id,parent_id,uri,code,name,description,is_menu,is_leaf,type,version,creator,updator,create_time,update_time) 
values (new.id,new.parent_id,new.uri,new.code,new.name,new.description,new.is_menu,new.is_leaf,new.type,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_resource_update  after update on `merchant_resource` for each row 
 begin
 insert into ods_merchant_resource(id,parent_id,uri,code,name,description,is_menu,is_leaf,type,version,creator,updator,create_time,update_time) 
values (new.id,new.parent_id,new.uri,new.code,new.name,new.description,new.is_menu,new.is_leaf,new.type,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_role_user_insert  after insert on `merchant_role_user` for each row 
 begin
 insert into ods_merchant_role_user(merchant_id,merchant_role_id,user_id,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.merchant_role_id,new.user_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_role_user_update  after update on `merchant_role_user` for each row 
 begin
 insert into ods_merchant_role_user(merchant_id,merchant_role_id,user_id,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.merchant_role_id,new.user_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_role_resource_insert  after insert on `merchant_role_resource` for each row 
 begin
 insert into ods_merchant_role_resource(merchant_id,merchant_role_id,merchant_resource_id,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.merchant_role_id,new.merchant_resource_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_role_resource_update  after update on `merchant_role_resource` for each row 
 begin
 insert into ods_merchant_role_resource(merchant_id,merchant_role_id,merchant_resource_id,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.merchant_role_id,new.merchant_resource_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_operate_record_insert  after insert on `merchant_operate_record` for each row 
 begin
 insert into ods_merchant_operate_record(merchant_id,user_id,id,uri,param,result,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.id,new.uri,new.param,new.result,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_operate_record_update  after update on `merchant_operate_record` for each row 
 begin
 insert into ods_merchant_operate_record(merchant_id,user_id,id,uri,param,result,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.id,new.uri,new.param,new.result,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_user_insert  after insert on `merchant_user` for each row 
 begin
 insert into ods_merchant_user(merchant_id,user_id,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_user_update  after update on `merchant_user` for each row 
 begin
 insert into ods_merchant_user(merchant_id,user_id,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dict_type_insert  after insert on `merchant_dict_type` for each row 
 begin
 insert into ods_merchant_dict_type(merchant_id,code,name,remark,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.code,new.name,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dict_type_update  after update on `merchant_dict_type` for each row 
 begin
 insert into ods_merchant_dict_type(merchant_id,code,name,remark,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.code,new.name,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dict_insert  after insert on `merchant_dict` for each row 
 begin
 insert into ods_merchant_dict(merchant_id,type_code,code,name,value,ext_value1,ext_value2,ext_value3,sort,remark,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.type_code,new.code,new.name,new.value,new.ext_value1,new.ext_value2,new.ext_value3,new.sort,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger merchant_dict_update  after update on `merchant_dict` for each row 
 begin
 insert into ods_merchant_dict(merchant_id,type_code,code,name,value,ext_value1,ext_value2,ext_value3,sort,remark,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.type_code,new.code,new.name,new.value,new.ext_value1,new.ext_value2,new.ext_value3,new.sort,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger fee_rule_insert  after insert on `fee_rule` for each row 
 begin
 insert into ods_fee_rule(merchant_id,id,name,description,status,rule_version,expire_time,spike_price,peak_price,flat_price,valley_price,spike_server_price,peak_server_price,flat_server_price,valley_server_price,t0000_0030,t0030_0100,t0100_0130,t0130_0200,t0200_0230,t0230_0300,t0300_0330,t0330_0400,t0400_0430,t0430_0500,t0500_0530,t0530_0600,t0600_0630,t0630_0700,t0700_0730,t0730_0800,t0800_0830,t0830_0900,t0900_0930,t0930_1000,t1000_1030,t1030_1100,t1100_1130,t1130_1200,t1200_1230,t1230_1300,t1300_1330,t1330_1400,t1400_1430,t1430_1500,t1500_1530,t1530_1600,t1600_1630,t1630_1700,t1700_1730,t1730_1800,t1800_1830,t1830_1900,t1900_1930,t1930_2000,t2000_2030,t2030_2100,t2100_2130,t2130_2200,t2200_2230,t2230_2300,t2300_2330,t2330_0000,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.name,new.description,new.status,new.rule_version,new.expire_time,new.spike_price,new.peak_price,new.flat_price,new.valley_price,new.spike_server_price,new.peak_server_price,new.flat_server_price,new.valley_server_price,new.t0000_0030,new.t0030_0100,new.t0100_0130,new.t0130_0200,new.t0200_0230,new.t0230_0300,new.t0300_0330,new.t0330_0400,new.t0400_0430,new.t0430_0500,new.t0500_0530,new.t0530_0600,new.t0600_0630,new.t0630_0700,new.t0700_0730,new.t0730_0800,new.t0800_0830,new.t0830_0900,new.t0900_0930,new.t0930_1000,new.t1000_1030,new.t1030_1100,new.t1100_1130,new.t1130_1200,new.t1200_1230,new.t1230_1300,new.t1300_1330,new.t1330_1400,new.t1400_1430,new.t1430_1500,new.t1500_1530,new.t1530_1600,new.t1600_1630,new.t1630_1700,new.t1700_1730,new.t1730_1800,new.t1800_1830,new.t1830_1900,new.t1900_1930,new.t1930_2000,new.t2000_2030,new.t2030_2100,new.t2100_2130,new.t2130_2200,new.t2200_2230,new.t2230_2300,new.t2300_2330,new.t2330_0000,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger fee_rule_update  after update on `fee_rule` for each row 
 begin
 insert into ods_fee_rule(merchant_id,id,name,description,status,rule_version,expire_time,spike_price,peak_price,flat_price,valley_price,spike_server_price,peak_server_price,flat_server_price,valley_server_price,t0000_0030,t0030_0100,t0100_0130,t0130_0200,t0200_0230,t0230_0300,t0300_0330,t0330_0400,t0400_0430,t0430_0500,t0500_0530,t0530_0600,t0600_0630,t0630_0700,t0700_0730,t0730_0800,t0800_0830,t0830_0900,t0900_0930,t0930_1000,t1000_1030,t1030_1100,t1100_1130,t1130_1200,t1200_1230,t1230_1300,t1300_1330,t1330_1400,t1400_1430,t1430_1500,t1500_1530,t1530_1600,t1600_1630,t1630_1700,t1700_1730,t1730_1800,t1800_1830,t1830_1900,t1900_1930,t1930_2000,t2000_2030,t2030_2100,t2100_2130,t2130_2200,t2200_2230,t2230_2300,t2300_2330,t2330_0000,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.name,new.description,new.status,new.rule_version,new.expire_time,new.spike_price,new.peak_price,new.flat_price,new.valley_price,new.spike_server_price,new.peak_server_price,new.flat_server_price,new.valley_server_price,new.t0000_0030,new.t0030_0100,new.t0100_0130,new.t0130_0200,new.t0200_0230,new.t0230_0300,new.t0300_0330,new.t0330_0400,new.t0400_0430,new.t0430_0500,new.t0500_0530,new.t0530_0600,new.t0600_0630,new.t0630_0700,new.t0700_0730,new.t0730_0800,new.t0800_0830,new.t0830_0900,new.t0900_0930,new.t0930_1000,new.t1000_1030,new.t1030_1100,new.t1100_1130,new.t1130_1200,new.t1200_1230,new.t1230_1300,new.t1300_1330,new.t1330_1400,new.t1400_1430,new.t1430_1500,new.t1500_1530,new.t1530_1600,new.t1600_1630,new.t1630_1700,new.t1700_1730,new.t1730_1800,new.t1800_1830,new.t1830_1900,new.t1900_1930,new.t1930_2000,new.t2000_2030,new.t2030_2100,new.t2100_2130,new.t2130_2200,new.t2200_2230,new.t2230_2300,new.t2300_2330,new.t2330_0000,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_insert  after insert on `user` for each row 
 begin
 insert into ods_user(id,type,phone,user_name,password,salt,status,version,creator,updator,create_time,update_time) 
values (new.id,new.type,new.phone,new.user_name,new.password,new.salt,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_update  after update on `user` for each row 
 begin
 insert into ods_user(id,type,phone,user_name,password,salt,status,version,creator,updator,create_time,update_time) 
values (new.id,new.type,new.phone,new.user_name,new.password,new.salt,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_detail_insert  after insert on `user_detail` for each row 
 begin
 insert into ods_user_detail(user_id,group_id,real_name,nick_name,id_number,sex,version,creator,updator,create_time,update_time) 
values (new.user_id,new.group_id,new.real_name,new.nick_name,new.id_number,new.sex,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_detail_update  after update on `user_detail` for each row 
 begin
 insert into ods_user_detail(user_id,group_id,real_name,nick_name,id_number,sex,version,creator,updator,create_time,update_time) 
values (new.user_id,new.group_id,new.real_name,new.nick_name,new.id_number,new.sex,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_wechat_insert  after insert on `user_wechat` for each row 
 begin
 insert into ods_user_wechat(openid,user_id,name,version,creator,updator,create_time,update_time) 
values (new.openid,new.user_id,new.name,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_wechat_update  after update on `user_wechat` for each row 
 begin
 insert into ods_user_wechat(openid,user_id,name,version,creator,updator,create_time,update_time) 
values (new.openid,new.user_id,new.name,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_login_record_insert  after insert on `user_login_record` for each row 
 begin
 insert into ods_user_login_record(id,user_id,phone_models,device_id,imei,ip,latitude,longitude,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.phone_models,new.device_id,new.imei,new.ip,new.latitude,new.longitude,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger user_login_record_update  after update on `user_login_record` for each row 
 begin
 insert into ods_user_login_record(id,user_id,phone_models,device_id,imei,ip,latitude,longitude,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.phone_models,new.device_id,new.imei,new.ip,new.latitude,new.longitude,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger car_insert  after insert on `car` for each row 
 begin
 insert into ods_car(id,user_id,vin,license_number,self_number,brand,category,status,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.vin,new.license_number,new.self_number,new.brand,new.category,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger car_update  after update on `car` for each row 
 begin
 insert into ods_car(id,user_id,vin,license_number,self_number,brand,category,status,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.vin,new.license_number,new.self_number,new.brand,new.category,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_team_insert  after insert on `biz_team` for each row 
 begin
 insert into ods_biz_team(id,merchant_id,name,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.name,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_team_update  after update on `biz_team` for each row 
 begin
 insert into ods_biz_team(id,merchant_id,name,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.name,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger order_insert  after insert on `order` for each row 
 begin
 insert into ods_order(id,sn,status,user_id,merchant_id,cg_id,advanced_money,discount_money,real_money,pay_money,refund_money,car_vin,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.sn,new.status,new.user_id,new.merchant_id,new.cg_id,new.advanced_money,new.discount_money,new.real_money,new.pay_money,new.refund_money,new.car_vin,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger order_update  after update on `order` for each row 
 begin
 insert into ods_order(id,sn,status,user_id,merchant_id,cg_id,advanced_money,discount_money,real_money,pay_money,refund_money,car_vin,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.sn,new.status,new.user_id,new.merchant_id,new.cg_id,new.advanced_money,new.discount_money,new.real_money,new.pay_money,new.refund_money,new.car_vin,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_order_info_insert  after insert on `biz_order_info` for each row 
 begin
 insert into ods_biz_order_info(id,cg_id,cp_id,sn,work_status,connect_status,output_contactor_status,electronic_lock_status,fault_number,voltage,current,bms_voltage,bms_current,bms_model,input_quantity,power,a_voltage,a_current,b_voltage,b_current,c_voltage,c_current,input_duration,left_duration,cp_temperature,cg_temperature,input_start_quantity,input_now_quantity,now_soc,cell_highest_temperature,cell_highest_voltage,fee_id,spike_quantity,spike_quantity_fee,spike_server_fee,peak_quantity,peak_quantity_fee,peak_server_fee,flat_quantity,flat_quantity_fee,flat_server_fee,valley_quantity,valley_quantity_fee,valley_server_fee,now_quantity_fee,now_total_fee,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.sn,new.work_status,new.connect_status,new.output_contactor_status,new.electronic_lock_status,new.fault_number,new.voltage,new.current,new.bms_voltage,new.bms_current,new.bms_model,new.input_quantity,new.power,new.a_voltage,new.a_current,new.b_voltage,new.b_current,new.c_voltage,new.c_current,new.input_duration,new.left_duration,new.cp_temperature,new.cg_temperature,new.input_start_quantity,new.input_now_quantity,new.now_soc,new.cell_highest_temperature,new.cell_highest_voltage,new.fee_id,new.spike_quantity,new.spike_quantity_fee,new.spike_server_fee,new.peak_quantity,new.peak_quantity_fee,new.peak_server_fee,new.flat_quantity,new.flat_quantity_fee,new.flat_server_fee,new.valley_quantity,new.valley_quantity_fee,new.valley_server_fee,new.now_quantity_fee,new.now_total_fee,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger biz_order_info_update  after update on `biz_order_info` for each row 
 begin
 insert into ods_biz_order_info(id,cg_id,cp_id,sn,work_status,connect_status,output_contactor_status,electronic_lock_status,fault_number,voltage,current,bms_voltage,bms_current,bms_model,input_quantity,power,a_voltage,a_current,b_voltage,b_current,c_voltage,c_current,input_duration,left_duration,cp_temperature,cg_temperature,input_start_quantity,input_now_quantity,now_soc,cell_highest_temperature,cell_highest_voltage,fee_id,spike_quantity,spike_quantity_fee,spike_server_fee,peak_quantity,peak_quantity_fee,peak_server_fee,flat_quantity,flat_quantity_fee,flat_server_fee,valley_quantity,valley_quantity_fee,valley_server_fee,now_quantity_fee,now_total_fee,version,creator,updator,create_time,update_time) 
values (new.id,new.cg_id,new.cp_id,new.sn,new.work_status,new.connect_status,new.output_contactor_status,new.electronic_lock_status,new.fault_number,new.voltage,new.current,new.bms_voltage,new.bms_current,new.bms_model,new.input_quantity,new.power,new.a_voltage,new.a_current,new.b_voltage,new.b_current,new.c_voltage,new.c_current,new.input_duration,new.left_duration,new.cp_temperature,new.cg_temperature,new.input_start_quantity,new.input_now_quantity,new.now_soc,new.cell_highest_temperature,new.cell_highest_voltage,new.fee_id,new.spike_quantity,new.spike_quantity_fee,new.spike_server_fee,new.peak_quantity,new.peak_quantity_fee,new.peak_server_fee,new.flat_quantity,new.flat_quantity_fee,new.flat_server_fee,new.valley_quantity,new.valley_quantity_fee,new.valley_server_fee,new.now_quantity_fee,new.now_total_fee,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_user_wallet_insert  after insert on `pay_user_wallet` for each row 
 begin
 insert into ods_pay_user_wallet(user_id,income,outcome,balance,frozen,check_sign,status,version,creator,updator,create_time,update_time) 
values (new.user_id,new.income,new.outcome,new.balance,new.frozen,new.check_sign,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_user_wallet_update  after update on `pay_user_wallet` for each row 
 begin
 insert into ods_pay_user_wallet(user_id,income,outcome,balance,frozen,check_sign,status,version,creator,updator,create_time,update_time) 
values (new.user_id,new.income,new.outcome,new.balance,new.frozen,new.check_sign,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_uw_deposit_record_insert  after insert on `pay_uw_deposit_record` for each row 
 begin
 insert into ods_pay_uw_deposit_record(id,user_id,money,type,sn_third,pay_status,check_status,check_time,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.money,new.type,new.sn_third,new.pay_status,new.check_status,new.check_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_uw_deposit_record_update  after update on `pay_uw_deposit_record` for each row 
 begin
 insert into ods_pay_uw_deposit_record(id,user_id,money,type,sn_third,pay_status,check_status,check_time,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.money,new.type,new.sn_third,new.pay_status,new.check_status,new.check_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_uw_withdraw_record_insert  after insert on `pay_uw_withdraw_record` for each row 
 begin
 insert into ods_pay_uw_withdraw_record(id,user_id,money,type,sn_third,pay_status,check_status,check_time,audit_status,audit_time,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.money,new.type,new.sn_third,new.pay_status,new.check_status,new.check_time,new.audit_status,new.audit_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_uw_withdraw_record_update  after update on `pay_uw_withdraw_record` for each row 
 begin
 insert into ods_pay_uw_withdraw_record(id,user_id,money,type,sn_third,pay_status,check_status,check_time,audit_status,audit_time,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.money,new.type,new.sn_third,new.pay_status,new.check_status,new.check_time,new.audit_status,new.audit_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_uw_pay_record_insert  after insert on `pay_uw_pay_record` for each row 
 begin
 insert into ods_pay_uw_pay_record(id,user_id,sn,money,status,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.sn,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_uw_pay_record_update  after update on `pay_uw_pay_record` for each row 
 begin
 insert into ods_pay_uw_pay_record(id,user_id,sn,money,status,version,creator,updator,create_time,update_time) 
values (new.id,new.user_id,new.sn,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_merchant_user_wallet_insert  after insert on `pay_merchant_user_wallet` for each row 
 begin
 insert into ods_pay_merchant_user_wallet(merchant_id,user_id,income,outcome,balance,frozen,check_sign,status,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.income,new.outcome,new.balance,new.frozen,new.check_sign,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_merchant_user_wallet_update  after update on `pay_merchant_user_wallet` for each row 
 begin
 insert into ods_pay_merchant_user_wallet(merchant_id,user_id,income,outcome,balance,frozen,check_sign,status,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.user_id,new.income,new.outcome,new.balance,new.frozen,new.check_sign,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_muw_deposit_record_insert  after insert on `pay_muw_deposit_record` for each row 
 begin
 insert into ods_pay_muw_deposit_record(id,merchant_id,user_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_muw_deposit_record_update  after update on `pay_muw_deposit_record` for each row 
 begin
 insert into ods_pay_muw_deposit_record(id,merchant_id,user_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_muw_withdraw_record_insert  after insert on `pay_muw_withdraw_record` for each row 
 begin
 insert into ods_pay_muw_withdraw_record(id,merchant_id,user_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_muw_withdraw_record_update  after update on `pay_muw_withdraw_record` for each row 
 begin
 insert into ods_pay_muw_withdraw_record(id,merchant_id,user_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_muw_pay_record_insert  after insert on `pay_muw_pay_record` for each row 
 begin
 insert into ods_pay_muw_pay_record(id,merchant_id,user_id,sn,money,status,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.sn,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_muw_pay_record_update  after update on `pay_muw_pay_record` for each row 
 begin
 insert into ods_pay_muw_pay_record(id,merchant_id,user_id,sn,money,status,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.sn,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_merchant_team_wallet_insert  after insert on `pay_merchant_team_wallet` for each row 
 begin
 insert into ods_pay_merchant_team_wallet(team_id,merchant_id,income,outcome,balance,frozen,check_sign,status,version,creator,updator,create_time,update_time) 
values (new.team_id,new.merchant_id,new.income,new.outcome,new.balance,new.frozen,new.check_sign,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_merchant_team_wallet_update  after update on `pay_merchant_team_wallet` for each row 
 begin
 insert into ods_pay_merchant_team_wallet(team_id,merchant_id,income,outcome,balance,frozen,check_sign,status,version,creator,updator,create_time,update_time) 
values (new.team_id,new.merchant_id,new.income,new.outcome,new.balance,new.frozen,new.check_sign,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mtw_deposit_record_insert  after insert on `pay_mtw_deposit_record` for each row 
 begin
 insert into ods_pay_mtw_deposit_record(id,merchant_id,team_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.team_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mtw_deposit_record_update  after update on `pay_mtw_deposit_record` for each row 
 begin
 insert into ods_pay_mtw_deposit_record(id,merchant_id,team_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.team_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mtw_withdraw_record_insert  after insert on `pay_mtw_withdraw_record` for each row 
 begin
 insert into ods_pay_mtw_withdraw_record(id,merchant_id,team_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.team_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mtw_withdraw_record_update  after update on `pay_mtw_withdraw_record` for each row 
 begin
 insert into ods_pay_mtw_withdraw_record(id,merchant_id,team_id,money,remark,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.team_id,new.money,new.remark,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mtw_pay_record_insert  after insert on `pay_mtw_pay_record` for each row 
 begin
 insert into ods_pay_mtw_pay_record(id,merchant_id,team_id,sn,money,status,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.team_id,new.sn,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mtw_pay_record_update  after update on `pay_mtw_pay_record` for each row 
 begin
 insert into ods_pay_mtw_pay_record(id,merchant_id,team_id,sn,money,status,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.team_id,new.sn,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_order_pay_record_insert  after insert on `pay_order_pay_record` for each row 
 begin
 insert into ods_pay_order_pay_record(id,merchant_id,user_id,order_id,money,type,sn_third,status,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.order_id,new.money,new.type,new.sn_third,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_order_pay_record_update  after update on `pay_order_pay_record` for each row 
 begin
 insert into ods_pay_order_pay_record(id,merchant_id,user_id,order_id,money,type,sn_third,status,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.user_id,new.order_id,new.money,new.type,new.sn_third,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_merchant_wallet_insert  after insert on `pay_merchant_wallet` for each row 
 begin
 insert into ods_pay_merchant_wallet(merchant_id,money,status,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_merchant_wallet_update  after update on `pay_merchant_wallet` for each row 
 begin
 insert into ods_pay_merchant_wallet(merchant_id,money,status,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.money,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mw_withdraw_record_insert  after insert on `pay_mw_withdraw_record` for each row 
 begin
 insert into ods_pay_mw_withdraw_record(id,merchant_id,money,type,sn_third,pay_status,check_status,check_time,audit_status,audit_time,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.money,new.type,new.sn_third,new.pay_status,new.check_status,new.check_time,new.audit_status,new.audit_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger pay_mw_withdraw_record_update  after update on `pay_mw_withdraw_record` for each row 
 begin
 insert into ods_pay_mw_withdraw_record(id,merchant_id,money,type,sn_third,pay_status,check_status,check_time,audit_status,audit_time,version,creator,updator,create_time,update_time) 
values (new.id,new.merchant_id,new.money,new.type,new.sn_third,new.pay_status,new.check_status,new.check_time,new.audit_status,new.audit_time,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger sms_send_record_insert  after insert on `sms_send_record` for each row 
 begin
 insert into ods_sms_send_record(id,phone,status,version,creator,updator,create_time,update_time) 
values (new.id,new.phone,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger sms_send_record_update  after update on `sms_send_record` for each row 
 begin
 insert into ods_sms_send_record(id,phone,status,version,creator,updator,create_time,update_time) 
values (new.id,new.phone,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger message_insert  after insert on `message` for each row 
 begin
 insert into ods_message(merchant_id,id,content,status,type,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.content,new.status,new.type,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger message_update  after update on `message` for each row 
 begin
 insert into ods_message(merchant_id,id,content,status,type,version,creator,updator,create_time,update_time) 
values (new.merchant_id,new.id,new.content,new.status,new.type,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger msg_record_insert  after insert on `msg_record` for each row 
 begin
 insert into ods_msg_record(id,message_Id,status,version,creator,updator,create_time,update_time) 
values (new.id,new.message_Id,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


CREATE trigger msg_record_update  after update on `msg_record` for each row 
 begin
 insert into ods_msg_record(id,message_Id,status,version,creator,updator,create_time,update_time) 
values (new.id,new.message_Id,new.status,new.version,new.creator,new.updator,new.create_time,new.update_time);
end;


