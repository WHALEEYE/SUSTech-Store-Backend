package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.GoodType;

import java.util.List;

@Mapper
public interface GoodTypeMapper {

    GoodType getGoodTypeById(@Param("typeId") Integer typeId);

    List<GoodType> getAllGoodTypes();

}