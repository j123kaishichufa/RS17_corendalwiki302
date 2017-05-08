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
 * Version: $Id: create_zip_cod_table_oracle.sql,v 1.3 2007/01/03 17:43:28 tdanard Exp $
 */

drop table zip_cod;

create table zip_cod
   (id varchar2(36) primary key not null
   ,cod_1 varchar2(255) not null
   ,cod_2 varchar2(255)
   ,state_id varchar2(36)
   ,country_id varchar2(36)
   ,enabled_flag varchar2(1) not null
   ) ;
  
create index zip_cod_cod_1_ctx on zip_cod(cod_1) indextype is ctxsys.context parameters ('sync(on commit)');

create index zip_cod_cod_2_ctx on zip_cod(cod_2) indextype is ctxsys.context parameters ('sync(on commit)');

drop sequence zip_cod_seq;

create sequence zip_cod_seq start with 2001 order;
