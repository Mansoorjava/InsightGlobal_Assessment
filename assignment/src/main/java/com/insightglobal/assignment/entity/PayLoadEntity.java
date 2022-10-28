package com.insightglobal.assignment.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class PayLoadEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String message;
}
