package com.fordogs.product.batch;

import com.fordogs.core.util.EmailSenderUtil;
import com.fordogs.core.util.constants.EmailConstants;
import com.fordogs.product.application.ProductService;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@StepScope
@Component
@RequiredArgsConstructor
public class StockAlertTasklet implements Tasklet {

    private static final int LOW_STOCK_THRESHOLD = 10;

    private final EmailSenderUtil emailSenderUtil;
    private final ProductService productService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        List<ProductEntity> lowStockProducts = productService.findLowStockProducts(LOW_STOCK_THRESHOLD);
        if (!lowStockProducts.isEmpty()) {
            Map<UserEntity, List<ProductEntity>> productsBySeller = lowStockProducts.stream()
                .collect(Collectors.groupingBy(ProductEntity::getSeller));

            for (Map.Entry<UserEntity, List<ProductEntity>> entry : productsBySeller.entrySet()) {
                UserEntity seller = entry.getKey();
                List<ProductEntity> products = entry.getValue();

                sendLowStockNotification(seller, products);
            }
        }
        return RepeatStatus.FINISHED;
    }

    private void sendLowStockNotification(UserEntity seller, List<ProductEntity> products) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sellerName", seller.getName().getValue());
        variables.put("minimumStock", LOW_STOCK_THRESHOLD);
        variables.put("products", products);

        emailSenderUtil.sendMail(seller.getEmail(), EmailConstants.STOCK_ALERT_EMAIL_SUBJECT, "stock_alert_email", variables);
    }
}
