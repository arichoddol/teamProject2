package org.spring.backendspring.API.marathonapi.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.spring.backendspring.API.marathonapi.repository.MarathonRepository;
import org.spring.backendspring.API.marathonapi.service.MarathonService;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class MarathonServiceImpl implements MarathonService {

    private final MarathonRepository marathonRepository;
    private final String API_KEY = "2399ece1ae2a664bdd55a849c06626fe9921df74d32f13eb72"; // API 키는 그대로 사용

    @PostConstruct
    public void loadData() {
        // ... (이전과 동일한 API 데이터 로드 로직 유지)
        try {
             String apiUrl = "https://api.odcloud.kr/api/15138980/v1/uddi:eedc77c5-a56b-4e77-9c1d-9396fa9cc1d3?page=1&perPage=50&serviceKey=" + API_KEY;

             URL url = new URL(apiUrl);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setRequestMethod("GET");
             conn.setRequestProperty("Content-Type", "application/json");

             BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = br.readLine()) != null) sb.append(line);
             br.close();

             JSONObject json = new JSONObject(sb.toString());
             JSONArray data = json.getJSONArray("data");

             for (int i = 0; i < data.length(); i++) {
                 JSONObject item = data.getJSONObject(i);
                 Marathon m = Marathon.builder()
                         .name(item.optString("대회명"))
                         .date(item.optString("대회일시"))
                         .location(item.optString("대회장소"))
                         .category(item.optString("종목"))
                         .host(item.optString("주최"))
                         .build();
                 marathonRepository.save(m);
             }

             System.out.println("✅ 마라톤 데이터 " + data.length() + "개 저장 완료");

         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    @Override
    public List<Marathon> findAll() {
        return marathonRepository.findAll();
    }

    // ⭐️ [추가] 검색 및 페이징 로직 구현
    @Override
    public Page<Marathon> findMarathons(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String searchPattern = "%" + searchTerm.trim() + "%";
            // MarathonRepository에 검색 조건(대회명, 장소 등)을 위한 메서드가 필요합니다.
            // 여기서는 'name'과 'location' 기준으로 검색하는 메서드를 호출한다고 가정합니다.
            return marathonRepository.findByNameContainingOrLocationContaining(searchPattern, searchPattern, pageable);
        }
        // 검색어가 없으면 전체 리스트 페이징
        return marathonRepository.findAll(pageable);
    }
}