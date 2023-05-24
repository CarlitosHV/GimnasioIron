-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 24-05-2023 a las 17:56:21
-- Versión del servidor: 8.0.31
-- Versión de PHP: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gimnasio`
--

DELIMITER $$
--
-- Procedimientos
--
DROP PROCEDURE IF EXISTS `actualizar_contrasenia`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `actualizar_contrasenia` (IN `correo_usuario` VARCHAR(25), IN `contrasenia_usuario` VARCHAR(25))   BEGIN
    -- Actualizamos la contraseña
    UPDATE USUARIOS 
    SET USUARIOS.contrasenia = contrasenia_usuario
    WHERE correo = correo_usuario;

END$$

DROP PROCEDURE IF EXISTS `actualizar_usuario`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `actualizar_usuario` (IN `p_id_usuario` INT(10), IN `p_nombre` VARCHAR(15), IN `p_apellido_paterno` VARCHAR(15), IN `p_apellido_materno` VARCHAR(25), IN `p_correo` VARCHAR(25), IN `p_contrasenia` VARCHAR(64), IN `p_telefono` BIGINT(10), IN `p_calle` VARCHAR(20), IN `p_numero` INT(10), IN `p_codigo_postal` INT(10), IN `p_municipio` VARCHAR(20), IN `p_estado` VARCHAR(20))   BEGIN
    DECLARE v_id_nombre INT(25);
    DECLARE v_id_direccion INT(25);
    DECLARE v_id_municipio INT(25);
    DECLARE v_id_estado INT(25);


    -- Validamos si existe el nombre con apellidos, si existe no se inserta de nuevo y se obtiene su ID
    -- pero si no existe se inserta y se obtiene el id
    IF NOT EXISTS(SELECT NOMBRES.nombre, NOMBRES.apellido_paterno, NOMBRES.apellido_materno FROM NOMBRES WHERE NOMBRES.nombre = p_nombre AND NOMBRES.apellido_paterno = p_apellido_paterno
    AND NOMBRES.apellido_materno = p_apellido_materno) THEN INSERT INTO NOMBRES (nombre, apellido_paterno, apellido_materno)
    VALUES (p_nombre, p_apellido_paterno, p_apellido_materno);
    END IF;
    BEGIN
        SELECT NOMBRES.id_nombre 
        INTO v_id_nombre
        FROM NOMBRES 
        WHERE NOMBRES.nombre = p_nombre AND NOMBRES.apellido_paterno = p_apellido_paterno
        AND NOMBRES.apellido_materno = p_apellido_materno;
    END;

    -- Validamos si existe el estado y si no, lo insertamos
    IF NOT EXISTS(SELECT ESTADO.nombre FROM ESTADO WHERE ESTADO.nombre = p_estado)
    THEN INSERT INTO ESTADO(nombre)
    VALUES (p_estado);
    END IF;
    BEGIN
        SELECT ESTADO.id_estado
        INTO v_id_estado
        FROM ESTADO 
        WHERE ESTADO.nombre = p_estado;
    END;

   -- Validamos si existe el Municipio y si no, lo insertamos
    IF NOT EXISTS(SELECT MUNICIPIO.nombre FROM MUNICIPIO WHERE MUNICIPIO.nombre = p_municipio)
    THEN INSERT INTO MUNICIPIO(nombre, id_estado)
    VALUES (p_municipio, v_id_estado);
    END IF;
    BEGIN
        SELECT MUNICIPIO.id_municipio
        INTO v_id_municipio
        FROM MUNICIPIO 
        WHERE MUNICIPIO.nombre = p_municipio;
    END;

    
    	-- Validamos e insertamos la dirección
	IF NOT EXISTS(SELECT DIRECCION.calle, DIRECCION.numero, DIRECCION.codigo_postal, DIRECCION.id_municipio, DIRECCION.id_estado 	FROM DIRECCION 
	WHERE DIRECCION.calle = p_calle AND DIRECCION.numero = p_numero AND DIRECCION.codigo_postal = p_codigo_postal AND 		DIRECCION.id_municipio = v_id_municipio AND DIRECCION.id_estado = v_id_estado)
    THEN INSERT INTO DIRECCION(calle, numero, codigo_postal, id_municipio, id_estado) 
    VALUES (p_calle, p_numero, p_codigo_postal, v_id_municipio, v_id_estado);
    END IF;
    BEGIN
        SELECT DIRECCION.id_direccion
        INTO v_id_direccion
        FROM DIRECCION 
	    WHERE DIRECCION.calle = p_calle AND DIRECCION.numero = p_numero AND DIRECCION.codigo_postal = p_codigo_postal AND DIRECCION.id_municipio = v_id_municipio AND DIRECCION.id_estado = v_id_estado;
    END;

    UPDATE USUARIOS
    SET 
        id_nombre = v_id_nombre, correo = p_correo, contrasenia = p_contrasenia, telefono = p_telefono,
        id_direccion = v_id_direccion
    WHERE 
        id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS `bloquear_cliente`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `bloquear_cliente` (IN `id_cliente` INT)   BEGIN
    UPDATE USUARIOS SET bloqueado = true WHERE USUARIOS.id_usuario = id_cliente;
END$$

DROP PROCEDURE IF EXISTS `buscar_avanzado`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `buscar_avanzado` (IN `p_palabra` VARCHAR(50))   BEGIN
    SELECT 
    u.id_usuario,
    n.nombre as nombre,
    n.apellido_paterno as apellido_paterno,
    n.apellido_materno as apellido_materno,
    u.correo,
    ts.nombre as tipo_suscripcion,
    s.fecha_termino,
    u.bloqueado
    FROM USUARIOS u
    JOIN NOMBRES n ON n.id_nombre = u.id_nombre
    LEFT JOIN SUSCRIPCIONES s ON s.id_usuario = u.id_usuario
    LEFT JOIN TIPO_SUSCRIPCION ts ON ts.id_tipo_suscripcion = s.id_tipo_suscripcion
    WHERE 
        LOWER(u.correo) LIKE CONCAT('%', LOWER(p_palabra), '%')
        OR LOWER(n.nombre) LIKE CONCAT('%', LOWER(p_palabra), '%')
        OR LOWER(n.apellido_paterno) LIKE CONCAT('%', LOWER(p_palabra), '%')
        OR LOWER(n.apellido_materno) LIKE CONCAT('%', LOWER(p_palabra), '%')
        OR LOWER(CONCAT(n.nombre, ' ', n.apellido_paterno, ' ', n.apellido_materno)) LIKE CONCAT('%', LOWER(p_palabra), '%')
    ORDER BY n.nombre ASC;   
END$$

DROP PROCEDURE IF EXISTS `consultar_clientes`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `consultar_clientes` ()   BEGIN
    SELECT 
    u.id_usuario,
    n.nombre as nombre,
    n.apellido_paterno as apellido_paterno,
    n.apellido_materno as apellido_materno,
    u.correo,
    ts.nombre as tipo_suscripcion,
    s.fecha_termino,
    u.bloqueado
    FROM USUARIOS u
    JOIN NOMBRES n ON n.id_nombre = u.id_nombre
    LEFT JOIN SUSCRIPCIONES s ON s.id_usuario = u.id_usuario
    LEFT JOIN TIPO_SUSCRIPCION ts ON ts.id_tipo_suscripcion = s.id_tipo_suscripcion
    ORDER BY n.nombre ASC;
END$$

DROP PROCEDURE IF EXISTS `consultar_cuenta`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `consultar_cuenta` (IN `correo_usuario` VARCHAR(25))   BEGIN
    -- Buscamos la posible cuenta existente
    SELECT USUARIOS.correo FROM USUARIOS WHERE USUARIOS.correo = correo_usuario;

END$$

DROP PROCEDURE IF EXISTS `consultar_suscripcion`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `consultar_suscripcion` (IN `p_id_usuario` INT(10))   BEGIN
    -- Buscamos y traemos la información de inicio de sesión del usuario
    SELECT 
    ts.nombre as tipo_suscripcion,
    s.fecha_inicio,
    s.fecha_termino
    FROM SUSCRIPCIONES s
    JOIN TIPO_SUSCRIPCION ts ON s.id_tipo_suscripcion = ts.id_tipo_suscripcion
    WHERE s.id_usuario = p_id_usuario AND s.fecha_termino > NOW();

END$$

DROP PROCEDURE IF EXISTS `desbloquear_cliente`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `desbloquear_cliente` (IN `id_cliente` INT)   BEGIN
    UPDATE USUARIOS SET bloqueado = false WHERE USUARIOS.id_usuario = id_cliente;
END$$

DROP PROCEDURE IF EXISTS `eliminar_usuario`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `eliminar_usuario` (IN `id_usuario` INT(25))   BEGIN
	DELETE FROM SUSCRIPCIONES WHERE id_usuario = id_usuario;
    DELETE FROM PAGOS WHERE id_usuario = id_usuario;
    DELETE FROM usuarios WHERE usuarios.id_usuario = id_usuario;
END$$

DROP PROCEDURE IF EXISTS `insertar_suscripcion`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `insertar_suscripcion` (IN `p_id_usuario` INT, IN `p_tipo_suscripcion` VARCHAR(15), IN `p_fecha_inicio` DATE, IN `p_fecha_termino` DATE, IN `p_pago` FLOAT)   BEGIN
    DECLARE vi_tipo_suscripcion INT;
    DECLARE vi_pago INT;

    IF NOT EXISTS(SELECT TIPO_SUSCRIPCION.nombre FROM TIPO_SUSCRIPCION WHERE TIPO_SUSCRIPCION.nombre = p_tipo_suscripcion) THEN
        INSERT INTO TIPO_SUSCRIPCION (nombre) VALUES (p_tipo_suscripcion);
    END IF;

    SELECT TIPO_SUSCRIPCION.id_tipo_suscripcion INTO vi_tipo_suscripcion
    FROM TIPO_SUSCRIPCION
    WHERE TIPO_SUSCRIPCION.nombre = p_tipo_suscripcion;

    INSERT INTO PAGOS (nombre, id_usuario) VALUES(CONCAT("Suscripción ", p_tipo_suscripcion), p_id_usuario);
    SELECT LAST_INSERT_ID() INTO vi_pago;

    IF NOT EXISTS(SELECT SUSCRIPCIONES.id_tipo_suscripcion, SUSCRIPCIONES.fecha_inicio, SUSCRIPCIONES.fecha_termino, SUSCRIPCIONES.id_pago, SUSCRIPCIONES.id_usuario FROM SUSCRIPCIONES
    WHERE SUSCRIPCIONES.fecha_inicio = p_fecha_inicio AND SUSCRIPCIONES.fecha_termino = p_fecha_termino AND SUSCRIPCIONES.id_pago = vi_pago AND SUSCRIPCIONES.id_usuario = p_id_usuario) THEN
        INSERT INTO SUSCRIPCIONES (id_tipo_suscripcion, fecha_inicio, fecha_termino, id_pago, id_usuario) 
        VALUES (vi_tipo_suscripcion, p_fecha_inicio, p_fecha_termino, vi_pago, p_id_usuario);
    END IF;
END$$

DROP PROCEDURE IF EXISTS `insertar_usuario`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `insertar_usuario` (IN `p_nombre` VARCHAR(15), IN `p_apellido_paterno` VARCHAR(15), IN `p_apellido_materno` VARCHAR(25), IN `p_correo` VARCHAR(25), IN `p_contrasenia` VARCHAR(64), IN `p_telefono` BIGINT(10), IN `p_usuario_administrador` BOOLEAN, IN `p_calle` VARCHAR(20), IN `p_numero` INT(10), IN `p_codigo_postal` INT(10), IN `p_municipio` VARCHAR(20), IN `p_estado` VARCHAR(20), IN `p_edad` INT(10), IN `p_sexo` CHAR(1), IN `p_bloqueado` BOOLEAN, IN `p_estado_suscripcion` BOOLEAN)   BEGIN
    DECLARE id_nombre INT(25);
    DECLARE id_direccion INT(25);
    DECLARE id_municipio INT(25);
    DECLARE id_estado INT(25);
    
    DECLARE EXIT HANDLER FOR 1062
        BEGIN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El registro ya existe.';
        END;


    -- Validamos si existe el nombre con apellidos, si existe no se inserta de nuevo y se obtiene su ID
    -- pero si no existe se inserta y se obtiene el id
    IF NOT EXISTS(SELECT NOMBRES.nombre, NOMBRES.apellido_paterno, NOMBRES.apellido_materno FROM NOMBRES WHERE NOMBRES.nombre = p_nombre AND NOMBRES.apellido_paterno = p_apellido_paterno
    AND NOMBRES.apellido_materno = p_apellido_materno) THEN INSERT INTO NOMBRES (nombre, apellido_paterno, apellido_materno)
    VALUES (p_nombre, p_apellido_paterno, p_apellido_materno);
    END IF;
    BEGIN
        SELECT NOMBRES.id_nombre 
        INTO id_nombre
        FROM NOMBRES 
        WHERE NOMBRES.nombre = p_nombre AND NOMBRES.apellido_paterno = p_apellido_paterno
        AND NOMBRES.apellido_materno = p_apellido_materno;
    END;

    -- Validamos si existe el Estado y si no, lo insertamos
    IF NOT EXISTS(SELECT ESTADO.nombre FROM ESTADO WHERE ESTADO.nombre = p_estado)
    THEN INSERT INTO ESTADO(nombre) 
    VALUES (p_estado);
    END IF;
    BEGIN
        SELECT ESTADO.id_estado
        INTO id_estado
        FROM ESTADO 
        WHERE ESTADO.nombre = p_estado;
    END;

    -- Validamos si existe el Municipio y si no, lo insertamos
    IF NOT EXISTS(SELECT MUNICIPIO.nombre FROM MUNICIPIO WHERE MUNICIPIO.nombre = p_municipio)
    THEN INSERT INTO MUNICIPIO(nombre, id_estado)
    VALUES (p_municipio, id_estado);
    END IF;
    BEGIN
        SELECT MUNICIPIO.id_municipio
        INTO id_municipio
        FROM MUNICIPIO 
        WHERE MUNICIPIO.nombre = p_municipio AND MUNICIPIO.id_estado = id_estado;
    END;
    
    
    -- Validamos e insertamos la dirección
    IF NOT EXISTS(SELECT DIRECCION.calle, DIRECCION.numero, DIRECCION.codigo_postal, DIRECCION.id_municipio, DIRECCION.id_estado FROM DIRECCION 
    WHERE DIRECCION.calle = p_calle AND DIRECCION.numero = p_numero AND DIRECCION.codigo_postal = p_codigo_postal AND DIRECCION.id_municipio = id_municipio AND DIRECCION.id_estado = id_estado)
    THEN INSERT INTO DIRECCION(calle, numero, codigo_postal, id_municipio, id_estado) 
    VALUES (p_calle, p_numero, p_codigo_postal, id_municipio, id_estado);
    END IF;
    BEGIN
        SELECT DIRECCION.id_direccion 
        INTO id_direccion
        FROM DIRECCION 
        WHERE DIRECCION.calle = p_calle AND DIRECCION.numero = p_numero AND DIRECCION.codigo_postal = p_codigo_postal AND DIRECCION.id_municipio = id_municipio AND DIRECCION.id_estado = id_estado;
    END;

    -- Validamos e insertamos el usuario
    IF NOT EXISTS(SELECT USUARIOS.id_nombre, USUARIOS.correo, USUARIOS.contrasenia FROM USUARIOS 
    WHERE USUARIOS.id_nombre = id_nombre AND USUARIOS.correo = p_correo AND USUARIOS.contrasenia = p_contrasenia)
    THEN INSERT INTO USUARIOS (id_nombre, correo, contrasenia, telefono, usuario_administrador, id_direccion, edad, sexo, bloqueado, estado_suscripcion)
    VALUES (id_nombre, p_correo, p_contrasenia, p_telefono, p_usuario_administrador, id_direccion, p_edad, p_sexo, p_bloqueado, p_estado_suscripcion);
    END IF;
END$$

DROP PROCEDURE IF EXISTS `login_usuario`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `login_usuario` (IN `correo_usuario` VARCHAR(25))   BEGIN
    -- Buscamos y traemos la información de inicio de sesión del usuario
    SELECT 
    n.nombre as nombre,
    n.apellido_paterno as apellido_paterno,
    n.apellido_materno as apellido_materno,
    u.correo,
    u.contrasenia,
    u.telefono,
    u.usuario_administrador,
    d.calle as calle,
    d.numero as numero,
    d.codigo_postal as codigo_postal,
    m.nombre as municipio,
    e.nombre as estado,
    u.edad,
    u.sexo,
    u.bloqueado,
    u.estado_suscripcion,
    u.id_usuario
    FROM USUARIOS u 
    JOIN NOMBRES n ON u.id_nombre = n.id_nombre
    JOIN DIRECCION d ON u.id_direccion = d.id_direccion
    JOIN MUNICIPIO m ON d.id_municipio = m.id_municipio
    JOIN ESTADO e ON d.id_estado = e.id_estado

    WHERE upper(u.correo) = upper(correo_usuario);

END$$

DROP PROCEDURE IF EXISTS `obtener_planes`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `obtener_planes` ()   BEGIN

    SELECT nombre FROM tipo_suscripcion;

END$$

DROP PROCEDURE IF EXISTS `traer_estado`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `traer_estado` ()   BEGIN
    SELECT ESTADO.nombre FROM ESTADO ORDER BY ESTADO.nombre ASC;
END$$

DROP PROCEDURE IF EXISTS `traer_id_usuario`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `traer_id_usuario` (IN `correo_usuario` VARCHAR(25))   BEGIN
    -- Buscamos y traemos la información de inicio de sesión del usuario
    SELECT 
    u.id_usuario
    FROM USUARIOS u 
    WHERE u.correo = correo_usuario;

END$$

DROP PROCEDURE IF EXISTS `traer_municipio`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `traer_municipio` (IN `v_nombre_estado` VARCHAR(20))   BEGIN
	DECLARE v_id_estado INT(15);
    
    SELECT id_estado 
    INTO v_id_estado
    FROM ESTADO where ESTADO.nombre = v_nombre_estado;
	
    SELECT MUNICIPIO.nombre FROM MUNICIPIO WHERE id_estado = v_id_estado ORDER BY MUNICIPIO.nombre ASC;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `corte`
--

DROP TABLE IF EXISTS `corte`;
CREATE TABLE IF NOT EXISTS `corte` (
  `id_corte` int NOT NULL AUTO_INCREMENT,
  `fecha_corte` date DEFAULT NULL,
  `hora_cierre` date DEFAULT NULL,
  `ganancias` double DEFAULT NULL,
  `id_gasto` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_corte`),
  KEY `id_gasto` (`id_gasto`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `direccion`
--

DROP TABLE IF EXISTS `direccion`;
CREATE TABLE IF NOT EXISTS `direccion` (
  `id_direccion` int NOT NULL AUTO_INCREMENT,
  `calle` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `numero` int DEFAULT NULL,
  `codigo_postal` int DEFAULT NULL,
  `id_municipio` int DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  PRIMARY KEY (`id_direccion`),
  KEY `id_municipio` (`id_municipio`),
  KEY `id_estado` (`id_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `direccion`
--

INSERT INTO `direccion` (`id_direccion`, `calle`, `numero`, `codigo_postal`, `id_municipio`, `id_estado`) VALUES
(25, 'Abel Salazar', 113, 52300, 413, 12),
(26, 'Abel Salazar', 113, 52300, 1500, 23),
(27, 'Abel Salazar', 113, 52300, 1794, 31),
(28, 'Abel Salazar', 113, 52300, 245, 9),
(29, 'Abel Salazar', 113, 52300, 247, 9),
(30, 'Independencia', 100, 52340, 357, 12),
(31, 'Juárez', 158, 52300, 825, 15),
(32, 'Abel Salazar', 113, 52300, 236, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado`
--

DROP TABLE IF EXISTS `estado`;
CREATE TABLE IF NOT EXISTS `estado` (
  `id_estado` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estado`
--

INSERT INTO `estado` (`id_estado`, `nombre`) VALUES
(3, 'Aguascalientes'),
(4, 'Baja California'),
(5, 'Baja California Sur'),
(6, 'Campeche'),
(7, 'Chiapas'),
(8, 'Chihuahua'),
(9, 'Coahuila'),
(10, 'Colima'),
(11, 'Durango'),
(12, 'Estado de México'),
(13, 'Guanajuato'),
(14, 'Guerrero'),
(15, 'Hidalgo'),
(16, 'Jalisco'),
(17, 'Michoacán'),
(18, 'Morelos'),
(19, 'Nayarit'),
(20, 'Nuevo León'),
(21, 'Oaxaca'),
(22, 'Puebla'),
(23, 'Querétaro'),
(24, 'Quintana Roo'),
(25, 'San Luis Potosí'),
(26, 'Sinaloa'),
(27, 'Sonora'),
(28, 'Tabasco'),
(29, 'Tamaulipas'),
(30, 'Tlaxcala'),
(31, 'Veracruz'),
(32, 'Yucatán'),
(33, 'Zacatecas'),
(43, 'CDMX');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gastos`
--

DROP TABLE IF EXISTS `gastos`;
CREATE TABLE IF NOT EXISTS `gastos` (
  `id_gasto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `monto` float DEFAULT NULL,
  `fecha_gasto` date DEFAULT NULL,
  PRIMARY KEY (`id_gasto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `municipio`
--

DROP TABLE IF EXISTS `municipio`;
CREATE TABLE IF NOT EXISTS `municipio` (
  `id_municipio` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_estado` int NOT NULL,
  PRIMARY KEY (`id_municipio`),
  KEY `id_estado` (`id_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=2039 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `municipio`
--

INSERT INTO `municipio` (`id_municipio`, `nombre`, `id_estado`) VALUES
(13, 'Aguascalientes', 3),
(14, 'Asientos', 3),
(15, 'Calvillo', 3),
(16, 'Cosío', 3),
(17, 'Jesús María', 3),
(18, 'Pabellón de Arteaga', 3),
(19, 'Rincón de Romos', 3),
(20, 'San José de Gracia', 3),
(21, 'Tepezalá', 3),
(22, 'El Llano', 3),
(23, 'San Francisco de los', 3),
(24, 'Ensenada', 4),
(25, 'Mexicali', 4),
(26, 'Tecate', 4),
(27, 'Tijuana', 4),
(28, 'Playas del Rosarito', 4),
(29, 'San Quintín', 4),
(30, 'San Felipe', 4),
(31, 'Comondú', 5),
(32, 'Mulegé', 5),
(33, 'La Paz', 5),
(34, 'Los Cabos', 5),
(35, 'Loreto', 5),
(36, 'Calkiní', 6),
(37, 'Campeche', 6),
(38, 'Carmen', 6),
(39, 'Champotón', 6),
(40, 'Hecelchakán', 6),
(41, 'Hopelchén', 6),
(42, 'Palizada', 6),
(43, 'Tenabo', 6),
(44, 'Escárcega', 6),
(45, 'Calakmul', 6),
(46, 'Candelaria', 6),
(47, 'Seybaplaya', 6),
(48, 'Acacoyagua', 7),
(49, 'Acala', 7),
(50, 'Acapetahua', 7),
(51, 'Altamirano', 7),
(52, 'Amatán', 7),
(53, 'Amatenango de la Fro', 7),
(54, 'Amatenango del Valle', 7),
(55, 'Ángel Albino Corzo', 7),
(56, 'Arriaga', 7),
(57, 'Bejucal de Ocampo', 7),
(58, 'Bella Vista', 7),
(59, 'Berriozábal', 7),
(60, 'Bochil', 7),
(61, 'El Bosque', 7),
(62, 'Cacahoatán', 7),
(63, 'Catazajá', 7),
(64, 'Cintalapa', 7),
(65, 'Coapilla', 7),
(66, 'Comitán de Domínguez', 7),
(67, 'La Concordia', 7),
(68, 'Copainalá', 7),
(69, 'Chalchihuitán', 7),
(70, 'Chamula', 7),
(71, 'Chanal', 7),
(72, 'Chapultenango', 7),
(73, 'Chenalhó', 7),
(74, 'Chiapa de Corzo', 7),
(75, 'Chiapilla', 7),
(76, 'Chicoasén', 7),
(77, 'Chicomuselo', 7),
(78, 'Chilón', 7),
(79, 'Escuintla', 7),
(80, 'Francisco León', 7),
(81, 'Frontera Comalapa', 7),
(82, 'Frontera Hidalgo', 7),
(83, 'La Grandeza', 7),
(84, 'Huehuetán', 7),
(85, 'Huixtán', 7),
(86, 'Huitiupán', 7),
(87, 'Huixtla', 7),
(88, 'La Independencia', 7),
(89, 'Ixhuatán', 7),
(90, 'Ixtacomitán', 7),
(91, 'Ixtapa', 7),
(92, 'Ixtapangajoya', 7),
(93, 'Jiquipilas', 7),
(94, 'Jitotol', 7),
(95, 'Juárez', 7),
(96, 'Larráinzar', 7),
(97, 'La Libertad', 7),
(98, 'Mapastepec', 7),
(99, 'Las Margaritas', 7),
(100, 'Mazapa de Madero', 7),
(101, 'Mazatán', 7),
(102, 'Metapa', 7),
(103, 'Mitontic', 7),
(104, 'Motozintla', 7),
(105, 'Nicolás Ruíz', 7),
(106, 'Ocosingo', 7),
(107, 'Ocotepec', 7),
(108, 'Ocozocoautla de Espi', 7),
(109, 'Ostuacán', 7),
(110, 'Osumacinta', 7),
(111, 'Oxchuc', 7),
(112, 'Palenque', 7),
(113, 'Pantepec', 7),
(114, 'Pichucalco', 7),
(115, 'Pijijiapan', 7),
(116, 'El Porvenir', 7),
(117, 'Villa Comaltitlán', 7),
(118, 'Pueblo Nuevo Solista', 7),
(119, 'Rayón', 7),
(120, 'Reforma', 7),
(121, 'Las Rosas', 7),
(122, 'Sabanilla', 7),
(123, 'Salto de Agua', 7),
(124, 'San Cristóbal de las', 7),
(125, 'San Fernando', 7),
(126, 'Siltepec', 7),
(127, 'Simojovel', 7),
(128, 'Sitalá', 7),
(129, 'Socoltenango', 7),
(130, 'Solosuchiapa', 7),
(131, 'Soyaló', 7),
(132, 'Suchiapa', 7),
(133, 'Suchiate', 7),
(134, 'Sunuapa', 7),
(135, 'Tapachula', 7),
(136, 'Tapalapa', 7),
(137, 'Tapilula', 7),
(138, 'Tecpatán', 7),
(139, 'Tenejapa', 7),
(140, 'Teopisca', 7),
(141, 'Tila', 7),
(142, 'Tonalá', 7),
(143, 'Totolapa', 7),
(144, 'La Trinitaria', 7),
(145, 'Tumbalá', 7),
(146, 'Tuxtla Gutiérrez', 7),
(147, 'Tuxtla Chico', 7),
(148, 'Tuzantán', 7),
(149, 'Tzimol', 7),
(150, 'Unión Juárez', 7),
(151, 'Venustiano Carranza', 7),
(152, 'Villa Corzo', 7),
(153, 'Villaflores', 7),
(154, 'Yajalón', 7),
(155, 'San Lucas', 7),
(156, 'Zinacantán', 7),
(157, 'San Juan Cancuc', 7),
(158, 'Aldama', 7),
(159, 'Benemérito de las Am', 7),
(160, 'Maravilla Tenejapa', 7),
(161, 'Marqués de Comillas', 7),
(162, 'Montecristo de Guerr', 7),
(163, 'San Andrés Duraznal', 7),
(164, 'Santiago el Pinar', 7),
(165, 'Capitán Luis Ángel V', 7),
(166, 'Rincón Chamula San P', 7),
(167, 'El Parral', 7),
(168, 'Emiliano Zapata', 7),
(169, 'Mezcalapa', 7),
(170, 'Honduras de la Sierr', 7),
(171, 'Ahumada', 8),
(172, 'Aldama', 8),
(173, 'Allende	', 8),
(174, 'Aquiles Serdán', 8),
(175, 'Ascensión', 8),
(176, 'Bachíniva', 8),
(177, 'Balleza', 8),
(178, 'Batopilas de Manuel ', 8),
(179, 'Bocoyna', 8),
(180, 'Buenaventura', 8),
(181, 'Camargo', 8),
(182, 'Carichí', 8),
(183, 'Casas Grandes', 8),
(184, 'Coronado', 8),
(185, 'Coyame del Sotol', 8),
(186, 'La Cruz', 8),
(187, 'Cuauhtémoc', 8),
(188, 'Cusihuiriachi', 8),
(189, 'Chihuahua', 8),
(190, 'Chínipas', 8),
(191, 'Delicias', 8),
(192, 'Dr. Belisario Domíng', 8),
(193, 'Galeana', 8),
(194, 'Santa Isabel', 8),
(195, 'Gómez Farías', 8),
(196, 'Gran Morelos', 8),
(197, 'Guachochi', 8),
(198, 'Guadalupe', 8),
(199, 'Guadalupe y Calvo', 8),
(200, 'Guazapares', 8),
(201, 'Guerrero', 8),
(202, 'Hidalgo del Parral', 8),
(203, 'Huejotitán', 8),
(204, 'Ignacio Zaragoza', 8),
(205, 'Janos', 8),
(206, 'Jiménez', 8),
(207, 'Juárez', 8),
(208, 'Julimes', 8),
(209, 'López', 8),
(210, 'Madera', 8),
(211, 'Maguarichi', 8),
(212, 'Manuel Benavides', 8),
(213, 'Matachí', 8),
(214, 'Matamoros', 8),
(215, 'Meoqui', 8),
(216, 'Morelos', 8),
(217, 'Moris', 8),
(218, 'Namiquipa', 8),
(219, 'Nonoava', 8),
(220, 'Nuevo Casas Grandes', 8),
(221, 'Ocampo', 8),
(222, 'Ojinaga', 8),
(223, 'Praxedis G. Guerrero', 8),
(224, 'Riva Palacio', 8),
(225, 'Rosales', 8),
(226, 'Rosario', 8),
(227, 'San Francisco de Bor', 8),
(228, 'San Francisco de Con', 8),
(229, 'San Francisco del Or', 8),
(230, 'Santa Bárbara', 8),
(231, 'Satevó', 8),
(232, 'Saucillo', 8),
(233, 'Temósachic', 8),
(234, 'El Tule', 8),
(235, 'Urique', 8),
(236, 'Uruachi', 8),
(237, 'Valle de Zaragoza', 8),
(238, 'Abasolo', 9),
(239, 'Acuña', 9),
(240, 'Allende', 9),
(241, 'Arteaga', 9),
(242, 'Candela', 9),
(243, 'Castaños', 9),
(244, 'Cuatro ciénegas', 9),
(245, 'Escobedo', 9),
(246, 'Francisco I. Madero', 9),
(247, 'Frontera', 9),
(248, 'General Cepeda', 9),
(249, 'Guerrero', 9),
(250, 'Hidalgo', 9),
(251, 'Jiménez', 9),
(252, 'Juárez', 9),
(253, 'Lamadrid', 9),
(254, 'Matamoros', 9),
(255, 'Monclova', 9),
(256, 'Morelos', 9),
(257, 'Múzquiz', 9),
(258, 'Nadadores', 9),
(259, 'Nava', 9),
(260, 'Ocampo', 9),
(261, 'Parras', 9),
(262, 'Piedras Negras', 9),
(263, 'Progreso', 9),
(264, 'Ramos Arizpe', 9),
(265, 'Sabinas', 9),
(266, 'Sacramento', 9),
(267, 'Saltillo', 9),
(268, 'San Buenaventura', 9),
(269, 'San Juan de Sabinas', 9),
(270, 'San Pedro', 9),
(271, 'Sierra Mojada', 9),
(272, 'Torreón', 9),
(273, 'Viesca', 9),
(274, 'Villa Unión', 9),
(275, 'Zaragoza', 9),
(276, 'Armería', 10),
(277, 'Colima', 10),
(278, 'Comala', 10),
(279, 'Coquimatlán', 10),
(280, 'Cuauhtémoc', 10),
(281, 'Ixtlahuacán', 10),
(282, 'Manzanillo', 10),
(283, 'Minatitlán', 10),
(284, 'Tecomán', 10),
(285, 'Villa de Álvarez', 10),
(286, 'Canatlán', 11),
(287, 'Canelas', 11),
(288, 'Coneto de Comonfort', 11),
(289, 'Cuencamé', 11),
(290, 'Durango', 11),
(291, 'General Simón Bolíva', 11),
(292, 'Gómez Palacio', 11),
(293, 'Guadalupe Victoria', 11),
(294, 'Guanaceví', 11),
(295, 'Hidalgo', 11),
(296, 'Indé', 11),
(297, 'Lerdo', 11),
(298, 'Mapimí', 11),
(299, 'Mezquital', 11),
(300, 'Nazas', 11),
(301, 'Nombre de Dios', 11),
(302, 'Nuevo Ideal', 11),
(303, 'Ocampo', 11),
(304, 'El Oro', 11),
(305, 'Otáez', 11),
(306, 'Pánuco de Coronado', 11),
(307, 'Peñón Blanco', 11),
(308, 'Poanas', 11),
(309, 'Pueblo Nuevo', 11),
(310, 'Rodeo', 11),
(311, 'San Bernardo', 11),
(312, 'San Dimas', 11),
(313, 'San Juan de Guadalup', 11),
(314, 'San Juan del Río', 11),
(315, 'San Luis del Cordero', 11),
(316, 'San Pedro del Gallo', 11),
(317, 'Santa Clara', 11),
(318, 'Santiago Papasquiaro', 11),
(319, 'Súchil', 11),
(320, 'Tamazula', 11),
(321, 'Tepehuanes', 11),
(322, 'Tlahualilo', 11),
(323, 'Topia', 11),
(324, 'Vicente Guerrero', 11),
(325, 'Acambay', 12),
(326, 'Acolman', 12),
(327, 'Aculco', 12),
(328, 'Almoloya de Alquisir', 12),
(329, 'Almoloya de Juárez', 12),
(330, 'Almoloya del Río', 12),
(331, 'Amanalco', 12),
(332, 'Amatepec', 12),
(333, 'Amecameca', 12),
(334, 'Apaxco', 12),
(335, 'Atenco', 12),
(336, 'Atizapán', 12),
(337, 'Atizapán de Zaragoza', 12),
(338, 'Atlacomulco', 12),
(339, 'Atlautla', 12),
(340, 'Axapusco', 12),
(341, 'Ayapango', 12),
(342, 'Calimaya', 12),
(343, 'Capulhuac', 12),
(344, 'Coacalco de Berriozá', 12),
(345, 'Coatepec Harinas', 12),
(346, 'Cocotitlán', 12),
(347, 'Coyotepec', 12),
(348, 'Cuautitlán', 12),
(349, 'Chalco', 12),
(350, 'Chapa de Mota', 12),
(351, 'Chapultepec', 12),
(352, 'Chiautla', 12),
(353, 'Chicoloapan', 12),
(354, 'Chiconcuac', 12),
(355, 'Chimalhuacán', 12),
(356, 'Donato Guerra', 12),
(357, 'Ecatepec de Morelos', 12),
(358, 'Ecatzingo', 12),
(359, 'Huehuetoca', 12),
(360, 'Hueypoxtla', 12),
(361, 'Huixquilucan', 12),
(362, 'Isidro Fabela', 12),
(363, 'Ixtapaluca', 12),
(364, 'Ixtapan de la Sal', 12),
(365, 'Ixtapan del Oro', 12),
(366, 'Ixtlahuaca', 12),
(367, 'Jaltenco', 12),
(368, 'Jilotepec', 12),
(369, 'Jilotzingo', 12),
(370, 'Jiquipilco', 12),
(371, 'Jocotitlán', 12),
(372, 'Joquicingo', 12),
(373, 'Juchitepec', 12),
(374, 'Lerma', 12),
(375, 'Malinalco', 12),
(376, 'Melchor Ocampo', 12),
(377, 'Metepec', 12),
(378, 'Mexicaltzingo', 12),
(379, 'Morelos', 12),
(380, 'Naucalpan de Juárez', 12),
(381, 'Nezahualcóyotl', 12),
(382, 'Nextlalpan', 12),
(383, 'Nicolás Romero', 12),
(384, 'Nopaltepec', 12),
(385, 'Ocoyoacac', 12),
(386, 'Ocuilan', 12),
(387, 'El Oro', 12),
(388, 'Otumba', 12),
(389, 'Otzoloapan', 12),
(390, 'Otzolotepec', 12),
(391, 'Ozumba', 12),
(392, 'Papalotla', 12),
(393, 'La Paz', 12),
(394, 'Polotitlán', 12),
(395, 'Rayón', 12),
(396, 'San Antonio la Isla', 12),
(397, 'San Felipe del Progr', 12),
(398, 'San Martín de las Pi', 12),
(399, 'San Mateo Atenco', 12),
(400, 'San Simón de Guerrer', 12),
(401, 'Santo Tomás', 12),
(402, 'Soyaniquilpan de Juá', 12),
(403, 'Sultepec', 12),
(404, 'Tecámac', 12),
(405, 'Tejupilco', 12),
(406, 'Temamatla', 12),
(407, 'Temascalapa', 12),
(408, 'Temascalcingo', 12),
(409, 'Temascaltepec', 12),
(410, 'Temoaya', 12),
(411, 'Tenancingo', 12),
(412, 'Tenango del Aire', 12),
(413, 'Tenango del Valle', 12),
(414, 'Teoloyucan', 12),
(415, 'Teotihuacán', 12),
(416, 'Tepetlaoxtoc', 12),
(417, 'Tepetlixpa', 12),
(418, 'Tepotzotlán', 12),
(419, 'Tequixquiac', 12),
(420, 'Texcaltitlán', 12),
(421, 'Texcalyacac', 12),
(422, 'Texcoco', 12),
(423, 'Tezoyuca', 12),
(424, 'Tianguistenco', 12),
(425, 'Timilpan', 12),
(426, 'Tlalmanalco', 12),
(427, 'Tlalnepantla de Baz', 12),
(428, 'Tlatlaya', 12),
(429, 'Toluca', 12),
(430, 'Tonatico', 12),
(431, 'Tultepec', 12),
(432, 'Tultitlan', 12),
(433, 'Valle de Bravo', 12),
(434, 'Villa de Allende', 12),
(435, 'Villa del Carbon', 12),
(436, 'Villa Guerrero', 12),
(437, 'Villa Victoria', 12),
(438, 'Xonacatlán', 12),
(439, 'Zacazonapan', 12),
(440, 'Zacualpan', 12),
(441, 'Zinacantepec', 12),
(442, 'Zumpahuacán', 12),
(443, 'Zumpango', 12),
(444, 'Abasolo', 13),
(445, 'Acámbaro', 13),
(446, 'San Miguel de Allend', 13),
(447, 'Apaseo el Alto', 13),
(448, 'Apaseo el Grande', 13),
(449, 'Atarjea', 13),
(450, 'Celaya', 13),
(451, 'Manuel Doblado', 13),
(452, 'Comonfort', 13),
(453, 'Coroneo', 13),
(454, 'Cortazar', 13),
(455, 'Cuerámaro', 13),
(456, 'Doctor Mora', 13),
(457, 'Dolores Hidalgo Cuna', 13),
(458, 'Guanajuato', 13),
(459, 'Huanímaro', 13),
(460, 'Irapuato', 13),
(461, 'Jaral del Progreso', 13),
(462, 'Jerécuaro', 13),
(463, 'León', 13),
(464, 'Moroleón', 13),
(465, 'Ocampo', 13),
(466, 'Pénjamo', 13),
(467, 'Pueblo Nuevo', 13),
(468, 'Purísima del Rincón', 13),
(469, 'Romita', 13),
(470, 'Salamanca', 13),
(471, 'Salvatierra', 13),
(472, 'San Diego de la Unió', 13),
(473, 'San Felipe', 13),
(474, 'San Francisco del Ri', 13),
(475, 'San José Iturbide', 13),
(476, 'San Luis de la Paz', 13),
(477, 'Santa Catarina', 13),
(478, 'Santa Cruz de Juvent', 13),
(479, 'Santiago Maravatío', 13),
(480, 'Silao de la Victoria', 13),
(481, 'Tarandacuao', 13),
(482, 'Tarimoro', 13),
(483, 'Tierra Blanca', 13),
(484, 'Uriangato', 13),
(485, 'Valle de Santiago', 13),
(486, 'Victoria', 13),
(487, 'Villagrán', 13),
(488, 'Xichú', 13),
(489, 'Yuriria', 13),
(490, 'Acapulco de Juárez', 15),
(571, 'Acatlán', 16),
(572, 'Acaxochitlán', 15),
(573, 'Actopan', 15),
(574, 'Agua Blanca de Iturb', 15),
(575, 'Ajacuba', 15),
(576, 'Alfajayucan', 15),
(577, 'Almoloya', 15),
(578, 'Apan', 15),
(579, 'Atitalaquia', 15),
(580, 'Atlapexco', 15),
(581, 'Atotonilco de Tula', 15),
(582, 'Atotonilco el Grande', 15),
(583, 'Calnali', 15),
(584, 'Cardonal', 15),
(585, 'Chapantongo', 15),
(586, 'Chapulhuacán', 15),
(587, 'Chilcuautla', 15),
(588, 'Cuautepec de Hinojos', 15),
(589, 'El Arenal', 15),
(590, 'Eloxochitlán', 15),
(591, 'Emiliano Zapata', 15),
(592, 'Epazoyucan', 15),
(593, 'Francisco I. Madero', 15),
(594, 'Huasca de Ocampo', 15),
(595, 'Huautla', 15),
(596, 'Huazalingo', 15),
(597, 'Huehuetla', 15),
(598, 'Huejutla de Reyes', 15),
(599, 'Huichapan', 15),
(600, 'Ixmiquilpan', 15),
(601, 'Jacala de Ledezma', 15),
(602, 'Jaltocán', 15),
(603, 'Juárez Hidalgo', 15),
(604, 'La Misión', 15),
(605, 'Lolotla', 15),
(606, 'Metepec', 15),
(607, 'Metztitlán', 15),
(608, 'Mineral de la Reform', 15),
(609, 'Mineral del Chico', 15),
(610, 'Mineral del Monte', 15),
(611, 'Mixquiahuala de Juár', 15),
(612, 'Molango de Escamilla', 15),
(613, 'Nicolás Flores', 15),
(614, 'Nopala de Villagrán', 15),
(615, 'Omitlán de Juárez', 15),
(616, 'Pachuca de Soto', 15),
(617, 'Pacula', 15),
(618, 'Pisaflores', 15),
(619, 'Progreso de Obregón', 15),
(620, 'San Agustín Metzquit', 15),
(621, 'San Agustín Tlaxiaca', 15),
(622, 'San Bartolo Tutotepe', 15),
(623, 'San Felipe Orizatlán', 15),
(624, 'San Salvador', 15),
(625, 'Santiago de Anaya', 15),
(626, 'Santiago Tulantepec ', 15),
(627, 'Singuilucan', 15),
(628, 'Acatic', 16),
(686, 'Acapulco de Juárez', 15),
(687, 'Ahuacuotzingo', 14),
(688, 'Ajuchitlán del Progr', 14),
(689, 'Alcozauca de Guerrer', 14),
(690, 'Alpoyeca', 14),
(691, 'Apaxtla', 14),
(692, 'Arcelia', 14),
(693, 'Atenango del Río', 14),
(694, 'Atlamajalcingo del M', 14),
(695, 'Atlixtac', 14),
(696, 'Atoyac de Álvarez', 14),
(697, 'Ayutla de los Libres', 14),
(698, 'Azoyú', 14),
(699, 'Benito Juárez', 14),
(700, 'Buenavista de Cuélla', 14),
(701, 'Coahuayutla de José ', 14),
(702, 'Cocula', 14),
(703, 'Copala', 14),
(704, 'Copalillo', 14),
(705, 'Copanatoyac', 14),
(706, 'Coyuca de Benítez', 14),
(707, 'Coyuca de Catalán', 14),
(708, 'Cuajinicuilapa', 14),
(709, 'Cualác', 14),
(710, 'Cuautepec', 14),
(711, 'Cuetzala del Progres', 14),
(712, 'Cutzamala de Pinzón', 14),
(713, 'Chilapa de Álvarez', 14),
(714, 'Chilpancingo de los ', 14),
(715, 'Florencio Villarreal', 14),
(716, 'General Canuto A. Ne', 14),
(717, 'General Heliodoro Ca', 14),
(718, 'Huamuxtitlán', 14),
(719, 'Huitzuco de los Figu', 14),
(720, 'Iguala de la Indepen', 14),
(721, 'Igualapa', 14),
(722, 'Ixcateopan de Cuauht', 14),
(723, 'Zihuatanejo de Azuet', 14),
(724, 'Juan R. Escudero', 14),
(725, 'Leonardo Bravo', 14),
(726, 'Malinaltepec', 14),
(727, 'Mártir de Cuilapan', 14),
(728, 'Metlatónoc', 14),
(729, 'Mochitlán', 14),
(730, 'Olinalá', 14),
(731, 'Ometepec', 14),
(732, 'Pedro Ascencio Alqui', 14),
(733, 'Petatlán', 14),
(734, 'Pilcaya', 14),
(735, 'Pungarabato', 14),
(736, 'Quechultenango', 14),
(737, 'San Luis Acatlán', 14),
(738, 'San Marcos', 14),
(739, 'San Miguel Totolapan', 14),
(740, 'Taxco de Alarcón', 14),
(741, 'Tecoanapa', 14),
(742, 'Técpan de Galeana', 14),
(743, 'Teloloapan', 14),
(744, 'Tepecoacuilco de Tru', 14),
(745, 'Tetipac', 14),
(746, 'Tixtla de Guerrero', 14),
(747, 'Tlacoachistlahuaca', 14),
(748, 'Tlacoapa', 14),
(749, 'Tlalchapa', 14),
(750, 'Tlalixtaquilla de Ma', 14),
(751, 'Tlapa de Comonfort', 14),
(752, 'Tlapehuala', 14),
(753, 'La Unión de Isidoro ', 14),
(754, 'Xalpatláhuac', 14),
(755, 'Xochihuehuetlán', 14),
(756, 'Xochistlahuaca', 14),
(757, 'Zapotitlán Tablas', 14),
(758, 'Zirándaro', 14),
(759, 'Zitlala', 14),
(760, 'Eduardo Neri', 14),
(761, 'Acatepec', 14),
(762, 'Marquelia', 14),
(763, 'Cochoapa el Grande', 14),
(764, 'José Joaquín de Herr', 14),
(765, 'Juchitán', 14),
(766, 'Iliatenco', 14),
(767, 'Acatic', 16),
(768, 'Acatlán de Juárez', 16),
(769, 'alulco de Mercado', 16),
(770, 'Amacueca', 16),
(771, 'Amatitán', 16),
(772, 'Ameca', 16),
(773, 'Arandas', 16),
(774, 'Atemajac de Brizuela', 16),
(775, 'Atengo', 16),
(776, 'Atenguillo', 16),
(777, 'Atotonilco el Alto', 16),
(778, 'Atoyac', 16),
(779, 'Autlán de Navarro', 16),
(780, 'Ayotlán', 16),
(781, 'Ayutla', 16),
(782, 'Bolaños', 16),
(783, 'Cabo Corrientes', 16),
(784, 'Cañadas de Obregón', 16),
(785, 'Casimiro Castillo', 16),
(786, 'Chapala', 16),
(787, 'Chimaltitán', 16),
(788, 'Cihuatlán', 16),
(789, 'Cocula', 16),
(790, 'Colotlán', 16),
(791, 'Concepción de Buenos', 16),
(792, 'Cuautitlán de García', 16),
(793, 'Cuautla', 16),
(794, 'Cuquío', 16),
(795, 'Degollado', 16),
(796, 'Ejutla', 16),
(797, 'El Arenal', 16),
(798, 'El Grullo', 16),
(799, 'El Limón', 16),
(800, 'El Salto', 16),
(801, 'Encarnación de Díaz', 16),
(802, 'Etzatlán', 16),
(803, 'Gómez Farías', 16),
(804, 'Guachinango', 16),
(805, 'Guadalajara', 16),
(806, 'Hostotipaquillo', 16),
(807, 'Huejúcar', 16),
(808, 'Huejuquilla el Alto', 16),
(809, 'Ixtlahuacán de los M', 16),
(810, 'Ixtlahuacán del Río', 16),
(811, 'Jalostotitlán', 16),
(812, 'Jamay', 16),
(813, 'Jesús María', 16),
(814, 'Jilotlán de los Dolo', 16),
(815, 'Jocotepec', 16),
(816, 'Juanacatlán', 16),
(817, 'Juchitlán', 16),
(818, 'La Barca', 16),
(819, 'La Huerta', 16),
(820, 'La Manzanilla de la ', 16),
(821, 'Lagos de Moreno', 16),
(822, 'Magdalena', 16),
(823, 'Mascota', 16),
(824, 'Mazamitla', 16),
(825, 'Acuitzio', 15),
(826, 'Aguililla', 17),
(827, 'Álvaro Obregón', 17),
(828, 'Angamacutiro', 17),
(829, 'Angangueo', 17),
(830, 'Apatzingán', 17),
(831, 'Aporo', 17),
(832, 'Aquila', 17),
(833, 'Ario', 17),
(834, 'Arteaga', 17),
(835, 'Briseñas', 17),
(836, 'Buenavista', 17),
(837, 'Carácuaro', 17),
(838, 'Coahuayana', 17),
(839, 'Coalcomán de Vázquez', 17),
(840, 'Coeneo', 17),
(841, 'Contepec', 17),
(842, 'Copándaro', 17),
(843, 'Cotija', 17),
(844, 'Cuitzeo', 17),
(845, 'Charapan', 17),
(846, 'Charo', 17),
(847, 'Chavinda', 17),
(848, 'Cherán', 17),
(849, 'Chilchota', 17),
(850, 'Chinicuila', 17),
(851, 'Chucándiro', 17),
(852, 'Churintzio', 17),
(853, 'Churumuco', 17),
(854, 'Ecuandureo', 17),
(855, 'Epitacio Huerta', 17),
(856, 'Gabriel Zamora', 17),
(857, 'Hidalgo', 17),
(858, 'La Huacana', 17),
(859, 'Huandacareo', 17),
(860, 'Huaniqueo', 17),
(861, 'Huetamo', 17),
(862, 'Huiramba', 17),
(863, 'Indaparapeo', 17),
(864, 'Irimbo', 17),
(865, 'Ixtlán', 17),
(866, 'Jacona', 17),
(867, 'Jiménez', 17),
(868, 'Jiquilpan', 17),
(869, 'Juárez', 17),
(870, 'Jungapeo', 17),
(871, 'Lagunillas', 17),
(872, 'Madero', 17),
(873, 'Maravatío', 17),
(874, 'Marcos Castellanos', 17),
(875, 'Lázaro Cárdenas', 17),
(876, 'Morelia', 17),
(877, 'Morelos', 17),
(878, 'Múgica', 17),
(879, 'Nahuatzen', 17),
(880, 'Nocupétaro', 17),
(881, 'Nuevo Parangaricutir', 17),
(882, 'Nuevo Urecho', 17),
(883, 'Numarán', 17),
(884, 'Ocampo', 17),
(885, 'Pajacuarán', 17),
(886, 'Panindícuaro', 17),
(887, 'Parácuaro', 17),
(888, 'Paracho', 17),
(889, 'Pátzcuaro', 17),
(890, 'Penjamillo', 17),
(891, 'Peribán', 17),
(892, 'La Piedad', 17),
(893, 'Purépero', 17),
(894, 'Puruándiro', 17),
(895, 'Queréndaro', 17),
(896, 'Quiroga', 17),
(897, 'Cojumatlán de Régule', 17),
(898, 'Los Reyes', 17),
(899, 'Sahuayo', 17),
(900, 'San Lucas', 17),
(901, 'Santa Ana Maya', 17),
(902, 'Salvador Escalante', 17),
(903, 'Senguio', 17),
(904, 'Susupu', 17),
(905, 'Tacámbaro', 17),
(906, 'Tancítaro', 17),
(907, 'Tangamandapio', 17),
(908, 'Tangancícuaro', 17),
(909, 'Tanhuato', 17),
(910, 'Taretan', 17),
(911, 'Tarímbaro', 17),
(912, 'Tepalcatepec', 17),
(913, 'Tingambato', 17),
(914, 'Tingüindín', 17),
(915, 'Tiquicheo de Nicolás', 17),
(916, 'Tlalpujahua', 17),
(917, 'Tlazazalca', 17),
(918, 'Tocumbo', 17),
(919, 'Tumbiscatío', 17),
(920, 'Turicato', 17),
(921, 'Tuxpan', 17),
(922, 'Tuzantla', 17),
(923, 'Tzintzuntzan', 17),
(924, 'Tzitzio', 17),
(925, 'Uruapan', 17),
(926, 'Venustiano Carranza', 17),
(927, 'Villamar', 17),
(928, 'Vista Hermosa', 17),
(929, 'Yurécuaro', 17),
(930, 'Zacapu', 17),
(931, 'Zamora', 17),
(932, 'Zináparo', 17),
(933, 'Zinapécuaro', 17),
(934, 'Ziracuaretiro', 17),
(935, 'Zitácuaro', 17),
(936, 'Amacuzac', 18),
(937, 'Atlatlahucan', 18),
(938, 'Axochiapan', 18),
(939, 'Ayala', 18),
(940, 'Coatlán del Río', 18),
(941, 'Cuautla', 18),
(942, 'Cuernavaca', 18),
(943, 'Emiliano Zapata', 18),
(944, 'Huitzilac', 18),
(945, 'Jantetelco', 18),
(946, 'Jiutepec', 18),
(947, 'Jojutla', 18),
(948, 'Jonacatepec', 18),
(949, 'Mazatepec', 18),
(950, 'Miacatlán', 18),
(951, 'Ocuituco', 18),
(952, 'Puente de Ixtla', 18),
(953, 'Temixco', 18),
(954, 'Tepalcingo', 18),
(955, 'Tepoztlán', 18),
(956, 'Tetecala', 18),
(957, 'Tetela del Volcán', 18),
(958, 'Tlalnepantla', 18),
(959, 'Tlaltizapán de Zapat', 18),
(960, 'Tlaquiltenango', 18),
(961, 'Tlayacapan', 18),
(962, 'Totolapan', 18),
(963, 'Xochitepec', 18),
(964, 'Yautepec', 18),
(965, 'Yecapixtla', 18),
(966, 'Zacatepec', 18),
(967, 'Zacualpan de Amilpas', 18),
(968, 'Acaponeta', 19),
(969, 'Ahuacatlán', 19),
(970, 'Amatlán de Cañas', 19),
(971, 'Bahía de Banderas', 19),
(972, 'Compostela', 19),
(973, 'Del Nayar', 19),
(974, 'Huajicori', 19),
(975, 'Ixtlán del Río', 19),
(976, 'Jala', 19),
(977, 'La Yesca', 19),
(978, 'Rosamorada', 19),
(979, 'Ruíz', 19),
(980, 'San Blas', 19),
(981, 'San Pedro Lagunillas', 19),
(982, 'Santa María del Oro', 19),
(983, 'Santiago Ixcuintla', 19),
(984, 'Tecuala', 19),
(985, 'Tepic', 19),
(986, 'Tuxpan', 19),
(987, 'Xalisco', 19),
(988, 'Abasolo', 20),
(989, 'Agualeguas', 20),
(990, 'Allende', 20),
(991, 'Anáhuac', 20),
(992, 'Apodaca', 20),
(993, 'Aramberri', 20),
(994, 'Bustamante', 20),
(995, 'Cadereyta Jiménez', 20),
(996, 'Cerralvo', 20),
(997, 'China', 20),
(998, 'Ciénega de Flores', 20),
(999, 'Doctor Arroyo', 20),
(1000, 'Doctor Coss', 20),
(1001, 'Doctor González', 20),
(1002, 'El Carmen', 20),
(1003, 'Galeana', 20),
(1004, 'García', 20),
(1005, 'General Bravo', 20),
(1006, 'General Escobedo', 20),
(1007, 'General Terán', 20),
(1008, 'General Treviño', 20),
(1009, 'General Zaragoza', 20),
(1010, 'General Zuazua', 20),
(1011, 'Guadalupe', 20),
(1012, 'Hidalgo', 20),
(1013, 'Higueras', 20),
(1014, 'Hualahuises', 20),
(1015, 'Iturbide', 20),
(1016, 'Juárez', 20),
(1017, 'Lampazos de Naranjo', 20),
(1018, 'Linares', 20),
(1019, 'Los Aldamas', 20),
(1020, 'Los Herreras', 20),
(1021, 'Los Ramones', 20),
(1022, 'Marín', 20),
(1023, 'Melchor Ocampo', 20),
(1024, 'Mier y Noriega', 20),
(1025, 'Mina', 20),
(1026, 'Montemorelos', 20),
(1027, 'Monterrey', 20),
(1028, 'Parás', 20),
(1029, 'Pesquería', 20),
(1030, 'Rayones', 20),
(1031, 'Sabinas Hidalgo', 20),
(1032, 'Salinas Victoria', 20),
(1033, 'San Nicolás de los G', 20),
(1034, 'San Pedro Garza Garc', 20),
(1035, 'Santa Catarina', 20),
(1036, 'Santiago', 20),
(1037, 'Vallecillo', 20),
(1038, 'Villaldama', 20),
(1039, 'Abejones', 21),
(1040, 'Acatlán de Pérez Fig', 21),
(1041, 'Ánimas Trujano', 21),
(1042, 'Asunción Cacalotepec', 21),
(1043, 'Asunción Cuyotepeji', 21),
(1044, 'Asunción Ixtaltepec', 21),
(1045, 'Asunción Nochixtlán', 21),
(1046, 'Asunción Ocotlán', 21),
(1047, 'Asunción Tlacolulita', 21),
(1048, 'Ayoquezco de Aldama', 21),
(1049, 'Ayotzintepec', 21),
(1050, 'Calihualá', 21),
(1051, 'Candelaria Loxicha', 21),
(1052, 'Capulálpam de Méndez', 21),
(1053, 'Chahuites', 21),
(1054, 'Chalcatongo de Hidal', 21),
(1055, 'Chiquihuitlán de Ben', 21),
(1056, 'Ciénega de Zimatlán', 21),
(1057, 'Ciudad Ixtepec', 21),
(1058, 'Coatecas Altas', 21),
(1059, 'Coicoyán de las Flor', 21),
(1060, 'Concepción Buenavist', 21),
(1061, 'Concepción Pápalo', 21),
(1062, 'Constancia del Rosar', 21),
(1063, 'Cosolapa', 21),
(1064, 'Cosoltepec', 21),
(1065, 'Cuilápam de Guerrero', 21),
(1066, 'Cuyamecalco Villa de', 21),
(1067, 'El Barrio de la Sole', 21),
(1068, 'El Espinal', 21),
(1069, 'Eloxochitlán de Flor', 21),
(1070, 'Fresnillo de Trujano', 21),
(1071, 'Guadalupe de Ramírez', 21),
(1072, 'Guadalupe Etla', 21),
(1073, 'Guelatao de Juárez', 21),
(1074, 'Guevea de Humboldt', 21),
(1075, 'Heroica Ciudad de Ej', 21),
(1076, 'Heroica Ciudad de Hu', 21),
(1077, 'Heroica Ciudad de Ju', 21),
(1078, 'Heroica Ciudad de Tl', 21),
(1079, 'Huautepec', 21),
(1080, 'Huautla de Jiménez', 21),
(1081, 'Ixpantepec Nieves', 21),
(1082, 'Ixtlán de Juárez', 21),
(1083, 'Juchitán de Zaragoza', 21),
(1084, 'La Compañía', 21),
(1085, 'La Pe', 21),
(1086, 'La Reforma', 21),
(1087, 'La Trinidad Vista He', 21),
(1088, 'Loma Bonita', 21),
(1089, 'Magdalena Apasco', 21),
(1090, 'Magdalena Jaltepec', 21),
(1091, 'Magdalena Mixtepec', 21),
(1092, 'Magdalena Ocotlán', 21),
(1093, 'Magdalena Peñasco', 21),
(1094, 'Magdalena Teitipac', 21),
(1095, 'Magdalena Tequisistl', 21),
(1096, 'Magdalena Tlacotepec', 21),
(1097, 'Magdalena Yodocono d', 21),
(1098, 'Magdalena Zahuatlán', 21),
(1099, 'Mariscala de Juárez', 21),
(1100, 'Mártires de Tacubaya', 21),
(1101, 'Matías Romero Avenda', 21),
(1102, 'Mazatlán Villa de Fl', 21),
(1103, 'Mesones Hidalgo', 21),
(1104, 'Miahuatlán de Porfir', 21),
(1105, 'Mixistlán de la Refo', 21),
(1106, 'Monjas', 21),
(1107, 'Natividad', 21),
(1108, 'Nazareno Etla', 21),
(1109, 'Nejapa de Madero', 21),
(1110, 'Nuevo Zoquiápam', 21),
(1111, 'Oaxaca de Juárez', 21),
(1112, 'Ocotlán de Morelos', 21),
(1113, 'Pinotepa de Don Luis', 21),
(1114, 'Pluma Hidalgo', 21),
(1115, 'Putla Villa de Guerr', 21),
(1116, 'Reforma de Pineda', 21),
(1117, 'Reyes Etla', 21),
(1118, 'Rojas de Cuauhtémoc', 21),
(1119, 'Salina Cruz', 21),
(1120, 'San Agustín Amatengo', 21),
(1121, 'San Agustín Atenango', 21),
(1122, 'San Agustín Chayuco', 21),
(1123, 'San Agustín de las J', 21),
(1124, 'San Agustín Etla', 21),
(1125, 'San Agustín Loxicha', 21),
(1126, 'San Agustín Tlacotep', 21),
(1127, 'San Agustín Yatareni', 21),
(1128, 'San Andrés Cabecera ', 21),
(1129, 'San Andrés Dinicuiti', 21),
(1130, 'San Andrés Huaxpalte', 21),
(1131, 'San Andrés Huayápam', 21),
(1132, 'San Andrés Ixtlahuac', 21),
(1133, 'San Andrés Lagunas', 21),
(1134, 'San Andrés Nuxiño', 21),
(1135, 'San Andrés Paxtlán', 21),
(1136, 'San Andrés Sinaxtla', 21),
(1137, 'San Andrés Solaga', 21),
(1138, 'San Andrés Teotilálp', 21),
(1139, 'San Andrés Tepetlapa', 21),
(1140, 'San Andrés Yaá', 21),
(1141, 'San Andrés Zabache', 21),
(1142, 'San Andrés Zautla', 21),
(1143, 'San Antonino Castill', 21),
(1144, 'San Antonino el Alto', 21),
(1145, 'San Antonino Monte V', 21),
(1146, 'San Antonio Acutla', 21),
(1147, 'San Antonio de la Ca', 21),
(1148, 'San Antonio Huitepec', 21),
(1149, 'San Antonio Nanahuat', 21),
(1150, 'San Antonio Sinicahu', 21),
(1151, 'San Antonio Tepetlap', 21),
(1152, 'San Baltazar Chichic', 21),
(1153, 'San Baltazar Loxicha', 21),
(1154, 'San Baltazar Yatzach', 21),
(1155, 'San Bartolo Coyotepe', 21),
(1156, 'San Bartolo Soyaltep', 21),
(1157, 'San Bartolo Yautepec', 21),
(1158, 'San Bartolomé Ayautl', 21),
(1159, 'San Bartolomé Loxich', 21),
(1160, 'San Bartolomé Quiala', 21),
(1161, 'San Bartolomé Loxich', 21),
(1162, 'San Bartolomé Zoogoc', 21),
(1163, 'San Bartolo Soyaltep', 21),
(1164, 'San Bartolo Yautepec', 21),
(1165, 'San Bernardo Mixtepe', 21),
(1166, 'San Blas Atempa', 21),
(1167, 'San Carlos Yautepec', 21),
(1168, 'San Cristóbal Amatlá', 21),
(1169, 'San Cristóbal Amolte', 21),
(1170, 'San Cristóbal Lachir', 21),
(1171, 'San Cristóbal Suchix', 21),
(1172, 'San Dionisio del Mar', 21),
(1173, 'San Dionisio Ocotepe', 21),
(1174, 'San Esteban Atatlahu', 21),
(1175, 'San Felipe Jalapa de', 21),
(1176, 'San Felipe Tejalápam', 21),
(1177, 'San Felipe Usila', 21),
(1178, 'San Francisco Cahuac', 21),
(1179, 'San Francisco Cajono', 21),
(1180, 'San Francisco Chapul', 21),
(1181, 'San Francisco Chindú', 21),
(1182, 'San Francisco del Ma', 21),
(1183, 'San Francisco Huehue', 21),
(1184, 'San Francisco Ixhuat', 21),
(1185, 'San Francisco Jaltep', 21),
(1186, 'San Francisco Lachig', 21),
(1187, 'San Francisco Loguec', 21),
(1188, 'San Francisco Nuxaño', 21),
(1189, 'San Francisco Ozolot', 21),
(1190, 'San Francisco Sola', 21),
(1191, 'San Francisco Telixt', 21),
(1192, 'San Francisco Teopan', 21),
(1193, 'San Francisco Tlapan', 21),
(1194, 'San Gabriel Mixtepec', 21),
(1195, 'San Ildefonso Amatlá', 21),
(1196, 'San Ildefonso Sola', 21),
(1197, 'San Ildefonso Villa ', 21),
(1198, 'San Jacinto Amilpas', 21),
(1199, 'San Jacinto Tlacotep', 21),
(1200, 'San Jerónimo Coatlán', 21),
(1201, 'San Jerónimo Silacay', 21),
(1202, 'San Jerónimo Sosola', 21),
(1203, 'San Jerónimo Taviche', 21),
(1204, 'San Jerónimo Tecóatl', 21),
(1205, 'San Jorge Nuchita', 21),
(1206, 'San José Ayuquila', 21),
(1207, 'San José Chiltepec', 21),
(1208, 'San José del Peñasco', 21),
(1209, 'San José del Progres', 21),
(1210, 'San José Estancia Gr', 21),
(1211, 'San José Independenc', 21),
(1212, 'San José Lachiguiri', 21),
(1213, 'San José Tenango', 21),
(1214, 'San Juan Ñumí', 21),
(1215, 'San Juan Achiutla', 21),
(1216, 'San Juan Atepec', 21),
(1217, 'San Juan Bautista At', 21),
(1218, 'San Juan Bautista Co', 21),
(1219, 'San Juan Bautista Cu', 21),
(1220, 'San Juan Bautista Gu', 21),
(1221, 'San Juan Bautista Ja', 21),
(1222, 'San Juan Bautista Lo', 21),
(1223, 'San Juan Bautista Su', 21),
(1224, 'San Juan Bautista Tl', 21),
(1225, 'San Juan Bautista Tl', 21),
(1226, 'San Juan Bautista Tu', 21),
(1227, 'San Juan Bautista Va', 21),
(1228, 'San Juan Cacahuatepe', 21),
(1229, 'San Juan Chicomezúch', 21),
(1230, 'San Juan Chilateca', 21),
(1231, 'San Juan Cieneguilla', 21),
(1232, 'San Juan Coatzóspam', 21),
(1233, 'San Juan Colorado', 21),
(1234, 'San Juan Comaltepec', 21),
(1235, 'San Juan Cotzocón', 21),
(1236, 'San Juan de los Cués', 21),
(1237, 'San Juan del Estado', 21),
(1238, 'San Juan del Río', 21),
(1239, 'San Juan Diuxi', 21),
(1240, 'San Juan Evangelista', 21),
(1241, 'San Juan Guelavía', 21),
(1242, 'San Juan Guichicovi', 21),
(1243, 'San Juan Ihualtepec', 21),
(1244, 'San Juan Juquila Mix', 21),
(1245, 'San Juan Juquila Vij', 21),
(1246, 'San Juan Lachao', 21),
(1247, 'San Juan Lachigalla', 21),
(1248, 'San Juan Lajarcia', 21),
(1249, 'San Juan Lalana', 21),
(1250, 'San Juan Mazatlán', 21),
(1251, 'San Juan Mixtepec -D', 21),
(1252, 'San Juan Mixtepec -D', 21),
(1253, 'San Juan Ñumí', 21),
(1254, 'San Juan Ozolotepec', 21),
(1255, 'San Juan Petlapa', 21),
(1256, 'San Juan Quiahije', 21),
(1257, 'San Juan Quiotepec', 21),
(1258, 'San Juan Sayultepec', 21),
(1259, 'San Juan Tabaá', 21),
(1260, 'San Juan Tamazola', 21),
(1261, 'San Juan Teita', 21),
(1262, 'San Juan Teitipac', 21),
(1263, 'San Juan Tepeuxila', 21),
(1264, 'San Juan Teposcolula', 21),
(1265, 'San Juan Yaeé', 21),
(1266, 'San Juan Yatzona', 21),
(1267, 'San Juan Yucuita', 21),
(1268, 'San Lorenzo', 21),
(1269, 'San Lorenzo Albarrad', 21),
(1270, 'San Lorenzo Cacaotep', 21),
(1271, 'San Lorenzo Cuaunecu', 21),
(1272, 'San Lorenzo Texmelúc', 21),
(1273, 'San Lorenzo Victoria', 21),
(1274, 'San Lucas Camotlán', 21),
(1275, 'San Lucas Ojitlán', 21),
(1276, 'San Lucas Quiaviní', 21),
(1277, 'San Lucas Zoquiápam', 21),
(1278, 'San Luis Amatlán', 21),
(1279, 'San Marcial Ozolotep', 21),
(1280, 'San Marcial Ozolotep', 21),
(1281, 'San Mateo Nejápam', 21),
(1282, 'San Mateo Peñasco', 21),
(1283, 'San Mateo Piñas', 21),
(1284, 'San Mateo Río Hondo', 21),
(1285, 'San Mateo Sindihui', 21),
(1286, 'San Mateo Tlapiltepe', 21),
(1287, 'San Melchor Betaza', 21),
(1288, 'San Miguel Achiutla', 21),
(1289, 'San Miguel Ahuehueti', 21),
(1290, 'San Miguel Aloápam', 21),
(1291, 'San Miguel Amatitlán', 21),
(1292, 'San Miguel Amatlán', 21),
(1293, 'San Miguel Chicahua', 21),
(1294, 'San Miguel Chimalapa', 21),
(1295, 'San Miguel Coatlán', 21),
(1296, 'San Miguel del Puert', 21),
(1297, 'San Miguel del Río', 21),
(1298, 'San Miguel Ejutla', 21),
(1299, 'San Miguel el Grande', 21),
(1300, 'San Miguel Huautla', 21),
(1301, 'San Miguel Mixtepec', 21),
(1302, 'San Miguel Panixtlah', 21),
(1303, 'San Miguel Peras', 21),
(1304, 'San Miguel Piedras', 21),
(1305, 'San Miguel Quetzalte', 21),
(1306, 'San Miguel Santa Flo', 21),
(1307, 'San Miguel Soyaltepe', 21),
(1308, 'San Miguel Suchixtep', 21),
(1309, 'San Miguel Tecomatlá', 21),
(1310, 'San Miguel Tenango', 21),
(1311, 'San Miguel Tequixtep', 21),
(1312, 'San Miguel Tilquiápa', 21),
(1313, 'San Miguel Tlacamama', 21),
(1314, 'San Miguel Tlacotepe', 21),
(1315, 'San Miguel Tulancing', 21),
(1316, 'San Miguel Yotao', 21),
(1317, 'San Nicolás', 21),
(1318, 'San Nicolás Hidalgo', 21),
(1319, 'San Pablo Coatlán', 21),
(1320, 'San Pablo Cuatro Ven', 21),
(1321, 'San Pablo Etla', 21),
(1322, 'San Pablo Huitzo', 21),
(1323, 'San Pablo Huixtepec', 21),
(1324, 'San Pablo Macuiltian', 21),
(1325, 'San Pablo Tijaltepec', 21),
(1326, 'San Pablo Villa de M', 21),
(1327, 'San Pablo Yaganiza', 21),
(1328, 'San Pedro Amuzgos', 21),
(1329, 'San Pedro Apóstol', 21),
(1330, 'San Pedro Atoyac', 21),
(1331, 'San Pedro Cajonos', 21),
(1332, 'San Pedro Comitancil', 21),
(1333, 'San Pedro Coxcaltepe', 21),
(1334, 'San Pedro el Alto', 21),
(1335, 'San Pedro Huamelula', 21),
(1336, 'San Pedro Huilotepec', 21),
(1337, 'San Pedro Ixcatlán', 21),
(1338, 'San Pedro Ixtlahuaca', 21),
(1339, 'San Pedro Jaltepeton', 21),
(1340, 'San Pedro Jicayán', 21),
(1341, 'San Pedro Jocotipac', 21),
(1342, 'San Pedro Juchatengo', 21),
(1343, 'San Pedro Mártir', 21),
(1344, 'San Pedro Mártir Qui', 21),
(1345, 'San Pedro Mártir Yuc', 21),
(1346, 'San Pedro Mártir Qui', 21),
(1347, 'San Pedro Tapanatepe', 21),
(1348, 'San Pedro Taviche', 21),
(1349, 'San Pedro Teozacoalc', 21),
(1350, 'San Pedro Teutila', 21),
(1351, 'San Pedro Tidaá', 21),
(1352, 'San Pedro Topiltepec', 21),
(1353, 'San Pedro Totolápam', 21),
(1354, 'San Pedro y San Pabl', 21),
(1355, 'San Pedro y San Pabl', 21),
(1356, 'San Pedro y San Pabl', 21),
(1357, 'San Pedro Yaneri', 21),
(1358, 'San Pedro Yólox', 21),
(1359, 'San Pedro Yucunama', 21),
(1360, 'San Raymundo Jalpan', 21),
(1361, 'San Sebastián Abasol', 21),
(1362, 'San Sebastián Coatlá', 21),
(1363, 'San Sebastián Ixcapa', 21),
(1364, 'San Sebastián Nicana', 21),
(1365, 'San Sebastián Río Ho', 21),
(1366, 'San Sebastián Tecoma', 21),
(1367, 'San Sebastián Teitip', 21),
(1368, 'San Sebastián Tutla', 21),
(1369, 'San Simón Almolongas', 21),
(1370, 'San Simón Zahuatlán', 21),
(1371, 'Santa Ana', 21),
(1372, 'Santa Ana Ateixtlahu', 21),
(1373, 'Santa Ana Cuauhtémoc', 21),
(1374, 'Santa Ana del Valle', 21),
(1375, 'Santa Ana Tavela', 21),
(1376, 'Santa Ana Tlapacoyan', 21),
(1377, 'Santa Ana Yareni', 21),
(1378, 'Santa Ana Zegache', 21),
(1379, 'Santa Catalina Quier', 21),
(1380, 'Santa Catarina Cuixt', 21),
(1381, 'Santa Catarina Ixtep', 21),
(1382, 'Santa Catarina Juqui', 21),
(1383, 'Santa Catarina Lacha', 21),
(1384, 'Santa Catarina Loxic', 21),
(1385, 'Santa Catarina Mecho', 21),
(1386, 'Santa Catarina Minas', 21),
(1387, 'Santa Catarina Quian', 21),
(1388, 'Santa Catarina Quioq', 21),
(1389, 'Santa Catarina Tayat', 21),
(1390, 'Santa Catarina Ticuá', 21),
(1391, 'Santa Catarina Yoson', 21),
(1392, 'Santa Catarina Zapoq', 21),
(1393, 'Santa Cruz Acatepec', 21),
(1394, 'Santa Cruz Amilpas', 21),
(1395, 'Santa Cruz de Bravo', 21),
(1396, 'Santa Cruz Itundujia', 21),
(1397, 'Santa Cruz Mixtepec', 21),
(1398, 'Santa Cruz Nundaco', 21),
(1399, 'Santa Cruz Papalutla', 21),
(1400, 'Santa Cruz Tacache d', 21),
(1401, 'Santa Cruz Tacahua', 21),
(1402, 'Acajete', 22),
(1403, 'Acateno', 22),
(1404, 'Acatlán', 22),
(1405, 'Acatzingo', 22),
(1406, 'Acteopan', 22),
(1407, 'Ahuacatlán', 22),
(1408, 'Ahuatlán', 22),
(1409, 'Ahuazotepec', 22),
(1410, 'Ahuehuetitla', 22),
(1411, 'Ajalpan', 22),
(1412, 'Albino Zertuche', 22),
(1413, 'Aljojuca', 22),
(1414, 'Altepexi', 22),
(1415, 'Amixtlán', 22),
(1416, 'Amozoc', 22),
(1417, 'Aquixtla', 22),
(1418, 'Atempan', 22),
(1419, 'Atexcal', 22),
(1420, 'Atlixco', 22),
(1421, 'Atoyatempan', 22),
(1422, 'Atzala', 22),
(1423, 'Atzitzihuacán', 22),
(1424, 'Atzitzintla', 22),
(1425, 'Axutla', 22),
(1426, 'Ayotoxco de Guerrero', 22),
(1427, 'Calpan', 22),
(1428, 'Caltepec', 22),
(1429, 'Camocuautla', 22),
(1430, 'Cañada Morelos', 22),
(1431, 'Caxhuacan', 22),
(1432, 'Chalchicomula de Ses', 22),
(1433, 'Chapulco', 22),
(1434, 'Chiautla', 22),
(1435, 'Chiautzingo', 22),
(1436, 'Chiconcuautla', 22),
(1437, 'Chichiquila', 22),
(1438, 'Chietla', 22),
(1439, 'Chigmecatitlán', 22),
(1440, 'Chignahuapan', 22),
(1441, 'Chignautla', 22),
(1442, 'Chila', 22),
(1443, 'Chila de la Sal', 22),
(1444, 'Chilchotla', 22),
(1445, 'Chinantla', 22),
(1446, 'Coatepec', 22),
(1447, 'Coatzingo', 22),
(1448, 'Cohetzala', 22),
(1449, 'Cohuecan', 22),
(1450, 'Coronango', 22),
(1451, 'Coxcatlán', 22),
(1452, 'Coyomeapan', 22),
(1453, 'Coyotepec', 22),
(1454, 'Cuapiaxtla de Madero', 22),
(1455, 'Cuautempan', 22),
(1456, 'Cuautinchán', 22),
(1457, 'Cuautlancingo', 22),
(1458, 'Cuayuca de Andrade', 22),
(1459, 'Cuetzalan del Progre', 22),
(1460, 'Cuyoaco', 22),
(1461, 'Domingo Arenas', 22),
(1462, 'Eloxochitlán', 22),
(1463, 'Epatlán', 22),
(1464, 'Esperanza', 22),
(1465, 'Francisco Z. Mena', 22),
(1466, 'General Felipe Ángel', 22),
(1467, 'Guadalupe', 22),
(1468, 'Guadalupe Victoria', 22),
(1469, 'Hermenegildo Galeana', 22),
(1470, 'Honey', 22),
(1471, 'Huaquechula', 22),
(1472, 'Huatlatlauca', 22),
(1473, 'Huauchinango', 22),
(1474, 'Huehuetla', 22),
(1475, 'Huehuetlán el Chico', 22),
(1476, 'Huejotzingo', 22),
(1477, 'Hueyapan', 22),
(1478, 'Hueytamalco', 22),
(1479, 'Hueytlalpan', 22),
(1480, 'Huitzilan de Serdán', 22),
(1481, 'Huitziltepec', 22),
(1482, 'Ixcamilpa de Guerrer', 22),
(1483, 'Ixcaquixtla', 22),
(1484, 'Ixtacamaxtitlán', 22),
(1485, 'Ixtepec', 22),
(1486, 'Izúcar de Matamor', 22),
(1487, 'Amealco de Bonfil', 23),
(1488, 'Arroyo Seco', 23),
(1489, 'Cadereyta de Montes', 23),
(1490, 'Colón', 23),
(1491, 'Corregidora', 23),
(1492, 'El Marqués', 23),
(1493, 'Ezequiel Montes', 23),
(1494, 'Huimilpan', 23),
(1495, 'Jalpan de Serra', 23),
(1496, 'Landa de Matamoros', 23),
(1497, 'Pedro Escobedo', 23),
(1498, 'Peñamiller', 23),
(1499, 'Pinal de Amoles', 23),
(1500, 'Querétaro', 23),
(1501, 'San Joaquín', 23),
(1502, 'San Juan del Río', 23),
(1503, 'Tequisquiapan', 23),
(1504, 'Tolimán', 23),
(1505, 'Cozumel', 24),
(1506, 'Chetumal', 24),
(1507, 'Felipe Carrillo Puer', 24),
(1508, 'Isla Mujeres', 24),
(1509, 'José María Morelos', 24),
(1510, 'Lázaro Cárdenas', 24),
(1511, 'Othón P. Blanco', 24),
(1512, 'Solidaridad', 24),
(1513, 'Tulum', 24),
(1514, 'Bacalar', 24),
(1515, 'Cozumel', 24),
(1516, 'Chetumal', 24),
(1517, 'Felipe Carrillo Puer', 24),
(1518, 'Isla Mujeres', 24),
(1519, 'José María Morelos', 24),
(1520, 'Lázaro Cárdenas', 24),
(1521, 'Othón P. Blanco', 24),
(1522, 'Solidaridad', 24),
(1523, 'Tulum', 24),
(1524, 'Bacalar', 24),
(1525, 'Ahualulco', 25),
(1526, 'Alaquines', 25),
(1527, 'Aquismón', 25),
(1528, 'Armadillo de los Inf', 25),
(1529, 'Cárdenas', 25),
(1530, 'Catorce', 25),
(1531, 'Cedral', 25),
(1532, 'Cerritos', 25),
(1533, 'Cerro de San Pedro', 25),
(1534, 'Charcas', 25),
(1535, 'Ciudad del Maíz', 25),
(1536, 'Ciudad Fernández', 25),
(1537, 'Ciudad Valles', 25),
(1538, 'Coxcatlán', 25),
(1539, 'Ebano', 25),
(1540, 'El Naranjo', 25),
(1541, 'Guadalcázar', 25),
(1542, 'Huehuetlán', 25),
(1543, 'Lagunillas', 25),
(1544, 'Matehuala', 25),
(1545, 'Matlapa', 25),
(1546, 'Mexquitic de Carmona', 25),
(1547, 'Moctezuma', 25),
(1548, 'Rayón', 25),
(1549, 'Rioverde', 25),
(1550, 'Salinas', 25),
(1551, 'San Antonio', 25),
(1552, 'San Ciro de Acosta', 25),
(1553, 'San Luis Potosí', 25),
(1554, 'San Martín Chalchicu', 25),
(1555, 'San Nicolás Tolentin', 25),
(1556, 'San Vicente Tancuaya', 25),
(1557, 'Santa Catarina', 25),
(1558, 'Santa María del Río', 25),
(1559, 'Santo Domingo', 25),
(1560, 'Soledad de Graciano ', 25),
(1561, 'Tamasopo', 25),
(1562, 'Tamazunchale', 25),
(1563, 'Tampacán', 25),
(1564, 'Tampamolón Corona', 25),
(1565, 'Tamuín', 25),
(1566, 'Tancanhuitz', 25),
(1567, 'Tanlajás', 25),
(1568, 'Tanquián de Escobedo', 25),
(1569, 'Tierra Nueva', 25),
(1570, 'Vanegas', 25),
(1571, 'Venado', 25),
(1572, 'Villa de Arriaga', 25),
(1573, 'Villa de Arista', 25),
(1574, 'Villa de Guadalupe', 25),
(1575, 'Villa de la Paz', 25),
(1576, 'Villa de Ramos', 25),
(1577, 'Villa de Reyes', 25),
(1578, 'Villa Hidalgo', 25),
(1579, 'Villa Juárez', 25),
(1580, 'Xilitla', 25),
(1581, 'Ahome', 26),
(1582, 'Angostura', 26),
(1583, 'Badiraguato', 26),
(1584, 'Choix', 26),
(1585, 'Concordia', 26),
(1586, 'Cosalá', 26),
(1587, 'Culiacán', 26),
(1588, 'Elota', 26),
(1589, 'Escuinapa', 26),
(1590, 'Guasave', 26),
(1591, 'Mazatlán', 26),
(1592, 'Mocorito', 26),
(1593, 'Navolato', 26),
(1594, 'Aconchi', 27),
(1595, 'Agua Prieta', 27),
(1596, 'Alamos', 27),
(1597, 'Altar', 27),
(1598, 'Arivechi', 27),
(1599, 'Arizpe', 27),
(1600, 'Atil', 27),
(1601, 'Bacadéhuachi', 27),
(1602, 'Bacanora', 27),
(1603, 'Bacerac', 27),
(1604, 'Bacoachi', 27),
(1605, 'Bácum', 27),
(1606, 'Banámichi', 27),
(1607, 'Baviácora', 27),
(1608, 'Bavispe', 27),
(1609, 'Benito Juárez', 27),
(1610, 'Caborca', 27),
(1611, 'Cajeme', 27),
(1612, 'Cananea', 27),
(1613, 'Carbó', 27),
(1614, 'Cucurpe', 27),
(1615, 'Divisaderos', 27),
(1616, 'Empalme', 27),
(1617, 'Etchojoa', 27),
(1618, 'Fronteras', 27),
(1619, 'Granados', 27),
(1620, 'Guaymas', 27),
(1621, 'Hermosillo', 27),
(1622, 'Huachinera', 27),
(1623, 'Huásabas', 27),
(1624, 'Huatabampo', 27),
(1625, 'Huépac', 27),
(1626, 'Imuris', 27),
(1627, 'La Colorada', 27),
(1628, 'Magdalena', 27),
(1629, 'Mazatán', 27),
(1630, 'Moctezuma', 27),
(1631, 'Naco', 27),
(1632, 'Nácori Chico', 27),
(1633, 'Nacozari de García', 27),
(1634, 'Navojoa', 27),
(1635, 'Nogales', 27),
(1636, 'Onavas', 27),
(1637, 'Opodepe', 27),
(1638, 'Oquitoa', 27),
(1639, 'Pitiquito', 27),
(1640, 'Puerto Peñasco', 27),
(1641, 'Quiriego', 27),
(1642, 'Rayón', 27),
(1643, 'Rosario', 27),
(1644, 'Sahuaripa', 27),
(1645, 'San Felipe de Jesús', 27),
(1646, 'San Ignacio Río Muer', 27),
(1647, 'San Javier', 27),
(1648, 'San Luis Río Colorad', 27),
(1649, 'San Miguel de Horcas', 27),
(1650, 'San Pedro de la Cuev', 27),
(1651, 'Santa Ana', 27),
(1652, 'Santa Cruz', 27),
(1653, 'Sáric', 27),
(1654, 'Soyopa', 27),
(1655, 'Suaqui Grande', 27),
(1656, 'Tepache', 27),
(1657, 'Trincheras', 27),
(1658, 'Tubutama', 27),
(1659, 'Ures', 27),
(1660, 'Villa Hidalgo', 27),
(1661, 'Villa Pesqueira', 27),
(1662, 'Yécora', 27),
(1663, 'Balancán', 28),
(1664, 'Cárdenas', 28),
(1665, 'Centla', 28),
(1666, 'Centro', 28),
(1667, 'Comalcalco', 28),
(1668, 'Cunduacán', 28),
(1669, 'Emiliano Zapata', 28),
(1670, 'Huimanguillo', 28),
(1671, 'Jalapa', 28),
(1672, 'Jalpa de Méndez', 28),
(1673, 'Jonuta', 28),
(1674, 'Macuspana', 28),
(1675, 'Nacajuca', 28),
(1676, 'Paraíso', 28),
(1677, 'Tacotalpa', 28),
(1678, 'Teapa', 28),
(1679, 'Tenosique', 28),
(1680, 'Abasolo', 29),
(1681, 'Aldama', 29),
(1682, 'Altamira', 29),
(1683, 'Antiguo Morelos', 29),
(1684, 'Burgos', 29),
(1685, 'Bustamante', 29),
(1686, 'Camargo', 29),
(1687, 'Casas', 29),
(1688, 'Ciudad Madero', 29),
(1689, 'Cruillas', 29),
(1690, 'El Mante', 29),
(1691, 'Gómez Farías', 29),
(1692, 'González', 29),
(1693, 'Güémez', 29),
(1694, 'Guerrero', 29),
(1695, 'Gustavo Díaz Ordaz', 29),
(1696, 'Hidalgo', 29),
(1697, 'Jaumave', 29),
(1698, 'Jiménez', 29),
(1699, 'Llera', 29),
(1700, 'Mainero', 29),
(1701, 'Matamoros', 29),
(1702, 'Méndez', 29),
(1703, 'Mier', 29),
(1704, 'Miguel Alemán', 29),
(1705, 'Miquihuana', 29),
(1706, 'Nuevo Laredo', 29),
(1707, 'Nuevo Morelos', 29),
(1708, 'Ocampo', 29),
(1709, 'Padilla', 29),
(1710, 'Palmillas', 29),
(1711, 'Reynosa', 29),
(1712, 'Río Bravo', 29),
(1713, 'San Carlos', 29),
(1714, 'San Fernando', 29),
(1715, 'San Nicolás', 29),
(1716, 'Soto la Marina', 29),
(1717, 'Tampico', 29),
(1718, 'Tula', 29),
(1719, 'Valle Hermoso', 29),
(1720, 'Victoria', 29),
(1721, 'Villagrán', 29),
(1722, 'Xicoténcatl', 29),
(1723, 'Amaxac de Guerrero', 30),
(1724, 'Apetatitlán de Anton', 30),
(1725, 'Atlangatepec', 30),
(1726, 'Atltzayanca', 30),
(1727, 'Calpulalpan', 30),
(1728, 'El Carmen Tequexquit', 30),
(1729, 'Cuapiaxtla', 30),
(1730, 'Cuaxomulco', 30),
(1731, 'Chiautempan', 30),
(1732, 'Muñoz de Domingo Are', 30),
(1733, 'Españita', 30),
(1734, 'Huamantla', 30),
(1735, 'Hueyotlipan', 30),
(1736, 'Ixtacuixtla de Maria', 30),
(1737, 'Ixtenco', 30),
(1738, 'Mazatecochco de José', 30),
(1739, 'Contla de Juan Cuama', 30),
(1740, 'Tepetitla de Lardizá', 30),
(1741, 'Sanctórum de Lázaro ', 30),
(1742, 'Nanacamilpa de Maria', 30),
(1743, 'Acuamanala de Miguel', 30),
(1744, 'Natívitas', 30),
(1745, 'Panotla', 30),
(1746, 'San Pablo del Monte', 30),
(1747, 'Santa Cruz Tlaxcala', 30),
(1748, 'Tenancingo', 30),
(1749, 'Teolocholco', 30),
(1750, 'Tepeyanco', 30),
(1751, 'Terrenate', 30),
(1752, 'Tetla de la Solidari', 30),
(1753, 'Tetlatlahuca', 30),
(1754, 'Tlaxcala', 30),
(1755, 'Tlaxco', 30),
(1756, 'Tocatlán', 30),
(1757, 'Totolac', 30),
(1758, 'Ziltlaltépec de Trin', 30),
(1759, 'Tzompantepec', 30),
(1760, 'Xaloztoc', 30),
(1761, 'Xaltocan', 30),
(1762, 'Papalotla de Xicohté', 30),
(1763, 'Xicohtzinco', 30),
(1764, 'Yauhquemehcan', 30),
(1765, 'Zacatelco', 30),
(1766, 'Acajete', 31),
(1767, 'Acatlán', 31),
(1768, 'Acayucan', 31),
(1769, 'Actopan', 31),
(1770, 'Acula', 31),
(1771, 'Acultzingo', 31),
(1772, 'Agua Dulce', 31),
(1773, 'Alpatláhuac', 31),
(1774, 'Alto Lucero de Gutié', 31),
(1775, 'Altotonga', 31),
(1776, 'Alvarado', 31),
(1777, 'Amatitlán', 31),
(1778, 'Amatlán de los Reyes', 31),
(1779, 'Angel R. Cabada', 31),
(1780, 'Apazapan', 31),
(1781, 'Aquila', 31),
(1782, 'Astacinga', 31),
(1783, 'Atlahuilco', 31),
(1784, 'Atoyac', 31),
(1785, 'Atzacan', 31),
(1786, 'Atzalan', 31),
(1787, 'Ayahualulco', 31),
(1788, 'Banderilla', 31),
(1789, 'Benito Juárez', 31),
(1790, 'Boca del Río', 31),
(1791, 'Calcahualco', 31),
(1792, 'Camerino Z. Mendoza', 31),
(1793, 'Carrillo Puerto', 31),
(1794, 'Catemaco', 31),
(1795, 'Cazones de Herrera', 31),
(1796, 'Cerro Azul', 31),
(1797, 'Chacaltianguis', 31),
(1798, 'Chalma', 31),
(1799, 'Chiconamel', 31),
(1800, 'Chiconquiaco', 31),
(1801, 'Chicontepec', 31),
(1802, 'Chinameca', 31),
(1803, 'Chinampa de Gorostiz', 31),
(1804, 'Chocamán', 31),
(1805, 'Chontla', 31),
(1806, 'Chumatlán', 31),
(1807, 'Citlaltépetl', 31),
(1808, 'Coacoatzintla', 31),
(1809, 'Coahuitlán', 31),
(1810, 'Coatepec', 31),
(1811, 'Coatzacoalcos', 31),
(1812, 'Coatzintla', 31),
(1813, 'Comapa', 31),
(1814, 'Córdoba', 31),
(1815, 'Cosamaloapan de Carp', 31),
(1816, 'Cosautlán de Carvaja', 31),
(1817, 'Coscomatepec', 31),
(1818, 'Cosoleacaque', 31),
(1819, 'Cotaxtla', 31),
(1820, 'Coxquihui', 31),
(1821, 'Coyutla', 31),
(1822, 'Cuichapa', 31),
(1823, 'Cuitláhuac', 31),
(1824, 'El Higo', 31),
(1825, 'Emiliano Zapata', 31),
(1826, 'Espinal', 31),
(1827, 'Filomeno Mata', 31),
(1828, 'Fortín', 31),
(1829, 'Gutiérrez Zamora', 31),
(1830, 'Hidalgotitlán', 31),
(1831, 'Huatusco', 31),
(1832, 'Huayacocotla', 31),
(1833, 'Hueyapan de Ocampo', 31),
(1834, 'Huiloapan', 31),
(1835, 'Ignacio de la Llave', 31),
(1836, 'Ilamatlán', 31),
(1837, 'Isla', 31),
(1838, 'Ixcatepec', 31),
(1839, 'Ixhuacán de los Reye', 31),
(1840, 'Ixhuatlán de Madero', 31),
(1841, 'Ixhuatlán del Café', 31),
(1842, 'Ixhuatlán del Surest', 31),
(1843, 'Ixhuatlancillo', 31),
(1844, 'Ixmatlahuacan', 31),
(1845, 'Ixtaczoquitlán', 31),
(1846, 'Jalacingo', 31),
(1847, 'Jalcomulco', 31),
(1848, 'Jáltipan', 31),
(1849, 'Jamapa', 31),
(1850, 'Jesús Carranza', 31),
(1851, 'Xalapa', 31),
(1852, 'Xico', 31),
(1853, 'Zacualpan', 31),
(1854, 'Zaragoza', 31),
(1855, 'Zentla', 31),
(1856, 'Zongolica', 31),
(1857, 'Zontecomatlán', 31),
(1858, 'Zozocolco de Hidalgo', 31),
(1859, 'Abalá', 32),
(1860, 'Acanceh', 32),
(1861, 'Akil', 32),
(1862, 'Baca', 32),
(1863, 'Bokobá', 32),
(1864, 'Buctzotz', 32),
(1865, 'Cacalchén', 32),
(1866, 'Calotmul', 32),
(1867, 'Cansahcab', 32),
(1868, 'Cantamayec', 32),
(1869, 'Celestún', 32),
(1870, 'Cenotillo', 32),
(1871, 'Chacsinkín', 32),
(1872, 'Chankom', 32),
(1873, 'Chapab', 32),
(1874, 'Chemax', 32),
(1875, 'Chicxulub Pueblo', 32),
(1876, 'Chichimilá', 32),
(1877, 'Chikindzonot', 32),
(1878, 'Chocholá', 32),
(1879, 'Chumayel', 32),
(1880, 'Conkal', 32),
(1881, 'Cuncunul', 32),
(1882, 'Cuzamá', 32),
(1883, 'Dzán', 32),
(1884, 'Dzemul', 32),
(1885, 'Dzidzantún', 32),
(1886, 'Dzilam de Bravo', 32),
(1887, 'Dzilam González', 32),
(1888, 'Dzitás', 32),
(1889, 'Dzoncauich', 32),
(1890, 'Espita', 32),
(1891, 'Halachó', 32),
(1892, 'Hocabá', 32),
(1893, 'Hoctún', 32),
(1894, 'Homún', 32),
(1895, 'Huhí', 32),
(1896, 'Hunucmá', 32),
(1897, 'Ixil', 32),
(1898, 'Izamal', 32),
(1899, 'Kanasín', 32),
(1900, 'Kantunil', 32),
(1901, 'Kaua', 32),
(1902, 'Kinchil', 32),
(1903, 'Kopomá', 32),
(1904, 'Mama', 32),
(1905, 'Maní', 32),
(1906, 'Maxcanú', 32),
(1907, 'Mayapán', 32),
(1908, 'Mérida', 32),
(1909, 'Mocochá', 32),
(1910, 'Motul', 32),
(1911, 'Muna', 32),
(1912, 'Muxupip', 32),
(1913, 'Opichén', 32),
(1914, 'Oxkutzcab', 32),
(1915, 'Panabá', 32),
(1916, 'Peto', 32),
(1917, 'Progreso', 32),
(1918, 'Quintana Roo', 32),
(1919, 'Río Lagartos', 32),
(1920, 'Sacalum', 32),
(1921, 'Samahil', 32),
(1922, 'San Felipe', 32),
(1923, 'Sanahcat', 32),
(1924, 'Santa Elena', 32),
(1925, 'Seyé', 32),
(1926, 'Sinanché', 32),
(1927, 'Sotuta', 32),
(1928, 'Sucilá', 32),
(1929, 'Sudzal', 32),
(1930, 'Suma', 32),
(1931, 'Tahdziú', 32),
(1932, 'Tahmek', 32),
(1933, 'Teabo', 32),
(1934, 'Tecoh', 32),
(1935, 'Tekal de Venegas', 32),
(1936, 'Tekantó', 32),
(1937, 'Tekax', 32),
(1938, 'Tekit', 32),
(1939, 'Tekom', 32),
(1940, 'Telchac Pueblo', 32),
(1941, 'Telchac Puerto', 32),
(1942, 'Temax', 32),
(1943, 'Temozón', 32),
(1944, 'Tepakán', 32),
(1945, 'Tetiz', 32),
(1946, 'Teya', 32),
(1947, 'Ticul', 32),
(1948, 'Timucuy', 32),
(1949, 'Tinum', 32),
(1950, 'Tixcacalcupul', 32),
(1951, 'Tixkokob', 32),
(1952, 'Tixmehuac', 32),
(1953, 'Tixpéhual', 32),
(1954, 'Tizimín', 32),
(1955, 'Tunkás', 32),
(1956, 'Tzucacab', 32),
(1957, 'Uayma', 32),
(1958, 'Ucú', 32),
(1959, 'Umán', 32),
(1960, 'Valladolid', 32),
(1961, 'Xocchel', 32),
(1962, 'Yaxcabá', 32),
(1963, 'Yaxkukul', 32),
(1964, 'Yobaín', 32),
(1965, 'Apozol', 33),
(1966, 'Apulco', 33),
(1967, 'Atolinga', 33),
(1968, 'Benito Juárez', 33),
(1969, 'Calera', 33),
(1970, 'Cañitas de Felipe Pe', 33),
(1971, 'Chalchihuites', 33),
(1972, 'Concepción del Oro', 33),
(1973, 'Cuauhtémoc', 33),
(1974, 'El Plateado de Joaqu', 33),
(1975, 'El Salvador', 33),
(1976, 'Fresnillo', 33),
(1977, 'Genaro Codina', 33),
(1978, 'General Enrique Estr', 33),
(1979, 'General Francisco R.', 33),
(1980, 'General Pánfilo Nate', 33),
(1981, 'Guadalupe', 33),
(1982, 'Huanusco', 33),
(1983, 'Jalpa', 33),
(1984, 'Jerez', 33),
(1985, 'Jiménez del Teul', 33),
(1986, 'Juan Aldama', 33),
(1987, 'Juchipila', 33),
(1988, 'Loreto', 33),
(1989, 'Luis Moya', 33),
(1990, 'Mazapil', 33),
(1991, 'Melchor Ocampo', 33),
(1992, 'Mezquital del Oro', 33),
(1993, 'Miguel Auza', 33),
(1994, 'Momax', 33),
(1995, 'Monte Escobedo', 33),
(1996, 'Morelos', 33),
(1997, 'Moyahua de Estrada', 33),
(1998, 'Nochistlán de Mejía', 33),
(1999, 'Noria de Ángeles', 33),
(2000, 'Ojocaliente', 33),
(2001, 'Pánuco', 33),
(2002, 'Pinos', 33),
(2003, 'Río Grande', 33),
(2004, 'Sain Alto', 33),
(2005, 'Santa María de la Pa', 33),
(2006, 'Sombrerete', 33),
(2007, 'Susticacán', 33),
(2008, 'Tabasco', 33),
(2009, 'Tepechitlán', 33),
(2010, 'Tepetongo', 33),
(2011, 'Teúl de González Ort', 33),
(2012, 'Tlaltenango de Sánch', 33),
(2013, 'Trancoso', 33),
(2014, 'Trinidad García de l', 33),
(2015, 'Valparaíso', 33),
(2016, 'Vetagrande', 33),
(2017, 'Villa de Cos', 33),
(2018, 'Villa García', 33),
(2019, 'Villa González Orteg', 33),
(2020, 'Villa Hidalgo', 33),
(2021, 'Villanueva', 33),
(2022, 'Zacatecas', 33),
(2023, 'Azcapotzalco', 43),
(2024, 'Coyoacán', 43),
(2025, 'Cuajimalpa de Morelo', 43),
(2026, 'Gustavo A. Madero', 43),
(2027, 'Iztacalco', 43),
(2028, 'Iztapalapa', 43),
(2029, 'La Magdalena Contrer', 43),
(2030, 'Milpa Alta', 43),
(2031, 'Álvaro Obregón', 43),
(2032, 'Tláhuac', 43),
(2033, 'Tlalpan', 43),
(2034, 'Xochimilco', 43),
(2035, 'Benito Juárez', 43),
(2036, 'Cuauhtémoc', 43),
(2037, 'Miguel Hidalgo', 43),
(2038, 'Venustiano Carranza', 43);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `nombres`
--

DROP TABLE IF EXISTS `nombres`;
CREATE TABLE IF NOT EXISTS `nombres` (
  `id_nombre` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellido_paterno` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellido_materno` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `nombres`
--

INSERT INTO `nombres` (`id_nombre`, `nombre`, `apellido_paterno`, `apellido_materno`) VALUES
(1, 'Carlos', 'Hernández', 'Velázquez'),
(2, 'Juan', 'Pérez', 'García'),
(3, 'Carlos Alberto', 'Hernández', 'Velázquez'),
(4, 'Laisha Denis', 'Rodríguez', 'García'),
(5, 'Carlos', 'Hernández', 'Velaz'),
(6, 'Charlie', 'Hernández', 'Velázquez'),
(7, 'Carl', 'Hernández', 'Velázquez'),
(8, 'Charlie Alberto', 'Hernández', 'Velázquez'),
(9, 'Carlitos', 'Hernández', 'Velázquez'),
(10, 'Juan', 'Hernández', 'Velázquez'),
(11, 'Pedro', 'Hernández', 'Velázquez'),
(12, 'Laisha', 'Rodríguez', 'García'),
(13, 'Alberto', 'Hernández', 'Velázquez');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

DROP TABLE IF EXISTS `pagos`;
CREATE TABLE IF NOT EXISTS `pagos` (
  `id_pago` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pagos`
--

INSERT INTO `pagos` (`id_pago`, `nombre`, `id_usuario`) VALUES
(9, 'Suscripción Premium', 12),
(10, 'Suscripción Premium', 12);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `suscripciones`
--

DROP TABLE IF EXISTS `suscripciones`;
CREATE TABLE IF NOT EXISTS `suscripciones` (
  `id_suscripcion` int NOT NULL AUTO_INCREMENT,
  `id_tipo_suscripcion` int DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_termino` date DEFAULT NULL,
  `id_pago` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_suscripcion`),
  KEY `id_pago` (`id_pago`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_tipo_suscripcion` (`id_tipo_suscripcion`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Disparadores `suscripciones`
--
DROP TRIGGER IF EXISTS `actualizar_suscripción`;
DELIMITER $$
CREATE TRIGGER `actualizar_suscripción` AFTER INSERT ON `suscripciones` FOR EACH ROW BEGIN
    UPDATE usuarios SET estado_suscripcion = TRUE WHERE usuarios.id_usuario = NEW.id_usuario;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_suscripcion`
--

DROP TABLE IF EXISTS `tipo_suscripcion`;
CREATE TABLE IF NOT EXISTS `tipo_suscripcion` (
  `id_tipo_suscripcion` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_tipo_suscripcion`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_suscripcion`
--

INSERT INTO `tipo_suscripcion` (`id_tipo_suscripcion`, `nombre`) VALUES
(1, 'Básico'),
(2, 'Premium');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `id_nombre` int DEFAULT NULL,
  `correo` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `contrasenia` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telefono` bigint DEFAULT NULL,
  `usuario_administrador` tinyint(1) DEFAULT NULL,
  `id_direccion` int DEFAULT NULL,
  `edad` int DEFAULT NULL,
  `sexo` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `bloqueado` tinyint(1) DEFAULT NULL,
  `estado_suscripcion` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  KEY `id_nombre` (`id_nombre`),
  KEY `id_direccion` (`id_direccion`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `id_nombre`, `correo`, `contrasenia`, `telefono`, `usuario_administrador`, `id_direccion`, `edad`, `sexo`, `bloqueado`, `estado_suscripcion`) VALUES
(11, 3, 'carloshv51@gmail.com', 'II9WbHYltuN1iLoFmhzk5g==', 7295412652, 1, 25, 23, 'M', 0, 1),
(12, 12, 'ldrg@gmail.com', 'imq4jRwNFQ+VVR2jduM36A==', 7265485222, 0, 30, 21, 'F', 0, 1),
(13, 10, 'juanitohv@gmail.com', 'NQEKK7+QnZJaXDD0VGLocg==', 7226548952, 0, 31, 21, 'M', 0, 0);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `corte`
--
ALTER TABLE `corte`
  ADD CONSTRAINT `corte_ibfk_1` FOREIGN KEY (`id_gasto`) REFERENCES `gastos` (`id_gasto`),
  ADD CONSTRAINT `corte_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `direccion`
--
ALTER TABLE `direccion`
  ADD CONSTRAINT `direccion_ibfk_1` FOREIGN KEY (`id_municipio`) REFERENCES `municipio` (`id_municipio`),
  ADD CONSTRAINT `direccion_ibfk_2` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`);

--
-- Filtros para la tabla `municipio`
--
ALTER TABLE `municipio`
  ADD CONSTRAINT `municipio_ibfk_1` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`);

--
-- Filtros para la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  ADD CONSTRAINT `pagos_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `suscripciones`
--
ALTER TABLE `suscripciones`
  ADD CONSTRAINT `suscripciones_ibfk_1` FOREIGN KEY (`id_pago`) REFERENCES `pagos` (`id_pago`),
  ADD CONSTRAINT `suscripciones_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  ADD CONSTRAINT `suscripciones_ibfk_3` FOREIGN KEY (`id_tipo_suscripcion`) REFERENCES `tipo_suscripcion` (`id_tipo_suscripcion`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_nombre`) REFERENCES `nombres` (`id_nombre`),
  ADD CONSTRAINT `usuarios_ibfk_2` FOREIGN KEY (`id_direccion`) REFERENCES `direccion` (`id_direccion`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
