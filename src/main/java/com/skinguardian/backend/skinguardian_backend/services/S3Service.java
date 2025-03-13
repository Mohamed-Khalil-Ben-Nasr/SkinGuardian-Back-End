package com.skinguardian.backend.skinguardian_backend.services;


import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;

import java.io.IOException;
import java.util.UUID;


@Service
public class S3Service {
    private final String bucketName = "skinguardian-images";
    private final Region region = Region.US_EAST_2;

    private final S3Client s3Client;

    // initialize the S3 service
    public S3Service(){
        this.s3Client = S3Client.builder()
                .region(region)
                // will get credentials from iam role that is attached to the ec2 instance
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    public String getImageUrl(String fileName) {
        return "https://" + bucketName + ".s3." + region.id() + ".amazonaws.com/" + fileName;
    }

    public String uploadImage(MultipartFile file){
        // create unique fileName
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        try{
            s3Client.putObject(
                    // build the putObject Request
                    PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                    // request body contains the image in bytes
                    RequestBody.fromBytes(file.getBytes())
            );

            // generate image s3 url and return it to user to include it in diagnosisDTO
            return getImageUrl(fileName);

        } catch (IOException e){
            throw new RuntimeException("Error uploading skin lesion image to S3",e);
        }

    }




}
