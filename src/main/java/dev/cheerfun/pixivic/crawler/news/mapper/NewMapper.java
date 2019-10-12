package dev.cheerfun.pixivic.crawler.news.mapper;

import dev.cheerfun.pixivic.common.model.ACGNew;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface NewMapper {
    @Select({
            "<script>",
            "SELECT temp.title FROM (",
            "<foreach collection='titleList' item='t' index='index' separator='UNION ALL'>",
            "(SELECT #{t} AS title )",
            "</foreach>",
            ") as temp WHERE temp.title  NOT IN (SELECT news.title FROM news)",
            "</script>"
    })
    Set<String> queryNewsNotInDb(@Param("titleList") Set<String> titleList);

    @Insert({
            "<script>",
            "insert IGNORE into news values (`title`, `intro`, `author`,`cover`, `referer_url`, `content`, `from`, `create_date`) ",
            "<foreach collection='acgNewList' item='acgNew' index='index' separator=','>",
            "(#{acgNew.title}," +
                    "#{acgNew.intro}, #{acgNew.author}," +
                    "#{acgNew.cover}, #{acgNew.refererUrl}," +
                    "#{acgNew.content}, #{acgNew.createDate}," +
                    "#{acgNew.from})",
            "</foreach>",
            "</script>"
    })
    int insert(@Param("acgNewList") List<ACGNew> acgNewList);
}
