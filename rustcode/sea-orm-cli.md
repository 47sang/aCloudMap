# 生成对象模型

- 安装工具
```shell
cargo install sea-orm-cli
```

- 准备数据库连接信息
```text
PostgreSQL: postgres://username:password@host:port/database_name

MySQL: mysql://username:password@host:port/database_name

SQLite: sqlite://./path/to/your/database.sqlite (相对路径) 
或 sqlite:///absolute/path/to/your/database.sqlite (绝对路径)
或 :memory: (内存数据库)
```
- 命令格式
```shell
sea-orm-cli generate entity -o <OUTPUT_DIR> -u <DATABASE_URL>
```
- 命令示例
```shell
sea-orm-cli generate entity -o src/entities -u sqlite://./rustdb.db
```