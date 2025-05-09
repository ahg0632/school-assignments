



#include <iostream>
#include <aws/core.aws.h>
#include <aws/s3/S3Client.h>
#include <aws/s3/model/PutObjectRequest.h>
#include <aws/s3/model/GetObjectRequest.h>
#include <fstream>

const std::string BUCKET_NAME = "example-bucket-school";
const std::string REGION = "us-east-2";

// Upload Object to S3 Bucket
void Upload_File_to_S3(const std::string& file_name, const std::string& s3_object_key) {
    Aws::S3::S3Client s3_client;

    // Put method for object request object
    Aws::S3::Model::PutObjectRequest object_request;
    object_request.WithBucket(BUCKET_NAME.c_str().WithKey(s3_object_key.c_str()));

    // Load file into a stream
    shared_ptr<Aws::IOStream> input_data = MakeShared<Aws::FStream>("SampleAllocationTag", file_name.c_str(), ios_base::in|ios_base::binary);

    // set body of request file
    Object_request.SetBody(input_data);

    // Put Object in S3 bucket
    auto put_object_outcome = s3_client.putObject(object_request);
    if(put_object_outcome.IsSuccess()) {
        std::cout << "Successfully Uploaded" << std::endl;
    } else {
        std::cout << "ERROR: Not Uploaded" << std::endl;
    }

}

// Download object from S3 Bucket
void Download_File_from_S3(const std::string& s3_object_key, downloaded_file_name) {
    Aws::s3::S3Client s3_client;

    // Get Method
    Aws::S3::Model::GetObjectRequest object_request;
    object_request.WithBucket(BUCKET_NAME.c_str().WithKey(s3_object_key.c_str()));

    // Get Object from S3
    auto get_object_outcome = s3_client.GetObject(object_request);
    if(get_object_outcome.IsSuccess()) {
        ofstream out_file(downlod_file_name, ios::binary);
        out_file << get_object_outcome.GetResult.GetBody().rdbuf();
        std::cout << "Successfully Downloaded" << std::endl;
    } else {
        std::cout << "" << std::endl;
    }
}

int main() {
    const std::string file_name = "test_sample.jpg";
    const std::string s3_object_key = "Key";
    const std::string downloaded_file_name = "download_1.jpg";
    bool exitCondition = true;

    // Add do-while loop to create menu
    do {
        std::cout << "Select option:\n"
                  << "[1]: Upload File\n"
                  << "[2]: Download File\n"
                  << "[3]: Exit\n";
        
        switch (std::cin) {
        case 1:
            // Upload file to S3
            Upload_File_to_S3(file_name, s3_object_key);
            continue;
        case 2:
            // Download file from S3
            Download_File_from_S3(s3_object_key, downloaded_file_name);
            continue;
        case 3:
            exitCondition = false;
            break;
        default:
            std::cout << "Invalid Input: Use Numbers 1-3\n";
            continue;
        }
    } while (exitCondition);
    std::endl;
    
    // API calls from AWS
    Aws::ShutdownAPI(options);

    return 0
}