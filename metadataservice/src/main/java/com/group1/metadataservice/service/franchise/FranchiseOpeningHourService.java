package com.group1.metadataservice.service.franchise;

import com.group1.metadataservice.model.dto.franchise.request.UpdateOpeningHoursRequest;
import com.group1.metadataservice.model.dto.franchise.response.OpeningHourResponse;

import java.util.UUID;

public interface FranchiseOpeningHourService {

    OpeningHourResponse updateOpeningHours(UUID franchiseId, UpdateOpeningHoursRequest request);

}
