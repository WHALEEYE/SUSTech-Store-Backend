package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BackUserRole implements Serializable {

    private Integer id;

    private String roleName;

    private String description;

}