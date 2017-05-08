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
 * Version: $Id: create_all_shared_table_indexes_oracle.sql,v 1.5 2007/01/19 23:09:37 tdanard Exp $
 */

select '/main-wiki/shared/index/oracle/create_all_shared_table_indexes_oracle.sql' from dual;

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

create index content_content__idx on content(content_class_typ_id);
create index content_content2_idx on content(content_rule_typ_id);

create index content_request_mater_idx on content_request(content_class_typ_id);
create index content_request_mate2_idx on content_request(content_rule_typ_id);
create index content_subscript_mat_idx on content_subscript(content_id);
create index content_subscript_ma2_idx on content_subscript(content_subscript_typ_id);
create index content_subscript_ma3_idx on content_subscript(content_subscript_mod_id);

create index content_whats_new_fla_idx on content(whats_new_flag);
create index content_enabled_flag_idx on content(enabled_flag);
create index content_access_day_en_idx on content_access_day(enabled_flag);
create index content_access_log_en_idx on content_access_log(enabled_flag);
create index content_access_period_idx on content_access_period(enabled_flag);
create index content_body_enabled__idx on content_body(enabled_flag);
create index content_info_enabled__idx on content_info(enabled_flag);
create index content_map_main_flag_idx on content_map(main_flag);
create index content_map_enabled_f_idx on content_map(enabled_flag);
create index content_request_enabl_idx on content_request(enabled_flag);
create index content_subscript_ena_idx on content_subscript(enabled_flag);
create index content_version_curre_idx on content_version(current_flag);
create index content_version_enabl_idx on content_version(enabled_flag);
