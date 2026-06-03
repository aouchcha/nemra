INSERT INTO users (
    full_name,
    phone,
    password_hash,
    role,
    is_active,
    created_at,
    updated_at
) VALUES (
             'admin admin',
             '0123456789',
             'ADMIN123@@',
             'ADMIN',
             true,
             NOW(),
             NOW()
         );