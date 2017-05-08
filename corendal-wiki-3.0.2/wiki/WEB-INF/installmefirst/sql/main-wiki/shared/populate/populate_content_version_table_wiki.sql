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
 * Version: $Id: populate_content_version_table_wiki.sql,v 1.2 2006/04/22 23:04:05 tdanard Exp $
 */

delete from content_version;

/* home */
insert into content_version
   (id
   ,current_flag
   ,version_num
   ,version_info_id
   ,content_id
   ,request_id
   ,friendly_address
   ,body
   ,cmnt
   ,enabled_flag
   ) values
   ('1'
   ,'Y'
   ,1
   ,'1'
   ,'1'
   ,null
   ,null
   ,'Welcome to this Wiki.<p>To write a new article, click the "Edit Article" tab, select a word, click the "Link" icon, enter a URL, press submit, click the "View Article" tab, and follow the red link that was just created.'
   ,null
   ,'Y');

