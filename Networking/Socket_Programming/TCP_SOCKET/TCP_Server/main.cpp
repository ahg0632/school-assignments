#include <WinSock2.h>
#include <WS2tcpip.h>
#include <iostream>
#include <fstream>
#include <cstdint>
#include <algorithm>

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
    SOCKET serverSocket = socket(AF_INET, SOCK_STREAM, 0);
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


    listen(serverSocket, 1);
    std::cout << "Server listening on port " << PORT << "....\n";


    //Accepting Client
    sockaddr_in clientAddr{};
    int clientSize = sizeof(clientAddr);
    SOCKET clientSocket = accept(serverSocket, (sockaddr*)&clientAddr, &clientSize);
    if(clientSocket == INVALID_SOCKET) {
        std::cerr << "Failed to Accept Socket.\n";
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    std::cout << "Connected to Client.\n";

    //Receive Image Size
    uint64_t imageSize = 0;
    int received = recv(clientSocket, (char*)&imageSize, sizeof(imageSize), 0);
    if(received != sizeof(imageSize)) {
        std::cerr << "Failed to receive image size.\n";
        closesocket(clientSocket);
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    std::cout << "Expecting image of size: " << imageSize << "bytes...\n";

    //Receive Image Entirely
    std::ofstream imageFile("received_image.jpg", std::ios::binary);
    char buffer[4096];
    uint64_t totalReceived = 0;
    while(totalReceived < imageSize) {
        using namespace std;
        int bytesToRead = min(sizeof(buffer), static_cast<size_t>(imageSize - totalReceived));
        int bytesReceived = recv(clientSocket, buffer, bytesToRead, 0);
        if(bytesReceived <= 0) {
            std::cerr << "Connection lost or error occured.\n";
            break;
        }
        imageFile.write(buffer, bytesReceived);
        totalReceived += bytesReceived;
    }

    imageFile.close();
    std::cout << "Image downloaded as 'received_image.jpg'.\n";
    
    //Cleanup
    closesocket(clientSocket);
    closesocket(serverSocket);
    WSACleanup();
    return 0;
}