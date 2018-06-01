@echo off
setlocal ENABLEEXTENSIONS
set ME=%~n0
set PARENT=%~dp0
:: Start custom settings. 
:: In most usecases, only the 4 settings below need to be changed:
set userid=
set password=
set space=
set org=
:: End custom settings
set svcKeyName=user0
set svc[0]=tone_analyzer lite ta0
set svc[1]=natural-language-understanding free nlu0
set svc[2]=discovery lite dsc0
set svc[3]=watson_vision_combined lite wvc0
set dscName=dsc0
set dscEnv=env0
set dscColl=coll0
set dscLang=en_us
set dscVer=2017-11-07

::Main
if -%1-==-- goto :displayUsage
if -%1-==-/- goto :displayUsage

set ARG=%1

if not %ARG:~0,1%==/ goto :displayUsage

if %ARG:~1,4%==lgb call :loginGB
if %ARG:~1,4%==lus call :loginUS
if %ARG:~1,4%==lde call :loginDE
if %ARG:~1,3%==lo call :logout
if %ARG:~1,3%==cs call :createService
if %ARG:~1,3%==us (
	if -%2-==-- (
		goto :displayUsage
	) else (
		call :unbindService %2
	)
)
if %ARG:~1,3%==ds call :removeService
if %ARG:~1,3%==cc call :createDiscoveryCollection
if %ARG:~1,3%==dc call :removeDiscoveryCollection
if %ARG:~1,3%==ca (
	call :createService
	call :createDiscoveryCollection
)
if %ARG:~1,3%==da (
	call :removeDiscoveryCollection
	call :removeService
)
if %ARG:~1,2%==t call :test

if %ARG:~1,2%==h goto :displayUsage

:epilogue
pause
exit /b
::End of Main

:displayUsage
	@echo:
	@echo %me% ^<command line options^>
	@echo:
	@echo Description: This command line tool manage a Cloud Foundry environment.
	@echo:
	@echo Parameter list:
	@echo:
	@echo /lgb			Login United Kingdom Region
	@echo /lus			Login US South Region  
	@echo /lde			Login Germany Region
	@echo /lo			Logout
	@echo /cs			Create services
	@echo /us ^<service instance^>	Unbind services
	@echo /ds			Delete services
	@echo /cc			Create Discovery service collection
	@echo /dc			Delete Discovery service collection
	@echo:
	@echo /ca			Create all
	@echo /da			Delete all
	@echo:
	@echo /h			Display Usage
	@echo:  
	goto :epilogue
:endDisplayUsage
exit /b

:loginGB
	cf l -a https://api.eu-gb.bluemix.net -u %userid% -p %password% --skip-ssl-validation -s %space% -o %org%
::	cf l -a https://api.eu-gb.bluemix.net -u %userid% --skip-ssl-validation -s %space% -o %org%
:endLoginGB
exit /b

:loginDE
	cf l -a https://api.eu-de.bluemix.net -u %userid% -p %password% --skip-ssl-validation -s %space% -o %org%
::	cf l -a https://api.eu-de.bluemix.net -u %userid% --skip-ssl-validation -s %space% -o %org%
:endLoginDE
exit /b

:loginUS
	cf l -a https://api.ng.bluemix.net -u %userid% -p %password% --skip-ssl-validation -s %space% -o %org%
::	cf l -a https://api.ng.bluemix.net -u %userid% --skip-ssl-validation -s %space% -o %org%
:endLoginUS
exit /b

:logout
	cf lo
:endLogout
exit /b

:createService
	setlocal ENABLEDELAYEDEXPANSION

	set /a svcCount=0
	:countLoop
	if defined svc[%svcCount%] (
		set /a svcCount+=1
		goto :countLoop
	)
	set /a svcCount=%svcCount%-1
	for /l %%i in (0,1,%svcCount%) do (
		set SVC=!svc[%%i]!
		for /f "tokens=1,2,3" %%a in ('echo !SVC!') do (
			set CMD="cf cs %%a %%b %%c"
			call :executeCmd !CMD!
			if %errorlevel% equ 0 (
				timeout 5 > null 2>&1 
				set CMD="cf csk %%c %svcKeyName%"
				call :executeCmd !CMD!
			)
		)
	)
:endCreateService
exit /b

:unbindService
setlocal ENABLEDELAYEDEXPANSION

	set SVC=%~1
	
	if "%SVC%"=="~1" goto :endUnbindService

	for /f "tokens=1,2 delims=:" %%a in ('cf service %SVC% ^| findstr /i "bound apps"') do set APPS=%%b

	set APPS=%APPS:~1%
	
	for %%a in (%APPS%) do (
		set APP=%%a
		cf us !APP! %SVC%
	)

:endUnbindService
exit /b

