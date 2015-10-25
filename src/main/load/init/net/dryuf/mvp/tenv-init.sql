DELETE FROM WebMenuItem WHERE providerName = 'dryuf';
DELETE FROM WebAccessiblePage WHERE providerName = 'dryuf';
DELETE FROM WebLanguage WHERE providerName = 'dryuf';
DELETE FROM WebProvider WHERE providerName = 'dryuf';


INSERT INTO WebProvider VALUES ('dryuf');
INSERT INTO WebLanguage VALUES ('dryuf', 'cs');


INSERT INTO WebAccessiblePage VALUES ('dryuf', 'index', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'about', 1, 'guest', 'net.dryuf.mvp.JspPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'notfound', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'login', 1, 'guest', 'net.dryuf.mvp.CommonLoginPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'logged', 1, 'guest', 'net.dryuf.mvp.LoggedPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'logout', 1, 'guest', 'net.dryuf.mvp.LogoutPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'register', 1, 'guest', 'net.dryuf.mvp.RegisterPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'registerok', 1, 'guest', 'net.dryuf.mvp.RegisterOkPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'loginforgot', 1, 'guest', 'net.dryuf.security.ForgotPasswdPresenter', 0);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'loginforgotok', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);

INSERT INTO WebMenuItem VALUES ('dryuf', 'login', null, 0);

INSERT INTO WebAccessiblePage VALUES ('dryuf', 'main1', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebMenuItem VALUES ('dryuf', 'main1', null, 10);

INSERT INTO WebAccessiblePage VALUES ('dryuf', 'main1_sub1', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebMenuItem VALUES ('dryuf', 'main1_sub1', 'main1', 10);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'main1_sub2', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebMenuItem VALUES ('dryuf', 'main1_sub2', 'main1', 20);

INSERT INTO WebAccessiblePage VALUES ('dryuf', 'main2', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebMenuItem VALUES ('dryuf', 'main2', null, 20);

INSERT INTO WebAccessiblePage VALUES ('dryuf', 'main2_sub1', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebMenuItem VALUES ('dryuf', 'main2_sub1', 'main2', 10);
INSERT INTO WebAccessiblePage VALUES ('dryuf', 'main2_sub2', 1, 'guest', 'net.dryuf.mvp.StaticPagePresenter', 0);
INSERT INTO WebMenuItem VALUES ('dryuf', 'main2_sub2', 'main2', 20);
