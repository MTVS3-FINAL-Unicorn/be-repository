package com.ohgiraffers.unicorn.point.controller;

import com.ohgiraffers.unicorn.point.entity.PointTransaction;
import com.ohgiraffers.unicorn.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ohgiraffers.unicorn._core.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/points")
public class PointController {

    @Autowired
    private PointService pointService;

    @PostMapping("/add")
    public ResponseEntity<PointTransaction> addPoints(
            @RequestParam Long userId,
            @RequestParam String activityType,
            @RequestParam int points,
            @RequestParam String location) {
        PointTransaction transaction = pointService.addPoints(userId, activityType, points, location);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<PointTransaction>> getUserTransactions(){
        Long userId = getCurrentUserId();
        List<PointTransaction> transactions = pointService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getUserTotalPoints() {
        Long userId = getCurrentUserId();
        int totalPoints = pointService.getUserTotalPoints(userId);
        return ResponseEntity.ok(totalPoints);
    }
}
