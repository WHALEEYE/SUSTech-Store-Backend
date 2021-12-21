package tech.whaleeye.model.vo.BackUser;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BackUserVO implements Serializable {

    private Integer id;

    private String username;

    private Integer roleId;

    private String roleName;

    private Boolean banned;

    private Date createdTime;

    private Date updatedTime;

}
