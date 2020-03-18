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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
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
name|jaxrs
operator|.
name|model
operator|.
name|AbstractResourceInfo
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSServicesListingTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerServiceListing
operator|.
name|PORT
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerServiceListing
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceListing
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services"
decl_stmt|;
name|String
name|expectedResult
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services/resources"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|String
name|result
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|contains
argument_list|(
name|expectedResult
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceListingUnformatted
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services?formatted=false"
decl_stmt|;
name|String
name|expectedResult
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services/resources"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|String
name|result
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|contains
argument_list|(
name|expectedResult
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEscapeHTML
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services/<script>alert(1)</script>/../../"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|String
name|result
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"<script>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEscapeHTMLUnformatted
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services/<script>alert(1)</script>/../../?formatted=false"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|String
name|result
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"<script>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceListingPrivate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services"
decl_stmt|;
name|String
name|expectedResult
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service_listing/services/resourcesprivate"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|String
name|result
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|contains
argument_list|(
name|expectedResult
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

