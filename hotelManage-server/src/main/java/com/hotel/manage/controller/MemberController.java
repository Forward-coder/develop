package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.Member;
import com.hotel.manage.entity.MemberLevel;
import com.hotel.manage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员管理Controller
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    /**
     * 获取会员列表
     */
    @GetMapping("/list")
    public Result<List<Member>> getMemberList(@RequestParam Map<String, Object> params) {
        try {
            List<Member> list = memberService.getMemberList(params);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取会员列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取会员信息
     */
    @GetMapping("/{id}")
    public Result<Member> getMemberById(@PathVariable Long id) {
        try {
            Member member = memberService.getMemberById(id);
            if (member == null) {
                return Result.error("会员信息不存在");
            }
            return Result.success(member);
        } catch (Exception e) {
            return Result.error("获取会员信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存会员信息（新增或更新）
     */
    @PostMapping("/save")
    public Result<Member> saveMember(@RequestBody Member member) {
        try {
            boolean success = memberService.saveMember(member);
            if (success) {
                // 如果是新增，重新查询返回完整信息
                if (member.getId() == null) {
                    List<Member> list = memberService.getMemberList(Map.of());
                    member = list.get(0);
                }
                return Result.success(member);
            }
            return Result.error("保存失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除会员信息
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMember(@PathVariable Long id) {
        try {
            boolean success = memberService.deleteMember(id);
            if (success) {
                return Result.success();
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据手机号查找会员
     */
    @GetMapping("/by-phone/{phone}")
    public Result<Member> getMemberByPhone(@PathVariable String phone) {
        try {
            Member member = memberService.getMemberByPhone(phone);
            return Result.success(member);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据会员卡号查找会员
     */
    @GetMapping("/by-member-no/{memberNo}")
    public Result<Member> getMemberByMemberNo(@PathVariable String memberNo) {
        try {
            Member member = memberService.getMemberByMemberNo(memberNo);
            return Result.success(member);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取会员等级列表
     */
    @GetMapping("/levels")
    public Result<List<MemberLevel>> getMemberLevelList() {
        try {
            List<MemberLevel> list = memberService.getMemberLevelList();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取会员等级列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 积分调整
     */
    @PostMapping("/{id}/adjust-points")
    public Result<Void> adjustPoints(@PathVariable Long id, 
                                   @RequestParam Integer points,
                                   @RequestParam(required = false) String remark) {
        try {
            boolean success = memberService.adjustPoints(id, points, remark);
            if (success) {
                return Result.success();
            }
            return Result.error("积分调整失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 保存会员等级
     */
    @PostMapping("/level/save")
    public Result<MemberLevel> saveMemberLevel(@RequestBody MemberLevel memberLevel) {
        try {
            boolean success = memberService.saveMemberLevel(memberLevel);
            if (success) {
                return Result.success(memberLevel);
            }
            return Result.error("保存失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 切换会员等级状态
     */
    @PostMapping("/level/{id}/toggle-status")
    public Result<Void> toggleMemberLevelStatus(@PathVariable Long id, 
                                              @RequestParam Boolean isEnabled) {
        try {
            boolean success = memberService.toggleMemberLevelStatus(id, isEnabled);
            if (success) {
                return Result.success();
            }
            return Result.error("操作失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取会员统计概览
     */
    @GetMapping("/statistics/overview")
    public Result<Map<String, Object>> getMemberStatistics() {
        try {
            Map<String, Object> stats = memberService.getMemberStatistics();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取会员等级分布
     */
    @GetMapping("/statistics/level-distribution")
    public Result<List<Map<String, Object>>> getLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = memberService.getLevelDistribution();
            return Result.success(distribution);
        } catch (Exception e) {
            return Result.error("获取等级分布失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取月度增长统计
     */
    @GetMapping("/statistics/monthly-growth")
    public Result<List<Map<String, Object>>> getMonthlyGrowth() {
        try {
            List<Map<String, Object>> growth = memberService.getMonthlyGrowth();
            return Result.success(growth);
        } catch (Exception e) {
            return Result.error("获取月度增长失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取积分排行榜
     */
    @GetMapping("/statistics/points-ranking")
    public Result<List<Member>> getPointsRanking(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Member> ranking = memberService.getPointsRanking(limit);
            return Result.success(ranking);
        } catch (Exception e) {
            return Result.error("获取积分排行失败: " + e.getMessage());
        }
    }
}