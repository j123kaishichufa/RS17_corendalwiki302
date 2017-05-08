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
 * Version: $Id: create_content_subscript_table_oracle.sql,v 1.2 2006/06/15 21:46:41 tdanard Exp $
 */

drop table content_subscript;

create table content_subscript
   (id varchar2(36) primary key not null
   ,content_id varchar2(36) not null
   ,content_subscript_typ_id varchar2(36) not null
   ,content_subscript_mod_id varchar2(36) not null
   ,ord number not null
   ,enabled_flag varchar2(1) not null
   ) ;

drop sequence content_subscript_seq;

create sequence content_subscript_seq start with 2001 order;
