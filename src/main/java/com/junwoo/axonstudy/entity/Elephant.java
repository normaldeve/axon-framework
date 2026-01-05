package com.junwoo.axonstudy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Data
@Entity
@Table(name = "elephant")
public class Elephant implements Serializable {

    @Serial
    private static final long serialVersionUID = 5777826802121382787L;

    @Id
    @Column(name = "id", nullable = false, length = 3)
    private String id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "weight", nullable = false)
    private int weight;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

}
