INSERT IGNORE INTO temp_stage (LEVEL, MAX_TEMP, MIN_TEMP, CREATED_AT, MODIFIED_AT)
VALUES
(1, 50, 28, NOW(), NOW()),
(2, 27, 23, NOW(), NOW()),
(3, 22, 20, NOW(), NOW()),
(4, 19, 17, NOW(), NOW()),
(5, 16, 12, NOW(), NOW()),
(6, 11, 9, NOW(), NOW()),
(7, 8, 5, NOW(), NOW()),
(8, 4, -50, NOW(), NOW());
