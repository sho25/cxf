begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|PostMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebLink
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|servletunit
operator|.
name|ServletUnitClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|DOMUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ExternalServicesServletTest
extends|extends
name|AbstractServletTest
block|{
specifier|static
specifier|final
name|String
name|FORCED_BASE_ADDRESS
init|=
literal|"http://localhost/somewhere"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getConfiguration
parameter_list|()
block|{
return|return
literal|"/org/apache/cxf/systest/servlet/web-external.xml"
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetServiceList
parameter_list|()
throws|throws
name|Exception
block|{
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|//test the '/' context get service list
name|WebResponse
name|res
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/"
argument_list|)
decl_stmt|;
name|WebLink
index|[]
name|links
init|=
name|res
operator|.
name|getLinks
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should get two links for the services"
argument_list|,
literal|2
argument_list|,
name|links
operator|.
name|length
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|links2
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WebLink
name|l
range|:
name|links
control|)
block|{
name|links2
operator|.
name|add
argument_list|(
name|l
operator|.
name|getURLString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|links2
operator|.
name|contains
argument_list|(
name|FORCED_BASE_ADDRESS
operator|+
literal|"/greeter?wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|links2
operator|.
name|contains
argument_list|(
name|FORCED_BASE_ADDRESS
operator|+
literal|"/greeter2?wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/html"
argument_list|,
name|res
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
comment|//HTTPUnit do not support require url with ""
comment|/*         res = client.getResponse(CONTEXT_URL);         links = res.getLinks();         assertEquals("There should get two links for the services", 1, links.length);         assertEquals(CONTEXT_URL + "/greeter?wsdl", links[0].getURLString());         assertEquals(CONTEXT_URL + "/greeter2?wsdl", links[1].getURLString());          assertEquals("text/html", res.getContentType());*/
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostInvokeServices
parameter_list|()
throws|throws
name|Exception
block|{
name|newClient
argument_list|()
expr_stmt|;
name|WebRequest
name|req
init|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/greeter"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"GreeterMessage.xml"
argument_list|)
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|newClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|response
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|response
operator|.
name|getCharacterSet
argument_list|()
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|response
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"h"
argument_list|,
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//h:sayHiResponse"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

