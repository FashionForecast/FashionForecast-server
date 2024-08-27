INSERT IGNORE INTO recommendation (CREATED_AT, MODIFIED_AT, OUTFIT_ID, TEMP_STAGE_ID)
VALUES
(NOW(), NOW(), 1, 1),
(NOW(), NOW(), 1, 2),
(NOW(), NOW(), 2, 1),
(NOW(), NOW(), 3, 2),
(NOW(), NOW(), 4, 2);