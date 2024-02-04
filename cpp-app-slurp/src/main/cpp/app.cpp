#define BEGIN_TIMER(N) const auto start_##N = std::chrono::steady_clock::now();
#define END_TIMER(N, label)                                                                                   \
    const auto end_##N = std::chrono::steady_clock::now();                                                    \
    const auto diff_##N = end_##N - start_##N;                                                                \
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(diff_##N).count() << " msec " << label \
              << std::endl;

#include <filesystem>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

const auto needle = "wax synthase";

void readAndPrintFileContent(const std::string& filePath, std::vector<std::string>& acc) {
    std::ifstream file(filePath);
    if (file.is_open()) {
        file.seekg(0, file.end);
        int length = file.tellg();
        file.seekg(0, file.beg);

        char* buffer = new char[length + 1];
        buffer[length] = '\0';

        // read data as a block:
        file.read(buffer, length);

        std::string bufferString(buffer);

        size_t pos = 0;
        while ((pos = bufferString.find(needle, pos)) != std::string::npos) {
            size_t lineStart = bufferString.rfind('\n', pos) + 1;
            size_t lineEnd = bufferString.find('\n', pos);
            acc.push_back(bufferString.substr(lineStart, lineEnd - lineStart));
            pos = lineEnd;
        }

        delete[] buffer;
    }
}

void findAndCategorizeFiles(const std::string& path, std::vector<std::string>& acc) {
    for (const auto& entry : std::filesystem::recursive_directory_iterator(path)) {
        if (std::filesystem::is_regular_file(entry.path())) {
            readAndPrintFileContent(entry.path().string(), acc);
        }
    }
}

int main() {
    BEGIN_TIMER(1)
    const auto path = "../genomas/";
    std::vector<std::string> acc;
    findAndCategorizeFiles(path, acc);
    END_TIMER(1, " elapsed")
    std::cout << acc.size() << " lines found" << std::endl;
    if (acc.size() != 139) {
        throw std::runtime_error("unexpected result count");
    }
    return 0;
}