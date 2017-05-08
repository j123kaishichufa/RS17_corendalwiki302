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
 * Version: $Id: create_content_map_table_mysql.sql,v 1.1 2005/09/06 21:25:34 tdanard Exp $
 */

drop table if exists content_map;

create table content_map
   (id varchar(36) primary key not null
   ,parent_content_id varchar(36) not null
   ,child_content_id varchar(36) not null
   ,main_flag varchar(1) not null
   ,request_id varchar(36)
   ,ord integer not null
   ,enabled_flag varchar(1) not null
   ) type = innodb;

drop table if exists content_map_seq;

create table content_map_seq
    (id int not null primary key auto_increment
    ,timestamp datetime);
    
insert into content_map_seq(id, timestamp) values (2000, now());
