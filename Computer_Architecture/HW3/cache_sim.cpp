#include <iostream>
#include <fstream>
#include <cstdlib>
#include <cmath>

class Entry {
    private:  
      bool valid;
      unsigned tag;
      int ref;
    
    public:
      Entry() : valid(0) {};
      ~Entry(){};
      void display(std::ofstream& outfile) {
        outfile << this->get_ref() << ": ";
      };
    
      void set_tag(int _tag) { tag = _tag; }
      int get_tag() { return tag; }
    
      void set_valid(bool _valid) { valid = _valid; }
      bool get_valid() { return valid; }
    
      void set_ref(int _ref) { ref = _ref; }
      int get_ref() { return ref; }
};
    
class Cache {
    private:
      int assoc;
      unsigned num_entries;
      int num_sets;
      Entry **entries;
      unsigned int counter = 0;
    
    public:
      Cache(int _num_entries, int _assoc) : num_entries(_num_entries), assoc(_assoc) {
        num_sets = std::log2(num_entries);
        entries = new Entry *[num_entries];
        for (int i = 0; i < num_entries; ++i) {
          entries[i] = new Entry[num_sets];
        }
      };
      ~Cache() {
        for (int i = 0; i < num_sets; ++i)
          delete[] entries[i];
        delete[] entries; 
      };
      
      int get_index(unsigned long addr) {
        return addr % num_sets;
      };
      int get_tag(unsigned long addr) {
        return addr / num_sets;
      };
    
      unsigned long retrieve_addr(int way, int index) {
        return entries[index][way].get_ref();
      };
      
      bool hit(std::ofstream& outfile, unsigned long addr) {
        for(int i = 0; i < assoc; ++i) {
          if(entries[this->get_index(addr)][i].get_valid()) {
            if((entries[this->get_index(addr)][i].get_tag() == this->get_tag(addr)) && (this->retrieve_addr(i, this->get_index(addr)) == addr)) {
              entries[this->get_index(addr)][i].display(outfile);
              outfile << "HIT";
              return true;
            }
          } else {
            counter = i;
            break;
          }
        }
        this->update(outfile, addr);
        return false;
      };
    
      void update(std::ofstream& outfile, unsigned long addr) {
        entries[this->get_index(addr)][counter].set_tag(this->get_tag(addr));
        entries[this->get_index(addr)][counter].set_ref(addr);
        entries[this->get_index(addr)][counter].set_valid(1);
        entries[this->get_index(addr)][counter].display(outfile);
        outfile << "MISS";
        counter = (counter + 1) % assoc;
      };
};

const int MAX_SIZE = 1000;

int main(int argc, char *argv[]) {
    if (argc != 4) {
      std::cout << "Error: missing or too many arguments\n";
      std::cout << "Sample: ./cache_sim num_entries associativity memory_reference_file" << std::endl;
    }

    unsigned num_entries = atoi(argv[1]);
    unsigned associativity = atoi(argv[2]);
    std::string userFile = argv[3];

    Cache CACHE(num_entries, associativity);

    std::string input_file = userFile;
    std::string output_file = userFile + "_output";
    
    std::ofstream output;
    std::ifstream input;
    input.open(input_file);
    if(!input.is_open()) {
      std::cerr << "Error opening file: " << input_file << ". Exiting..." << std::endl;
      exit(0);
    };

    unsigned long *nums = new unsigned long[MAX_SIZE];
    int count = 0;
    while (!input.eof() && count < MAX_SIZE) {
      input >> nums[count];
      count++;
    }
    input.close();

    output.open(output_file);
    for(int i = 0; i < count; i++) {
      CACHE.hit(output, nums[i]);
      output << "\n";
    }

    output.close();
    return 0;
}