drop index if exists collector_id_index;
drop table if exists collect_relation;
drop index if exists good_id_of_picture;
drop table if exists good_picture;
drop trigger if exists on_delete_second_hand_order_trigger on second_hand_order;
drop table if exists deleted_second_hand_order;
drop table if exists second_hand_order;
drop sequence if exists second_hand_order_id;
drop trigger if exists on_delete_second_hand_good_trigger on second_hand_good;
drop table if exists deleted_second_hand_good;
drop table if exists second_hand_good;
drop sequence if exists second_hand_good_id;
drop index if exists follower_id_index;
drop table if exists follow_relation;
drop table if exists good_type;
drop table if exists v_code_record;
drop table if exists deleted_store_user;
drop table if exists store_user;
drop sequence if exists store_user_id;

create sequence if not exists store_user_id start with 1 increment by 2;

create table if not exists store_user
(
    id                         int primary key          default nextval('store_user_id'),
    phone_number               char(11) unique not null,
    nickname                   varchar(20)     not null default 'H3ll0_W0RlD!',
    card_number                char(8) unique,
    password                   char(32),
    salt                       char(16),
    introduction               varchar(255),
    avatar_path                varchar(255)    not null default 'no_avatar.png',
    sex                        bool,
    alipay_account             varchar(50),
    account_balance            decimal(12, 2)  not null default 0,
    credit_score               decimal(6, 1)   not null default 100,
    total_sold_value           decimal(12, 2)  not null default 0,
    second_hand_notification   bool            not null default true,
    agent_service_notification bool            not null default true,
    api_trade_notification     bool            not null default true,
    banned                     bool            not null default false,
    created_time               timestamp       not null default now(),
    updated_time               timestamp       not null default now()
);
comment on column store_user.sex is 'true: male; false: female';


create table if not exists deleted_store_user
(
    id                         int,
    phone_number               char(11),
    nickname                   varchar(20),
    card_number                char(8),
    password                   char(60),
    salt                       char(16),
    introduction               varchar(255),
    avatar_path                varchar(255),
    sex                        bool,
    alipay_account             varchar(50),
    account_balance            decimal(12, 2),
    credit_score               decimal(6, 1),
    total_sold_value           decimal(12, 2),
    second_hand_notification   bool,
    agent_service_notification bool,
    api_trade_notification     bool,
    banned                     bool,
    created_time               timestamp,
    updated_time               timestamp,
    deleted_time               timestamp
);

create or replace function on_delete_store_user()
    returns trigger as
$$
begin
    insert into deleted_store_user (id, phone_number, nickname, card_number, password, salt, introduction, avatar_path,
                                    sex, alipay_account, credit_score, total_sold_value, second_hand_notification,
                                    agent_service_notification, api_trade_notification, banned, created_time,
                                    updated_time, deleted_time)
    values (old.id, old.phone_number, old.nickname, old.card_number, old.password, old.salt, old.introduction,
            old.avatar_path, old.sex, old.alipay_account, old.credit_score, old.total_sold_value,
            old.second_hand_notification, old.agent_service_notification, old.api_trade_notification, old.banned,
            old.created_time, old.updated_time, now());
    return null;
end;
$$ language plpgsql;

create trigger on_delete_store_user
    after delete
    on store_user
    for each row
execute procedure on_delete_store_user();

create table if not exists v_code_record
(
    id            serial primary key,
    store_user_id int references store_user (id),
    phone_number  char(11),
    card_number   char(8),
    v_code        char(6)   not null,
    expire_time   timestamp not null default now()::timestamp + '5 min',
    type          smallint  not null,
    created_time  timestamp not null default now(),
    used_time     timestamp
);
comment on column v_code_record.type is '0: Login; 1: Change Password; 2: Change Alipay; 3: Cancel Account; 4: Card Verification';

create table if not exists good_type
(
    id           serial primary key,
    type_name    varchar(30) unique not null,
    sort_no      serial             not null,
    created_time timestamp          not null default now()
);

create or replace function move_up_type(type_id int)
    returns bool as
$$
declare
    sort_no1 int;
    sort_no2 int;
    temp_id  int;
begin
    select sort_no into sort_no1 from good_type where id = type_id for update;
    if not exists(select null from good_type where sort_no < sort_no1) then
        return false;
    end if;
    select id, sort_no
    into temp_id, sort_no2
    from good_type
    where sort_no < sort_no1
    order by sort_no desc
    limit 1 for update;

    update good_type set sort_no = sort_no2 where id = type_id;
    update good_type set sort_no = sort_no1 where id = temp_id;
    return true;
end;
$$ language plpgsql;

create or replace function move_down_type(type_id int)
    returns bool as
$$
declare
    sort_no1 int;
    sort_no2 int;
    temp_id  int;
begin
    select sort_no into sort_no1 from good_type where id = type_id for update;
    if not exists(select null from good_type where sort_no > sort_no1) then
        return false;
    end if;
    select id, sort_no
    into temp_id, sort_no2
    from good_type
    where sort_no > sort_no1
    order by sort_no desc
    limit 1 for update;

    update good_type set sort_no = sort_no2 where id = type_id;
    update good_type set sort_no = sort_no1 where id = temp_id;
    return true;
