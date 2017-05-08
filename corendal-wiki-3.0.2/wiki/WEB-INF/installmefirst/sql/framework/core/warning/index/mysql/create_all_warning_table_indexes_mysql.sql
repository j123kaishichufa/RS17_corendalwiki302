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
 * Version: $Id: create_all_warning_table_indexes_mysql.sql,v 1.1 2007/01/03 17:43:29 tdanard Exp $
 */
 
select '/framework/core/warning/index/mysql/create_all_warning_table_indexes_mysql.sql';
 
create index warning_extension_id_idx on warning(extension_id);
create index warning_page_id_idx on warning(page_id);
create index warning_app_id_idx on warning(app_id);
create index warning_locale_id_idx on warning(locale_id);


