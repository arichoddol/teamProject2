package org.spring.backendspring.API.marathonapi.service;

import java.util.List;

import org.spring.backendspring.API.marathonapi.entity.Marathon;

public interface MarathonService {
    List<Marathon> findAll();
}