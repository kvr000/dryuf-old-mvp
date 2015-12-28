/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.mvp.proc.test;

import net.dryuf.tenv.AppTenvObject;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dryuf.tenv.TestChild;
import net.dryuf.tenv.TestMain;
import net.dryuf.oper.MetaExport;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class MetaExportTest extends AppTenvObject
{
	@Test
	public void			testExportTestMain() throws Exception
	{
		String meta = MetaExport.buildMeta(createCallerContext(), TestMain.class, null, "/_oper/");
		XMLAssert.assertXMLEqual(
				"<?xml version='1.0' encoding='UTF-8' ?>\n"+
				"<meta xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://dryuf.org/schema/net/dryuf/app/meta/\" xsi:schemaLocation=\"http://dryuf.org/schema/net/dryuf/app/meta/ http://www.znj.cz/schema/net/dryuf/app/meta.xsd\" name=\"net.dryuf.tenv.TestMain\" rpc=\"/_oper/\" >\n"+
				"<req roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" roleDel=\"guest\" />\n"+
				"<pkeyDef pkEmbedded=\"0\" pkClass=\"java.lang.Long\" pkField=\"testId\" additionalPkFields=\"\">\n"+
				"</pkeyDef>\n"+
				"<refFields fields=\"testId,name,svalue,ivalue\" />\n"+
				"<relations>\n"+
				"</relations>\n"+
				"<fields name=\"net.dryuf.tenv.TestMain\">\n"+
				"<field name=\"testId\" assocType=\"none\" roleGet=\"guest\" roleSet=\"_denied_\" roleNew=\"guest\" mandatory=\"1\" display=\"text(160px)\" textual=\"net.dryuf.textual.NaturalLongTextual\"></field>\n"+
				"<field name=\"name\" assocType=\"none\" roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" mandatory=\"1\" display=\"text(160px)\" textual=\"net.dryuf.textual.TrimTextual\"></field>\n"+
				"<field name=\"svalue\" assocType=\"none\" roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" mandatory=\"1\" display=\"text(160px)\" textual=\"net.dryuf.textual.TrimTextual\"></field>\n"+
				"<field name=\"ivalue\" assocType=\"none\" roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" mandatory=\"0\" display=\"text(160px)\" textual=\"net.dryuf.textual.NaturalTextual\"></field>\n"+
				"</fields>\n"+
				"<actions>\n"+
				"</actions>\n"+
				"<view name=\"Default\" supplier=\"\" renderer=\"\" clientClass=\"\" fields=\"testId,name,svalue,ivalue\" actions=\"\"/>\n"+
				"</meta>\n"+
				"",
				meta);
	}

	@Test
	public void			testExportTestChild() throws Exception
	{
		String meta = MetaExport.buildMeta(createCallerContext(), TestChild.class, null, "/_oper/");
		XMLAssert.assertXMLEqual(
				"<?xml version='1.0' encoding='UTF-8' ?>\n"+
				"<meta xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://dryuf.org/schema/net/dryuf/app/meta/\" xsi:schemaLocation=\"http://dryuf.org/schema/net/dryuf/app/meta/ http://www.znj.cz/schema/net/dryuf/app/meta.xsd\" name=\"net.dryuf.tenv.TestChild\" rpc=\"/_oper/\" >\n"+
				"<req roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" roleDel=\"guest\" />\n"+
				"<pkeyDef pkEmbedded=\"1\" pkClass=\"net.dryuf.tenv.TestChild.Pk\" pkField=\"pk\" additionalPkFields=\"childId\" composClass=\"net.dryuf.tenv.TestMain\" composPkClass=\"java.lang.Long\" composPath=\"pk.testId\">\n"+
				"</pkeyDef>\n"+
				"<refFields fields=\"pk,svalue\" />\n"+
				"<relations>\n"+
				"</relations>\n"+
				"<fields name=\"net.dryuf.tenv.TestChild\">\n"+
				"<field name=\"pk\" assocType=\"none\" roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" embedded=\"net.dryuf.tenv.TestChild.Pk\">\n"+
				"<fields name=\"net.dryuf.tenv.TestChild.Pk\">\n"+
				"<field name=\"testId\" assocType=\"compos\" roleGet=\"guest\" roleSet=\"_denied_\" roleNew=\"_denied_\" ref=\"net.dryuf.tenv.TestMain\"></field>\n"+
				"<field name=\"childId\" assocType=\"none\" roleGet=\"guest\" roleSet=\"_denied_\" roleNew=\"guest\" mandatory=\"1\" display=\"text(160px)\" textual=\"net.dryuf.textual.NaturalTextual\"></field>\n"+
				"</fields>\n"+
				"</field>\n"+
				"<field name=\"svalue\" assocType=\"none\" roleGet=\"guest\" roleSet=\"guest\" roleNew=\"guest\" mandatory=\"0\" display=\"text(160px)\" textual=\"net.dryuf.textual.TrimTextual\"></field>\n"+
				"</fields>\n"+
				"<actions>\n"+
				"</actions>\n"+
				"<view name=\"Default\" supplier=\"\" renderer=\"\" clientClass=\"\" fields=\"childId,pk,svalue\" actions=\"\"/>\n"+
				"</meta>\n"+
				"",
				meta);
	}
}
