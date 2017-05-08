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
 * Version: $Id: populate_account_role_xref_table_core.sql,v 1.4 2007/01/03 17:43:29 tdanard Exp $
 */

delete from account_role_xref;

insert into account_role_xref
   (id
   ,account_id
   ,role_id
   ,entity_id
   ,record_id
   ,enabled_flag
   ) values
   ('demoadmin'
   ,'demoadmin'
   ,'CORE-ROLE-1'
   ,null
   ,null
   ,'Y');
