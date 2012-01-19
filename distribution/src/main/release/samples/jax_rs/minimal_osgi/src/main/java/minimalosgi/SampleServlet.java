begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|minimalosgi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|JAXRSServerFactoryBean
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
name|servlet
operator|.
name|CXFNonSpringJaxrsServlet
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
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_class
specifier|public
class|class
name|SampleServlet
extends|extends
name|CXFNonSpringJaxrsServlet
block|{
specifier|protected
name|void
name|createServerFromApplication
parameter_list|(
name|String
name|cName
parameter_list|,
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// technically, you should look up the application name from ServletConfig's init parameters
comment|// but creating the actual application object is slower via reflection than actually
comment|// instantiating it
name|SampleApplication
name|app
init|=
operator|new
name|SampleApplication
argument_list|()
decl_stmt|;
name|JAXRSServerFactoryBean
name|bean
init|=
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
name|app
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|bean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

