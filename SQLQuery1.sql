ALTER TABLE Cliente
ADD Email VARCHAR(100),
    [Password] VARCHAR(255);
GO

UPDATE c
SET c.Email    = LOWER(p.Nombre) + '.' + LOWER(p.Apellido) + '@correo.com',
    c.Password = '1234'
FROM Cliente c
INNER JOIN Persona p ON c.IdPersona = p.IdPersona
WHERE c.Email IS NULL;
GO