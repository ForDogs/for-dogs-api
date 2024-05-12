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
