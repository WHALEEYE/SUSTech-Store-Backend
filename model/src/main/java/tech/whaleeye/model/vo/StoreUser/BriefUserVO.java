package tech.whaleeye.model.vo.StoreUser;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BriefUserVO implements Serializable {

    private Integer id;

    private String nickname;

    private String avatarPath;

}
