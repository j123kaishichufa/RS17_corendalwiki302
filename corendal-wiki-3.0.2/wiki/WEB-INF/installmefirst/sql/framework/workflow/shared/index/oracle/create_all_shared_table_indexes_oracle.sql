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
 * Version: $Id: create_all_shared_table_indexes_oracle.sql,v 1.3 2007/01/03 17:43:29 tdanard Exp $
 */
 
select '/framework/workflow/shared/index/oracle/create_all_shared_table_indexes_oracle.sql' from dual;

create index process_exe_date_process_1_idx on process_exe_date(process_exe_id);
create index process_exe_date_date_typ1_idx on process_exe_date(date_typ_id);
create index process_exe_date_custom_t1_idx on process_exe_date(custom_timestamp);

create index process_exe_record_id_idx on process_exe(record_id);
create index process_exe_entity_id_idx on process_exe(entity_id);
create index process_exe_process_1_idx on process_exe(process_def_version_id);

create index process_exe_event_process1_idx on process_exe_event(process_exe_id);
create index process_exe_event_process2_idx on process_exe_event(process_def_event_id);

create index process_exe_stat_process_1_idx on process_exe_stat(process_exe_id);
create index process_exe_stat_process_2_idx on process_exe_stat(process_def_stat_id);

create index process_exe_trans_process1_idx on process_exe_trans(process_exe_id);
create index process_exe_trans_process2_idx on process_exe_trans(process_def_trans_id);

create index process_exe_trans_stat_ma1_idx on process_exe_trans_stat_map(process_exe_trans_id);
create index process_exe_trans_stat_ma2_idx on process_exe_trans_stat_map(parent_process_exe_stat_id);
create index process_exe_trans_stat_ma3_idx on process_exe_trans_stat_map(child_process_exe_stat_id);
create index process_exe_trans_stat_ma4_idx on process_exe_trans_stat_map(process_def_trans_stat_map_id);
