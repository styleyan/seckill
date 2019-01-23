-- 秒杀成功明细表
create TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识: -1: 无效，0: 成功, 1: 已付款，2: 已发货',
`create_time` timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id, user_phone), /* 联合主键 */
KEY idx_create_time(create_time)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';