insert into genres(name) values('cartoon') ON CONFLICT DO NOTHING;
insert into genres(name) values('film') ON CONFLICT DO NOTHING;