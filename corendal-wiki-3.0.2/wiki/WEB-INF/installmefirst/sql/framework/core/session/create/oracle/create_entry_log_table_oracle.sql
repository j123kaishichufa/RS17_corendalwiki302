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
 * Version: $Id: create_entry_log_table_oracle.sql,v 1.8 2007/01/03 17:43:29 tdanard Exp $
 */

drop table entry_log;

create table entry_log
   (id varchar2(36) primary key not null
   ,entity_id varchar2(36) not null
   ,record_id varchar2(255) not null
   ,timestamp date not null
   ,typ_id varchar2(36) not null
   ,client_address varchar2(255)
   ,server_address varchar2(255)
   ,session_obj_id varchar2(255)
   ,actual_account_id varchar2(255)
   ,proxy_account_id varchar2(255)
   ,enabled_flag varchar2(1) not null
   ) ;

drop sequence entry_log_seq;

create sequence entry_log_seq start with 2001 order;
