package com.skinguardian.backend.skinguardian_backend.services;


import com.skinguardian.backend.skinguardian_backend.dtos.DiagnosisDTO;
import com.skinguardian.backend.skinguardian_backend.dtos.DiagnosisMetadataDTO;
import com.skinguardian.backend.skinguardian_backend.entities.Diagnosis;
import com.skinguardian.backend.skinguardian_backend.repositories.DiagnosisRepository;
import com.skinguardian.backend.skinguardian_backend.repositories.UserRepository;
import com.skinguardian.backend.skinguardian_backend.security.WrongUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skinguardian.backend.skinguardian_backend.entities.User;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointRequest;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointResponse;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import org.json.JSONObject;


@Service
public class DiagnosisService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DiagnosisRepository diagnosisRepository;

    @Autowired
    S3Service s3Service;

    // initialize sagemaker runtime client
    // private final -> to make sure this method is only initialized once and then always reused
    private final SageMakerRuntimeClient runtimeClient = SageMakerRuntimeClient.builder()
            .region(Region.US_EAST_2)
            .build();

    // function used to upload image to s3 and then get back the url of the image in the s3 bucket to include it in the diagnosisDTO
    public String storeImageInS3(MultipartFile image) {
        // Assuming s3Service.uploadFile(file) returns the S3 URL
        return s3Service.uploadImage(image);
    }


    // function used to encode skinlesion image to base64
    // this preprocessing step is necessary because the model thats hosted in the aws sagemaker inference endpoint
    // needs the preprocessed image in base64 along with localization, sex, and age to do inference
    public String encodeImageToBase64(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    // build the JSON payload that im going to send to my sagemaker inference endpoint
    public String buildInferencePayload(String base64Image, DiagnosisMetadataDTO metadata) {
        JSONObject payload = new JSONObject();
        payload.put("image", base64Image);
        payload.put("sex", metadata.getSex());
        payload.put("localization", metadata.getLocalization());
        payload.put("age", metadata.getAge());
        return payload.toString();
    }

    public String callSageMakerEndpoint(String payload) {
        try {
            InvokeEndpointRequest request = InvokeEndpointRequest.builder()
                    .endpointName("skinguardian-classification-endpoint")
                    .contentType("application/json")
                    .body(SdkBytes.fromUtf8String(payload))
                    .build();

            InvokeEndpointResponse response = runtimeClient.invokeEndpoint(request);
            String responseBody = response.body().asUtf8String();
            return responseBody.trim();
        } catch (Exception e) {
            // Log and handle the exception appropriately
            throw new RuntimeException("Error calling SageMaker endpoint: " + e.getMessage(), e);
        }
    }

    public String processDiagnosis(UUID userId, DiagnosisMetadataDTO metadata, MultipartFile image) throws WrongUserException{
        try{
            // get the user
            Optional<User> maybeUser = userRepository.findById(userId);
            if(maybeUser.isEmpty())
                throw new WrongUserException();
            User user = maybeUser.get();

            // upload the image to s3 bucket and get back the image s3 url
            String imageUrl = storeImageInS3(image);

            // preprocess the image to prepare it for inference
            String base64Image = encodeImageToBase64(image);

            // build the inference payload
            String payload = buildInferencePayload(base64Image, metadata);

            // get the diagnosis result after calling the sagemaker inference endpoint
            String diagnosisResult = callSageMakerEndpoint(payload);

            // build the diagnosis entity after getting the result
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setUser(user);
            diagnosis.setDiagnosisResult(diagnosisResult);
            diagnosis.setSex(metadata.getSex());
            diagnosis.setLocalization(metadata.getLocalization());
            diagnosis.setAge(metadata.getAge());
            diagnosis.setImageUrl(imageUrl);
            diagnosisRepository.save(diagnosis);

            return diagnosisResult;

        } catch (Exception e){
            throw new RuntimeException("error processing diagnosis", e);
        }

    }

    public Diagnosis findById(UUID id){
        return diagnosisRepository.findById(id).get();
    }

}
