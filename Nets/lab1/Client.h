#pragma once

#include <sys/poll.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>

#include "RunningClientsList.h"

class Client {
private:
    int port = 1234;
    int bufSize = 32;

    RunningClientsList fl;
    std::string ID;
    std::string addr;
    int inSock, outSock;
    struct sockaddr_in bcAddr{};

public:
    Client(std::string ipAddr, std::string name) {
        addr = std::move(ipAddr);
        ID = std::move(name);

        int optVal = 1;
        inSock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

        setsockopt(inSock, SOL_SOCKET, SO_REUSEADDR, &optVal, sizeof optVal);

        struct sockaddr_in sockAddr{};
        sockAddr.sin_family = AF_INET;
        sockAddr.sin_port = htons(port);
        sockAddr.sin_addr.s_addr = htonl(INADDR_ANY);

        bind(inSock, (const struct sockaddr *) &sockAddr, sizeof(sockAddr));

        struct ip_mreq mreq{};
        inet_aton(addr.c_str(), &(mreq.imr_multiaddr));
        mreq.imr_interface.s_addr = htonl(INADDR_ANY);

        setsockopt(inSock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq));

        outSock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

        bcAddr.sin_family = AF_INET;
        bcAddr.sin_addr.s_addr = inet_addr(addr.c_str());
        bcAddr.sin_port = htons(port);
    }

    void start() {
        struct pollfd fd[1];
        fd[0].fd = inSock;
        fd[0].events = POLLIN;

        char buf[bufSize];
        char hostname[INET_ADDRSTRLEN];

        struct sockaddr_in someClient{};
        socklen_t len = sizeof(someClient);

        fl.setID(ID);
        while (true) {
            sendto(outSock, ID.c_str(), ID.length(), 0, (sockaddr*) &bcAddr, sizeof(bcAddr));
            fl.removeAFK();

            int ret = poll(fd, 1, 500);
            if (ret != 0) {
                long read = recvfrom(inSock, &buf, bufSize, MSG_WAITALL, (sockaddr*) &someClient, &len);
                buf[read] = '\0';
                std::string clientName(buf);
                if (clientName == ID || read < 1) {
                    continue;
                }
                getnameinfo((sockaddr *) &someClient, len, hostname, INET_ADDRSTRLEN, nullptr, 0, NI_NUMERICHOST);

                fl.addClient(hostname, clientName);
            } else {
                break;
            }
            fl.showClientList();
        }
    }
};