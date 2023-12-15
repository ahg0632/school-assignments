#ifndef PASSWORDMANAGER
#define PASSWORDMANAGER
#include <string>

class PasswordManager {
    private:
        std::string username;
        std::string encrp_pwd;
        std::string encrypt(std::string);
        bool meetsCriteria(std::string) const;
    public:
        void setUsername(std::string);
        std::string getUsername() const;
        void setEncryptedPassword(std::string);
        std::string getEncryptedPassword() const;
        bool setNewPassword(std::string);
        bool authenticate(std::string);
        PasswordManager();
        PasswordManager(std::string, std::string);
};

#endif