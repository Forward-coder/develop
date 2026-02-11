package com.hotel.manage.service.impl;

import com.hotel.manage.entity.Member;
import com.hotel.manage.entity.MemberLevel;
import com.hotel.manage.mapper.MemberMapper;
import com.hotel.manage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员服务实现类
 */
@Service
public  class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Override
    public List<Member> getMemberList(Map<String, Object> params) {
        return memberMapper.selectMemberList(params);
    }
    
    @Override
    public Member getMemberById(Long id) {
        return memberMapper.selectMemberById(id);
    }
    
    @Transactional
    @Override
    public boolean saveMember(Member member) {
        // 数据校验
        validateMember(member);
        
        if (member.getId() != null) {
            // 更新
            Member existing = memberMapper.selectMemberById(member.getId());
            if (existing == null) {
                throw new RuntimeException("会员信息不存在");
            }
            
            return memberMapper.updateMember(member) > 0;
        } else {
            // 新增
            // 生成会员卡号
            if (!StringUtils.hasText(member.getMemberNo())) {
                member.setMemberNo(generateMemberNo());
            }
            
            // 检查会员卡号重复
            int memberNoCount = memberMapper.existsMemberByMemberNo(member.getMemberNo(), null);
            if (memberNoCount > 0) {
                throw new RuntimeException("会员卡号已存在");
            }
            
            // 设置默认值
            if (member.getPoints() == null) {
                member.setPoints(0);
            }
            if (member.getTotalPoints() == null) {
                member.setTotalPoints(0);
            }
            if (member.getStatus() == null) {
                member.setStatus(1); // 默认正常状态
            }
            
            return memberMapper.insertMember(member) > 0;
        }
    }
    
    @Transactional
    @Override
    public boolean deleteMember(Long id) {
        Member member = memberMapper.selectMemberById(id);
        if (member == null) {
            throw new RuntimeException("会员信息不存在");
        }
        
        return memberMapper.deleteMemberById(id) > 0;
    }
    
    @Override
    public Member getMemberByPhone(String phone) {
        return memberMapper.selectMemberByPhone(phone);
    }
    
    @Override
    public Member getMemberByMemberNo(String memberNo) {
        return memberMapper.selectMemberByMemberNo(memberNo);
    }
    
    @Override
    public List<MemberLevel> getMemberLevelList() {
        return memberMapper.selectMemberLevelList();
    }
    
    @Override
    public MemberLevel getMemberLevelById(Long id) {
        return memberMapper.selectMemberLevelById(id);
    }
    
    @Transactional
    @Override
    public boolean adjustPoints(Long memberId, Integer points, String remark) {
        if (memberId == null || points == null) {
            throw new RuntimeException("参数不能为空");
        }
        
        Member member = memberMapper.selectMemberById(memberId);
        if (member == null) {
            throw new RuntimeException("会员信息不存在");
        }
        
        // 计算新的积分
        int newPoints = member.getPoints() + points;
        int newTotalPoints = member.getTotalPoints() + Math.max(points, 0); // 累计积分只增加正值
        
        if (newPoints < 0) {
            throw new RuntimeException("积分不足，无法扣除");
        }
        
        return memberMapper.updateMemberPoints(memberId, newPoints, newTotalPoints) > 0;
    }
    
    @Transactional
    @Override
    public boolean saveMemberLevel(MemberLevel memberLevel) {
        if (memberLevel.getName() == null || memberLevel.getName().trim().isEmpty()) {
            throw new RuntimeException("等级名称不能为空");
        }
        
        if (memberLevel.getDiscountRate() == null || memberLevel.getDiscountRate().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("折扣率必须大于0");
        }
        
        if (memberLevel.getMinPoints() == null || memberLevel.getMinPoints() < 0) {
            throw new RuntimeException("最低积分不能为负数");
        }
        
        if (memberLevel.getId() != null) {
            // 更新
            MemberLevel existing = memberMapper.selectMemberLevelById(memberLevel.getId());
            if (existing == null) {
                throw new RuntimeException("会员等级不存在");
            }
            return memberMapper.updateMemberLevel(memberLevel) > 0;
        } else {
            // 新增
            if (memberLevel.getIsEnabled() == null) {
                memberLevel.setIsEnabled(true);
            }
            return memberMapper.insertMemberLevel(memberLevel) > 0;
        }
    }
    
    @Transactional
    @Override
    public boolean toggleMemberLevelStatus(Long id, Boolean isEnabled) {
        if (id == null || isEnabled == null) {
            throw new RuntimeException("参数不能为空");
        }
        
        MemberLevel memberLevel = memberMapper.selectMemberLevelById(id);
        if (memberLevel == null) {
            throw new RuntimeException("会员等级不存在");
        }
        
        return memberMapper.updateMemberLevelStatus(id, isEnabled) > 0;
    }
    
    /**
     * 数据校验
     */
    private void validateMember(Member member) {
        if (member.getGuestId() == null) {
            throw new RuntimeException("请选择关联的客人信息");
        }
        
        if (member.getLevelId() == null) {
            throw new RuntimeException("请选择会员等级");
        }
    }
    
    /**
     * 生成会员卡号
     * 格式: M + YYYYMMDD + 6位序号 (如: M20240115000001)
     * 序号从000001开始，每日重置
     */
    private String generateMemberNo() {
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 查询当日最大ID，计算序号
        Long maxId = memberMapper.getMaxIdToday();
        long seq = (maxId == null ? 0 : maxId) + 1;
        
        // 生成6位序号，不足6位前面补0
        String seqStr = String.format("%06d", seq % 1000000);
        
        return "M" + dateStr + seqStr;
    }
    
    @Override
    public Map<String, Object> getMemberStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总会员数
        List<Member> allMembers = memberMapper.selectMemberList(new HashMap<>());
        stats.put("totalMembers", allMembers.size());
        
        // 活跃会员数（状态为正常的会员）
        long activeCount = allMembers.stream()
            .filter(m -> m.getStatus() != null && m.getStatus() == 1)
            .count();
        stats.put("activeMembers", activeCount);
        
        // 平均积分
        double avgPoints = allMembers.stream()
            .mapToInt(m -> m.getPoints() != null ? m.getPoints() : 0)
            .average()
            .orElse(0.0);
        stats.put("averagePoints", Math.round(avgPoints));
        
        // 本月新增会员
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        long monthlyGrowth = allMembers.stream()
            .filter(m -> m.getCreatedAt() != null && 
                m.getCreatedAt().toLocalDate().isAfter(firstDayOfMonth.minusDays(1)))
            .count();
        stats.put("monthlyGrowth", monthlyGrowth);
        
        return stats;
    }
    
    @Override
    public List<Map<String, Object>> getLevelDistribution() {
        List<MemberLevel> levels = memberMapper.selectMemberLevelList();
        List<Member> members = memberMapper.selectMemberList(new HashMap<>());
        
        List<Map<String, Object>> result = new ArrayList<>();
        int totalMembers = members.size();
        
        for (MemberLevel level : levels) {
            long count = members.stream()
                .filter(m -> m.getLevelId() != null && m.getLevelId().equals(level.getId()))
                .count();
            
            double percentage = totalMembers > 0 ? (double) count / totalMembers * 100 : 0;
            double avgPoints = members.stream()
                .filter(m -> m.getLevelId() != null && m.getLevelId().equals(level.getId()))
                .mapToInt(m -> m.getPoints() != null ? m.getPoints() : 0)
                .average()
                .orElse(0.0);
            
            Map<String, Object> levelStat = new HashMap<>();
            levelStat.put("levelName", level.getName());
            levelStat.put("count", count);
            levelStat.put("percentage", Math.round(percentage * 100.0) / 100.0);
            levelStat.put("avgPoints", Math.round(avgPoints));
            
            result.add(levelStat);
        }
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getMonthlyGrowth() {
        List<Member> members = memberMapper.selectMemberList(new HashMap<>());
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 获取最近6个月的数据
        for (int i = 5; i >= 0; i--) {
            LocalDate startDate = LocalDate.now().minusMonths(i).withDayOfMonth(1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);
            
            long newMembers = members.stream()
                .filter(m -> m.getCreatedAt() != null && 
                    m.getCreatedAt().toLocalDate().compareTo(startDate) >= 0 &&
                    m.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
                .count();
            
            long totalMembers = members.stream()
                .filter(m -> m.getCreatedAt() != null && 
                    m.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
                .count();
            
            Map<String, Object> monthStat = new HashMap<>();
            monthStat.put("month", startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
            monthStat.put("newMembers", newMembers);
            monthStat.put("totalMembers", totalMembers);
            
            // 计算增长率
            if (i < 5) {
                LocalDate prevMonth = startDate.minusMonths(1);
                long prevTotal = members.stream()
                    .filter(m -> m.getCreatedAt() != null && 
                        m.getCreatedAt().toLocalDate().compareTo(prevMonth) <= 0)
                    .count();
                double growthRate = prevTotal > 0 ? ((double)(totalMembers - prevTotal) / prevTotal) * 100 : 0;
                monthStat.put("growthRate", Math.round(growthRate * 100.0) / 100.0);
            } else {
                monthStat.put("growthRate", 0.0);
            }
            
            result.add(monthStat);
        }
        
        return result;
    }
    
    @Override
    public List<Member> getPointsRanking(Integer limit) {
        List<Member> members = memberMapper.selectMemberList(new HashMap<>());
        
        return members.stream()
            .sorted((m1, m2) -> {
                int points1 = m1.getPoints() != null ? m1.getPoints() : 0;
                int points2 = m2.getPoints() != null ? m2.getPoints() : 0;
                return Integer.compare(points2, points1); // 降序排列
            })
            .limit(limit)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}