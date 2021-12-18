package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.GoodType;

import java.util.List;

@Mapper
public interface GoodTypeMapper {

    GoodType getGoodTypeById(@Param("typeId") Integer typeId);

    List<GoodType> getAllGoodTypes();

    /* used in background system */

    List<GoodType> listAllGoodTypesForBack(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countAllGoodTypesForBack();

    Boolean moveUp(@Param("typeId") Integer typeId);

    Boolean moveDown(@Param("typeId") Integer typeId);

    Integer createNewType(@Param("typeName") String typeName);

    Integer updateTypeName(@Param("typeId") Integer typeId, @Param("typeName") String typeName);

    Integer deleteType(@Param("typeId") Integer typeId);

}