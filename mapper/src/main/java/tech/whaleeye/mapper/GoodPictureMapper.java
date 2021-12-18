package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.dto.GoodPictureDTO;
import tech.whaleeye.model.entity.GoodPicture;

import java.util.List;

@Mapper
public interface GoodPictureMapper {

    String getMainPicPathByGoodId(@Param("goodId") Integer goodId);

    List<String> getPicturesByGoodId(@Param("goodId") Integer goodId);

    Integer insertGoodPictures(@Param("goodId") Integer goodId, @Param("picturePathList") List<String> picturePathList);

    void deletePicturesByGoodId(@Param("goodId") Integer goodId);

}
