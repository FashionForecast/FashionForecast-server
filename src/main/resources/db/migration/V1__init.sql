-- temp_stage 테이블 생성
CREATE TABLE IF NOT EXISTS temp_stage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    level INT NOT NULL,
    min_temp INT NOT NULL,
    max_temp INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW()
);

-- outfit 테이블 생성
CREATE TABLE IF NOT EXISTS outfit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    outfit_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- recommendation 테이블 생성
CREATE TABLE IF NOT EXISTS recommendation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    temp_stage_id BIGINT,
    outfit_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP NOT NULL DEFAULT NOW()
);