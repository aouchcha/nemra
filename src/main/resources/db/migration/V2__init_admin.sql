INSERT INTO users (
    full_name,
    phone,
    password_hash,
    role,
    city,
    is_active,
    created_at,
    updated_at
) VALUES (
             'admin admin',
             '0123456789',
             '$2a$12$gBRNhH.ZubRlJ6ZntSaCAurLSbNm5rkP3Zk0xOHJBHucLg1o18epW',
             'ADMIN',
             'jerada',
             true,
             NOW(),
             NOW()
         );