package com.hotel.manage.mapper;

import com.hotel.manage.entity.Member;
import com.hotel.manage.entity.MemberLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 会员Mapper接口
 */
@Mapper
public interface MemberMapper {
    
    /**
     * 获取会员列表
     * @param params 查询参数
     * @return 会员列表
     */
    List<Member> selectMemberList(Map<String, Object> params);
    
    /**
     * 根据ID获取会员信息
     * @param id 会员ID
     * @return 会员信息
     */
    Member selectMemberById(Long id);
    
    /**
     * 插入会员信息
     * @param member 会员信息
     * @return 影响行数
     */
    int insertMember(Member member);
    
    /**
     * 更新会员信息
     * @param member 会员信息
     * @return 影响行数
     */
    int updateMember(Member member);
    
    /**
     * 删除会员信息
     * @param id 会员ID
     * @return 影响行数
     */
    int deleteMemberById(Long id);
    
    /**
     * 根据手机号查找会员
     * @param phone 手机号
     * @return 会员信息
     */
    Member selectMemberByPhone(String phone);
    
    /**
     * 根据会员卡号查找会员
     * @param memberNo 会员卡号
     * @return 会员信息
     */
    Member selectMemberByMemberNo(String memberNo);
    
    /**
     * 检查手机号是否存在（排除指定ID）
     * @param phone 手机号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    int existsMemberByPhone(@Param("phone") String phone, @Param("excludeId") Long excludeId);
    
    /**
     * 检查会员卡号是否存在（排除指定ID）
     * @param memberNo 会员卡号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    int existsMemberByMemberNo(@Param("memberNo") String memberNo, @Param("excludeId") Long excludeId);
    
    /**
     * 更新会员积分
     * @param id 会员ID
     * @param points 新积分
     * @param totalPoints 累计积分
     * @return 影响行数
     */
    int updateMemberPoints(@Param("id") Long id, @Param("points") Integer points, @Param("totalPoints") Integer totalPoints);
    
    /**
     * 获取会员等级列表
     * @return 会员等级列表
     */
    List<MemberLevel> selectMemberLevelList();
    
    /**
     * 根据ID获取会员等级
     * @param id 等级ID
     * @return 会员等级
     */
    MemberLevel selectMemberLevelById(Long id);
    
    /**
     * 插入会员等级
     * @param memberLevel 会员等级信息
     * @return 影响行数
     */
    int insertMemberLevel(MemberLevel memberLevel);
    
    /**
     * 更新会员等级
     * @param memberLevel 会员等级信息
     * @return 影响行数
     */
    int updateMemberLevel(MemberLevel memberLevel);
    
    /**
     * 更新会员等级状态
     * @param id 等级ID
     * @param isEnabled 是否启用
     * @return 影响行数
     */
    int updateMemberLevelStatus(@Param("id") Long id, @Param("isEnabled") Boolean isEnabled);
    
    /**
     * 获取当日最大会员ID用于生成序号
     * @return 最大ID
     */
    Long getMaxIdToday();
}