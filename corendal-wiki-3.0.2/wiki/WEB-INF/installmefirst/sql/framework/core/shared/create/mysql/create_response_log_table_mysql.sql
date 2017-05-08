/*
 * Corendal NetApps Framework - Java framework for web applications
 * Copyright (C) Thierry Danard
 * 
 * This program is free software; you can redistribute it and|or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Version: $Id: create_response_log_table_mysql.sql,v 1.1 2007/10/25 00:34:14 tdanard Exp $
 */


drop table if exists response_log;

create table response_log
   (id varchar(36) primary key not null
   ,timestamp datetime not null
   ,page_id varchar(36) not null
   ,complete_time integer not null
   ,response_cod integer not null
   ,response_log_typ_id varchar(36) not null
   ,enabled_flag varchar(1) not null
   ) type = innodb;
   
drop table if exists response_log_seq;

create table response_log_seq
    (id int not null primary key auto_increment
    ,timestamp datetime);

insert into response_log_seq(id, timestamp) values (2000, now());
