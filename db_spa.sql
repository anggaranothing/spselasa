-- Adminer 4.3.0 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

CREATE DATABASE `db_spa` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `db_spa`;

DROP TABLE IF EXISTS `data_diagnosa`;
CREATE TABLE `data_diagnosa` (
  `diagnosa_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `diagnosa_date` datetime NOT NULL,
  `user_id` bigint(24) NOT NULL,
  `status` enum('done','cancel') NOT NULL DEFAULT 'cancel',
  `cf_user` text NOT NULL,
  `cf_kombinasi` text NOT NULL,
  `cf_persentase` text NOT NULL,
  PRIMARY KEY (`diagnosa_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `data_diagnosa_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `data_user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_diagnosa` (`diagnosa_id`, `diagnosa_date`, `user_id`, `status`, `cf_user`, `cf_kombinasi`, `cf_persentase`) VALUES
(1,	'2017-04-01 00:03:14',	1,	'done',	'C00010=0,60 C00001=0,60 C00002=0,60 C00003=0,60 C00004=0,60 C00005=0,60 C00006=0,60 C00007=0,60 C00008=0,60 C00009=0,60 C00011=0,60 C00012=0,60 C00013=0,60 C00014=0,60 C00015=0,60 C00016=0,60 ',	'D00001=0,99 D00002=0,98 D00003=0,97 ',	'D00001=99,35 D00002=98,08 D00003=97,44 '),
(2,	'2017-04-01 00:04:26',	1,	'done',	'C00010=0,00 C00001=0,20 C00002=0,40 C00003=0,40 C00004=0,80 C00005=0,40 C00006=0,60 C00007=1,00 C00008=1,00 C00009=0,20 C00011=0,60 C00012=0,40 C00013=0,60 C00014=1,00 C00015=0,20 C00016=0,80 ',	'D00001=1,00 D00002=0,91 D00003=1,00 ',	'D00001=100,00 D00002=91,45 D00003=100,00 '),
(4,	'2017-04-01 00:17:58',	3,	'done',	'C00010=0,20 C00001=0,20 C00002=0,20 C00003=0,40 C00004=0,20 C00005=0,20 C00006=0,20 C00007=0,40 C00008=0,60 C00009=0,80 C00011=1,00 C00012=0,40 C00013=0,20 C00014=0,40 C00015=1,00 C00016=0,80 ',	'D00001=0,98 D00002=0,67 D00003=1,00 ',	'D00001=97,81 D00002=66,76 D00003=100,00 '),
(5,	'2017-04-01 01:39:04',	2,	'done',	'C00010=0,60 C00001=0,40 C00002=0,80 C00003=0,60 C00004=0,40 C00005=0,80 C00006=0,60 C00007=0,40 C00008=0,80 C00009=0,60 C00011=0,00 C00012=0,00 C00013=0,00 C00014=1,00 C00015=1,00 C00016=1,00 ',	'D00001=0,96 D00002=0,99 D00003=1,00 ',	'D00001=96,01 D00002=99,20 D00003=100,00 '),
(6,	'2017-04-01 02:12:58',	3,	'cancel',	'CANCELLED',	'CANCELLED',	'CANCELLED'),
(7,	'2017-04-01 02:33:57',	2,	'done',	'C00001=0,60 C00002=0,60 C00003=0,60 C00004=0,60 C00005=0,60 C00006=0,60 C00007=0,60 C00008=0,60 C00009=0,60 C00010=0,60 C00011=0,60 C00012=0,60 C00013=0,60 C00014=0,60 C00015=0,60 C00016=0,60 ',	'D00001=0,99 D00002=0,98 D00003=0,97 ',	'D00001=99,35 D00002=98,08 D00003=97,44 '),
(8,	'2017-04-01 14:31:32',	3,	'cancel',	'CANCELLED',	'CANCELLED',	'CANCELLED'),
(9,	'2017-04-01 14:33:38',	2,	'done',	'C00010=0,40 C00001=0,20 C00002=0,00 C00003=0,00 C00004=0,00 C00005=0,00 C00006=0,00 C00007=0,60 C00008=0,60 C00009=0,80 C00011=1,00 C00012=0,00 C00013=0,00 C00014=0,00 C00015=0,00 C00016=0,00 ',	'D00001=0,97 D00002=0,20 D00003=0,40 ',	'D00001=97,37 D00002=20,00 D00003=40,00 '),
(10,	'2017-04-01 14:34:52',	2,	'done',	'C00010=0,60 C00001=0,60 C00002=0,60 C00003=0,60 C00004=0,60 C00005=0,60 C00006=0,60 C00007=0,60 C00008=0,60 C00009=0,60 C00011=0,60 C00012=0,60 C00013=0,60 C00014=0,60 C00015=0,60 C00016=0,60 ',	'D00001=0,99 D00002=0,98 D00003=0,97 ',	'D00001=99,35 D00002=98,08 D00003=97,44 ');

DROP TABLE IF EXISTS `data_gejala`;
CREATE TABLE `data_gejala` (
  `gejala_id` char(6) NOT NULL,
  `gejala_deskripsi` text NOT NULL,
  PRIMARY KEY (`gejala_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_gejala` (`gejala_id`, `gejala_deskripsi`) VALUES
('C00001',	'Lama mencatat'),
('C00002',	'Tidak bisa membaca tulisan sendiri'),
('C00003',	'Tulisan ; Huruf besar dan kecil campur-aduk, dan tidak sejajar'),
('C00004',	'Kertas lusuh, penuh coretan, dan bekas Tipp-Ex'),
('C00005',	'Memegang alat tulis terlihat janggal'),
('C00006',	'Sering mengeluh tangannya sakit jika menulis'),
('C00007',	'Kesulitan melakukan kegiatan yang sederhana, seperti misalnya mengikat tali sepatu'),
('C00008',	'Sangat benci ketika mendapat nilai jelek di pelajaran olahraga'),
('C00009',	'Kesulitan dalam menghafal urutan seperti alphabet'),
('C00010',	'Susah berkonsentrasi'),
('C00011',	'Sulit mendengar jika ada banyak orang'),
('C00012',	'Berbicara dengan suara keras, bicara cepat, dan sering menginterupsi'),
('C00013',	'Sulit diajak bercanda'),
('C00014',	'Tidak bisa diam'),
('C00015',	'Sulit menyelesaikan tugas'),
('C00016',	'Pelupa\n');

DROP TABLE IF EXISTS `data_nilai_cf`;
CREATE TABLE `data_nilai_cf` (
  `value` float NOT NULL,
  `description` varchar(16) NOT NULL,
  PRIMARY KEY (`value`),
  UNIQUE KEY `description` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_nilai_cf` (`value`, `description`) VALUES
(0.6,	'Cukup Yakin'),
(1,	'Sangat Yakin'),
(0.4,	'Sedikit Yakin'),
(0,	'Tidak'),
(0.2,	'Tidak Tahu'),
(0.8,	'Yakin');

DROP TABLE IF EXISTS `data_penyakit`;
CREATE TABLE `data_penyakit` (
  `penyakit_id` char(6) NOT NULL,
  `penyakit_nama` varchar(200) NOT NULL,
  `penyakit_deskripsi` text,
  `penyakit_pencegahan` text,
  `penyakit_pengobatan` text,
  `penyakit_gambar` text,
  `penyakit_lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`penyakit_id`),
  UNIQUE KEY `penyakit_nama` (`penyakit_nama`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_penyakit` (`penyakit_id`, `penyakit_nama`, `penyakit_deskripsi`, `penyakit_pencegahan`, `penyakit_pengobatan`, `penyakit_gambar`, `penyakit_lastupdate`) VALUES
('D00001',	'Dyspraxia',	'Dyspraxia, a form of developmental coordination disorder (DCD) is a common disorder affecting fine and/or gross motor coordination in children and adults. It may also affect speech. DCD is a lifelong condition, formally recognised by international organisations including the World Health Organisation. DCD is distinct from other motor disorders such as cerebral palsy and stroke, and occurs across the range of intellectual abilities. Individuals may vary in how their difficulties present: these may change over time depending on environmental demands and life experiences.\r\nAn individual’s coordination difficulties may affect participation and functioning of everyday life skills in education, work and employment.\r\nChildren may present with difficulties with self-care, writing, typing, riding a bike and play as well as other educational and recreational activities. In adulthood many of these difficulties will continue, as well as learning new skills at home, in education and work, such as driving a car and DIY.\r\nThere may be a range of co-occurring difficulties which can also have serious negative impacts on daily life. These include social and emotional difficulties as well as problems with time management, planning and personal organisation, and these may also affect an adult’s education or employment experiences.\r\nMany people with DCD also experience difficulties with memory, perception and processing. While DCD is often regarded as an umbrella term to cover motor coordination difficulties, dyspraxia refers to those people who have additional problems planning, organising and carrying out movements in the right order in everyday situations. Dyspraxia can also affect articulation and speech, perception and thought.',	'The cause of dyspraxia is not clearly understood. It is considered to develop as a result of abnormal development of damage to the nerve cells called neurons. It may be seen since birth due to oxygen depletion during childbirth. It can also be acquired later due to the damage of the brain tissue in conditions like a stroke or trauma. It may also be familial or hereditary in nature.',	'Dyspraxia is a lifelong disease which has no cure. The treatment modalities include special educational facility, occupational therapy, physical therapy, orthotic treatment, all of which may help in improving the quality of life. The primary treatment modality is to provide special education facilities to such affected individuals. Innovative teaching methodologies are required to overcome the learning blocks and make such children cope with reading and writing skills. Speech therapists may also help in the improvement of speech, if needed.\n\nPhysical exercises and motor training may be needed to overcome the muscular coordination problems. Orthotic devices, walkers or wheelchairs may be needed in certain individuals who are unable to walk properly. The entire treatment plan is designed to improve the capability and overcome developmental shortcomings in affected children and help them lead a fulfilling and independent life.',	'http://redbridgeserc.org/images/illustrations/Dyspraxia.jpg',	NULL),
('D00002',	'Dysgraphia',	'Dysgraphia is a deficiency in the ability to write, primarily handwriting, but also coherence. Dysgraphia is a transcription disability, meaning that it is a writing disorder associated with impaired handwriting, orthographic coding (orthography, the storing process of written words and processing the letters in those words), and finger sequencing (the movement of muscles required to write). It often overlaps with other learning disabilities such as speech impairment, attention deficit disorder, or developmental coordination disorder. In the Diagnostic and Statistical Manual of Mental Disorders (DSM-IV), dysgraphia is characterized as a learning disability in the category of written expression when one’s writing skills are below those expected given a person’s age measured through intelligence and age-appropriate education. The DSM is not clear in whether or not writing refers only to the motor skills involved in writing, or if it also includes orthographic skills and spelling.',	'A few people with dysgraphia lack only the fine-motor coordination to produce legible handwriting, but some may have a physical tremor that interferes with writing. In most cases, however, several brain systems interact to produce dysgraphia. Some experts believe that dysgraphia involves a dysfunction in the interaction between the two main brain systems that allows a person to translate mental into written language (phoneme-to-grapheme translation, i.e., sound to symbol, and lexicon-to-grapheme translation, i.e., mental to written word). Other studies have shown that split attention, memory load, and familiarity of graphic material affect writing ability. Typically, a person with illegible handwriting has a combination of fine-motor difficulty, inability to re-visualize letters, and inability to remember the motor patterns of letter forms.',	'Treatment for dysgraphia varies and may include treatment for motor disorders to help control writing movements. Other treatments may address impaired memory or other neurological problems. Some physicians recommend that individuals with dysgraphia use computers to avoid the problems of handwriting.\r\nOccupational therapy should be considered to correct an inefficient pencil grasp, strengthen muscle tone, improve dexterity, and evaluate eye-hand coordination. Dysgraphic children should also be evaluated for ambidexterity, which can delay fine motor skills in early childhood.',	'http://homeschoolgameschool.com/wp-content/uploads/2016/03/dysgraphi11.jpg',	NULL),
('D00003',	'Attention Deficit and Hyperactivity Disorder (ADHD)',	'Attention deficit hyperactivity disorder (ADHD) is a mental disorder of the neurodevelopmental type. It is characterized by problems paying attention, excessive activity, or difficulty controlling behavior which is not appropriate for a person\'s age. These symptoms begin by age six to twelve, are present for more than six months, and cause problems in at least two settings (such as school, home, or recreational activities). In children, problems paying attention may result in poor school performance.[3] Although it causes impairment, particularly in modern society, many children with ADHD have a good attention span for tasks they find interesting.',	'Though there is no way to prevent ADHD , there are ways to help all children feel and do their best at home and at school.',	'Stimulant medications commonly prescribed for attention deficit disorder include methylphenidate (Ritalin, Concerta, Metadate, Methylin) and certain amphetamines (Dexedrine, Dextrostat, Adderall). Methylphenidate is a short acting drug, and in older forms, had to be taken multiple times a day. Longer-acting versions of the drug are now available for once-daily use. Although taking stimulants for treatment may seem risky, there is significant research that demonstrates that when taken as directed by your psychiatrist or physician, they are safe and effective in the treatment of adult ADHD.',	'http://www.healthline.com/hlcmsresource/images/topic_centers/adhd/adhd-stats/Intro_1_ADHD-Stats.jpg',	NULL),
('D00004',	'test',	'test',	'test',	'test',	'test',	NULL);

DROP TABLE IF EXISTS `data_relasi_gp`;
CREATE TABLE `data_relasi_gp` (
  `penyakit_id` char(6) NOT NULL,
  `gejala_id` char(6) NOT NULL,
  `relasi_cf` float NOT NULL DEFAULT '0',
  KEY `gejala_id` (`gejala_id`),
  KEY `penyakit_id` (`penyakit_id`),
  KEY `relasi_cf` (`relasi_cf`),
  CONSTRAINT `data_relasi_gp_ibfk_5` FOREIGN KEY (`gejala_id`) REFERENCES `data_gejala` (`gejala_id`) ON UPDATE CASCADE,
  CONSTRAINT `data_relasi_gp_ibfk_7` FOREIGN KEY (`penyakit_id`) REFERENCES `data_penyakit` (`penyakit_id`) ON UPDATE CASCADE,
  CONSTRAINT `data_relasi_gp_ibfk_9` FOREIGN KEY (`relasi_cf`) REFERENCES `data_nilai_cf` (`value`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_relasi_gp` (`penyakit_id`, `gejala_id`, `relasi_cf`) VALUES
('D00001',	'C00007',	1),
('D00001',	'C00008',	1),
('D00001',	'C00009',	0.8),
('D00001',	'C00010',	0.6),
('D00001',	'C00011',	0.4),
('D00001',	'C00012',	1),
('D00001',	'C00013',	1),
('D00002',	'C00001',	1),
('D00002',	'C00002',	1),
('D00002',	'C00003',	0.4),
('D00002',	'C00004',	0.4),
('D00002',	'C00005',	1),
('D00002',	'C00006',	0.8),
('D00003',	'C00010',	1),
('D00003',	'C00014',	1),
('D00003',	'C00015',	1),
('D00003',	'C00016',	1);

DROP TABLE IF EXISTS `data_user`;
CREATE TABLE `data_user` (
  `user_id` bigint(24) NOT NULL AUTO_INCREMENT,
  `user_email` varchar(254) NOT NULL,
  `user_nama` varchar(32) NOT NULL,
  `user_pass` text,
  `user_female` bit(1) NOT NULL DEFAULT b'0',
  `grup_id` int(11) NOT NULL,
  `user_registerdate` datetime DEFAULT NULL,
  `user_lastlogindate` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_email` (`user_email`),
  KEY `grup_id` (`grup_id`),
  CONSTRAINT `data_user_ibfk_2` FOREIGN KEY (`grup_id`) REFERENCES `data_user_grup` (`grup_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_user` (`user_id`, `user_email`, `user_nama`, `user_pass`, `user_female`, `grup_id`, `user_registerdate`, `user_lastlogindate`) VALUES
(1,	'admin@localhost',	'Super Admin',	'root',	CONV('0', 2, 10) + 0,	1,	'2017-03-26 15:16:41',	'2017-04-01 02:56:51'),
(2,	'test_expert@localhost',	'Test Expert User',	'test',	CONV('1', 2, 10) + 0,	2,	'2017-03-25 20:25:45',	'2017-04-01 14:39:22'),
(3,	'test_patient@localhost',	'Test Patient User',	'test',	CONV('0', 2, 10) + 0,	3,	'2017-03-25 20:25:52',	'2017-04-01 14:31:03');

DROP TABLE IF EXISTS `data_user_grup`;
CREATE TABLE `data_user_grup` (
  `grup_id` int(11) NOT NULL AUTO_INCREMENT,
  `grup_name` varchar(32) NOT NULL,
  PRIMARY KEY (`grup_id`),
  UNIQUE KEY `grup_name` (`grup_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `data_user_grup` (`grup_id`, `grup_name`) VALUES
(1,	'admin'),
(2,	'expert'),
(3,	'patient');

DROP VIEW IF EXISTS `view_diagnosa`;
CREATE TABLE `view_diagnosa` (`diagnosa_id` bigint(20), `user_id` bigint(24), `user_nama` varchar(32), `diagnosa_date` datetime, `status` enum('done','cancel'), `cf_user` text, `cf_kombinasi` text, `cf_persentase` text);


DROP VIEW IF EXISTS `view_gejala_count`;
CREATE TABLE `view_gejala_count` (`gejala_id` char(6), `gejala_deskripsi` text, `count` bigint(21));


DROP VIEW IF EXISTS `view_loguser`;
CREATE TABLE `view_loguser` (`id` bigint(24), `email` varchar(254), `nama` varchar(32), `pass` text, `grup_id` int(11), `register` datetime, `lastlogin` datetime);


DROP VIEW IF EXISTS `view_relasi_gp`;
CREATE TABLE `view_relasi_gp` (`penyakit_id` char(6), `penyakit_nama` varchar(200), `gejala_id` char(6), `gejala_deskripsi` text, `cf` float);


DROP TABLE IF EXISTS `view_diagnosa`;
CREATE ALGORITHM=UNDEFINED DEFINER=`spa_user`@`localhost` SQL SECURITY DEFINER VIEW `view_diagnosa` AS select `data_diagnosa`.`diagnosa_id` AS `diagnosa_id`,`data_diagnosa`.`user_id` AS `user_id`,`data_user`.`user_nama` AS `user_nama`,`data_diagnosa`.`diagnosa_date` AS `diagnosa_date`,`data_diagnosa`.`status` AS `status`,`data_diagnosa`.`cf_user` AS `cf_user`,`data_diagnosa`.`cf_kombinasi` AS `cf_kombinasi`,`data_diagnosa`.`cf_persentase` AS `cf_persentase` from (`data_diagnosa` left join `data_user` on((`data_user`.`user_id` = `data_diagnosa`.`user_id`))) order by `data_diagnosa`.`diagnosa_id`;

DROP TABLE IF EXISTS `view_gejala_count`;
CREATE ALGORITHM=UNDEFINED DEFINER=`spa_user`@`localhost` SQL SECURITY DEFINER VIEW `view_gejala_count` AS select `gp`.`gejala_id` AS `gejala_id`,`g`.`gejala_deskripsi` AS `gejala_deskripsi`,count(0) AS `count` from (`data_relasi_gp` `gp` left join `data_gejala` `g` on((`gp`.`gejala_id` = `g`.`gejala_id`))) group by `gp`.`gejala_id` order by count(0) desc,`gp`.`gejala_id`;

DROP TABLE IF EXISTS `view_loguser`;
CREATE ALGORITHM=UNDEFINED DEFINER=`spa_user`@`localhost` SQL SECURITY DEFINER VIEW `view_loguser` AS select `u`.`user_id` AS `id`,`u`.`user_email` AS `email`,`u`.`user_nama` AS `nama`,`u`.`user_pass` AS `pass`,`u`.`grup_id` AS `grup_id`,`u`.`user_registerdate` AS `register`,`u`.`user_lastlogindate` AS `lastlogin` from `data_user` `u`;

DROP TABLE IF EXISTS `view_relasi_gp`;
CREATE ALGORITHM=UNDEFINED DEFINER=`spa_user`@`localhost` SQL SECURITY DEFINER VIEW `view_relasi_gp` AS select `r`.`penyakit_id` AS `penyakit_id`,`p`.`penyakit_nama` AS `penyakit_nama`,`r`.`gejala_id` AS `gejala_id`,`g`.`gejala_deskripsi` AS `gejala_deskripsi`,`r`.`relasi_cf` AS `cf` from ((`data_relasi_gp` `r` join `data_gejala` `g` on((`r`.`gejala_id` = `g`.`gejala_id`))) join `data_penyakit` `p` on((`r`.`penyakit_id` = `p`.`penyakit_id`))) order by `r`.`penyakit_id`,`r`.`gejala_id`;

-- 2017-05-26 18:08:51