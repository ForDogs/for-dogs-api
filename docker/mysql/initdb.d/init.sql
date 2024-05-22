CREATE TABLE `mysql_db`.`user`
(
    `id`              BINARY(16)               NOT NULL COMMENT '회원 ID',
    `user_identifier` VARCHAR(50)              NOT NULL UNIQUE COMMENT '회원 계정 ID',
    `name`            VARCHAR(100)             NOT NULL COMMENT '회원명',
    `email_id`        VARCHAR(50)              NOT NULL COMMENT '이메일 ID',
    `email_domain`    VARCHAR(50)              NOT NULL COMMENT '이메일 도메인',
    `password`        VARCHAR(100)             NOT NULL COMMENT '비밀번호',
    `role`            ENUM ('SELLER', 'BUYER') NOT NULL DEFAULT 'BUYER' COMMENT '회원 역할 (SELLER: 판매자, BUYER: 구매자)',
    `is_deleted`      TINYINT(1)               NOT NULL DEFAULT 0 COMMENT '회원 탈퇴 여부 (0: 활성화, 1: 비활성화)',
    `created_at`      DATETIME                 NULL     DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`      DATETIME                 NULL     DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='회원 정보';

CREATE TABLE `mysql_db`.`refresh_token`
(
    `id`         BINARY(16)   NOT NULL COMMENT 'RefreshToken ID',
    `token`      VARCHAR(255) NOT NULL COMMENT 'RefreshToken 값',
    `user_id`    BINARY(16)   NOT NULL COMMENT '회원 ID',
    `created_at` DATETIME     NULL DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at` DATETIME     NULL DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_refresh_token_user_id` FOREIGN KEY (`user_id`) REFERENCES `mysql_db`.`user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='RefreshToken 정보';

CREATE TABLE `mysql_db`.`product`
(
    `id`          BINARY(16)                                                                   NOT NULL COMMENT '상품 ID',
    `seller_id`   BINARY(16)                                                                   NOT NULL COMMENT '판매자 ID',
    `name`        VARCHAR(255)                                                                 NOT NULL UNIQUE COMMENT '상품명',
    `price`       DECIMAL(10, 2)                                                               NOT NULL COMMENT '상품 가격',
    `quantity`    INT                                                                          NOT NULL COMMENT '상품 수량',
    `description` TEXT                                                                         NULL COMMENT '상품 설명',
    `category`    ENUM ('FOOD', 'CLOTHING', 'SNACK', 'TOY', 'ACCESSORY', 'SUPPLEMENT', 'NONE') NOT NULL DEFAULT 'NONE' COMMENT '상품 카테고리 (FOOD: 식품, CLOTHING: 의류, SNACK: 간식, TOY: 장난감, ACCESSORY: 악세서리, SUPPLEMENT: 보충제,  NONE: 카테고리 없음)',
    `images`      JSON                                                                         NULL COMMENT '상품 이미지',
    `is_deleted`  TINYINT(1)                                                                   NOT NULL DEFAULT 0 COMMENT '상품 삭제 여부 (0: 등록 상태, 1: 삭제 상태)',
    `created_at`  DATETIME                                                                     NULL     DEFAULT NOW() COMMENT '최초 생성 일시',
    `updated_at`  DATETIME                                                                     NULL     DEFAULT NOW() COMMENT '최종 수정 일시',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_product_seller_id` FOREIGN KEY (`seller_id`) REFERENCES `mysql_db`.`user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='상품 정보';
