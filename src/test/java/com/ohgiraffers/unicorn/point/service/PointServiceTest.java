package com.ohgiraffers.unicorn.point.service;

import com.ohgiraffers.unicorn.point.entity.PointTransaction;
import com.ohgiraffers.unicorn.point.reporitory.PointTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointTransactionRepository pointTransactionRepository;

    @InjectMocks
    private PointService pointService;

    @Test
    void addPoints_savesTransactionWithGivenValues() {
        given(pointTransactionRepository.save(any(PointTransaction.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        PointTransaction result = pointService.addPoints(1L, "AD_WATCHED", 50, "MAIN_HALL");

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getActivityType()).isEqualTo("AD_WATCHED");
        assertThat(result.getPoints()).isEqualTo(50);
        assertThat(result.getLocation()).isEqualTo("MAIN_HALL");
        verify(pointTransactionRepository).save(any(PointTransaction.class));
    }

    @Test
    void getUserTransactions_returnsRepositoryResult() {
        PointTransaction transaction = new PointTransaction();
        transaction.setUserId(1L);
        given(pointTransactionRepository.findByUserId(1L)).willReturn(List.of(transaction));

        List<PointTransaction> result = pointService.getUserTransactions(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(1L);
    }

    @Test
    void getUserTotalPoints_sumsAllTransactionPoints() {
        PointTransaction t1 = new PointTransaction();
        t1.setPoints(50);
        PointTransaction t2 = new PointTransaction();
        t2.setPoints(100);
        given(pointTransactionRepository.findByUserId(1L)).willReturn(List.of(t1, t2));

        int total = pointService.getUserTotalPoints(1L);

        assertThat(total).isEqualTo(150);
    }

    @Test
    void getUserTotalPoints_returnsZeroWhenNoTransactions() {
        given(pointTransactionRepository.findByUserId(1L)).willReturn(List.of());

        int total = pointService.getUserTotalPoints(1L);

        assertThat(total).isZero();
    }
}
