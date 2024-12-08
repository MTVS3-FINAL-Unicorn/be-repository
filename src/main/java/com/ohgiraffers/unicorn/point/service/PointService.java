package com.ohgiraffers.unicorn.point.service;

import com.ohgiraffers.unicorn.point.entity.PointTransaction;
import com.ohgiraffers.unicorn.point.reporitory.PointTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    public PointTransaction addPoints(Long userId, String activityType, int points, String location) {
        PointTransaction transaction = new PointTransaction();
        transaction.setUserId(userId);
        transaction.setActivityType(activityType);
        transaction.setPoints(points);
        transaction.setLocation(location);

        return pointTransactionRepository.save(transaction);
    }

    public List<PointTransaction> getUserTransactions(Long userId) {
        return pointTransactionRepository.findByUserId(userId);
    }

    public int getUserTotalPoints(Long userId) {
        List<PointTransaction> transactions = pointTransactionRepository.findByUserId(userId);
        return transactions.stream().mapToInt(PointTransaction::getPoints).sum();
    }
}
