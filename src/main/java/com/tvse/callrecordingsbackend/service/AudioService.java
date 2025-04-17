package com.tvse.callrecordingsbackend.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlobClientBase;
import com.azure.storage.common.sas.*;

import com.tvse.callrecordingsbackend.repository.CallRecordingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AudioService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public String generateSasAudioUrl(String blobName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            System.out.println("blobClient doesnot exists");
            return null;
        }

        // Generate SAS Token valid for 5 minutes
        BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);
        OffsetDateTime expiryTime = OffsetDateTime.now().plus(5, ChronoUnit.MINUTES);

        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permission)
                .setStartTime(OffsetDateTime.now())
                .setProtocol(SasProtocol.HTTPS_ONLY)
                .setContentDisposition("inline; filename=audio.mp3");

        String sasToken = blobClient.generateSas(sasValues);
        System.out.println("Sas token: "+sasToken);

        return blobClient.getBlobUrl() + "?" + sasToken;
    }


    @Autowired
    private CallRecordingsRepo repo;

    public String generateSasAudioUrlByCallId(String callId) {
        String blobName = repo.fetchBlobUrlByCallId(callId);
        if (blobName == null) return null;

        return generateSasAudioUrl(blobName);
    }

}