end;
$$ language plpgsql;

insert into good_type (id, type_name, sort_no, created_time)
values (default, 'Digital Products', default, default),
       (default, 'Groceries', default, default),
       (default, 'Clothes', default, default),
       (default, 'Makeups', default, default),
       (default, 'Electrical Goods', default, default),
       (default, 'Books', default, default),
       (default, 'Entertainment', default, default);

create table if not exists follow_relation
(
    followed_id  int references store_user (id) on delete cascade,
    follower_id  int references store_user (id) on delete cascade,
    created_time timestamp not null default now()
);
create index if not exists follower_id_index on follow_relation (follower_id);

create sequence if not exists second_hand_good_id start with 1 increment by 3;
create table if not exists second_hand_good
(
    id           int primary key         default nextval('second_hand_good_id'),
    type_id      int            references good_type (id) on delete set null,
    title        varchar(30)    not null,
    description  varchar(500),
    price        decimal(12, 2) not null,
    publisher    int            not null references store_user (id) on delete cascade,
    sold         bool           not null default false,
    created_time timestamp      not null default now(),
    updated_time timestamp      not null default now()
);

create table if not exists deleted_second_hand_good
(
    id           int,
    title        varchar(30),
    description  varchar(500),
    price        decimal(12, 2),
    publisher    int,
    sold         bool,
    created_time timestamp,
    updated_time timestamp,
    deleted_time timestamp,
    deleted_by   int
);

create or replace function delete_second_hand_good(deleted_good_id int, delete_user_id int)
    returns bool as
$$
begin
    if not exists(select null from second_hand_good where id = deleted_good_id and not sold) then
        return false;
    end if;
    delete from second_hand_good where id = deleted_good_id;
    update deleted_second_hand_good set deleted_by = delete_user_id where id = deleted_good_id;
    return true;
end;
$$
    language plpgsql;

create or replace function on_delete_second_hand_good()
    returns trigger as
$$
begin
    insert into deleted_second_hand_good (id, title, description, price, publisher, sold, created_time, updated_time,
                                          deleted_time, deleted_by)
    values (old.id, old.title, old.description, old.price, old.publisher, old.sold, old.created_time, old.updated_time,
            now(), -1);
    return null;
end;
$$ language plpgsql;

create trigger on_cascade_delete_good_trigger
    after delete
    on second_hand_good
    for each row
execute procedure on_delete_second_hand_good();

create sequence if not exists second_hand_order_id start with 10000001 increment by 3;
create table if not exists second_hand_order
(
    id                int primary key         default nextval('second_hand_order_id'),
    good_id           int            not null references second_hand_good (id) on delete cascade,
    buyer_id          int            not null references store_user (id) on delete cascade,
    order_status      int            not null default 1,
    actual_price      decimal(12, 2) not null,
    trade_location    varchar(255)   not null,
    trade_latitude    varchar(30)    not null,
    trade_longitude   varchar(30)    not null,
    trade_time        timestamp      not null,
    trade_password    char(4),
    deal_code         char(6),
    refund_code       char(6),
    grade_by_seller   smallint,
    grade_by_buyer    smallint,
    comment_by_seller varchar(255),
    comment_by_buyer  varchar(255),
    created_time      timestamp      not null default now(),
    updated_time      timestamp      not null default now()
);
comment on column second_hand_order.order_status is '0: Waiting for acknowledge; 1: Waiting for payment; 2: Trading; 3: Trade Success; 4: Refund Success; 5: Closed';

create table if not exists deleted_second_hand_order
(
    id                int,
    good_id           int,
    buyer_id          int,
    order_status      int,
    actual_price      decimal(12, 2),
    trade_location    varchar(255),
    trade_latitude    varchar(30),
    trade_longitude   varchar(30),
    trade_time        timestamp,
    trade_password    char(4),
    deal_code         char(6),
    refund_code       char(6),
    grade_by_seller   smallint,
    grade_by_buyer    smallint,
    comment_by_seller varchar(255),
    comment_by_buyer  varchar(255),
    created_time      timestamp,
    updated_time      timestamp,
    deleted_time      timestamp
);

create or replace function on_delete_second_hand_order()
    returns trigger as
$$
begin
    insert into deleted_second_hand_order (id, good_id, buyer_id, order_status, actual_price, trade_latitude,
                                           trade_longitude, trade_time, trade_password, deal_code, refund_code,
                                           grade_by_seller, grade_by_buyer, comment_by_seller, comment_by_buyer,
                                           created_time, updated_time, deleted_time)
    values (old.id, old.good_id, old.buyer_id, old.order_status, old.actual_price, old.trade_latitude,
            old.trade_longitude, old.trade_time, old.trade_password, old.deal_code, old.refund_code,
            old.grade_by_seller, old.grade_by_buyer, old.comment_by_seller, old.comment_by_buyer, old.created_time,
            old.updated_time, now());
    return null;
