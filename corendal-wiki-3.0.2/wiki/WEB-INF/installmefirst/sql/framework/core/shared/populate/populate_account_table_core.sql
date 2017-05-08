/*
 * Corendal NetApps Directory - Web application for account management
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
 * Version: $Id: populate_account_table_core.sql,v 1.4 2007/01/03 17:43:29 tdanard Exp $
 */

delete from account;
delete from person;
delete from password;
delete from username_search;

/* add admin person */
insert into person
   (id
   ,enabled_flag
   ) values
   ('demoadmin'
   ,'Y'
   );

/* add admin account */
insert into account
   (id
   ,person_id
   ,active_flag
   ,enabled_flag
   ) values
   ('demoadmin'
   ,'demoadmin'
   ,'Y'
   ,'Y'
   );

/* add "admin" password for "admin" account */
insert into password
   (id
   ,account_id
   ,password
   ,enabled_flag
   ) values
   ('1'
   ,'demoadmin'
   ,'0DPiKuNIrrVmD8IUCuw1hQxNqZc' /* encrypted value for password "admin" */
   ,'Y'
   );

/* add admin user to user name index */
insert into username_search
   (id
   ,person_id
   ,account_id
   ,nam
   ,enabled_flag
   ) values
   ('1'
   ,'demoadmin'
   ,'demoadmin'
   ,'DEMOADMIN'
   ,'Y'
   );


/* add guest person */
insert into person
   (id
   ,enabled_flag
   ) values
   ('demoguest'
   ,'Y'
   );

/* add guest native account */
insert into account
   (id
   ,person_id
   ,active_flag
   ,enabled_flag
   ) values
   ('demoguest'
   ,'demoguest'
   ,'Y'
   ,'Y'
   );

/* add "guest" password for "guest" account */
insert into password
   (id
   ,account_id
   ,password
   ,enabled_flag
   ) values
   ('2'
   ,'demoguest'
   ,'NWdeaPS1r3uZXZIFrQ/EOELxZFA' /* encrypted value for password "guest" */
   ,'Y'
   );

/* add guest user to user name index */
insert into username_search
   (id
   ,person_id
   ,account_id
   ,nam
   ,enabled_flag
   ) values
   ('2'
   ,'demoguest'
   ,'demoguest'
   ,'DEMOGUEST'
   ,'Y'
   );

