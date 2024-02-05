#define BEGIN_TIMER(N) const auto start_##N = std::chrono::steady_clock::now();
#define END_TIMER(N, label)                                                                                   \
    const auto end_##N = std::chrono::steady_clock::now();                                                    \
    const auto diff_##N = end_##N - start_##N;                                                                \
    std::cout << std::chrono::duration_cast<std::chrono::milliseconds>(diff_##N).count() << " msec " << label \
              << std::endl;

#include <boost/iostreams/device/mapped_file.hpp>
#include <filesystem>
#include <fstream>
#include <future>
#include <iostream>
#include <string>
#include <vector>

const auto needle = "wax synthase";

std::vector<std::string> readAndPrintFileContent(const std::string& filePath) {
    std::vector<std::string> result;
    boost::iostreams::mapped_file mmap;
    mmap.open(filePath, boost::iostreams::mapped_file::readonly);
    if (mmap.is_open()) {
        std::string bufferString(mmap.const_data(), mmap.size());

        size_t pos = 0;
        while ((pos = bufferString.find(needle, pos)) != std::string::npos) {
            size_t lineStart = bufferString.rfind('\n', pos) + 1;
            size_t lineEnd = bufferString.find('\n', pos);
            result.push_back(bufferString.substr(lineStart, lineEnd - lineStart));
            pos = lineEnd;
        }

        mmap.close();
    }
    return result;
}

std::vector<std::string> readFilesConcurrently(const std::vector<std::string>& fileNames) {
    std::vector<std::future<std::vector<std::string>>> futures;
    for (const auto& fileName : fileNames) {
        futures.push_back(std::async(std::launch::async | std::launch::deferred, readAndPrintFileContent, fileName));
    }

    std::vector<std::string> result;
    for (auto& future : futures) {
        const auto& futureResult = future.get();
        for (const auto& line : futureResult) result.push_back(line);
    }

    return result;
}

std::vector<std::string> findAndCategorizeFiles(const std::string& path) {
    std::vector<std::string> files;
    for (const auto& entry : std::filesystem::recursive_directory_iterator(path)) {
        if (std::filesystem::is_regular_file(entry.path())) {
            files.push_back(entry.path().string());
        }
    }
    return readFilesConcurrently(files);
}

int main() {
    BEGIN_TIMER(1)
    const auto path = "../genomas/";
    auto lines = findAndCategorizeFiles(path);
    END_TIMER(1, " elapsed")
    std::cout << lines.size() << " lines found" << std::endl;
    if (lines.size() != 139) {
        throw std::runtime_error("unexpected result count");
    }
    return 0;
}