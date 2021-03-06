-- MySQL Script generated by MySQL Workbench
-- Thu Oct 19 19:28:07 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema minibanco
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema minibanco
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `minibanco` DEFAULT CHARACTER SET utf8 ;
USE `minibanco` ;

-- -----------------------------------------------------
-- Table `minibanco`.`tipo_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`tipo_cliente` (
  `id_tipo_cliente` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_tipo_cliente`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`tipo_documento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`tipo_documento` (
  `id_tipo_documento` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_tipo_documento`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`cliente` (
  `id_cliente` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(70) NULL DEFAULT NULL,
  `identificacion` VARCHAR(125) NOT NULL,
  `telefono` VARCHAR(30) NOT NULL,
  `direccion` VARCHAR(60) NOT NULL,
  `usuario` VARCHAR(15) NOT NULL,
  `contrasena` VARCHAR(150) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `correo` VARCHAR(60) NOT NULL,
  `id_tipo_cliente` BIGINT(20) NOT NULL,
  `id_tipo_documento` BIGINT(20) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE INDEX `identificacion_UNIQUE` (`identificacion` ASC),
  UNIQUE INDEX `username_UNIQUE` (`usuario` ASC),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC),
  INDEX `fk_cliente_tipo_cliente_idx` (`id_tipo_cliente` ASC),
  INDEX `fk_cliente_tipo_documento1_idx` (`id_tipo_documento` ASC),
  CONSTRAINT `fk_cliente_tipo_cliente`
    FOREIGN KEY (`id_tipo_cliente`)
    REFERENCES `minibanco`.`tipo_cliente` (`id_tipo_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cliente_tipo_documento1`
    FOREIGN KEY (`id_tipo_documento`)
    REFERENCES `minibanco`.`tipo_documento` (`id_tipo_documento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`auditoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`auditoria` (
  `id_auditoria` INT(11) NOT NULL AUTO_INCREMENT,
  `transaccion` VARCHAR(150) NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id_cliente` BIGINT(20) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_auditoria`),
  INDEX `fk_auditoria_cliente1_idx` (`id_cliente` ASC),
  CONSTRAINT `fk_auditoria_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `minibanco`.`cliente` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`tipo_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`tipo_producto` (
  `id_tipo_producto` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_tipo_producto`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`cliente_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`cliente_producto` (
  `id_cliente_producto` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(45) NOT NULL,
  `valor` BIGINT(20) NOT NULL,
  `pago_minimo` BIGINT(20) NOT NULL,
  `fecha_pago_minimo` VARCHAR(5) NOT NULL,
  `total_pagar` BIGINT(20) NOT NULL,
  `interes` DOUBLE(2,1) NOT NULL,
  `estado` BIT(1) NULL DEFAULT NULL,
  `numero_cuotas` INT(11) NULL DEFAULT NULL,
  `id_tipo_producto` BIGINT(20) NOT NULL,
  `id_cliente` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_cliente_producto`),
  INDEX `fk_cliente_producto_tipo_producto1_idx` (`id_tipo_producto` ASC),
  INDEX `fk_cliente_producto_cliente1_idx` (`id_cliente` ASC),
  CONSTRAINT `fk_cliente_producto_cliente1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `minibanco`.`cliente` (`id_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cliente_producto_tipo_producto1`
    FOREIGN KEY (`id_tipo_producto`)
    REFERENCES `minibanco`.`tipo_producto` (`id_tipo_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`tipo_movimiento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`tipo_movimiento` (
  `id_tipo_movimiento` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_tipo_movimiento`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `minibanco`.`movimiento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `minibanco`.`movimiento` (
  `id_movimiento` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(100) NOT NULL,
  `fecha` DATE NOT NULL,
  `valor` BIGINT(20) NOT NULL,
  `cuota` INT(11) NULL DEFAULT NULL,
  `id_tipo_movimiento` BIGINT(20) NOT NULL,
  `id_cliente_producto` BIGINT(20) NOT NULL,
  `estado` BIT(1) NOT NULL,
  PRIMARY KEY (`id_movimiento`),
  INDEX `fk_movimiento_tipo_movimiento1_idx` (`id_tipo_movimiento` ASC),
  INDEX `fk_movimiento_cliente_producto1_idx` (`id_cliente_producto` ASC),
  CONSTRAINT `fk_movimiento_cliente_producto1`
    FOREIGN KEY (`id_cliente_producto`)
    REFERENCES `minibanco`.`cliente_producto` (`id_cliente_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_movimiento_tipo_movimiento1`
    FOREIGN KEY (`id_tipo_movimiento`)
    REFERENCES `minibanco`.`tipo_movimiento` (`id_tipo_movimiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
