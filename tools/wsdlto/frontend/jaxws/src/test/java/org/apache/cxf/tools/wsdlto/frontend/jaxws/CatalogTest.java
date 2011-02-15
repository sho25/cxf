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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|catalog
operator|.
name|OASISCatalogManager
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
name|resource
operator|.
name|ExtendedURIResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|CatalogTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|OASISCatalogManager
name|catalogManager
init|=
operator|new
name|OASISCatalogManager
argument_list|()
decl_stmt|;
name|URL
name|jaxwscatalog
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/META-INF/jax-ws-catalog.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jaxwscatalog
argument_list|)
expr_stmt|;
name|catalogManager
operator|.
name|loadCatalog
argument_list|(
name|jaxwscatalog
argument_list|)
expr_stmt|;
name|String
name|xsd
init|=
literal|"http://www.w3.org/2005/08/addressing/ws-addr.xsd"
decl_stmt|;
name|String
name|resolvedSchemaLocation
init|=
name|catalogManager
operator|.
name|resolveSystem
argument_list|(
name|xsd
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"classpath:/schemas/wsdl/ws-addr.xsd"
argument_list|,
name|resolvedSchemaLocation
argument_list|)
expr_stmt|;
name|ExtendedURIResolver
name|resolver
init|=
operator|new
name|ExtendedURIResolver
argument_list|()
decl_stmt|;
name|InputSource
name|in
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|resolvedSchemaLocation
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|getSystemId
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"api"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|getSystemId
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"/schemas/wsdl/ws-addr.xsd"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

