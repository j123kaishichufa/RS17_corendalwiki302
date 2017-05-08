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
 * Version: $Id: create_state_table_oracle.sql,v 1.3 2007/01/03 17:43:28 tdanard Exp $
 */

drop table state;

create table state
   (id varchar2(36) primary key not null
   ,cod varchar2(255)
   ,nam varchar2(255)
   ,country_id varchar2(36)
   ,enabled_flag varchar2(1) not null
   ) ;
   
create index state_cod_ctx on state(cod) indextype is ctxsys.context parameters ('sync(on commit)');

create index state_nam_ctx on state(nam) indextype is ctxsys.context parameters ('sync(on commit)');

drop sequence state_seq;

create sequence state_seq start with 2001 order;
