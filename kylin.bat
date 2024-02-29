@echo off
setlocal enabledelayedexpansion

:: 获取批处理文件的绝对路径
set "script_path=%~fullpath%"

:: 删除路径中的批处理文件名
set "script_dir=%script_path:~0,-%"

:: 检查jdk目录是否存在
if not exist "!script_dir!jdk\bin\java" (
    echo jdk/bin/java 命令不存在。
    goto :eof
)

:: 调用jdk/bin/kylin命令，并将命令行参数传递给它
start "" "!script_dir!jdk\bin\java" -XX:+DisableExplicitGC -jar "!script_dir!release\kylin_language.jar" %~1

goto :eof
