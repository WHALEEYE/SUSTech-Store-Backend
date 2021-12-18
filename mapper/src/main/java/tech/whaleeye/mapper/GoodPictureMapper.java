package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.dto.GoodPictureDTO;
import tech.whaleeye.model.entity.GoodPicture;

import java.util.List;

@Mapper
public interface GoodPictureMapper {

    String getMainPicPathByGoodId(@Param("goodId") Integer goodId);

    List<GoodPicture> getPicturesByGoodId(@Param("goodId") Integer goodId);

    Integer insertGoodPictures(@Param("goodId") Integer goodId, @Param("goodPictureList") List<GoodPictureDTO> goodPictureList);

    void deletePicturesByGoodId(@Param("goodId") Integer goodId);

}
