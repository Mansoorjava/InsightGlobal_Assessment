package com.insightglobal.assignment.controller;

import com.insightglobal.assignment.entity.PayLoadEntity;
import com.insightglobal.assignment.model.PayLoad;
import com.insightglobal.assignment.repository.PayLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/soap")
public class SoapConsumerController {
    @Autowired
    private PayLoadRepository repository;
    @PostMapping("/addMessage")
    public Mono<PayLoad> addMessage(@RequestBody PayLoad data){
        Mono<PayLoad> dataMono = WebClient.create("http://localhost:8080/soap/add")
                .post()
                .body(Mono.just(data), PayLoad.class)
                .retrieve()
                .bodyToMono(PayLoad.class);
        Mono<PayLoad> messageMono = dataMono
                .map(d -> maptoEntity(d))
                .doOnNext(message -> repository.save(message))
                .map(e->mapToDto(e));
        return  messageMono;
    }
    @PostMapping("/add")
    public PayLoad addToSoap(@RequestBody PayLoad p){
        //mocking soap webservice
        if(p.getId() == 0){
            double random = Math.random()*10000;
            p.setId(Double.valueOf(random).intValue());
        }
        return p;
    }

    private PayLoad mapToDto(PayLoadEntity e) {
        PayLoad p = new PayLoad();
        p.setId(e.getId());
        p.setMessage(e.getMessage());
        return p;
    }

    private PayLoadEntity maptoEntity(PayLoad dto){
        PayLoadEntity entity = new PayLoadEntity();
        entity.setId(dto.getId());
        entity.setMessage(dto.getMessage());
        return entity;
    }
}
