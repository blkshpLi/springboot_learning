ALTER TABLE comment_like
ADD FOREIGN KEY (comment_id) REFERENCES comment(id) ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE notification
ADD FOREIGN KEY (outer_id) REFERENCES question(id) ON DELETE CASCADE ON UPDATE CASCADE ;