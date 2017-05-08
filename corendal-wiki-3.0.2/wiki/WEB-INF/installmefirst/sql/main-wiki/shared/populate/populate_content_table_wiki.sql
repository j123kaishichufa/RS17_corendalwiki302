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
 * Version: $Id: populate_content_table_wiki.sql,v 1.1 2005/09/06 21:25:34 tdanard Exp $
 */

delete from content;

/* home page */
insert into content
   (id
   ,content_typ_id
   ,content_class_typ_id
   ,whats_new_flag
   ,content_rule_typ_id
   ,ord
   ,enabled_flag
   ) values
   ('1'
   ,'WIKI-REST-2'
   ,'WIKI-RESC-2'
   ,'Y'
   ,'WIKI-RESR-4'
   ,1
   ,'Y');
