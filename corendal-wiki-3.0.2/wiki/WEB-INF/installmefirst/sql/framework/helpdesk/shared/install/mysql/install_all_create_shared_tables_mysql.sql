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
 * Version: $Id: install_all_create_shared_tables_mysql.sql,v 1.4 2007/07/11 22:41:54 tdanard Exp $
 */

select '/framework/helpdesk/shared/install/mysql/install_all_create_shared_tables_mysql.sql';

source ./sql/framework/helpdesk/shared/create/mysql/create_group_obj_attr_table_mysql.sql;
source ./sql/framework/helpdesk/shared/create/mysql/create_group_obj_table_mysql.sql;
source ./sql/framework/helpdesk/shared/create/mysql/create_group_obj_info_table_mysql.sql;
source ./sql/framework/helpdesk/shared/create/mysql/create_group_obj_role_xref_table_mysql.sql;



