#include "Client.h"
#include <iostream>

int main(int argc, char *argv[]) {
    if (argc < 2) {
        std::cout << "Need ip address as an argument" << std::endl;
        return 1;
    }

    char buf[sizeof(in_addr)];
    const std::string ID = generateName();

    if (inet_pton(AF_INET, argv[1], buf) == 1) {
        Client client(argv[1], ID);
        std::cout << "Waiting for connections" << std::endl;
        client.start();
    } else {
        std::cout << "Invalid address" << std::endl;
        return 2;
    }
    return 0;
}
