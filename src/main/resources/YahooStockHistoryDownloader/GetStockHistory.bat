@echo off
setlocal enabledelayedexpansion
set /a counter=1
for /f "tokens=*" %%p in (ChromeExePath.txt) do (
	for /f "tokens=*" %%n in (HKStockList.txt) do (
		set "download=https://query1.finance.yahoo.com/v7/finance/download/"
		set "dlVariable=?period1=946915200&period2=1514217600&interval=1d&events=history&crumb="
		set "url=!download!%%n!dlVariable!%1%
		set line=%%p "!url!"
rem		echo %%n
rem		echo !line!
		!line!
		ping 127.0.0.1 -n 2 > nul
		set /a counter=!counter!+1
rem		echo !counter!
		set /a div=!counter!/20
		set /a mod=!counter!%%100
		if !mod! == 0 (
			echo !div!%% completed
rem			set /a counter=1
rem			echo !counter!
rem			ping 127.0.0.1 -n 6 > nul
rem			taskkill /IM chrome.exe /T
rem			ping 127.0.0.1 -n 6 > nul
		)
	)
)
echo 100%% completed