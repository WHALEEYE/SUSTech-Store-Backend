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
    deleted_time               timestamp,
    deleted_by                 int
);

create or replace function delete_store_user(deleted_user_id int, delete_user_id int)
    returns void as
$$
begin
    insert into deleted_store_user (id, phone_number, nickname, card_number, password, salt, introduction, avatar_path,
                                    sex, alipay_account, credit_score, total_sold_value, second_hand_notification,
                                    agent_service_notification, api_trade_notification, banned, created_time,
                                    updated_time, deleted_time, deleted_by)
    select id,
           phone_number,
           nickname,
           card_number,
           password,
           salt,
           introduction,
           avatar_path,
           sex,
           alipay_account,
           credit_score,
           total_sold_value,
           second_hand_notification,
           agent_service_notification,
           api_trade_notification,
           banned,
           created_time,
           updated_time,
           now(),
           delete_user_id
    from store_user
    where id = deleted_user_id;
    delete from store_user where id = deleted_user_id;
end;
$$
    language plpgsql;

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
    sort_no      int unique         not null,
    created_time timestamp          not null default now()
);

insert into good_type (id, type_name, sort_no)
values (default, 'Digital Products', 1),
       (default, 'Groceries', 2),
       (default, 'Clothes', 3),
       (default, 'Makeups', 4),
       (default, 'Electrical Goods', 5),
       (default, 'Books', 6),
       (default, 'Entertainment', 7);

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
    returns void as
$$
begin
    delete from second_hand_good where id = deleted_good_id;
    update deleted_second_hand_good set deleted_by = delete_user_id where id = deleted_good_id;
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
    good_id      int          not null references second_hand_good (id) on delete cascade,
    picture_path varchar(255) not null,
    sort_no      int          not null,
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