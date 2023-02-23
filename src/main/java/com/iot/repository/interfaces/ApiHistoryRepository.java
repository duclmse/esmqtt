package com.iot.repository.interfaces;

import com.iot.model.request.ApiHistoryRequest;
import com.iot.model.response.ApiHistory;
import org.springframework.http.HttpMethod;

import java.util.List;

public interface ApiHistoryRepository {

    int save(long ts, HttpMethod method, String endpoint, String body);

    List<ApiHistory> getHistory(ApiHistoryRequest request);
}
