pub fn create_sql() -> String {
    format!("-- a_data_json definition
CREATE TABLE
  IF NOT EXISTS a_data_json (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  json TEXT, -- 个股数据
  section TEXT, -- 板块数据
  today TEXT, -- 数据日期
  creat_time TEXT -- 创建时间
);


-- a_holiday definition
CREATE TABLE
  IF NOT EXISTS a_holiday (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  holiday INTEGER, -- 是否为节假日
  name TEXT, -- 节日名称
  date TEXT -- 日期
);


-- a_info definition
CREATE TABLE
  IF NOT EXISTS a_info (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  code TEXT NOT NULL, -- 股票代码
  name TEXT NOT NULL, -- 名称
  bk_id TEXT, -- 板块id
  bk_name TEXT, -- 板块名称
  ej_id TEXT, -- 二级板块
  ej_name TEXT, -- 二级板块名称
  exchange TEXT -- 所属交易所
);


-- a_sw definition
CREATE TABLE
  IF NOT EXISTS a_sw (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  stock_code TEXT, -- 股票代码
  sw_code TEXT, -- 申万板块代码
  industry_name TEXT, -- 板块名称
  industry_type TEXT, -- 申万类型
  source TEXT -- 来源
);

-- a_sw_dict definition
CREATE TABLE
  IF NOT EXISTS a_sw_dict (
  code TEXT, -- 代码
  name TEXT, -- 名称
  type TEXT -- 类型
);

-- a_today definition
CREATE TABLE
  IF NOT EXISTS a_today (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT, -- 股票名称
  code TEXT, -- 股票代码
  total TEXT, -- 总市值
  price TEXT, -- 当前价
  increase TEXT, -- 涨跌幅%
  arr_value TEXT, -- 数据数组
  turnover TEXT, -- 换手率
  into_date TEXT, -- 上市日期
  today TEXT, -- 数据日期
  creat_time TEXT -- 创建时间
);")
}
