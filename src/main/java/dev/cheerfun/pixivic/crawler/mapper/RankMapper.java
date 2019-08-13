package dev.cheerfun.pixivic.crawler.mapper;

import dev.cheerfun.pixivic.common.model.Illustration;
import dev.cheerfun.pixivic.common.model.illust.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankMapper {
    @Insert("<script>" +
            "REPLACE  INTO illusts VALUES" +
            "<foreach collection='illustrations' item='illustration' index='index'  separator=','>" +
            "(#{illustration.id}," +
            "#{illustration.title}," +
            "#{illustration.type}," +
            "#{illustration.caption}," +
            "#{illustration.restrict}," +
            "#{illustration.artistPreView,typeHandler=dev.cheerfun.pixivic.common.handler.JsonTypeHandler}," +
            "#{illustration.tools,typeHandler=dev.cheerfun.pixivic.common.handler.JsonTypeHandler}," +
            "#{illustration.tags,typeHandler=dev.cheerfun.pixivic.common.handler.JsonTypeHandler}," +
            "#{illustration.createDate}," +
            "#{illustration.pageCount}," +
            "#{illustration.width}," +
            "#{illustration.height}," +
            "#{illustration.sanityLevel}," +
            "#{illustration.xRestrict}," +
            "#{illustration.totalView}," +
            "#{illustration.totalBookmarks}," +
            "#{illustration.imageUrls,typeHandler=dev.cheerfun.pixivic.common.handler.JsonTypeHandler}," +
            "#{illustration.artistId} )" +
            "</foreach>" +
            "</script>")
    int insert(@Param("illustrations") List<Illustration> illustrations);

    @Insert({
            "<script>",
            "insert IGNORE into tags(name, translated_name) values ",
            "<foreach collection='tags' item='tag' index='index' separator=','>",
            "(#{tag.name}, #{tag.translatedName})",
            "</foreach>",
            "</script>"
    })
    int insertTag(@Param("tags") List<Tag> tags);

    @Insert({
            "<script>",
            "insert IGNORE into tag_illust_relation values",
            "<foreach collection='illustrations' item='illustration' index='index' separator=','>",
            "<foreach collection='illustration.tags' item='tag' index='index' separator=','>",
            "(" +
                    "(SELECT tag_id FROM tags WHERE NAME = #{tag.name} AND translated_name = #{tag.translatedName})" +
                    ",#{illustration.id})",
            "</foreach>",
            "</foreach>",
            "</script>"
    })
    int insertTagIllustRelation(@Param("illustrations") List<Illustration> illustrations);
}