:removeService
setlocal ENABLEDELAYEDEXPANSION

	set /a svcCount=0
	:countLoop
	if defined svc[%svcCount%] (
		set /a svcCount+=1
		goto :countLoop
	)
	set /a svcCount=%svcCount%-1

	for /l %%i in (0,1,%svcCount%) do (
		set SVC=!svc[%%i]!
		for /f "tokens=1,2,3" %%a in ('echo !SVC!') do (
			set INST=%%c
			
			for /f "tokens=*" %%r in ('cf service !INST! ^| findstr "not found"') do set RESP=%%r
			
			if not -!RESP!-==-- ( 
				@echo !INST! does not exists.
			) else (
				call :unbindService !INST!
				set CMD="cf dsk !INST! %svcKeyName% -f"
				call :executeCmd !CMD!
				if %errorlevel% equ 0 (
					set CMD="cf ds !INST! -f"
					call :executeCmd !CMD!
				)
			)
		)
	)
	
:endRemoveService
exit /b

:createDiscoveryCollection
	setlocal ENABLEDELAYEDEXPANSION

	for /f "tokens=* skip=2" %%a in ('cf service-key %dscName% %svcKeyName%') do (
		set LINE=%%a
		set credential=!credential!!LINE!
	)
	
	if "-%credential%-"=="--" (
		@echo WARNING: No Discovery credential found.
		goto :endRemoveDiscoveryCollection
	)
	
	for /f "delims=" %%a in ('cmd /c "echo %credential% | jq -r .password"') do set PASSWORD=%%a
	for /f "delims=" %%a in ('cmd /c "echo %credential% | jq -r .username"') do set USERNAME=%%a
	for /f "delims=" %%a in ('cmd /c "echo %credential% | jq -r .url"') do set URL=%%a

	@echo %PASSWORD%
	@echo %USERNAME%
	@echo %URL%

	curl -X POST -u %USERNAME%:%PASSWORD% -H "Content-Type: application/json" -d "{\"name\": \"%dscEnv%\"}" "%URL%/v1/environments?version=%dscVer%"

	for /f "tokens=*" %%a in ('curl -u %USERNAME%:%PASSWORD% "%URL%/v1/environments?version=%dscVer%" ^| jq -r --arg ENV %dscEnv% ".environments[] | select(.name == $ENV) | .environment_id"') do set ENVID=%%a
	@echo %ENVID%

	curl -u %USERNAME%:%PASSWORD% %URL%/v1/environments/%ENVID%/configurations?version=%dscVer%

	for /f "tokens=*" %%a in ('curl -u %USERNAME%:%PASSWORD% "%URL%/v1/environments/%ENVID%/configurations?version=%dscVer%" ^| jq -r ".configurations[] | .configuration_id"') do set CONFID=%%a
	@echo %CONFID%

	curl -X POST -H "Content-Type: application/json" -u %USERNAME%:%PASSWORD% -d "{\"name\": \"%dscColl%\", \"configuration_id\":\"%CONFID%\" , \"language\": \"%dscLang%\"}" %URL%/v1/environments/%ENVID%/collections?version=%dscVer%

	@echo rc=%errorlevel%

:endCreateDiscoveryCollection
exit /b

:removeDiscoveryCollection
	setlocal ENABLEDELAYEDEXPANSION

	for /f "tokens=* skip=2" %%a in ('cf service-key %dscName% %svcKeyName%') do (
		set LINE=%%a
		set credential=!credential!!LINE!
	)
	
	if "-%credential%-"=="--" (
		@echo WARNING: No Discovery credential found.
		goto :endRemoveDiscoveryCollection
	)
	
	for /f "delims=" %%a in ('cmd /c "echo %credential% | jq -r .password"') do set PASSWORD=%%a
	for /f "delims=" %%a in ('cmd /c "echo %credential% | jq -r .username"') do set USERNAME=%%a
	for /f "delims=" %%a in ('cmd /c "echo %credential% | jq -r .url"') do set URL=%%a

	@echo %PASSWORD%
	@echo %USERNAME%
	@echo %URL%

	for /f "tokens=*" %%a in ('curl -u %USERNAME%:%PASSWORD% "%URL%/v1/environments?version=%dscVer%" ^| jq -r --arg ENV %dscEnv% ".environments[] | select(.name == $ENV) | .environment_id"') do set ENVID=%%a
	@echo %ENVID%

	for /f "tokens=*" %%a in ('curl -u %USERNAME%:%PASSWORD% "%URL%/v1/environments/%ENVID%/collections?version=%dscVer%" ^| jq -r ".collections[] | .collection_id"') do set COLLID=%%a
	@echo %COLLID%

	curl -u %USERNAME%:%PASSWORD% -X DELETE  "%URL%/v1/environments/%ENVID%/collections/%COLLID%?version=%dscVer%"

	curl -u %USERNAME%:%PASSWORD% -X DELETE "%URL%/v1/environments/%ENVID%?version=%dscVer%"

	@echo rc=%errorlevel%
	
:endRemoveDiscoveryCollection
exit /b

:executeCmd
	set CMD=%~1
	@echo %CMD%
	%CMD%
	@echo rc=%errorlevel%
:endExecuteCmd
exit /b

:test

::set VAR=app0,app1,app2,app3
set VAR=

set count=1

for %%a in (%VAR%) do (
	@echo %%a
	@echo !count!
	set /a count+=1
)

:endTest
exit /b
