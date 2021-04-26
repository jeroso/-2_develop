INSERT INTO users(seq,email,passwd,name) VALUES (null,'test00@gmail.com','$2a$10$mzF7/rMylsnxxwNcTsJTEOFhh1iaHv3xVox.vpf6JQybEhE4jDZI.','song');
INSERT INTO users(seq,email,passwd,name) VALUES (null,'test01@gmail.com','$2a$10$Mu/akK4gI.2RHm7BQo/kAO1cng2TUgxpoP.zBbPOeccVGP4lKVGYy','ko');
INSERT INTO users(seq,email,passwd,name) VALUES (null,'test02@gmail.com','$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG','kim');

INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,1,'test01 first post',1,1,'2019-03-01 13:10:00');
INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,1,'test01 second post',0,0,'2019-03-12 09:45:00');
INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,1,'test01 third post',0,0,'2019-03-20 19:05:00');
INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,2,'test02 post',0,0,'2019-03-20 15:13:20');

INSERT INTO comments(seq,user_seq,post_seq,contents,create_at) VALUES (null,1,1,'first comment','2019-03-01 13:15:00');
INSERT INTO comments(seq,user_seq,post_seq,contents,create_at) VALUES (null,2,4,'first comment','2019-03-01 13:15:00');

INSERT INTO connections(seq,user_seq,target_seq,granted_at,create_at) VALUES (null,1,2,'2019-03-31 13:00:00','2019-03-31 00:10:00');