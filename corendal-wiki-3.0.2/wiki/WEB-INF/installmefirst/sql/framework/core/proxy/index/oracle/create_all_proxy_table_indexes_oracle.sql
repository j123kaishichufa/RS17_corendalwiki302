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
 * Version: $Id: create_all_proxy_table_indexes_oracle.sql,v 1.2 2007/01/03 17:43:31 tdanard Exp $
 */
 
select '/framework/core/proxy/index/oracle/create_all_proxy_table_indexes_oracle.sql' from dual;
 
create index account_role_xref_account1_idx on account_role_xref(account_id);
create index account_role_xref_role_id_idx on account_role_xref(role_id);
create index account_role_xref_entity_1_idx on account_role_xref(entity_id);
create index account_role_xref_record_1_idx on account_role_xref(record_id);

create index proxy_email_pref_id_idx on proxy(proxy_email_pref_id);
create index proxy_read_pref_id_idx on proxy(proxy_read_pref_id);
create index proxy_app_id_idx on proxy(app_id);
