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
 * Version: $Id: create_phys_addr_table_mysql.sql,v 1.4 2007/01/03 17:43:28 tdanard Exp $
 */

drop table if exists phys_addr;

create table phys_addr
   (id varchar(36) primary key not null
   ,post_addr_1 varchar(255) 
   ,post_addr_2 varchar(255)
   ,post_addr_3 varchar(255)
   ,post_addr_4 varchar(255)
   ,city_id varchar(36) 
   ,zip_cod_id varchar(36) 
   ,state_id varchar(36) 
   ,country_id varchar(36) 
   ,enabled_flag varchar(1) not null
   ,FULLTEXT(post_addr_1)
   ,FULLTEXT(post_addr_2)
   ,FULLTEXT(post_addr_3)
   ,FULLTEXT(post_addr_4)
   ) TYPE=MyISAM;

drop table if exists phys_addr_seq;

create table phys_addr_seq
    (id int not null primary key auto_increment
    ,timestamp datetime);

insert into phys_addr_seq(id, timestamp) values (2000, now());