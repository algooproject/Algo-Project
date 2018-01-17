Step 1: Config the ChromeExePath.txt
Step 2: Browse https://hk.finance.yahoo.com/quote/0001.HK/history?p=0001.HK
Step 3: Right click 下載數據
Step 4: Click 複製連結網址
e.g. https://query1.finance.yahoo.com/v7/finance/download/0001.HK?period1=1513519199&period2=1516197599&interval=1d&events=history&crumb=EVn1Qy8s8hn
Step 5: Copy the value of variable "crumb". The value is "EVn1Qy8s8hn" in above example.
Step 6: Run GetStockHistory "crumb value"
e.g. GetStockHistory EVn1Qy8s8hn
Step 7: The files are saved in the default download folder of Chrome.