package com.group1.metadataservice.service;

import com.group1.metadataservice.model.dto.EffectiveConfigDTO;
import com.group1.metadataservice.model.entity.BaseConfig.BaseConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EffectiveConfigService {
    EffectiveConfigDTO getEffectiveConfig(String key, String regionCode);

    Page<BaseConfig> getAllBaseConfigs(Pageable pageable);
}
