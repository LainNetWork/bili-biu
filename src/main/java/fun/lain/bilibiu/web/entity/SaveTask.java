package fun.lain.bilibiu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * 缓存配置信息实体类
 * 删除某个收藏夹的监视，并不会缓存的文件，也不会将收藏夹中已经缓存的视频删除
 * 缓存的mid号是独立出来的，除非手动在列表中将缓存清除重下
 * 至于收藏夹，av号，cid之间的关系，在数据库中维持
 * @author Lain
 * @date 2019-12-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("save_task")
public class SaveTask {
    public interface Status{
        Integer CREATE = 0;
        Integer STOP = 1;
        Integer RUNNING = 2;
    }


    @TableId(type = IdType.AUTO)
    private Long Id;

    //用户UID，目前只支持单账号,用cookie也要先获取用户信息，以uid为准
    //即使是多账号，一个账号也应该只能保存一个任务，添加，删除收藏夹监视都是在一个任务里的
    private Long userId;

    private String cookie;

    /**
     * cron表达式，代表检测收藏夹更新的周期
     */
    private String cron;

    //删除就直接从数据库中删除，不做标记
    private Integer status;
}
