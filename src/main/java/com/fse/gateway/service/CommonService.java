package com.fse.gateway.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse.gateway.exception.CommonException;
import com.fse.gateway.response.ErrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class CommonService<T> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper ob;

    public T getAllTypeResponseRestTemplate(String uri, HttpEntity<T> request, Class className, HttpMethod method)
            throws CommonException {
        try {
            log.info("getAllTypeResponseRestTemplate rest URL => " + uri);
            ResponseEntity<T> req = restTemplate.exchange(uri, method, request, className);
            return req.getBody();
        } catch (HttpClientErrorException e) {
            String responseError = e.getResponseBodyAsString();
            ErrResponse errRes = new ErrResponse();
            try {
                errRes = ob.readValue(responseError,
                        new TypeReference<ErrResponse>() {
                        });
            } catch (Exception ex) {
                log.error("CommonService:: {}", ex);
            }
            throw new CommonException(errRes);
        }
    }
}
