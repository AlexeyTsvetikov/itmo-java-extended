package com.example.itmo.extended.clients;

import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yandexDisk", url = "disk.yandex.ru")
public interface RemoteClient {
    @GetMapping
    ResponseEntity<UserInfoResponse> getYaUser(@RequestParam Long id, @RequestHeader("api-key") String apiKey);
}
