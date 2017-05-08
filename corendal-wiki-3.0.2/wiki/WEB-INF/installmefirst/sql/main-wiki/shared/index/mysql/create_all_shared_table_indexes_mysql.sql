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
 * Version: $Id: create_all_shared_table_indexes_mysql.sql,v 1.2 2006/04/03 00:29:55 tdanard Exp $
 */

select '/main-wiki/shared/index/mysql/create_all_shared_table_indexes_mysql.sql';

create index content_version_request_id_idx on content_version(request_id);
create index content_version_content_id_idx on content_version(content_id);
create index content_version_friendly_a_idx on content_version(friendly_address);
create index content_version_version_i1_idx on content_version(version_info_id);

create index content_request_request_t1_idx on content_request(request_typ_id);
create index content_request_content_i1_idx on content_request(content_info_id);
create index content_request_content_t1_idx on content_request(content_typ_id);
create index content_request_status_ty1_idx on content_request(status_typ_id);
create index content_request_parent_co1_idx on content_request(parent_content_id);
create index content_request_child_con1_idx on content_request(child_content_id);

create index content_map_parent_conten1_idx on content_map(parent_content_id);
create index content_map_child_content1_idx on content_map(child_content_id);
create index content_map_request_id_idx on content_map(request_id);

create index content_access_log_conten1_idx on content_access_log(content_id);

create index content_access_day_conten1_idx on content_access_day(content_id);
create index content_access_day_access1_idx on content_access_day(access_date);

create index content_access_period_con1_idx on content_access_period(content_id);
create index content_access_period_acc1_idx on content_access_period(access_date);

create index content_content_typ_id_idx on content(content_typ_id);
create index content_body_content_vers1_idx on content_body(content_version_id);
