#include "PasswordManager.h"
#include <string>

//Private
std::string PasswordManager::encrypt(std::string buffer) {
    const int SHIFT = 37;
    std::string encryptedStr;

    for (std::size_t i=0; i < buffer.length(); i++) {
        char x = ((buffer[i]-33) + SHIFT) % 94 + 33;
        encryptedStr.push_back(x);
    };
    return encryptedStr;
};

bool PasswordManager::meetsCriteria(std::string check) const {
    const int MAX_LENGTH = 15;
    int counter = 0;
    bool upper = true;
    bool lower = true;
    bool num = true;
    bool sym = true;
    if (check.length() < MAX_LENGTH) {return false;}
    for (std::size_t i = 0; i < check.length(); i++) {
        if (isupper(check[i]) && upper) {counter++;  upper = !upper;}
        if (islower(check[i]) && lower) {counter++;  lower = !lower;}
        if (isdigit(check[i]) && num) {counter++;  num = !num;}
        if (ispunct(check[i]) && sym) {counter++;  sym = !sym;}
    };
    if (counter < 3) {
        return false;
    }else {
        return true;
    }
};


//Public
void PasswordManager::setUsername(std::string setNewUsr) {
    username = setNewUsr;
};

std::string PasswordManager::getUsername() const {
    return username;
};

void PasswordManager::setEncryptedPassword(std::string setEncrpPwd) {
    encrp_pwd = encrypt(setEncrpPwd);
};

std::string PasswordManager::getEncryptedPassword() const {
    return encrp_pwd;
};

bool PasswordManager::setNewPassword(std::string setPwd) {
    if (meetsCriteria(setPwd)) {
        setEncryptedPassword(setPwd);
        return true;
    } else {
        return false;
    }
};

bool PasswordManager::authenticate(std::string auth) {
    if (encrypt(auth) == encrp_pwd) {
        return true;
    } else {
        return false;
    }
};

PasswordManager::PasswordManager(std::string set_usr, std::string set_pwd) {
    setUsername(set_usr);
    setNewPassword(set_pwd);
};