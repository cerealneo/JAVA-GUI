-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- 생성 시간: 22-12-08 12:17
-- 서버 버전: 10.4.11-MariaDB
-- PHP 버전: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 데이터베이스: `dpsdy2`
--

-- --------------------------------------------------------

--
-- 테이블 구조 `book`
--

CREATE TABLE `book` (
  `name` varchar(45) DEFAULT NULL COMMENT '이름',
  `number` varchar(45) DEFAULT NULL COMMENT '분류기호',
  `publisher` varchar(45) DEFAULT NULL COMMENT '출판사',
  `writer` varchar(45) DEFAULT NULL COMMENT '저자',
  `loan` varchar(10) DEFAULT NULL COMMENT '대출여부',
  `isbn` varchar(45) NOT NULL COMMENT 'ISBN'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `book`
--

INSERT INTO `book` (`name`, `number`, `publisher`, `writer`, `loan`, `isbn`) VALUES
('a', 'a', 'a', 'a', 'null', '123456123'),
('데이터베이스 배움터', '645', '행능', '홍의경', 'null', '789456321'),
('명품 자바 에센셜', '000-000-0000', '생능출판', '황기태', '대출중', '978-89-7050-956-3'),
('ddd', 'dd', 'dd', 'dd', 'null', 'dd');

-- --------------------------------------------------------

--
-- 테이블 구조 `library`
--

CREATE TABLE `library` (
  `no` varchar(15) NOT NULL COMMENT '넘버',
  `loanday` date DEFAULT NULL COMMENT '대출일',
  `returnday` date DEFAULT NULL COMMENT '반납일',
  `willreturnday` date DEFAULT NULL COMMENT '반납 예정일',
  `isbn` varchar(45) DEFAULT NULL COMMENT 'isbn',
  `phone` char(13) NOT NULL COMMENT 'phone',
  `name` varchar(45) DEFAULT NULL COMMENT '책이름',
  `number` varchar(45) DEFAULT NULL COMMENT '분류기호',
  `publisher` varchar(45) DEFAULT NULL COMMENT '출판사',
  `writer` varchar(45) DEFAULT NULL COMMENT '저자',
  `loan` varchar(10) DEFAULT NULL COMMENT '대출여부'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `library`
--

INSERT INTO `library` (`no`, `loanday`, `returnday`, `willreturnday`, `isbn`, `phone`, `name`, `number`, `publisher`, `writer`, `loan`) VALUES
('1', '2022-12-08', '2022-12-22', '2022-12-07', '978-89-7050-956-3', '010-0000-0000', '홍길동', '123', 'www', 'wwww', '대출중');

-- --------------------------------------------------------

--
-- 테이블 구조 `loan`
--

CREATE TABLE `loan` (
  `no` varchar(15) NOT NULL COMMENT '넘버',
  `loanday` date DEFAULT NULL COMMENT '대출일',
  `returnday` date DEFAULT NULL COMMENT '반납일',
  `willreturnday` date DEFAULT NULL COMMENT '반납예정일',
  `isbn` varchar(45) DEFAULT NULL COMMENT 'ISBN',
  `phone` char(13) DEFAULT NULL COMMENT '전화번호'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `loan`
--

INSERT INTO `loan` (`no`, `loanday`, `returnday`, `willreturnday`, `isbn`, `phone`) VALUES
('1', '2022-12-01', '2022-12-02', '2022-12-07', '789456321', '010-0000-0000');

-- --------------------------------------------------------

--
-- 테이블 구조 `member`
--

CREATE TABLE `member` (
  `phone` char(13) NOT NULL COMMENT 'phone',
  `mname` varchar(16) DEFAULT NULL COMMENT '이름',
  `address` varchar(45) DEFAULT NULL COMMENT '주소',
  `birthday` date DEFAULT NULL COMMENT '생년월일',
  `overdue` date DEFAULT current_timestamp() COMMENT '연체일'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `member`
--

INSERT INTO `member` (`phone`, `mname`, `address`, `birthday`, `overdue`) VALUES
('010-0000-0000', '홍길동', '부산시 남구 용호동 ', '2019-01-01', '2022-12-05');

--
-- 덤프된 테이블의 인덱스
--

--
-- 테이블의 인덱스 `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`isbn`);

--
-- 테이블의 인덱스 `library`
--
ALTER TABLE `library`
  ADD PRIMARY KEY (`no`),
  ADD KEY `phone` (`phone`);

--
-- 테이블의 인덱스 `loan`
--
ALTER TABLE `loan`
  ADD PRIMARY KEY (`no`),
  ADD KEY `book_laon` (`isbn`),
  ADD KEY `member_loan` (`phone`);

--
-- 테이블의 인덱스 `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`phone`);

--
-- 덤프된 테이블의 제약사항
--

--
-- 테이블의 제약사항 `library`
--
ALTER TABLE `library`
  ADD CONSTRAINT `ddd` FOREIGN KEY (`phone`) REFERENCES `member` (`phone`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 테이블의 제약사항 `loan`
--
ALTER TABLE `loan`
  ADD CONSTRAINT `book_laon` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
