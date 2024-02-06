#include<iostream>
#include <windows.h>

using namespace std;

int main(int argc, char* argv[]) {
    if (argc > 1) {
        char* in = argc[1];
        char* console = "-console";
        char* version = "-version";

        wchar_t fullPath[MAX_PATH];
        wchar_t relativePath[] = L"your_relative_path"; // 确保使用宽字符串
        wchar_t currentDirectory[MAX_PATH];

        // 获取当前工作目录
        GetCurrentDirectory(MAX_PATH, currentDirectory);
        // 构建相对路径
        wcscat(currentDirectory, L"\\");
        wcscat(currentDirectory, relativePath);
        GetFullPathName(currentDirectory, MAX_PATH, fullPath, NULL);

        if (in == console) {
            system("cd " << currentDirectory << "\\release && " << currentDirectory << "\\..\\jdk\\bin\\kylin -jar kylin_language.jar " << in)
        }
        if (in == version) {
            system("cd " << currentDirectory << "\\release && " << currentDirectory << "\\..\\jdk\\bin\\kylin -jar kylin_language.jar " << in)
        }
        else {
            system("cd " << currentDirectory << "\\release && " << currentDirectory << "\\..\\jdk\\bin\\kylin -jar kylin_language.jar " << in)
        }
    } else {
        printf("\nKyLin Programming Language.\n 1. -version             Get Version Information.\n 2. -console              The kylin vm console.\n");
    }
    return 0;
}