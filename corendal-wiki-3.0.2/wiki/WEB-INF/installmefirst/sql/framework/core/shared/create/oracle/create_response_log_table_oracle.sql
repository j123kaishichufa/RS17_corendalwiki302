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
 * Version: $Id: create_response_log_table_oracle.sql,v 1.1 2007/10/25 00:33:50 tdanard Exp $
 */

drop table response_log;

create table response_log
   (id varchar2(36) primary key not null
   ,timestamp date not null
   ,page_id varchar2(36) not null
   ,complete_time number not null
   ,response_cod number not null
   ,response_log_typ_id varchar2(36) not null
   ,enabled_flag varchar2(1) not null
   ) ;

drop sequence response_log_seq;

create sequence response_log_seq start with 2001 order;

