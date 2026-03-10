//package com.group1.metadataservice.controller;
//
//import com.group1.metadataservice.common.config.MetadataKeyConfig;
//import com.group1.metadataservice.common.response.ApiResponse;
//import com.group1.metadataservice.common.response.PageResponse;
//import com.group1.metadataservice.model.dto.EffectiveConfigDTO;
////import com.group1.metadataservice.service.EffectiveConfigService;
////import com.group1.platform.errorcode.ErrorCode;
////import com.group1.platform.exception.BusinessException;
////import jakarta.transaction.SystemException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//
//import java.util.regex.Pattern;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/metadata")
//public class ConfigController {
//
//    private final EffectiveConfigService effectiveConfigService;
//    private final MetadataKeyConfig metadataKeyConfig;
//    private Pattern keyPattern;
//
//    // GET /api/metadata/effective?key=timeout&region=VN
//    @GetMapping("/effective")
//    public ApiResponse<EffectiveConfigDTO> getEffectiveConfig(
//            @RequestParam String key,
//            @RequestParam(required = false) String region
//    ) {
//        // Validate key format
//        if (key == null || key.isBlank()) {
//            throw new BusinessException(ErrorCode.FR_001);
//        }
//
//        // Lazy compile pattern from config
//        if (keyPattern == null) {
//            keyPattern = Pattern.compile(metadataKeyConfig.getKeyPattern());
//        }
//
//        if (!keyPattern.matcher(key).matches()) {
//            throw new BusinessException(ErrorCode.SYS_001);
//        }
//
//        return ApiResponse.success(
//                effectiveConfigService.getEffectiveConfig(key, region)
//        );
//    }
//
//    @GetMapping
//    public ApiResponse<PageResponse<?>> getAllMetadata(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//
//        if (page < 0) page = 0;
//        if (size <= 0) size = 10;
//        if (size > 100) size = 100;
//
//        Pageable pageable = PageRequest.of(
//                page,
//                size,
//                Sort.by("createdAt").descending()
//        );
//
//        Page<?> result = effectiveConfigService.getAllBaseConfigs(pageable);
//
//        return ApiResponse.success(
//                PageResponse.from(result)
//        );
//    }
//}
