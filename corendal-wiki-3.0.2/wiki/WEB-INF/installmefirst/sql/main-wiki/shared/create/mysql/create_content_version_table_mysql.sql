/*
 * Corendal NetApps Wiki - Web application for content management
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
 * Version: $Id: create_content_version_table_mysql.sql,v 1.2 2006/01/23 18:37:06 tdanard Exp $
 */

drop table if exists content_version;

create table content_version
   (id varchar(36) primary key not null
   ,current_flag varchar(1) not null
   ,version_num integer not null
   ,version_info_id varchar(36) not null
   ,content_id varchar(36) not null
   ,request_id varchar(36)
   ,friendly_address varchar(255)
   ,body longtext
   ,cmnt longtext
   ,enabled_flag varchar(1) not null
   ) type = innodb;

drop table if exists content_version_seq;

create table content_version_seq
    (id int not null primary key auto_increment
    ,timestamp datetime);
 
insert into content_version_seq(id, timestamp) values (2000, now());
