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
 * Version: $Id: populate_content_info_table_wiki.sql,v 1.1 2005/09/06 21:25:34 tdanard Exp $
 */

delete from content_info;

/* home page */
insert into content_info
   (id
   ,nam
   ,dsc
   ,enabled_flag
   ) values
   ('1'
   ,'Home'
   ,'Home Page'
   ,'Y');

