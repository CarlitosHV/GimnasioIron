-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 13-05-2023 a las 01:28:44
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

    -- Validamos si existe el Estado y si no, lo insertamos
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
    VALUES (p_municipio);
    END IF;
    BEGIN
        SELECT MUNICIPIO.id_municipio
        INTO v_id_municipio
        FROM MUNICIPIO 
        WHERE MUNICIPIO.nombre = p_municipio;
    END;
    
    
    -- Validamos e insertamos la dirección
    IF NOT EXISTS(SELECT DIRECCION.calle, DIRECCION.numero, DIRECCION.codigo_postal, DIRECCION.id_municipio, DIRECCION.id_estado FROM DIRECCION 
    WHERE DIRECCION.calle = p_calle AND DIRECCION.numero = p_numero AND DIRECCION.codigo_postal = p_codigo_postal AND DIRECCION.id_municipio = id_municipio AND DIRECCION.id_estado = id_estado)
    THEN INSERT INTO DIRECCION(calle, numero, codigo_postal, id_municipio, id_estado) 
    VALUES (p_calle, p_numero, p_codigo_postal, id_municipio, id_estado);
    END IF;
    BEGIN
        SELECT DIRECCION.id_direccion 
        INTO v_id_direccion
        FROM DIRECCION 
        WHERE DIRECCION.calle = p_calle AND DIRECCION.numero = p_numero AND DIRECCION.codigo_postal = p_codigo_postal AND DIRECCION.id_municipio = id_municipio AND DIRECCION.id_estado = id_estado;
    END;

    -- Validamos e insertamos el usuario
    IF NOT EXISTS(SELECT USUARIOS.id_nombre, USUARIOS.correo, USUARIOS.contrasenia FROM USUARIOS 
    WHERE USUARIOS.id_nombre = id_nombre AND USUARIOS.correo = p_correo AND USUARIOS.contrasenia = p_contrasenia)
    THEN
    UPDATE USUARIOS
    SET 
        id_nombre = v_id_nombre, correo = p_correo, contrasenia = p_contrasenia, telefono = p_telefono,
        id_direccion = v_id_direccion
    WHERE 
        id_usuario = p_id_usuario;
    END IF;
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
    s.fecha_termino
    FROM USUARIOS u
    JOIN NOMBRES n ON n.id_nombre = u.id_nombre
    JOIN SUSCRIPCIONES s ON s.id_usuario = u.id_usuario
    JOIN TIPO_SUSCRIPCION ts ON ts.id_tipo_suscripcion = s.id_tipo_suscripcion;
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

    WHERE u.correo = correo_usuario;

END$$

DROP PROCEDURE IF EXISTS `obtener_planes`$$
CREATE DEFINER=`AdminGimnasio`@`%` PROCEDURE `obtener_planes` ()   BEGIN

    SELECT nombre FROM tipo_suscripcion;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `direccion`
--

INSERT INTO `direccion` (`id_direccion`, `calle`, `numero`, `codigo_postal`, `id_municipio`, `id_estado`) VALUES
(4, 'Abel Salazar', 113, 52300, 9, 35),
(5, 'Abel Salazar', 113, 52300, 10, 35),
(6, 'Benito Juárez', 109, 52240, 9, 34);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado`
--

DROP TABLE IF EXISTS `estado`;
CREATE TABLE IF NOT EXISTS `estado` (
  `id_estado` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(34, 'Estado México'),
(35, 'CDMX');

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `municipio`
--

INSERT INTO `municipio` (`id_municipio`, `nombre`, `id_estado`) VALUES
(9, 'Metepec', 3),
(10, 'Toluca', 35);

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `nombres`
--

INSERT INTO `nombres` (`id_nombre`, `nombre`, `apellido_paterno`, `apellido_materno`) VALUES
(1, 'Carlos', 'Hernández', 'Velázquez'),
(2, 'Juan', 'Pérez', 'García'),
(3, 'Carlos Alberto', 'Hernández', 'Velázquez'),
(4, 'Laisha Denis', 'Rodríguez', 'García');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pagos`
--

INSERT INTO `pagos` (`id_pago`, `nombre`, `id_usuario`) VALUES
(1, 'Suscripción Básico', 6),
(2, 'Suscripción Premium', 5),
(3, 'Suscripción Premium', 7);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `suscripciones`
--

INSERT INTO `suscripciones` (`id_suscripcion`, `id_tipo_suscripcion`, `fecha_inicio`, `fecha_termino`, `id_pago`, `id_usuario`) VALUES
(1, 1, '2023-05-05', '2023-08-05', 1, 6),
(2, 2, '2023-05-05', '2023-11-05', 2, 5),
(3, 2, '2023-05-05', '2024-05-05', 3, 7);

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `id_nombre`, `correo`, `contrasenia`, `telefono`, `usuario_administrador`, `id_direccion`, `edad`, `sexo`, `bloqueado`, `estado_suscripcion`) VALUES
(5, 3, 'carloshvsa21@hotmail.com', 'v6mzPpnjSX66wfZGhGmUMA==', 7297516216, 0, 4, 23, 'M', 0, 1),
(6, 3, 'carloshv51@gmail.com', 'II9WbHYltuN1iLoFmhzk5g==', 7297516216, 1, 5, 23, 'M', 0, 1),
(7, 4, 'laisha.denis@gmail.com', 'QgT4gCIYvLdBXJKV+UXcLw==', 7222543322, 0, 6, 21, 'F', 0, 1);

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
  ADD CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

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
