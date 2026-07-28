# A-Share Market Treemap

## September 30, 2024: A Colorful Display of A-Shares

[![Data Visualization](./img/1.jpg)](./img/1.jpg)

[Market Sector Classification Treemap](https://47sang.github.io/) 


[![Data Visualization](./img/2.jpg)](./img/2.jpg)

[Market Capitalization Sorting Treemap](https://47sang.github.io/sort) 


[![Data Visualization](./img/3.jpg)](./img/3.jpg)

[Sector Percentage Change Treemap](https://47sang.github.io/section) 

[![Data Visualization](./img/4.jpg)](./img/4.jpg)

[Secondary Sector Positive/Negative Bar Chart](https://47sang.github.io/sectionBar) 

# Data Workflow

- First, obtain Shenwan sector information and the primary/secondary classifications of individual stocks, and store them in `a_sw`.
- Extract the Shenwan sector dictionary and store it in the `a_sw_dict` table.
- Get individual stock data, match them with sector information, and place them in the `a_info` table.
- The frontend retrieves stock data, cleans it, and stores it in the `a_today` table, adding or replacing data on a daily basis.
- Before returning data to the frontend, store today's API result in the `a_data_json` table to serve as a data cache.
- During market opening hours, data updates frequently; a time check is required to determine if data needs updating. There is no need to update data after closing or during weekends; cached data can be read directly from the database.

# Technical Support Contact

- QQ: 568261517
- WeChat: prodzhou
