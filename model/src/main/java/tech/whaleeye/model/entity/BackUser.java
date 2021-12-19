package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BackUser implements Serializable {

    private Integer id;


    private String username;

    private String password;

    private String salt;

    private Integer roleId;

    private Boolean banned;

    private Date createdTime;

    private Date updatedTime;

}