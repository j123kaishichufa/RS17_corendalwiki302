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
 * Version: $Id: install_all_shared_mysql.sql,v 1.2 2006/04/22 23:04:05 tdanard Exp $
 */

select '/main-wiki/shared/install/mysql/install_all_shared_mysql.sql';

source ./sql/framework/core/shared/install/mysql/install_all_shared_mysql.sql;
source ./sql/framework/helpdesk/shared/install/mysql/install_all_shared_mysql.sql;

source ./sql/main-wiki/shared/create/mysql/create_content_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_map_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_version_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_body_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_info_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_access_log_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_access_day_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_access_period_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_subscript_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_request_table_mysql.sql;
source ./sql/main-wiki/shared/create/mysql/create_content_address_table_mysql.sql;

source ./sql/main-wiki/shared/install/mysql/install_all_populate_shared_tables_mysql.sql;

source ./sql/main-wiki/shared/index/mysql/create_all_shared_table_indexes_mysql.sql;
