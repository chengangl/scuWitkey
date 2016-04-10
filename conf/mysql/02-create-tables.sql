DROP TABLE IF EXISTS `index_recommend_table`;

CREATE TABLE IF NOT EXISTS `index_recommend_table` (
  `id`                   BIGINT(20)   NOT NULL,
  `imgUrl`               VARCHAR(128) NOT NULL,
  `title`                VARCHAR(128) NOT NULL,
  `description`          VARCHAR(256) NOT NULL,
  `recommendPublisherId` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`)
)
  DEFAULT CHARSET =utf8;

# ALTER TABLE `index_recommend_table` ADD `recommendPublisherId` BIGINT(20) NOT NULL;

DROP TABLE IF EXISTS `user_table`;

CREATE TABLE IF NOT EXISTS `user_table` (
  `id`               BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `userName`         VARCHAR(128) NOT NULL,
  `email`            VARCHAR(128) NOT NULL,
  `password`         VARCHAR(128) NOT NULL,
  `loginFingerprint` VARCHAR(256),
  PRIMARY KEY (`id`)
)
  DEFAULT CHARSET =utf8;
ALTER TABLE `user_table` ADD `school` VARCHAR(50);
ALTER TABLE `user_table` ADD `college` VARCHAR(50);
ALTER TABLE `user_table` ADD `specialty` VARCHAR(50);
ALTER TABLE `user_table` ADD `grade` VARCHAR(20);
ALTER TABLE `user_table` ADD `qq` VARCHAR(20);
ALTER TABLE `user_table` ADD `telephone` VARCHAR(20);
ALTER TABLE `user_table` ADD `avatar` VARCHAR(128);
ALTER TABLE `user_table` ADD `score` INT(10) DEFAULT 0;


DROP TABLE IF EXISTS `mission_table`;

CREATE TABLE IF NOT EXISTS `mission_table` (
  `id`                 BIGINT(20)               NOT NULL AUTO_INCREMENT,
  `missionTitle`       VARCHAR(50)              NOT NULL,
  `missionDescription` VARCHAR(256)             NOT NULL,
  `missionBeginDate`   DATE                     NOT NULL,
  `missionEndDate`     DATE                     NOT NULL,
  `missionWinningMode` VARCHAR(10)              NOT NULL,
  `missionMode`        VARCHAR(10)              NOT NULL,
  `missionReward`      INTEGER(10) DEFAULT 0    NOT NULL,
  `missionPublisherId` BIGINT(20)               NOT NULL,
  `missionStatus`      VARCHAR(20) DEFAULT '进行中'NOT NULL, #进行中-待审核-已结束
  PRIMARY KEY (`id`)
)
  DEFAULT CHARSET =utf8;
ALTER TABLE `mission_table` ADD `missionData` VARCHAR(128);
ALTER TABLE `mission_table` ADD `missionDataName` VARCHAR(128);
# ALTER TABLE `mission_table` ADD `missionStatus` VARCHAR(20) DEFAULT '进行中' NOT NULL;
#已发布-进行中-审核中-已结束

DROP TABLE IF EXISTS `mission_user_relationship_table`;

CREATE TABLE IF NOT EXISTS `mission_user_relationship_table` (
  `id`                      BIGINT(20)                NOT NULL AUTO_INCREMENT,
  `missionId`               BIGINT(20)                NOT NULL,
  `userId`                  BIGINT(20)                NOT NULL,
  `missionAcceptDate`       DATE                      NOT NULL,
  `missionFinishDate`       DATE,
  `missionCompletionStatus` VARCHAR(20) DEFAULT '进行中' NOT NULL, #进行中-已提交
  PRIMARY KEY (`id`)
)
  DEFAULT CHARSET =utf8;
ALTER TABLE `mission_user_relationship_table` ADD `missionSubmitData` VARCHAR(128);
ALTER TABLE `mission_user_relationship_table` ADD `missionSubmitDataName` VARCHAR(128);
ALTER TABLE `mission_user_relationship_table` ADD `missionBidStatus` VARCHAR(20);

DROP TABLE IF EXISTS `session_table`;
CREATE TABLE `session_table` (
  `sessionKey` VARCHAR(32) NOT NULL DEFAULT '',
  `sessionArray` VARCHAR(128) NOT NULL,
  `sessionExpTime` TIMESTAMP NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`sessionKey`),
  KEY `sessionKey` (`sessionKey`)
) ENGINE=MyISAM
  DEFAULT CHARSET=utf8