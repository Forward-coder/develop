package com.hotel.manage.service;

import com.hotel.manage.entity.Member;
import com.hotel.manage.entity.MemberLevel;
import java.util.List;
import java.util.Map;

/**
 * 会员服务接口
 */
public interface MemberService {
    
    /**
     * 获取会员列表
     * @param params 查询参数
     * @return 会员列表
     */
    List<Member> getMemberList(Map<String, Object> params);
    
    /**
     * 根据ID获取会员信息
     * @param id 会员ID
     * @return 会员信息
     */
    Member getMemberById(Long id);
    
    /**
     * 保存会员信息（新增或更新）
     * @param member 会员信息
     * @return 是否成功
     */
    boolean saveMember(Member member);
    
    /**
     * 删除会员信息
     * @param id 会员ID
     * @return 是否成功
     */
    boolean deleteMember(Long id);
    
    /**
     * 根据手机号查找会员
     * @param phone 手机号
     * @return 会员信息
     */
    Member getMemberByPhone(String phone);
    
    /**
     * 根据会员卡号查找会员
     * @param memberNo 会员卡号
     * @return 会员信息
     */
    Member getMemberByMemberNo(String memberNo);
    
    /**
     * 获取会员等级列表
     * @return 会员等级列表
     */
    List<MemberLevel> getMemberLevelList();
    
    /**
     * 根据ID获取会员等级
     * @param id 等级ID
     * @return 会员等级
     */
    MemberLevel getMemberLevelById(Long id);
    
    /**
     * 积分变动
     * @param memberId 会员ID
     * @param points 变动积分（正数为增加，负数为减少）
     * @param remark 备注
     * @return 是否成功
     */
    boolean adjustPoints(Long memberId, Integer points, String remark);
    
    /**
     * 保存会员等级
     * @param memberLevel 会员等级信息
     * @return 是否成功
     */
    boolean saveMemberLevel(MemberLevel memberLevel);
    
    /**
     * 切换会员等级状态
     * @param id 等级ID
     * @param isEnabled 是否启用
     * @return 是否成功
     */
    boolean toggleMemberLevelStatus(Long id, Boolean isEnabled);
    
    /**
     * 获取会员统计概览
     * @return 统计数据
     */
    Map<String, Object> getMemberStatistics();
    
    /**
     * 获取会员等级分布
     * @return 等级分布数据
     */
    List<Map<String, Object>> getLevelDistribution();
    
    /**
     * 获取月度增长统计
     * @return 月度增长数据
     */
    List<Map<String, Object>> getMonthlyGrowth();
    
    /**
     * 获取积分排行榜
     * @param limit 限制数量
     * @return 积分排行
     */
    List<Member> getPointsRanking(Integer limit);
}