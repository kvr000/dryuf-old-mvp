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

package net.dryuf.mvp.test;

import static org.junit.Assert.*;

import java.util.Map;

import net.dryuf.tenv.AppTenvObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.nio.charset.StandardCharsets;

import net.dryuf.core.Options;
import net.dryuf.mvp.TextRootPresenter;
import net.dryuf.srvui.DummyRequest;
import net.dryuf.mvp.RootPresenter;
import net.dryuf.mvp.StaticPagePresenter;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class JacksonCharsetTest extends AppTenvObject
{
	protected StaticPagePresenter	testPresenter;
	protected DummyRequest		testRequest;

	public RootPresenter		createPresenter(String path)
	{
		testRequest = new DummyRequest();
		testRequest.setPath(path);
		testPresenter = new StaticPagePresenter(
					new TextRootPresenter(createCallerContext(), testRequest),
					Options.buildListed("page", "net/dryuf/web/testpage")
				);
		return testPresenter.getRootPresenter();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void			testRoot() throws Exception
	{
		Map<String, Object> m;
		m = new ObjectMapper().readValue("{ \"x\": \"čšří\" }".getBytes(StandardCharsets.UTF_8), Map.class);
		assertEquals("čšří", m.get("x"));
	}
}
