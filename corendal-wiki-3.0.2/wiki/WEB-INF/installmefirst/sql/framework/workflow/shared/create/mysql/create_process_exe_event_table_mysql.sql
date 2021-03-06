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
 * Version: $Id: create_process_exe_event_table_mysql.sql,v 1.4 2007/01/03 17:43:27 tdanard Exp $
 */

drop table if exists process_exe_event;

create table process_exe_event
   (id varchar(36) primary key not null
   ,process_exe_id varchar(36) not null
   ,process_def_event_id varchar(36) not null
   ,cmnt longtext
   ,timestamp datetime not null
   ,ord integer not null
   ,enabled_flag varchar(1) not null
   ) type = innodb;

drop table if exists process_exe_event_seq;

create table process_exe_event_seq
    (id int not null primary key auto_increment
    ,timestamp datetime);