end;
$$
    language plpgsql;

create trigger on_delete_second_hand_order_trigger
    after delete
    on second_hand_order
    for each row
execute procedure on_delete_second_hand_order();

create table if not exists good_picture
(
    id           serial primary key,
    good_id      int          not null references second_hand_good (id) on delete cascade,
    picture_path varchar(255) not null,
    created_time timestamp    not null default now()
);
create index if not exists good_id_of_picture on good_picture (good_id);

create table if not exists collect_relation
(
    good_id      int       not null references second_hand_good (id) on delete cascade,
    collector_id int       not null references store_user (id) on delete cascade,
    create_time  timestamp not null default now()
);
create index if not exists collector_id_index on collect_relation (collector_id);

create or replace function buyer_ack(order_id int, d_code char(6), r_code char(6), pwd char(4))
    returns bool as
$$
declare
    v_balance  numeric(12, 2);
    v_price    numeric(12, 2);
    v_buyer_id int;
    v_good_id  int;
begin
    select actual_price, buyer_id into v_price, v_buyer_id from second_hand_order where id = order_id;
    select account_balance into v_balance from store_user where id = v_buyer_id for update;

    if v_price > v_balance then
        return false;
    end if;

    update second_hand_order
    set deal_code      = d_code,
        refund_code    = r_code,
        trade_password = pwd,
        order_status   = 2,
        updated_time   = now()
    where id = order_id;

    update store_user set account_balance = v_balance - v_price where id = v_buyer_id;

    select good_id into v_good_id from second_hand_order where id = order_id;
    update second_hand_good set sold = true, updated_time = now() where id = v_good_id;
    update second_hand_order
    set order_status = 5,
        updated_time = now()
    where good_id = v_good_id
      and not id = order_id;

    return true;
end
$$ language plpgsql;

create or replace function order_confirm(order_id int)
    returns int as
$$
declare
    v_balance   numeric(12, 2);
    v_price     numeric(12, 2);
    v_seller_id int;
begin
    select sho.actual_price, shg.publisher
    into v_price, v_seller_id
    from second_hand_order sho
             join second_hand_good shg on shg.id = sho.good_id
    where sho.id = order_id;
    select account_balance into v_balance from store_user where id = v_seller_id for update;

    update second_hand_order
    set order_status = 3,
        updated_time = now()
    where id = order_id;

    update store_user set account_balance = v_balance - v_price where id = v_seller_id;
    return 1;
end
$$ language plpgsql;

create or replace function order_refund(order_id int)
    returns int as
$$
declare
    v_balance  numeric(12, 2);
    v_price    numeric(12, 2);
    v_buyer_id int;
begin
    select actual_price, buyer_id into v_price from second_hand_order where id = order_id;
    select account_balance into v_balance from store_user where id = v_buyer_id for update;

    update second_hand_order
    set order_status = 4,
        updated_time = now()
    where id = order_id;

    update store_user set account_balance = v_balance - v_price where id = v_buyer_id;
    return 1;
end
$$ language plpgsql;


drop table if exists deleted_back_user;
drop table if exists back_user;
drop table if exists back_user_role;
drop sequence if exists back_user_id;

create sequence if not exists back_user_id start with 2 increment by 2;
create table if not exists back_user_role
(
    id          serial primary key,
    role_name   varchar(10) not null,
    description varchar(255)
);

insert into back_user_role (id, role_name, description)
values (default, 'Checker', 'Only have read privilege to the statistics.'),
       (default, 'Admin', 'Have full access to the statistics.'),
       (default, 'Super', 'Have full access and can control background users');

create table if not exists back_user
(
    id           int primary key      default nextval('back_user_id'),
    username     varchar(20) not null unique,
    password     char(32)    not null,
    salt         char(16)    not null,
    role_id      int         not null references back_user_role (id) on delete cascade,
    banned       bool        not null default false,
    created_time timestamp   not null default now(),
    updated_time timestamp   not null default now()
);

insert into back_user (id, username, password, salt, role_id, banned, created_time, updated_time)
values (default, 'admin', 'a3b1059374faee992ed4a1999cec59f5', '6bb1b3e788ec0212', 3, default, default, default);

create table if not exists deleted_back_user
(
    id           int,
    username     varchar(20),
    password     char(32),
    salt         char(16),
    role_id      int,
    banned       bool,
    created_time timestamp,
    updated_time timestamp,
    deleted_time timestamp,
    deleted_by   int
);

create or replace function delete_store_user(deleted_user_id int, delete_user_id int)
    returns void as
$$
begin
    insert into deleted_back_user (id, username, password, salt, role_id, banned, created_time, updated_time,
                                   deleted_time, deleted_by)
    select id,
           username,
           password,
           salt,
           role_id,
           banned,
           created_time,
           updated_time,
           now(),
           delete_user_id
    from back_user
    where id = deleted_user_id;
    delete from back_user where id = deleted_user_id;
end;
$$
    language plpgsql;