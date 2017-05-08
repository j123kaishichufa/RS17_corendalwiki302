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
 * Version: $Id: create_phys_addr_table_oracle.sql,v 1.6 2007/01/03 17:43:27 tdanard Exp $
 */

drop table phys_addr;

create table phys_addr
   (id varchar2(36) primary key not null
   ,post_addr_1 varchar2(255) 
   ,post_addr_2 varchar2(255)
   ,post_addr_3 varchar2(255)
   ,post_addr_4 varchar2(255)
   ,city_id varchar2(36) 
   ,zip_cod_id varchar2(36) 
   ,state_id varchar2(36) 
   ,country_id varchar2(36) 
   ,enabled_flag varchar2(1) not null
   ) ;
   

create index phys_addr_post_addr_1_ctx on phys_addr(post_addr_1) indextype is ctxsys.context parameters ('sync(on commit)');

create index phys_addr_post_addr_2_ctx on phys_addr(post_addr_2) indextype is ctxsys.context parameters ('sync(on commit)');

create index phys_addr_post_addr_3_ctx on phys_addr(post_addr_3) indextype is ctxsys.context parameters ('sync(on commit)');

create index phys_addr_post_addr_4_ctx on phys_addr(post_addr_4) indextype is ctxsys.context parameters ('sync(on commit)');

drop sequence phys_addr_seq;

create sequence phys_addr_seq start with 2001 order;
