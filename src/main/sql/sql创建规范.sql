CREATE TABLE `box_billing_info` (

  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,

  `partner_code` varchar(64)  NULL COMMENT '合作方标识',

  `partner_no` varchar(64)   NULL COMMENT '客户编号',

  `contract_no` varchar(64)  NULL COMMENT '合同编号',

  `product_start_time` timestamp  NULL COMMENT '产品生效时间',

  `product_end_time` timestamp NULL COMMENT '产品失效时间',

  `product_name` varchar(100)  NULL COMMENT '产品名称',

  `product_no` varchar(64)  NULL COMMENT '产品编号',

  `price` decimal(18,4)  NULL COMMENT '单价',

  `amount_invoke`  decimal(18,4)  NULL COMMENT '本期消费金额',

  `cur_debit_invoke` bigint(20) NULL COMMENT '本期新增调用量',

  `end_rest_product_invoke` bigint(20) NULL COMMENT '当期剩余调用量',

  `fmonth` int(11) NULL COMMENT '收入月份',

  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_cn_pn_fmonth` (`contract_no`,`product_no`,`fmonth`),

  KEY `indx_contract_no_fmonth` (`contract_no`,`fmonth`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月账单表';
