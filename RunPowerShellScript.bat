echo off

IF [%1] == [] (
	SET isTestContainer=false
) ELSE (
	SET isTestContainer=%1
)

IF %isTestContainer%=="true" (
	start powershell.exe -windowstyle hidden "%~dp0DockerRunTestPostgreSQL.ps1" 
) ELSE (
	start powershell.exe -windowstyle hidden "%~dp0DockerRunPostgreSQL.ps1" 
)
