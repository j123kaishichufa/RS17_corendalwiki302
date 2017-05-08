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
 * Version: $Id: create_all_shared_table_indexes_mysql.sql,v 1.1 2007/01/03 17:43:30 tdanard Exp $
 */
 
select '/helpdesk/core/shared/index/mysql/create_all_shared_table_indexes_mysql.sql';
 
create index group_obj_role_xref_group1_idx on group_obj_role_xref(group_obj_id);
create index group_obj_role_xref_role_1_idx on group_obj_role_xref(role_id);
create index group_obj_role_xref_entit1_idx on group_obj_role_xref(entity_id);
create index group_obj_role_xref_recor1_idx on group_obj_role_xref(record_id);

create index group_obj_group_obj_info_1_idx on group_obj(group_obj_info_id);
create index group_obj_group_obj_typ_i1_idx on group_obj(group_obj_typ_id);
