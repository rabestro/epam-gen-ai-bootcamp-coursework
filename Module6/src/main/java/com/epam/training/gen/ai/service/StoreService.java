package com.epam.training.gen.ai.service;

import org.springframework.web.multipart.MultipartFile;

@FunctionalInterface
public interface StoreService {
    boolean save(MultipartFile file);
}
