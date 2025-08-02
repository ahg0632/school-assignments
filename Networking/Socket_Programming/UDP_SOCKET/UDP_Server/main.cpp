#include <WinSock2.h>
#include <WS2tcpip.h>
#include <iostream>
#include <fstream>

#pragma comment(lib, "ws2_32.lib")

//Change "INADDR_ANY" to a hardcoded address if desired
#define IPADDR INADDR_ANY
//Change number to any desired port number
#define PORT 8888


int main() {
    //Windows Socket
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    //Socket Creation
    SOCKET serverSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if(serverSocket == INVALID_SOCKET) {
        std::cerr << "Socket creation failed.\n";
        return 1;
    }

    //Bind Socket
    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = IPADDR;
    if(bind(serverSocket, (sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        std::cerr << "Bind Failed.\n";
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }

    //Receive Image
    std::ofstream imageFile("received_image.jpg", std::ios::binary);
    char buffer[2048];
    sockaddr_in clientAddr;
    int clientLength = sizeof(clientAddr);
    std::cout << "Receiving image....\n";
    
    while(true) {
        int bytesReceived = recvfrom(serverSocket, buffer, sizeof(buffer), 0, (sockaddr*)&clientAddr, &clientLength);
        if(bytesReceived <= 0) {
            std::cerr << "Connection lost or error occured.\n";
            break;
        }
        if(bytesReceived == 3 && std::string(buffer, 3) == "EOF") {
            std::cout << "Transmission Ended....\n";
            break;
        }
        imageFile.write(buffer, bytesReceived);
    }
    std::cout << "Imaged Successfully Received." << std::endl;

    //Cleanup
    imageFile.close();
    closesocket(serverSocket);
    WSACleanup();
    return 0;
}