#pragma once

#include <random>
#include <fstream>
#include <map>
#include <utility>
#include <iostream>
#include <algorithm>

// Borrowed from Borodun
std::string generateName() {
    std::ifstream names("./names.txt");
    long lineCount = std::count(std::istreambuf_iterator<char>(names), std::istreambuf_iterator<char>(), '\n');
    names.seekg(0);

    std::random_device dev;
    std::mt19937 rng(dev());
    std::uniform_int_distribution<std::mt19937::result_type> distrib(1, lineCount);

    std::string name;
    unsigned long count = distrib(rng);
    for (int i = 0; i < count; ++i) {
        getline(names, name);
    }

    char hexStr[10];
    snprintf(hexStr, 10, "%lX", count);
    return name + "-" + hexStr;
}

class RunningClientsList {
private:
    std::string ID;
    const int timeoutSeconds = 30;
    struct client {
        time_t lastSeen;
        std::string addr;
    };
    std::map<std::string, client> copies;

public:
    void setID(std::string id) {
        ID = std::move(id);
    }

    void addClient(const std::string &addr, const std::string &id) {
        if (copies.find(id) != copies.end()) {
            copies.find(id)->second.lastSeen = time(nullptr);
        } else {
            client c {
                    time(nullptr),
                    addr,
            };
            copies[id] = c;
        }
    }

    void removeAFK() {
        for (const auto &el: copies) {
            if (time(nullptr) - el.second.lastSeen > timeoutSeconds) {
                copies.erase(el.first);
            }
        }
    }

    void showClientList() {
        system("clear");
        printf("ID is %s\n", ID.c_str());
        printf("%-15s ||%-24s\n", "ID", "Address");
        for (auto &fr: copies) {
            printf("%-15s ||%-24s\n", fr.first.c_str(), fr.second.addr.c_str());
        }
    }
};