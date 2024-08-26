-- 다대일 연관관계 매핑을 위한 외래키 제약 조건
ALTER TABLE recommendation
ADD CONSTRAINT fk_temp_stage_id
FOREIGN KEY (temp_stage_id) REFERENCES temp_stage(id) ON DELETE CASCADE;

-- 다대일 연관관계 매핑을 위한 외래키 제약 조건
ALTER TABLE recommendation
ADD CONSTRAINT fk_outfit_id
FOREIGN KEY (outfit_id) REFERENCES outfit(id) ON DELETE CASCADE;