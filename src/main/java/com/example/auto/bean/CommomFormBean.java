package com.example.auto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommomFormBean {
    private String type;
    private Object[] objects;
    private String yName;
    private String xName;
    private String id;
}
