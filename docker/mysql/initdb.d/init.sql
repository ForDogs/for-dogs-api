-- INDEX
CREATE INDEX idx_orders_createdAt_buyerId ON orders (created_at, user_id);
CREATE INDEX idx_product_sellerId ON product (seller_id);
CREATE INDEX idx_orders_createdAt ON orders (created_at);

-- TABLE
CREATE TABLE `for_dog_db`.`user`
(
    `id`           BINARY(16)               NOT NULL COMMENT '회원 ID',
    `account`      VARCHAR(50)              NOT NULL UNIQUE COMMENT '회원 계정 ID',
    `name`         VARCHAR(100)             NOT NULL COMMENT '회원명',
    `email_id`     VARCHAR(50)              NOT NULL COMMENT '이메일 ID',
    `email_domain` VARCHAR(50)              NOT NULL COMMENT '이메일 도메인',
    `password`     VARCHAR(100)             NOT NULL COMMENT '비밀번호',
    `role`         ENUM ('SELLER', 'BUYER') NOT NULL DEFAULT 'BUYER' COMMENT '회원 역할 (SELLER: 판매자, BUYER: 구매자)',
    `enabled`      TINYINT(1)               NOT NULL DEFAULT 1 COMMENT '회원 활성화 여부 (1: 활성화, 0: 비활성화)',
    `created_at`   DATETIME                 NULL     DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`   DATETIME                 NULL     DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='회원 정보';

CREATE TABLE `for_dog_db`.`product`
(
    `id`          BINARY(16)                                                           NOT NULL COMMENT '상품 ID',
    `seller_id`   BINARY(16)                                                           NOT NULL COMMENT '판매자 ID',
    `name`        VARCHAR(255)                                                         NOT NULL UNIQUE COMMENT '상품명',
    `price`       INT                                                                  NOT NULL COMMENT '상품 가격',
    `quantity`    INT                                                                  NOT NULL COMMENT '상품 수량',
    `description` TEXT                                                                 NULL COMMENT '상품 설명',
    `category`    ENUM ('FOOD', 'CLOTHING', 'SNACK', 'TOY', 'ACCESSORY', 'SUPPLEMENT') NOT NULL COMMENT '상품 카테고리 (FOOD: 식품, CLOTHING: 의류, SNACK: 간식, TOY: 장난감, ACCESSORY: 악세서리, SUPPLEMENT: 보충제)',
    `images`      JSON                                                                 NULL COMMENT '상품 이미지',
    `enabled`     TINYINT(1)                                                           NOT NULL DEFAULT 1 COMMENT '상품 활성화 여부 (1: 활성화, 0: 비활성화)',
    `created_at`  DATETIME                                                             NULL     DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`  DATETIME                                                             NULL     DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_product_seller_id` FOREIGN KEY (`seller_id`) REFERENCES `for_dog_db`.`user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='상품 정보';

CREATE TABLE `for_dog_db`.`orders`
(
    `id`          BINARY(16)                                                                                                                 NOT NULL COMMENT '주문 ID',
    `user_id`     BINARY(16)                                                                                                                 NOT NULL COMMENT '구매자 회원 ID',
    `status`      ENUM ('AWAITING_PAYMENT', 'PAID', 'PAYMENT_FAILED', 'CONFIRMED', 'AWAITING_SHIPMENT', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PAID' COMMENT '주문 상태 (AWAITING_PAYMENT: 결제 대기 중, PAID: 결제 완료, PAYMENT_FAILED: 결제 오류, CONFIRMED: 구매 확인, AWAITING_SHIPMENT: 배송 대기 중, SHIPPED: 배송 중, DELIVERED: 배송 완료, CANCELLED: 주문 취소)',
    `total_price` INT                                                                                                                        NOT NULL COMMENT '총 주문 금액',
    `created_at`  DATETIME                                                                                                                   NULL     DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`  DATETIME                                                                                                                   NULL     DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `for_dog_db`.`user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주문 정보';

CREATE TABLE `for_dog_db`.`order_items`
(
    `id`         BINARY(16) NOT NULL COMMENT '주문 항목 ID',
    `order_id`   BINARY(16) NOT NULL COMMENT '주문 ID',
    `product_id` BINARY(16) NOT NULL COMMENT '상품 ID',
    `quantity`   INT        NOT NULL COMMENT '주문 항목 수량',
    `unit_price` INT        NOT NULL COMMENT '주문 당시의 상품 단가',
    `created_at` DATETIME   NULL DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at` DATETIME   NULL DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_order_items_order_id` FOREIGN KEY (`order_id`) REFERENCES `for_dog_db`.`orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_items_product_id` FOREIGN KEY (`product_id`) REFERENCES `for_dog_db`.`product` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주문 항목 정보';

CREATE TABLE `for_dog_db`.`payment`
(
    `id`               BINARY(16)   NOT NULL COMMENT '결제 ID',
    `order_id`         BINARY(16)   NOT NULL COMMENT '주문 ID (가맹점 주문번호)',
    `imp_uid`          VARCHAR(50)  NOT NULL COMMENT '포트원 결제 고유 번호',
    `pay_method`       VARCHAR(20)  NOT NULL COMMENT '결제 수단 구분코드',
    `channel`          VARCHAR(20)  NOT NULL COMMENT '결제 채널 (모바일, PC 등) 구분코드',
    `pg_provider`      VARCHAR(30)  NOT NULL COMMENT 'PG사 구분코드',
    `emb_pg_provider`  VARCHAR(30)  NULL COMMENT '허브형 결제 PG사 구분코드',
    `pg_tid`           VARCHAR(50)  NOT NULL COMMENT 'PG사 거래 번호',
    `pg_id`            VARCHAR(30)  NULL COMMENT 'PG사 상점 아이디',
    `escrow`           TINYINT(1)   NOT NULL COMMENT '에스크로 여부 (1: 에스크로, 0: 일반)',
    `apply_num`        VARCHAR(30)  NULL COMMENT '신용 카드 승인 번호',
    `bank_code`        VARCHAR(10)  NULL COMMENT '은행 표준 코드 - 실시간 계좌 이체 결제 건',
    `bank_name`        VARCHAR(30)  NULL COMMENT '은행명 - 실시간 계좌 이체 결제 건',
    `card_code`        VARCHAR(10)  NULL COMMENT '카드 코드 번호 - 카드 결제 건',
    `card_name`        VARCHAR(30)  NULL COMMENT '카드사 명 - 카드 결제 건',
    `card_issuer_code` VARCHAR(10)  NULL COMMENT '카드 발급사 코드 - 카드 결제 건',
    `card_issuer_name` VARCHAR(30)  NULL COMMENT '카드 발행사명 - 카드 결제 건',
    `card_quota`       INT          NULL COMMENT '할부 개월 수 (일시불은 0 표기) - 신용 카드 결제 건',
    `card_number`      VARCHAR(20)  NULL COMMENT '카드 번호',
    `card_type`        INT          NULL COMMENT '카드 구분코드 (NULL: 제공안함, 0: 신용카드, 1: 체크카드)',
    `buyer_address`    VARCHAR(100) NULL COMMENT '주문자 주소',
    `buyer_email`      VARCHAR(50)  NULL COMMENT '주문자 이메일',
    `buyer_name`       VARCHAR(50)  NULL COMMENT '주문자 이름',
    `buyer_postcode`   VARCHAR(10)  NULL COMMENT '주문자 우편번호',
    `buyer_tel`        VARCHAR(20)  NULL COMMENT '주문자 전화번호',
    `custom_data`      TEXT         NULL COMMENT '결제 요청 시 가맹점에서 전달한 추가 정보',
    `status`           VARCHAR(20)  NOT NULL COMMENT '결제 상태',
    `name`             VARCHAR(100) NOT NULL COMMENT '제품명',
    `amount`           INT          NOT NULL COMMENT '결제 금액',
    `currency`         VARCHAR(10)  NOT NULL COMMENT '결제 통화 구분코드 e.g) KRW, USD, KRW',
    `receipt_url`      VARCHAR(200) NULL COMMENT '매출전표 URL',
    `started_at`       INT          NOT NULL COMMENT '결제 요청 일시',
    `paid_at`          INT          NULL COMMENT '결제 완료 일시 (완료 아닌 경우 0)',
    `failed_at`        INT          NULL COMMENT '결제 실패 일시 (실패 아닌 경우 0)',
    `fail_reason`      VARCHAR(255) NULL COMMENT '결제 실패 사유 (실패 아닌 경우 NULL)',
    `created_at`       DATETIME     NOT NULL DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`       DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_payment_order_id` FOREIGN KEY (`order_id`) REFERENCES `for_dog_db`.`orders` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='결제 정보';

CREATE TABLE `for_dog_db`.`payment_cancel`
(
    `id`                 BINARY(16)   NOT NULL COMMENT '결제 취소 ID',
    `payment_id`         BINARY(16)   NOT NULL COMMENT '결제 ID',
    `cancellation_id`    VARCHAR(50)  NOT NULL COMMENT '결제 취소 ID',
    `cancelled_at`       INT          NOT NULL COMMENT '결제 취소 시각',
    `cancel_amount`      INT          NOT NULL COMMENT '결제건의 누적 취소 금액',
    `cancel_receipt_url` VARCHAR(200) NULL COMMENT '취소 매출전표 확인 URL',
    `cancel_reason`      VARCHAR(255) NULL COMMENT '결제 취소 사유',
    `created_at`         DATETIME     NOT NULL DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`         DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_payment_cancel_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `for_dog_db`.`payment` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='결제 취소 정보';
